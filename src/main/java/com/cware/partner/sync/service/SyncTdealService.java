package com.cware.partner.sync.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cware.partner.common.code.Application;
import com.cware.partner.common.code.CdcReason;
import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.code.PaSaleGb;
import com.cware.partner.common.code.PaStatus;
import com.cware.partner.common.code.SaleGb;
import com.cware.partner.sync.domain.SyncResult;
import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.GoodsImage;
import com.cware.partner.sync.domain.entity.GoodsInfoImage;
import com.cware.partner.sync.domain.entity.GoodsPrice;
import com.cware.partner.sync.domain.entity.PaCustShipCost;
import com.cware.partner.sync.domain.entity.PaGoodsImage;
import com.cware.partner.sync.domain.entity.PaGoodsPrice;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.domain.entity.PaSaleNoGoods;
import com.cware.partner.sync.domain.entity.PaTdealEvent;
import com.cware.partner.sync.domain.entity.PaTdealGoods;
import com.cware.partner.sync.domain.entity.PaTdealShipCost;
import com.cware.partner.sync.domain.id.PaGoodsId;
import com.cware.partner.sync.domain.id.PaGroupGoodsId;
import com.cware.partner.sync.domain.id.PaTdealShipCostId;
import com.cware.partner.sync.repository.GoodsInfoImageRepository;
import com.cware.partner.sync.repository.PaBrandMappingRepository;
import com.cware.partner.sync.repository.PaGoodsPriceRepository;
import com.cware.partner.sync.repository.PaSaleNoGoodsRepository;
import com.cware.partner.sync.repository.PaTdealEventRepository;
import com.cware.partner.sync.repository.PaTdealGoodsRepository;
import com.cware.partner.sync.repository.PaTdealShipCostRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SyncTdealService extends PartnerSyncService {

	@Autowired 
	private PaTdealGoodsRepository partnerGoodsRepository;

	@Autowired
	private PaSaleNoGoodsRepository saleNoGoodsRepository;
	
    @Autowired
    private PaBrandMappingRepository brandMappingRepository;
    
	@Autowired
	private PaTdealShipCostRepository paTdealShipCostRepository;
	
	@Autowired
	private PaTdealEventRepository paTdealEventRepository;
	
    @Autowired
    private GoodsInfoImageRepository goodsInfoImageRepository;   
    
	@Autowired
	private PaGoodsPriceRepository paGoodsPriceRepository;

    @Async("partnerAsyncExecutor")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public CompletableFuture<SyncResult> asyncService(Goods goods, PaGoodsTarget target) {
		boolean result = syncProduct(goods, target);
		SyncResult sync = SyncResult.builder().isSync(result).target(target).build();
		return CompletableFuture.completedFuture(sync);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean syncProduct(Goods goods, PaGoodsTarget target) {
		tag = "티딜 동기화";

		target.setMediaCode(PaGroup.TDEAL.mediaCode());

		log.info("{} -------> {}", tag, target);

		Optional<PaTdealGoods> optional = partnerGoodsRepository
				.findById(new PaGoodsId(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()));
		PaTdealGoods partnerGoods;
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
			// 티딜의 경우 자동재입점기능 이슈로 인하여 사용안함
//			isTransTarget = syncForSale(target);

			// 카테고리 동기화
			result = syncGoodsKindsModify(goods.getLmsdCode(), target);
			isTransTarget = isTransTarget || result;
			
			// 정보고시 동기화
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
			
			// 옵션별 이미지 동기화
			result = syncGoodsdtImage(goods, target);
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
			
			// 티딜 행사 동기화
			result = syncTdealEvent(goods, target);
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

				partnerGoodsRepository.save(partnerGoods);
			}

			return isTransTarget;

		}

		// 신규입점
		partnerGoods = PaTdealGoods.builder().goodsCode(target.getGoodsCode())
				.paCode(target.getPaCode())
				.paGroupCode(target.getPaGroupCode())
				.paSaleGb(PaSaleGb.FORSALE.code())
				.paStatus(PaStatus.REQUEST.code())
				.goodsName(goods.getGoodsName())
				.transTargetYn("1")
				.transSaleYn("0")
				.transOrderAbleQty(target.getPartnerStockQty())
				.lastSyncDate(procDate)
				.lastDtimageSyncDate(procDate)
				.insertId(Application.ID.code())
				.insertDate(procDate)
				.modifyId(Application.ID.code())
				.modifyDate(procDate)
				.build();

		target.setPartnerGoods(partnerGoods);

		// 표준카테고리매핑
		partnerGoods.setPaLmsdKey(getLmsdKey(goods.getLmsdCode(), target));
		if (partnerGoods.getPaLmsdKey() == null) return false;

		
		// 전시카테고리 매핑
		partnerGoods.setDispCatId(paGoodsKindsMappingRepository.getTdealDispalyCategory(partnerGoods.getPaLmsdKey()));
		
		if (partnerGoods.getDispCatId() == null) {
			target.setExcept(true);
			target.setExceptNote("제휴사 전시카테고리 매핑이 되지 않습니다.");
			log.info("{}: {} [상품:{}]", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("SYNCTDEAL-DISP_CAT_ID", target);
			return false;
		}
				
        // 브랜드매핑
        String paBrandNo = brandMappingRepository.findMappingByPaGroupCode(target.getPaGroupCode(), goods.getBrandCode());
        partnerGoods.setPaBrandNo(StringUtils.hasText(paBrandNo) ? paBrandNo : "173990");
		
		// 정보고시 동기화
		if (!syncGoodsOffer(goods.getOfferType(), target)) return false;

		// 상품 기본 동기화
		partnerGoodsRepository.save(partnerGoods);

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
 		
		// 티딜 행사 동기화
		syncTdealEvent(goods, target);
		
		
		// 제휴입점요청처리
		return requestSyncPartner(target);

	}

	private boolean syncGoodsKindsModify(String lmsdCode, PaGoodsTarget target) {
		PaTdealGoods partnerGoods = (PaTdealGoods) target.getPartnerGoods();
		String paLmsdKey = "";
		String dispCatId = "";
		
		//표준카테고리 매핑
		paLmsdKey = getLmsdKey(lmsdCode, target);
		if(paLmsdKey == null) return false;
		
	
		// 전시카테고리 매핑
		dispCatId = paGoodsKindsMappingRepository.getTdealDispalyCategory(paLmsdKey);
		if (dispCatId == null) {
			target.setExcept(true);
			target.setExceptNote("제휴사 전시카테고리 매핑이 되지 않습니다.");
			log.info("{}: {} [상품:{}]", tag, target.getExceptNote(), target.getGoodsCode());
			logFilter("SYNCTDEAL-DISP_CAT_ID", target);
			return false;
		}
		
		//표준,전시카테고리 변경 체크
		if(paLmsdKey.equals(partnerGoods.getPaLmsdKey()) && dispCatId.equals(partnerGoods.getDispCatId())) {
			return false;
		}
		
		partnerGoods.setPaLmsdKey(paLmsdKey);
		partnerGoods.setDispCatId(dispCatId);

		return true;
	}

	// 상품가격변경
	protected boolean syncGoodsPriceMoidfy(GoodsPrice price, String lmsdCode, PaGoodsTarget target) {
		Optional<PaGoodsPrice> optional = paGoodsPriceRepository.findApplyGoodsPrice(target.getPaCode(), target.getGoodsCode());
		boolean isIdentical = false;

		if (optional.isPresent()) {
			PaGoodsPrice paPrice = optional.get();
			
			Double catCommission = null;
			
			//적용된 행사의 카테고리 수수료율 우선 적용
			List<PaTdealEvent> paTdealEventList = paTdealEventRepository.selectTdealEvent(target.getGoodsCode());
			
			if (!paTdealEventList.isEmpty() && paTdealEventList.size()==1 && paTdealEventList.get(0).getPaLmsdKey()!=null) {
				PaTdealEvent paTdealEvent = paTdealEventList.get(0);
				catCommission = categoryService.getTdealPaCatCommission(paTdealEvent.getPaLmsdKey(), target.getPaCode());
			}else {
				catCommission = categoryService.getTdealCatCommission(lmsdCode, target.getPaCode());
			}
			
			if (paPrice.getApplyDate().equals(price.getApplyDate())
					&& String.valueOf(paPrice.getCommision()).equals(String.valueOf(catCommission))) {
				return false;
			}
			
			// 실제 가격정보가 변경되었는지 체크하여 변경되지 않은 경우 전송된걸로 처리
			if (paPrice.getTransDate() != null && paPrice.getSalePrice() == price.getSalePrice()
					&& paPrice.getDcAmt() == price.getArsDcAmt() && paPrice.getLumpSumDcAmt() == price.getLumpSumDcAmt()
					&& paPrice.getLumpSumOwnDcAmt() == price.getLumpSumOwnDcAmt()
					&& paPrice.getLumpSumEntpDcAmt() == price.getLumpSumEntpDcAmt()) {
				isIdentical = true;
			}
		}
		syncGoodsPrice(price, lmsdCode, target, isIdentical);
		return true;
	}
	
	// 재입점
	private boolean syncForSale (PaGoodsTarget target) {

		PaTdealGoods partnerGoods = (PaTdealGoods)target.getPartnerGoods();

		// 입점완료상태이면서 판매중지 상태인 경우 재입점 처리
		if (PaStatus.COMPLETE.code().equals(partnerGoods.getPaStatus())
				&& PaSaleGb.SUSPEND.code().equals(partnerGoods.getPaSaleGb()) && "1".equals(target.getAutoYn())) {
			String syncNote = "상품 재입점";
			Timestamp currentDate = commonService.currentDate();

			partnerGoods.setTransSaleYn("1");
			partnerGoods.setPaSaleGb(PaSaleGb.FORSALE.code());
			partnerGoods.setModifyId(Application.ID.code());
			partnerGoods.setModifyDate(currentDate);
			partnerGoodsRepository.save(partnerGoods);
			logSync(CdcReason.SALE_START.code(), syncNote, target);

			return true;
		} else if (PaStatus.REJECT.code().equals(partnerGoods.getPaStatus()) && "1".equals(target.getAutoYn())) { // 반려건 입점요청
			String syncNote = "반려상품 입점요청";
			Timestamp currentDate = commonService.currentDate();
			partnerGoods.setPaStatus(PaSaleGb.REQUEST.code());
			partnerGoods.setPaSaleGb(PaSaleGb.FORSALE.code());
			partnerGoods.setModifyId(Application.ID.code());
			partnerGoods.setModifyDate(currentDate);
			partnerGoodsRepository.save(partnerGoods);
			logSync(CdcReason.SALE_START.code(), syncNote, target);
		} else {
			if (requestSyncPartner(target))
				return true;
		}

		return false;
	}

	// 상품정보동기화
	private boolean syncPaGoods(Goods goods, PaGoodsTarget target) {

		PaTdealGoods partnerGoods = (PaTdealGoods)target.getPartnerGoods();
		if (goods.getPaGoods().getLastSyncDate().after(partnerGoods.getLastSyncDate())) {
			partnerGoods.setGoodsName(goods.getGoodsName());

            partnerGoods.setPaBrandNo(
                    brandMappingRepository.findMappingByPaGroupCode(partnerGoods.getPaGroupCode(),
                            goods.getBrandCode()));
            partnerGoods.setPaBrandNo(
                    StringUtils.hasText(partnerGoods.getPaBrandNo()) ? partnerGoods.getPaBrandNo() : "173990"); 
            
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
		int result = partnerGoodsRepository.stopSale(goodsCode, paCode, Application.ID.code());

		log.info("티딜 판매중지 상품: {} ({})", goodsCode, result);

		return result;
	}

	// 제휴필터에서 호출
	public boolean stopSale(PaGoodsTarget target) {
		Optional<PaTdealGoods> optional = partnerGoodsRepository
				.findById(new PaGoodsId(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()));

		if (!optional.isPresent())
			return false;

		PaTdealGoods partnerGoods = optional.get();
		Timestamp currentDate = commonService.currentDate();

		// 입점완료이고, 판매중인 경우 판매중단연동타겟팅 및 이력생성
		if (PaStatus.COMPLETE.code().equals(partnerGoods.getPaStatus())
				&& PaSaleGb.FORSALE.code().equals(partnerGoods.getPaSaleGb())) {

			partnerGoods.setPaSaleGb(PaSaleGb.SUSPEND.code());
			partnerGoods.setTransSaleYn("1");
			partnerGoods.setModifyDate(currentDate);
			partnerGoods.setModifyId(Application.ID.code());
			partnerGoods.setReturnNote(target.getExceptNote());
			partnerGoodsRepository.save(partnerGoods);

			PaSaleNoGoods saleNo = PaSaleNoGoods.builder()
					.paGroupCode(target.getPaGroupCode())
					.paGoodsCode(target.getPaGroupCode())
					.paCode(target.getPaCode())
					.goodsCode(target.getGoodsCode())
					.seqNo(saleNoGoodsRepository.getNextSeq(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()))
					.paGoodsCode(partnerGoods.getMallProductNo())
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
			partnerGoodsRepository.save(partnerGoods);
		}

		return false;
	}

	// 전송대상설정
	public boolean enableTransTarget(String goodsCode, String paGroupCode, String paCode) {
		int result = partnerGoodsRepository.enableTransTarget(goodsCode, paCode, Application.ID.code());

		log.info("티딜 전송대상 상품: {} ({})", goodsCode, paCode, result);

		return result > 0;
	}
	
	// 고객부담배송비
	protected boolean syncShipCost(Goods goods, PaGoodsTarget target) {
		String syncNote = "배송비 동기화";

		PaCustShipCost shipCost = goods.getPaCustShipCost();
		Optional<PaTdealShipCost> optional = paTdealShipCostRepository
				.findById(new PaTdealShipCostId(target.getPaCode(), shipCost.getEntpCode(), 
												goods.getDelyType().equals("10") ? "003" : goods.getShipManSeq(),
												goods.getDelyType().equals("10") ? "002" : goods.getReturnManSeq(),
												shipCost.getShipCostCode()));

		Timestamp currentDate = commonService.currentDate();
		PaTdealShipCost tdealShipCost = new PaTdealShipCost();
		boolean isDirty = false;

		PaCustShipCost targetCost = new PaCustShipCost();
		BeanUtils.copyProperties(shipCost, targetCost);

		if (optional.isPresent()) {
			isDirty = optional.get().getLastSyncDate().after(target.getPartnerGoods().getLastSyncDate());
			if (!shipCost.getApplyDate().after(optional.get().getLastSyncDate())) {
				if (isDirty) logSync(CdcReason.SHIP_COST_APPLY.code(), syncNote, target);
				return isDirty;
			}
		} else {
			tdealShipCost.setInsertId(Application.ID.code());
			tdealShipCost.setInsertDate(currentDate);
		}
		
		tdealShipCost.setPaCode(target.getPaCode());
		tdealShipCost.setEntpCode(shipCost.getEntpCode());
		tdealShipCost.setShipManSeq(goods.getDelyType().equals("10") ? "003" : goods.getShipManSeq());
		tdealShipCost.setReturnManSeq(goods.getDelyType().equals("10") ? "002" : goods.getReturnManSeq());
		tdealShipCost.setShipCostCode(goods.getShipCostCode());
		
		tdealShipCost.setShipCostBaseAmt(shipCost.getShipCostBaseAmt());
		tdealShipCost.setOrdCost(shipCost.getOrdCost());
		tdealShipCost.setReturnCost(shipCost.getReturnCost());
		tdealShipCost.setChangeCost(shipCost.getChangeCost());
		tdealShipCost.setIslandCost(shipCost.getIslandCost());
		tdealShipCost.setJejuCost(shipCost.getJejuCost());
		
		tdealShipCost.setTransTargetYn("1");
		tdealShipCost.setLastSyncDate(currentDate);
		tdealShipCost.setModifyId(Application.ID.code());
		tdealShipCost.setModifyDate(currentDate);
		
		try {
			paTdealShipCostRepository.saveAndFlush(tdealShipCost);
		} catch (DataIntegrityViolationException ex) {
			log.info("배송비정책이 이미 생성되었습니다. 상품:{}, 제휴사:{}", target.getGoodsCode(), target.getPaCode());
		}
		logSync(CdcReason.SHIP_COST_APPLY.code(), syncNote, target);
		return true;
	}
	
	// 티딜 이미지 동기화 (500*500 으로 연동)
	protected boolean syncGoodsImage(GoodsImage image, PaGoodsTarget target) {
		String syncNote = "이미지 동기화";

		Timestamp currentDate = commonService.currentDate();

		Optional<PaGoodsImage> optional = imageRepository
				.findById(new PaGroupGoodsId(target.getPaGroupCode(), target.getGoodsCode()));

		PaGoodsImage goodsImage;

		GoodsInfoImage mcImage = goodsInfoImageRepository.getTdealGoodsMcImage(target.getGoodsCode());
		GoodsInfoImage listImage =goodsInfoImageRepository.getTdealGoodsListImage(target.getGoodsCode());
		GoodsInfoImage infoImageG =goodsInfoImageRepository.getTdealGoodsInfoImage(target.getGoodsCode());
		
		if (optional.isPresent()) {
			goodsImage = optional.get();
		} else {
			goodsImage = PaGoodsImage.builder()
					.goodsCode(target.getGoodsCode())
					.paGroupCode(target.getPaGroupCode())
					.insertDate(currentDate)
					.insertId(Application.ID.code())
					.build();
		}
		
		if (goodsImage.getLastSyncDate() == null ||
				image.getModifyDate().after(goodsImage.getLastSyncDate())) { 
			goodsImage.setImageP(image.getImageG());
			goodsImage.setImageAp(image.getImageAg());
			goodsImage.setImageBp(image.getImageBg());
			goodsImage.setImageCp(image.getImageCg());
			goodsImage.setImageDp(image.getImageDg());
			goodsImage.setImageUrl(image.getImageUrl());
			goodsImage.setTransTargetYn("1");
			goodsImage.setLastSyncDate(currentDate);
			goodsImage.setModifyId(Application.ID.code());
			goodsImage.setModifyDate(currentDate);
			imageRepository.save(goodsImage);
			logSync(CdcReason.IMAGE_MODIFY.code(), syncNote, target);
			return true;
		}
		
		//티딜 리스트 이미지 동기화
		if(listImage != null) {
			if(listImage.getModifyDate().after(goodsImage.getLastSyncDate())) { 
				logSync(CdcReason.INFO_IMAGE_MODIFY.code(), syncNote, target);
				imageRepository.updateLastSyncDate(goodsImage.getPaGroupCode(), goodsImage.getGoodsCode());
				return true;
			}
		}else if(mcImage != null) {
			if(mcImage.getModifyDate().after(goodsImage.getLastSyncDate())) {
				logSync(CdcReason.INFO_IMAGE_MODIFY.code(), syncNote, target);
				imageRepository.updateLastSyncDate(goodsImage.getPaGroupCode(), goodsImage.getGoodsCode());
				return true;
			}
		}

		//티딜 대표이미지 동기화
		if(infoImageG != null) {
			if(infoImageG.getModifyDate().after(goodsImage.getLastSyncDate())) { 
				logSync(CdcReason.INFO_IMAGE_MODIFY.code(), syncNote, target);
				imageRepository.updateLastSyncDate(goodsImage.getPaGroupCode(), goodsImage.getGoodsCode());
				return true;
			}
		}
		
		return false;
	}
	
	// 티딜 옵션별 이미지 동기화
	protected boolean syncGoodsdtImage(Goods goods, PaGoodsTarget target) {
		String syncNote = "옵션별 이미지 동기화";
		
		Timestamp currentDate = commonService.currentDate();

		PaTdealGoods partnerGoods = (PaTdealGoods)target.getPartnerGoods();
		if (goods.getTdealDtImageModifyDate() != null &&
				goods.getTdealDtImageModifyDate().after(partnerGoods.getLastDtimageSyncDate())) {
			
			partnerGoods.setLastDtimageSyncDate(currentDate);
			
			logSync(CdcReason.TDEAL_DTIMAGE_MODIFY.code(), syncNote, target);
			return true;
		}

		return false;
	}
	
	// 티딜 행사 동기화
	protected boolean syncTdealEvent(Goods goods, PaGoodsTarget target) {
		String syncNote = "티딜 행사 동기화";

		
		//행사 종료 
		List<PaTdealEvent> endlist = paTdealEventRepository.selectTdealEventEnd(target.getGoodsCode());
		
		if(!endlist.isEmpty()) {
			logSync(CdcReason.TDEAL_EVENT_APPLY.code(), syncNote, target);
			PaTdealEvent paTdealEvent = endlist.get(0);
			
			paTdealEventRepository.updateLastSyncDate(paTdealEvent.getEventNo(),paTdealEvent.getGoodsCode());
			logSync(CdcReason.TDEAL_EVENT_END.code(), syncNote, target);
			return true;
		}
		
		//행사 적용
		List<PaTdealEvent> applylist = paTdealEventRepository.selectTdealEventApply(target.getGoodsCode());
		
		if(!applylist.isEmpty()) {
			if(applylist.size()==1) {
				logSync(CdcReason.TDEAL_EVENT_APPLY.code(), syncNote, target);
				PaTdealEvent paTdealEvent = applylist.get(0);
				
				paTdealEventRepository.updateLastSyncDate(paTdealEvent.getEventNo(),paTdealEvent.getGoodsCode());
				logSync(CdcReason.TDEAL_EVENT_APPLY.code(), syncNote, target);
				return true;
			}
		}
		

		
		return false;
	
	}
	
	
}
