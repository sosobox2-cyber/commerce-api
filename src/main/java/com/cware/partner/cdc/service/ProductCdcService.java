package com.cware.partner.cdc.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import com.cware.partner.cdc.domain.entity.PaCdcSnapshot;
import com.cware.partner.cdc.repository.DescribeRepository;
import com.cware.partner.cdc.repository.EntpUserRepository;
import com.cware.partner.cdc.repository.GoodsDtRepository;
import com.cware.partner.cdc.repository.GoodsImageRepository;
import com.cware.partner.cdc.repository.GoodsPriceRepository;
import com.cware.partner.cdc.repository.GoodsRepository;
import com.cware.partner.cdc.repository.OfferRepository;
import com.cware.partner.cdc.repository.PaCdcGoodsTargetRepository;
import com.cware.partner.cdc.repository.PaCdcSnapshotRepository;
import com.cware.partner.cdc.repository.PaGoodsKindsMappingRepository;
import com.cware.partner.cdc.repository.PaNoticeRepository;
import com.cware.partner.cdc.repository.PaSourcingExceptInputRepository;
import com.cware.partner.cdc.repository.PaTargetExceptRepository;
import com.cware.partner.cdc.repository.PromoRepository;
import com.cware.partner.cdc.repository.ShipCostDtRepository;
import com.cware.partner.cdc.repository.StockRepository;
import com.cware.partner.cdc.repository.PaTdealEventRepository;
import com.cware.partner.common.code.CdcReason;
import com.cware.partner.common.domain.GoodsTarget;
import com.cware.partner.common.domain.entity.PaCdcReason;
import com.cware.partner.common.repository.CommonRepository;
import com.cware.partner.common.repository.PaCdcReasonRepository;
import com.cware.partner.common.service.CodeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductCdcService {

	@Autowired
	private CodeService codeService;

	@Autowired
	private CommonRepository commonRepository;

	@Autowired
	private PaCdcSnapshotRepository cdcSnapshotRepository;

	@Autowired
	private PaCdcGoodsTargetRepository paCdcGoodsTargetRepository;

	@Autowired
	private PaCdcReasonRepository cdcReasonRepository;

	@Autowired
	private PaTargetExceptRepository paTargetExceptRepository;

	@Autowired
	private OfferRepository offerRepository;

	@Autowired
	private GoodsRepository goodsRepository;

	@Autowired
	private GoodsDtRepository goodsDtRepository;

	@Autowired
	private DescribeRepository describeRepository;

	@Autowired
	private GoodsImageRepository goodsImageRepository;

	@Autowired
	private PromoRepository promoRepository;

	@Autowired
	private GoodsPriceRepository goodsPriceRepository;

	@Autowired
	private ShipCostDtRepository shipCostDtRepository;

	@Autowired
	private EntpUserRepository entpUserRepository;

	@Autowired
	private PaNoticeRepository paNoticeRepository;

	@Autowired
	private StockRepository stockRepository;

	@Autowired
	private PaSourcingExceptInputRepository exceptInputRepository;
	
	@Autowired
	private PaTdealEventRepository paTdealEventRepository;

	@Autowired
	private PaGoodsKindsMappingRepository paGoodsKindsMappingRepository;
	// 페이징 사이즈
	@Value("${partner.cdc.page-size}")
	public int PAGE_SIZE;

	// 캡처할 제휴사그룹코드 목록
	@Value("${partner.cdc.pa-groups}")
	List<String> PA_GROUPS;
	
	// 카테고리매핑 캡처할 제휴사그룹코드 목록
	@Value("${partner.cdc.pa-groups-category}")
	List<String> PA_GROUPS_C;
	
	//이베이 그룹코드 목록
	@Value("${partner.cdc.ebay-pa-groups}")
	List<String> EBAY_PA_GROUPS;

	public void executeProductCdc() {

		// CDC 큐 생성

		log.info("*******Partner Product CDC service start*******");
		// 상품입점 대상
		createCdcGoods(CdcReason.SALE_START);

		// 사용자 임의 판매재개
		createCdcGoods(CdcReason.MANUAL_START);

		// 행사입점 (상품동기화시 마진체크에서 필터링 된 상품에 대해서 구간을 정해 입점)
		createCdcGoods(CdcReason.EVENT_START);

		// 행사재입점
		createCdcGoods(CdcReason.EVENT_FORSALE);

		// 제외소싱상품 예외입점 (입점이 제외된 소싱코드 중 특정 상품만 입점
		createCdcGoods(CdcReason.SOURCING_EXCEPT_START);

		// 판매종료
		createCdcGoods(CdcReason.SALE_END);

		// 행사종료
		createCdcGoods(CdcReason.EVENT_END);
		createCdcGoods(CdcReason.EVENT_STATUS);

		// 입점제외
		createCdcGoods(CdcReason.TARGET_EXCEPT);
		createCdcGoods(CdcReason.ENTP_EXCEPT);
		createCdcGoods(CdcReason.BRAND_EXCEPT);

		// 상품변경/판매상태변경
		createCdcGoods(CdcReason.GOODS_MODIFY);

		// 가격적용
		createCdcGoods(CdcReason.PRICE_APPLY);
		
		// 배송비 변경
		createCdcGoods(CdcReason.SHIP_COST_APPLY);

		// 단품정보변경
		createCdcGoods(CdcReason.GOODSDT_MODIFY);

		// 정보고시변경
		createCdcGoods(CdcReason.OFFER_MODIFY);

		// 기술서변경
		createCdcGoods(CdcReason.DESCRIBE_MODIFY);

		// 상품이미지변경
		createCdcGoods(CdcReason.IMAGE_MODIFY);
		createCdcGoods(CdcReason.INFO_IMAGE_MODIFY);

		// 업체주소변경: 업체회수지 (PaEntpSlip기준 동기화는 별도 서비스로 구현)
		createCdcGoods(CdcReason.ENTPUSER_MODIFY);

		// 공지사항변경
		createCdcGoods(CdcReason.NOTICE_APPLY);
		createCdcGoods(CdcReason.NOTICE_END);
		createCdcGoods(CdcReason.NOTICE_MODIFY);
		createCdcGoods(CdcReason.NOTICE_EXCEPT);
		
		// 업체휴무일적용
		createCdcGoods(CdcReason.ENTP_HOLIDAY_APPLY);
				
		// 카테고리 매핑 변경(for 11번가, 위메프, 티몬, 카카오)
		createCdcGoods(CdcReason.CATEGORY_MODIFY);
		// 이베이 카테고리 매핑 변경
		createCdcGoods(CdcReason.EBAY_CATEGORY_MODIFY);
		
		// 티딜 옵션별 이미지 변경
		createCdcGoods(CdcReason.TDEAL_DTIMAGE_MODIFY);
		
		// 티딜 행사 상품명 변경  
		createCdcGoods(CdcReason.TDEAL_EVENT_APPLY);
		createCdcGoods(CdcReason.TDEAL_EVENT_END);
		
		// 프로모션최소마진행사적용
		createCdcGoods(CdcReason.PROMO_MARGIN_APPLY);
		createCdcGoods(CdcReason.PROMO_MARGIN_END);
		createCdcGoods(CdcReason.PROMO_MARGIN_STATUS);
		
		// 쿠팡 구매옵션재입점 처리(target 있는경우)
		createCdcGoods(CdcReason.COPN_ATTRI_FORSALE);
		// 쿠팡 구매옵션재입점 처리(target 없는경우)
		createCdcGoods(CdcReason.COPN_ATTRI_FORSALE_TARGET);

		log.info("*******Partner Product CDC service end*******");
	}
	
	public void executeStockCdc() {
		
		// CDC 큐 생성

		log.info("*******Partner Stock CDC service start*******");
	
		// 재고변경
		createCdcGoods(CdcReason.STOCK_APPLY);
		createCdcGoods(CdcReason.STOCK_CHANGE);		

		log.info("*******Partner Stock CDC service end*******");
	}
	
	public void executePromoCdc() {
		
		// CDC 큐 생성

		log.info("*******Partner Promo CDC service start*******");
	
		// 프로모션적용
		createCdcGoods(CdcReason.PROMO_APPLY);
		createCdcGoods(CdcReason.PROMO_END);
		createCdcGoods(CdcReason.PROMO_STATUS);

		createCdcGoods(CdcReason.PROMO_PA_EXCEPT);
		createCdcGoods(CdcReason.PROMO_PA_EXCEPT_ENTP);
		createCdcGoods(CdcReason.PROMO_PA_EXCEPT_BRAND);
		createCdcGoods(CdcReason.PROMO_PA_EXCEPT_END);
		createCdcGoods(CdcReason.PROMO_PA_EXCEPT_ENTP_END);
		createCdcGoods(CdcReason.PROMO_PA_EXCEPT_BRAND_END);

		createCdcGoods(CdcReason.PROMO_EXCEPT);

		log.info("*******Partner Promo CDC service end*******");
	}


	// CDC 수행
	public void createCdcGoods(CdcReason reason) {

		PaCdcReason cdcReason = codeService.getCdcReason(reason.code());
		if (!"1".equals(cdcReason.getUseYn()))
			return;

		Slice<GoodsTarget> slice = null;
		Pageable pageable = PageRequest.of(0, PAGE_SIZE);

		Timestamp currentDate = commonRepository.currentDate();

		PaCdcSnapshot cdcSnapshot = new PaCdcSnapshot();
		cdcSnapshot.setCdcReasonCode(reason.code());
		cdcSnapshot.setStartDate(currentDate);
		cdcSnapshotRepository.save(cdcSnapshot);

		log.info("상품 타겟팅 [{}({})] 이전캡처일시:{} Start....", reason.name(), cdcSnapshot.getCdcSnapshotNo(), cdcReason.getLastCdcDate());

		// 상품타겟팅
		while (true) {

			if (reason == CdcReason.SALE_START) {
				// 상품입점
				slice = goodsRepository.findGoodsStartTargetList(pageable, PA_GROUPS);
			} else if (reason == CdcReason.MANUAL_START) {
				// 사용자판매재개 (등록일시 갭이 발생하여 5초 이전부터 캡처)
				slice = goodsRepository.findManualStartTargetList(
						Timestamp.from(cdcReason.getLastCdcDate().toInstant().minusSeconds(5)),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.EVENT_START) {
				// 행사입점
				slice = goodsRepository.findGoodsEventStartTargetList(pageable, PA_GROUPS);
			} else if (reason == CdcReason.EVENT_FORSALE) {
				// 행사재입점
				slice = goodsRepository.findGoodsEventForSaleTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.SOURCING_EXCEPT_START) {
				// 제외소싱상품 예외입점
				slice = exceptInputRepository.findPaExceptStartGoodsList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.SALE_END) {
				// 상품판매종료
				slice = goodsRepository.findGoodsEndTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.EVENT_END) {
				// 행사종료
				slice = goodsRepository.findGoodsEventEndTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.EVENT_STATUS) {
				// 행사중단
				slice = goodsRepository.findGoodsEventStatusTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.TARGET_EXCEPT) {
				// 상품입점제외
				slice = paTargetExceptRepository.findPaTargetExceptGoodsList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.ENTP_EXCEPT) {
				// 업체입점제외
				slice = paTargetExceptRepository.findPaExceptEntpGoodsList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.BRAND_EXCEPT) {
				// 업체브랜드입점제외
				slice = paTargetExceptRepository.findPaExceptBrandGoodsList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.GOODS_MODIFY) {
				// 상품정보변경
				slice = goodsRepository.findGoodsModifyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PRICE_APPLY) {
				// 가격적용 (즉시 가격변경시 과거일시로 설정되는 경우가 있어 5초 이전부터 캡처)
				slice = goodsPriceRepository.findPriceApplyTargetList(
						Timestamp.from(cdcReason.getLastCdcDate().toInstant().minusSeconds(5)),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_APPLY) {
				// 프로모션적용
				slice = promoRepository.findPromoApplyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_END) {
				// 프로모션종료
				slice = promoRepository.findPromoEndTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_STATUS) {
				// 프로모션변경
				slice = promoRepository.findPromoStatusTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_PA_EXCEPT) {
				// 프로모션제휴제외(상품)
				slice = promoRepository.findPaPromoExceptTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_PA_EXCEPT_ENTP) {
				// 프로모션제휴제외(업체)
				slice = promoRepository.findPaPromoExceptEntpList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_PA_EXCEPT_BRAND) {
				// 프로모션제휴제외(브랜드)
				slice = promoRepository.findPaPromoExceptBrandList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_PA_EXCEPT_END) {
				// 프로모션제휴제외 종료(상품)
				slice = promoRepository.findPaPromoExceptEndTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_PA_EXCEPT_ENTP_END) {
				// 프로모션제휴제외 종료(업체)
				slice = promoRepository.findPaPromoExceptEndEntpList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_PA_EXCEPT_BRAND_END) {
				// 프로모션제휴제외 종료(브랜드) native
				slice = promoRepository.findPaPromoExceptEndBrandList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), PAGE_SIZE, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_EXCEPT) {
				// 프로모션제외 native
				slice = promoRepository.findPromoExceptTargetList(cdcReason.getLastCdcDate(), PA_GROUPS);
			} else if (reason == CdcReason.SHIP_COST_APPLY) {
				// 배송비변경 (즉시 적용일 반영시 과거일시로 설정되는 경우가 있어 5초 이전부터 캡처)
				slice = shipCostDtRepository.findShipCostApplyTargetList(
						Timestamp.from(cdcReason.getLastCdcDate().toInstant().minusSeconds(5)),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_MARGIN_APPLY) {
				// 프로모션최소마진행사적용
				slice = promoRepository.findPaPromoMarginAutoApplyList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_MARGIN_END) {
				// 프로모션최소마진행사종료
				slice = promoRepository.findPaPromoMarginAutoEndList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.PROMO_MARGIN_STATUS) {
				// 프로모션최소마진행사상태변경(중단)
				slice = promoRepository.findPaPromoMarginAutoStatusList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			}else if (reason == CdcReason.GOODSDT_MODIFY) {
				// 단품정보변경
				slice = goodsDtRepository.findGoodsDtModifyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.OFFER_MODIFY) {
				// 상품정보고시변경 native
				slice = offerRepository.findOfferModifyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), PAGE_SIZE, PA_GROUPS);
			} else if (reason == CdcReason.DESCRIBE_MODIFY) {
				// 기술서변경 native
				slice = describeRepository.findDescribeModifyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), PAGE_SIZE, PA_GROUPS);
			} else if (reason == CdcReason.IMAGE_MODIFY) {
				// 이미지변경
				slice = goodsImageRepository.findGoodsImageModifyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.INFO_IMAGE_MODIFY) {
				// 상세이미지변경 (for 이베이, 쿠팡)
				slice = goodsImageRepository.findGoodsInfoImageModifyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.ENTPUSER_MODIFY) {
				// 업체회수지변경
				slice = entpUserRepository.findEntpReturnModifyList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.NOTICE_APPLY) {
				// 공지사항적용
				slice = paNoticeRepository.findNoticeApplyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.NOTICE_END) {
				// 공지사항종료
				slice = paNoticeRepository.findNoticeEndTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.NOTICE_MODIFY) {
				// 공지사항변경
				slice = paNoticeRepository.findNoticeModifyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.NOTICE_EXCEPT) {
				// 공지사항제외
				slice = paNoticeRepository.findNoticeExceptTargetList(cdcSnapshot.getCdcSnapshotNo(), pageable,
						PA_GROUPS);
			} else if (reason == CdcReason.ENTP_HOLIDAY_APPLY) {
				// 업체휴무일적용
				slice = paNoticeRepository.findEntpHolidayApplyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.STOCK_APPLY) {
				// 재고적용
				slice = stockRepository.findStockApplyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.STOCK_CHANGE) {
				// 주문재고변경
				slice = stockRepository.findStockChangeTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.CATEGORY_MODIFY) {
				// 카테고리 매핑 변경(for 11번가, 위메프, 티몬, 카카오)
				slice = paGoodsKindsMappingRepository.findPaGoodsKindsMappingChangeTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS_C);
			} else if (reason == CdcReason.EBAY_CATEGORY_MODIFY) {
				// 이베이카테고리 매핑 변경
				slice = paGoodsKindsMappingRepository.findEbayGoodsKindsMappingChangeTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, EBAY_PA_GROUPS);
			} else if (reason == CdcReason.TDEAL_DTIMAGE_MODIFY) {
				// 티딜 옵션별 이미지 변경
				slice = goodsImageRepository.findTdealGoodsdtImageModifyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.TDEAL_EVENT_APPLY) {
				// 티딜 행사 적용
				slice = paTdealEventRepository.findTdeaEventApplyTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.TDEAL_EVENT_END) {
				// 티딜 행사 종료
				slice = paTdealEventRepository.findTdeaEventEndTargetList(cdcReason.getLastCdcDate(),
						cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.COPN_ATTRI_FORSALE) {
				// 쿠팡 구매옵션재입점 처리 (target 있는경우)
				slice = goodsRepository.findCopnAttriForSaleList(cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else if (reason == CdcReason.COPN_ATTRI_FORSALE_TARGET) {
				// 쿠팡 구매옵션재입점 처리 (target 없는경우)
				slice = goodsRepository.findCopnAttriForSaleTargetList(cdcSnapshot.getCdcSnapshotNo(), pageable, PA_GROUPS);
			} else {
			
				log.warn("Undefined CdcReason");
			}

			if (slice == null) break;

			createTarget(cdcReason, cdcSnapshot, slice);

			if (!slice.hasNext())
				break;

		}
		cdcSnapshot.setEndDate(commonRepository.currentDate());
		cdcSnapshotRepository.save(cdcSnapshot);
		cdcReason.setLastCdcDate(currentDate);
		cdcReasonRepository.save(cdcReason);

		log.info("상품 타겟팅 [{}({})] 마지막캡처일시:{} 타겟팅:{} 큐생성:{} End ==> {}s elapsed.", reason.name(),
				cdcSnapshot.getCdcSnapshotNo(), cdcReason.getLastCdcDate(), cdcSnapshot.getTargetCnt(),
				cdcSnapshot.getCdcCnt(), (cdcSnapshot.getEndDate().getTime() - currentDate.getTime()) / 1000);
	}

	// 큐생성
	private int createTarget(PaCdcReason cdcReason, PaCdcSnapshot cdcSnapshot, Slice<GoodsTarget> slice) {
		if (slice == null)
			return 0;

		List<GoodsTarget> targetList = slice.getContent();

		int cdcCnt = 0;

		log.info("타겟팅 추출[{}({})] 타겟:{}", cdcReason.getCdcEvent(), cdcSnapshot.getCdcSnapshotNo(), targetList.size());

		for (GoodsTarget target : targetList) {
			// 큐 생성
            try {
                if (paCdcGoodsTargetRepository.createCdcGoodsTarget(target, cdcReason,
                        cdcSnapshot.getCdcSnapshotNo()))
                    cdcCnt++;
            } catch (Exception e) {
                log.error("큐 생성 오류: ", e);
            }
		}

		cdcSnapshot.setTargetCnt(cdcSnapshot.getTargetCnt() + targetList.size());
		cdcSnapshot.setCdcCnt(cdcSnapshot.getCdcCnt() + cdcCnt);
		cdcSnapshotRepository.saveAndFlush(cdcSnapshot);
		log.info("cdc 큐 생성[{}({})] 큐:{}", cdcReason.getCdcEvent(), cdcSnapshot.getCdcSnapshotNo(), cdcCnt);

		return cdcCnt;
	}

}
