package com.cware.partner.sync.service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

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
import com.cware.partner.common.code.ShipCostFlag;
import com.cware.partner.sync.domain.SyncResult;
import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.GoodsDt;
import com.cware.partner.sync.domain.entity.PaCustShipCost;
import com.cware.partner.sync.domain.entity.PaGoodsDt;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.domain.entity.PaOriginMapping;
import com.cware.partner.sync.domain.entity.PaSaleNoGoods;
import com.cware.partner.sync.domain.entity.PaSsgGoods;
import com.cware.partner.sync.domain.entity.PaSsgGoodsDtMapping;
import com.cware.partner.sync.domain.entity.PaSsgShipCost;
import com.cware.partner.sync.domain.id.PaGoodsId;
import com.cware.partner.sync.domain.id.PaOriginId;
import com.cware.partner.sync.domain.id.PaSsgShipCostId;
import com.cware.partner.sync.repository.PaBrandMappingRepository;
import com.cware.partner.sync.repository.PaOriginMappingRepository;
import com.cware.partner.sync.repository.PaSaleNoGoodsRepository;
import com.cware.partner.sync.repository.PaSsgGoodsDtMappingRepository;
import com.cware.partner.sync.repository.PaSsgGoodsRepository;
import com.cware.partner.sync.repository.PaSsgShipCostRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class SyncSsgService extends PartnerSyncService {

	@Autowired
	private PaSsgGoodsRepository partnerGoodsRepository;

	@Autowired
	private PaOriginMappingRepository originMappingRepository;

	@Autowired
	private PaBrandMappingRepository brandMappingRepository;

	@Autowired
	private PaSsgGoodsDtMappingRepository goodsDtRepository;

	@Autowired
	private PaSsgShipCostRepository shipCostRepository;

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
		tag = "SSG동기화";

		target.setMediaCode(PaGroup.SSG.mediaCode());

		log.info("{} -------> {}", tag, target);

		Optional<PaSsgGoods> optional = partnerGoodsRepository
				.findById(new PaGoodsId(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()));
		PaSsgGoods partnerGoods;

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
			result = syncShipCost(goods.getPaCustShipCost(), target);
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
		partnerGoods = PaSsgGoods.builder().goodsCode(target.getGoodsCode())
				.paCode(target.getPaCode())
				.paGroupCode(target.getPaGroupCode())
				.paSaleGb(PaSaleGb.FORSALE.code())
				.paStatus(PaStatus.REQUEST.code())
				.transTargetYn("1")
				.transSaleYn("0")
				.transOrderAbleQty(target.getPartnerStockQty())
				.lastSyncDate(procDate)
				.manufcoNm(goods.getMakecoName())
				.goodsName(goods.getGoodsNameMc())
				.massTargetYn("0")
				.insertId(Application.ID.code())
				.insertDate(procDate)
				.modifyId(Application.ID.code())
				.modifyDate(procDate)
				.build();

		target.setPartnerGoods(partnerGoods);

		// 표준카테고리매핑
		partnerGoods.setStdCtgId(getLmsdKey(goods.getLmsdCode(), target));
		if (partnerGoods.getStdCtgId() == null) return false;

		// 정보고시 동기화
		if (!syncGoodsOffer(goods.getOfferType(), target)) return false;

		// 원산지코드매핑
		mappingOriginCode(goods.getOriginCode(), target);

		// 브랜드매핑
		String brandId = brandMappingRepository.findMappingByPaGroupCode(target.getPaGroupCode(), goods.getBrandCode());
		partnerGoods.setBrandId(StringUtils.hasText(brandId) ? brandId : "9999999999");

		// 상품 기본 동기화
		partnerGoodsRepository.save(partnerGoods);

		// 가격 동기화 (공통으로 이동)
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
		syncShipCost(goods.getPaCustShipCost(), target);
		
		// 제휴입점요청처리
		return requestSyncPartner(target);
	}

	private boolean mappingOriginCode(String originCode, PaGoodsTarget target) {
		PaSsgGoods partnerGoods = (PaSsgGoods) target.getPartnerGoods();
		Optional<PaOriginMapping> optional = originMappingRepository.findById(new PaOriginId(partnerGoods.getPaGroupCode(), originCode));
		if (optional.isPresent()) {
			partnerGoods.setOrplcId(optional.get().getPaOriginCode());
		} else {
			partnerGoods.setOrplcId("1000000000");
		}
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

			Optional<PaSsgGoodsDtMapping> optional = goodsDtRepository
					.findGoodsDtMapping(target.getPaCode(), target.getGoodsCode(), goodsDt.getGoodsdtCode());

			PaSsgGoodsDtMapping goodsDtMapping;
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

					goodsDtMapping = PaSsgGoodsDtMapping.builder()
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
				} else {
					// 재고동기화
					if (transOrderAbleQty != goodsDtMapping.getTransOrderAbleQty()) {
						goodsDtMapping.setTransOrderAbleQty(transOrderAbleQty);
						goodsDtMapping.setTransStockYn("1");
						goodsDtRepository.save(goodsDtMapping);
						logSync(CdcReason.STOCK_CHANGE.code(), "재고동기화[" + goodsDt.getGoodsdtCode() + "]", target);
					}
				}
			} else if (SaleGb.FORSALE.code().equals(goodsDt.getSaleGb())) {
				isDirty = true;
				goodsDtMapping = PaSsgGoodsDtMapping.builder().goodsCode(target.getGoodsCode()).paCode(target.getPaCode())
						.goodsdtCode(paGoodsDt.getGoodsdtCode())
						.goodsdtSeq("0001")
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

	// 고객부담배송비
	protected boolean syncShipCost(PaCustShipCost shipCost, PaGoodsTarget target) {
		if (super.syncShipCost(shipCost, target)) {
			// 쓱닷컴 배송비정책 생성
			String shppcstAplUnitCd = "40"; // 배송비적용단위코드(10:주문금액합산, 30:상품수량별, 40:상품별주문금액합산)
			if (shipCost.getShipCostFlag().equals(ShipCostFlag.FREE.code()) ||
					shipCost.getShipCostFlag().equals(ShipCostFlag.BASEAMT.code()) ||
					shipCost.getShipCostFlag().equals(ShipCostFlag.BASEAMT_CODE.code())) {
				shppcstAplUnitCd = "10";
			}

			String collectYn = "0"; 
			if("2".equals(shipCost.getShipCostReceipt())) {
				collectYn = "1";
				shipCost.setShipCostBaseAmt(0);
			}
			
			boolean isDirty = false;
			boolean result;

			// 출고배송비
			isDirty = createSsgShipCost(target.getPaCode(), shppcstAplUnitCd, "10", collectYn, shipCost.getShipCostBaseAmt(),
					(int) shipCost.getOrdCost());

			// 반품배송비
			result = createSsgShipCost(target.getPaCode(), shppcstAplUnitCd, "20", collectYn, 0, (int) shipCost.getReturnCost());
			isDirty = isDirty || result;

			// 도서산간
			result = createSsgShipCost(target.getPaCode(), shppcstAplUnitCd, "60", "0", 0,
					shipCost.getIslandCost() > shipCost.getOrdCost()
							? (int) (shipCost.getIslandCost() - shipCost.getOrdCost())
							: 0);
			isDirty = isDirty || result;

			// 제주
			result = createSsgShipCost(target.getPaCode(), shppcstAplUnitCd, "70", "0", 0,
					shipCost.getJejuCost() > shipCost.getOrdCost()
							? (int) (shipCost.getJejuCost() - shipCost.getOrdCost())
							: 0);
			isDirty = isDirty || result;

			return isDirty;
		}

		return false;
	}

	private boolean createSsgShipCost(String paCode, String shppcstAplUnitCd, String shppcstPlcyDivCd, String collectYn,
			int shipCostBaseAmt, int shipCostAmt) {

		Optional<PaSsgShipCost> optional = shipCostRepository.findById(new PaSsgShipCostId(paCode, shppcstAplUnitCd,
				shppcstPlcyDivCd, collectYn, shipCostBaseAmt, shipCostAmt));

		if (!optional.isPresent()) {
			PaSsgShipCost shipCost =
					PaSsgShipCost.builder().paCode(paCode)
					.shppcstAplUnitCd(shppcstAplUnitCd)
					.shppcstPlcyDivCd(shppcstPlcyDivCd)
					.collectYn(collectYn)
					.shipCostBaseAmt(shipCostBaseAmt)
					.shipCost(shipCostAmt)
					.insertId(Application.ID.code())
					.insertDate(commonService.currentDate())
					.build();

			try {
				shipCostRepository.saveAndFlush(shipCost);
			} catch (DataIntegrityViolationException ex) {
				log.info("{} 배송비정책이 이미 생성되었습니다. {}-{}-{}-{}", tag, shppcstAplUnitCd, shppcstPlcyDivCd, shipCostBaseAmt, shipCostAmt);
			}
			return true;
		}
		return false;
	}

	// 재입점
	private boolean syncForSale (PaGoodsTarget target) {

		PaSsgGoods partnerGoods = (PaSsgGoods)target.getPartnerGoods();

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

		PaSsgGoods partnerGoods = (PaSsgGoods)target.getPartnerGoods();
		if (goods.getPaGoods().getLastSyncDate().after(partnerGoods.getLastSyncDate())) {
			mappingOriginCode(goods.getOriginCode(), target);
			partnerGoods.setBrandId(
					brandMappingRepository.findMappingByPaGroupCode(partnerGoods.getPaGroupCode(),
							goods.getBrandCode()));
			partnerGoods.setBrandId(
					StringUtils.hasText(partnerGoods.getBrandId()) ? partnerGoods.getBrandId() : "9999999999");
			partnerGoods.setManufcoNm(goods.getMakecoName());
			partnerGoods.setGoodsName(goods.getGoodsNameMc());
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

		log.info("{} 판매중지 상품: {} ({})", tag, goodsCode, result );

		return result;
	}

	// 제휴필터에서 호출
	public boolean stopSale(PaGoodsTarget target, boolean isDiscard) {
		Optional<PaSsgGoods> optional = partnerGoodsRepository
				.findById(new PaGoodsId(target.getPaGroupCode(), target.getPaCode(), target.getGoodsCode()));

		if (!optional.isPresent()) return false;

		PaSsgGoods partnerGoods = optional.get();
		Timestamp currentDate = commonService.currentDate();

		// 입점완료이고, 판매중인 경우 판매중단연동타겟팅 및 이력생성
		if (PaStatus.COMPLETE.code().equals(partnerGoods.getPaStatus()) &&
			PaSaleGb.FORSALE.code().equals(partnerGoods.getPaSaleGb())) {

			if (isDiscard) partnerGoods.setPaStatus(PaStatus.DELETED.code()); // 영구판매중지
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
					.paGoodsCode(partnerGoods.getItemId())
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
		} else if(PaStatus.COMPLETE.code().equals(partnerGoods.getPaStatus()) && isDiscard) {
			partnerGoods.setPaStatus(PaStatus.DELETED.code()); // 영구판매중지
			partnerGoods.setTransSaleYn("1");
			partnerGoods.setModifyDate(currentDate);
			partnerGoods.setModifyId(Application.ID.code());
			partnerGoodsRepository.save(partnerGoods);
			String syncNote = "제휴필터 영구중지";
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
	public boolean enableTransTarget(String goodsCode, String paCode) {
		int result = partnerGoodsRepository.enableTransTarget(goodsCode, paCode, Application.ID.code());

		log.info("{} 전송대상 상품: {} ({})", tag, goodsCode, result );

		return result > 0;
	}

}