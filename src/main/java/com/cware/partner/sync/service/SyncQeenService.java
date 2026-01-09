package com.cware.partner.sync.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cware.partner.common.code.Application;
import com.cware.partner.common.code.CdcReason;
import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.code.PaSaleGb;
import com.cware.partner.common.code.PaStatus;
import com.cware.partner.common.code.SaleGb;
import com.cware.partner.common.util.StringUtil;
import com.cware.partner.sync.domain.QeenGoodsOffer;
import com.cware.partner.sync.domain.QeenIslandJejuReturnCost;
import com.cware.partner.sync.domain.SyncResult;
import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.GoodsDt;
import com.cware.partner.sync.domain.entity.PaCustShipCost;
import com.cware.partner.sync.domain.entity.PaGoodsDt;
import com.cware.partner.sync.domain.entity.PaGoodsOffer;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.domain.entity.PaQeenGoods;
import com.cware.partner.sync.domain.entity.PaQeenGoodsDtMapping;
import com.cware.partner.sync.domain.entity.PaQeenGoodsKindsEnc;
import com.cware.partner.sync.domain.entity.PaQeenGoodsKindsOffer;
import com.cware.partner.sync.domain.entity.PaQeenShipCost;
import com.cware.partner.sync.domain.entity.PaSaleNoGoods;
import com.cware.partner.sync.domain.id.PaGoodsId;
import com.cware.partner.sync.domain.id.PaQeenShipCostId;
import com.cware.partner.sync.repository.PaBrandMappingRepository;
import com.cware.partner.sync.repository.PaQeenGoodsDtMappingRepository;
import com.cware.partner.sync.repository.PaQeenGoodsKindsEncRepository;
import com.cware.partner.sync.repository.PaQeenGoodsKindsOfferRepository;
import com.cware.partner.sync.repository.PaQeenGoodsRepository;
import com.cware.partner.sync.repository.PaQeenShipCostRepository;
import com.cware.partner.sync.repository.PaSaleNoGoodsRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SyncQeenService extends PartnerSyncService {

	@Autowired 
	private PaQeenGoodsRepository paQeenGoodsRepository;

	@Autowired
	private PaSaleNoGoodsRepository saleNoGoodsRepository;
	
	@Autowired
	private PaQeenShipCostRepository paQeenShipCostRepository;
	
	@Autowired
	private PaQeenGoodsDtMappingRepository goodsDtRepository;
	
    @Autowired
    private PaBrandMappingRepository brandMappingRepository;
    
    @Autowired
    private PaQeenGoodsKindsEncRepository paQeenGoodsKindsEncRepository;
    
    @Autowired
    private PaQeenGoodsKindsOfferRepository paQeenGoodsKindsOfferRepository;
    
    @Async("partnerAsyncExecutor")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public CompletableFuture<SyncResult> asyncService(Goods goods, PaGoodsTarget target) {
		boolean result = syncProduct(goods, target);
		SyncResult sync = SyncResult.builder().isSync(result).target(target).build();
		return CompletableFuture.completedFuture(sync);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean syncProduct(Goods goods, PaGoodsTarget target) {
		tag = "퀸잇 동기화";

		target.setMediaCode(PaGroup.QEEN.mediaCode());

		log.info("{} -------> {}", tag, target);

		Optional<PaQeenGoods> optional = paQeenGoodsRepository
				.findById(new PaGoodsId(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()));
		PaQeenGoods partnerGoods;
		Timestamp procDate = commonService.currentDate();
		
		// 재입점, 동기화
		if (optional.isPresent()) {
			partnerGoods = optional.get();
			target.setPartnerGoods(partnerGoods);

			// 마지막 동기화일시 마이그레이션
			if (partnerGoods.getLastSyncDate() == null) {
				partnerGoods.setLastSyncDate(partnerGoods.getModifyDate().after(goods.getPaGoods().getLastSyncDate())
						? goods.getPaGoods().getLastSyncDate()
						: partnerGoods.getModifyDate());
			}

			boolean isTransTarget = false;
			boolean result;
			// 재입점 처리
//			isTransTarget = syncForSale(target);

			// 카테고리 동기화
			/* 카테고리 변경 되어도 무시하기로 협의 >> 해당 내용 코드 주석 처리 2025-01-14
			result = syncGoodsKindsModify(goods.getLmsdCode(), target);
			isTransTarget = isTransTarget || result;
			*/
			
			// 정보고시 동기화(카테고리 동기화 이후 처리해야 됨)
			result = syncGoodsOfferModify(goods.getOfferType(), target);
			isTransTarget = isTransTarget || result;

			// 상품정보변경 동기화
			result = syncPaGoods(goods, target);
			isTransTarget = isTransTarget || result;

			// 단품정보변경 동기화
			result = syncGoodsDt(goods.getGoodsDtList(), target);
			isTransTarget = isTransTarget || result;

			// 이미지 동기화
			result = syncGoodsImage(goods.getGoodsImage(), target);
			isTransTarget = isTransTarget || result;
			
			// 가격변경 동기화
			result = syncGoodsPriceMoidfy(goods.getGoodsPrice(),goods.getLmsdCode(), target);
			isTransTarget = isTransTarget || result;

			// 프로모션변경 동기화
			result = syncPromoModifyTarget(goods, target);
			isTransTarget = isTransTarget || result;

			// 공지사항 동기화
			result = syncPaNotice(goods, target);
			isTransTarget = isTransTarget || result;
			
			// 배송비 동기화
			result = syncShipCost(goods, target);
			isTransTarget = isTransTarget || result;
			
			if (isTransTarget) {
				partnerGoods.setTransTargetYn("1");
				partnerGoods.setLastSyncDate(procDate);
				partnerGoods.setModifyDate(procDate);
				partnerGoods.setModifyId(Application.ID.code());

				if (PaStatus.REJECT.code().equals(partnerGoods.getPaStatus())
						&& SaleGb.FORSALE.code().equals(goods.getSaleGb())) {
					partnerGoods.setPaSaleGb(PaSaleGb.FORSALE.code());
				}

				paQeenGoodsRepository.save(partnerGoods);
			}

			return isTransTarget;

		}

		// 신규입점
		partnerGoods = PaQeenGoods.builder().goodsCode(target.getGoodsCode())
				.paCode(target.getPaCode())
				.paGroupCode(target.getPaGroupCode())
				.paSaleGb(PaSaleGb.FORSALE.code())
				.paStatus(PaStatus.REQUEST.code())
				.goodsName(goods.getGoodsName().replaceAll("[^a-zA-Z0-9가-힣()\\[\\]/\\-_+~&%,.]", ""))
				.transTargetYn("1")
				.transSaleYn("0")
				.transOrderAbleQty(target.getPartnerStockQty())
				.lastSyncDate(procDate)
				.insertId(Application.ID.code())
				.insertDate(procDate)
				.modifyId(Application.ID.code())
				.modifyDate(procDate)
				.build();

		target.setPartnerGoods(partnerGoods);

		// 표준카테고리매핑
		partnerGoods.setPaLmsdKey(getLmsdKey(goods.getLmsdCode(), target));
		if (partnerGoods.getPaLmsdKey() == null) return false;

        // 브랜드매핑
        String paBrandCode = brandMappingRepository.findQeenMapping(partnerGoods.getPaCode(), goods.getBrandCode());
       
        partnerGoods.setPaBrandCode(paBrandCode);
		
		// 정보고시 동기화(카테고리 동기화 이후 처리해야 됨)
		if (!syncGoodsOffer(goods.getOfferType(), target)) return false;

		// 상품 기본 동기화
		paQeenGoodsRepository.save(partnerGoods);

		// 가격 동기화
		syncGoodsPrice(goods.getGoodsPrice(),goods.getLmsdCode(), target);

		// 프로모션 동기화
		syncPromoTarget(goods, target);

		// 이미지 동기화
		syncGoodsImage(goods.getGoodsImage(), target);

		// 단품 동기화
		syncGoodsDt(goods.getGoodsDtList(), target);

		// 공지사항 동기화
		syncPaNotice(goods, target);

		// 배송비 동기화
		syncShipCost(goods, target);
 		
		// 제휴입점요청처리
		return requestSyncPartner(target);

	}
	
	// 정보고시동기화
	protected boolean syncGoodsOfferModify(String offerType, PaGoodsTarget target) {
		String syncNote = "정보고시변경 동기화";

		boolean isDirty;

		Timestamp currentDate = commonService.currentDate();

		String paGroupCode = target.getPaGroupCode();
		
		/* 카테고리 변경 되어도 무시하기로 협의 >> 해당 내용 코드 주석 처리 2025-01-14
		PaQeenGoods partnerGoods = (PaQeenGoods) target.getPartnerGoods();
		
		// 퀸잇 카테고리 별 필수 정보고시 유형 정보 
		PaQeenGoodsKindsOffer qeenKindsOffer = paQeenGoodsKindsOfferRepository.getByCategoryId(partnerGoods.getPaLmsdKey());
		
		if(qeenKindsOffer==null) {
			target.setExcept(true);
			target.setExceptNote("제휴사 카레고리 별 정보고시 유형과 매핑된 데이터가 없습니다.");
			log.info("{}: {} [상품:{} 제휴사:{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
			logFilter("SYNC-GOODS_OFFER_MODIFY", target);
			return false;
		}*/
		

		// 이전 동기화된 offerType
		// 퀸잇의 경우 tpaoffercodemapping과 tpaoffercode 1대1이 아니기 때문에 goodsOfferRepository.getOfferType 사용 할수없음
		// 최초 정보고시 등록 시 remark1에 offerCode 저장해서 비교하도록 하였음
		String syncOfferType = goodsOfferRepository.getRemark1(paGroupCode, target.getGoodsCode()); // 퀸잇 전용
		
		if (StringUtil.compare(offerType, syncOfferType)) {
			
			List<PaGoodsOffer> list = goodsOfferRepository.selectGoodsOfferModify(paGroupCode, target.getGoodsCode());
			
			/*카테고리 변경 되어도 무시하기로 협의 >> 해당 내용 코드 주석 처리 2025-01-14
			if(!list.isEmpty() && !list.get(0).getPaOfferType().equals(qeenKindsOffer.getAnnouncementType())) {
				target.setExcept(true);
				target.setExceptNote("제휴사 카테고리 별 필수 정보고시 유형과 일치하지 않습니다");
				log.info("{}: {} [상품:{} 제휴사:{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
				logFilter("SYNC-GOODS_OFFER_MODIFY", target);
				return false;
			} */

			for (PaGoodsOffer offer : list) {
				
				if("FURNITURE".equals(offer.getPaOfferType())) {
					// 우리 정보고시 '침구류' 일때  제휴사 정보고시 KC 인증번호/ 배송, 설치비용/ 재공급(리퍼브) 관한 내용 >  - 로 대체
					if("05".equals(offerType)) {
						if(offer.getPaOfferCode().contains("KC")
								|| offer.getPaOfferCode().contains("재공급")
						  ) {
							offer.setPaOfferExt("-");
						}
					}
				}
				
				if("APPAREL".equals(offer.getPaOfferType())) {
					// 우리 정보고시 '귀금속/보석/시계류' 일때 제휴사 정보고시 색상/ 세탁방법 및 취급/ 제조일자에 관한 내용   >  - 로 대체
					if("19".equals(offerType)) {
						if(offer.getPaOfferCode().contains("색상")
								|| offer.getPaOfferCode().contains("세탁방법")
								|| offer.getPaOfferCode().contains("제조일자")
								) {
							offer.setPaOfferExt("-");
						}
					}
				}
				offer.setRemark1(offerType);
				offer.setTransTargetYn("1");
				offer.setLastSyncDate(currentDate);
				offer.setModifyId(Application.ID.code());
				offer.setModifyDate(currentDate);
				goodsOfferRepository.saveAndFlush(offer);
			}

			isDirty = list.size() > 0;

			list = goodsOfferRepository.selectGoodsOfferNew(paGroupCode, target.getGoodsCode());

			for (PaGoodsOffer offer : list) {             
				
				if("FURNITURE".equals(offer.getPaOfferType())) {
					// 우리 정보고시 '침구류' 일때  제휴사 정보고시 KC 인증번호/ 배송, 설치비용/ 재공급(리퍼브) 관한 내용 >  -로 대체
					if("05".equals(offerType)) {
						if(offer.getPaOfferCode().contains("KC")
								|| offer.getPaOfferCode().contains("재공급")
						  ) {
							offer.setPaOfferExt("-");
						}
					}
				}
				
				if("APPAREL".equals(offer.getPaOfferType())) {
					// 우리 정보고시 '귀금속/보석/시계류' 일때 제휴사 정보고시 색상/ 세탁방법 및 취급/ 제조일자에 관한 내용   >  -로 대체
					if("19".equals(offerType)) {
						if(offer.getPaOfferCode().contains("색상")
								|| offer.getPaOfferCode().contains("세탁방법")
								|| offer.getPaOfferCode().contains("제조일자")
								) {
							offer.setPaOfferExt("-");
						}
					}
				}
				
				offer.setRemark1(offerType);
				offer.setGoodsCode(target.getGoodsCode());
				offer.setTransTargetYn("1");
				offer.setLastSyncDate(currentDate);
				offer.setInsertId(Application.ID.code());
				offer.setInsertDate(currentDate);
				offer.setModifyId(Application.ID.code());
				offer.setModifyDate(currentDate);
				goodsOfferRepository.save(offer);
			}

			isDirty = isDirty || (list.size() > 0);
		} else {
			// 이전 정보고시유형과 달라졌을 경우 기존 정보고시 비활성화 후 카테고리 매핑하여 새로 생성
			goodsOfferRepository.disableGooodsOffer(paGroupCode, target.getGoodsCode(), Application.ID.code());
			syncGoodsOffer(offerType, target);
			isDirty = true;
		}

		if (isDirty) {
			logSync(CdcReason.OFFER_MODIFY.code(), syncNote, target);
			return true;
		}
		return false;
	}
	
	
	
	// 정보고시동기화
	protected boolean syncGoodsOffer(String offerType, PaGoodsTarget target) {
		String tag = "정보고시 동기화";
		
		PaQeenGoods partnerGoods = (PaQeenGoods) target.getPartnerGoods();
		
		// 퀸잇 카테고리 별 필수 정보고시 유형 정보 
		PaQeenGoodsKindsOffer qeenKindsOffer = paQeenGoodsKindsOfferRepository.getByCategoryId(partnerGoods.getPaLmsdKey());
		
		if(qeenKindsOffer==null) {
			target.setExcept(true);
			target.setExceptNote("제휴사 카레고리 별 정보고시 유형과 매핑된 데이터가 없습니다.");
			log.info("{}: {} [상품:{} 제휴사:{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
			logFilter("SYNC-GOODS_OFFER", target);
			return false;
		}
		
		String paGroupCode = target.getPaGroupCode();
		Timestamp currentDate = commonService.currentDate();

		List<QeenGoodsOffer> qeenOfferlist = goodsOfferRepository.selectQeenGoodsOffer(paGroupCode, target.getGoodsCode(), offerType);
		
		if (qeenOfferlist.isEmpty()) {
			target.setExcept(true);
			target.setExceptNote("제휴사 정보고시 매핑 데이터가 없습니다.");
			log.info("{}: {} [상품:{} 제휴사:{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
			logFilter("SYNC-GOODS_OFFER", target);
			return false;
		}
		
		if(!qeenOfferlist.get(0).getPaOfferType().equals(qeenKindsOffer.getAnnouncementType())) {
			target.setExcept(true);
			target.setExceptNote("제휴사 카테고리 별 필수 정보고시 유형과 일치하지 않습니다");
			log.info("{}: {} [상품:{} 제휴사:{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
			logFilter("SYNC-GOODS_OFFER", target);
			return false;
		}
		
		List<PaGoodsOffer> list  = new ArrayList<PaGoodsOffer>();

		for(QeenGoodsOffer qeenOffer : qeenOfferlist) {
			PaGoodsOffer offer = new PaGoodsOffer();
			
			offer.setPaGroupCode(paGroupCode);
			offer.setPaOfferType(qeenOffer.getPaOfferType());
			offer.setPaOfferCode(qeenOffer.getPaOfferCode());
			
			boolean chkOffer = false;
		
			if("FURNITURE".equals(qeenOffer.getPaOfferType())) {
				// 우리 정보고시 '침구류' 일때  제휴사 정보고시 KC 인증번호/ 배송, 설치비용/ 재공급(리퍼브) 관한 내용 >  -로 대체
				if("05".equals(offerType)) {
					if(qeenOffer.getPaOfferCode().contains("KC")
							|| qeenOffer.getPaOfferCode().contains("재공급")
					  ) {
						chkOffer = true;
					}
				}
			}
			
			if("APPAREL".equals(qeenOffer.getPaOfferType())) {
				// 우리 정보고시 '귀금속/보석/시계류' 일때 제휴사 정보고시 색상/ 세탁방법 및 취급/ 제조일자에 관한 내용   >  -로 대체
				if("19".equals(offerType)) {
					if(qeenOffer.getPaOfferCode().contains("색상")
							|| qeenOffer.getPaOfferCode().contains("세탁방법")
							|| qeenOffer.getPaOfferCode().contains("제조일자")
							) {
						chkOffer = true;
					}
				}
			}
			
			offer.setRemark1(offerType);// 퀸잇의 경우 tpaoffercodemapping과 tpaoffercode 1대1이 아니기 때문에 정보고시 수정 동기화 시 그전 정보고시 유형 알기 위해 remark1에 저장
			offer.setPaOfferExt(chkOffer ?  "-" : qeenOffer.getPaOfferExt());
			offer.setGoodsCode(target.getGoodsCode());
			offer.setUseYn("1");
			offer.setTransTargetYn("1");
			offer.setLastSyncDate(currentDate);
			offer.setInsertId(Application.ID.code());
			offer.setInsertDate(currentDate);
			offer.setModifyId(Application.ID.code());
			offer.setModifyDate(currentDate);
			
			list.add(offer);
		}
		
		log.info("{}: [상품:{} 제휴사:{}]", tag, target.getGoodsCode(), target.getPaGroupCode());
		goodsOfferRepository.saveAll(list);
		return true;
	}
	/*카테고리 변경 되어도 무시하기로 협의 >> 해당 내용 코드 주석 처리 2025-01-14
	private boolean syncGoodsKindsModify(String lmsdCode, PaGoodsTarget target) {
		PaQeenGoods partnerGoods = (PaQeenGoods) target.getPartnerGoods();
		String paLmsdKey = "";
		
		//표준카테고리 매핑
		paLmsdKey = getLmsdKey(lmsdCode, target);
		
		if(paLmsdKey == null) {
			return false;
		}else {
			//퀸잇DEC카테고리매핑
			String paDecLmsdKey =  getQeenLmsdDec(paLmsdKey,target);
			if(paDecLmsdKey == null) {
				return false;
			}else {
				paLmsdKey = paDecLmsdKey;
			}
			
		}
		
		//표준,전시카테고리 변경 체크
		if(paLmsdKey.equals(partnerGoods.getPaLmsdKey())) {
			return false;
		}
		
		partnerGoods.setPaLmsdKey(paLmsdKey);
				
		return true;
	}*/
	
	//퀸잇DEC카테고리매핑
	protected String getQeenLmsdDec(String paLmsdKey, PaGoodsTarget target) {
		String tag = "퀸잇DEC카테고리매핑";
		
		PaQeenGoodsKindsEnc categoryInfo = paQeenGoodsKindsEncRepository.getById(paLmsdKey);
		
		try {
			return categoryInfo.getPaLmsdKeyDec();
		} catch (EntityNotFoundException e) {
			target.setExcept(true);
			target.setExceptNote("제휴사 DEC카테고리가 존재하지 않습니다.");
			log.info("{}: {} [상품:{} 제휴사:{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
			logFilter("SYNC-DEC_LMSD_CODE", target);
			return null;
		}

	}

	// 재입점
	private boolean syncForSale (PaGoodsTarget target) {

		PaQeenGoods partnerGoods = (PaQeenGoods)target.getPartnerGoods();

		// 입점완료상태이면서 판매중지 상태인 경우 재입점 처리
		if (PaStatus.COMPLETE.code().equals(partnerGoods.getPaStatus())
				&& PaSaleGb.SUSPEND.code().equals(partnerGoods.getPaSaleGb()) && "1".equals(target.getAutoYn())) {
			String syncNote = "상품 재입점";
			Timestamp currentDate = commonService.currentDate();

			partnerGoods.setTransSaleYn("1");
			partnerGoods.setPaSaleGb(PaSaleGb.FORSALE.code());
			partnerGoods.setModifyId(Application.ID.code());
			partnerGoods.setModifyDate(currentDate);
			paQeenGoodsRepository.save(partnerGoods);
			logSync(CdcReason.SALE_START.code(), syncNote, target);

			return true;
		} else if (PaStatus.REJECT.code().equals(partnerGoods.getPaStatus()) && "1".equals(target.getAutoYn())) { // 반려건 입점요청
			String syncNote = "반려상품 입점요청";
			Timestamp currentDate = commonService.currentDate();
			partnerGoods.setPaStatus(PaSaleGb.REQUEST.code());
			partnerGoods.setPaSaleGb(PaSaleGb.FORSALE.code());
			partnerGoods.setModifyId(Application.ID.code());
			partnerGoods.setModifyDate(currentDate);
			paQeenGoodsRepository.save(partnerGoods);
			logSync(CdcReason.SALE_START.code(), syncNote, target);
		} else {
			if (requestSyncPartner(target))
				return true;
		}

		return false;
	}

	// 상품정보동기화
	private boolean syncPaGoods(Goods goods, PaGoodsTarget target) {

		PaQeenGoods partnerGoods = (PaQeenGoods)target.getPartnerGoods();
		if (goods.getPaGoods().getLastSyncDate().after(partnerGoods.getLastSyncDate())) {
			//  해당 특수문자[ ()[]/-_+~&%,. ] 제외 전부 스페이스 치환 
			String goodsName = goods.getGoodsName().replaceAll("[^a-zA-Z0-9가-힣()\\[\\]/\\-_+~&%,.]", "");
			partnerGoods.setGoodsName(goodsName);
			
			String paBrandCode = brandMappingRepository.findQeenMapping(partnerGoods.getPaCode(), goods.getBrandCode());
			
			// 퀸잇 방송계정이면서, MD 분류 '뷰티' 일 경우 -> SKSTORE_B_SKSTOA_HB 브랜드 로 변경되도록 수정 2025.11.17 leejy
			if("F1".equals(partnerGoods.getPaCode()) && "0023".equals(goods.getMdKind())){
				partnerGoods.setPaBrandCode("SKSTORE_B_SKSTOA_HB"); 
			} else {
				partnerGoods.setPaBrandCode(paBrandCode);
			}
			
			String syncNote = "상품정보변경";
			log.info("{} {} 상품: {} ", tag, syncNote, goods.getGoodsCode() );
			logSync(CdcReason.GOODS_MODIFY.code(), syncNote, target);

			return true;
		} else if (goods.getPaGoods().getLastDescribeSyncDate().after(partnerGoods.getLastSyncDate())) { // 기술서 변경되면 연동타겟으로 설정
			String syncNote = "상품기술서변경";
			log.info("{} {} 상품: {} ", tag, syncNote, goods.getGoodsCode() );
			logSync(CdcReason.DESCRIBE_MODIFY.code(), syncNote, target);
			return true;
		}

		return false;
	}

	// 판매중지처리
	public int stopSale(String goodsCode) {
		return stopSale(goodsCode, "%", "%");

	}

	// 판매중지처리
	public int stopSale(String goodsCode, String paGroupCode, String paCode) {
		int result = paQeenGoodsRepository.stopSale(goodsCode, paCode, Application.ID.code());

		log.info("퀸잇 판매중지 상품: {} ({})", goodsCode, result);

		return result;
	}

	// 제휴필터에서 호출
	public boolean stopSale(PaGoodsTarget target) {
		Optional<PaQeenGoods> optional = paQeenGoodsRepository
				.findById(new PaGoodsId(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()));

		if (!optional.isPresent())
			return false;

		PaQeenGoods partnerGoods = optional.get();
		Timestamp currentDate = commonService.currentDate();

		// 입점완료이고, 판매중인 경우 판매중단연동타겟팅 및 이력생성
		if (PaStatus.COMPLETE.code().equals(partnerGoods.getPaStatus())
				&& PaSaleGb.FORSALE.code().equals(partnerGoods.getPaSaleGb())) {

			partnerGoods.setPaSaleGb(PaSaleGb.SUSPEND.code());
			partnerGoods.setTransSaleYn("1");
			partnerGoods.setModifyDate(currentDate);
			partnerGoods.setModifyId(Application.ID.code());
			partnerGoods.setReturnNote(target.getExceptNote());
			paQeenGoodsRepository.save(partnerGoods);

			PaSaleNoGoods saleNo = PaSaleNoGoods.builder()
					.paGroupCode(target.getPaGroupCode())
					.paCode(target.getPaCode())
					.goodsCode(target.getGoodsCode())
					.seqNo(saleNoGoodsRepository.getNextSeq(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()))
					.paGoodsCode(partnerGoods.getProductProposalId())
					.paSaleGb(PaSaleGb.SUSPEND.code())
					.note(target.getExceptNote())
					.insertId(Application.ID.code())
					.insertDate(currentDate)
					.build();
			saleNoGoodsRepository.save(saleNo);

			String syncNote = "제휴필터 판매중지";
			log.info("{} {} 상품: {} ", tag, syncNote, target.getGoodsCode());
			logSync(CdcReason.SALE_END.code(), syncNote, target);

			return true;
		} else if (PaStatus.COMPLETE.code().compareTo(partnerGoods.getPaStatus()) > 0
				&& PaSaleGb.FORSALE.code().equals(partnerGoods.getPaSaleGb())) {
			// 입점전 판매중 데이터가 존재하는 경우 필터메시지와 판매중단 업데이트 처리
			partnerGoods.setPaStatus(PaStatus.REJECT.code());
			partnerGoods.setPaSaleGb(PaSaleGb.SUSPEND.code());
			partnerGoods.setReturnNote(target.getExceptNote());
			partnerGoods.setModifyDate(currentDate);
			partnerGoods.setModifyId(Application.ID.code());
			paQeenGoodsRepository.save(partnerGoods);
		}

		return false;
	}

	// 전송대상설정
	public boolean enableTransTarget(String goodsCode, String paGroupCode, String paCode) {
		int result = paQeenGoodsRepository.enableTransTarget(goodsCode, paCode, Application.ID.code());

		log.info("퀸잇 전송대상 상품: {} ({})", goodsCode, paCode, result);

		return result > 0;
	}
	
	// 고객부담배송비
	protected boolean syncShipCost(Goods goods, PaGoodsTarget target) {
		String syncNote = "배송비 동기화";

		PaCustShipCost shipCost = goods.getPaCustShipCost();
		Optional<PaQeenShipCost> optional = paQeenShipCostRepository
				.findById(new PaQeenShipCostId(target.getPaCode(), shipCost.getEntpCode(), 
												goods.getDelyType().equals("10") ? "003" : goods.getShipManSeq(),
												goods.getDelyType().equals("10") ? "002" : goods.getReturnManSeq(), 
												shipCost.getShipCostCode()));

		Timestamp currentDate = commonService.currentDate();
		PaQeenShipCost qeenShipCost = new PaQeenShipCost();
		boolean isDirty = false;

		if (optional.isPresent()) { 
			
			BeanUtils.copyProperties(optional.get(), qeenShipCost);
			
			isDirty = optional.get().getLastSyncDate().after(target.getPartnerGoods().getLastSyncDate());
			if (!shipCost.getApplyDate().after(optional.get().getLastSyncDate())) {
				if (isDirty) logSync(CdcReason.SHIP_COST_APPLY.code(), syncNote, target);
				return isDirty;
			}

		} else {
			qeenShipCost.setInsertId(Application.ID.code());
			qeenShipCost.setInsertDate(currentDate);
			
			qeenShipCost.setLastEntpSyncDate(currentDate);// 최초 등록시에만 싱크업데이트, 그 이후는 EntpAddrSyncQeenService 업체 동기화로
		}
		
		qeenShipCost.setPaCode(target.getPaCode());
		qeenShipCost.setEntpCode(shipCost.getEntpCode());
		qeenShipCost.setShipManSeq(goods.getDelyType().equals("10") ? "003" : goods.getShipManSeq());
		qeenShipCost.setReturnManSeq(goods.getDelyType().equals("10") ? "002" : goods.getReturnManSeq());
		qeenShipCost.setShipCostCode(goods.getShipCostCode());
		qeenShipCost.setPaShipcostName(target.getPaCode()+"_"+shipCost.getEntpCode()+"_"+goods.getShipCostCode()+"_"+qeenShipCost.getShipManSeq()+"_"+qeenShipCost.getReturnManSeq());
		
		// 반품비를 나누기 2로 해서 주문 배송비(2500), 반품 배송비(2500) 세팅, 이렇게 하면 최초 구매시 배송료 0원, 고객귀책 반품 시 2500+2500원 
		double returnCost = shipCost.getReturnCost();
		
		qeenShipCost.setShipCostBaseAmt(shipCost.getShipCostBaseAmt());
		qeenShipCost.setOrdCost(returnCost/2);
		
		
		double islandCost = shipCost.getIslandCost() -shipCost.getOrdCost()<=0 ? 0 : shipCost.getIslandCost() -shipCost.getOrdCost();
		double jejuCost = shipCost.getJejuCost() -shipCost.getOrdCost()<=0 ? 0 : shipCost.getJejuCost() -shipCost.getOrdCost();
		
		
		qeenShipCost.setIslandCost(islandCost); 
		qeenShipCost.setJejuCost(jejuCost);
	
		qeenShipCost.setReturnCost(returnCost/2);
		
		// 제주/도서산간 반품비
		QeenIslandJejuReturnCost islandJejuShipCost = paQeenShipCostRepository.getIslandJejuReturnShipCost(shipCost.getEntpCode(), goods.getShipCostCode());
		
		double islandReturnCost = islandJejuShipCost.getIslandReturnCost()-returnCost <= 0 ? 0 : islandJejuShipCost.getIslandReturnCost()-returnCost ;
		double jejuReturnCost = islandJejuShipCost.getJejuReturnCost()-returnCost <= 0 ? 0 : islandJejuShipCost.getJejuReturnCost()-returnCost ;
		
		qeenShipCost.setIslandReturnCost(islandReturnCost);// 도서산간 추가 반품비
		qeenShipCost.setJejuReturnCost(jejuReturnCost); // 제주 추가 반품비
		
		qeenShipCost.setTransTargetYn("1");
		qeenShipCost.setLastSyncDate(currentDate);
		qeenShipCost.setModifyId(Application.ID.code());
		qeenShipCost.setModifyDate(currentDate);
		
		try {
			paQeenShipCostRepository.saveAndFlush(qeenShipCost);
		} catch (DataIntegrityViolationException ex) {
			log.info("배송비정책이 이미 생성되었습니다. 상품:{}, 제휴사:{}", target.getGoodsCode(), target.getPaCode());
		}
		logSync(CdcReason.SHIP_COST_APPLY.code(), syncNote, target);
		return true;
	}
	
	
	// 단품/재고 동기화
	protected boolean syncGoodsDt(List<GoodsDt> goodsDtList, PaGoodsTarget target) {
		String syncNote = "단품 동기화";

		Timestamp currentDate = commonService.currentDate();
		boolean isDirty = false;
		double stockRate = codeService.getStockRate(target.getPaCode());


		for (GoodsDt goodsDt : goodsDtList) {
			PaGoodsDt paGoodsDt = goodsDt.getPaGoodsDt();

			if (paGoodsDt == null)
				continue;

			Optional<PaQeenGoodsDtMapping> optional = goodsDtRepository
					.findGoodsDtMapping(target.getPaCode(), target.getGoodsCode(), goodsDt.getGoodsdtCode());

			PaQeenGoodsDtMapping goodsDtMapping;
			int transOrderAbleQty = SaleGb.FORSALE.code().equals(goodsDt.getSaleGb()) ?
					(int) Math.ceil(goodsDt.getOrderAbleQty() * stockRate) : 0;

			if (optional.isPresent()) {
				goodsDtMapping = optional.get();
				
				// 단품명이 바뀌면 기존 단품 매핑 비활성화 후 이력 생성
				if (!goodsDt.getGoodsdtInfo().equals(goodsDtMapping.getGoodsdtInfo())) {
					isDirty = true;
					goodsDtMapping.setUseYn("0");
					goodsDtMapping.setModifyDate(currentDate);
					goodsDtMapping.setModifyId(Application.ID.code());
					goodsDtRepository.save(goodsDtMapping);
					
					Optional<PaQeenGoodsDtMapping> seqOptional = goodsDtRepository.findTopByGoodsCodeAndGoodsdtCodeAndPaCodeAndGoodsdtInfoOrderByGoodsdtSeqAsc(target.getGoodsCode(),paGoodsDt.getGoodsdtCode(),target.getPaCode(),goodsDt.getGoodsdtInfo());
					
					if(seqOptional.isPresent()) { // 동일한 옵션명으로 수정 시 기존 옵션 살리기
						
						PaQeenGoodsDtMapping seqGoodsDtMapping = seqOptional.get();
						seqGoodsDtMapping.setTransOrderAbleQty(transOrderAbleQty);
						seqGoodsDtMapping.setUseYn("1");
						seqGoodsDtMapping.setModifyDate(currentDate);
						seqGoodsDtMapping.setModifyId(Application.ID.code());
						goodsDtRepository.save(seqGoodsDtMapping);
						logSync(CdcReason.GOODSDT_MODIFY.code(), syncNote, target);
						
					}else {
						goodsDtMapping = PaQeenGoodsDtMapping.builder()
								.goodsCode(target.getGoodsCode()).paCode(target.getPaCode())
								.goodsdtCode(paGoodsDt.getGoodsdtCode())
								.goodsdtSeq(goodsDtRepository.getNextSeq(target.getPaCode(), target.getGoodsCode(), paGoodsDt.getGoodsdtCode()))
								.goodsdtInfo(goodsDt.getGoodsdtInfo())
								.transOrderAbleQty(transOrderAbleQty)
								.useYn("1")
								.insertDate(currentDate).insertId(Application.ID.code())
								.modifyDate(currentDate).modifyId(Application.ID.code())
								.build();
						goodsDtRepository.save(goodsDtMapping);
						logSync(CdcReason.GOODSDT_MODIFY.code(), syncNote, target);
						
					}
					
					
				} else {
					// 재고동기화
					if (transOrderAbleQty != goodsDtMapping.getTransOrderAbleQty()) {
						String saleNote = "";
						if (goodsDtMapping.getTransOrderAbleQty() == 0 && transOrderAbleQty > 0) {
//							goodsDtMapping.setTransSaleYn("1");
							saleNote = "판매재개";
						}
						goodsDtMapping.setTransOrderAbleQty(transOrderAbleQty);
						goodsDtMapping.setTransStockYn("1");
						goodsDtRepository.save(goodsDtMapping);
						logSync(CdcReason.STOCK_CHANGE.code(), "재고동기화[" + goodsDt.getGoodsdtCode() + "]" + saleNote,
								target);
					}
				}

			} else if (SaleGb.FORSALE.code().equals(goodsDt.getSaleGb())) {
				isDirty = true;
				goodsDtMapping = PaQeenGoodsDtMapping.builder().goodsCode(target.getGoodsCode()).paCode(target.getPaCode())
						.goodsdtCode(paGoodsDt.getGoodsdtCode())
						.goodsdtSeq("001")
						.goodsdtInfo(goodsDt.getGoodsdtInfo())
						.transOrderAbleQty(transOrderAbleQty)
						.useYn("1")
						.insertDate(currentDate).insertId(Application.ID.code())
						.modifyDate(currentDate).modifyId(Application.ID.code())
						.build();
				goodsDtRepository.save(goodsDtMapping);
			}


		}
		return isDirty; // 변경동기화 여부
	}

	
}
