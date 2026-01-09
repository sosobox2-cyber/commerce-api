package com.cware.netshopping.pacommon.common.process.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cware.framework.common.exception.ProcessException;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.core.exchange.domain.SmsSendVO;
import com.cware.netshopping.core.exchange.process.UmsBizProcess;
import com.cware.netshopping.domain.PaCustShipCostVO;
import com.cware.netshopping.domain.PaGoodsOfferVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.PaLtonGoodsVO;
import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.PaNoticeApplyVO;
import com.cware.netshopping.domain.PaSsgGoodsVO;
import com.cware.netshopping.domain.model.Custcounseldt;
import com.cware.netshopping.domain.model.Custcounselm;
import com.cware.netshopping.domain.model.Mobileordercancelhis;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsImage;
import com.cware.netshopping.domain.model.PaGoodsTargetRec;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoGoodsPrice;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.SaleNoGoods;
import com.cware.netshopping.domain.model.SpAuthTransInfo;
import com.cware.netshopping.domain.model.SpPaGoodsDescInfo;
import com.cware.netshopping.domain.model.SpPaGoodsDtInfo;
import com.cware.netshopping.domain.model.SpPaGoodsInfo;
import com.cware.netshopping.domain.model.SpPaOfferInfo;
import com.cware.netshopping.pacommon.claim.repository.PaClaimDAO;
import com.cware.netshopping.pacommon.common.process.PaCommonProcess;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;

@Service("pacommon.common.pacommonProcess")
public class PaCommonProcessImpl extends AbstractService implements PaCommonProcess {
	
	@Resource(name = "pacommon.common.paCommonDAO")
    private PaCommonDAO pacommonDAO;
	
	@Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;
	
	@Resource(name = "common.system.systemProcess")
    private SystemProcess systemProcess;
	
	@Resource(name = "pacommon.claim.paclaimDAO")
    private PaClaimDAO paclaimDAO;
	
	@Resource(name = "core.exchange.umsBizProcess")
	private UmsBizProcess umsBizProcess;
	
	@Override
	public String selectCheckOpenApiCode(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCheckOpenApiCode(paramMap);
	}

	@Override
	public List<PaGoodsTargetRec> selectPaGoodsInsertTarget(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectPaGoodsInsertTarget(paramMap);
	}
	
	@Override
	public int selectEtvMarginCheck() throws Exception {
		return pacommonDAO.selectEtvMarginCheck();
	}
	
	@Override
	public String selectPaOfferCheck(PaGoodsTargetRec paGoodstarget) throws Exception {
		String rtnMsg = "";
		int offerCnt = 0;
		
		offerCnt = pacommonDAO.selectPaOfferCheck(paGoodstarget);
		if(offerCnt <= 0) rtnMsg = "정보고시, ";
		
		return rtnMsg;
	}

	@Override
	public String selectOriginMappingNaver(String originCode) throws Exception {
		return pacommonDAO.selectOriginMappingNaver(originCode);
	}

	@Override
	public String selectOriginMapping(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonDAO.selectOriginMapping(paGoodstarget);
	}

	@Override
	public String selectImageCheck(String goodsCode) throws Exception {
		String rtnMsg = "";
		int rtnCnt = 0;
		
		rtnCnt = pacommonDAO.selectImageCheck(goodsCode);
		if(rtnCnt <= 0) rtnMsg = "이미지, ";
		
		return rtnMsg;
	}

	@Override
	public String selectPaDescribeCheck(PaGoodsTargetRec paGoodstarget) throws Exception {
		String rtnMsg = "";
		int rtnCnt = 0;
		
		rtnCnt = pacommonDAO.selectPaDescribeCheck(paGoodstarget);
		if(rtnCnt <= 0) rtnMsg = "기술서, ";
		
		return rtnMsg;
	}

	@Override
	public String selectPaStockCheck(PaGoodsTargetRec paGoodstarget) throws Exception {
		String rtnMsg = "";
		int rtnCnt = 0;
		
		rtnCnt = pacommonDAO.selectPaStockCheck(paGoodstarget);
		if(rtnCnt <= 0) rtnMsg = "재고, ";
		return rtnMsg;
	}

	@Override
	public String selectPaShipCostCheck(PaGoodsTargetRec paGoodstarget) throws Exception {
		String rtnMsg = "";
		int rtnCnt = 0;
		
		rtnCnt = pacommonDAO.selectPaShipCostCheck(paGoodstarget);
		if(rtnCnt <= 0) rtnMsg = "배송비, ";
		return rtnMsg;
	}

	@Override
	public String selectChkMinMarSale(PaGoodsTargetRec paGoodstarget) throws Exception {
		ParamMap paramMap = new ParamMap();
		String rtnMsg = "";
		int rtnCnt = 0;
		
		String paGroupCode = paGoodstarget.getPaGroupCode();
		
		paramMap.put("paCode",	   paGoodstarget.getPaCode());
		paramMap.put("eventYn",    paGoodstarget.getEventYn());
		paramMap.put("marginRate", paGoodstarget.getMarginRate());
		paramMap.put("salePrice",  paGoodstarget.getSalePrice());
		paramMap.put("taxYn", paGoodstarget.getTaxYn());
		
		switch(paGroupCode) {
			case "01" : // 11번가
				paramMap.put("compareGroup", "11");
				paramMap.put("marginCode",   "10");
				paramMap.put("minimumCode",  "20");
				break;
			case "02" : // G마켓
			case "03" : // 옥션
				paramMap.put("compareGroup", "21");
				paramMap.put("marginCode",   "30");
				paramMap.put("minimumCode",  "40");
				break;
			case "04" : // 네이버
				paramMap.put("compareGroup", "41");
				paramMap.put("marginCode",   "50");
				paramMap.put("minimumCode",  "51");
				break;
			case "05" : // 쿠팡
				paramMap.put("compareGroup", "51");
				paramMap.put("marginCode",   "60");
				paramMap.put("minimumCode",  "61");
				break;
			case "06" : // 위메프
				paramMap.put("compareGroup", "61");
				paramMap.put("marginCode",   "70");
				paramMap.put("minimumCode",  "71");
				break;
			case "07" : //인터파크
				paramMap.put("compareGroup", "71");
				paramMap.put("marginCode",   "80");
				paramMap.put("minimumCode",  "81");
				break;
			case "08" : // 롯데ON
				paramMap.put("compareGroup", "81");
				paramMap.put("marginCode",   "82");
				paramMap.put("minimumCode",  "83");
				break;
			case "09" : //티몬
				paramMap.put("compareGroup", "91");
				paramMap.put("marginCode",   "84");
				paramMap.put("minimumCode",  "85");
				break;
			case "10" : //SSG
				paramMap.put("compareGroup", "A1");
				paramMap.put("marginCode",   "86");
				paramMap.put("minimumCode",  "87");
				break;
			case "11" : //카카오
				paramMap.put("compareGroup", "B1");
				paramMap.put("marginCode",   "88");
				paramMap.put("minimumCode",  "89");
				break;
			default :
				break;
		}
		
		rtnCnt = pacommonDAO.selectChkMinMarSale(paramMap);
		if(rtnCnt <= 0) rtnMsg = "마진/최저판매가, ";
		return rtnMsg;
	}

	@Override
	public String selectBrandMapping11st(String brandCode) throws Exception {
		return pacommonDAO.selectBrandMapping11st(brandCode);
	}

	@Override
	public String selectBrandMappingGmkt(String brandCode) throws Exception {
		return pacommonDAO.selectBrandMappingGmkt(brandCode);
	}

	@Override
	public String selectMakerMappingGmkt(String makecoCode) throws Exception {
		return pacommonDAO.selectMakerMappingGmkt(makecoCode);
	}

	@Override
	public String selectBrandMapping(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonDAO.selectBrandMapping(paGoodstarget);
	}

	@Override
	public String selectPaOriginCheck(ParamMap paramMap) throws Exception {
		String rtnMsg = "";
		String goodsCode   = paramMap.getString("goodsCode");
		String paGroupCode = paramMap.getString("paGroupCode");
		int rtnCnt = 0;
		
		if(paGroupCode.equals("01") || paGroupCode.equals("02") || paGroupCode.equals("03") || paGroupCode.equals("04")) {
			rtnCnt = pacommonDAO.selectPaOriginCheck(goodsCode);
		} else {
			rtnCnt = 1;
		}
		
		if(rtnCnt <= 0) rtnMsg = "B023원산지없음, ";
		
		return rtnMsg;
	}

	@Override
	public String selectPaMobGiftGbCheck(String goodsCode) throws Exception {
		String rtnMsg = "";
		int rtnCnt = 0;
		
		rtnCnt = pacommonDAO.selectPaMobGiftGbCheck(goodsCode);
		if(rtnCnt <= 0) rtnMsg = "모바일이용권 , ";
		return rtnMsg;
	}

	@Override
	public String selectEntpuserCheck(ParamMap paramMap) throws Exception {
		String rtnMsg = "";
		String goodsCode   = paramMap.getString("goodsCode");
		String paGroupCode = paramMap.getString("paGroupCode");
		int rtnCnt = 0;
		
		if(paGroupCode.equals("05")) {
			rtnCnt = pacommonDAO.selectPaEntpuserCheckCopn(goodsCode);
		} else if(paGroupCode.equals("06")) {
			rtnCnt = pacommonDAO.selectEntpuserCheck(goodsCode);
		} else if(paGroupCode.equals("10") || paGroupCode.equals("11")) {
			//출고지, 회수지 모두 지번주소, 도로명주소 필요
			rtnCnt = pacommonDAO.selectEntpuserCheckSsg(goodsCode);
		} else {
			rtnCnt = 1;
		}
		
		if(rtnCnt <= 0) rtnMsg = "출고담당자주소체크, ";
		return rtnMsg;
	}

	@Override
	public String selectPaGoodsdtCnt(ParamMap paramMap) throws Exception {
		String rtnMsg = "";
		String goodsCode   = paramMap.getString("goodsCode");
		String paGroupCode = paramMap.getString("paGroupCode");
		int rtnCnt = 0;
		
		if(paGroupCode.equals("06") || paGroupCode.equals("09")) { // 롯데ON 단품 최대 갯수 확인 후 추가 예정   , 티몬 단품개수 200개 제한 
			rtnCnt = pacommonDAO.selectPaGoodsdtCnt(goodsCode);
		} else if(paGroupCode.equals("08")) { //롯데ON 단품 등록 최대 갯수 : 500개
			rtnCnt = pacommonDAO.selectPaLtonGoodsdtCnt(goodsCode);
	//	} else if(paGroupCode.equals("10")) { //SSG 단품 등록 최대 개수 : 100개
	//		rtnCnt = pacommonDAO.selectPaSsgGoodsdtCnt(goodsCode);
		} else {
			rtnCnt = 1;
		}
		
		if(rtnCnt <= 0) rtnMsg = "단품개수, ";
		
		return rtnMsg;
	}

	@Override
	public String selectPaGoodsdtLength(ParamMap paramMap) throws Exception {
		String rtnMsg = "";
		String goodsCode   = paramMap.getString("goodsCode");
		String paGroupCode = paramMap.getString("paGroupCode");
		int rtnCnt = 0;
		
		if(paGroupCode.equals("05")) {
			rtnCnt = pacommonDAO.selectPaGoodsdtLength(goodsCode);
		} else {
			rtnCnt = 1;
		}
		
		if(rtnCnt <= 0) rtnMsg = "옵션명길이, ";
		
		return rtnMsg;
	}

	@Override
	public String saveTarget(PaGoodsTargetRec paGoodstarget, ParamMap paGoodsInfo) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		String paGroupCode = paGoodstarget.getPaGroupCode();
		String goodsCode = paGoodstarget.getGoodsCode();
		String displayCateList = "";
		List<HashMap<String, String>> ltonDisplayList = null;
		
		int executedRtn = 0;
		
		try {
			
			// 대량 수정(입점) 배치 조건 체크 - 대량입점 : 개발 하다 중지
			/*
			if(checkMassJoinGoods(paGoodstarget)) {
				paGoodstarget.setMassTargetYn	 ("1");
				paGoodstarget.setMassTargetCode	 ("01");
			}*/
			
			paGoodstarget.setDescribeCode(String.valueOf(paGoodsInfo.get("describeCode")));
			//PAGOODS 등록여부 확인
			int newPaGoodsYn = pacommonDAO.selectChkPaGoodsYn(goodsCode);
			if(newPaGoodsYn > 0) {
				executedRtn = pacommonDAO.updatePaGoods(paGoodstarget);
			} else {
				executedRtn = pacommonDAO.insertPaGoods(paGoodstarget);
			}
			if(executedRtn != 1) throw processException("msg.cannot_save", new String[] { "TPAGOODS MERGE" });
			
			paGoodstarget.setNaverOriginCode(paGoodsInfo.getString("naverOriginCode"));
			paGoodstarget.setOrgnTypDtlsCd(paGoodsInfo.getString("orgnTypDtlsCd"));
			paGoodstarget.setPaOriginCode(paGoodsInfo.getString("paOriginCode"));
			paGoodstarget.setOriginEnum(paGoodsInfo.getString("originEnum"));
			paGoodstarget.setOrgnTypCd(paGoodsInfo.getString("orgnTypCd"));
			paGoodstarget.setBrandNo(paGoodsInfo.getString("brandNo"));
			paGoodstarget.setMakerNo(paGoodsInfo.getString("makerNo"));
			
			if(paGroupCode.equals("01")) { //11번가
				
				pacommonDAO.insertPa11stGoods(paGoodstarget);
				paGoodstarget.setCodeGroup("O531");
				paGoodstarget.setCompareGroup("11");
				pacommonDAO.insertPaGoodsPrice(paGoodstarget);
				
			} else if(paGroupCode.equals("02") || paGroupCode.equals("03")) { //G마켓, 이베이
				
				pacommonDAO.insertPaGmktGoods(paGoodstarget);
				pacommonDAO.insertPaGmktGoodsPrice(paGoodstarget);
				
			} else if(paGroupCode.equals("04")) { //네이버
				
				pacommonDAO.insertPaNaverGoods(paGoodstarget);
				paGoodstarget.setCodeGroup("O640");
				paGoodstarget.setCompareGroup("41");
				pacommonDAO.insertPaGoodsPrice(paGoodstarget);
				
			} else if(paGroupCode.equals("05")) { //쿠팡
				
				pacommonDAO.insertPaCopnGoods(paGoodstarget);
				paGoodstarget.setCodeGroup("O669");
				paGoodstarget.setCompareGroup("51");
				pacommonDAO.insertPaGoodsPrice(paGoodstarget);
				
			} else if(paGroupCode.equals("06")) { //위메프
				
				pacommonDAO.insertPaWempGoods(paGoodstarget);
				paGoodstarget.setCodeGroup("O680");
				paGoodstarget.setCompareGroup("61");
				pacommonDAO.insertPaGoodsPrice(paGoodstarget);
					
			} else if(paGroupCode.equals("07")) { //인터파크
				
				pacommonDAO.insertPaIntpGoods(paGoodstarget);
				paGoodstarget.setCodeGroup("O696");
				paGoodstarget.setCompareGroup("71");
				pacommonDAO.insertPaGoodsPrice(paGoodstarget);
				
			} else if(paGroupCode.equals("08")) { // 롯데ON
				displayCateList = "";
				ltonDisplayList = pacommonDAO.selectLtonDisplayCategoryList(paGoodstarget);
				for(int i=0; i<ltonDisplayList.size(); i++) {
					if(i > 0) {
						displayCateList = displayCateList + ",";
					}
					displayCateList = displayCateList + ltonDisplayList.get(i).get("DISP_CAT_ID");
				}
				paGoodstarget.setLfDcatNo(displayCateList);			
				pacommonDAO.insertPaLtonGoods(paGoodstarget);
				paGoodstarget.setCodeGroup("O697");
				paGoodstarget.setCompareGroup("81");
				pacommonDAO.insertPaGoodsPrice(paGoodstarget);
				
			} else if(paGroupCode.equals("09")) { // 티몬
				pacommonDAO.insertPaTmonGoods(paGoodstarget);
				paGoodstarget.setCodeGroup("O698");
				paGoodstarget.setCompareGroup("91");
				pacommonDAO.insertPaGoodsPrice(paGoodstarget);
			} else if(paGroupCode.equals("10")) { // SSG
				pacommonDAO.insertPaSsgGoods(paGoodstarget);
				paGoodstarget.setCodeGroup("O699");
				paGoodstarget.setCompareGroup("A1");
				pacommonDAO.insertPaGoodsPrice(paGoodstarget);
			}
			
			if(paGroupCode.equals("01") || paGroupCode.equals("02") || paGroupCode.equals("03")) {
				pacommonDAO.insertPaPromoTarget(paGoodstarget);
			} else {
				pacommonDAO.insertPaPromoTargetCommon(paGoodstarget);
			}
			
			if(paGroupCode.equals("10")) {
				//프로모션. 우선 SSG.COM만 적용
				
				ParamMap paramMap = new ParamMap();
				paramMap.put("goodsCode", goodsCode);
				paramMap.put("paGroupCode", paGroupCode);
				
				pacommonDAO.savePaPromoTargetHistory(paramMap);
				String compareGoodsCode = "";
				List<PaPromoGoodsPrice> paPromoGoodsPriceList = pacommonDAO.selectPaPromoGoodsPriceList(paramMap);

				for(int i=0; i<paPromoGoodsPriceList.size(); i++) {
					if(compareGoodsCode.equals(paPromoGoodsPriceList.get(i).getGoodsCode())) {
						continue;
					}
					executedRtn = pacommonDAO.insertPaPromoGoodsPrice(paPromoGoodsPriceList.get(i));
					if(executedRtn < 0){
						log.info("TPAPROMOGOODSPRICE insert fail");
						throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICEPROMO INSERT" });
					}
					compareGoodsCode = paPromoGoodsPriceList.get(i).getGoodsCode();
				}
			}
			
			pacommonDAO.insertPaGoodsImage(paGoodstarget);
			pacommonDAO.insertPaGoodsDt(goodsCode);			
			if(paGroupCode.equals("08")) { // 롯데ON 단품 정보는 따로 처리
				pacommonDAO.insertPaLtonGoodsDtMapping(paGoodstarget);
			} else if(paGroupCode.equals("10")){ // SSG 단품 정보는 따로 처리 
				pacommonDAO.insertPaSsgGoodsDtMapping(paGoodstarget);
			} else {
				pacommonDAO.insertPaGoodsDtMapping(paGoodstarget);
			}
			
			if(paGroupCode.equals("05")) {
				if(paGoodsInfo.getInt("chkGoodsOffer") == 0) {
					if(paGoodsInfo.getInt("paCtgEtcChk") > 0) {
						pacommonDAO.insertPaGoodsOfferCopnEtc(goodsCode);
					} else if(!paGoodsInfo.getString("paCtgEtcName").equals("")) {
						pacommonDAO.insertPaGoodsOfferCopnName(paGoodsInfo);
					} else {
						pacommonDAO.insertPaGoodsOffer(paGoodstarget);
					}
				}
				pacommonDAO.insertPaCopnAttriNoLimit(goodsCode);
			} else {
				pacommonDAO.insertPaGoodsOffer(paGoodstarget);
			}
			
			if(paGroupCode.equals("09")) {
				int shipCnt = pacommonDAO.selectChkPaTmonCustShipCostYn(paGoodstarget);
				if(shipCnt == 0) {
					pacommonDAO.insertPaTmonCustShipCost(paGoodstarget);					
				}
			} else {
				int shipCnt = pacommonDAO.selectChkPaCustShipCostYn(paGoodstarget);
				if(shipCnt == 0) {
					pacommonDAO.insertPaCustShipCost(paGoodstarget);
					if(paGroupCode.equals("01")) {
						if(StringUtils.contains(String.valueOf(paGoodstarget.getShipCostCode()), "CN")) {
							pacommonDAO.updateTransCnCostYn(paGoodstarget);
						}
					}
					if(paGroupCode.equals("08")) {
						pacommonDAO.insertPaLtonAddShipCost();
					}
					if(paGroupCode.equals("10")) {
						pacommonDAO.insertPaSsgShipCost();
					}
				}
			}
			
			pacommonDAO.updateTpaGoodsTarget(paGoodstarget);
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}

	@Override
	public String selectOriginMapping11st(String originCode) throws Exception {
		return pacommonDAO.selectOriginMapping11st(originCode);
	}

	@Override
	public String selectOriginMappingGmkt(String originCode) throws Exception {
		return pacommonDAO.selectOriginMappingGmkt(originCode);
	}

	@Override
	public int selectPaCopnCheckGoodsOffer(String goodsCode) throws Exception {
		return pacommonDAO.selectPaCopnCheckGoodsOffer(goodsCode);
	}

	@Override
	public String selectPaCopnPaOfferBo(String goodsCode) throws Exception {
		return pacommonDAO.selectPaCopnPaOfferBo(goodsCode);
	}

	@Override
	public int selectCheckPaCopnAttr(ParamMap paGoodsInfo) throws Exception {
		return pacommonDAO.selectCheckPaCopnAttr(paGoodsInfo);
	}

	@Override
	public String selectPaCopnPaOffer(String goodsCode) throws Exception {
		return pacommonDAO.selectPaCopnPaOffer(goodsCode);
	}

	@Override
	public int selectCheckPaCopnAttrEtc(ParamMap paGoodsInfo) throws Exception {
		return pacommonDAO.selectCheckPaCopnAttrEtc(paGoodsInfo);
	}

	@Override
	public String selectPaCopnAttrEtcName(ParamMap paGoodsInfo) throws Exception {
		return pacommonDAO.selectPaCopnAttrEtcName(paGoodsInfo);
	}

	@Override
	public int inertPaGoodsQaLog(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonDAO.inertPaGoodsQaLog(paGoodstarget);
	}

	@Override
	public int deletePaGoodsTarget(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonDAO.deletePaGoodsTarget(paGoodstarget);
	}

	@Override
	public List<SpPaOfferInfo> selectOfferInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectOfferInfoList(paramMap);
	}

	@Override
	public String saveOfferInfo(SpPaOfferInfo paOfferData, Set<HashMap<String, String>> transTargetSet, Set<HashMap<String, String>> saleStopSet) throws Exception {
		HashMap<String, Object> offerTypeChkData = new HashMap<String, Object>();
		HashMap<String, String> paramMap = new HashMap<String, String>();
		String rtnMsg = Constants.SAVE_SUCCESS;
		String goodsCode   = paOfferData.getGoodsCode();
		
		paramMap.put("dateTime"		, paOfferData.getDateTime());
		paramMap.put("goodsCode"	, goodsCode);
		paramMap.put("paGroupCode"	, paOfferData.getPaGroupCode());
		
		try {
			if(paOfferData.getProcGb().equals("U")) {
				pacommonDAO.updatePaOfferData(paOfferData);
				
			} else {  //PROC_GB = 'I'
				offerTypeChkData = pacommonDAO.selectOfferTypeCheck(paOfferData);
				if(offerTypeChkData == null || offerTypeChkData.isEmpty()) {
					offerTypeChkData.put("PA_OFFER_TYPE", "NONE");
				}
				paOfferData.setPaOfferType(offerTypeChkData.get("PA_OFFER_TYPE").toString());
				pacommonDAO.updatePaOfferUseYn(paOfferData);					
				
				if(String.valueOf(offerTypeChkData.get("PA_OFFER_TYPE")).equals("NONE")) {
					saleStopSet.add(paramMap);
				} else {
					if(String.valueOf(offerTypeChkData.get("PRE_INPUT_YN")).equals("0")) {
						pacommonDAO.insertPaofferData(paOfferData);
					} else {
						pacommonDAO.updatePaoffergoodsData(paOfferData);
					}
					if(paOfferData.getPaGroupCode().toString().equals("06")) {
						pacommonDAO.updatePaWempGoodsGroupNoticeNo(paramMap);
					}
				}
			}
			
			transTargetSet.add(paramMap);
		
		} catch(Exception e) {
			rtnMsg = Constants.SAVE_FAIL;
		}
		return rtnMsg;
	}
		

	@Override
	public List<SpPaGoodsInfo> selectGoodsInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectGoodsInfoList(paramMap);
	}

	@Override
	public String saveGoodsInfo(SpPaGoodsInfo paGoodsData) throws Exception {
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		String newSaleEndDate = "";
		String oldSaleEndDate = "";
		String goodsCode = paGoodsData.getGoodsCode();
		
		paramMap.put("dateTime", paGoodsData.getDateTime());
		paramMap.put("goodsCode", goodsCode);
		
		newSaleEndDate = ComUtil.isNull(DateUtil.timestampToString(paGoodsData.getNewSaleEndDate()), "2999/01/01 00:00:00");
		oldSaleEndDate = ComUtil.isNull(DateUtil.timestampToString(paGoodsData.getOldSaleEndDate()), "2999/01/01 00:00:00");
		
		try {
			if(!paGoodsData.getNewMakecoCode().equals(paGoodsData.getOldMakecoCode()) 		|| !paGoodsData.getNewBrandName().equals(paGoodsData.getOldBrandName())   			  ||
			   !paGoodsData.getNewOriginCode().equals(paGoodsData.getOldOriginCode()) 		|| !paGoodsData.getNewOriginName().equals(paGoodsData.getOldOriginName()) 			  ||
			   !paGoodsData.getNewTaxYn().equals(paGoodsData.getOldTaxYn()) 		  		|| !paGoodsData.getNewTaxSmallYn().equals(paGoodsData.getOldTaxSmallYn())			  ||
			   !paGoodsData.getNewAdultYn().equals(paGoodsData.getOldAdultYn()) 	  		|| paGoodsData.getNewOrderMinQty() != paGoodsData.getOldOrderMinQty() 			 	  ||
			   paGoodsData.getNewOrderMaxQty() != paGoodsData.getOldOrderMaxQty() 	  		|| paGoodsData.getNewCustOrdQtyCheckTerm() != paGoodsData.getOldCustOrdQtyCheckTerm() ||
			   !paGoodsData.getNewEntpCode().equals(paGoodsData.getOldEntpCode()) 	  		|| !paGoodsData.getNewShipManSeq().equals(paGoodsData.getOldShipManSeq()) 			  ||
			   !paGoodsData.getNewReturnManSeq().equals(paGoodsData.getOldReturnManSeq()) 	|| !paGoodsData.getNewShipCostCode().equals(paGoodsData.getOldShipCostCode()) 		  ||
			   paGoodsData.getNewAvgDelyLeadtime() != paGoodsData.getOldAvgDelyLeadtime() 	|| !paGoodsData.getNewGoodsName().equals(paGoodsData.getOldGoodsName()) 			  ||
			   !paGoodsData.getNewSaleStartDate().equals(paGoodsData.getOldSaleStartDate()) || !paGoodsData.getNewOrderCreateYn().equals(paGoodsData.getOldOrderCreateYn()) 	  ||
			   !String.valueOf(paGoodsData.getNewKeyword()).equals(String.valueOf(paGoodsData.getOldKeyword())) || !paGoodsData.getNewCollectYn().equals(paGoodsData.getOldCollectYn()) ||
			   !paGoodsData.getNewDoNotIslandDelyYn().equals(paGoodsData.getOldDoNotIslandDelyYn())         ||  paGoodsData.getSyncMode().equals("E") || !newSaleEndDate.equals(oldSaleEndDate)) {
				
				paGoodsData.setNewSaleEndDate(DateUtil.toTimestamp(newSaleEndDate));
				paGoodsData.setOldSaleEndDate(DateUtil.toTimestamp(oldSaleEndDate));
				
				pacommonDAO.updatePaGoodsData	   (paGoodsData);
//				pacommonDAO.updatePa11stGoodsSync  (paGoodsData);
//				pacommonDAO.updatePaGmktGoodsSync  (paGoodsData);
				pacommonDAO.updatePaNaverGoodsSync (paGoodsData);
//				pacommonDAO.updatePaCopnGoodsSync  (paGoodsData);
//				pacommonDAO.updatePaWempGoodsSync  (paGoodsData);
//				pacommonDAO.updatePaIntpGoodsSync  (paGoodsData);
//				pacommonDAO.updatePaLtonGoodsSync  (paGoodsData);
//				pacommonDAO.updatePaTmonGoodsSync  (paGoodsData);
				pacommonDAO.updatePaSsgGoodsSync  (paGoodsData);
				//pacommonDAO.updatePaKakaoGoodsSync  (paGoodsData);
				pacommonDAO.updateNaverMappingTrans(paramMap);
				
				// 출고지/회수지 변경이 될 경우 이력 관리-네이버용
				if(paGoodsData.getPaCodeChk() != 0) {
					paramMap.put("entpCode",   paGoodsData.getNewEntpCode());
					if(!paGoodsData.getNewShipManSeq().equals(paGoodsData.getOldShipManSeq())) {
						paramMap.put("entpManSeq", paGoodsData.getNewShipManSeq());
						pacommonDAO.insertPaEntpSlipChange(paramMap);
					}
					if(!paGoodsData.getNewReturnManSeq().equals(paGoodsData.getOldReturnManSeq())) {
						paramMap.put("entpManSeq", paGoodsData.getNewReturnManSeq());
						pacommonDAO.insertPaEntpSlipChange(paramMap);
					}
				}
			} else {
				pacommonDAO.updatePaGoodsSyncDate(paGoodsData);
			}
		} catch(Exception e) {
			log.info("STEP 2. Error - GOODS_CODE : " + paGoodsData.getGoodsCode());
			rtnMsg = Constants.SAVE_FAIL;
			throw e;
		}
		return rtnMsg;
	}

	@Override
	public List<SpPaGoodsDtInfo> selectGoodsDtInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectGoodsDtInfoList(paramMap);
	}

	@Override
	public String saveGoodsDtInfo(SpPaGoodsDtInfo paGoodsDtInfo, String flag) throws Exception {
		ParamMap paramMap = new ParamMap();
		String rtnMsg     = Constants.SAVE_SUCCESS;
		String goodsCode  = paGoodsDtInfo.getGoodsCode();
		
		paramMap.put("dateTime", paGoodsDtInfo.getDateTime());
		paramMap.put("goodsCode", goodsCode);
		
		try {
			if(paGoodsDtInfo.getProcGb().equals("U")) {
				pacommonDAO.updatePaGoodsDtData(paGoodsDtInfo);
			} else if(paGoodsDtInfo.getProcGb().equals("M")) {
				pacommonDAO.insertGoodsDtMappingData(paGoodsDtInfo);
//				pacommonDAO.insertPaLtonGoodsDtMappingData(paGoodsDtInfo);
				pacommonDAO.insertPaSsgGoodsDtMappingData(paGoodsDtInfo);
				
			} else { // "I"
				pacommonDAO.insertPaGoodsDtData(paGoodsDtInfo);
				pacommonDAO.insertGoodsDtMappingData(paGoodsDtInfo);
//				pacommonDAO.insertPaLtonGoodsDtMappingData(paGoodsDtInfo);
				pacommonDAO.insertPaSsgGoodsDtMappingData(paGoodsDtInfo);
			}
			pacommonDAO.updatePaGoodsDtMappingData(paGoodsDtInfo);
			
			if(flag.equals("Y")) {
//				pacommonDAO.update11stTrans (paramMap);
//				pacommonDAO.updateGmktTrans (paramMap);
				pacommonDAO.updateNaverTrans(paramMap);
//				pacommonDAO.updateCopnTrans (paramMap);
//				pacommonDAO.updateWempTrans (paramMap);
//				pacommonDAO.updateIntpTrans (paramMap);
//				pacommonDAO.updateLtonTrans (paramMap);
//				pacommonDAO.updateTmonTrans (paramMap);				
				pacommonDAO.updateSsgTrans (paramMap);
				//pacommonDAO.updateKakaoTrans (paramMap);	
			}
			
		} catch(Exception e) {
			rtnMsg = Constants.SAVE_FAIL;
			log.info("STEP 3. Error - GOODS_CODE : " + paGoodsDtInfo.getGoodsCode());
			throw e;
		}
		return rtnMsg;
	}
	
	@Override
	public List<SpPaGoodsDescInfo> selectPaGoodsDescInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectPaGoodsDescInfoList(paramMap);
	}

	@Override
	public String saveGoodsDescInfo(SpPaGoodsDescInfo paGoodsDescInfo) throws Exception {
		ParamMap paramMap = new ParamMap();
		String goodsCode = paGoodsDescInfo.getGoodsCode();
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		paramMap.put("dateTime", paGoodsDescInfo.getDateTime());
		paramMap.put("goodsCode", goodsCode);
		
		try {
			pacommonDAO.updatePaGoodsDescribe(paGoodsDescInfo);
//			pacommonDAO.update11stTrans(paramMap);
//			pacommonDAO.updateGmktTrans(paramMap);
			pacommonDAO.updateNaverTrans(paramMap);
			pacommonDAO.updateNaverMappingTrans(paramMap);
//			pacommonDAO.updateCopnTrans(paramMap);
//			pacommonDAO.updateWempTrans(paramMap);
//			pacommonDAO.updateIntpTrans(paramMap);
//			pacommonDAO.updateLtonTrans(paramMap);
//			pacommonDAO.updateTmonTrans(paramMap);
			pacommonDAO.updateSsgTrans(paramMap);
			//pacommonDAO.updateKakaoTrans(paramMap);	
		} catch(Exception e) {
			rtnMsg = Constants.SAVE_FAIL;
			log.info("STEP 4. Error - GOODS_CODE : " + paGoodsDescInfo.getGoodsCode());
			throw e;
		}
		return rtnMsg;
	}

	@Override
	public List<HashMap<String, String>> selectSaleEndDateTargetList() throws Exception {
		return pacommonDAO.selectSaleEndDateTargetList();
	}

	@Override
	public String saveSaleEndDateTargetInfo(HashMap<String, String> saleEndDateTarget) throws Exception {
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		String goodsCode   = saleEndDateTarget.get("GOODS_CODE");
		String paGroupCode = saleEndDateTarget.get("PA_GROUP_CODE");

		paramMap.put("dateTime", saleEndDateTarget.get("dateTime"));
		paramMap.put("goodsCode", goodsCode);
		try {
			switch (paGroupCode) {
			case "01":
				pacommonDAO.updatePa11stGoodsSaleGb(paramMap);
				break;
			case "02":
			case "03":
				pacommonDAO.updatePaGmktGoodsSaleGb(paramMap);
				break;
			case "04":
				pacommonDAO.updatePaNaverGoodsSaleGb(paramMap);
				break;
			case "05":
				pacommonDAO.updatePaCopnGoodsSaleGb(paramMap);
				break;
			case "06":
				pacommonDAO.updatePaWempGoodsSaleGb(paramMap);
				break;
			case "07":
				pacommonDAO.updatePaIntpGoodsSaleGb(paramMap);
				break;
			case "08":
				pacommonDAO.updatePaLtonGoodsSaleGb(paramMap);
				break;
			case "09":
				pacommonDAO.updatePaTmonGoodsSaleGb(paramMap);
				break;
			case "10":
				pacommonDAO.updatePaSsgGoodsSaleGb(paramMap);
				break;
			//case "11":
			//	pacommonDAO.updatePaKakaoGoodsSaleGb(paramMap);
			//	break;
			default:
				break;
			}
		} catch(Exception e) {
			rtnMsg = Constants.SAVE_FAIL;
			log.info("STEP 6. Error - GOODS_CODE : " + goodsCode);
			throw e;
		}
		return rtnMsg;
	}

	@Override
	public List<SpAuthTransInfo> selectGoodsAuthTransList() throws Exception {
		return pacommonDAO.selectGoodsAuthTransList();
	}

	@Override
	public String saveGoodsAuthTransInfo(SpAuthTransInfo authTransInfo) throws Exception {
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		String paGroupCode = authTransInfo.getPaGroupCode();
		
		paramMap.put("dateTime", authTransInfo.getDateTime());
		paramMap.put("goodsCode", authTransInfo.getGoodsCode());
		
		try {
			switch (paGroupCode) {
			case "01":
				pacommonDAO.update11stGoodsAuthTrans(authTransInfo);
				break;
			case "02":
			case "03":
				pacommonDAO.updateGmktGoodsAuthTrans(authTransInfo);
				break;
			case "04":
				pacommonDAO.updateNaverGoodsAuthTrans(authTransInfo);
				pacommonDAO.updateNaverMappingTrans(paramMap);
				break;
			case "05":
				pacommonDAO.updateCopnGoodsAuthTrans(authTransInfo);
				break;
			case "06":
				pacommonDAO.updateWempGoodsAuthTrans(authTransInfo);
				break;
			case "07":
				pacommonDAO.updateIntpGoodsAuthTrans(authTransInfo);
				break;
			case "08":
				pacommonDAO.updateLtonGoodsAuthTrans(authTransInfo);
				break;
			case "09":
				pacommonDAO.updateTmonGoodsAuthTrans(authTransInfo);
				break;
			case "10":
				pacommonDAO.updateSsgGoodsAuthTrans(authTransInfo);
				break;
			default:
				break;
			}
		} catch(Exception e) {
			rtnMsg = Constants.SAVE_FAIL;
			log.info("상품 자동재입점 선행 Error - GOODS_CODE : " + authTransInfo.getGoodsCode());
			throw e;
		}
		
		return rtnMsg;
	}

	@Override
	public List<HashMap<String, String>> selectGoodsEventTransInfoList() throws Exception {
		return pacommonDAO.selectGoodsEventTransInfoList();
	}

	@Override
	public String saveGoodsEventTransInfo(HashMap<String, String> goodsEventTransData) throws Exception {
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		String goodsCode   = goodsEventTransData.get("GOODS_CODE");
		String paGroupCode = goodsEventTransData.get("PA_GROUP_CODE");
		
		paramMap.put("dateTime", goodsEventTransData.get("dateTime"));
		paramMap.put("paGroupCode", paGroupCode);
		paramMap.put("goodsCode", goodsCode);
		
		try {
			switch (paGroupCode) {
			case "01":
				pacommonDAO.update11stGoodsEventTrans(paramMap);
				break;
			case "02":
			case "03":
				pacommonDAO.updateGmktGoodsEventTrans(paramMap);
				break;
			case "04":
				pacommonDAO.updateNaverGoodsEventTrans(paramMap);
				pacommonDAO.updateNaverMappingTrans(paramMap);
				break;
			case "05":
				pacommonDAO.updateCopnGoodsEventTrans(paramMap);
				break;
			case "06":
				pacommonDAO.updateWempGoodsEventTrans(paramMap);
				break;
			case "07":
				pacommonDAO.updateIntpGoodsEventTrans(paramMap);
				break;
			case "08":
				pacommonDAO.updateLtonGoodsEventTrans(paramMap);
				break;
			case "09":
				pacommonDAO.updateTmonGoodsEventTrans(paramMap);
				break;
			case "10":
				pacommonDAO.updateSsgGoodsEventTrans(paramMap);
				break;
			default:
				break;
			}
		} catch(Exception e) {
			rtnMsg = Constants.SAVE_FAIL;
			log.info("행사상품 재입점 Error - GOODS_CODE : " + goodsCode);
			throw e;
		}
			
		return rtnMsg;
	}

	@Override
	public List<PaPromoTarget> selectPaPromoTargetAllList() throws Exception {
		
		List<PaPromoTarget> paPromomTargetAllList = pacommonDAO.selectPaPromoTargetAllList();
		
		checkMassModifyPromotion(paPromomTargetAllList);
				
		return paPromomTargetAllList;
		
	}

	@Override
	public String savePaPromoTargetAll(PaPromoTarget paPromotargetAllData) throws Exception {
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		String goodsCode = paPromotargetAllData.getGoodsCode();
		
		paramMap.put("dateTime"			, paPromotargetAllData.getDateTime());
		paramMap.put("goodsCode"		, goodsCode);
		paramMap.put("paGroupCode"		, paPromotargetAllData.getPaGroupCode());
		paramMap.put("massTargetYn"		, paPromotargetAllData.getMassTargetYn());
		paramMap.put("massTargetCode"	, "02");
		
		try {
			pacommonDAO.insertPaPromoTargetAll(paPromotargetAllData);
			updatePaPromoTransTarget(paramMap);
		} catch(Exception e) {
			rtnMsg = Constants.SAVE_FAIL;
			log.info("판매가 기준변경을 위한 즉시할인 프로모션 동기화  Error - GOODS_CODE : " + goodsCode);
			//throw e;
		}
		return rtnMsg;
	}
	
	@Override
	public List<PaGoodsImage> selectCurImageInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurImageInfoList(paramMap);
	}
	
	@Override
	public List<PaGoodsPriceVO> selectCurPriceInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurPriceInfoList(paramMap);
	}
	
	@Override
	public List<PaCustShipCostVO> selectCurShipCostInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurShipCostInfoList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurShipStopSaleList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurShipStopSaleList(paramMap);
	}
	
	@Override
	public List<PaEntpSlip> selectCurEntpSlipInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurEntpSlipInfoList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurSaleStopList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurSaleStopList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurEventMarginList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurEventMarginList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurCheckDtCntList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurCheckDtCntList(paramMap);
	}
	
	@Override
	public List<PaLtonGoodsVO> selectCurGoodsTransQtyList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurGoodsTransQtyList(paramMap);
	}
	
	@Override
	public List<PaLtonGoodsdtMappingVO> selectCurGoodsDtTransQtyList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurGoodsDtTransQtyList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurStockCheckList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurStockCheckList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurEpNameInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurEpNameInfoList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurEntpSlipChangeInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurEntpSlipChangeInfoList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurCnShipCostInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurCnShipCostInfoList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurCnShipCostDtInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurCnShipCostDtInfoList(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurCnShipCostTransSingle(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurCnShipCostTransSingle(paramMap);
	}
	
	@Override
	public List<HashMap<String, String>> selectCurCnShipCostTransMulti(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectCurCnShipCostTransMulti(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectMinMarginPrice(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectMinMarginPrice(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectApplyCnCostSeq(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectApplyCnCostSeq(paramMap);
	}
	
	@Override
	public HashMap<String, String> selectMaxOrdCost(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectMaxOrdCost(paramMap);
	}
	
	@Override
	public String saveCurImageInfo(PaGoodsImage curImageInfoTarget) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		ParamMap paramMap = new ParamMap();
		
		try {		
			
			executedRtn = pacommonDAO.updateTpaGoodsImage(curImageInfoTarget);
			if (executedRtn < 0) {
				log.info("TPAGOODSIMAGE update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });
			}
			
			paramMap.put("dateTime", curImageInfoTarget.getRemark());
			paramMap.put("goodsCode", curImageInfoTarget.getGoodsCode());
			paramMap.put("paGroupCode", curImageInfoTarget.getPaGroupCode());
			
			if("01".equals(curImageInfoTarget.getPaGroupCode())) {
				executedRtn = pacommonDAO.update11stTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPA11STGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
				}
			} else if("02".equals(curImageInfoTarget.getPaGroupCode())) {
				executedRtn = pacommonDAO.updateGmktTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPAGMKTGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
				}
			} else if("04".equals(curImageInfoTarget.getPaGroupCode())) {
				executedRtn = pacommonDAO.updateNaverTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPANAVERGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPANAVERGOODS UPDATE" });
				}
				executedRtn = pacommonDAO.updateNaverMappingTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPAGOODSDTMAPPING update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
				}
			} else if("05".equals(curImageInfoTarget.getPaGroupCode())) {
				executedRtn = pacommonDAO.updateCopnTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPACOPNGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPACOPNGOODS UPDATE" });
				}
			} else if("06".equals(curImageInfoTarget.getPaGroupCode())) {
				executedRtn = pacommonDAO.updateWempTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPAWEMPGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPAWEMPGOODS UPDATE" });
				}
			} else if("07".equals(curImageInfoTarget.getPaGroupCode())) {
				executedRtn = pacommonDAO.updateIntpTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPAINTPGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPAINTPGOODS UPDATE" });
				}
			} else if("08".equals(curImageInfoTarget.getPaGroupCode())) {
				executedRtn = pacommonDAO.updateLtonTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPALTONGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPALTONGOODS UPDATE" });
				}
			} else if("09".equals(curImageInfoTarget.getPaGroupCode())) {
				executedRtn = pacommonDAO.updateTmonTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPATMONGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPATMONGOODS UPDATE" });
				}
			} else if("10".equals(curImageInfoTarget.getPaGroupCode())) {
				executedRtn = pacommonDAO.updateSsgTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPASSGGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPASSGGOODS UPDATE" });
				}
			} /*else if("11".equals(curImageInfoTarget.getPaGroupCode())) {
				pacommonDAO.deletePaKakaoGoodsImage(paramMap);
				pacommonDAO.insertPaKakaoGoodsImage(paramMap);
				executedRtn = pacommonDAO.updateKakaoTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPASSGGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPASSGGOODS UPDATE" });
				}
			}*/
			//TPAGOODSSYNCLOG 테이블에 인서트.			
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String saveCurPriceInfo(PaGoodsPriceVO curPriceInfoTarget) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		ParamMap paramMap = new ParamMap();
		String priceChangeYn = null;
		
		try {
             priceChangeYn = pacommonDAO.selectPriceChangeYn(curPriceInfoTarget);            
			// 제휴에 연동하는 가격이 변하지 않는다면, 제휴에 가격 연동된것처럼 업데이트하고, 실제 API 통신 않도록 예외처리함 2021.12.31 leejy 			
			// priceChangeYn == 'Y' (제휴연동에 연동한 가격이 다시 변경됨), priceChangeYn == 'N' (제휴연동에 연동한 가격이 변경되지 않음)
			// 제휴에 연동하는 가격이 변하지 않는다면 trans_date, trans_id 임의로 세팅, tarns_target_yn 업데이트 하지 않는다.
			if(("A1".equals(curPriceInfoTarget.getPaCode()) || "A2".equals(curPriceInfoTarget.getPaCode()))) { // 2022.03.21 SSG의 경우 PRICE_SKIP 처리하지 않음(매입가도 체크해야하는 이슈있음)
				priceChangeYn = "Y";
				curPriceInfoTarget.setTransDate(null);
				curPriceInfoTarget.setTransId(null);
			} else if(priceChangeYn == null || ("").equals(priceChangeYn)){ // selectPriceChangeYn => null 일때도 가격 정상 연동 처리 
				priceChangeYn = "Y";
				curPriceInfoTarget.setTransDate(null);
				curPriceInfoTarget.setTransId(null);
			} else if(("Y").equals(priceChangeYn)) { // 제휴연동에 연동한 가격이 다시 변경되었다면 재연동 처리 
				curPriceInfoTarget.setTransDate(null);
				curPriceInfoTarget.setTransId(null);				
			} else { //제휴연동에 연동한 가격이 변경되지 않았다면 연동된 것처럼 업데이트 처리
				curPriceInfoTarget.setTransDate(DateUtil.toTimestamp(systemDAO.getSysdatetime()));
				curPriceInfoTarget.setTransId("PRICE_SKIP");
			}		
						
			executedRtn = pacommonDAO.insertTpaGoodsPrice(curPriceInfoTarget);
			if (executedRtn < 0) {
				log.info("TPAGOODSPRICE insert fail");
				
				throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICE INSERT" });
			}
			
			paramMap.put("dateTime", curPriceInfoTarget.getDateTime());
			paramMap.put("goodsCode", curPriceInfoTarget.getGoodsCode());
			paramMap.put("transDiscountYn", "1");

			//제휴에 연동하는 가격이 변이 변할때만 제휴상품테이블에 trans_target_yn = '1' 처리함
			if(priceChangeYn != null && !("").equals(priceChangeYn)){				
				if(priceChangeYn.equals("Y")){					
					if("11".equals(curPriceInfoTarget.getPaCode()) || "12".equals(curPriceInfoTarget.getPaCode())) {
						executedRtn = pacommonDAO.update11stTrans(paramMap);
						if (executedRtn < 0) {
							log.info("TPA11STGOODS update fail");
							throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
						}	
					} else if("21".equals(curPriceInfoTarget.getPaCode()) || "22".equals(curPriceInfoTarget.getPaCode())) {
						executedRtn = pacommonDAO.updateGmktTrans(paramMap);
						if (executedRtn < 0) {
							log.info("TPAGMKTGOODS update fail");
							throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
						}	
					} else if("41".equals(curPriceInfoTarget.getPaCode())) {
						executedRtn = pacommonDAO.updateNaverTrans(paramMap);
						if (executedRtn < 0) {
							log.info("TPANAVERGOODS update fail");
							throw processException("msg.cannot_save", new String[] { "TPANAVERGOODS UPDATE" });
						}
						executedRtn = pacommonDAO.updateNaverMappingTrans(paramMap);
						if (executedRtn < 0) {
							log.info("TPAGOODSDTMAPPING update fail");
							throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
						}
					} else if("51".equals(curPriceInfoTarget.getPaCode()) || "52".equals(curPriceInfoTarget.getPaCode())) {
						executedRtn = pacommonDAO.updateCopnTrans(paramMap);
						if (executedRtn < 0) {
							log.info("TPACOPNGOODS update fail");
							throw processException("msg.cannot_save", new String[] { "TPACOPNGOODS UPDATE" });
						}	
					} else if("61".equals(curPriceInfoTarget.getPaCode()) || "62".equals(curPriceInfoTarget.getPaCode())) {
						executedRtn = pacommonDAO.updateWempTrans(paramMap);
						if (executedRtn < 0) {
							log.info("TPAWEMPGOODS update fail");
							throw processException("msg.cannot_save", new String[] { "TPAWEMPGOODS UPDATE" });
						}	
					} else if ("71".equals(curPriceInfoTarget.getPaCode()) || "72".equals(curPriceInfoTarget.getPaCode())) {
						executedRtn = pacommonDAO.updateIntpTrans(paramMap);
						if (executedRtn < 0) {
							log.info("TPAINTPGOODS update fail");
							throw processException("msg.cannot_save", new String[] { "TPAINTPGOODS UPDATE" });
						}
					} else if ("81".equals(curPriceInfoTarget.getPaCode()) || "82".equals(curPriceInfoTarget.getPaCode())) {
						executedRtn = pacommonDAO.updateLtonTrans(paramMap);
						if (executedRtn < 0) {
							log.info("TPALTONGOODS update fail");
							throw processException("msg.cannot_save", new String[] { "TPALTONGOODS UPDATE" });
						}				
					} else if ("91".equals(curPriceInfoTarget.getPaCode()) || "92".equals(curPriceInfoTarget.getPaCode())) {
						executedRtn = pacommonDAO.updateTmonTrans(paramMap);
						if (executedRtn < 0) {
							log.info("TPATMONGOODS update fail");
							throw processException("msg.cannot_save", new String[] { "TPATMONGOODS UPDATE" });
						}				
					} else if ("A1".equals(curPriceInfoTarget.getPaCode()) || "A2".equals(curPriceInfoTarget.getPaCode())) {
						executedRtn = pacommonDAO.updateSsgTrans(paramMap);
						if (executedRtn < 0) {
							log.info("TPASSGGOODS update fail");
							throw processException("msg.cannot_save", new String[] { "TPASSGGOODS UPDATE" });
						}				
					}/* else if ("B1".equals(curPriceInfoTarget.getPaCode()) || "B2".equals(curPriceInfoTarget.getPaCode())) {
						executedRtn = pacommonDAO.updateKakaoTrans(paramMap);
						if (executedRtn < 0) {
							log.info("TPAKAKAOGOODS update fail");
							throw processException("msg.cannot_save", new String[] { "TPAKAKAOGOODS UPDATE" });
						}				
					}*/
				}			
			}
			//TPAGOODSSYNCLOG 테이블에 인서트.
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String saveCurShipCostInfo(PaCustShipCostVO curShipCostInfoTarget) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {		
			
			if("91".equals(curShipCostInfoTarget.getPaCode()) || "92".equals(curShipCostInfoTarget.getPaCode())) {
				//티몬이면
				executedRtn = pacommonDAO.insertTpaTmonCustShipCost(curShipCostInfoTarget);
			} else if("U".equals(curShipCostInfoTarget.getProcGb())) {
				executedRtn = pacommonDAO.updateTpaCustShipCost(curShipCostInfoTarget);
			} else {
				executedRtn = pacommonDAO.insertTpaCustShipCost(curShipCostInfoTarget);
			}
			
			if (executedRtn < 0) {
				log.info("TPACUSTSHIPCOST update/insert fail");
				throw processException("msg.cannot_save", new String[] { "TPACUSTSHIPCOST UPDATE INSERT" });
			}
			
			//11번가 조건부 배송비일 경우
			if("11".equals(curShipCostInfoTarget.getPaCode()) || "12".equals(curShipCostInfoTarget.getPaCode())) {
				if("CN".equals(curShipCostInfoTarget.getShipCostCode().substring(0,2))) {
					executedRtn = pacommonDAO.updateTransCnCostYn2(curShipCostInfoTarget);
					if (executedRtn < 0) {
						log.info("TPACUSTSHIPCOST update fail");
						throw processException("msg.cannot_save", new String[] { "TPACUSTSHIPCOST UPDATE" });
					}
				}
			}
			
			if(curShipCostInfoTarget.getPaCode().equals("81") || curShipCostInfoTarget.getPaCode().equals("82")) {
				pacommonDAO.insertPaLtonAddShipCost();
			}
			
			if(curShipCostInfoTarget.getPaCode().equals("A1") || curShipCostInfoTarget.getPaCode().equals("A2")) {
				pacommonDAO.insertPaSsgShipCost();
			}
			
			List<PaCustShipCostVO> PaCustShipCostList = new ArrayList<PaCustShipCostVO>();
			PaCustShipCostList.add(curShipCostInfoTarget);
			//checkMessModifyGoods(PaCustShipCostList , "03");
			
			executedRtn = pacommonDAO.updateTransShipCost(PaCustShipCostList.get(0));
			if (executedRtn < 0) {
				log.info("TPA제휴사GOODS update fail");
				throw processException("msg.cannot_save", new String[] { "TPA제휴사GOODS UPDATE" });
			}
			//TPAGOODSSYNCLOG 테이블에 인서트.
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String saveCurEntpSlipInfo(PaEntpSlip curEntpSlipInfoTarget) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {		
			
			executedRtn = pacommonDAO.updateTpaEntpSlip(curEntpSlipInfoTarget);
			if (executedRtn < 0) {
				log.info("TPAENTPSLIP update fail");
				throw processException("msg.cannot_save", new String[] { "TPAENTPSLIP UPDATE" });
			}
			
			List<PaEntpSlip> PaEntpSlipList = new ArrayList<PaEntpSlip>();
			PaEntpSlipList.add(curEntpSlipInfoTarget);
			//checkMessModifyGoods(PaEntpSlipList , "04");
			curEntpSlipInfoTarget = PaEntpSlipList.get(0);
			
			executedRtn = pacommonDAO.updateTransEntpSlip(curEntpSlipInfoTarget);
			if (executedRtn < 0) {
				log.info("TPA제휴사GOODS update fail");
				throw processException("msg.cannot_save", new String[] { "TPA제휴GOODS UPDATE" });
			}
			//TPAGOODSSYNCLOG 테이블에 인서트.
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String saveCurSaleStopInfo(ParamMap paramMap) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {		
			
			executedRtn = pacommonDAO.updateStopSale(paramMap);
			if (executedRtn < 0) {
				log.info("TPA제휴GOODS update fail");
				throw processException("msg.cannot_save", new String[] { "TPA제휴GOODS UPDATE" });
			}
			
			executedRtn = pacommonDAO.insertSaleNoGoods(paramMap);
			if (executedRtn < 0) {
				log.info("TPASALENOGOODS insert fail");
				throw processException("msg.cannot_save", new String[] { "TPASALENOGOODS INSERT" });
			}
			
			//TPAGOODSSYNCLOG 테이블에 인서트.			
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String saveCurGoodsTransQty(PaLtonGoodsVO curGoodsTransQtyTarget) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {		
			
			executedRtn = pacommonDAO.updateTpaLtonGoodsQty(curGoodsTransQtyTarget);
			if (executedRtn < 0) {
				log.info("TPALTONGOODS update fail");
				throw processException("msg.cannot_save", new String[] { "TPALTONGOODS UPDATE" });
			}		
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String saveCurGoodsDtTransQty(PaLtonGoodsdtMappingVO curGoodsDtTransQtyTarget) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {		
			
			executedRtn = pacommonDAO.updateTpaLtonGoodsDtQty(curGoodsDtTransQtyTarget);
			
			
			
			
			
			if (executedRtn < 0) {
				log.info("TPALTONGOODS update fail");
				throw processException("msg.cannot_save", new String[] { "TPALTONGOODS UPDATE" });
			}		
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String saveCurStockCheck(ParamMap paramMap) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			if("21".equals(paramMap.get("paCode").toString()) || "22".equals(paramMap.get("paCode").toString())) {
				executedRtn = pacommonDAO.updateGmktTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPAGMKTGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
				}
				executedRtn = pacommonDAO.updatePaGoodsDtMappingTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPAGOODSDTMAPPING update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
				}
			} else if("41".equals(paramMap.get("paCode").toString())) {
				executedRtn = pacommonDAO.updateNaverTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPANAVERGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPANAVERGOODS UPDATE" });
				}
				executedRtn = pacommonDAO.updateNaverMappingTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPAGOODSDTMAPPING update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
				}
			}
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String saveCurNaverEntpSlip(ParamMap paramMap) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = pacommonDAO.updateNaverTrans(paramMap);
			if (executedRtn < 0) {
				log.info("TPANAVERGOODS update fail");
				throw processException("msg.cannot_save", new String[] { "TPANAVERGOODS UPDATE" });
			}
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String savePaCustCnShipCost(ParamMap paramMap) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = pacommonDAO.insertPaCustCnShipCost(paramMap);
			if (executedRtn < 0) {
				log.info("TPACUSTCNSHIPCOST INSERT fail");
				throw processException("msg.cannot_save", new String[] { "TPACUSTCNSHIPCOST INSERT" });
			}
			
			executedRtn = pacommonDAO.updateCnTransYn(paramMap);
			if (executedRtn < 0) {
				log.info("TPACUSTCNSHIPCOST UPDATE fail");
				throw processException("msg.cannot_save", new String[] { "TPACUSTCNSHIPCOST UPDATE" });
			}
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String saveCnCostYn(ParamMap paramMap) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = pacommonDAO.updateCnCostYn(paramMap);
			if (executedRtn < 0) {
				log.info("TPACUSTCNSHIPCOST UPDATE fail");
				throw processException("msg.cannot_save", new String[] { "TPACUSTCNSHIPCOST UPDATE" });
			}
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String saveCnCostTrans(ParamMap paramMap) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			executedRtn = pacommonDAO.updateCnCostTrans(paramMap);
			if (executedRtn < 0) {
				log.info("TPACUSTCNSHIPCOST UPDATE fail");
				throw processException("msg.cannot_save", new String[] { "TPACUSTCNSHIPCOST UPDATE" });
			}
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String saveCnCostTrans2(ParamMap paramMap) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			
			String cnCostSeq = pacommonDAO.selectCnCostSeq(paramMap);
			paramMap.put("cnCostSeq", cnCostSeq);
			
			executedRtn = pacommonDAO.updateCnt2CnCost(paramMap);
			if (executedRtn < 0) {
				log.info("TPACUSTCNSHIPCOST UPDATE fail");
				throw processException("msg.cannot_save", new String[] { "TPACUSTCNSHIPCOST UPDATE" });
			}
			
			executedRtn = pacommonDAO.updateCnt2Target(paramMap);
			if (executedRtn < 0) {
				log.info("TPACUSTCNSHIPCOST UPDATE fail");
				throw processException("msg.cannot_save", new String[] { "TPACUSTCNSHIPCOST UPDATE" });
			}
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}
	
	@Override
	public String saveStopSale(ParamMap paramMap) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {		
			
			executedRtn = pacommonDAO.updateStopSale(paramMap);
			if (executedRtn < 0) {
				log.info("TPA제휴GOODS update fail");
				throw processException("msg.cannot_save", new String[] { "TPA제휴GOODS UPDATE" });
			}
			
			if(!"Y".equals(paramMap.get("priceStopSale").toString())) {
				executedRtn = pacommonDAO.insertSaleNoGoods(paramMap);
				if (executedRtn < 0) {
					log.info("TPASALENOGOODS insert fail");
					throw processException("msg.cannot_save", new String[] { "TPASALENOGOODS INSERT" });
				}
				
				executedRtn = pacommonDAO.updatePagoodstargetAutoYn(paramMap);
				if (executedRtn < 0) {
					log.info("TPAGOODSTARGET update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSTARGET update" });
				}
				
				executedRtn = pacommonDAO.insertPagoodsAuthYnLog(paramMap);
				if (executedRtn < 0) {
					log.info("TPAGOODSAUTHYNLOG insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSAUTHYNLOG INSERT" });
				}
			}
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}

	@Override
	public List<HashMap<String, String>> selectGoodsPaExceptList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectGoodsPaExceptList(paramMap);
	}

	@Override
	public String saveGoodsPaExcept(HashMap<String, String> goodsPaExceptData) throws Exception {
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		String paCode	   = goodsPaExceptData.get("PA_CODE");
		String goodsCode   = goodsPaExceptData.get("GOODS_CODE");
		String paGroupCode = goodsPaExceptData.get("PA_GROUP_CODE");
		
		paramMap.put("paCode", paCode);
		paramMap.put("goodsCode", goodsCode);
		paramMap.put("paGroupCode", paGroupCode);
		paramMap.put("dateTime", goodsPaExceptData.get("dateTime"));
		
		try {
			switch (paGroupCode) {
			case "01":
				pacommonDAO.update11stGoodsExceptTrans(paramMap);
				break;
			case "02":
			case "03":
				pacommonDAO.updateGmktGoodsExceptTrans(paramMap);
				break;
			case "04":
				pacommonDAO.updateNaverGoodsExceptTrans(paramMap);
				break;
			case "05":
				pacommonDAO.updateCopnGoodsExceptTrans(paramMap);
				break;
			case "06":
				pacommonDAO.updateWempGoodsExceptTrans(paramMap);
				break;
			case "07":
				pacommonDAO.updateIntpGoodsExceptTrans(paramMap);
				break;
			case "08":
				pacommonDAO.updateLtonGoodsExceptTrans(paramMap);
				break;
			case "09":
				pacommonDAO.updateTmonGoodsExceptTrans(paramMap);
				break;
			case "10":
				pacommonDAO.updateSsgGoodsExceptTrans(paramMap);
				break;
			default:
				break;
			}
			pacommonDAO.updateTargetAytoYn(paramMap);
		} catch(Exception e) {
			rtnMsg = Constants.SAVE_FAIL;
			log.info("연동제외관리 Error - GOODS_CODE[PA_CODE] : " + goodsCode + "[" + paCode + "]");
			throw e;
		}
		return rtnMsg;
	}

	@Override
	public List<SpPaGoodsDtInfo> selectPaLtonSsgGoodsDtInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectPaLtonSsgGoodsDtInfoList(paramMap);
	}

	@Override
	public String updateLtonSsgGoodsDtInfo(SpPaGoodsDtInfo paLtonSsgGoodsDtInfo) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {
			if("A1".equals(paLtonSsgGoodsDtInfo.getPaCode()) || "A2".equals(paLtonSsgGoodsDtInfo.getPaCode())) {
				//SSG단품정보수정
				executedRtn = pacommonDAO.updateSsgGoodsDtInfo(paLtonSsgGoodsDtInfo);
				if (executedRtn < 0) {
					log.info("TPASSGGOODSDTMAPPING update fail");
					throw processException("msg.cannot_save", new String[] { "TPASSGGOODSDTMAPPING UPDATE" });
				}
				
				executedRtn = pacommonDAO.insertPaSsgGoodsDtMappingUdata(paLtonSsgGoodsDtInfo);
				if (executedRtn < 0) {
					log.info("TPASSGGOODSDTMAPPING insert fail");
					throw processException("msg.cannot_save", new String[] { "TPASSGGOODSDTMAPPING INSERT" });
				}
			} else {
				//롯데온단품정보수정
				executedRtn = pacommonDAO.updateLtonGoodsDtInfo(paLtonSsgGoodsDtInfo);
				if (executedRtn < 0) {
					log.info("TPALTONGOODSDTMAPPING update fail");
					throw processException("msg.cannot_save", new String[] { "TPALTONGOODSDTMAPPING UPDATE" });
				}
				
				executedRtn = pacommonDAO.insertPaLtonGoodsDtMappingUdata(paLtonSsgGoodsDtInfo);
				if (executedRtn < 0) {
					log.info("TPALTONGOODSDTMAPPING insert fail");
					throw processException("msg.cannot_save", new String[] { "TPALTONGOODSDTMAPPING INSERT" });
				}
			}
			
		} catch(Exception e) {
			rtnMsg = Constants.SAVE_FAIL;
			log.info("TPALTON/SSGGOODSDTMAPPING UPDATE FAIL : " + paLtonSsgGoodsDtInfo.getGoodsCode() + " | " + paLtonSsgGoodsDtInfo.getGoodsdtCode());
			throw e;
		}
		return rtnMsg;
	}
	
	public void setExceptReason(ParamMap paramMap){
	//goodsCode, paCode, paGroupCode, limitMarginAmt
		try {
			String sdPromoNo = paramMap.getString("sdPromoNo");   // 연동되는 outPromotion
			String outPromoNo = paramMap.getString("outPromoNo");  // 연동되는 sdPromotion
			
			//1) = 가장 최신 연동일을 가져옴
			HashMap<String, Object> lastDateMap = pacommonDAO.selectLastPromoTransDate(paramMap);
			paramMap.put("transDate", lastDateMap.get("TRANS_DATE"));
			
			//2) = 가장 최신 연동일 이후에 들어온 데이터 중에서 SD와 OUT 가격의 합이 , 최저 마진률을 만족하는 금액보다 큰 데이터 추출  
			List<HashMap<String, Object>> promoList = pacommonDAO.selectPaPromoTargetOverMarginList(paramMap); 
			
			for(HashMap<String, Object> promo : promoList) {
				
				if(!sdPromoNo.equals(promo.get("PROMO_NO_SD"))) {
					paramMap.put("promoNo"			, promo.get("PROMO_NO_SD"));
					paramMap.put("seq"				, promo.get("SEQ_SD"));
					paramMap.put("exceptReason"		, "마진률 초과  , 연동 예외 시간 : " );
					//paramMap.put("exceptReason"		, "마진률 초과 :: " + promo.get("PROMO_NO_OUT") + "/" + promo.get("SEQ_SD"));
					pacommonDAO.updateExceptReason(paramMap);	
				}
				if(!outPromoNo.equals(promo.get("PROMO_NO_OUT"))) {
					paramMap.put("promoNo"			, promo.get("PROMO_NO_OUT"));
					paramMap.put("seq"				, promo.get("SEQ_OUT"));
					paramMap.put("exceptReason"		, "마진률 초과  , 연동 예외 시간 : " );
					pacommonDAO.updateExceptReason(paramMap);
				}
			}
			
		}catch (Exception e) {
			log.info("프로모션 예외사유 갱신 실패 :::" + e.toString());
		}
	}
	
	public void setExceptReason4OnePromotion(ParamMap paramMap) {
		try {
			//1) = 가장 최신 연동일을 가져옴
			HashMap<String, Object> lastDateMap = pacommonDAO.selectLastPromoTransDate(paramMap);
			paramMap.put("transDate"		, lastDateMap.get("TRANS_DATE"));
			paramMap.put("exceptReason"		, "마진률 초과  , 연동 예외 시간 : " );
			pacommonDAO.updateExceptReasonByGoodsCode(paramMap);
			
		}catch (Exception e) {
			log.info("프로모션 예외사유 갱신 실패 :::" + e.toString());
		}
	}

	@Override
	public List<SpPaOfferInfo> selectOfferInfoListInsert(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectOfferInfoListInsert(paramMap);
	}

	@Override
	public List<SpPaOfferInfo> selectOfferInfoListUpdate(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectOfferInfoListUpdate(paramMap);
	}

	@Override
	public void saveOfferGoodsTarget(Set<HashMap<String, String>> transTargetSet, Set<HashMap<String, String>> saleGbStopSet) throws Exception {
		Iterator<HashMap<String, String>> saleIt = saleGbStopSet.iterator();
		ParamMap paramMap = new ParamMap();
		
		while (saleIt.hasNext()) {
			HashMap<String, String> tempMap  = saleIt.next();
			
			paramMap.put("paGroupCode"	, tempMap.get("paGroupCode"));  //paramMap.setParamMap(tempMap);
			paramMap.put("goodsCode"	, tempMap.get("goodsCode"));
			paramMap.put("dateTime"		, tempMap.get("dateTime"));
						
			String paGroupCode = paramMap.getString("paGroupCode");
			
//			if(paGroupCode.equals("01")) {
//				pacommonDAO.updatePa11stGoodsSaleGb(paramMap);
//			} else if(paGroupCode.equals("02") || paGroupCode.equals("03")) {
//				pacommonDAO.updatePaGmktGoodsSaleGb(paramMap);
			if(paGroupCode.equals("04")) {
				pacommonDAO.updatePaNaverGoodsSaleGb(paramMap);
//			} else if(paGroupCode.equals("05")) {
//				pacommonDAO.updatePaCopnGoodsSaleGb(paramMap);
//			} else if(paGroupCode.equals("06")) {
//				pacommonDAO.updatePaWempGoodsSaleGb(paramMap);
//			} else if(paGroupCode.equals("07")) {
//				pacommonDAO.updatePaIntpGoodsSaleGb(paramMap);
//			} else if(paGroupCode.equals("08")) {
//				pacommonDAO.updatePaLtonGoodsSaleGb(paramMap);
//			} else if(paGroupCode.equals("09")) {
//				pacommonDAO.updatePaTmonGoodsSaleGb(paramMap);
			} else if(paGroupCode.equals("10")) {
				pacommonDAO.updatePaSsgGoodsSaleGb(paramMap);
			}
			//pacommonDAO.updatePaGoodsTargetAutoYnOff(paramMap);
			//pacommonDAO.insertPaGoodsAutoYnOffLog(paramMap);   // 각각의 제휴사별 쿼리를 파라미터로 받음으로써 하나로 통합 
		}
		
		
		Iterator<HashMap<String, String>> transTargetIt = transTargetSet.iterator();
		while (transTargetIt.hasNext()) {
			HashMap<String, String> tempMap  = transTargetIt.next();
			
			paramMap.put("paGroupCode"	, tempMap.get("paGroupCode"));  //paramMap.setParamMap(tempMap);
			paramMap.put("goodsCode"	, tempMap.get("goodsCode"));
			paramMap.put("dateTime"		, tempMap.get("dateTime"));
			
			String paGroupCode = paramMap.getString("paGroupCode");
			
			
//			if(paGroupCode.equals("01")) {
//				pacommonDAO.update11stTrans (paramMap);
//			} else if(paGroupCode.equals("02") || paGroupCode.equals("03")) {
//				pacommonDAO.updateGmktTrans (paramMap);
			if(paGroupCode.equals("04")) {
				pacommonDAO.updateNaverTrans(paramMap);
				pacommonDAO.updateNaverMappingTrans(paramMap);
//			} else if(paGroupCode.equals("05")) {
//				pacommonDAO.updateCopnTrans (paramMap);
//			} else if(paGroupCode.equals("06")) {
//				pacommonDAO.updateWempTrans (paramMap);
//			} else if(paGroupCode.equals("07")) {
//				pacommonDAO.updateIntpTrans (paramMap);
//			} else if(paGroupCode.equals("08")) {
//				pacommonDAO.updateLtonTrans (paramMap);
			} else if(paGroupCode.equals("10")) {
				pacommonDAO.updateSsgTrans (paramMap);
			}
			pacommonDAO.updateOfferTrans(paramMap);
		}
	}

	@Override
	public void queryTestFunction() throws Exception {
		ParamMap paramMap = new ParamMap();
		
		paramMap.put("paGroupCode"	, "01");  //paramMap.setParamMap(tempMap);
		paramMap.put("goodsCode"	, "20012341");
					
		pacommonDAO.updatePaGoodsTargetAutoYnOff(paramMap);
	}

	@Override
	public List<HashMap<String, String>> selectTmonCurCheckDtCntList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectTmonCurCheckDtCntList(paramMap);
	}

	@Override
	public String paExceptGoodsYn(ParamMap paramMap) throws Exception {
		
		String rtnMsg 		= "연동제약사항 체크";
		boolean succeess 	= true;
		int executedRtn 	= 0;
		HashMap<String, Object> paExceptListMap = null;
		
		executedRtn = pacommonDAO.selectChkShipCost(paramMap.getString("goodsCode"));
		
		if (executedRtn < 1) {
			rtnMsg += ", 배송비정책";
			succeess = false;
		}
		
		paExceptListMap = pacommonDAO.selectPaCheckGoods(paramMap);
		if (paExceptListMap.get("MARGIN_RATE").equals("N")) {
			rtnMsg += ", 마진율";
			succeess = false;
		}
		if (paExceptListMap.get("SALE_PRICE_YN").equals("N")) {
			rtnMsg += ", 판매가";
			succeess = false;
		}
		if (paExceptListMap.get("STOCK_QTY").equals("N")) {
			rtnMsg += ", 재고";
			succeess = false;
		}
		if (paExceptListMap.get("OMBUDSMAN_YN").equals("N")) {
			rtnMsg += ", 옴부즈맨";
			succeess = false;
		}
		if (paExceptListMap.get("BROAD_SALE_YN").equals("N")) {
			rtnMsg += ", 방송중판매";
			succeess = false;
		}
		if (paExceptListMap.get("INVI_GOODS_TYPE_YN").equals("N")) {
			rtnMsg += ", 무형상품";
			succeess = false;
		}
		if (paExceptListMap.get("ORDER_MEDIA_YN").equals("N")) {
			rtnMsg += ", 주문매체";
			succeess = false;
		}
		if (paExceptListMap.get("SALE_END_YN").equals("N")) {
			rtnMsg += ", 판매종료일";
			succeess = false;
		}
		if(!succeess){
			return rtnMsg;
		}else{
			return "000000";
		}	
		
	}

	@Override
	public List<HashMap<String, String>> selectTmonCurGoodsNameLengthCheckList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectTmonCurGoodsNameLengthCheckList(paramMap);
	}

	@Override
	public String selectPaGoodsNameLength(ParamMap paramMap) throws Exception {
		String rtnMsg = "";
		String paGroupCode = paramMap.getString("paGroupCode");
		int rtnCnt = 0;
		
		if(paGroupCode.equals("11")) {
			rtnCnt = pacommonDAO.selectPaGoodsNameLength(paramMap);
		}
		
		if(rtnCnt > 0) rtnMsg = "상품명길이, ";
		
		return rtnMsg;
	}
	
	//대량입점 ///////////////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unused")
	private boolean checkMassJoinGoods(PaGoodsTargetRec paGoodstarget) {
		Boolean returnVal = false;
		
		if(paGoodstarget.getSourcingCode() == null) 	 	 return false;
		if(paGoodstarget.getSourcingCode().equals("")) 	 	 return false;
	
		String sourcingCode = paGoodstarget.getSourcingCode();
		int cnt = pacommonDAO.selectCheckMassSourcingCode(sourcingCode); //TCODE_B711
		if(cnt > 0 ) returnVal = true;
		
		return returnVal;
	}
	
	@SuppressWarnings("unchecked")
	public void checkMassModifyPromotion(List<?> list) throws Exception {	
		int maxPromotionCnt 	= Integer.parseInt(systemDAO.getVal("MASS_PROMO_CNT"));
		
		try {
			List<PaPromoTarget> paPromomTargetAllList = (List<PaPromoTarget>) list;	
			String massPromo = "";
			int	   cnt		 = 0;
			
			Map<String, Integer> tempMap = new HashMap<String, Integer>();
			
			for(PaPromoTarget pa : paPromomTargetAllList) {
				
				if(tempMap.get(pa.getPromoNo()) != null) {
					cnt = tempMap.get(pa.getPromoNo());
				}
				
				if(cnt < 1) {
					tempMap.put(pa.getPromoNo(), 1);
				}else {
					tempMap.put(pa.getPromoNo(), cnt + 1);
				}
			}
			
			
			Iterator<Map.Entry<String, Integer>> it = tempMap.entrySet().iterator();
			while(it.hasNext()) {
				Map.Entry<String, Integer> entry = (Map.Entry<String, Integer>) it.next();
				String promoNo = entry.getKey();
				int	   count   = entry.getValue();
				
				if(count > maxPromotionCnt) { 
					massPromo += promoNo + " ";
				}
			}
			
			for(PaPromoTarget pa : paPromomTargetAllList) {
				if(massPromo.indexOf(pa.getPromoNo()) > -1 ) {
					if(pa.getPaCode().equals("81") || pa.getPaCode().equals("82")) {
						pa.setMassTargetYn("1");
					}else if(pa.getPaCode().equals("51") || pa.getPaCode().equals("52")) {
						pa.setMassTargetYn("1"); 
					}else if(pa.getPaCode().equals("21") || pa.getPaCode().equals("22")) {
						pa.setMassTargetYn("1"); 
					}else if(pa.getPaCode().equals("11") || pa.getPaCode().equals("12")) {
						pa.setMassTargetYn("1"); 
					}else if(pa.getPaCode().equals("61") || pa.getPaCode().equals("62")) {
						pa.setMassTargetYn("1"); 
					}else if(pa.getPaCode().equals("71") || pa.getPaCode().equals("72")) {
						pa.setMassTargetYn("1");
					}
				}
			}			
		}catch (Exception e) {
			log.error(e.toString());
		}
	}
	
	
	public void checkMassModifyGoods(String paGroupCode) throws Exception {
		try {
			int checkCnt 	= 0;  //N개 이상은 대량 입점 인제휴로 판단한다.
			
			switch (paGroupCode) {
			case "01":
				checkCnt = Integer.parseInt(systemDAO.getVal("MASS_PA11ST_LMT_CNT"));
				break;
			case "02":
				checkCnt = Integer.parseInt(systemDAO.getVal("MASS_PAGMKT_LMT_CNT"));
				break;
			case "05":
				checkCnt = Integer.parseInt(systemDAO.getVal("MASS_PACOPN_LMT_CNT"));
				break;
			case "06":
				checkCnt = Integer.parseInt(systemDAO.getVal("MASS_PAWEMP_LMT_CNT"));
				break;
			case "07":
				checkCnt = Integer.parseInt(systemDAO.getVal("MASS_PAINTP_LMT_CNT"));
				break;				
			case "08":
				checkCnt = Integer.parseInt(systemDAO.getVal("MASS_PALTON_LMT_CNT"));
				break;				
			default:
				return;
			}
						
			ParamMap paramMap 		= new ParamMap();
			
			paramMap.put("checkCnt"		,  checkCnt);
			paramMap.put("paGroupCode"	, paGroupCode);
			
			List<HashMap<String, Object>> pa11stGoodsList =  pacommonDAO.selectGoodsInfoBySourcingCode(paramMap);
			updateMassTargetYnBySourcingCode(pa11stGoodsList, paGroupCode);

		}catch (Exception e) {
			log.error(e.toString());
		}
	}
	
	public void updateMassTargetYnBySourcingCode( List<HashMap<String, Object>> paGoodsList, String paGroupCode) throws Exception{
		
		for(HashMap<String, Object> goods : paGoodsList) {
			goods.put("MASS_TARGET_CODE", "99");
			goods.put("MASS_TARGET_YN"	, "1"); 
			pacommonDAO.updateMassTargetYnBySourcingCode( goods,  paGroupCode);	
		}
	}
	
	private int updatePaPromoTransTarget(ParamMap paramMap) throws Exception{
		String paGroupCode  = paramMap.getString("paGroupCode");
		int 	excuteRtn	=  0;
		
		switch (paGroupCode) {
		case "01":
			excuteRtn =pacommonDAO.update11stTrans(paramMap);
			break;
		case "02":
		case "03":
		case "02,03":
			excuteRtn = pacommonDAO.updateGmktTrans(paramMap);
			break;
		case "04":
			excuteRtn = pacommonDAO.updateNaverTrans(paramMap);
			break;
		case "05":
			excuteRtn = pacommonDAO.updateCopnTrans(paramMap);
			break;
		case "06":
			excuteRtn = pacommonDAO.updateWempTrans(paramMap);
			break;
		case "07":
			excuteRtn = pacommonDAO.updateIntpTrans(paramMap);
			break;
		case "08":
			excuteRtn = pacommonDAO.updateLtonTrans(paramMap);
			break;
		case "09":
			excuteRtn = pacommonDAO.updateTmonTrans(paramMap);
			break;
		default:
			break;
		}
		
		return excuteRtn;
	}

	@Override
	public HashMap<String, Object> selectDescData(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectDescData(paramMap);
	}
	
	@Override
	public List<Object> selectPaSoldOutordList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectPaSoldOutordList(paramMap);
	}
	/**
     * 판매취소처리 상담 생성
     * @param ParamMap
     * @return 
     * @throws Exception
     */
	public void saveOrderCancelCounsel(HashMap<String, String> reqMap) throws Exception{
		
    	Custcounselm  custcounselm  = null;
		Custcounseldt custcounseldt = null;
 		String  counselSeq 	 	    = "";
		HashMap<String, String> srMap = null; 
		String sysdate  = systemDAO.getSysdatetime();
		
		try {
			//제휴 주문건 SR 생성(판매취소 실패 시)
			if(reqMap != null) {
				
				srMap = pacommonDAO.selectCounselInfo(reqMap);
				
				custcounselm  = new Custcounselm();
	 			custcounseldt = new Custcounseldt();
	 			
				//SR 생성 
	    		counselSeq = systemProcess.getSequenceNo("COUNSEL_SEQ");
				if (counselSeq.equals("")) {
					throw processException("msg.cannot_create", new String[] { "COUNSEL_SEQ" });
				}
				//tcustcounselm
				custcounselm.setCounselSeq(counselSeq);
				custcounselm.setClaimNo	  (counselSeq);
				custcounselm.setCustNo(srMap.get("CUST_NO").toString());
				
				// 일괄취소반품 중 성공은 완료까지 생성
				if("06".equals(reqMap.get("ORG_ORD_CAN_YN").toString()) && "10".equals(reqMap.get("ORDER_CANCEL_YN").toString())) {
					custcounselm.setDoFlag("40");
				} else {
					custcounselm.setDoFlag("10");
				}
				
				custcounselm.setRefNo1(reqMap.get("ORDER_NO").toString());
				custcounselm.setOutLgroupCode("99");
				custcounselm.setOutMgroupCode("99");
				
				custcounselm.setCsLgroup("13");
				custcounselm.setCsMgroup("01");
				custcounselm.setCsSgroup("02");
				custcounselm.setCsLmsCode("130102");
				
				custcounselm.setGoodsCode(ComUtil.NVL(srMap.get("GOODS_CODE").toString(), "" ));
				custcounselm.setGoodsdtCode(ComUtil.NVL(srMap.get("GOODSDT_CODE").toString(), "" ));
				custcounselm.setCsSendYn("0");
				
				custcounselm.setInsertId(srMap.get("PROC_ID").toString());
				custcounselm.setProcId(srMap.get("PROC_ID").toString());
		    	custcounselm.setRefId1(pacommonDAO.selectPaRefIdForCounsel(srMap.get("MEDIA_CODE").toString()));
		    	
		    	custcounselm.setQuickYn("0");
		    	custcounselm.setQuickEndYn("0");
		    	custcounselm.setInsertDate(DateUtil.toTimestamp(sysdate));
		    	
				if (paclaimDAO.insertCounselCustcounselm(custcounselm) < 1){
					throw processException("msg.cannot_save", new String[]{"soldOut cancel TCUSTCOUNSELM insert"});
				}
				
				//tcustcounseldt
				custcounseldt.setCounselSeq(counselSeq);
				custcounseldt.setCounselDtSeq("100");
				custcounseldt.setDoFlag("10");
				
				if("06".equals(reqMap.get("ORG_ORD_CAN_YN").toString())) {
					custcounseldt.setProcNote("일괄반품취소");
				} else {
					custcounseldt.setProcNote("업체 품절 신청으로 인한 일괄취소반품");
				}

				custcounseldt.setProcId(srMap.get("PROC_ID").toString());
				custcounseldt.setProcDate(DateUtil.toTimestamp(sysdate));
				
				if (paclaimDAO.insertCounselCustcounseldt(custcounseldt) < 1){
					throw processException("msg.cannot_save", new String[]{"soldOut cancel TCUSTCOUNSELDT insert"});
				}
				
				if("06".equals(reqMap.get("ORG_ORD_CAN_YN").toString()) && "10".equals(reqMap.get("ORDER_CANCEL_YN").toString())) {
					custcounseldt.setCounselDtSeq("101");
					custcounseldt.setDoFlag("40");
					if (paclaimDAO.insertCounselCustcounseldt(custcounseldt) < 1){
						throw processException("msg.cannot_save", new String[]{"soldOut cancel TCUSTCOUNSELDT insert"});
					}
				}
				
				// 품절취소반품만 문자발송
				if("05".equals(reqMap.get("ORG_ORD_CAN_YN").toString())) {
					try {
						String phoneNo = pacommonDAO.selectReceiverSmsHp(srMap);
						
						SmsSendVO smsSendVO = new SmsSendVO();
						smsSendVO.setSendFlag("120");
						smsSendVO.setGoodsName(srMap.get("GOODS_NAME").toString());
						smsSendVO.setReceiveNo(phoneNo);
						smsSendVO.setSendNo(systemProcess.getValRealTime("SMS_SEND_NO"));
						smsSendVO.setInsertId(srMap.get("PROC_ID").toString());
						umsBizProcess.sendSms(smsSendVO);
						
					}catch (Exception e) {
						log.info(e.getMessage());
					}
				}
			}
		} catch(Exception e) {
			log.info(e.getMessage());
		}
		
	}
	
	@Override
	public List<PaGoodsOfferVO> selectPaGoodsOfferList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectPaGoodsOfferList(paramMap);
	}

	@Override
	public List<PaGoodsdtMapping> selectPaGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectPaGoodsdtInfoList(paramMap);
	}
	
	//프로모션
	@Override
	public void savePaPromoTargetHistory(ParamMap paramMap) throws Exception {
		pacommonDAO.savePaPromoTargetHistory(paramMap);
	}
	
	@Override
	public List<PaPromoGoodsPrice> selectPaPromoGoodsPriceList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectPaPromoGoodsPriceList(paramMap);
	}

	@Override
	public String savePaPromoGoodsPrice(PaPromoGoodsPrice paPromoGoodsPrice) throws Exception {
		String rtnMsg 	= Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		ParamMap paramMap = new ParamMap();
		String dateTime  = systemDAO.getSysdatetime();
		
		try {
			paramMap.put("dateTime", dateTime);
			paramMap.put("goodsCode", paPromoGoodsPrice.getGoodsCode());
			
			executedRtn = pacommonDAO.insertPaPromoGoodsPrice(paPromoGoodsPrice);
			if(executedRtn < 0){
				log.info("TPAPROMOGOODSPRICE insert fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICEPROMO INSERT" });
			}
			
			if ("A1".equals(paPromoGoodsPrice.getPaCode()) || "A2".equals(paPromoGoodsPrice.getPaCode())) {
				executedRtn = pacommonDAO.updateSsgTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPASSGGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPASSGGOODS UPDATE" });
				}				
			}/* else { 
				executedRtn = pacommonDAO.updateKakaoTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPASSGGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPASSGGOODS UPDATE" });
				}
			}*/
			/*
			if("11".equals(paPromoGoodsPrice.getPaCode()) || "12".equals(paPromoGoodsPrice.getPaCode())) {
				executedRtn = pacommonDAO.update11stTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPA11STGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPA11STGOODS UPDATE" });
				}	
			} else if("21".equals(paPromoGoodsPrice.getPaCode()) || "22".equals(paPromoGoodsPrice.getPaCode())) {
				executedRtn = pacommonDAO.updateGmktTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPAGMKTGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGMKTGOODS UPDATE" });
				}	
			} else if("41".equals(paPromoGoodsPrice.getPaCode())) {
				executedRtn = pacommonDAO.updateNaverTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPANAVERGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPANAVERGOODS UPDATE" });
				}
				executedRtn = pacommonDAO.updateNaverMappingTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPAGOODSDTMAPPING update fail");
					throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
				}
			} else if("51".equals(paPromoGoodsPrice.getPaCode()) || "52".equals(paPromoGoodsPrice.getPaCode())) {
				executedRtn = pacommonDAO.updateCopnTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPACOPNGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPACOPNGOODS UPDATE" });
				}	
			} else if("61".equals(paPromoGoodsPrice.getPaCode()) || "62".equals(paPromoGoodsPrice.getPaCode())) {
				executedRtn = pacommonDAO.updateWempTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPAWEMPGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPAWEMPGOODS UPDATE" });
				}	
			} else if ("71".equals(paPromoGoodsPrice.getPaCode()) || "72".equals(paPromoGoodsPrice.getPaCode())) {
				executedRtn = pacommonDAO.updateIntpTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPAINTPGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPAINTPGOODS UPDATE" });
				}				
			} else if ("81".equals(paPromoGoodsPrice.getPaCode()) || "82".equals(paPromoGoodsPrice.getPaCode())) {
				executedRtn = pacommonDAO.updateLtonTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPALTONGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPALTONGOODS UPDATE" });
				}				
			} else if ("91".equals(paPromoGoodsPrice.getPaCode()) || "92".equals(paPromoGoodsPrice.getPaCode())) {
				executedRtn = pacommonDAO.updateTmonTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPATMONGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPATMONGOODS UPDATE" });
				}				
			} else if ("A1".equals(paPromoGoodsPrice.getPaCode()) || "A2".equals(paPromoGoodsPrice.getPaCode())) {
				executedRtn = pacommonDAO.updateSsgTrans(paramMap);
				if (executedRtn < 0) {
					log.info("TPASSGGOODS update fail");
					throw processException("msg.cannot_save", new String[] { "TPASSGGOODS UPDATE" });
				}				
			}
			*/
			
		} catch (Exception e) {
			rtnMsg = e.getMessage();
		}
		return rtnMsg;
	}

	@Override
	public String selectPaSsgGoodsOfferCodeCheck(ParamMap paramMap) throws Exception {
		String rtnMsg = "";
		String paGroupCode = paramMap.getString("paGroupCode");
		int rtnCnt = 0;
		
		if(paGroupCode.equals("10")) {
			rtnCnt = pacommonDAO.selectPaSsgGoodsOfferCodeCheck(paramMap);
		}
		
		if(rtnCnt > 0) rtnMsg = "SSG 정보고시 (농산물), ";
		
		return rtnMsg;
	}

	@Override
	public List<PaSsgGoodsVO> selectPaSsgGoodsOfferList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectPaSsgGoodsOfferList(paramMap);
	}
	
	@Override
	public List<PaNoticeApplyVO> selectPaNoticeTargetList() throws Exception {		
		List<PaNoticeApplyVO> paNoticeTargetAllList = pacommonDAO.selectPaNoticeTargetList();	
		return paNoticeTargetAllList;	
	}
	
	public String savePaNoticeTargetAll(PaNoticeApplyVO paNoticeAllData) throws Exception {
		ParamMap paramMap = new ParamMap();
		String rtnMsg = Constants.SAVE_SUCCESS;
		String goodsCode = paNoticeAllData.getGoodsCode();
		
		try {
			 paramMap.put("dateTime"       , paNoticeAllData.getDateTime());
			 paramMap.put("goodsCode"    , goodsCode);
			 paramMap.put("paGroupCode" , paNoticeAllData.getPaGroupCode());
			 
			 if(paNoticeAllData.getProcGb().equals("I")) {
				 pacommonDAO.insertPaNoticeApply(paNoticeAllData);	
			 }else {
				 pacommonDAO.deletePaNoticeApply(paNoticeAllData);	
			 }
			 updatePaTransTarget(paramMap);
		} catch(Exception e) {
			rtnMsg = Constants.SAVE_FAIL;
			log.info("제휴공지사항 데이터 동기화  Error - GOODS_CODE : " + goodsCode);
			//throw e;
		}
		return rtnMsg;		
	}
	
	/*	기존의 updatePaTransTarget은 SSG 이전의 제휴사들 프로모션 전용 메소드로 사용(updatePaPromoTransTarget 으로 이름 변경) 
	 * 	앞으로 추가될 제휴사들의 공지사항 수정시 trans_target_yn UPDATE */
	private int updatePaTransTarget(ParamMap paramMap) throws Exception{
		String paGroupCode  = paramMap.getString("paGroupCode");
		int 	excuteRtn	=  0;
		
		switch (paGroupCode) {
		case "01":
			excuteRtn =pacommonDAO.update11stTrans(paramMap);
			break;
		case "02":
		case "03":
		case "02,03":
			excuteRtn = pacommonDAO.updateGmktTrans(paramMap);
			break;
		case "04":
			excuteRtn = pacommonDAO.updateNaverTrans(paramMap);
			break;
		case "05":
			excuteRtn = pacommonDAO.updateCopnTrans(paramMap);
			break;
		case "06":
			excuteRtn = pacommonDAO.updateWempTrans(paramMap);
			break;
		case "07":
			excuteRtn = pacommonDAO.updateIntpTrans(paramMap);
			break;
		case "08":
			excuteRtn = pacommonDAO.updateLtonTrans(paramMap);
			break;
		case "09":
			excuteRtn = pacommonDAO.updateTmonTrans(paramMap);
			break;
		case "10":
			excuteRtn = pacommonDAO.updateSsgTrans(paramMap);
			break;
		//case "11":
		//	excuteRtn = pacommonDAO.updateKakaoTrans(paramMap);
		//	break;
		default:
			break;
		}
		
		return excuteRtn;
	}
	
	@Override
	public int saveRetentionGoods(ParamMap paramMap) throws Exception {
		int executedRtn = 0;

		executedRtn = pacommonDAO.execSP_PA_GOODS_RESET(paramMap);
		executedRtn = pacommonDAO.insertTPaRetentionGoods(paramMap);
		
		return executedRtn;
	}
	
	@Override
	public String selectMakerMapping(PaGoodsTargetRec paGoodstarget) throws Exception {
		return pacommonDAO.selectMakerMapping(paGoodstarget);
	}
	
	@Override
	public String saveSsgFoodInfo(ParamMap paramMap) throws Exception {
		String resultMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		try {		
			executedRtn = pacommonDAO.updatePaSsgGoodsFoodSync(paramMap);
			if (executedRtn < 0) {
				log.info("TPASSGGOODS update fail");
				throw processException("msg.cannot_save", new String[] { "TPASSGGOODS UPDATE" });
			}
			
		} catch(Exception e) {
			resultMsg = Constants.SAVE_FAIL;
			throw e;
		}
		
		return resultMsg;
	}

	public String selectPaSsgGoodsCollectYnCheck(ParamMap paramMap) throws Exception {
		String rtnMsg = "";
		String paGroupCode = paramMap.getString("paGroupCode");
		int rtnCnt = 0;
		
		if(paGroupCode.equals("10")) {
			rtnCnt = pacommonDAO.selectPaSsgGoodsCollectYnCheck(paramMap);
		}
		
		if(rtnCnt > 0) rtnMsg = "SSG 착불상품, ";
		
		return rtnMsg;
	}
	
	@Override
	public List<PaSsgGoodsVO> selectPaSsgCollectGoodsList(ParamMap paramMap) throws Exception {
		return pacommonDAO.selectPaSsgCollectGoodsList(paramMap);
	}

	@Override
	public String savePaMobileOrderCancel(HashMap<String, String> map) throws Exception {
    	String rtnMsg = "000000";
    	
		HashMap<String, String> srMap = null; 
		
		try {
			//제휴 주문건 SR 생성
			if(map != null) {
				
				srMap = pacommonDAO.selectCounselInfo(map);
				
				srMap.put("ORDER_NO", map.get("ORDER_NO").toString());
				
				// = 1.상담내용저장
				insertCustCounsel(srMap);
				
				// = 2.상품품절처리
				insertSalesNoGoods(map);
				
				// = 3.자동취소 히스토리 저장
				insertMobileOrderCancelHis(map);
				
				// = 4 주문취소 SMS
				try {
					String phoneNo = pacommonDAO.selectReceiverSmsHp(srMap);

					SmsSendVO smsSendVO = new SmsSendVO();
					smsSendVO.setSendFlag("120");
					smsSendVO.setGoodsName(srMap.get("GOODS_NAME").toString());
					smsSendVO.setReceiveNo(phoneNo);
					smsSendVO.setSendNo(systemProcess.getValRealTime("SMS_SEND_NO"));
					smsSendVO.setInsertId(srMap.get("PROC_ID").toString());
					umsBizProcess.sendSms(smsSendVO);

				} catch (Exception e) {
					log.info(e.getMessage());
				}
			}
			
		} catch(Exception e) {
			log.info(e.getMessage());
    		throw new ProcessException(e.getMessage());

		}
    	return rtnMsg;

	}

	private String insertCustCounsel(HashMap<String, String> srMap) throws Exception {
		String sysdate  = systemDAO.getSysdatetime();
		Custcounselm  custcounselm  = null;
		Custcounseldt custcounseldt = null;
 		String  counselSeq 	 	    = "";
 		
 		
		custcounselm  = new Custcounselm();
		custcounseldt = new Custcounseldt();
			
		counselSeq = systemProcess.getSequenceNo("COUNSEL_SEQ");
		if (counselSeq.equals("")) {
			throw processException("msg.cannot_create", new String[] { "COUNSEL_SEQ" });
		}
		//tcustcounselm
		custcounselm.setCounselSeq(counselSeq);
		custcounselm.setCustNo(srMap.get("CUST_NO").toString());
		custcounselm.setDoFlag("40");

		custcounselm.setRefNo1(srMap.get("ORDER_NO").toString());
		custcounselm.setOutLgroupCode("99");
		custcounselm.setOutMgroupCode("99");
		
		custcounselm.setCsLgroup("83");
		custcounselm.setCsMgroup("03");
		custcounselm.setCsSgroup("02");
		custcounselm.setCsLmsCode("830302");
		
		custcounselm.setGoodsCode(ComUtil.NVL(srMap.get("GOODS_CODE").toString(), "" ));
		custcounselm.setGoodsdtCode(ComUtil.NVL(srMap.get("GOODSDT_CODE").toString(), "" ));
		custcounselm.setCsSendYn("0");
		custcounselm.setClaimNo(counselSeq);
		custcounselm.setInsertId(srMap.get("PROC_ID").toString());
		custcounselm.setProcId(srMap.get("PROC_ID").toString());
    	custcounselm.setRefId1(pacommonDAO.selectPaRefIdForCounsel(srMap.get("MEDIA_CODE").toString()));
    	
    	custcounselm.setQuickYn("0");
    	custcounselm.setQuickEndYn("0");
    	custcounselm.setInsertDate(DateUtil.toTimestamp(sysdate));
    	
		if (paclaimDAO.insertCounselCustcounselm(custcounselm) < 1){
			throw processException("msg.cannot_save", new String[]{"soldOut cancel TCUSTCOUNSELM insert"});
		}
		
		//tcustcounseldt
		custcounseldt.setCounselSeq(counselSeq);
		custcounseldt.setCounselDtSeq("100");
		custcounseldt.setDoFlag("10");
		
		custcounseldt.setProcId(srMap.get("PROC_ID").toString());
		custcounseldt.setProcDate(DateUtil.toTimestamp(sysdate));
		
		if (paclaimDAO.insertCounselCustcounseldt(custcounseldt) < 1){
			throw processException("msg.cannot_save", new String[]{"soldOut cancel TCUSTCOUNSELDT insert"});
		}
		
		if("40".equals(custcounselm.getDoFlag())) {
			custcounseldt.setCounselDtSeq("101");
			custcounseldt.setDoFlag("40");
			custcounseldt.setProcNote("배송예정일 경과로 인하여 자동 품절취소가 완료되었습니다.");
			if (paclaimDAO.insertCounselCustcounseldt(custcounseldt) < 1){
				throw processException("msg.cannot_save", new String[]{"soldOut cancel TCUSTCOUNSELDT insert"});
			}
		}
		
		return null;
		
	}

	private String insertMobileOrderCancelHis(HashMap<String, String> map) throws Exception {
    	
    	String successYn = "";
    	Mobileordercancelhis mobileordercancelhisTmp = null;
    	int executedRtn = 0;
    	Timestamp insert_date = null;
    	
    	try {
    		insert_date = systemProcess.getSysdatetime();
    		
    		mobileordercancelhisTmp = new Mobileordercancelhis();
    		mobileordercancelhisTmp.setOrderNo(map.get("ORDER_NO"));
    		mobileordercancelhisTmp.setOrderGSeq(map.get("ORDER_G_SEQ"));
    		mobileordercancelhisTmp.setOrderDSeq("001");
    		mobileordercancelhisTmp.setOrderWSeq("001");
    		mobileordercancelhisTmp.setInsertId(map.get("PROC_ID"));
    		mobileordercancelhisTmp.setModifyId(map.get("PROC_ID"));
    		mobileordercancelhisTmp.setInsertDate(insert_date);
    		mobileordercancelhisTmp.setModifyDate(insert_date);
    		
    		executedRtn = pacommonDAO.insertMobileOrderCancelHis(mobileordercancelhisTmp);
    		if (executedRtn != 1) {
    			throw processException("errors.db.save", new String[] { "allCancel :: TMOBILEORDERCANCELHIS insert" });
    		}
    		successYn = "Y";
    	} catch (Exception e) {
    		log.error(e.getMessage());
    		throw new ProcessException(e.getMessage());
    	}
    	
    	return successYn;
    }

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String insertSalesNoGoods(HashMap<String, String> map) throws Exception {
    	
    	String successYn = "";
    	int executedRtn = 0;
		Timestamp sysDateTime = null;
		String goods_code = "";
		String goods_code_old = "";
		String goodsdt_code = "";
		String sale_gb_goods = "";
		String set_goods_code = "";
		String set_goodsdt_code = "";
		String sale_gb_setgoods = "";
		String category_code = "";
		String modSql = "";
		HashMap<String, Object> retSheet = null;
		HashMap<String, Object> retSetGoodsData = null;
		ParamMap paramMap1 = new ParamMap();
		ParamMap paramMap2 = new ParamMap();
		List<HashMap<String, Object>> list1 = null;
		List<?> OrderList = null;
		HashMap<String, Object> tMap = null;
		
		try {

			sysDateTime = systemProcess.getSysdatetime();
			if (sysDateTime.equals("")) {
				throw processException("errors.db.no.datetime");
			}

			
			OrderList = pacommonDAO.selectOrderGoodsListDt(map);
			SaleNoGoods[] tsalenogoods = null;
			SaleNoGoods	salenogoods			= null;
			
			List saveList = null;
			
			if(OrderList != null && OrderList.size() > 0){
				saveList = new ArrayList();
				for(int i = 0; i < OrderList.size(); i++){
					tMap = (HashMap<String, Object>)OrderList.get(i);
					salenogoods = new SaleNoGoods();
					salenogoods.setGoodsCode		(tMap.get("GOODS_CODE").toString());
		            salenogoods.setGoodsdtCode		(tMap.get("GOODSDT_CODE").toString());
		            salenogoods.setSaleGb			("11");
		            salenogoods.setSaleNoCode		("");
		            salenogoods.setSaleNoNote		("");
					salenogoods.setCwareAction		("U");
					salenogoods.setInsertId		("999999");
					salenogoods.setModifyId("999999");
					
					saveList.add(salenogoods);
				}
				
				tsalenogoods = (SaleNoGoods[])saveList.toArray(new SaleNoGoods[0]);
			}
			
			if (tsalenogoods != null) {
				SaleNoGoods regSalenogoods = null;
				for (int i = 0; i < tsalenogoods.length; i++) {
					regSalenogoods = tsalenogoods[i];
					if (regSalenogoods.getCwareAction().equals("U") || regSalenogoods.getCwareAction().equals("UC")) {
						regSalenogoods.setInsertDate(sysDateTime);
						regSalenogoods.setModifyDate(sysDateTime);

						executedRtn = pacommonDAO.updateGoodsdt(regSalenogoods);
						if (executedRtn != 1) {
							throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTgoodsdt" });
						}
					}
				}

				regSalenogoods = null;
				for (int i = 0; i < tsalenogoods.length; i++) {
					regSalenogoods = tsalenogoods[i];
					if (regSalenogoods.getCwareAction().equals("U") || regSalenogoods.getCwareAction().equals("UC")) {

						goods_code = regSalenogoods.getGoodsCode();
						goodsdt_code = regSalenogoods.getGoodsdtCode();

						paramMap1.put("goods_code", goods_code);
						regSalenogoods.setInsertDate(sysDateTime);

						/*modSql = " GOODS_CODE = '" + goods_code + "' AND  GOODSDT_CODE = '" + goodsdt_code + "'";

						regSalenogoods.setSaleNoSeq(systemProcess.getSeqNo("TSALENOGOODS", "SALE_NO_SEQ", modSql, 5));
						if (executedRtn != 1) {
							throw processException("errors.db.save", new String[] { "SALE_NO_SEQ" });
						}*/

						executedRtn = pacommonDAO.insertSalenogoods(regSalenogoods);
						if (executedRtn != 1) {
							throw processException("errors.db.save", new String[] { "TSALENOGOODS insert" });
						}

						if (!goods_code.equals(goods_code_old)) {
							goods_code_old = goods_code;

							sale_gb_goods = pacommonDAO.getSaleGb(goods_code); // =
																			// tgoods에
																			// 반영할
																			// sale_gb

							paramMap1.put("saleGbGoods", sale_gb_goods);
							paramMap1.put("modifyDate", regSalenogoods.getInsertDate());
							paramMap1.put("modifyId", regSalenogoods.getInsertId());
							executedRtn = pacommonDAO.updateGoodsSaleGb(paramMap1);
							if (executedRtn != 1) {
								throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTgoods" });
							}

							if (!sale_gb_goods.equals("00") && !sale_gb_goods.equals("05")) {
								// = TCATEGORYGOODS
								executedRtn = pacommonDAO.getCgCnt(goods_code);
								if (executedRtn > 0) {
									list1 = pacommonDAO.selectCgList(paramMap1);

									if (list1 != null) {
										if (list1.size() > 0) {
											for (int j = 0; j < list1.size(); j++) {
												retSheet = (HashMap<String, Object>) list1.get(j);

												category_code = ComUtil.objToStr(retSheet.get("CATEGORY_CODE"), "");

												// = tcategory
												executedRtn = pacommonDAO.updateCategory(category_code);
												if (executedRtn < 0) {
													throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTcategory" });
												}
											}
										}
									}

									// = tcategorygoods
									executedRtn = pacommonDAO.updateCategorygoods(regSalenogoods);
									if (executedRtn < 0) {
										throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTcategorygoods" });
									}

									// = tpresentation
									executedRtn = pacommonDAO.updatePresentation(regSalenogoods);
									if (executedRtn < 0) {
										throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTpresentation" });
									}
								}

								list1 = null;
								retSheet = null;
								// = STCATEGORYGOODS
								executedRtn = pacommonDAO.getScgCnt(goods_code);
								if (executedRtn > 0) {
									list1 = pacommonDAO.selectScgList(paramMap1);

									if (list1 != null) {
										if (list1.size() > 0) {
											for (int j = 0; j < list1.size(); j++) {
												retSheet = (HashMap<String, Object>) list1.get(j);
												// = stcategory
												category_code = ComUtil.objToStr(retSheet.get("CATEGORY_CODE"), "");

												executedRtn = pacommonDAO.updateStcategory(category_code);
												if (executedRtn < 0) {
													throw processException("errors.db.save", new String[] { "TSALENOGOODS updateStcategory" });
												}
											}
										}
									}

									// = tcategorygoods
									executedRtn = pacommonDAO.updateStcategorygoods(regSalenogoods);
									if (executedRtn < 0) {
										throw processException("errors.db.save", new String[] { "TSALENOGOODS updateStcategorygoods" });
									}

									// = stpresentation
									executedRtn = pacommonDAO.updateStpresentation(regSalenogoods);
									if (executedRtn < 0) {
										throw processException("errors.db.save", new String[] { "TSALENOGOODS updateStpresentation" });
									}
								}
							}else if(sale_gb_goods.equals("00")){ // 진행으로 변경했을 경우 전시 복귀 추가 2018.08.24 by sglee.
								list1 = null;
								retSheet = null;
								// = TCATEGORYGOODS
								executedRtn = pacommonDAO.getCgCnt2(goods_code);
								if (executedRtn > 0) {
									list1 = pacommonDAO.selectCgList2(paramMap1);

									if (list1 != null) {
										if (list1.size() > 0) {
											for (int j = 0; j < list1.size(); j++) {
												retSheet = (HashMap<String, Object>) list1.get(j);

												category_code = ComUtil.objToStr(retSheet.get("CATEGORY_CODE"), "");

												// = tcategory
												executedRtn = pacommonDAO.updateCategory2(category_code);
												if (executedRtn < 0) {
													throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTcategory2" });
												}
											}
										}
									}

									// = tcategorygoods
									executedRtn = pacommonDAO.updateCategorygoods2(regSalenogoods);
									if (executedRtn < 0) {
										throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTcategorygoods2" });
									}

									// = tpresentation
									executedRtn = pacommonDAO.updatePresentation2(regSalenogoods);
									if (executedRtn < 0) {
										throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTpresentation2" });
									}
								}

								list1 = null;
								retSheet = null;
								// = STCATEGORYGOODS
								executedRtn = pacommonDAO.getScgCnt2(goods_code);
								if (executedRtn > 0) {
									list1 = pacommonDAO.selectScgList2(paramMap1);

									if (list1 != null) {
										if (list1.size() > 0) {
											for (int j = 0; j < list1.size(); j++) {
												retSheet = (HashMap<String, Object>) list1.get(j);
												// = stcategory
												category_code = ComUtil.objToStr(retSheet.get("CATEGORY_CODE"), "");

												executedRtn = pacommonDAO.updateStcategory2(category_code);
												if (executedRtn < 0) {
													throw processException("errors.db.save", new String[] { "TSALENOGOODS updateStcategory2" });
												}
											}
										}
									}

									// = tcategorygoods
									executedRtn = pacommonDAO.updateStcategorygoods2(regSalenogoods);
									if (executedRtn < 0) {
										throw processException("errors.db.save", new String[] { "TSALENOGOODS updateStcategorygoods2" });
									}

									// = stpresentation
									executedRtn = pacommonDAO.updateStpresentation2(regSalenogoods);
									if (executedRtn < 0) {
										throw processException("errors.db.save", new String[] { "TSALENOGOODS updateStpresentation2" });
									}
								}
							}

							// = 해당 상품의 SET 상품의 SALE_GB 변경
							// = 해당 상품을 포함하는 SET상품을 찾아서 구성품들의 MAX(SALE_GB)로
							// UPDATE한다.
							list1 = null;
							retSetGoodsData = null;
							list1 = pacommonDAO.getSetgoods(paramMap1); // = tgoods에
																		// 반영할
																		// sale_gb

							if (list1 != null && list1.size() > 0)
								for (int j = 0; j < list1.size(); j++) {
									retSetGoodsData = (HashMap<String, Object>) list1.get(j);
									set_goods_code = ComUtil.NVL((String) retSetGoodsData.get("SET_GOODS_CODE"), "");
									set_goodsdt_code = ComUtil.NVL((String) retSetGoodsData.get("SET_GOODSDT_CODE"), "");
									sale_gb_setgoods = ComUtil.NVL((String) retSetGoodsData.get("SALE_GB"), "");

									regSalenogoods.setGoodsCode(set_goods_code);
									regSalenogoods.setGoodsdtCode(set_goodsdt_code);
									regSalenogoods.setSaleGb(sale_gb_setgoods);

									modSql = " GOODS_CODE = '" + set_goods_code + "' AND  GOODSDT_CODE = '" + set_goodsdt_code + "'";
									regSalenogoods.setSaleNoSeq(systemProcess.getMaxNo("TSALENOGOODS", "SALE_NO_SEQ", modSql, 3));
									if (regSalenogoods.getSaleNoSeq().equals("")) {
										throw processException("errors.db.save", new String[] { "SALE_NO_SEQ" });
									}

									executedRtn = pacommonDAO.updateGoodsdt(regSalenogoods);
									if (executedRtn != 1) {
										throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTgoodsdt" });
									}

									executedRtn = pacommonDAO.insertSalenogoods(regSalenogoods);
									if (executedRtn != 1) {
										throw processException("errors.db.save", new String[] { "TSALENOGOODS insert" });
									}

									ParamMap updateGoodsMap = new ParamMap();
									updateGoodsMap.put("saleGbGoods", sale_gb_setgoods);
									updateGoodsMap.put("modifyDate", regSalenogoods.getModifyDate());
									updateGoodsMap.put("modifyId", regSalenogoods.getModifyId());
									updateGoodsMap.put("goods_code", regSalenogoods.getGoodsCode());

									executedRtn = pacommonDAO.updateGoodsSaleGb(updateGoodsMap);
									if (executedRtn != 1) {
										throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTgoods" });
									}

									// = set상품들의 전시도 내려준다.
									if (!sale_gb_setgoods.equals("00")) {
										// = TCATEGORYGOODS
										retSheet = null;
										list1 = null;
										paramMap2.put("goods_code", set_goods_code);
										executedRtn = pacommonDAO.getCgCnt(set_goods_code);
										if (executedRtn > 0) {
											list1 = pacommonDAO.selectCgList(paramMap2);

											if (list1 != null) {
												if (list1.size() > 0) {
													for (int k = 0; k < list1.size(); k++) {
														retSheet = (HashMap<String, Object>) list1.get(k);
														category_code = ComUtil.objToStr(retSheet.get("CATEGORY_CODE"), "");
													}
												}
											}

											// = tcategorygoods
											executedRtn = pacommonDAO.updateCategorygoods(regSalenogoods);
											if (executedRtn < 0) {
												throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTcategorygoods" });
											}

											// = tpresentation
											executedRtn = pacommonDAO.updatePresentation(regSalenogoods);
											if (executedRtn < 0) {
												throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTpresentation" });
											}
										}
										retSheet = null;
										list1 = null;
										// = STCATEGORYGOODS
										executedRtn = pacommonDAO.getScgCnt(set_goods_code);
										if (executedRtn > 0) {
											list1 = pacommonDAO.selectScgList(paramMap2);

											if (list1 != null) {
												if (list1.size() > 0) {
													for (int l = 0; l < list1.size(); l++) {
														retSheet = (HashMap<String, Object>) list1.get(l);
														// = stcategory
														category_code = ComUtil.objToStr(retSheet.get("CATEGORY_CODE"), "");

														executedRtn = pacommonDAO.updateStcategory(category_code);
														if (executedRtn < 0) {
															throw processException("errors.db.save", new String[] { "TSALENOGOODS updateStcategory" });
														}
													}
												}
											}

											// = tcategorygoods
											executedRtn = pacommonDAO.updateStcategorygoods(regSalenogoods);
											if (executedRtn < 0) {
												throw processException("errors.db.save", new String[] { "TSALENOGOODS updateStcategorygoods" });
											}

											// = stpresentation
											executedRtn = pacommonDAO.updateStpresentation(regSalenogoods);
											if (executedRtn < 0) {
												throw processException("errors.db.save", new String[] { "TSALENOGOODS updateStpresentation" });
											}
										}
									}else if(sale_gb_setgoods.equals("00")){// 진행으로 변경했을 경우 전시 복귀 추가 2018.08.24 by sglee.
										// = TCATEGORYGOODS
										retSheet = null;
										list1 = null;
										paramMap2.put("goods_code", set_goods_code);
										executedRtn = pacommonDAO.getCgCnt2(set_goods_code);
										if (executedRtn > 0) {
											list1 = pacommonDAO.selectCgList2(paramMap2);

											if (list1 != null) {
												if (list1.size() > 0) {
													for (int k = 0; k < list1.size(); k++) {
														retSheet = (HashMap<String, Object>) list1.get(k);
														category_code = ComUtil.objToStr(retSheet.get("CATEGORY_CODE"), "");
													}
												}
											}

											// = tcategorygoods
											executedRtn = pacommonDAO.updateCategorygoods2(regSalenogoods);
											if (executedRtn < 0) {
												throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTcategorygoods2" });
											}

											// = tpresentation
											executedRtn = pacommonDAO.updatePresentation2(regSalenogoods);
											if (executedRtn < 0) {
												throw processException("errors.db.save", new String[] { "TSALENOGOODS updateTpresentation2" });
											}
										}
										retSheet = null;
										list1 = null;
										// = STCATEGORYGOODS
										executedRtn = pacommonDAO.getScgCnt2(set_goods_code);
										if (executedRtn > 0) {
											list1 = pacommonDAO.selectScgList2(paramMap2);

											if (list1 != null) {
												if (list1.size() > 0) {
													for (int l = 0; l < list1.size(); l++) {
														retSheet = (HashMap<String, Object>) list1.get(l);
														// = stcategory
														category_code = ComUtil.objToStr(retSheet.get("CATEGORY_CODE"), "");

														executedRtn = pacommonDAO.updateStcategory2(category_code);
														if (executedRtn < 0) {
															throw processException("errors.db.save", new String[] { "TSALENOGOODS updateStcategory2" });
														}
													}
												}
											}

											// = tcategorygoods
											executedRtn = pacommonDAO.updateStcategorygoods2(regSalenogoods);
											if (executedRtn < 0) {
												throw processException("errors.db.save", new String[] { "TSALENOGOODS updateStcategorygoods2" });
											}

											// = stpresentation
											executedRtn = pacommonDAO.updateStpresentation2(regSalenogoods);
											if (executedRtn < 0) {
												throw processException("errors.db.save", new String[] { "TSALENOGOODS updateStpresentation2" });
											}
										}
									}
								}
						}
					}
				}
			}
    		
    	} catch (Exception e) {
    		log.error(e.getMessage());
    		throw new ProcessException(e.getMessage());
    	}
    	
    	return successYn;
		
	}

	@Override
	public HashMap<String, Object> procPagoodsRetention(ParamMap paramMap) throws Exception {
		HashMap<String,Object> resultMap = null;

		try{
			resultMap = (HashMap<String,Object>) paramMap.get();
			pacommonDAO.execSP_PAGOODS_RETENTION(resultMap);
		    log.info(resultMap.get("rtn_code").toString());
		    log.info(resultMap.get("rtn_msg").toString());
		}catch(Exception e){
		    log.info(e.getMessage());
		}
		return resultMap;
	}
}
