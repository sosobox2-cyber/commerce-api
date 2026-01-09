
package com.cware.netshopping.pahalf.goods.process.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.process.SystemProcess;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.PaHalfGoodsVO;
import com.cware.netshopping.domain.PaHalfGoodsdtMappingVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsTransLog;
import com.cware.netshopping.domain.model.PaHalfGoods;
import com.cware.netshopping.domain.model.PaHalfGoodsdtMapping;
import com.cware.netshopping.pacommon.common.process.PaCommonProcess;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pahalf.goods.process.PaHalfGoodsProcess;
import com.cware.netshopping.pahalf.goods.repository.PaHalfGoodsDAO;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;

@Service("pahalf.goods.paHalfGoodsProcess")
public class PaHalfGoodsProcessImpl extends AbstractService implements PaHalfGoodsProcess {
    
    @Resource(name = "pahalf.goods.paHalfGoodsDAO")
    private PaHalfGoodsDAO paHalfGoodsDAO;
    
    @Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
    
	@Resource(name = "pacommon.common.pacommonProcess")
    private PaCommonProcess pacommonProcess;
	
	@Resource(name = "common.system.systemProcess")
	private SystemProcess systemProcess;
	
	@Override
	public List<PaHalfGoods> selectPaHalfSellStateList(ParamMap paramMap) throws Exception {
		return paHalfGoodsDAO.selectPaHalfSellStateList(paramMap);
	}

	@Override
	public int savePaHalfGoodsSell(PaHalfGoods paHalfGoods) throws Exception {
		int executedRtn = 0;

		executedRtn = paHalfGoodsDAO.updatePaHalfGoodsSellState(paHalfGoods);

		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] { "TPAHALFGOODS UPDATE" });
		}
		return executedRtn;

	}
	
	@Override
	public PaHalfGoodsdtMapping selectCheckPaOptionCode(ParamMap paramMap) throws Exception {
		return paHalfGoodsDAO.selectCheckPaOptionCode(paramMap);
	}

	@Override
	public void updateTpaGoodsdtMapping(PaHalfGoodsdtMapping paGoodsdtMapping) throws Exception {
		int executedRtn = 0;
		
		executedRtn = paHalfGoodsDAO.updateTpaGoodsdtMapping(paGoodsdtMapping);
		
		if (executedRtn < 0) {
			throw processException("msg.cannot_save", new String[] { "TPAHALFGOODSDTMAPPING UPDATE" });
		}
		
	}



	@Override
	public List<PaHalfGoodsdtMappingVO> selectPaHalfGoodsdtMappingStockList(ParamMap paramMap) throws Exception{
		return paHalfGoodsDAO.selectPaHalfGoodsdtMappingStockList(paramMap);
	}

	@Override
	public List<Map<String, Object>> selectPaHalfStockInfoList(ParamMap paramMap) throws Exception{
		return paHalfGoodsDAO.selectPaHalfStockInfoList(paramMap);
	}

	@Override
	public void savePaHalfGoodsdtStock(List<Map<String, Object>> paHalfGoodsdtMappingList) throws Exception {
		int executedRtn = 0;
		
		for(Map<String,Object> paHalfGoodsdtMapping : paHalfGoodsdtMappingList) {
			try {
				paHalfGoodsdtMapping.put("modifyId", Constants.PA_HALF_PROC_ID);
				executedRtn = paHalfGoodsDAO.updatePaHalfGoodsdtStock(paHalfGoodsdtMapping);
				if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPABRAND INSERT" });
			} catch (Exception e) {
				 log.info(PaHalfComUtill.getErrorMessage(e));
			}
		}
		
	}

	@Override
	public int insertPaHalfGoodsTransLog(PaGoodsTransLog paGoodsTransLog) throws Exception {
		int excuteRtn = 0;
		
		try {
			String dateTime = systemProcess.getSysdatetimeToString();
			paGoodsTransLog.setProcDate(DateUtil.toTimestamp(dateTime, "yyyy/MM/dd HH:mm:ss"));
			
			excuteRtn =  paCommonDAO.insertPaGoodsTransLog(paGoodsTransLog);	
		}catch (Exception e) {
			log.info(PaHalfComUtill.getErrorMessage(e));
		}
		return excuteRtn;
	}

	@Override
	public List<HashMap<String, String>> selectPaHalfGoodsTrans(ParamMap paramMap) throws Exception {
		return paHalfGoodsDAO.selectPaHalfGoodsTrans(paramMap);
	}

	@Override
	public int goodsValidationCheck(String paCode, String goodsCode) throws Exception {
		int rtnCode		  = 1;
		ParamMap paramMap = new ParamMap();
		try {
			paramMap.put("goodsCode"	, goodsCode);
			paramMap.put("paCode"		, paCode);
			paramMap.put("paGroupCode"	, "12");
			paramMap.put("marginCode"	, "90");
			paramMap.put("minpriceCode"	, "91");	
			
			
			String exceptStr = pacommonProcess.paExceptGoodsYn(paramMap);
			if(!exceptStr.equals("000000")){
				PaHalfGoodsVO halfgoods = new PaHalfGoodsVO();
				halfgoods.setPaCode		(paCode);
				halfgoods.setGoodsCode	(goodsCode);
				halfgoods.setReturnNote	(exceptStr);
				halfgoods.setPaStatus	("20");
								
				//신규동기화에서 체크하기 떄문에 제거
				/*if("I".equals(flag)) { //I:입점, U:판매상태 변경, M:상품 수정
					halfgoods.setPaStatus	("20");
				}
				else if("U".equals(flag)){
					if(!exceptStr.contains("판매가") && !exceptStr.contains("배송비정책") && !exceptStr.contains("마진율")) return rtnCode;
					halfgoods.setPaSaleGb("30");
					halfgoods.setTransSaleYn("0");
				}else {
					if(!exceptStr.contains("판매가") && !exceptStr.contains("배송비정책") && !exceptStr.contains("마진율")) return rtnCode;			
					halfgoods.setPaSaleGb("30");
					halfgoods.setTransSaleYn("1");
				}*/

				paHalfGoodsDAO.updatePaHalfGoods(halfgoods);
				return 0;
			}
			
			
		}catch (Exception e) {
			return 0;
		}
		return rtnCode;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int saveHalfGoodsShipCostCode(Map<String, String> goodsSlipMap) throws Exception {
		int executedRtn = 0;
		goodsSlipMap = (Map<String, String>) PaHalfComUtill.replaceCamel(goodsSlipMap);
		PaHalfGoodsVO 	halfgoods 	 = (PaHalfGoodsVO) PaHalfComUtill.map2VO(goodsSlipMap, PaHalfGoodsVO.class);
		//PaHalfShipInfo  halfShipInfo = (PaHalfShipInfo) PaHalfComUtill.map2VO(goodsSlipMap, PaHalfShipInfo.class);
		
		String paShipCostCode = paHalfGoodsDAO.selectPaShipCostCode(halfgoods); 
		if(paShipCostCode == null || "".equals(paShipCostCode)) throw processException("msg.cannot_save", new String[] { "TPAHALFGOODS UPDATE(NO-PASHIPCOSTCODE)" });
		halfgoods.setPaShipCostCode(paShipCostCode);
		
		executedRtn = paHalfGoodsDAO.updatePaHalfGoods(halfgoods);
		
		if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAHALFGOODS UPDATE :: PA_SHIP_COST_CODE" });
	
		return executedRtn;
	}

	@Override
	public List<PaHalfGoodsVO> selectPaHalfGoodsInfo(ParamMap paramMap) throws Exception {
		List<PaHalfGoodsVO> paHalfGoodsList = paHalfGoodsDAO.selectPaHalfGoodsInfo(paramMap);

		if("".equals(paramMap.getString("goodsCode"))) return paHalfGoodsList;
		
		if(paHalfGoodsList ==null || paHalfGoodsList.size() < 1) {
			paramMap.put("code"		, "401");
			throw processException("msg.no.select", new String[] { "TPAHALFGOODS-GOODSINFO" });
		}
		
		return paHalfGoodsList;
	}

	@Override
	public List<PaHalfGoodsdtMapping> selectPaHalfGoodsdtInfoList(ParamMap paramMap) throws Exception {
		List<PaHalfGoodsdtMapping> goodsdtList = paHalfGoodsDAO.selectPaHalfGoodsdtInfoList(paramMap);
		
		if(goodsdtList == null || goodsdtList.size() < 1) {
			paramMap.put("code"		, "401");
			throw processException("msg.no.select", new String[] { "TPAGOODSMAPPING-GOODSDTINFO" });
		}
		return goodsdtList;
	}

	@Override
	public List<PaGoodsOffer> selectPaHalfGoodsOfferList(ParamMap paramMap) throws Exception {
		return paHalfGoodsDAO.selectPaHalfGoodsOfferList(paramMap);
	}

	@Override
	public void setGoodsDtInfo(PaHalfGoodsVO paHalfGoods, List<PaHalfGoodsdtMapping> goodsdtMapping, ParamMap apiDataMap, boolean isUpdate) throws Exception {
		
		// 정책정보  
		Map<String, Object> prdMstInfoMap = new HashMap<String, Object>();
		prdMstInfoMap.put("prdNo", paHalfGoods.getProductNo()); // 하프클럽 상품 번호
		apiDataMap.put("prdMstInfo"	, prdMstInfoMap);
		
		//기본정보
		Map<String, Object> prdStdInfoMap = new HashMap<String, Object>();
		prdStdInfoMap.put("bkshwSelYn"	, " ");
		prdStdInfoMap.put("isbnNo"		, "");
		apiDataMap.put("prdStdInfo", prdStdInfoMap);
		
        //전시정보 미입력시 표준으로 적용됨
		Map<String, Object> prdDispInfoMap = new HashMap<String, Object>();
		apiDataMap.put("prdDispInfo", prdDispInfoMap);
		
		//베이스정보
		Map<String, Object> prdBaseInfoMap = new HashMap<String, Object>();
		prdBaseInfoMap.put("prdNm"			, paHalfGoods.getGoodsName());//상품 명
		prdBaseInfoMap.put("disChanTypCd"	, "01"); //노출 채널 01 고정 ( PC + 모바일 )
		apiDataMap.put("prdBaseInfo", prdBaseInfoMap);
		
		//판매정보
		Map<String, Object> prdSelInfoMap = new HashMap<String, Object>();
		if(isUpdate) {
			prdSelInfoMap.put("prdPrcNo", paHalfGoods.getPrdPrcNo());
		}
		
		double promoSumAmt = paHalfGoods.getSalePrice()  - paHalfGoods.getCouponDcAmt() - paHalfGoods.getArsDcAmt() - paHalfGoods.getLumpSumDcAmt();
		
		prdSelInfoMap.put("normPrc"		, paHalfGoods.getSalePrice()); //정상가(tag가)
		prdSelInfoMap.put("selPrc"		, promoSumAmt); //판매가 (정상가- 할인가)
		prdSelInfoMap.put("selYr"		, paHalfGoods.getSaleStartDate());//판매년도 YYYY 
		if(paHalfGoods.getOrderMaxQty() > 0) {
			prdSelInfoMap.put("pchsLimitCnt"		, paHalfGoods.getOrderMaxQty());// 상품별 1일 최대구매수량		
		}else {
			prdSelInfoMap.put("pchsLimitCnt"		, 0);// 상품별 1일 최대구매수량 제한이 없을경우 0으로 세팅
		}
		prdSelInfoMap.put("maxPchsQty"			, 999); //최대구매수량		
		prdSelInfoMap.put("vtaxTypCd"		, "1".equals(paHalfGoods.getTaxYn()) ? "01" : "02");// 부가세  (01:과세, 02:면세, 03:영세)
		prdSelInfoMap.put("dispYn"			, "Y"); //전시여부 Y고정
		//subMap.put("prdWght"		, paHalfGoods.getWeight());
		prdSelInfoMap.put("refundYn"		, "Y");//고정 - 반품 불가 여부는 브랜드 코드로 관리하기로 함. (하프클럽 문의결과 이 컬럼은 별 의미없는 컬럼이라는 답변 받음)
		prdSelInfoMap.put("outStockDispYn"	, "Y");//품절시 노출 여부
		apiDataMap.put("prdSelInfo", prdSelInfoMap);
		
		//배송정보
		Map<String, Object> prdDlvInfoMap = new HashMap<String, Object>();
		Map<String, Object> subMap 	  = new HashMap<String, Object>();
		subMap.put("dlvTmpltSeq", paHalfGoods.getPaShipCostCode());//배송 템플릿 번호
		prdDlvInfoMap.put("dlvRtrnAmtInfo", subMap);
		apiDataMap.put("prdDlvInfo", prdDlvInfoMap);
		
		//단품 옵션 정보 (단품 리스트 loop)

		 Map<String, Object> prdOptInfoMap 		= new HashMap<String, Object>();
		 List<Map<String, Object>> optStckList 	= new ArrayList<Map<String,Object>>();
		 for (PaHalfGoodsdtMapping goodsDtMappingItem : goodsdtMapping) {
			 Map<String, Object> subMap2 = new HashMap<String, Object>();
			 
			 if(isUpdate && goodsDtMappingItem.getPaOptionCode() != null) {
				 subMap2.put("stockNo", goodsDtMappingItem.getPaOptionCode()); //단품 옵션 키
			 } else if(isUpdate && goodsDtMappingItem.getPaOptionCode() == null && !"00".equals(goodsDtMappingItem.getSaleGb())) {
				 continue;//수정 시 판매상태가 00이 아니면서 옵션코드가 없는 건은 넘긴다.
			 } else if(isUpdate && goodsDtMappingItem.getPaOptionCode() == null && !"1".equals(goodsDtMappingItem.getUseYn())) {
				 continue; //판매중이면서 연동하기전에 옵션명이 두번 바뀌었을때
			 } else if(!isUpdate && !"00".equals(goodsDtMappingItem.getSaleGb())) {
				 continue;
		 	 } else if(!isUpdate && !"1".equals(goodsDtMappingItem.getUseYn())) {
		 		 continue;
		 	 }
		
			 subMap2.put("optValueNm1"	, goodsDtMappingItem.getGoodsdtInfo()); //단품 상세
			 subMap2.put("realStckQty"	, goodsDtMappingItem.getTransOrderAbleQty()); //실재고 수량 (단품 재고수량의 70%);
			 subMap2.put("tempStckQty"	, goodsDtMappingItem.getTransOrderAbleQty()); //전산 재고 수량
			 subMap2.put("optValueArf"	, goodsDtMappingItem.getGoodsCode() + "_" + goodsDtMappingItem.getGoodsdtCode() + "_" +  goodsDtMappingItem.getGoodsdtSeq()); //상품코드_옵션코드(임의설정 구분 값)
			 subMap2.put("addPrc"		, 0); //옵션가
			 if("00".equals(goodsDtMappingItem.getSaleGb()) && "1".equals(goodsDtMappingItem.getUseYn())) {
				 subMap2.put("useYn", "Y");	
		 	 }else {
				 subMap2.put("useYn", "N");
			 }
			 optStckList.add(subMap2);
			 prdOptInfoMap.put("optItemNm1", goodsDtMappingItem.getGoodsdtInfoKind());//옵션명 : 단일상품 OR TPAGOODSDT.GOODSDT_INFO_KIND
		 }
		 prdOptInfoMap.put("optStckList", optStckList);
		 apiDataMap.put("prdOptInfo", prdOptInfoMap);
		 
	}

	@Override
	public void setGoodsContentsInfo(PaHalfGoodsVO paHalfGoods, List<PaGoodsOffer> paHalfGoodsOffer, ParamMap apiDataMap, boolean isUpdate) throws Exception {
		Config imageUrl 	 = systemProcess.getConfig("IMG_SERVER_1_URL");
		String imageURL 	 = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
        String imageResizePath = "/dims/resize/600X600";
		String goodsDescData = "";
				
		// 정책정보
		Map<String ,Object > prdMstInfoMap 	= new HashMap<String, Object>();
		prdMstInfoMap.put("prdNo"	 , paHalfGoods.getProductNo()); // 하프클럽 상품 번호
		apiDataMap.put("prdMstInfo"	 , prdMstInfoMap);
		
		//기본정보
		Map<String ,Object > prdBaseInfoMap  		= new HashMap<String, Object>();
		apiDataMap.put("prdBaseInfo" , prdBaseInfoMap);
		
		//고시정보
		List<Map<String, Object>> subList 	= new ArrayList<Map<String,Object>>();
		for (PaGoodsOffer offerItem : paHalfGoodsOffer) {
			Map<String ,Object > notiItemMap = new HashMap<String, Object>();
			notiItemMap.put("notiItemGrpCd"	, offerItem.getPaOfferType()); //고시정보 코드(하프클럽)
			notiItemMap.put("notiItemCd"	, offerItem.getPaOfferCode()); //고시정보 순서(하프클럽)
			notiItemMap.put("notiItemValue"	, offerItem.getPaOfferExt().replaceAll("\\p{C}", "")); //고시정보 내용
			subList.add(notiItemMap);
		}
		Map<String ,Object > notiItemListMap = new HashMap<String, Object>();
		notiItemListMap.put("notiItemList" , subList);
		apiDataMap.put("prdNotiInfo"	   , notiItemListMap);
		
		
		//인증정보
		Map<String ,Object > prdCertiInfoMap = new HashMap<String, Object>();
		prdCertiInfoMap.put("certiTypCd", "03"); //"03"고정 (상세 기술서 별도 표기)
		if("1".equals(paHalfGoods.getAdultYn())) {
			prdCertiInfoMap.put("adltCertYn"	, "Y"); //성인 인증 여부(default : N)
		}else {
			prdCertiInfoMap.put("adltCertYn"	, "N"); //성인 인증 여부(default : N)
		}
		apiDataMap.put("prdCertiInfo" , prdCertiInfoMap);
	
		
		//상품설명
		//기술서 Setting
		String goodsCom = (!ComUtil.NVL(paHalfGoods.getGoodsCom()).equals("")) ? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4><pre>" + paHalfGoods.getGoodsCom() + "</pre></div></div>") : "";
		String noDelyImage = "";
		String collectIamge= "";
		
		if(paHalfGoods.getDelyNoImage() != null && !"".equals(paHalfGoods.getDelyNoImage())) {
			noDelyImage =  "<img alt='' src='" + paHalfGoods.getDelyNoImage() + "' /><br /><br /> ";
		}
		if(paHalfGoods.getCollectImage() != null  && !"".equals(paHalfGoods.getCollectImage()) ) {
			collectIamge = "<img alt='' src='" + paHalfGoods.getCollectImage() + "' /><br /><br /> ";
		}
		
		goodsDescData = ("<div palign='center'><img alt='' src='" + paHalfGoods.getTopImage() + "' /><br /><br /><br />"	//상단 이미지 + 착불 이미지
				+ noDelyImage //제주도서산간 불가
				+ collectIamge //착불이미지
				+ goodsCom	//상품 구성
				+ paHalfGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br /><img alt='' src='"	//기술서
				+ paHalfGoods.getBottomImage() + "' /></div>");	//하단 이미지
		
		//하프클럽 Width 제거 후 연동 (하프클럽 측 요청)
		String widthPattern = "width:('|\\\")?[0-9]*('|\\\")?px";
		goodsDescData = goodsDescData.replaceAll(widthPattern, "");
		goodsDescData = goodsDescData.replace("\"", "'");
		
		if(!ComUtil.NVL(paHalfGoods.getNoticeExt()).equals("")) {
			goodsDescData = paHalfGoods.getNoticeExt() + goodsDescData;
		}

		Map<String ,Object > prdDescInfoMap 		= new HashMap<String, Object>();

		prdDescInfoMap.put("prdDescTypCd"	, "02"); //"02"고정 (01 - text , 02 - HTML)
		prdDescInfoMap.put("prdDescContClob", goodsDescData); //스토아 웹 기술서내용 
		
		String orgCode = paHalfGoods.getOriginCode();
		
		if(paHalfGoods.getOriginCode() == null || "".equals(orgCode) || 
				"60".equals(paHalfGoods.getLgroup())) { // 대카테고리 식품일 경우 '상품 상세페이지 참조' 연동 (2024.02.29)
			prdDescInfoMap.put("orgnTypCd"		, "03"); // 상품 상세페이지 참조
		}else if("KR".equals(orgCode)) {
			prdDescInfoMap.put("orgnTypCd"		, "01"); // 국내
		}else {
			prdDescInfoMap.put("orgnTypCd"		, "02"); // 해외
		}
		prdDescInfoMap.put("orgnCd", orgCode); //원산지 코드(하프클럽)
		
		apiDataMap.put("prdDescInfo" , prdDescInfoMap);
				
		//상품이미지
		Map<String ,Object > prdImgInfoMap 		= new HashMap<String, Object>();

		prdImgInfoMap.put("basicImgUrl", imageURL + paHalfGoods.getImageUrl() + paHalfGoods.getImageP() + imageResizePath); //상품대표 이미지
		
        if (StringUtils.isNotBlank(paHalfGoods.getImageAP())) { // 상품 추가 이미지1
           	prdImgInfoMap.put("add1ImgUrl", imageURL + paHalfGoods.getImageUrl() + paHalfGoods.getImageAP() + imageResizePath);
        }
        if (StringUtils.isNotBlank(paHalfGoods.getImageBP())) { // 상품 추가 이미지2
          	prdImgInfoMap.put("add2ImgUrl", imageURL + paHalfGoods.getImageUrl() + paHalfGoods.getImageBP() + imageResizePath);
        }
        if (StringUtils.isNotBlank(paHalfGoods.getImageCP())) { // 상품 추가 이미지3
           	prdImgInfoMap.put("add3ImgUrl", imageURL + paHalfGoods.getImageUrl() + paHalfGoods.getImageCP() + imageResizePath);
        }
        if (StringUtils.isNotBlank(paHalfGoods.getImageDP())) { // 상품 추가 이미지4
          	prdImgInfoMap.put("add4ImgUrl", imageURL + paHalfGoods.getImageUrl() + paHalfGoods.getImageDP() + imageResizePath);
        }
		apiDataMap.put("prdImgInfo", prdImgInfoMap);
	
		
		List<Map<String, Object>> attriMainList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> attriList 	= new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> attriList2 	= new ArrayList<Map<String,Object>>();
		//속성정보
		for (int i = 1; i <= 5; i++) {
			String attrCd = "0";
			if(i < 4) { //속성정보 모두 사용, AC001(성별, 01- 남성, 02- 여성, 03- 남녀공용))
				Map<String ,Object > attriMap1 = new HashMap<String, Object>();
				attriMap1.put("attrGrpCd", "AC001");
				attriMap1.put("attrCd"	, attrCd+i);
				attriMap1.put("chkYn"	, "Y");
				attriList.add(attriMap1);
			}
			//속성정보 모두 사용, AC002(시즌, 01- 봄, 02- 여름, 03- 가을, 04- 겨울, 05- 사계절))
			Map<String ,Object > attriMap2 = new HashMap<String, Object>();
			attriMap2.put("attrGrpCd", "AC002");
			attriMap2.put("attrCd"	, attrCd+i);
			attriMap2.put("chkYn"	, "Y");
			attriList2.add(attriMap2);
		}
		Map<String ,Object > attrItemListMap = new HashMap<String, Object>();
		Map<String ,Object > attrItemListMap2 = new HashMap<String, Object>();

		attrItemListMap.put("attrItemList", attriList);
		attriMainList.add(attrItemListMap);

		attrItemListMap2.put("attrItemList", attriList2);
		attriMainList.add(attrItemListMap2);
		
		Map<String ,Object > prdAttrInfoMap = new HashMap<String, Object>();

		prdAttrInfoMap.put("comAttrList", attriMainList);
		apiDataMap.put("prdAttrInfo", prdAttrInfoMap);
		
	}

	@Override
	public List<PaHalfGoodsVO> selectPaHalfGoodsImageList(ParamMap paramMap) throws Exception {
		return paHalfGoodsDAO.selectPaHalfGoodsImageList(paramMap);
	}

	@Override
	public void updatePaHalfGoodsImage(PaHalfGoodsVO paHalfGoods) throws Exception {
		int executedRtn = 0;

		executedRtn = paHalfGoodsDAO.updatePaHalfGoodsImage(paHalfGoods);

		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });
		}
	}

	public Map<String, String> selectGoodsEntpSlip(ParamMap paramMap) throws Exception {
		return paHalfGoodsDAO.selectGoodsEntpSlip(paramMap);
	}
	
	@Override
	public String savePaHalfGoods(PaHalfGoodsVO paHalfGoods, List<PaHalfGoodsdtMapping> goodsdtMapping,  ParamMap paramMap) throws Exception {
		
		// 특정 메시지에 대해서 수기중단
		if (isRejectMsg(paramMap.getString("message"))) {
			paHalfGoods.setReturnNote(ComUtil.subStringBytes(paramMap.getString("message"), 2000));
			paHalfGoodsDAO.stopTransTarget(paHalfGoods);
			return Constants.SAVE_FAIL;
		}
		
		if(!"200".equals(paramMap.get("code"))) {
			paHalfGoods.setReturnNote(ComUtil.subStringBytes(paramMap.getString("message"), 2000));
			paHalfGoods.setPaStatus			(null);
			paHalfGoods.setPrdPrcNo			(null);
			paHalfGoods.setProductNo		(null);
			paHalfGoods.setCurrentTime      (null);
			paHalfGoodsDAO.updatePaHalfGoods(paHalfGoods);
			return Constants.SAVE_FAIL;
		}
		
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try{
			
			for(PaHalfGoodsdtMapping goodsdt : goodsdtMapping){
				executedRtn = paHalfGoodsDAO.updateTpaGoodsdtMapping(goodsdt);
				if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAHALFGOODSDTMAPPING UPDATE" });
			}
			
			executedRtn = paHalfGoodsDAO.updatePaHalfGoodsOffer(paHalfGoods);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TPAGOODSOFFER UPDATE" });
			}
			
			executedRtn = paHalfGoodsDAO.updatePaHalfGoodsImage(paHalfGoods);
			if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });	
			}
			
			//상품 등록일 경우에만 사용
			if (!"MODIFY".equals(paramMap.getString("modCase"))) {
				executedRtn = paHalfGoodsDAO.updatePaGoodsTarget(paHalfGoods);
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TPAGOODSTARGET UPDATE" });
				}
			}
			
			executedRtn = paHalfGoodsDAO.updatePaGoodsPrice(paHalfGoods);
			if(!"MODIFY".equals(paramMap.getString("modCase")) ) {
				if (executedRtn < 1) {
					throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICEAPPLY UPDATE" });
				}
			}
			
			
			paHalfGoods.setPaStatus("30");
			executedRtn = paHalfGoodsDAO.updatePaHalfGoods(paHalfGoods);
			if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAHALFGOODS UPDATE" });
			
			
		} catch (Exception e) {
			paramMap.put("code"		, "500");
			paramMap.put("message"	, "savePaHalfGoods :: " +  PaHalfComUtill.getErrorMessage(e));
			log.info(PaHalfComUtill.getErrorMessage(e));
			rtnMsg = Constants.SAVE_FAIL;
		}
		return rtnMsg;
	}
	
	private boolean isRejectMsg(String rejectMsg) {

		if (rejectMsg == null || rejectMsg.isEmpty())
			return false;

		String[] rejectMatch = new String[] { "옵션1(명) 중복입니다"};

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}

	@Override
	public PaHalfGoodsVO selectPaHalfGoodsContentsInfo(ParamMap paramMap) throws Exception {
		return paHalfGoodsDAO.selectPaHalfGoodsContentsInfo(paramMap);
	}

	@Override
	public void checkGoodsInsertFail(ParamMap paramMap) throws Exception {
		int executedRtn = 0;
		int count = paHalfGoodsDAO.selectGoodsResetCount(paramMap);
		int tryCnt = ConfigUtil.getInt("PAHALF_GOODS_INSERT_TRY_CNT") ;
		if (count > tryCnt ) {
			PaHalfGoodsVO halfGoods = new PaHalfGoodsVO();
			halfGoods.setGoodsCode(paramMap.getString("goodsCode"));
			halfGoods.setPaStatus("20");
			halfGoods.setProductNo(null);
			halfGoods.setPrdPrcNo(null);
			halfGoods.setPaCode(paramMap.getString("paCode"));
			halfGoods.setReturnNote(paramMap.getString("prgId") + "||하프클럽 상품등록리셋 "+ tryCnt + "회 초과 시도로 인한 입점반려처리");

            executedRtn = paHalfGoodsDAO.rejectTransTarget(halfGoods);
            if (executedRtn < 1) {
				throw processException("msg.cannot_save", new String[] { "TPAHALFGOODS UPDATE" });
			}
		}
	}
	
	@Override
	public void updatePaHalfGoodsOffer(PaHalfGoodsVO paHalfGoods) throws Exception {
		int executedRtn = 0;
		executedRtn = paHalfGoodsDAO.updatePaHalfGoodsOffer(paHalfGoods);
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] { "TPAGOODSOFFER UPDATE" });
		}	
		
	}

	@Override
	public void updateEntpSlipInsertFail(PaHalfGoodsVO paHalfGoods) throws Exception {
		//단순 배송비 같은 배송 정책 의 변경이 아닌 , 상품 자체의 출고지 또는 회수지, 배송정책이 바뀌어서 연동이 되어야하는데 배송 템플릿 등록이 실패한 경우는 상품 중단처리
		int executedRtn = 0;
		paHalfGoods.setReturnNote	("상품 수정_배송비 템플릿 연동 실패"); 
		paHalfGoods.setPaStatus		("30"); 
		paHalfGoods.setPaSaleGb		("30");
		paHalfGoods.setTransSaleYn	("1");
		
		executedRtn = paHalfGoodsDAO.updatePaHalfGoods(paHalfGoods);
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] { "TPAHALFGOODS UPDATE" });
		}	
	}
	/* 
	@Override
	public String selectPaHalfBrandNo(PaHalfGoodsVO paHalfGoods) throws Exception {
		
		String paBrandNo = paHalfGoodsDAO.selectPaHalfBrandNo(paHalfGoods);

		if(paBrandNo == null || paBrandNo == "") throw processException("msg.no.select", new String[] { "TPAHALFBRANDMAPPING SELECT" });

		return paBrandNo;
	}
	*/

	@Override
	public PaHalfGoodsVO selectPrdPrcNoList(ParamMap paramMap) throws Exception {
		return paHalfGoodsDAO.selectPrdPrcNoList(paramMap);
	}
	
	@Override
	public void updatePrdPrcNo(PaHalfGoodsVO paHalfGoods) throws Exception {
		int executedRtn = 0;
		
		executedRtn = paHalfGoodsDAO.updatePaHalfGoods(paHalfGoods);
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAGOODS UPDATE" });
		
	}

	@Override
	public void updatePaHalfGoodsReset(ParamMap paramMap) throws Exception {
		
		int executedRtn = 0;
		
		//하프클럽 상품 번호 리셋
		PaHalfGoodsVO halfGoods = new PaHalfGoodsVO();
		halfGoods.setGoodsCode	(paramMap.getString("goodsCode"));
		halfGoods.setProductNo(null);
		halfGoods.setPrdPrcNo(null);
		halfGoods.setPaCode		(paramMap.getString("paCode"));
		
		executedRtn = paHalfGoodsDAO.updatePaHalfGoodsReset(halfGoods);
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] { "TPAHALFGOODS UPDATE" });
		}
		
		//상품 번호가 리셋되면 상품 옵션코드도 리셋되어야 함 
		executedRtn = paHalfGoodsDAO.updatePaGoodsdtMappingReset(paramMap);
		if (executedRtn < 1) {
			throw processException("msg.cannot_save", new String[] { "TPAHALFGOODSDTMAPPING UPDATE" });
		}
		
        //TPAGOODSTARGET 리셋
        executedRtn = paHalfGoodsDAO.updatePaGoodsTargetReset(halfGoods);
        if (executedRtn < 1) {
            throw processException("msg.cannot_save", new String[] { "TPAGOODSTARGET UPDATE" });
        }
		
	}

	@Override
	public void savePrdPlcyDtInfoModify(PaHalfGoodsVO paHalfGoods, List<PaHalfGoodsdtMapping> goodsdtMappingList) throws Exception {
		int executedRtn = 0;
		
		try{
			for(PaHalfGoodsdtMapping goodsdt : goodsdtMappingList){
				executedRtn = paHalfGoodsDAO.updateTpaGoodsdtMapping(goodsdt);
				if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAHALFGOODSDTMAPPING UPDATE" });
			}
			
			paHalfGoods.setPaStatus(null);
			executedRtn = paHalfGoodsDAO.updatePaHalfGoods(paHalfGoods);
			if (executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAHALFGOODS UPDATE" });
			
		} catch (Exception e) {
			log.info(PaHalfComUtill.getErrorMessage(e));
		}
	}

	@Override
	public void updatePaHalfGoodsReturnNote(PaHalfGoodsVO paHalfGoods, ParamMap paramMap) throws Exception {
		//기준정보 등록 실패시 리턴노트 작성
		paHalfGoods.setReturnNote(ComUtil.subStringBytes(paramMap.getString("message"), 2000));
		paHalfGoodsDAO.updatePaHalfGoods(paHalfGoods);
	}

	@Override
	public String selectDelyNoAreaGb(ParamMap paramMap) throws Exception {
		return paHalfGoodsDAO.selectDelyNoAreaGb(paramMap);
	}

	@Override
	public PaHalfGoodsVO selectPaHalfGoodsStatus(ParamMap paramMap) throws Exception {
		return paHalfGoodsDAO.selectPaHalfGoodsStatus(paramMap);
	}

	@Override
	public void updateProceeding(ParamMap paramMap) throws Exception {
		paHalfGoodsDAO.updateProceeding(paramMap);
	}

	@Override
	public void updateClearProceeding(ParamMap paramMap) throws Exception {
		paHalfGoodsDAO.updateClearProceeding(paramMap);
		
	}
	
}
