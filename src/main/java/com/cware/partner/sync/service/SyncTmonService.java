package com.cware.partner.sync.service;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
import com.cware.partner.sync.domain.SyncResult;
import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.PaCustShipCost;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.domain.entity.PaSaleNoGoods;
import com.cware.partner.sync.domain.entity.PaTmonGoods;
import com.cware.partner.sync.domain.entity.PaTmonShipCost;
import com.cware.partner.sync.domain.id.PaGoodsId;
import com.cware.partner.sync.domain.id.PaTmonShipCostId;
import com.cware.partner.sync.repository.PaSaleNoGoodsRepository;
import com.cware.partner.sync.repository.PaTmonGoodsRepository;
import com.cware.partner.sync.repository.PaTmonShipCostRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SyncTmonService extends PartnerSyncService {

	@Autowired
	private PaTmonGoodsRepository partnerGoodsRepository;

	@Autowired
	private PaTmonShipCostRepository tmonShipCostRepository;

	@Autowired
	private PaSaleNoGoodsRepository saleNoGoodsRepository;

    @Async("partnerAsyncExecutor")
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public CompletableFuture<SyncResult> asyncService(Goods goods, PaGoodsTarget target) {
		boolean result = syncProduct(goods, target);
		SyncResult sync = SyncResult.builder().isSync(result).target(target).build();
		return CompletableFuture.completedFuture(sync);
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public boolean syncProduct(Goods goods, PaGoodsTarget target) {
		tag = "티몬 동기화";

		target.setMediaCode(PaGroup.TMON.mediaCode());

		log.info("{} -------> {}", tag, target);

		Optional<PaTmonGoods> optional = partnerGoodsRepository
				.findById(new PaGoodsId(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()));

		PaTmonGoods partnerGoods;
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
			isTransTarget = syncForSale(target);
			
			// 카테고리 동기화
			result = syncGoodsKindsModify(goods.getLmsdCode(), target);
			isTransTarget = isTransTarget || result;

			// 정보고시 동기화
			result = syncGoodsOfferModify(goods.getOfferType(), target);
			isTransTarget = isTransTarget || result;

			// 상품정보변경 동기화
			result =  syncPaGoods(goods, target);
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

				partnerGoodsRepository.save(partnerGoods);
			}

			return isTransTarget;

		}

		// 신규입점
		partnerGoods = PaTmonGoods.builder().goodsCode(target.getGoodsCode())
				.paCode(target.getPaCode())
				.paGroupCode(target.getPaGroupCode())
				.paSaleGb(PaSaleGb.FORSALE.code())
				.paStatus(PaStatus.REQUEST.code())
				.transTargetYn("1")
				.transSaleYn("0")
				.transOrderAbleQty(target.getPartnerStockQty())
				.lastSyncDate(procDate)
				.managedTitle(goods.getGoodsName())
				.title(goods.getGoodsName())
				.brandName(goods.getBrandName())
				.massTargetYn("0")
				.insertId(Application.ID.code())
				.insertDate(procDate)
				.modifyId(Application.ID.code())
				.modifyDate(procDate)
				.build();

		target.setPartnerGoods(partnerGoods);

		// 표준카테고리매핑
		partnerGoods.setCategoryNo(getLmsdKey(goods.getLmsdCode(), target));
		if (partnerGoods.getCategoryNo() == null) return false;

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

		// 제휴입점요청처리
		return requestSyncPartner(target);
	}

	// 고객부담배송비
	protected boolean syncShipCost(Goods goods, PaGoodsTarget target) {
		String syncNote = "배송비 동기화";

		PaCustShipCost shipCost = goods.getPaCustShipCost();
		String productType;

		if (goods.getInstallYn().equals("1")) {
			productType = "DP04";
		} else if (goods.getGoodsAddInfo().getOrderCreateYn().equals("1")) {
			productType = "DP05";
		} else {
			productType = "DP07";
		}
		Optional<PaTmonShipCost> optional = tmonShipCostRepository
				.findById(new PaTmonShipCostId(target.getPaCode(), shipCost.getEntpCode(), productType
						, goods.getShipManSeq(), goods.getReturnManSeq(), goods.getShipCostCode()
						, shipCost.getApplyDate(), goods.getDoNotIslandDelyYn()));

		if (optional.isPresent()) return false;

		Timestamp currentDate = commonService.currentDate();
		PaTmonShipCost tmonShipCost = PaTmonShipCost.builder().paCode(target.getPaCode())
				.entpCode(shipCost.getEntpCode())
				.productType(productType)
				.shipManSeq(goods.getDelyType().equals("10") ? "003" : goods.getShipManSeq())
				.returnManSeq(goods.getDelyType().equals("10") ? "002" : goods.getReturnManSeq())
				.shipCostCode(goods.getShipCostCode())
				.applyDate(shipCost.getApplyDate())
				.noShipIsland(goods.getDoNotIslandDelyYn())
				.shipCostBaseAmt(shipCost.getShipCostBaseAmt())
				.ordCost(shipCost.getOrdCost())
				.returnCost(shipCost.getReturnCost())
				.changeCost(shipCost.getChangeCost())
				.islandCost(shipCost.getIslandCost())
				.jejuCost(shipCost.getJejuCost())
				.insertId(Application.ID.code())
				.insertDate(currentDate)
				.modifyId(Application.ID.code())
				.modifyDate(currentDate)
				.build();

		try {
			tmonShipCostRepository.saveAndFlush(tmonShipCost);
		} catch (DataIntegrityViolationException ex) {
			log.info("티몬배송비정책이 이미 생성되었습니다. 상품:{}, 제휴사:{}", target.getGoodsCode(), target.getPaCode());
		}
		logSync(CdcReason.SHIP_COST_APPLY.code(), syncNote, target);
		return true;

	}

	// 재입점
	private boolean syncForSale (PaGoodsTarget target) {

		PaTmonGoods partnerGoods = (PaTmonGoods)target.getPartnerGoods();

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
	private boolean syncPaGoods (Goods goods, PaGoodsTarget target) {

		PaTmonGoods partnerGoods = (PaTmonGoods)target.getPartnerGoods();

		if (goods.getPaGoods().getLastSyncDate().after(partnerGoods.getLastSyncDate())) {
			partnerGoods.setManagedTitle(goods.getGoodsName());
			partnerGoods.setTitle(goods.getGoodsName());
			partnerGoods.setBrandName(goods.getBrandName());
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
		return stopSale(goodsCode, "%");
	}

	// 판매중지처리
	public int stopSale(String goodsCode, String paCode) {
		int result = partnerGoodsRepository.stopSale(goodsCode, paCode, Application.ID.code());

		log.info("티몬 판매중지 상품: {} ({})", goodsCode, result );

		return result;
	}

	// 제휴필터에서 호출
	public boolean stopSale(PaGoodsTarget target) {
		Optional<PaTmonGoods> optional = partnerGoodsRepository
				.findById(new PaGoodsId(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()));

		if (!optional.isPresent()) return false;

		PaTmonGoods partnerGoods = optional.get();
		Timestamp currentDate = commonService.currentDate();

		// 입점완료이고, 판매중인 경우 판매중단연동타겟팅 및 이력생성
		if (PaStatus.COMPLETE.code().equals(partnerGoods.getPaStatus()) &&
			PaSaleGb.FORSALE.code().equals(partnerGoods.getPaSaleGb())) {

			partnerGoods.setPaSaleGb(PaSaleGb.SUSPEND.code());
			partnerGoods.setTransSaleYn("1");
			partnerGoods.setModifyDate(currentDate);
			partnerGoods.setModifyId(Application.ID.code());
			partnerGoods.setReturnNote(target.getExceptNote());
			partnerGoodsRepository.save(partnerGoods);


			PaSaleNoGoods saleNo = PaSaleNoGoods.builder()
					.paGroupCode(target.getPaGroupCode())
					.paCode(target.getPaCode())
					.goodsCode(target.getGoodsCode())
					.seqNo(saleNoGoodsRepository.getNextSeq(target.getPaGroupCode(), target.getPaCode(),
							target.getGoodsCode()))
					.paGoodsCode(partnerGoods.getDealNo())
					.paSaleGb(PaSaleGb.SUSPEND.code())
					.note(target.getExceptNote())
					.insertId(Application.ID.code())
					.insertDate(currentDate)
					.build();
			saleNoGoodsRepository.save(saleNo);

			String syncNote = "제휴필터 판매중지";
			log.info("{} {} 상품: {} ", tag, syncNote, target.getGoodsCode() );
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
	public boolean enableTransTarget(String goodsCode) {
		int result = partnerGoodsRepository.enableTransTarget(goodsCode, Application.ID.code());

		log.info("티몬 전송대상 상품: {} ({})", goodsCode, result );

		return result > 0;

	}
	
	// 카테고리 동기화 
	private boolean syncGoodsKindsModify(String lmsdCode, PaGoodsTarget target) {
		String categoryNo = "";
		
		PaTmonGoods partnerGoods = (PaTmonGoods) target.getPartnerGoods();
		
		// 표준카테고리매핑
		categoryNo = getLmsdKey(lmsdCode, target);
		if (categoryNo == null) return false;
		
		// 표준카테고리번호 변경 체크
		if (categoryNo.equals(partnerGoods.getCategoryNo())) {
			return false;
		}
		partnerGoods.setCategoryNo(categoryNo);
		
		return true;
	}
}
