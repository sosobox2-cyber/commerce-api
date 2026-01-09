package com.cware.partner.coupang.service;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.cware.partner.common.code.Application;
import com.cware.partner.common.service.CodeService;
import com.cware.partner.common.service.CommonService;
import com.cware.partner.common.util.StringUtil;
import com.cware.partner.coupang.domain.ProductStatus;
import com.cware.partner.coupang.repository.PaCopnGoodsRepository;
import com.cware.partner.sync.domain.entity.PaCopnGoods;

import lombok.extern.slf4j.Slf4j;

/**
 * 쿠팡 상태 불일치건 처리
 */
@Slf4j
@Service
public class RejectProductService {

	@Autowired
	PaCopnGoodsRepository copnGoodsRepository;

	@Autowired
	private ProductApiService productApiService;

	@Autowired
	CommonService commonService;

	@Autowired
	CodeService codeService;

	@Autowired
	RestTemplate restTemplate;

	// 페이징 사이즈
	@Value("${partner.coupang.page-size}")
	int PAGE_SIZE;

	/**
	 * 입점완료/판매중 상태이나 제휴사에는 승인반려 상태인 경우 판매중지/반려사유 업데이트
	 *
	 */
	public void updateRejectProducts() {

		int targetCnt = 0;
		int procCnt = 0;
		int failCnt = 0;

		Slice<PaCopnGoods> slice = null;

        Pageable pageable = PageRequest.of(0, PAGE_SIZE);

		Timestamp startDate = commonService.currentDate();
		log.info("쿠팡 상태불일치 타겟팅시작일시:{} Start....", startDate);

		while (true) {

			// 판매진행/입점완료 상태이나 제휴상품상태가 승인반려건 추출 (2주전 입점요청 기준)
			slice = copnGoodsRepository.findRejectTargetList(pageable);

			List<PaCopnGoods> targetList = slice.getContent();

			log.info("타겟팅 추출:{}", targetList.size());

			for (PaCopnGoods copnGoods : targetList) {
				ProductStatus productStatus = productApiService.getRecentChangeStatus(copnGoods.getSellerProductId(), copnGoods.getPaCode());
				log.info("No.{} 상품:{} {}", ++targetCnt, copnGoods.getGoodsCode(), productStatus);
				if (productStatus != null && StringUtil.compare(productStatus.getStatusName(), "승인반려")) {
					procCnt++;
					if (copnGoodsRepository.updateRejectStatus(copnGoods.getGoodsCode(), copnGoods.getPaCode(),
							Application.ID.code(), StringUtil.truncate(productStatus.getComment(), 500)) < 1) {
						failCnt++;
						log.info("No.{} 상품:{} 업데이트 실패", targetCnt, copnGoods.getGoodsCode());

					}
				} else {
					log.info("No.{} 상품:{} 반려상태가 아닙니다.", targetCnt, copnGoods.getGoodsCode());
				}
			}

			if (!slice.hasNext())
				break;

		}
		Timestamp endDate = commonService.currentDate();
		log.info("쿠팡 상태불일치 업데이트 완료일시:{} 타겟:{} 처리:{} 실패:{} End ==> {}s elapsed.", endDate, targetCnt, procCnt, failCnt, (endDate.getTime() - startDate.getTime()) / 1000);

	}

}
