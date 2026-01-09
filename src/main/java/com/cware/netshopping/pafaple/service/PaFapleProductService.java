package com.cware.netshopping.pafaple.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.PaSaleGb;
import com.cware.netshopping.common.code.PaStatus;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.StringUtil;
import com.cware.netshopping.domain.PaFapleGoodsVO;
import com.cware.netshopping.domain.model.Config;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsPriceApply;
import com.cware.netshopping.pacommon.v2.domain.PaGoodsSync;
import com.cware.netshopping.pacommon.v2.domain.GoodsVod;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsPriceApplyMapper;
import com.cware.netshopping.pacommon.v2.repository.PaGoodsTargetMapper;
import com.cware.netshopping.pacommon.v2.repository.GoodsVodMapper;
import com.cware.netshopping.pacommon.v2.service.SyncProductService;
import com.cware.netshopping.pafaple.domain.Options;
import com.cware.netshopping.pafaple.repository.PaFapleGoodsMapper;
import com.cware.netshopping.pafaple.util.PaFapleApiRequest;
import com.cware.netshopping.pafaple.util.PaFapleConnectUtil;
import com.cware.netshopping.pafaple.domain.Product;


@Service
public class PaFapleProductService {
	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	TransLogService transLogService;
	
	@Autowired
	PaFapleGoodsMapper paFapleGoodsMapper;
	
	@Autowired
	PaGoodsPriceApplyMapper goodsPriceMapper;
	
    @Autowired
    GoodsVodMapper goodsVodMapper;
    
	@Autowired
	PaFapleConnectUtil paFapleConnectUtil;
	
	@Autowired
	PaFapleResultService paFapleResultService;
	
	@Autowired
	PaFapleApiRequest paFapleApiRequest;
	
	@Autowired
	SyncProductService syncProductService;
	
	@Autowired
	PaGoodsTargetMapper goodsTargetMapper;
	
	@Autowired
	FapleProductService fapleProductService;
	private Logger log = LoggerFactory.getLogger(this.getClass());
	
	public ResponseMsg registerProduct(String goodsCode, String paCode, String procId, long transBatchNo,String modCase) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PAFAPLEAPI_01_001");
		serviceLog.setServiceNote("[API]패션플러스-상품 등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.FAPLE.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품입점
		ResponseMsg result = callRegisterProduct(goodsCode, paCode, procId, serviceLog, modCase);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	public ResponseMsg updateProduct(String goodsCode, String paCode, String procId, long transBatchNo) {
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(goodsCode);
		serviceLog.setTransType(TransType.PRODUCT.name());
		serviceLog.setServiceName("IF_PAFAPLEAPI_01_002");
		serviceLog.setServiceNote("[API]패션플러스-상품수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.FAPLE.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 상품수정
		ResponseMsg result = callUpdateProduct(goodsCode, paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	@SuppressWarnings("unchecked")
	private ResponseMsg callRegisterProduct(String goodsCode, String paCode, String procId, PaTransService serviceLog, String modCase) {

		ResponseMsg result = new ResponseMsg("", "");

		PaFapleGoodsVO paFapleGoods = null;

		try {
			log.info("===== 상품등록서비스 Start - {} =====", goodsCode);

			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("apiCode", serviceLog.getServiceName());
			paramMap.put("paGroupCode", PaGroup.FAPLE.code());
			
			// 상품정보 조회
			paramMap.put("modCase", modCase);
			paFapleGoods = paFapleGoodsMapper.selectPaFapleGoodsInfo(paramMap.get());

			if (paFapleGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}
			
			if (paFapleGoods.getBrandId() == null) {
				result.setCode("404");
				result.setMessage("등록된 브랜드가 없습니다. 상품코드 : " + goodsCode);
				return result;
			}

			if (paFapleGoods.getItemId() != null && modCase.equals("INSERT")) {
				result.setCode("411");
				result.setMessage("이미 등록된 상품입니다. 패션플러스 상품코드 : " + paFapleGoods.getItemId());
				return result;
			}

			if (paFapleGoods.getDescribeExt() == null) {
				paFapleGoods.setDescribeExt("");
				if (paFapleGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			// 정보고시 조회
			List<PaGoodsOffer> goodsOffer = paFapleGoodsMapper.selectPaFapleGoodsOfferList(paramMap.get());
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.FAPLE.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}
            
			// 상품 VOD 조회
            GoodsVod goodsVod = goodsVodMapper.getGoodsVod(goodsCode);
            
			// 상품설명설정
			settingDescribeExt(paFapleGoods);

			// 단품 옵션 조회
			List<PaGoodsdtMapping> goodsdtMapping = paFapleGoodsMapper.selectPaFapleGoodsdtInfoList(paramMap.get());

			paFapleGoods.setModifyId(procId);

			// 패플 상품 전문
            Product product = createProduct(paFapleGoods, goodsdtMapping, goodsOffer, goodsPrice, goodsVod, serviceLog.getTransServiceNo());

			// Body 세팅
			ParamMap apiDataObject = new ParamMap();

			apiDataObject.put("body", paFapleApiRequest.getBody(product));

			log.info("상품등록 API 호출 {}", goodsCode);
			
			Map<String, Object> productMap = new HashMap<String, Object>();	
			List<Map<String, Object>> productList = new ArrayList<Map<String,Object>>();
			productMap = paFapleConnectUtil.callPaFapleAPI(paramMap, apiDataObject , serviceLog);
			
			if(productMap == null) {
				result.setCode("500");
				result.setMessage("상품 등록에 실패했습니다(Response : null)");	
				throw new TransApiException(result.getMessage(), result.getCode());
			}
			
			if(productMap.get("Status").equals("OK")) {
				result.setCode("200");
				result.setMessage(PaFapleApiRequest.API_SUCCESS_CODE);
				paFapleGoods.setItemId(productMap.get("ItemID").toString());
				paFapleGoods.setPaStatus("30");
				productList = (List<Map<String, Object>>) productMap.get("Options");
				for(PaGoodsdtMapping goodsdt: goodsdtMapping) {
					String VendorOptID = goodsdt.getPaCode() + goodsdt.getGoodsCode() + goodsdt.getGoodsdtCode() + goodsdt.getGoodsdtSeq();
					for(Map<String, Object> option :productList) {
						if(VendorOptID.equals(option.get("VendorOptID"))) {
							goodsdt.setPaOptionCode(option.get("OptID").toString());
						}
					}
				}
			    paFapleResultService.saveTransProduct(paFapleGoods, goodsPrice, goodsdtMapping, modCase);
			}else {
				result.setCode("500");
				String message = productMap.get("Message") != null ? productMap.get("Message").toString() : "";
				
				if("".equals(message)) {
					result.setMessage("상품 등록에 실패했습니다");	
				}else {
					result.setMessage(StringUtil.truncate(message, 500));
				}
				paFapleGoodsMapper.rejectTransTarget(goodsCode, paCode, procId, StringUtil.truncate(message, 500));
			}
			
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());

			// 반려처리 제외 메시지 체크
			if (!isNotRejectMsg(result.getCode(), result.getMessage())) {
				paFapleGoodsMapper.rejectTransTarget(goodsCode, paCode, procId, StringUtil.truncate(result.getMessage(), 500));
			}

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 상품등록서비스 End - {} =====", goodsCode);
		}

		return result;
	}
	
	/**
	 * 반려제외여부
	 * 
	 * @param resultCode
	 * @param rejectMsg
	 * @return
	 */
    private boolean isNotRejectMsg(String resultCode, String rejectMsg) {

        if (!StringUtils.hasText(rejectMsg) )
            return false;

        String[] rejectNotMatch =
                new String[] {};

        return Arrays.stream(rejectNotMatch).anyMatch(s -> rejectMsg.contains(s));
    }
	
	/**
	 * 기술서와 공지사항 설정
	 * 
	 * @param paLtonGoods
	 */
	private void settingDescribeExt(PaFapleGoodsVO pafapleGoods) {

		String goodsCom = StringUtils.hasText(pafapleGoods.getGoodsCom())
				? ("<div style='line-height: 2.0em; font-family: NanumBarunGothic; font-size: 19px;'><div><h4>&middot;&nbsp;상품구성<h4><pre>"
						+ pafapleGoods.getGoodsCom() + "</pre></div></div>")
				: "";

		// 웹기술서
		pafapleGoods.setDescribeExt(
				"<div align='center'><img alt='' src='" + pafapleGoods.getTopImage() + "' /><br /><br /><br />" // 상단이미지
						+ goodsCom // 상품구성
						+ pafapleGoods.getDescribeExt().replaceAll("src=\"//", "src=\"http://") + "<br /><br /><br />" // 기술서
						+ "<img alt='' src='" + pafapleGoods.getBottomImage() + "' /></div>"); // 하단이미지

		// 제휴 공지사항
		if (StringUtils.hasText(pafapleGoods.getNoticeExt())) {
			pafapleGoods.setDescribeExt(
					pafapleGoods.getNoticeExt().replaceAll("src=\"//", "src=\"http://") + pafapleGoods.getDescribeExt());
		}
	}
	
	private Product createProduct(PaFapleGoodsVO paFapleGoods, List<PaGoodsdtMapping> goodsdtMapping,
            List<PaGoodsOffer> goodsOffer, PaGoodsPriceApply goodsPrice, GoodsVod goodsVod, long transServiceNo) throws Exception {
		Product product = new Product();
		List<Options> OptionsList = new ArrayList<Options>();
		if(goodsdtMapping != null) { //상품등록, 단품세팅 
			for (PaGoodsdtMapping goodsDt : goodsdtMapping) {
			  Options option = new Options();
			  String tempGoodsdtCode = goodsDt.getPaCode() + goodsDt.getGoodsCode() + goodsDt.getGoodsdtCode() + goodsDt.getGoodsdtSeq();
			  option.setVendorOptID(tempGoodsdtCode);
			  option.setColor(goodsDt.getGoodsdtInfo());
			  option.setStockQty(ComUtil.objToInt(goodsDt.getTransOrderAbleQty()));
			  option.setOptPrice(0);
			  OptionsList.add(option);
		    }
			product.setOptions(OptionsList);
		}else { //상품 수정(패플은 상품수정으로 단품을 추가 할 수 없다. 따라서 가 단품이 없으면 상품 수정이다)
		    //이미지 변경여부(상품수정에서만 사용)
			product.setIsImgUpdate("Y");
		}
		// 상품이미지 주소 세팅
		Config imageUrl = systemService.getConfig("IMG_SERVER_1_URL");
		String imageServer = "http:" + imageUrl.getVal().substring(imageUrl.getVal().indexOf("//"));
				
		//관리 품목 번호 상품번호_실적브랜드ID 
		product.setItemNo(paFapleGoods.getItemNo());
		//관리 품목명 상품명_상품코드_실적브랜드ID
		product.setItemName(paFapleGoods.getGoodsName()+"_"+paFapleGoods.getGoodsCode()+"_"+paFapleGoods.getBrandId());	
		//수정에서 사용 패플상품코드
		if(paFapleGoods.getItemId() != null) {
			product.setItemID(ComUtil.objToInt(paFapleGoods.getItemId()));
		}
		//판매자단가
		product.setSalePrice(goodsPrice.getBestPrice());		
		//소비자단가
		product.setConsumerPrice(goodsPrice.getSalePrice());	
		//실적브랜드ID
		product.setBrandId(ComUtil.objToInt(paFapleGoods.getBrandId()));		
		//SK는 단일 카테고리 이므로  카테고리1만 연동
		product.setCategory1(ComUtil.objToInt(paFapleGoods.getPaLmsdKey()));	
		//전시 상품명
		product.setDisplayItemName(paFapleGoods.getGoodsName() + "_" +paFapleGoods.getBrandId());
		//기술서
		product.setDescription(paFapleGoods.getDescribeExt());
		
		//정보고시
		product.setProduct_num(ComUtil.objToInt(goodsOffer.get(0).getPaOfferType()));
		for(int a = 0; a < goodsOffer.size() ; a++) {
			switch(goodsOffer.get(a).getPaOfferCode().toString()){
			case"Product_Material":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 500) {
					product.setProduct_Material(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),500));
				}else {
					product.setProduct_Material(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Color":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 300) {
					product.setProduct_Color(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),300));
				}else {
					product.setProduct_Color(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Size":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 7900) {
					product.setProduct_Size(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),7900));
				}else {
					product.setProduct_Size(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Maker":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 300) {
					product.setProduct_Maker(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),300));
				}else {
					product.setProduct_Maker(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Home":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 40) {
					product.setProduct_Home(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),40));
				}else {
					product.setProduct_Home(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Washing":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 1000) {
					product.setProduct_Washing(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),1000));
				}else {
					product.setProduct_Washing(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_MakeYear":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 100) {
					product.setProduct_MakeYear(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),100));
				}else {
					product.setProduct_MakeYear(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Warranty":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 1000) {
					product.setProduct_Warranty(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),1000));
				}else {
					product.setProduct_Warranty(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_KindType":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 100) {
					product.setProduct_KindType(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),100));
				}else {
					product.setProduct_KindType(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Comprisal":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 500) {
					product.setProduct_Comprisal(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),500));
				}else {
					product.setProduct_Comprisal(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Model":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 100) {
					product.setProduct_Model(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),100));
				}else {
					product.setProduct_Model(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_KC":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 50) {
					product.setProduct_KC(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),50));
				}else {
					product.setProduct_KC(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_InstallCost":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 50) {
					product.setProduct_InstallCost(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),50));
				}else {
					product.setProduct_InstallCost(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Voltage":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 50) {
					product.setProduct_Voltage(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),50));
				}else {
					product.setProduct_Voltage(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Type":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 1000) {
					product.setProduct_Type(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),1000));
				}else {
					product.setProduct_Type(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Weight":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 50) {
					product.setProduct_Weight(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),50));
				}else {
					product.setProduct_Weight(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Period":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 50) {
					product.setProduct_Period(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),50));
				}else {
					product.setProduct_Period(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Way":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 1000) {
					product.setProduct_Way(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),1000));
				}else {
					product.setProduct_Way(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Ingredient":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 500) {
					product.setProduct_Ingredient(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),500));
				}else {
					product.setProduct_Ingredient(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_EvaluateYN":
				product.setProduct_EvaluateYN("Y");
				break;
			case"Product_Careful":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 1000) {
					product.setProduct_Careful(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),1000));
				}else {
					product.setProduct_Careful(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Grade":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 50) {
					product.setProduct_Grade(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),50));
				}else {
					product.setProduct_Grade(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_Function":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 50) {
					product.setProduct_Function(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),50));
				}else {
					product.setProduct_Function(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_WarrantyofferYN":
				product.setProduct_WarrantyofferYN("Y");
				break;
			case"Product_UseAge":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 50) {
					product.setProduct_UseAge(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),50));
				}else {
					product.setProduct_UseAge(goodsOffer.get(a).getPaOfferExt().toString());
				}
				break;
			case"Product_ASTel":
				if(goodsOffer.get(a).getPaOfferExt().toString().getBytes().length >= 300) {
					product.setProduct_ASTel(ComUtil.subStringBytes(goodsOffer.get(a).getPaOfferExt().toString(),300));
				}else {
					product.setProduct_ASTel(goodsOffer.get(a).getPaOfferExt().toString());							
				}
				break;
			case"OverSeaDeliveryYN":
				product.setOverSeaDeliveryYN("N");
				break;
			case"Min_OverSeaDeliveryDay":
				product.setMin_OverSeaDeliveryDay(ComUtil.objToInt(goodsOffer.get(a).getPaOfferExt()));
				break;
			case"Max_OverSeaDeliveryDay":
				product.setMax_OverSeaDeliveryDay(ComUtil.objToInt(goodsOffer.get(a).getPaOfferExt()));
				break;
			default:
				break;
			}
			         
		}
        				
		//이미지
		product.setImageURL1(imageServer + paFapleGoods.getImageUrl()+ paFapleGoods.getImageG());
		if(paFapleGoods.getImageAg() != null) {		
			product.setImageURL2(imageServer + paFapleGoods.getImageUrl()+ paFapleGoods.getImageAg());
		}else if(goodsdtMapping == null) {
			product.setImageURL2("del");
		}
		if(paFapleGoods.getImageBg() != null) {
			product.setImageURL3(imageServer + paFapleGoods.getImageUrl()+ paFapleGoods.getImageBg());
		}else if(goodsdtMapping == null) {
			product.setImageURL3("del");
		}
		if(paFapleGoods.getImageCg() != null) {
			product.setImageURL4(imageServer + paFapleGoods.getImageUrl()+ paFapleGoods.getImageCg());
		}else if(goodsdtMapping == null) {
			product.setImageURL4("del");
		}
        
        if(goodsVod != null) {
            product.setVideoURL(goodsVod.getGoodsVodUrl());
        }
        
        if(paFapleGoods.getIsbn() != null) {
			product.setISBNCode(paFapleGoods.getIsbn());
		}
		
		return product;

	}
	
	private ResponseMsg callUpdateProduct(String goodsCode, String paCode, String procId, PaTransService serviceLog) {

		ResponseMsg result = new ResponseMsg("", "");

		PaFapleGoodsVO paFapleGoods = null;

		try {
			log.info("===== 상품수정서비스 Start - {} =====", goodsCode);

			ParamMap paramMap = new ParamMap();
			paramMap.put("goodsCode", goodsCode);
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("apiCode", serviceLog.getServiceName());
			paramMap.put("paGroupCode", PaGroup.FAPLE.code());
			
			// 상품정보 조회
			paramMap.put("modCase", "MODIFY");
			paFapleGoods = paFapleGoodsMapper.selectPaFapleGoodsInfo(paramMap.get());

			if (paFapleGoods == null) {
				result.setCode("404");
				result.setMessage("처리할 내역이 없습니다.");
				return result;
			}

			if (paFapleGoods.getOriginItemId() == null) {
				result.setCode("411");
				result.setMessage("등록되지 않은 상품입니다. 상품코드 : " + goodsCode);
				return result;
			}

			if (paFapleGoods.getBrandId() == null) {
				result.setCode("404");
				result.setMessage("등록된 브랜드가 없습니다. 상품코드 : " + goodsCode);
				return result;
			}
			
			if (paFapleGoods.getDescribeExt() == null) {
				paFapleGoods.setDescribeExt("");
				if (paFapleGoods.getGoodsCom() == null) {
					result.setCode("420");
					result.setMessage("상품기술서를 입력하세요. 상품코드 : " + goodsCode);
					return result;
				}
			}

			// 정보고시 조회
			List<PaGoodsOffer> goodsOffer = paFapleGoodsMapper.selectPaFapleGoodsOfferList(paramMap.get());
			if (goodsOffer.size() < 1) {
				result.setCode("430");
				result.setMessage("상품정보고시를 입력하세요. 상품코드 : " + goodsCode);
				return result;
			}

			// 프로모션개선
			PaGoodsPriceApply goodsPrice = goodsPriceMapper.getGoodsPrice(goodsCode, PaGroup.FAPLE.code(), paCode);
			if (goodsPrice == null) {
				result.setCode("404");
				result.setMessage("정보가 불일치하거나 제외된 상품입니다. 상품동기화를 선행하세요.");
				return result;
			}
			            
            // 상품 VOD 조회
            GoodsVod goodsVod = goodsVodMapper.getGoodsVod(goodsCode);
                        
			// 상품설명설정
			settingDescribeExt(paFapleGoods);

			paFapleGoods.setModifyId(procId); 

			// 패션플러스 상품 전문
            Product product = createProduct(paFapleGoods,null, goodsOffer, goodsPrice, goodsVod, serviceLog.getTransServiceNo());
			
			// Body 세팅
			ParamMap apiDataObject = new ParamMap();

			apiDataObject.put("body", paFapleApiRequest.getBody(product));

			log.info("상품수정 API 호출 {}", goodsCode);
			
			Map<String, Object> productMap = new HashMap<String, Object>();	

			productMap = paFapleConnectUtil.callPaFapleAPI(paramMap, apiDataObject , serviceLog);
			
			if(productMap == null) {
				result.setCode("500");
				result.setMessage("상품 수정에 실패했습니다(Response : null)");	
				throw new TransApiException(result.getMessage(), result.getCode());
			}
			
			if(productMap.get("Status").equals("OK")) {
				result.setCode("200");
				result.setMessage(PaFapleApiRequest.API_SUCCESS_CODE);
			    paFapleResultService.saveTransProduct(paFapleGoods, goodsPrice, null, paramMap.get("modCase").toString());
			}else {
				result.setCode("500");
				String message = productMap.get("Message") != null ? productMap.get("Message").toString() : "";
				
				if("".equals(message)) {
					result.setMessage("상품 수정에 실패했습니다");	
				}else {
					result.setMessage(StringUtil.truncate(message, 500));
				}
				throw new TransApiException(result.getMessage(), result.getCode());
			}
			
		} catch (TransApiException ex) {
			
			if (paFapleResultService.applyRetention(goodsCode, paCode, procId, ex.getMessage())) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
				result.setMessage("삭제된 상품입니다. 재입점해주세요.");
				return result;
			}
			
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
			
			if (isRejectMsg(result.getMessage())) {
				// 특정 메시지에 대해서 수기중단
				paFapleGoodsMapper.stopTransTarget(goodsCode, paCode, procId,
						StringUtil.truncate(result.getMessage(), 500));
			}

		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 상품수정서비스 End - {} =====", goodsCode);
		}

		return result;
	}
	
	/**
	 * 수기중단대상 여부
	 * 
	 * @param rejectMsg
	 * @return
	 */
	private boolean isRejectMsg(String rejectMsg) {

		if (!StringUtils.hasText(rejectMsg))
			return false;

        String[] rejectMatch = new String[] { "진열상품명이 이미 존재","등록할 수 없는 단어", "직전 판매가", "상품고시법"};

		return Arrays.stream(rejectMatch).anyMatch(s -> rejectMsg.contains(s));
	}
	
	/**
	 * 티딜 상품 개별 전송
	 * @param goodsCode
	 * @param paCode
	 * @param procId
	 * @return
	 */
	public ResponseMsg transProduct(String goodsCode, String paCode, String procId) {
		ResponseMsg result = new ResponseMsg("", "");

		// 상품동기화
		PaGoodsSync sync = syncProductService.syncProduct(goodsCode, PaGroup.FAPLE.code(), procId);
		result = sync.getResponseMsg();
		if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) {
			return result;
		}

		PaFapleGoodsVO paFapleGoods = paFapleGoodsMapper.getGoods(goodsCode,paCode);
		
		if(paFapleGoods == null) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("연동 대상 상품이 아닙니다.");
			return result;
		}

		// 입점요청중인건은 처리하지 않음
		if (PaStatus.PROCEEDING.code().equals(paFapleGoods.getPaStatus())) {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
			result.setMessage("입점요청중인 상품입니다.");
			return result;
		}

		// 입점요청/입점반려건에 대해서 신규입점 요청
		if (PaStatus.REQUEST.code().equals(paFapleGoods.getPaStatus())
				|| PaStatus.REJECT.code().equals(paFapleGoods.getPaStatus())) {

			// 동기화에서 필터링된 경우
			if (String.valueOf(HttpStatus.NO_CONTENT.value()).equals(result.getCode())) {
				return result;
			}

			if (goodsTargetMapper.existsGoodsTarget(goodsCode, paCode, PaGroup.FAPLE.code()) < 1) {
				result.setCode(String.valueOf(HttpStatus.NO_CONTENT));
				result.setMessage("입점 대상 상품이 아닙니다(타겟데이터 없음)");
				return result;
			}

			// 신규입점
			result = registerProduct(goodsCode, paCode, procId, 0 ,"INSERT");
			if (String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
				result.setMessage("입점완료되었습니다.");
			}
			return result;
		}

		PaTransService transService;

		if ("1".equals(paFapleGoods.getTransSaleYn()) && PaSaleGb.SUSPEND.code().equals(paFapleGoods.getPaSaleGb())) {
			// 판매중지
			transService = fapleProductService.stopSaleProduct(goodsCode, paCode, procId);
			result.setCode(transService.getResultCode());
			if ("1".equals(transService.getSuccessYn())) {
				result.setMessage("판매중지되었습니다.");
			} else {
				result.setMessage(transService.getResultMsg());
			}
			return result;
		}
		
		boolean isUpdated = false;
		boolean isStock = false;
		boolean isResume = false;
		boolean isBrand = false;
		boolean isOption = false;
		
		
		if(paFapleGoods.getBrandId() == null || paFapleGoods.getBrandId().isEmpty()) {
			
			transService = fapleProductService.brandChangeProduct(goodsCode, paCode, procId);
			if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) { 
				result.setMessage(
						( result.getMessage() + " " ) + transService.getResultMsg());
				isBrand = true;
			}else{ // 브랜드(배송비) 변경 되면 상품 30 90 판매중단 처리되어 다른 로직 탈필요 없음
				result.setMessage(transService.getResultMsg());
				result.setCode(transService.getResultCode());
				
				return result;
			}
		}
		
		if( (paFapleGoods.getBrandId() != null && !paFapleGoods.getBrandId().isEmpty()) || isBrand) {
			
			if (isBrand ||
					"1".equals(paFapleGoods.getTransSaleYn()) && PaSaleGb.FORSALE.code().equals(paFapleGoods.getPaSaleGb()) 
					   															&& "0".equals(paFapleGoods.getBrandChangeYn())){
				
				// 판매재개
				transService = fapleProductService.resumeSaleProduct(goodsCode, paCode, procId);
				result.setCode(transService.getResultCode());
				if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
					result.setMessage("판매재개실패: " + transService.getResultMsg());
					return result;
				}
				if ("1".equals(transService.getSuccessYn())) {
					isResume = true;
					result.setMessage(
							(isBrand ? result.getMessage() + " " : "") + transService.getResultMsg());
				}
				
			}
			
			if ("1".equals(paFapleGoods.getTransTargetYn())) {
				// 상품수정
				ResponseMsg updated = updateProduct(goodsCode, paCode, procId, 0);
				if (String.valueOf(HttpStatus.OK.value()).equals(updated.getCode())) {
					isUpdated = true;
					result.setMessage(
							( isBrand || isResume ? result.getMessage() + " " : "") + "수정완료되었습니다.");
				} else {
					return updated;
				}
			}
			
			
			transService = fapleProductService.updateStockProduct(goodsCode, paCode, procId);
			if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
				result.setMessage(
						(isBrand || isUpdated || isResume? result.getMessage() + " " : "") + transService.getResultMsg());
				isStock = true;
			}
			// 옵션수정
			transService = fapleProductService.optionAddProduct(goodsCode, paCode, procId);
			if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) {
				result.setMessage(
						(isBrand || isStock || isUpdated ||isResume  ? result.getMessage() + " " : "") + transService.getResultMsg());
				isOption = true;
			}	
			

		}
		
		
		if (isStock || isUpdated || isResume || isBrand || isOption) { // 상품/재고/브랜드/옵션 변경된 경우
			result.setCode(String.valueOf(HttpStatus.OK.value()));
		} else {
			result.setCode(String.valueOf(HttpStatus.NO_CONTENT.value()));
			result.setMessage("변경된 사항이 없습니다.");
		}
		return result;
	}
	
	public ResponseMsg brandReRegisterProduct(String paCode, String procId) {
		
		// 패션플러스 브랜드 변경 재입점 대상 TPAFAPLEGOODSBRANDTARGET 테이블 삽입
		paFapleGoodsMapper.insertBrandReRegisterProduct();
		
		ResponseMsg result = new ResponseMsg("", "");
		
		// 브랜드 변경으로 인해 판매중단 된 상품 중 TPAFAPLEGOODSBRANDTARGET 테이블에 다시 입점 시키고 싶은 상품 데이터 입력 시 
		// 해당 상품들 새로운 브랜드로 등록되도록 or(배송비 a에서 b로 바꿔서 중단 된 상품인데 다시 a로 바꾼 경웅는 기존 브랜드로 등록된 상품 활성화)
		
		List<PaFapleGoodsVO> brandTarget =  paFapleGoodsMapper.selectPaFapleGoodsBrandTarget(paCode);
		
		for(PaFapleGoodsVO g : brandTarget) {
			try {
				// 상품동기화
				PaGoodsSync sync = syncProductService.syncProduct(g.getGoodsCode(), PaGroup.FAPLE.code(), procId);
				result = sync.getResponseMsg();
				if (String.valueOf(HttpStatus.NOT_FOUND.value()).equals(result.getCode())) { 
					continue;
				}
				
				PaTransService transService;
				
				transService = fapleProductService.brandReRegisterProduct(g.getGoodsCode(), paCode, procId);
				
				if(String.valueOf(HttpStatus.NOT_FOUND.value()).equals(transService.getResultCode())) {
					result.setMessage(transService.getResultMsg());
					continue;
				}else {
					if (String.valueOf(HttpStatus.OK.value()).equals(transService.getResultCode())) { 
						result.setMessage(transService.getResultMsg());
						
						// 판매재개 (변경하려는 브랜드로 등록된 상품 존재하여 해당 상품 업데이트 후 판매 재개)
						transService = fapleProductService.resumeSaleProduct(g.getGoodsCode(), paCode, procId);
						result.setCode(transService.getResultCode());
						if (!String.valueOf(HttpStatus.OK.value()).equals(result.getCode())) {
							result.setMessage("(브랜드 변경 반영 후)판매재개 실패: " + transService.getResultMsg());
							
						}
						
					}else if(String.valueOf(HttpStatus.CREATED.value()).equals(transService.getResultCode())) {
						result.setMessage( transService.getResultMsg());
						
					}
					
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("paCode", g.getPaCode());
					map.put("goodsCode", g.getGoodsCode());
					paFapleGoodsMapper.deleteFapleGoodsBrandTarget(map);
				}
			} catch (Exception e) {
				result.setCode("500");
				result.setMessage(e.getMessage());
			}
	       
		}
			
		return result;
	}
	

	
}

