package com.cware.netshopping.patdeal.process.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaTdealClaimListVO;
import com.cware.netshopping.domain.model.Paorderm;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.patdeal.domain.ClaimInfos;
import com.cware.netshopping.patdeal.domain.OrderDetail;
import com.cware.netshopping.patdeal.domain.OrderOptions;
import com.cware.netshopping.patdeal.domain.OrderProducts;
import com.cware.netshopping.patdeal.domain.Shippings;
import com.cware.netshopping.patdeal.process.PaTdealClaimProcess;
import com.cware.netshopping.patdeal.repository.PaTdealCancelDAO;
import com.cware.netshopping.patdeal.repository.PaTdealClaimDAO;
import com.cware.netshopping.patdeal.service.PaTdealClaimService;
import com.cware.netshopping.patdeal.util.PaTdealApiRequest;
import com.cware.netshopping.patdeal.util.PaTdealComUtil;
import com.cware.netshopping.patdeal.util.PaTdealConnectUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service("patdeal.claim.paTdealClaimProcess")
public class PaTdealClaimProcessImpl extends AbstractService implements PaTdealClaimProcess {

	@Autowired
	private SystemService systemService;

	@Autowired
	private PaTdealConnectUtil paTdealConnectUtil;

	@Resource(name = "patdeal.claim.paTdealClaimDAO")
	PaTdealClaimDAO paTdealClaimDAO;

	@Resource(name = "patdeal.cancel.paTdealCancelDAO")
	PaTdealCancelDAO paTdealCancelDAO;

	@Autowired
	@Qualifier("patdeal.claim.paTdealClaimService")
	private PaTdealClaimService paTdealClaimService;

	@Autowired
	private PaCommonDAO paCommonDAO;
	
	@Autowired
	private PaTdealApiRequest paTdealApiRequest;

	@SuppressWarnings("unchecked")
	@Override
	public List<PaTdealClaimListVO> getTdealClaimList(String claimStatus, String fromDate, String toDate,
			HttpServletRequest request) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		ParamMap apiInfoMap = new ParamMap();
		String apiCode = "";
		String paCode = "";
		String paOrderGb = "";
		String rtnMsg = "";
		String dateTime = systemService.getSysdatetimeToString();
		List<Map<String, Object>> tdealClaimDataList = null;
		OrderDetail detailResponseMsg = null;
		List<PaTdealClaimListVO> paTdealClaimList = new ArrayList<PaTdealClaimListVO>();
		String duplicateCheck = "";
		ParamMap apiDataObject = new ParamMap();
		String pathParameters = "";
		Map<String, String> queryParameters = new HashMap<String, String>();
		log.info("======= 티딜 클레임 목록 조회 API Start - {} =======");

		try {
			switch (claimStatus) {
			case "20": // 취소 접수
				apiCode = "IF_PATDEALAPI_03_006";
				paOrderGb = "20";
				break;
			case "21": // 취소 완료
				apiCode = "IF_PATDEALAPI_03_017";
				paOrderGb = "20";
				break;
			case "30": // 반품 접수
				apiCode = "IF_PATDEALAPI_03_007";
				paOrderGb = "30";
				break;
			case "60": // 반품완료(직권환불대비)
				apiCode = "IF_PATDEALAPI_03_018";
				paOrderGb = "30";
				break;
			case "40": // 교환
				apiCode = "IF_PATDEALAPI_03_009";
				paOrderGb = "40";
				break;
			default:
				throw new Exception("claimStatus is wrong " + claimStatus + " ip:"
						+ request.getHeader("X-Forwarded-For") + " " + request.getRemoteAddr());
			}

			apiInfoMap = paTdealApiRequest.getApiInfo(apiCode);
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { apiCode });
			apiInfoMap.put("duplicateCheck", duplicateCheck);
			
			fromDate = ComUtil.NVL(fromDate).length() == 10 ? fromDate
					: DateUtil.addDay(DateUtil.getCurrentNaverDateAsString(), -1, DateUtil.TDEAL_DATE_FORMAT); // 조회시작일
			toDate = ComUtil.NVL(toDate).length() == 10 ? toDate : DateUtil.getCurrentNaverDateAsString(); // 조회종료일

			pathParameters = "startYmd=" + fromDate + "&endYmd=" + toDate;

			for (int i = 0; i < Constants.PA_TDEAL_CONTRACT_CNT; i++) {
				paCode = (i == 0) ? Constants.PA_TDEAL_BROAD_CODE : Constants.PA_TDEAL_ONLINE_CODE;
				apiInfoMap.put("paCode", paCode);
				apiInfoMap.put("apiCode", apiCode);

				rtnMsg += paCode + ":";

				map = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters, queryParameters,
						apiDataObject);

				if ("0".equals(String.valueOf(map.get("totalCount")))) {
					apiInfoMap.put("code", "500");
					rtnMsg += getMessage("pa.not_exists_process_list");
					continue;
				}

				tdealClaimDataList = (List<Map<String, Object>>) map.get("contents");

				for (Map<String, Object> m : tdealClaimDataList) {
					m.put("paCode", paCode);
					m.put("paOrderGb", paOrderGb);
					m.put("outBefClaimGb", "0");
					Map<String, Object> returnAddress = (Map<String, Object>) m.get("returnAddress");
					// 클레임 수량, 단품번호를 알아 내기 위해 주문상세조회API 실행
					detailResponseMsg = orderInfoDetail(paCode, m.get("orderNo").toString(), request);
					// 클레임 수량, 단품번호를 알아 내기 위해 탐색
					List<OrderProducts> responseData = detailResponseMsg.getOrderProducts();
					List<Shippings> responseShippingData = detailResponseMsg.getShippings();
					for (OrderProducts op : responseData) {
						for (OrderOptions oo : op.getOrderOptions()) {
							if (Long.valueOf(m.get("claimNo").toString()) == oo.getClaimNo()) {
								for (String OpNo : m.get("orderOptionNos").toString().replaceAll("[^0-9,]", "")
										.split(",")) {
									if (Long.valueOf(OpNo) == oo.getOrderOptionNo()) {
										PaTdealClaimListVO vo = new PaTdealClaimListVO();
										m.put("optionManagementCd", oo.getOptionManagementCd());
										m.put("orderCnt", oo.getOrderCnt());
										m.put("shippingNo", oo.getShippingNo());
										m.put("originalShippingNo", oo.getOriginShippingNo());
										m.put("orderOptionNo", OpNo);
										m.put("refundName", returnAddress.get("name").toString());
										m.put("refundContact1", returnAddress.get("contact1").toString());
										m.put("refundContact2", returnAddress.get("contact2").toString());
										m.put("refundZipCd", returnAddress.get("zipCd").toString());
										m.put("refundAddress", returnAddress.get("address").toString());
										m.put("refundDetailAddress", returnAddress.get("detailAddress").toString());
										if (paOrderGb.equals("30")) { // 반품배송비 세팅
											for (Shippings rc : responseShippingData) {
												if (rc.getShippingNo() == oo.getOriginShippingNo()) {
													m.put("returnDeliveryAmt", rc.getReturnDeliveryAmt());
													m.put("remoteDeliveryAmt", rc.getRemoteDeliveryAmt());
												}
											}
										} else { // 티딜은 고객귀책 교환이 없다 따라서 무조건 교환비 0원처리
											m.put("returnDeliveryAmt", "0");
										}
										vo = (PaTdealClaimListVO) PaTdealComUtil.map2VO(m, PaTdealClaimListVO.class);
										vo.setInsertId(Constants.PA_TDEAL_PROC_ID);
										vo.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
										vo.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
										vo.setPaDoFlag(claimStatus);
										vo.setPaOrderGb(paOrderGb);
										paTdealClaimList.add(vo);
										break;
									}
								}
							}
						}
					}
				}
			}
			apiInfoMap.put("code", "200");
			apiInfoMap.put("message", rtnMsg);
			log.info("======= 티딜 클레임 목록 조회 API End - {} =======");
		} catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paTdealConnectUtil.closeApi(request, apiInfoMap);
		}

		return paTdealClaimList;
	}

	@Override
	public ResponseMsg procTdealClaimCancelList(String claimStatus, HttpServletRequest request) throws Exception {
		ParamMap apiInfoMap = new ParamMap();
		String rtnMsg = "";
		List<Map<String, Object>> tdealClaimDataList = null;
		OrderDetail detailResponseMsg = null;
		int executedRtn = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		String status = claimStatus.equals("31") ? "30" : "40";
		String dateTime = systemService.getSysdatetimeToString();
		
		try {
			tdealClaimDataList = paTdealClaimDAO.selectNotEndClaimList(status);
			if (tdealClaimDataList.size() < 1) {
				return new ResponseMsg("200", "대상이 없습니다.");
			}
			for (Map<String, Object> m : tdealClaimDataList) {
				executedRtn = 0;
				// 클레임 상태를 알아내기 위해 탐색
				detailResponseMsg = orderInfoDetail(m.get("PA_CODE").toString(), m.get("PA_ORDER_NO").toString(),
						request);
				List<ClaimInfos> responseData = detailResponseMsg.getClaimInfos();
				for (ClaimInfos ci : responseData) {
					if (ComUtil.objToStr(ci.getClaimNo()).equals(m.get("PA_CLAIM_NO").toString())) {
						if (ci.getClaimStatusType() == null) {
							PaTdealClaimListVO tdealClaimList = new PaTdealClaimListVO();
							tdealClaimList.setOrderNo(m.get("PA_ORDER_NO").toString());
							tdealClaimList.setClaimNo(ComUtil.objToLong(m.get("PA_CLAIM_NO")));
							tdealClaimList.setOptionManagementCd(m.get("PA_ORDER_SEQ").toString());
							tdealClaimList.setPaOrderGb(claimStatus);
							tdealClaimList.setPaCode(m.get("PA_CODE").toString());
							tdealClaimList.setOrderCnt(ComUtil.objToLong(m.get("PA_PROC_QTY")));
							tdealClaimList.setOrderOptionNo(ComUtil.objToLong(m.get("PA_SHIP_NO")));
							tdealClaimList.setClaimYmdt(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
							tdealClaimList.setClaimAmt(ComUtil.objToLong(m.get("CLAIM_AMT")));
							tdealClaimList.setResponsibleObjectType(m.get("RESPONSIBLE_OBJECT_TYPE").toString());
							tdealClaimList.setModifyDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							tdealClaimList.setInsertDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
							executedRtn = paTdealClaimDAO.insertPaTdealClaimList(tdealClaimList);
							
							if (executedRtn != 1)
								throw processException("msg.cannot_save",
										new String[] { "TPATDEALCLAIMLIST CLAIM CANCEL INSERT" });

							executedRtn = insertPaOrderm(tdealClaimList, new Paorderm());
							if (executedRtn != 1)
								throw processException("msg.cannot_save",
										new String[] { "TPAORDERM CLAIM CANCEL INSERT" });
							if(m.get("CREATE_YN").equals("0")) {
								executedRtn = updatePaOrderMPreCancel(m);
								if (executedRtn < 1)
								throw processException("msg.cannot_save",
										new String[] { "TPAORDERM CLAIM CANCEL UPDATE" });
							}
						}
					}
				}
			}

			apiInfoMap.put("message", rtnMsg);
		} catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(apiInfoMap, e);
		}
		return new ResponseMsg("200", "대상추출완료");
	}

	public int updatePaOrderMPreCancel(Map<String, Object> map) throws Exception {

		int executedRtn = 0;
		map.put("PRE_CANCEL_REASON", "클레임 생성 이전 철회");
		map.put("PRE_CANCEL_YN", "1");
		executedRtn = paTdealClaimDAO.updatePaOrderMPreCancel(map);

		return executedRtn;
	}
	
	@Override
	public OrderDetail orderInfoDetail(String paCode, String orderNo, HttpServletRequest request) throws Exception {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap = new ParamMap();
		String apiCode = "IF_PATDEALAPI_03_002";

		ParamMap apiDataObject = new ParamMap();
		String pathParameters = "";
		Map<String, String> queryParameters = new HashMap<String, String>();
		OrderDetail orderDetail = new OrderDetail();
		log.info("======= 티딜 주문 상세 조회 API Start - {} =======");
		try {
			// 취소, 반품, 교환에서 다 사용하므로 중복 실행 체크 안함
//			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
//			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});

			apiInfoMap = paTdealApiRequest.getApiInfo(apiCode);
			pathParameters = orderNo;
			apiInfoMap.put("apiCode", apiCode);
			apiInfoMap.put("paCode", paCode);

			resultMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters, queryParameters,
					apiDataObject);

			ObjectMapper objectMapper = new ObjectMapper();

			orderDetail = objectMapper.convertValue(resultMap, OrderDetail.class);

			apiInfoMap.put("code", "200");
			apiInfoMap.put("message", "티딜 주문 상세 조회 성공");
		} catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paTdealConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info("======= 티딜 주문 상세 조회 API End - {} =======");
		return orderDetail;

	}

	@Override
	public List<HashMap<String, Object>> selectClaimTargetList(ParamMap paramMap) throws Exception {
		return paTdealClaimDAO.selectClaimTargetList(paramMap);
	}

	@Override
	public int saveTdealClaimList(String claimStatus, PaTdealClaimListVO paTdealClaimVo) throws Exception {
		int executedRtn = 0;
		int checkClaimCnt = 0;
		int checkClaimDoFlag = 0;
		// 원주문 데이터 체크
		checkClaimCnt = paTdealCancelDAO.countOrderList(paTdealClaimVo);
		if (checkClaimCnt < 1)
			return 0; // 주문이 없는 데이터는 스킵

		// TPATDEALClaimList 중복 데이터 유무 체크
		checkClaimDoFlag = paTdealClaimDAO.selectPaTdealClaimListCount(paTdealClaimVo);
		if (checkClaimDoFlag > 0) {
			if (claimStatus.equals("60") && checkClaimDoFlag != 60) {
				// 반품완료조회인데 저장됐던 자료이면서 반품 완료 상태가 아니다 => 반품 접수 이후 티딜 관리자 페이지에서 반품완료를 한것이다.
				// 따라서 DO_FLAG를 60으로 업데이트를 한다.
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("PA_DO_FLAG", "60");
				map.put("API_RESULT_CODE", "000000");
				map.put("API_RESULT_MESSAGE", "직권 반품 완료");
				map.put("PA_CLAIM_NO", paTdealClaimVo.getClaimNo());
				map.put("PA_CODE", paTdealClaimVo.getPaCode());
				map.put("PA_ORDER_NO", paTdealClaimVo.getOrderNo());
				map.put("PA_ORDER_SEQ", paTdealClaimVo.getOptionManagementCd());
				map.put("PA_SHIP_NO", paTdealClaimVo.getOrderOptionNo());
				map.put("PA_ORDER_GB", paTdealClaimVo.getPaOrderGb());
				paTdealClaimDAO.updatePaOrderMDoFlag(map);
			}
			// 이미 저장됐었던 취소 자료이니 스킵
			return 0;
		} else {
			// INSERT TpaTdealClaimList
			executedRtn = paTdealClaimDAO.insertPaTdealClaimList(paTdealClaimVo);
			if (executedRtn != 1) {
				throw processException("msg.cannot_save", new String[] { "TPATDEALCALIMLIST INSERT" });
			}
		}
		if("CANCEL".equals(paTdealClaimVo.getClaimType().toString())) {
			return executedRtn; //주문 취소로 인입됐으면 insertPaOrderm을 넘긴다.
		}
			
		switch (claimStatus) {
		case "30":
		case "60":
			executedRtn = insertPaOrderm(paTdealClaimVo, new Paorderm());
			if (executedRtn != 1)
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT(CLAIM30)" });
			break;
		case "40":
			executedRtn = insertPaOrderm(paTdealClaimVo, new Paorderm());
			if (executedRtn != 1)
				throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT(CLAIM40)" });
			break;
		}

		return executedRtn;
	}

	private int insertPaOrderm(PaTdealClaimListVO paTdealClaimListVO, Paorderm paorderm) throws Exception {

		if (paorderm == null)
			paorderm = new Paorderm();

		Map<String, String> voMap = null;

		voMap = BeanUtils.describe(paTdealClaimListVO);
		voMap.put("outBefClaimGb", "0");
		paorderm.setPaOrderNo(voMap.get("orderNo"));
		paorderm.setPaClaimNo(voMap.get("claimNo"));
		paorderm.setPaOrderSeq(voMap.get("optionManagementCd").replaceAll("_", ""));
		paorderm.setPaShipNo(voMap.get("orderOptionNo"));

		// 반품 인입 시 원주문 doFlag 확인
		if ("30".equals(String.valueOf(voMap.get("paOrderGb")))) {
			HashMap<String, Object> claim = paTdealClaimDAO.selectPaTdealClaimInfo(paTdealClaimListVO);
			// 출하지시 상태일 경우 출고전반품 데이터로 세팅
			if ("30".equals(String.valueOf(claim.get("DO_FLAG")))) {
				paorderm.setRemark1V("출고전반품 대상");
			}
		}
		
		int executedRtn = 0;
		String sysdate = DateUtil.getCurrentDateTimeAsString();
		String paOrderGb = voMap.get("paOrderGb");
		String outClaimGb = voMap.get("outBefClaimGb");

		paorderm.setPaGroupCode(Constants.PA_TDEAL_GROUP_CODE);
		paorderm.setPaOrderGb(paOrderGb);
		paorderm.setPaCode(voMap.get("paCode"));

		paorderm.setPaProcQty(String.valueOf(voMap.get("orderCnt")));

		if ("RETURN_PROC_WAITING_REFUND".equals(ComUtil.objToStr(voMap.get("claimStatusType")))
				|| "RETURN_DONE".equals(ComUtil.objToStr(voMap.get("claimStatusType")))) {
			paorderm.setPaDoFlag("60"); // 반품 완료일때
		} else if("40".equals(String.valueOf(voMap.get("paOrderGb")))) {
			paorderm.setChangeFlag("00");
			paorderm.setPaDoFlag("20");
		} else if ("0".equals(outClaimGb)) {
			paorderm.setPaDoFlag("20");
		} else {
			paorderm.setPaDoFlag("60");
		}

		paorderm.setOutBefClaimGb(outClaimGb);
		paorderm.setInsertDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyDate(DateUtil.toTimestamp(sysdate, "yyyyMMddHHmmss"));
		paorderm.setModifyId(Constants.PA_TDEAL_PROC_ID);
		
		executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		if ("40".equals(paOrderGb) || "41".equals(paOrderGb)) { // 교환일경우 45(회수) or 46(회수취소) 도 같이 생성
			paOrderGb = paOrderGb.equals("40") ? "45" : "46";
			paorderm.setPaOrderGb(paOrderGb);
			paorderm.setChangeFlag("00");
			paorderm.setPaDoFlag("20");
			executedRtn = paCommonDAO.insertPaOrderM(paorderm);
		}
		if (executedRtn != 1)
			throw processException("msg.cannot_save", new String[] { "TPAORDERM INSERT" });

		return executedRtn;
	}

	@Override
	public List<Object> selectOrderCalimTargetDt30List(ParamMap paramMap) throws Exception {
		return paTdealClaimDAO.selectOrderCalimTargetDt30List(paramMap);
	}

	@Override
	public List<Object> selectOrderCalimTargetDt20List(ParamMap paramMap) throws Exception {
		return paTdealClaimDAO.selectOrderCalimTargetDt20List(paramMap);
	}

	@Override
	public List<Object> selectClaimCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paTdealClaimDAO.selectClaimCancelTargetDtList(paramMap);
	}

	public int updatePaOrderMDoFlag(Map<String, Object> map, String doFlag, String resultMessage, String resultCode)
			throws Exception {

		int executedRtn = 0;
		if (doFlag == null)
			doFlag = String.valueOf(map.get("PA_DO_FLAG")).equals("null") ? "" : map.get("PA_DO_FLAG").toString();

		map.put("PA_DO_FLAG", doFlag);
		map.put("API_RESULT_CODE", resultCode);
		map.put("API_RESULT_MESSAGE", resultMessage);

		executedRtn = paTdealClaimDAO.updatePaOrderMDoFlag(map);

		return executedRtn;
	}

	@Override
	public List<HashMap<String, Object>> selectTdealReturnCompleList() throws Exception {
		return paTdealClaimDAO.selectTdealReturnCompleList();
	}

	@Override
	public ResponseMsg returnCompleProc(HashMap<String, Object> tdealReturnCompleItem, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap = new ParamMap();
		String returnApiCode = "IF_PATDEALAPI_03_015";
		String changeApiCode = "IF_PATDEALAPI_03_016";
		String apiCode = "";
		
		ParamMap apiDataObject = new ParamMap();
		Map<String, Object> map = new HashMap<String, Object>();
		String pathParameters = "";
		Map<String, String> queryParameters = new HashMap<String, String>();
		String duplicateCheck = "";
		String paCode = "";
		List<Map<String, String>> restoreStockOrderOptionNosList = new ArrayList<Map<String, String>>();
		int executedRtn = 0;

		log.info("======= 티딜 수거 완료  API Start - {} =======");
		try {
			if(tdealReturnCompleItem.get("PA_ORDER_GB").equals("30")){ // 반품
				apiCode = returnApiCode;
				// 재고 복원할 상품 세팅 (배열을 빈값으로 넣어 복원하지 않는다)
				map.put("restoreStockOrderOptionNos", restoreStockOrderOptionNosList);	
			}else { //교환
				apiCode = changeApiCode;
				// 교환신청은 반품과 다르게 하나씩만 할 수 있어 배열이 아니다. (false 를 넣어 재고 복원하지 않음)
				map.put("restoresStock", "false");
			}
			apiInfoMap = paTdealApiRequest.getApiInfo(apiCode);
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if (duplicateCheck.equals("1"))
				throw processException("msg.batch_process_duplicated", new String[] { apiCode });
			apiInfoMap.put("duplicateCheck", duplicateCheck);
			apiInfoMap.put("apiCode", apiCode);
			apiInfoMap.put("paCode", tdealReturnCompleItem.get("PA_CODE").toString());

			pathParameters = tdealReturnCompleItem.get("PA_CLAIM_NO").toString();
			paCode = tdealReturnCompleItem.get("PA_CODE").toString();
			
			apiDataObject.put("body", map);

			resultMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters, queryParameters,
					apiDataObject);
			if (resultMap.get("status").toString().equals("204")) {
				tdealReturnCompleItem.put("PA_DO_FLAG", "60");
				tdealReturnCompleItem.put("API_RESULT_CODE", "204");
				tdealReturnCompleItem.put("API_RESULT_MESSAGE", "수거완료");
				executedRtn = paTdealClaimDAO.updatePaOrderMDoFlag(tdealReturnCompleItem);
				if (executedRtn != 1)
					throw processException("msg.cannot_save", new String[] { "TPAORDERM RETURN COMPLETE UPDATE" });
			} else {
				tdealReturnCompleItem.put("API_RESULT_CODE", resultMap.get("code").toString());
				tdealReturnCompleItem.put("API_RESULT_MESSAGE", resultMap.get("message").toString());
				executedRtn = paTdealClaimDAO.updatePaOrderMDoFlag(tdealReturnCompleItem);
				if (executedRtn != 1)
					throw processException("msg.cannot_save", new String[] { "TPAORDERM RETURN COMPLETE UPDATE" });
			}
			apiInfoMap.put("code", tdealReturnCompleItem.get("API_RESULT_CODE"));
			apiInfoMap.put("message", tdealReturnCompleItem.get("API_RESULT_MESSAGE"));
		} catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paTdealConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info("======= 티딜 수거 완료 API End - {} =======");

		return new ResponseMsg(tdealReturnCompleItem.get("API_RESULT_CODE").toString(),
				tdealReturnCompleItem.get("API_RESULT_MESSAGE").toString());

	}

	@Override
	public List<Object> selectOrderChangeTargetDtList(ParamMap paramMap) throws Exception {
		return paTdealClaimDAO.selectOrderChangeTargetDtList(paramMap);
	}

	@Override
	public int updatePaOrdermChangeFlag(String changeFlag, String mappingSeq) throws Exception {
		ParamMap paramMap = new ParamMap();
		paramMap.put("changeFlag", changeFlag);
		paramMap.put("mappingSeq", mappingSeq);
		return paTdealClaimDAO.updatePaOrdermChangeFlag(paramMap);
	}
	
	@Override
	public List<HashMap<String, Object>> selectTdealExchangeReturnDoFlag60List() throws Exception {
		return paTdealClaimDAO.selectTdealExchangeReturnDoFlag60List();
	}

	@Override
	public ResponseMsg exchangeDeliveryProc(HashMap<String, Object> tdealExchangeReturnDoFlag60Item, HttpServletRequest request) throws Exception {
		OrderDetail detailResponseMsg = null;
		String optionManagementCd = tdealExchangeReturnDoFlag60Item.get("PA_ORDER_SEQ").toString();
		String orderNo = tdealExchangeReturnDoFlag60Item.get("PA_ORDER_NO").toString();
		String paCode = tdealExchangeReturnDoFlag60Item.get("PA_CODE").toString();
		int executedRtn = 0;
		
		detailResponseMsg = orderInfoDetail(paCode, orderNo, request);
		List<OrderProducts> responseData = detailResponseMsg.getOrderProducts();
		for (OrderProducts op : responseData) {
			for (OrderOptions oo : op.getOrderOptions()) {
				if(oo.isExchangedRelease() && oo.getOptionManagementCd().replaceAll("_", "").equals(optionManagementCd) && oo.getOrderStatusType().equals("PAY_DONE")) {
					tdealExchangeReturnDoFlag60Item.put("orderOptionNo", oo.getOrderOptionNo());
					tdealExchangeReturnDoFlag60Item.put("API_RESULT_CODE", "000000");
					tdealExchangeReturnDoFlag60Item.put("API_RESULT_MESSAGE", "옵션관리번호 업데이트");
					executedRtn = paTdealClaimDAO.updatePaOrdermPaShipNo(tdealExchangeReturnDoFlag60Item);
					if (executedRtn != 1)
						throw processException("msg.cannot_save", new String[] { "TPAORDERM PA_SHIP_NO UPDATE" });
					executedRtn = paTdealClaimDAO.updateTdealClaimListOrderOprtionNo(tdealExchangeReturnDoFlag60Item);
					if (executedRtn != 1)
						throw processException("msg.cannot_save", new String[] { "TPAORDERM ORDER_OPTION_NO UPDATE" });
				}
			}
		}
		
		return null;
	}

	@Override
	public ResponseMsg returnApprovalProc(HashMap<String, Object> tdealReturnCompleItem, HttpServletRequest request)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		ParamMap apiInfoMap = new ParamMap();
		String apiCode = "IF_PATDEALAPI_03_011";
		Map<String,Object> map = new HashMap<String, Object>();
		ParamMap apiDataObject = new ParamMap();
		String pathParameters = "";
		Map<String, String> queryParameters = new HashMap<String, String>();
//		String duplicateCheck = "";
		String paCode = "";
		int executedRtn = 0;

		log.info("======= 티딜 클레임 승인  API Start - {} =======");
		try {
			apiInfoMap = paTdealApiRequest.getApiInfo(apiCode);
//			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode); 클레임 승인의 경우 여러 클레임을 한번에 승인 할 수 있으므로 중복실행 체크를 하지 않는다
//			if (duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] { apiCode });
//			apiInfoMap.put("duplicateCheck", duplicateCheck);
			apiInfoMap.put("apiCode", apiCode);
			apiInfoMap.put("paCode", tdealReturnCompleItem.get("PA_CODE").toString());
			
			map.put("claimYmdt", tdealReturnCompleItem.get("CLAIM_YMDT").toString().substring(0, 10));
			apiDataObject.put("body", map);
			
			pathParameters = tdealReturnCompleItem.get("PA_CLAIM_NO").toString();
			paCode = tdealReturnCompleItem.get("PA_CODE").toString();
			
			resultMap = paTdealConnectUtil.getCommonLegacy(paCode, apiCode, pathParameters, queryParameters,apiDataObject);
			
			if (resultMap.get("status").toString().equals("204") || resultMap.get("message").toString().indexOf("당월이전거래") > -1 || resultMap.get("message").toString().indexOf("상점으로]") > -1) {
				// KCP 고객센터에서 처리해야 하는 건도 정상 처리
				tdealReturnCompleItem.put("API_RESULT_CODE", "204");
				tdealReturnCompleItem.put("API_RESULT_MESSAGE", "클레임 승인");
				executedRtn = paTdealClaimDAO.updatePaOrderMDoFlag(tdealReturnCompleItem);
				if (executedRtn != 1)
					throw processException("msg.cannot_save", new String[] { "TPAORDERM RETURN COMPLETE UPDATE" });
			} else {
				OrderDetail detailResponseMsg = null;
				detailResponseMsg = orderInfoDetail(paCode, tdealReturnCompleItem.get("PA_ORDER_NO").toString(), request);
				List<ClaimInfos> responseData = detailResponseMsg.getClaimInfos();
				    // 클레임 수량, 단품번호를 알아 내기 위해 탐색
				for(ClaimInfos m : responseData) {
					if(tdealReturnCompleItem.get("PA_CLAIM_NO").toString().equals(ComUtil.objToStr(m.getClaimNo()))) {
						if(m.getClaimStatusType().equals("RETURN_PROC_BEFORE_RECEIVE")) { // 수거 진행 상태
							tdealReturnCompleItem.put("API_RESULT_CODE", "204");
							tdealReturnCompleItem.put("API_RESULT_MESSAGE", "클레임 승인");
							executedRtn = paTdealClaimDAO.updatePaOrderMDoFlag(tdealReturnCompleItem);
						}else { // 그 외 상태값 오류 발생 or 반품 완료 상태
							tdealReturnCompleItem.put("API_RESULT_CODE", resultMap.get("code").toString());
							tdealReturnCompleItem.put("API_RESULT_MESSAGE", resultMap.get("message").toString());
							executedRtn = paTdealClaimDAO.updatePaOrderMDoFlag(tdealReturnCompleItem);
						}
					}
				}
				
				if (executedRtn != 1)
					throw processException("msg.cannot_save", new String[] { "TPAORDERM RETURN COMPLETE UPDATE" });
			}
			apiInfoMap.put("code", tdealReturnCompleItem.get("API_RESULT_CODE"));
			apiInfoMap.put("message", tdealReturnCompleItem.get("API_RESULT_MESSAGE"));

		} catch (Exception e) {
			log.error(e.getMessage());
			paTdealConnectUtil.checkException(apiInfoMap, e);
		} finally {
			paTdealConnectUtil.closeApi(request, apiInfoMap);
		}
		log.info("======= 티딜 클레임 승인 API End - {} =======");

		return new ResponseMsg(tdealReturnCompleItem.get("API_RESULT_CODE").toString(),
				tdealReturnCompleItem.get("API_RESULT_MESSAGE").toString());

	}

	@Override
	public List<Object> selectChangeCancelTargetDtList(ParamMap paramMap) throws Exception {
		return paTdealClaimDAO.selectChangeCancelTargetDtList(paramMap);
	}

	@Override
	public String compareAddress(ParamMap paramMap) throws Exception {
		return paTdealClaimDAO.compareAddress(paramMap);
	}

}
