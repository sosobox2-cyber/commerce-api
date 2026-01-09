package com.cware.partner.sync.service;

import java.sql.Timestamp;
import java.util.ArrayList;
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

import com.cware.partner.common.code.Application;
import com.cware.partner.common.code.CdcReason;
import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.code.PaSaleGb;
import com.cware.partner.common.code.PaStatus;
import com.cware.partner.common.code.SaleGb;
import com.cware.partner.common.code.ShipCostFlag;
import com.cware.partner.common.util.StringUtil;
import com.cware.partner.sync.domain.FapleGoodsOffer;
import com.cware.partner.sync.domain.SyncResult;
import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.GoodsDt;
import com.cware.partner.sync.domain.entity.PaCustShipCost;
import com.cware.partner.sync.domain.entity.PaFapleGoods;
import com.cware.partner.sync.domain.entity.PaFapleGoodsDtMapping;
import com.cware.partner.sync.domain.entity.PaFapleGoodsKindsOffer;
import com.cware.partner.sync.domain.entity.PaFapleShipCost;
import com.cware.partner.sync.domain.entity.PaGoodsDt;
import com.cware.partner.sync.domain.entity.PaGoodsOffer;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.domain.entity.PaSaleNoGoods;
import com.cware.partner.sync.domain.id.PaGoodsId;
import com.cware.partner.sync.repository.PaFapleBrandKindsMappingRepository;
import com.cware.partner.sync.repository.PaFapleGoodsDtMappingRepository;
import com.cware.partner.sync.repository.PaFapleGoodsKindsOfferRepository;
import com.cware.partner.sync.repository.PaFapleGoodsRepository;
import com.cware.partner.sync.repository.PaFapleShipCostRepository;
import com.cware.partner.sync.repository.PaSaleNoGoodsRepository;
import com.cware.partner.sync.domain.id.PaFapleShipCostId;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SyncFapleService extends PartnerSyncService {

	@Autowired 
	private PaFapleGoodsRepository paFapleGoodsRepository;

	@Autowired
	private PaSaleNoGoodsRepository saleNoGoodsRepository;
	
	@Autowired
	private PaFapleShipCostRepository paFapleShipCostRepository;
	
	@Autowired
	private PaFapleBrandKindsMappingRepository paFapleBrandKindsMappingRepository;
	
	@Autowired
	private PaFapleGoodsDtMappingRepository goodsDtRepository;
	
	@Autowired
	private PaFapleGoodsKindsOfferRepository paFapleGoodsKindsOfferRepository;
	
	
    @Async("partnerAsyncExecutor")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public CompletableFuture<SyncResult> asyncService(Goods goods, PaGoodsTarget target) {
		boolean result = syncProduct(goods, target);
		SyncResult sync = SyncResult.builder().isSync(result).target(target).build();
		return CompletableFuture.completedFuture(sync);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean syncProduct(Goods goods, PaGoodsTarget target) {
		tag = "패션플러스 동기화";

		target.setMediaCode(PaGroup.FAPLE.mediaCode());

		log.info("{} -------> {}", tag, target);

		Optional<PaFapleGoods> optional = paFapleGoodsRepository
				.findById(new PaGoodsId(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()));
		PaFapleGoods partnerGoods;
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
			boolean shipChangeResult = false;
			boolean shipResult = false;
			boolean result;
			// 재입점 처리
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
			shipResult = result;
			isTransTarget = isTransTarget || result;
			
			if(!shipResult) {
				result = syncShipCostCodeModifyChk(goods, target, partnerGoods);
				shipChangeResult = result;
				isTransTarget = isTransTarget || result;
			}
			
			
			if (isTransTarget) {
				
				if(shipResult|| shipChangeResult) { // 배송비 변경이 이루어진 경우 brandId 삭제 
					partnerGoods.setBrandId(null);
				}
				
				partnerGoods.setTransTargetYn("1");
				partnerGoods.setLastSyncDate(procDate);
				partnerGoods.setModifyDate(procDate);
				partnerGoods.setModifyId(Application.ID.code());

				if (PaStatus.REJECT.code().equals(partnerGoods.getPaStatus())
						&& SaleGb.FORSALE.code().equals(goods.getSaleGb())) {
					partnerGoods.setPaSaleGb(PaSaleGb.FORSALE.code());
				}

				paFapleGoodsRepository.save(partnerGoods);
			}

			return isTransTarget;

		}

		// 신규입점
		partnerGoods = PaFapleGoods.builder().goodsCode(target.getGoodsCode())
				.paCode(target.getPaCode())
				.paGroupCode(target.getPaGroupCode())
				.paSaleGb(PaSaleGb.FORSALE.code())
				.paStatus(PaStatus.REQUEST.code())
				.goodsName(goods.getGoodsName())
				.transTargetYn("1")
				.transSaleYn("0")
				.brandChangeYn("0")
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
		
		// 정보고시 동기화
		if (!syncGoodsOffer(goods.getOfferType(), target)) return false;

		// 상품 기본 동기화
		paFapleGoodsRepository.save(partnerGoods);

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

	private boolean syncGoodsKindsModify(String lmsdCode, PaGoodsTarget target) {
		PaFapleGoods partnerGoods = (PaFapleGoods) target.getPartnerGoods();
		String paLmsdKey = "";
		
		//표준카테고리 매핑
		paLmsdKey = getLmsdKey(lmsdCode, target);
		if(paLmsdKey == null) return false;
		
		//표준,전시카테고리 변경 체크
		if(paLmsdKey.equals(partnerGoods.getPaLmsdKey())) {
			return false;
		}
		
		partnerGoods.setPaLmsdKey(paLmsdKey);
				
		return true;
	}

	// 재입점
	private boolean syncForSale (PaGoodsTarget target) {

		PaFapleGoods partnerGoods = (PaFapleGoods)target.getPartnerGoods();

		// 입점완료상태이면서 판매중지 상태인 경우 재입점 처리
		if (PaStatus.COMPLETE.code().equals(partnerGoods.getPaStatus())
				&& PaSaleGb.SUSPEND.code().equals(partnerGoods.getPaSaleGb()) && "1".equals(target.getAutoYn())) {
			String syncNote = "상품 재입점";
			Timestamp currentDate = commonService.currentDate();

			partnerGoods.setTransSaleYn("1");
			partnerGoods.setPaSaleGb(PaSaleGb.FORSALE.code());
			partnerGoods.setModifyId(Application.ID.code());
			partnerGoods.setModifyDate(currentDate);
			paFapleGoodsRepository.save(partnerGoods);
			logSync(CdcReason.SALE_START.code(), syncNote, target);

			return true;
		} else if (PaStatus.REJECT.code().equals(partnerGoods.getPaStatus()) && "1".equals(target.getAutoYn())) { // 반려건 입점요청
			String syncNote = "반려상품 입점요청";
			Timestamp currentDate = commonService.currentDate();
			partnerGoods.setPaStatus(PaSaleGb.REQUEST.code());
			partnerGoods.setPaSaleGb(PaSaleGb.FORSALE.code());
			partnerGoods.setModifyId(Application.ID.code());
			partnerGoods.setModifyDate(currentDate);
			paFapleGoodsRepository.save(partnerGoods);
			logSync(CdcReason.SALE_START.code(), syncNote, target);
		} else {
			if (requestSyncPartner(target))
				return true;
		}

		return false;
	}

	// 상품정보동기화
	private boolean syncPaGoods(Goods goods, PaGoodsTarget target) {

		PaFapleGoods partnerGoods = (PaFapleGoods)target.getPartnerGoods();
		if (goods.getPaGoods().getLastSyncDate().after(partnerGoods.getLastSyncDate())) {
			partnerGoods.setGoodsName(goods.getGoodsName());

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
		int result = paFapleGoodsRepository.stopSale(goodsCode, paCode, Application.ID.code());

		log.info("패션플러스 판매중지 상품: {} ({})", goodsCode, result);

		return result;
	}

	// 제휴필터에서 호출
	public boolean stopSale(PaGoodsTarget target) {
		Optional<PaFapleGoods> optional = paFapleGoodsRepository
				.findById(new PaGoodsId(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()));

		if (!optional.isPresent())
			return false;

		PaFapleGoods partnerGoods = optional.get();
		Timestamp currentDate = commonService.currentDate();

		// 입점완료이고, 판매중인 경우 판매중단연동타겟팅 및 이력생성
		if (PaStatus.COMPLETE.code().equals(partnerGoods.getPaStatus())
				&& PaSaleGb.FORSALE.code().equals(partnerGoods.getPaSaleGb())) {

			partnerGoods.setPaSaleGb(PaSaleGb.SUSPEND.code());
			partnerGoods.setTransSaleYn("1");
			partnerGoods.setModifyDate(currentDate);
			partnerGoods.setModifyId(Application.ID.code());
			partnerGoods.setReturnNote(target.getExceptNote());
			paFapleGoodsRepository.save(partnerGoods);

			PaSaleNoGoods saleNo = PaSaleNoGoods.builder()
					.paGroupCode(target.getPaGroupCode())
					.paCode(target.getPaCode())
					.goodsCode(target.getGoodsCode())
					.seqNo(saleNoGoodsRepository.getNextSeq(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()))
					.paGoodsCode(partnerGoods.getItemId())
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
			paFapleGoodsRepository.save(partnerGoods);
		}

		return false;
	}

	// 전송대상설정
	public boolean enableTransTarget(String goodsCode, String paGroupCode, String paCode) {
		int result = paFapleGoodsRepository.enableTransTarget(goodsCode, paCode, Application.ID.code());

		log.info("패션플러스 전송대상 상품: {} ({})", goodsCode, paCode, result);

		return result > 0;
	}
	
	
	// 고객부담배송비
	protected boolean syncShipCost(Goods goods, PaGoodsTarget target) {
		String syncNote = "배송비 동기화";
		
		PaCustShipCost shipCost = goods.getPaCustShipCost();
		
		double ordCost = shipCost.getOrdCost();
		// 반품비 vs 교환비 중 큰값 - 주문배송비
		double returnCost = (shipCost.getReturnCost() >= shipCost.getChangeCost() ? shipCost.getReturnCost() : shipCost.getChangeCost()) - ordCost;
		// 도서 vs 제주 중 큰값
		double islandCost = shipCost.getIslandCost() >= shipCost.getJejuCost() ? shipCost.getIslandCost() : shipCost.getJejuCost();
		// 도서산간 vs 제주 반품비or교환비 중 큰값 - 주문배송비
		double islandReturnCost = (paFapleShipCostRepository.getIslandReturnShipCost(shipCost.getEntpCode(), shipCost.getShipCostCode())) - ordCost;
		
		
		Optional<PaFapleShipCost> optional = paFapleShipCostRepository
				.findById(new PaFapleShipCostId(target.getPaCode(), shipCost.getEntpCode(), goods.getBrandCode(),
												 "998", "999", shipCost.getShipCostCode(), ordCost, returnCost,
												 islandCost, islandReturnCost)); // 패션플러스 출고지 회수지 고정(임의값)
		
		Timestamp currentDate = commonService.currentDate();
		PaFapleShipCost fapleShipCost = new PaFapleShipCost();
		boolean isDirty = false;
		
		if (optional.isPresent()) { 
			
			BeanUtils.copyProperties(optional.get(), fapleShipCost);
			
			isDirty = optional.get().getLastSyncDate().after(target.getPartnerGoods().getLastSyncDate());
			if (!shipCost.getApplyDate().after(optional.get().getLastSyncDate())) {
				if (isDirty) logSync(CdcReason.SHIP_COST_APPLY.code(), syncNote, target);
				return isDirty;
			}
		} else {
			fapleShipCost.setInsertId(Application.ID.code());
			fapleShipCost.setInsertDate(currentDate);
			
			fapleShipCost.setLastEntpSyncDate(currentDate);// 최초 등록시에만 싱크업데이트, 그 이후는 EntpAddrSyncFapleService 업체 동기화로
		}
		
		fapleShipCost.setPaCode(target.getPaCode());
		fapleShipCost.setEntpCode(shipCost.getEntpCode());
		fapleShipCost.setBrandCode(goods.getBrandCode());
		fapleShipCost.setShipManSeq("998");
		fapleShipCost.setReturnManSeq("999");
		fapleShipCost.setShipCostCode(goods.getShipCostCode());
		
		fapleShipCost.setSaleBrandName(goods.getBrandName()+ "_" +target.getPaCode() + "_" + shipCost.getEntpCode()+"_"+goods.getBrandCode()+"_"+ goods.getShipCostCode()
									+ "_" +	String.valueOf((int)ordCost)+ "_" +	String.valueOf((int)returnCost)+ "_" + String.valueOf((int)islandCost) + "_" + String.valueOf((int)islandReturnCost));
		
		fapleShipCost.setBrandName(goods.getBrandName());
		
		
		if(ShipCostFlag.GOODS.code().equals(shipCost.getShipCostFlag())) {
			fapleShipCost.setShipCostBaseAmt(99999999);
		}else {
			fapleShipCost.setShipCostBaseAmt(shipCost.getShipCostBaseAmt());
		}
		
		fapleShipCost.setOrdCost(ordCost);
		fapleShipCost.setReturnCost(returnCost); 
		fapleShipCost.setIslandCost(islandCost);
		fapleShipCost.setIslandReturnCost(islandReturnCost); 
		fapleShipCost.setPaLgroupName(paFapleBrandKindsMappingRepository.getBrandKindsName(getLmsdKey(goods.getLmsdCode(), target))); // 패플 카테고리 대분류
		
		fapleShipCost.setTransTargetYn("1");
		fapleShipCost.setLastSyncDate(currentDate);
		fapleShipCost.setModifyId(Application.ID.code());
		fapleShipCost.setModifyDate(currentDate);
		
		try {
			paFapleShipCostRepository.saveAndFlush(fapleShipCost);
		} catch (DataIntegrityViolationException ex) {
			log.info("배송비정책이 이미 생성되었습니다. 상품:{}, 제휴사:{}", target.getGoodsCode(), target.getPaCode());
		}
		logSync(CdcReason.SHIP_COST_APPLY.code(), syncNote, target);
		return true;
	}

	// 고객부담배송비
	protected boolean syncShipCostCodeModifyChk(Goods goods, PaGoodsTarget target, PaFapleGoods partnerGoods ) {
		String syncNote = "배송비 변경 체크";
		
		PaCustShipCost shipCost = goods.getPaCustShipCost();
		
		double ordCost = shipCost.getOrdCost();
		double returnCost = (shipCost.getReturnCost() >= shipCost.getChangeCost() ? shipCost.getReturnCost() : shipCost.getChangeCost()) - ordCost;
		double islandCost = shipCost.getIslandCost() >= shipCost.getJejuCost() ? shipCost.getIslandCost() : shipCost.getJejuCost();
		double islandReturnCost = (paFapleShipCostRepository.getIslandReturnShipCost(shipCost.getEntpCode(), shipCost.getShipCostCode())) - ordCost;
		
		
		Optional<PaFapleShipCost> optional = paFapleShipCostRepository
				.findById(new PaFapleShipCostId(target.getPaCode(), shipCost.getEntpCode(), goods.getBrandCode(),
						"998", "999", shipCost.getShipCostCode(), ordCost, returnCost,
						islandCost, islandReturnCost)); // 패션플러스 출고지 회수지 고정(임의값)
		
		PaFapleShipCost fapleShipCost = new PaFapleShipCost();
		
		if (optional.isPresent()) { 
			
			BeanUtils.copyProperties(optional.get(), fapleShipCost);
			
			String oldBrandId = (partnerGoods.getBrandId() == null || partnerGoods.getBrandId().isEmpty()) ? "" : partnerGoods.getBrandId();
	        String newBrandId = (fapleShipCost.getBrandId() == null || fapleShipCost.getBrandId().isEmpty()) ? "" : fapleShipCost.getBrandId() ;
			if(!oldBrandId.equals(newBrandId)) {
				return true ;
			}
			
		}
		
		
		logSync(CdcReason.SHIP_COST_APPLY.code(), syncNote, target);
		return false;
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

			Optional<PaFapleGoodsDtMapping> optional = goodsDtRepository
					.findGoodsDtMapping(target.getPaCode(), target.getGoodsCode(), goodsDt.getGoodsdtCode());

			PaFapleGoodsDtMapping goodsDtMapping;
			int transOrderAbleQty = SaleGb.FORSALE.code().equals(goodsDt.getSaleGb()) ?
					(int) Math.ceil(goodsDt.getOrderAbleQty() * stockRate) : 0;

			if (optional.isPresent()) {
				goodsDtMapping = optional.get();
				
				// 단품명이 바뀌면 기존 단품 매핑 비활성화 후 이력 생성
				if (!goodsDt.getGoodsdtInfo().equals(goodsDtMapping.getGoodsdtInfo())) {
					//isDirty = true;
					goodsDtMapping.setUseYn("0");
//					goodsDtMapping.setTransSaleYn("1");
					goodsDtMapping.setTransStockYn("1");// 재고 0으로 만들기 위해
					goodsDtMapping.setModifyDate(currentDate);
					goodsDtMapping.setModifyId(Application.ID.code());
					goodsDtRepository.save(goodsDtMapping);
					
					Optional<PaFapleGoodsDtMapping> seqOptional = goodsDtRepository.findTopByGoodsCodeAndGoodsdtCodeAndPaCodeAndGoodsdtInfoOrderByGoodsdtSeqAsc(target.getGoodsCode(),paGoodsDt.getGoodsdtCode(),target.getPaCode(),goodsDt.getGoodsdtInfo());
					
					if(seqOptional.isPresent()) { // 동일한 옵션명으로 수정 시 기존 옵션 살리기
						PaFapleGoodsDtMapping seqGoodsDtMapping = seqOptional.get();
						
						seqGoodsDtMapping.setTransOrderAbleQty(transOrderAbleQty);
//							seqGoodsDtMapping.setTransSaleYn("1"); 
						seqGoodsDtMapping.setTransStockYn("1");
						seqGoodsDtMapping.setUseYn("1");
						seqGoodsDtMapping.setModifyDate(currentDate);
						seqGoodsDtMapping.setModifyId(Application.ID.code());
						goodsDtRepository.save(seqGoodsDtMapping);
						logSync(CdcReason.GOODSDT_MODIFY.code(), syncNote, target);
					}else {
						goodsDtMapping = PaFapleGoodsDtMapping.builder()
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
				//isDirty = true;
				goodsDtMapping = PaFapleGoodsDtMapping.builder().goodsCode(target.getGoodsCode()).paCode(target.getPaCode())
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

	
	// 정보고시동기화
	protected boolean syncGoodsOffer(String offerType, PaGoodsTarget target) {
		String tag = "정보고시 동기화";

		PaFapleGoods partnerGoods = (PaFapleGoods) target.getPartnerGoods();
		PaFapleGoodsKindsOffer fapleKindsOffer = paFapleGoodsKindsOfferRepository.getByPaLmsdKey(partnerGoods.getPaLmsdKey());
		
		if(fapleKindsOffer==null) {
			target.setExcept(true);
			target.setExceptNote("제휴사 카레고리 별 정보고시 유형과 매핑된 데이터가 없습니다.");
			log.info("{}: {} [상품:{} 제휴사:{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
			logFilter("SYNC-GOODS_OFFER", target);
			return false;
		}
		
		String paGroupCode = target.getPaGroupCode();
		Timestamp currentDate = commonService.currentDate();

		List<PaGoodsOffer> list = goodsOfferRepository.selectGoodsOffer(paGroupCode, target.getGoodsCode(), offerType);
		
		if (list.isEmpty()) {
			target.setExcept(true);
			target.setExceptNote("제휴사 정보고시 매핑 데이터가 없습니다.");
			log.info("{}: {} [상품:{} 제휴사:{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
			logFilter("SYNC-GOODS_OFFER", target);
			return false;
		}
		
		// 패션플러스 카테고리별로 허용되는 정보고시 항목이 맞지 않는 경우
		// 패션플러스 허용 정보고시 항목으로 가짜 데이터 생성하여 연동하도록 협의 (대신 정보고시 내용은 '상세설명참조'로 모두 통일)	
		if(!list.get(0).getPaOfferType().equals(fapleKindsOffer.getPaOfferType())) {
			
			List<PaGoodsOffer> FakeOfferlist = new ArrayList<>();
			List<FapleGoodsOffer> FapleFakeOfferlist = goodsOfferRepository.selectGoodsFakeOffer(paGroupCode, target.getGoodsCode(), fapleKindsOffer.getPaOfferType());
						
			if (FapleFakeOfferlist.isEmpty()) {
				target.setExcept(true);
				target.setExceptNote("제휴사 정보고시 매핑 데이터가 없습니다.");
				log.info("{}: {} [상품:{} 제휴사:{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
				logFilter("SYNC-GOODS_OFFER", target);
				return false;
			}
			
			for (FapleGoodsOffer fapleOffer : FapleFakeOfferlist) {
				PaGoodsOffer offer = new PaGoodsOffer();
				
				offer.setGoodsCode(fapleOffer.getGoodsCode());
				offer.setPaGroupCode(fapleOffer.getPaGroupCode());
				offer.setPaOfferType(fapleOffer.getPaOfferType());
				offer.setPaOfferCode(fapleOffer.getPaOfferCode());
				offer.setPaOfferExt(fapleOffer.getPaOfferExt());				
				offer.setUseYn("1");
				offer.setTransTargetYn("1");
				offer.setLastSyncDate(currentDate);
				offer.setInsertId(Application.ID.code());
				offer.setInsertDate(currentDate);
				offer.setModifyId(Application.ID.code());
				offer.setModifyDate(currentDate);
				
				FakeOfferlist.add(offer);
			}
			
			log.info("{}: [상품:{} 제휴사:{}]", tag, target.getGoodsCode(), target.getPaGroupCode());
			goodsOfferRepository.saveAll(FakeOfferlist);
			
		} else { // 패션플러스 허용 정보고시 항목이 일치할 경우 정상적으로 정보고시 연동처리
			
			for (PaGoodsOffer offer : list) {
				offer.setGoodsCode(target.getGoodsCode());
				offer.setUseYn("1");
				offer.setTransTargetYn("1");
				offer.setLastSyncDate(currentDate);
				offer.setInsertId(Application.ID.code());
				offer.setInsertDate(currentDate);
				offer.setModifyId(Application.ID.code());
				offer.setModifyDate(currentDate);
			}
			
			log.info("{}: [상품:{} 제휴사:{}]", tag, target.getGoodsCode(), target.getPaGroupCode());
			goodsOfferRepository.saveAll(list);
			
		}
		
		return true;
	}
	
	// 정보고시동기화
	protected boolean syncGoodsOfferModify(String offerType, PaGoodsTarget target) {
		String syncNote = "정보고시변경 동기화";

		boolean isDirty;

		Timestamp currentDate = commonService.currentDate();

		String paGroupCode = target.getPaGroupCode();
		
		PaFapleGoods partnerGoods = (PaFapleGoods) target.getPartnerGoods();
		PaFapleGoodsKindsOffer fapleKindsOffer = paFapleGoodsKindsOfferRepository.getByPaLmsdKey(partnerGoods.getPaLmsdKey());
		
		if(fapleKindsOffer==null) {
			target.setExcept(true);
			target.setExceptNote("제휴사 카레고리 별 정보고시 유형과 매핑된 데이터가 없습니다.");
			log.info("{}: {} [상품:{} 제휴사:{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
			logFilter("SYNC-GOODS_OFFER_MODIFY", target);
			return false;
		}
		
		// 이전 동기화된 offerType
		String syncOfferType = goodsOfferRepository.getOfferType(paGroupCode, target.getGoodsCode());
		
		if (StringUtil.compare(offerType, syncOfferType)) {
			
			String paOfferType = goodsOfferRepository.getFaplePaOfferType(paGroupCode, target.getGoodsCode());
			
			if(!paOfferType.equals(fapleKindsOffer.getPaOfferType())) {//카테고리 변경된 경우 체크하기 위해				
// 				제휴사 카테고리 별 필수 정보고시 유형과 일치하지 않는 경우 정보고시를 동기화 하지 않는다. (가짜 정보고시의 경우 동기화안함) 2025.05.16
//				target.setExcept(true);
//				target.setExceptNote("제휴사 카테고리 별 필수 정보고시 유형과 일치하지 않습니다");
//				log.info("{}: {} [상품:{} 제휴사:{}]", tag, target.getExceptNote(), target.getGoodsCode(), target.getPaGroupCode());
//				logFilter("SYNC-GOODS_OFFER_MODIFY", target);
				return true;
			} 
			
			List<PaGoodsOffer> list = goodsOfferRepository.selectGoodsOfferModify(paGroupCode, target.getGoodsCode());
			
			
			for (PaGoodsOffer offer : list) {
				offer.setTransTargetYn("1");
				offer.setLastSyncDate(currentDate);
				offer.setModifyId(Application.ID.code());
				offer.setModifyDate(currentDate);
				goodsOfferRepository.saveAndFlush(offer);
			}

			isDirty = list.size() > 0;

			list = goodsOfferRepository.selectGoodsOfferNew(paGroupCode, target.getGoodsCode());

			for (PaGoodsOffer offer : list) {
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
			
//			제휴사 카테고리 별 필수 정보고시 유형과 일치하지 않는 경우 정보고시를 동기화 하지 않는다. (가짜 정보고시의 경우 동기화안함) 2025.05.16
//			이전 정보고시유형과 달라졌을 경우 기존 정보고시 비활성화 후 카테고리 매핑하여 새로 생성
//			goodsOfferRepository.disableGooodsOffer(paGroupCode, target.getGoodsCode(), Application.ID.code());
//			syncGoodsOffer(offerType, target);
			
			isDirty = true;
		}

		if (isDirty) {
			logSync(CdcReason.OFFER_MODIFY.code(), syncNote, target);
			return true;
		}
		return false;
	}
	
}
