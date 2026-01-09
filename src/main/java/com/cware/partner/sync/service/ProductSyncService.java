package com.cware.partner.sync.service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cware.partner.common.code.Application;
import com.cware.partner.common.code.ExceptFapleSourcingCode;
import com.cware.partner.common.code.PaGroup;
import com.cware.partner.common.code.SaleGb;
import com.cware.partner.common.code.SortType;
import com.cware.partner.common.service.CodeService;
import com.cware.partner.common.service.CommonService;
import com.cware.partner.common.util.DateUtil;
import com.cware.partner.common.util.StringUtil;
import com.cware.partner.sync.domain.SyncResult;
import com.cware.partner.sync.domain.entity.Goods;
import com.cware.partner.sync.domain.entity.GoodsDt;
import com.cware.partner.sync.domain.entity.PaCdcGoods;
import com.cware.partner.sync.domain.entity.PaGoods;
import com.cware.partner.sync.domain.entity.PaGoodsDt;
import com.cware.partner.sync.domain.entity.PaGoodsSync;
import com.cware.partner.sync.domain.entity.PaGoodsTarget;
import com.cware.partner.sync.domain.entity.PaNoticeM;
import com.cware.partner.sync.domain.entity.PaNoticeTarget;
import com.cware.partner.sync.domain.entity.PaPromoTarget;
import com.cware.partner.sync.domain.entity.PaTdealEvent;
import com.cware.partner.sync.filter.CoupangFilter;
import com.cware.partner.sync.filter.EbayFilter;
import com.cware.partner.sync.filter.FapleFilter;
import com.cware.partner.sync.filter.GoodsFilter;
import com.cware.partner.sync.filter.GoodsPriceFilter;
import com.cware.partner.sync.filter.HalfFilter;
import com.cware.partner.sync.filter.InterparkFilter;
import com.cware.partner.sync.filter.KakaoFilter;
import com.cware.partner.sync.filter.LotteonFilter;
import com.cware.partner.sync.filter.NaverFilter;
import com.cware.partner.sync.filter.PartnerFilter;
import com.cware.partner.sync.filter.QeenFilter;
import com.cware.partner.sync.filter.Sk11stFilter;
import com.cware.partner.sync.filter.SsgFilter;
import com.cware.partner.sync.filter.TdealFilter;
import com.cware.partner.sync.filter.TmonFilter;
import com.cware.partner.sync.filter.WempFilter;
import com.cware.partner.sync.repository.GoodsPriceRepository;
import com.cware.partner.sync.repository.GoodsRepository;
import com.cware.partner.sync.repository.PaCopnGoodsRepository;
import com.cware.partner.sync.repository.PaCustShipCostRepository;
import com.cware.partner.sync.repository.PaGoodsDtRepository;
import com.cware.partner.sync.repository.PaGoodsRepository;
import com.cware.partner.sync.repository.PaGoodsTargetRepository;
import com.cware.partner.sync.repository.PaNoticeApplyRepository;
import com.cware.partner.sync.repository.PaNoticeTargetRepository;
import com.cware.partner.sync.repository.PaPromoTargetRepository;
import com.cware.partner.sync.repository.PaTargetExceptRepository;
import com.cware.partner.sync.repository.PaTdealEventRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductSyncService {

	@Autowired
	GoodsRepository goodsRepository;

	@Autowired
	GoodsPriceRepository goodsPriceRepository;

	@Autowired
	PaCustShipCostRepository shipCostRepository;

	@Autowired
	PaPromoTargetRepository promoRepository;

	@Autowired
	PaGoodsTargetRepository paGoodsTargetRepository;

	@Autowired
	PaTargetExceptRepository paTargetExceptRepository;

	@Autowired
	PaGoodsRepository paGoodsRepository;
	@Autowired
	PaGoodsDtRepository paGoodsDtRepository;
	
	@Autowired
	private PaNoticeApplyRepository noticeApplyRepository;
	
	@Autowired
	private PaNoticeTargetRepository noticeTargetRepository;
	
	@Autowired
	private PaCopnGoodsRepository paCopnGoodsRepository;
	
	@Autowired
	private CommonService commonRepository;
	
	@Autowired
	CodeService codeService;

	@Autowired
	CommonService commonService;

	@Autowired
	GoodsFilter goodsFilter;
	@Autowired
	GoodsPriceFilter goodsPriceFilter;

	@Autowired
	PartnerFilter partnerFilter;
	@Autowired
	PartnerSyncService partnerSyncService;

	@Autowired
	Sk11stFilter sk11stFilter;
	@Autowired
	Sync11stService sync11stService;

	@Autowired
	EbayFilter ebayFilter;
	@Autowired
	SyncEbayService syncEbayService;

	@Autowired
	NaverFilter naverFilter;
	@Autowired
	SyncNaverService syncNaverService;

	@Autowired
	CoupangFilter coupangFilter;
	@Autowired
	SyncCoupangService syncCoupangService;

	@Autowired
	LotteonFilter lotteonFilter;
	@Autowired
	SyncLotteonService syncLotteonService;

	@Autowired
	WempFilter wempFilter;
	@Autowired
	SyncWempService syncWempService;

	@Autowired
	InterparkFilter interparkFilter;
	@Autowired
	SyncInterparkService syncInterparkService;

	@Autowired
	TmonFilter tmonFilter;
	@Autowired
	SyncTmonService syncTmonService;

	@Autowired
	SsgFilter ssgFilter;
	@Autowired
	SyncSsgService syncSsgService;

	@Autowired
	KakaoFilter kakaoFilter;

	@Autowired
	HalfFilter halfFilter;
	
	@Autowired
	TdealFilter tdealFilter;
	
	@Autowired
	FapleFilter fapleFilter;
	
	@Autowired
	QeenFilter qeenFilter;

	@Autowired
	SyncKakaoService syncKakaoService;

	@Autowired
	SyncHalfService syncHalfService;
	
	@Autowired
	SyncTdealService syncTdealService;
	
	@Autowired
	SyncFapleService syncFapleService;
	
	@Autowired
	private PaTdealEventRepository paTdealEventRepository;

	@Autowired
	SyncQeenService syncQeenService;

	// 동기화할 제휴사그룹코드 목록
	@Value("${partner.sync.pa-groups}")
	List<String> PA_GROUPS;

    @Async
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public CompletableFuture<PaGoodsSync> asyncService(PaCdcGoods target, long goodsSyncNo) {
		log.info("========== product sync start ({} - {}) ========== ", goodsSyncNo,  target.getGoodsCode());
		PaGoodsSync sync = syncProduct(target.getGoodsCode(), goodsSyncNo, target.getRanking());
		log.info("========== product sync end ({} - {}) ========== ", goodsSyncNo,  target.getGoodsCode());
		return CompletableFuture.completedFuture(sync);
	}

	@Transactional
	public PaGoodsSync syncProduct(String goodsCode) {
		return syncProduct(goodsCode, 0, 0);
	}

	@Transactional
	public PaGoodsSync syncProduct(String goodsCode, long goodsSyncNo, int ranking) {
		PaGoodsSync sync = new PaGoodsSync();
		sync.setGoodsSyncNo(goodsSyncNo);
		sync.setSyncGoodsCode(goodsCode);
		sync.setTargetCnt(1);
		sync.setFilterPaGroup(new ArrayList<String>());

		try {

			log.info("동기화 시작====> {} ", goodsCode);

			// 상품조회
			Goods goods = goodsRepository.getById(goodsCode);
			goods.setGoodsSyncNo(goodsSyncNo);

			goods.setTargetList(paGoodsTargetRepository.findByGoodsCode(goods.getGoodsCode(), PA_GROUPS));
						
			// 쿠팡 구매옵션재입점 처리 (target 없는 경우)
			copnAttriForSaleTarget(goods, goods.getTargetList());			

//			log.info("제휴: {}  ", goods.getTargetList());

			if  (goods.getTargetList().size() > 0) {

				// 공통 Filter수행
				if (applyFilter(goods)) {

					// 제휴사별 필터 처리후 제휴사 수만큼 동기화 수행
					syncData(goods, sync, ranking);

				} else {

					sync.setFilterCnt(goods.getTargetList().size());

					// 이미 입점 상태인데 필터링 제외된 경우 판매 중단 처리
					// 전체 제휴사 판매중지처리
					stopSaleAll(
							goods.getTargetList(), goods.isExcept(), goods.getExceptNote(), goods.isDiscard(), sync);
				}
			}

			if (sync.getProcCnt() == 0) sync.setProcCnt(1);
		} catch (EntityNotFoundException e) {
			log.info("존재하지 않은 상품코드입니다. {}", goodsCode);
			throw e;
		} catch (Exception e) {
			log.error("상품동기화실패 {}", goodsCode, e);
			throw e;
		}

		return sync;
	}

	private boolean applyFilter(Goods goods) {

		// 제휴연동 제외
		goods.setTargetExcept(paTargetExceptRepository.findTargetExcept(goods.getGoodsCode(), goods.getEntpCode(),
				goods.getBrandCode(), goods.getSourcingMedia()));

		// 배송비조회
		goods.setPaCustShipCost(shipCostRepository.getCustShipCost(goods.getShipEntpCode(), goods.getShipCostCode()));

		if (!goodsFilter.apply(goods)) return false;

		// 상품가격조회
		goods.setGoodsPrice(goodsPriceRepository.findApplyGoodsPrice(goods.getGoodsCode()));
		if (!goodsPriceFilter.apply(goods))	return false;

		// 중고상품 예외 업체 확인
		goods.setUsedEntpCnt(paTargetExceptRepository.countUsedEntp(goods.getGoodsCode()));
		
		return true;
	}

	// 제휴사별 동기화
	// 공통 입점 조건이 유효한 경우에 해당 로직 수행
	private int syncData(Goods goods, PaGoodsSync sync, int ranking) {

		List<PaGoodsTarget> targetList = goods.getTargetList();

		// 프로모션대상 설정
		loadPromoTarget(goods);

		// TPAGOODS 생성/변경
		PaGoods paGoods = syncPaGoods(goods, ranking);
		syncPaGoodsDt(goods.getGoodsDtList());

		// 업체휴무일데이터 공지사항 타겟 등록
		syncEntpHoliday(goods);
		
		int syncCnt = 0;
		boolean isEbay = false;
		PaGoodsTarget syncEbayTarget = null;

		List<CompletableFuture<SyncResult>> futures = new ArrayList<>();

		for (PaGoodsTarget target : targetList) {

			target.setPartnerBase(codeService.getPartnerBase(target.getPaCode()));
			target.setGoodsSyncNo(goods.getGoodsSyncNo());

			// 제외처리 등 제휴사별 필터
			if (!partnerFilter.apply(goods, target)) {
				sync.setFilterCnt(sync.getFilterCnt() + 1);
				stopSale(target, goods.isDiscard(), sync);
				continue;
			}

			// 레거시에서는 제휴사별 필터의 경우 실패하더라도 변경동기화시 판매중지 처리를 하지 않음.
			// 개선된 동기화에서는 제휴사 동기화 수행 중 필터처리되면 판매중지 처리
			// 제휴사별 동기화
			try {
				if (PaGroup.SK11ST.code().equals(target.getPaGroupCode())) {
					// 11번가 제약조건 체크
					if (sk11stFilter.apply(goods, target)) {
						futures.add(sync11stService.asyncService(goods, target));
					} else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				} else if (PaGroup.GMARKET.code().equals(target.getPaGroupCode())
						|| PaGroup.AUCTION.code().equals(target.getPaGroupCode())) {
					// 이베이 제약조건 체크
					if (ebayFilter.apply(goods, target)) {
						if (isEbay) {
							// 지마켓/옥션 동시 처리시 뒤에 타겟은 비동기 처리 완료 후 처리
							syncEbayTarget = target;
							continue;
						}
						isEbay = true;
						futures.add(syncEbayService.asyncService(goods, target));
					} else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				} else if (PaGroup.NAVER.code().equals(target.getPaGroupCode())) {
					// 네이버 제약조건 체크
					if (naverFilter.apply(goods, target)) {
						futures.add(syncNaverService.asyncService(goods, target));
					} else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				} else if (PaGroup.COUPANG.code().equals(target.getPaGroupCode())) {
					// 쿠팡 제약조건 체크
					if (coupangFilter.apply(goods, target)) {
						futures.add(syncCoupangService.asyncService(goods, target));
					} else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				} else if (PaGroup.WEMP.code().equals(target.getPaGroupCode())) {
					// 위메프 제약조건 체크
					if (wempFilter.apply(goods, target)) {
						futures.add(syncWempService.asyncService(goods, target));
					} else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				} else if (PaGroup.INTERPARK.code().equals(target.getPaGroupCode())) {
					// 인터파크 제약조건 체크
					if (interparkFilter.apply(goods, target)) {
						futures.add(syncInterparkService.asyncService(goods, target));
					} else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				} else if (PaGroup.LOTTEON.code().equals(target.getPaGroupCode())) {
					// 롯데온 제약조건 체크
					if (lotteonFilter.apply(goods, target)) {
						futures.add(syncLotteonService.asyncService(goods, target));
					} else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				} else if (PaGroup.TMON.code().equals(target.getPaGroupCode())) {
					// 티몬 제약조건 체크
					if (tmonFilter.apply(goods, target)) {
						futures.add(syncTmonService.asyncService(goods, target));
					} else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				} else if (PaGroup.SSG.code().equals(target.getPaGroupCode())) {
					// 쓱닷컴 제약조건 체크
					if (ssgFilter.apply(goods, target)) {
						futures.add(syncSsgService.asyncService(goods, target));
					} else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, goods.isDiscard(), sync); // 일시/영구중지
					}
				} else if (PaGroup.KAKAO.code().equals(target.getPaGroupCode())) {
					// 카카오쇼핑 제약조건 체크
					if (kakaoFilter.apply(goods, target)) {
						futures.add(syncKakaoService.asyncService(goods, target));
					} else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				} else if(PaGroup.HALF.code().equals(target.getPaGroupCode())) {
					// 하프클럽 제약조건 체크
					if (halfFilter.apply(goods, target)) {
						futures.add(syncHalfService.asyncService(goods, target));
					}else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				} else if(PaGroup.TDEAL.code().equals(target.getPaGroupCode())) {
					// 티딜 제약조건 체크
					if (tdealFilter.apply(goods, target)) {
						futures.add(syncTdealService.asyncService(goods, target));
					}else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				}else if(PaGroup.FAPLE.code().equals(target.getPaGroupCode())) {
					// 패션플러스 제약조건 체크
					if (fapleFilter.apply(goods, target)) {
						futures.add(syncFapleService.asyncService(goods, target));
					}else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				}else if(PaGroup.QEEN.code().equals(target.getPaGroupCode())) {
					// 퀸잇 제약조건 체크
					if (qeenFilter.apply(goods, target)) {
						futures.add(syncQeenService.asyncService(goods, target));
					}else {
						sync.setFilterCnt(sync.getFilterCnt() + 1);
						stopSale(target, sync);
					}
				}else {
					log.warn("연동 대상 제휴사가 존재하지 않습니다");
				}
			} catch (Exception e) {
				log.error("동기화실패 제휴사:{}, 상품:{} ", target.getPaCode(), goods.getGoodsCode(), e);
				sync.setProcCnt(-1);
			}

		}

		SyncResult result;
		PaGoodsTarget target;
		String salePaCode = paGoods.getSalePaCode();
		for(CompletableFuture<SyncResult> future : futures) {
			try {
				result = future.get();

				log.info("제휴사 동기화결과 {} ",  result);

				if (result.isSync()) syncCnt++;

				target = result.getTarget();

				// 동기화 중 필터 처리
				if (target.isExcept()) {
					sync.setFilterCnt(sync.getFilterCnt() + 1);
					stopSale(target, sync);
				} else {
					// 제휴연동사 코드 설정
					if (StringUtils.hasText(salePaCode)) {
						if (!salePaCode.contains(target.getPaCode()))  {
							salePaCode += "," + target.getPaCode();
						}
					} else {
						salePaCode = target.getPaCode();
					}
				}

				// 이베이 동기화인 경우 동시처리 타겟 동기방식으로 실행
				if (PaGroup.GMARKET.code().equals(target.getPaGroupCode())
						|| PaGroup.AUCTION.code().equals(target.getPaGroupCode())) {
					if (syncEbayTarget != null) {

						log.info("이베이 추가 동기화 {} {} {}", syncEbayTarget.getGoodsCode(), syncEbayTarget.getPaCode(),
								syncEbayTarget.getPaGroupCode());
						boolean isSync = syncEbayService.syncProduct(goods, syncEbayTarget);
						log.info("이베이 추가 동기화결과 {} {} {} {}", syncEbayTarget.getGoodsCode(), syncEbayTarget.getPaCode(),
								syncEbayTarget.getPaGroupCode(), isSync);
						if (isSync) syncCnt++;
						if (syncEbayTarget.isExcept()) {
							sync.setFilterCnt(sync.getFilterCnt() + 1);
							stopSale(syncEbayTarget, sync);
						} else {
							// 제휴연동사 코드 설정
							if (StringUtils.hasText(salePaCode)) {
								if (!salePaCode.contains(syncEbayTarget.getPaCode()))  {
									salePaCode += "," + syncEbayTarget.getPaCode();
								}
							} else {
								salePaCode = syncEbayTarget.getPaCode();
							}
						}
					}
				}
			} catch (Exception e) {
				log.error("제휴사별 동기화 오류: ", e);
				sync.setProcCnt(-1);
			}
		}

		paGoods.setSalePaCode(salePaCode);
		paGoodsRepository.save(paGoods);

		sync.setSyncCnt(syncCnt);

		return syncCnt;
	}

	private void syncEntpHoliday(Goods goods) {
		Timestamp currentDate = commonService.currentDate();

		List<PaNoticeM> noticeMList = noticeApplyRepository.selecEntpHolidayNotice(goods.getGoodsCode());

		List<PaNoticeTarget> noticeTargetList = noticeApplyRepository.selecNoticeTarget(goods.getGoodsCode());
		
		// goods 엔티티에 저장 할 임시 NoticeTarget Temp 생성
		List<PaNoticeTarget> noticeTargetListTemp = new ArrayList<PaNoticeTarget>();
		
		Optional<PaNoticeTarget> noticeTargetOptional;
		
		for (PaNoticeM noticeM : noticeMList) {
			noticeTargetOptional = noticeTargetList.stream().filter(noticeTarget -> noticeTarget.getNoticeNo().equals(noticeM.getNoticeNo())).findFirst();
			if(!noticeTargetOptional.isPresent()) {
				PaNoticeTarget paNoticeTarget = PaNoticeTarget.builder()
						.noticeNo(noticeM.getNoticeNo())
						.goodsCode(goods.getGoodsCode())
						.targetGb("00")
						.insertId(Application.ID.code()).insertDate(currentDate)
						.modifyId(Application.ID.code()).modifyDate(currentDate).build();
				noticeTargetRepository.saveAndFlush(paNoticeTarget);
				
				// NoticeTarget 생성 시, goods 엔티티에 임시저장
				noticeTargetListTemp.add(paNoticeTarget);
			}
		}		
		goods.setPaNoticeTarget(noticeTargetListTemp);
		
	}
	
	private void copnAttriForSaleTarget(Goods goods, List<PaGoodsTarget> targetList) {
		
		boolean isCopnExists = false;		
		List<PaGoodsTarget> targetListTemp = new ArrayList<PaGoodsTarget>();
		
		isCopnExists = targetList.stream().anyMatch(target -> "05".equals(target.getPaGroupCode()));
		
		// 필수구매옵션 등록되어지만, 쿠팡 target 데이터 없을 때, 쿠팡 target 데이터 생성
		if(!isCopnExists) {
			if ("N".equals(paCopnGoodsRepository.findCopnOptExceptYn(goods.getGoodsCode()))) { // 옵션등록 예외 상품 확인
				if ("Y".equals(paCopnGoodsRepository.findCopnOptRequiredYn(goods.getGoodsCode()))) { // 필수 옵션 필요로 하는지 확인
					if ("등록".equals(paCopnGoodsRepository.findCopnPurchaseOptionYn(goods.getGoodsCode()))) { // 필수 옵션 등록 유무 확인					
						
						Timestamp currentDate = commonRepository.currentDate();
						
						PaGoodsTarget paGoodsTarget = new PaGoodsTarget();
						paGoodsTarget.setPaGroupCode("05");
						paGoodsTarget.setGoodsCode(goods.getGoodsCode());		
						paGoodsTarget.setPaCode("01".equals(goods.getSourcingMedia()) ? "51" : "52");
						paGoodsTarget.setEventYn("0");
						paGoodsTarget.setInsertId(Application.ID.code());
						paGoodsTarget.setInsertDate(currentDate);
						paGoodsTarget.setModifyId(Application.ID.code());
						paGoodsTarget.setModifyDate(currentDate);						
						
						targetListTemp.add(paGoodsTarget);
						goods.getTargetList().addAll(targetListTemp);
					}
				}
			}	
		}
	}

	// 제휴상품(공통) 생성
	// 상품정보 변경동기화시 속성을 전부 비교하지 말고 수정일시 기준으로만 비교 후 처리.
	private PaGoods syncPaGoods(Goods goods, int ranking) {
		Optional<PaGoods> optional = paGoodsRepository.findById(goods.getGoodsCode());
		PaGoods paGoods = optional.isPresent() ? optional.get() : null;

		Timestamp procDate = commonService.currentDate();

		if (paGoods == null) {
			paGoods = PaGoods.builder().goodsCode(goods.getGoodsCode())
					.goodsName(goods.getGoodsName())
					.saleGb(goods.getSaleGb())
					.lmsdCode(goods.getLmsdCode())
					.makecoCode(goods.getMakecoCode())
					.brandName(goods.getBrandName())
					.originCode(goods.getOriginCode())
					.originName(codeService.getOriginName(goods.getOriginCode()))
					.taxYn(goods.getTaxYn())
					.taxSmallYn(goods.getTaxSmallYn())
					.adultYn(goods.getAdultYn())
					.orderMinQty(goods.getOrderMinQty())
					.orderMaxQty(goods.getOrderMaxQty())
					.custOrdQtyCheckTerm(goods.getCustOrdQtyCheckTerm())
					.doNotIslandDelyYn(goods.getDoNotIslandDelyYn())
					.entpCode(goods.getEntpCode())
					.shipManSeq(goods.getShipManSeq())
					.returnManSeq(goods.getReturnManSeq())
					.shipCostCode(goods.getShipCostCode())
					.avgDelyLeadtime(goods.getAvgDelyLeadtime())
					.saleStartDate(goods.getSaleStartDate())
					.saleEndDate(goods.getSaleEndDate())
					.keyword(goods.getKeyword())
					.collectYn(goods.getCollectYn())
					.orderCreateYn(goods.getGoodsAddInfo().getOrderCreateYn())
					.salePaCode(" ")
					.lastSyncDate(procDate)
					.lastDescribeSyncDate(procDate)
					.ranking(ranking)
					.insertDate(procDate)
					.insertId(Application.ID.code())
					.modifyDate(procDate)
					.modifyId(Application.ID.code())
					.installYn(goods.getInstallYn()).build();
			paGoodsRepository.save(paGoods);
		} else {
			
			// 티딜 행사상품의 경우(적용일시 기준으로 현재 진행중인 행사)
			// 기존 스토아 상품명이 변경되면 진행중인 티딜 행사를 종료한다.
			if (!StringUtil.compare(paGoods.getGoodsName(), goods.getGoodsName())) {
				
				// 진행중인 티딜행사(적용일시 기준) 여부 확인
                Optional<PaTdealEvent> optionalEvent = paTdealEventRepository.selectTdealSaleEvent(goods.getGoodsCode());
                
                if(optionalEvent.isPresent()) {
                	PaTdealEvent paTdealEvent = optionalEvent.get();
                	paTdealEvent.setGoodsEventName("(강제종료)" + paTdealEvent.getGoodsEventName());
                	paTdealEvent.setEventEdate(procDate); // 행사종료 처리
                	paTdealEventRepository.save(paTdealEvent);
                }
			}
			
			// 상품 변경여부 체크
			if (goods.getModifyDate().after(paGoods.getLastSyncDate()) || goods.getGoodsAddInfo().getModifyDate().after(paGoods.getLastSyncDate())) {
				if (!StringUtil.compare(paGoods.getGoodsName(), goods.getGoodsName())
						|| !StringUtil.compare(paGoods.getSaleGb(), goods.getSaleGb())
						|| !StringUtil.compare(paGoods.getLmsdCode(), goods.getLmsdCode())
						|| !StringUtil.compare(paGoods.getMakecoCode(), goods.getMakecoCode())
						|| !StringUtil.compare(paGoods.getBrandName(), goods.getBrandName())
						|| !StringUtil.compare(paGoods.getOriginCode(), goods.getOriginCode())
						|| !StringUtil.compare(paGoods.getTaxYn(), goods.getTaxYn())
						|| !StringUtil.compare(paGoods.getTaxSmallYn(), goods.getTaxSmallYn())
						|| !StringUtil.compare(paGoods.getAdultYn(), goods.getAdultYn())
						|| paGoods.getOrderMinQty() != goods.getOrderMinQty()
						|| paGoods.getOrderMaxQty() != goods.getOrderMaxQty()
						|| paGoods.getCustOrdQtyCheckTerm() != goods.getCustOrdQtyCheckTerm()
						|| paGoods.getAvgDelyLeadtime() != goods.getAvgDelyLeadtime()
						|| !StringUtil.compare(paGoods.getDoNotIslandDelyYn(), goods.getDoNotIslandDelyYn())
						|| !StringUtil.compare(paGoods.getEntpCode(), goods.getEntpCode())
						|| !StringUtil.compare(paGoods.getShipManSeq(), goods.getShipManSeq())
						|| !StringUtil.compare(paGoods.getReturnManSeq(), goods.getReturnManSeq())
						|| !StringUtil.compare(paGoods.getShipCostCode(), goods.getShipCostCode())
						|| !StringUtil.compare(paGoods.getKeyword(), goods.getKeyword())
						|| !StringUtil.compare(paGoods.getCollectYn(), goods.getCollectYn())
						|| !StringUtil.compare(paGoods.getOrderCreateYn(), goods.getGoodsAddInfo().getOrderCreateYn())
						|| !DateUtil.compare(paGoods.getSaleStartDate(), goods.getSaleStartDate())
						|| !DateUtil.compare(paGoods.getSaleEndDate(), goods.getSaleEndDate())
						|| !StringUtil.compare(paGoods.getInstallYn(), goods.getInstallYn())) {
					paGoods.setGoodsName(goods.getGoodsName());
					paGoods.setSaleGb(goods.getSaleGb());
					paGoods.setLmsdCode(goods.getLmsdCode());
					paGoods.setMakecoCode(goods.getMakecoCode());
					paGoods.setBrandName(goods.getBrandName());
					paGoods.setOriginCode(goods.getOriginCode());
					paGoods.setOriginName(codeService.getOriginName(goods.getOriginCode()));
					paGoods.setTaxYn(goods.getTaxYn());
					paGoods.setTaxSmallYn(goods.getTaxSmallYn());
					paGoods.setAdultYn(goods.getAdultYn());
					paGoods.setOrderMinQty(goods.getOrderMinQty());
					paGoods.setOrderMaxQty(goods.getOrderMaxQty());
					paGoods.setCustOrdQtyCheckTerm(goods.getCustOrdQtyCheckTerm());
					paGoods.setDoNotIslandDelyYn(goods.getDoNotIslandDelyYn());
					paGoods.setEntpCode(goods.getEntpCode());
					paGoods.setShipManSeq(goods.getShipManSeq());
					paGoods.setReturnManSeq(goods.getReturnManSeq());
					paGoods.setShipCostCode(goods.getShipCostCode());
					paGoods.setAvgDelyLeadtime(goods.getAvgDelyLeadtime());
					paGoods.setSaleStartDate(goods.getSaleStartDate());
					paGoods.setSaleEndDate(goods.getSaleEndDate());
					paGoods.setKeyword(goods.getKeyword());
					paGoods.setCollectYn(goods.getCollectYn());
					paGoods.setOrderCreateYn(goods.getGoodsAddInfo().getOrderCreateYn());
					paGoods.setLastSyncDate(procDate);
					paGoods.setModifyId(Application.ID.code());
					paGoods.setModifyDate(procDate);
					paGoods.setDirty(true);
					paGoods.setInstallYn(goods.getInstallYn());
					paGoodsRepository.save(paGoods);
				}
			}

			// 기술서 변경여부 체크
			if (goods.getDescribeModifyDate().after(paGoods.getLastDescribeSyncDate())) {
				paGoods.setLastDescribeSyncDate(procDate);
				paGoods.setModifyId(Application.ID.code());
				paGoods.setModifyDate(procDate);
				paGoods.setDescribeDirty(true);
				paGoodsRepository.save(paGoods);
			}

			// 랭킹 변경여부 체크
			if (ranking > 0 && paGoods.getRanking() != ranking) {
				paGoods.setModifyId(Application.ID.code());
				paGoods.setModifyDate(procDate);
				paGoods.setRanking(ranking);
				paGoodsRepository.save(paGoods);
			}

		}

		goods.setPaGoods(paGoods);

		return paGoods;

	}

	private boolean syncPaGoodsDt(List<GoodsDt> list) {

		Timestamp procDate = commonService.currentDate();

		// TPAGOODSDT 동기화
		for (GoodsDt goodsDt : list) {
			PaGoodsDt paGoodsDt = goodsDt.getPaGoodsDt();

			String infoKind = ("000".equals(goodsDt.getColorCode()) ? "" : "색상/")
					+ ("000".equals(goodsDt.getSizeCode()) ? "" : "사이즈/")
					+ ("000".equals(goodsDt.getPatternCode()) ? "" : "무늬/")
					+ ("000".equals(goodsDt.getFormCode()) ? "" : "형태/")
					+ (StringUtils.hasLength(goodsDt.getOtherText()) ? "기타" : "");

			if (!StringUtils.hasLength(infoKind)) {
				infoKind = "없음";
			}

			if (paGoodsDt == null) {
				if (!SaleGb.EOS.code().equals(goodsDt.getSaleGb())) {

					paGoodsDt = PaGoodsDt.builder().goodsCode(goodsDt.getGoodsCode())
							.goodsdtCode(goodsDt.getGoodsdtCode())
							.goodsdtInfo(goodsDt.getGoodsdtInfo())
							.goodsdtInfoKind(infoKind)
							.saleGb(goodsDt.getSaleGb())
							.sortType(SortType.REGISTERED.code())
							.insertDate(procDate)
							.insertId(Application.ID.code())
							.modifyDate(procDate)
							.modifyId(Application.ID.code()).build();
					paGoodsDtRepository.saveAndFlush(paGoodsDt);
					goodsDt.setPaGoodsDt(paGoodsDt);
				}
			} else if (goodsDt.getModifyDate().after(paGoodsDt.getModifyDate())) {
				paGoodsDt.setGoodsdtInfo(goodsDt.getGoodsdtInfo());
				paGoodsDt.setGoodsdtInfoKind(infoKind);
				paGoodsDt.setSaleGb(goodsDt.getSaleGb());
				paGoodsDt.setModifyId(Application.ID.code());
				paGoodsDt.setModifyDate(procDate);
				paGoodsDtRepository.saveAndFlush(paGoodsDt);
			}


		}

		return true;
	}


	// 프로모션 대상 설정
	public void loadPromoTarget(Goods goods) {
		//프로모션 연동 제외 체크(업체, 브랜드 단위 조회)
		goods.setPromoEntpBrandExcept( promoRepository.findTargetExceptEntpNBrand(goods.getGoodsCode()));
		if(goods.getPromoEntpBrandExcept() != null && "1".equals(goods.getPromoEntpBrandExcept().getPaGroupCodeAllYn())) return;
		//프로모션 연동 제외 체크 (상품단위)
		goods.setPromoTargetExcept(promoRepository.findTargetExcept(goods.getGoodsCode()));
		if(goods.getPromoTargetExcept() != null && "1".equals(goods.getPromoTargetExcept().getPaGroupCodeAllYn())
				&& goods.getPromoTargetExcept().getPaExceptMargin() >= 99999999) return; //마진금액을 입력하지 않은경우 99999999값으로 세팅

		//프로모션(PromoM, Promotarget) 적재
		goods.setPaPromoTargetList(promoRepository.selectPromoTarget(goods.getGoodsCode()));

		for (PaPromoTarget promo : goods.getPaPromoTargetList()) {
			// 정률일때 1원단위 절삭 AMT_ROUND_POINT 이걸 굳이 해야하나 싶은데...
			if ("2".equals(promo.getAmtRateFlag())) {
				BigDecimal doRate = new BigDecimal(promo.getDoRate());	
				BigDecimal percentage = new BigDecimal(100);
				BigDecimal salePrice = new BigDecimal(goods.getGoodsPrice().getSalePrice());
				
				BigDecimal doRate_divide_percentage = doRate.divide(percentage);
				BigDecimal salePrice_multiply_doRate = salePrice.multiply(doRate_divide_percentage);
				
				double targetAmt = salePrice_multiply_doRate.setScale(-1, BigDecimal.ROUND_DOWN).doubleValue();
				
				promo.setDoAmt(promo.getLimitAmt() > 0 && targetAmt > promo.getLimitAmt() ? promo.getLimitAmt() : targetAmt);
				
				BigDecimal entpCost = new BigDecimal(promo.getEntpCost());
				BigDecimal doAmt 	= new BigDecimal(promo.getDoAmt());
				
				BigDecimal doRate_multiply_entpCost = doAmt.multiply((percentage.subtract(entpCost)).divide(percentage));
				
				promo.setOwnCost(doRate_multiply_entpCost.setScale(-1, BigDecimal.ROUND_DOWN).doubleValue());
				promo.setEntpCost(promo.getDoAmt() - promo.getOwnCost());
			}
			
			if("1".equals(promo.getDirectTargetYn())) { //티딜 최저가할인 프로모션
				promo.setDoAmt(promo.getGiftAmt());
			}

// 			해당 소스 부동소수점 버그로 오 계산되어 주석처리 2023.05.22 Leejy		
//			// 정률일때 1원단위 절삭 AMT_ROUND_POINT 이걸 굳이 해야하나 싶은데...
//			if ("2".equals(promo.getAmtRateFlag())) {
//				BigDecimal calcAmt = new BigDecimal(goods.getGoodsPrice().getSalePrice() * promo.getDoRate() / 100);
//				double targetAmt = calcAmt.setScale(-1, BigDecimal.ROUND_DOWN).doubleValue();
//				promo.setDoAmt(
//						promo.getLimitAmt() > 0 && targetAmt > promo.getLimitAmt() ? promo.getLimitAmt() : targetAmt);
//
//				calcAmt = new BigDecimal(promo.getDoAmt() * ((100 - promo.getEntpCost()) / 100));
//				promo.setOwnCost(calcAmt.setScale(-1, BigDecimal.ROUND_DOWN).doubleValue());
//				promo.setEntpCost(promo.getDoAmt() - promo.getOwnCost());
//			}
			
		}
	}
	/*
	// 프로모션 대상 설정
	private void loadPromoTarget(Goods goods) {
		//프로모션 연동 제외 체크
		goods.setPromoTargetExcept(promoRepository.findTargetExcept(goods.getGoodsCode()));
		if (goods.getPromoTargetExcept() == null || "0".equals(goods.getPromoTargetExcept().getPaGroupCodeAllYn())) { // 전체프로모션연동제외가 아닌경우
			// 프로모션 조회
			goods.setPaPromoTargetList(promoRepository.selectPromoTarget(goods.getGoodsCode()));

			for (PaPromoTarget promo : goods.getPaPromoTargetList()) {


				// 정률일때 1원단위 절삭 AMT_ROUND_POINT 이걸 굳이 해야하나 싶은데...
				if ("2".equals(promo.getAmtRateFlag())) {
					BigDecimal calcAmt = new BigDecimal(goods.getGoodsPrice().getSalePrice() * (promo.getDoRate() / 100));
					double targetAmt = calcAmt.setScale(-1, BigDecimal.ROUND_DOWN).doubleValue();
					promo.setDoAmt(
							promo.getLimitAmt() > 0 && targetAmt > promo.getLimitAmt() ? promo.getLimitAmt() : targetAmt);

					calcAmt = new BigDecimal(promo.getDoAmt() * ((100 - promo.getEntpCost()) / 100));
					promo.setOwnCost(calcAmt.setScale(-1, BigDecimal.ROUND_DOWN).doubleValue());
					promo.setEntpCost(promo.getDoAmt() - promo.getOwnCost());
				}
			}
		}
	}*/

	// 전체 제휴사 판매중단처리
	/**
	 * @param targetList
	 * @param isExcept
	 * @param exceptNote
	 * @param isDiscard 영구중단여부
	 * @param sync
	 */
	private void stopSaleAll(List<PaGoodsTarget> targetList, boolean isExcept, String exceptNote, boolean isDiscard, PaGoodsSync sync) {
		for (PaGoodsTarget target : targetList) {
			target.setGoodsSyncNo(sync.getGoodsSyncNo());
			target.setExcept(isExcept);
			target.setExceptNote(exceptNote);
			stopSale(target, isDiscard, sync);
		}

	}

	// 제휴사별 판매중지
	private void stopSale(PaGoodsTarget target, PaGoodsSync sync) {
		stopSale(target, false, sync);
	}

	private void stopSale(PaGoodsTarget target, boolean isDiscard, PaGoodsSync sync) {
		if (target.getPaSaleGb() == null) {
			if(target.isExcept()) { // 타겟팅에서 제외
				paGoodsTargetRepository.delete(target);
				log.info("타겟팅 제외 {} {} {} ", target.getGoodsCode(), target.getPaCode(), target.getPaGroupCode());
			}
			return;
		}

		boolean isStop;

		if (PaGroup.SK11ST.code().equals(target.getPaGroupCode())) {
			// 11번가
			isStop = sync11stService.stopSale(target);
		} else if (PaGroup.GMARKET.code().equals(target.getPaGroupCode())
				|| PaGroup.AUCTION.code().equals(target.getPaGroupCode())) {
			// 이베이
			isStop = syncEbayService.stopSale(target);
		} else if (PaGroup.NAVER.code().equals(target.getPaGroupCode())) {
			// 네이버
			isStop = syncNaverService.stopSale(target);
		} else if (PaGroup.COUPANG.code().equals(target.getPaGroupCode())) {
			// 쿠팡
			isStop = syncCoupangService.stopSale(target);
		} else if (PaGroup.WEMP.code().equals(target.getPaGroupCode())) {
			// 위메프
			isStop = syncWempService.stopSale(target);
		} else if (PaGroup.INTERPARK.code().equals(target.getPaGroupCode())) {
			// 인터파크
			isStop = syncInterparkService.stopSale(target);
		} else if (PaGroup.LOTTEON.code().equals(target.getPaGroupCode())) {
			// 롯데온
			isStop = syncLotteonService.stopSale(target);
		} else if (PaGroup.TMON.code().equals(target.getPaGroupCode())) {
			// 티몬
			isStop = syncTmonService.stopSale(target);
		} else if (PaGroup.SSG.code().equals(target.getPaGroupCode())) {
			// 쓱닷컴 (일시/영구중단)
			isStop = syncSsgService.stopSale(target, isDiscard);
		} else if (PaGroup.KAKAO.code().equals(target.getPaGroupCode())) {
			// 카카오쇼핑
			isStop = syncKakaoService.stopSale(target);
		} else if (PaGroup.HALF.code().equals(target.getPaGroupCode())) {
			// 하프클럽
			isStop = syncHalfService.stopSale(target);
		} else if (PaGroup.TDEAL.code().equals(target.getPaGroupCode())) {
			// 티딜
			isStop = syncTdealService.stopSale(target);
		} else if (PaGroup.FAPLE.code().equals(target.getPaGroupCode())) {
			// 패션플러스
			isStop = syncFapleService.stopSale(target);
		} else if (PaGroup.QEEN.code().equals(target.getPaGroupCode())) {
			// 퀸잇
			isStop = syncQeenService.stopSale(target);
		} else {
			log.warn("판매중지할 수 있는 제휴사가 아닙니다");
			return;
		}

		if (isStop)	sync.setStopCnt(sync.getStopCnt() + 1);

		sync.getFilterPaGroup().add(target.getPaGroupCode());

	}
}
