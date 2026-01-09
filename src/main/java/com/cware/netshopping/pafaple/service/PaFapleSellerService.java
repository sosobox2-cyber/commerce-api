package com.cware.netshopping.pafaple.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.code.PaGroup;
import com.cware.netshopping.common.code.TransType;
import com.cware.netshopping.common.log.domain.PaTransService;
import com.cware.netshopping.common.log.exception.TransApiException;
import com.cware.netshopping.common.log.service.TransLogService;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.pafaple.domain.Brand;
import com.cware.netshopping.pafaple.repository.PaFapleCommonMapper;
import com.cware.netshopping.pafaple.util.PaFapleApiRequest;
import com.cware.netshopping.pafaple.util.PaFapleConnectUtil;
import com.cware.netshopping.pafaple.domain.model.PaFapleShipCost;
import com.cware.netshopping.pafaple.domain.Addr;
import com.cware.netshopping.pafaple.domain.AddrVO;
import com.cware.netshopping.pafaple.domain.SBrand;
import com.cware.netshopping.pafaple.domain.SBrandVO;

@Service
public class PaFapleSellerService {
	@Autowired
	@Qualifier("common.system.systemService")
	private SystemService systemService;
	
	@Autowired
	TransLogService transLogService;
	
	@Autowired
	PaFapleConnectUtil paFapleConnectUtil;
		
	@Autowired
	PaFapleCommonMapper paFapleCommonMapper;
	
	@Autowired
	PaFapleApiRequest paFapleApiRequest;
	
	// 패플 디폴트 타겟 코드
	String defaultCode = "999999";
		
	private Logger log = LoggerFactory.getLogger(this.getClass());

	public ResponseMsg registerBrand(String paCode, String procId, long transBatchNo) {
		
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PAFAPLEAPI_00_001");
		serviceLog.setServiceNote("[API]패션플러스-실적 브랜드 조회");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.FAPLE.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 조회
		ResponseMsg result = callRegisterBrand(paCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	public ResponseMsg registerAddr(String paCode, String entpCode, String brandCode, String procId, long transBatchNo) {
		
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PAFAPLEAPI_00_002");
		serviceLog.setServiceNote("[API]패션플러스-배송처 등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.FAPLE.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송처 등록
		ResponseMsg result = callRegisterAddr(paCode, entpCode, brandCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	public ResponseMsg updateAddr(String paCode, String entpCode, String procId, long transBatchNo) {
		
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PAFAPLEAPI_00_004");
		serviceLog.setServiceNote("[API]패션플러스-배송처 수정");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.FAPLE.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 배송처 수정
		ResponseMsg result = callUpdateAddr(paCode, entpCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	public ResponseMsg directRegisterBrand(String paCode, String entpCode, String brandCode, String procId, long transBatchNo) {
		
		// 서비스로그 생성
		PaTransService serviceLog = new PaTransService();
		serviceLog.setTransCode(defaultCode);
		serviceLog.setTransType(TransType.SHIPCOST.name());
		serviceLog.setServiceName("IF_PAFAPLEAPI_00_003");
		serviceLog.setServiceNote("[API]패션플러스-실적 브랜드 등록");
		serviceLog.setTransBatchNo(transBatchNo);
		serviceLog.setPaGroupCode(PaGroup.FAPLE.code());
		serviceLog.setPaCode(paCode);
		serviceLog.setProcessId(procId);
		transLogService.logTransServiceStart(serviceLog);

		// 브랜드 등록
		ResponseMsg result = callDirectRegisterBrand(paCode, entpCode, brandCode, procId, serviceLog);

		serviceLog.setResultCode(result.getCode());
		serviceLog.setResultMsg(result.getMessage());
		transLogService.logTransServiceEnd(serviceLog);

		return result;
	}
	
	@SuppressWarnings("unchecked")
	private ResponseMsg callRegisterBrand(String paCode, String procId, PaTransService serviceLog) {
		ResponseMsg result = new ResponseMsg("", "");

		Brand brand = new Brand();
		String partnerLoginID = ""; 
		PaFapleShipCost paFapleShipCost = new PaFapleShipCost();
		try {
			log.info("===== 실적브랜드조회서비스 Start -=====");

			List<Map<String, Object>> brandUpdateTargetList= paFapleCommonMapper.selectbrandUpdateTargetList(paCode);
			if(brandUpdateTargetList.size()<1) {
				result.setCode("200");
				result.setMessage("실적브랜드 업데이트 대상 없음");
				return result;
			}
			
			ParamMap paramMap = new ParamMap();
			paramMap.put("paCode", paCode);
			paramMap.put("modifyId", procId);
			paramMap.put("apiCode", serviceLog.getServiceName());
			paramMap.put("paGroupCode", PaGroup.FAPLE.code());
			
			partnerLoginID = paFapleCommonMapper.selectFapleId(paramMap.get());

			// Body 세팅
			ParamMap apiDataObject = new ParamMap();
			brand.setBrandGrp(0);
			brand.setPartnerLoginID(partnerLoginID);
			
			apiDataObject.put("body", paFapleApiRequest.getBody(brand));

			
			Map<String, Object> productMap = new HashMap<String, Object>();	
			productMap = paFapleConnectUtil.callPaFapleAPI(paramMap, apiDataObject , serviceLog);

			if(productMap.get("Status").equals("OK")) {
				result.setCode("200");
				result.setMessage(PaFapleApiRequest.API_SUCCESS_CODE);
				List<Map<String, Object>> brandList = (List<Map<String, Object>>) productMap.get("BrandList");
				for (Map<String, Object> brandInfoMap : brandList) {
					if("568095".equals(brandInfoMap.get("BrandId").toString())) {//테스트서버기본 브랜드
						continue;
					}
					String[] fapleBrandName = brandInfoMap.get("BrandName").toString().split("_");
					if(fapleBrandName.length < 9) {
						continue;
					}
					
					for(Map<String, Object> brandUpdateTarget:brandUpdateTargetList) {
						if(brandUpdateTarget.get("SALE_BRAND_NAME").toString().equals(brandInfoMap.get("BrandName").toString())) {
							paFapleShipCost.setPaCode(fapleBrandName[1]);
							paFapleShipCost.setEntpCode(fapleBrandName[2]);
							paFapleShipCost.setBrandCode(fapleBrandName[3]);
							paFapleShipCost.setShipCostCode(fapleBrandName[4]);
							paFapleShipCost.setOrdCost(ComUtil.objToDouble(fapleBrandName[5]));
							paFapleShipCost.setReturnCost(ComUtil.objToDouble(fapleBrandName[6]));
							paFapleShipCost.setIslandCost(ComUtil.objToDouble(fapleBrandName[7]));
							paFapleShipCost.setIslandReturnCost(ComUtil.objToDouble(fapleBrandName[8]));
							paFapleShipCost.setBrandId(brandInfoMap.get("BrandId").toString());
							paFapleShipCost.setSenderId(Integer.parseInt(String.valueOf(brandInfoMap.get("SenderID"))));
							
							paFapleCommonMapper.updateFapleShipCost(paFapleShipCost);
						}
					}
				}
			   
			}
			
		} catch (TransApiException ex) {
			result.setCode(ex.getCode());
			result.setMessage(ex.getMessage());
		} catch (Exception e) {
			result.setCode("500");
			result.setMessage(e.getMessage());
			log.error(e.getMessage(), e);
		} finally {
			log.info("===== 실적브랜드조회서비스 End - =====");
		}

		return result;
	}
	
	
	private ResponseMsg callRegisterAddr(String paCode, String entpCode, String brandCode, String procId, PaTransService serviceLog) {
		
		ResponseMsg result = new ResponseMsg("", "");
		PaFapleShipCost paFapleShipCost = new PaFapleShipCost();
		
		log.info("===== 패션플러스 배송처 등록 Start -=====");
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("paCode", paCode);
		paramMap.put("entpCode", entpCode);
		paramMap.put("brandCode", brandCode);
		paramMap.put("modifyId", procId);
		paramMap.put("apiCode", serviceLog.getServiceName());
		paramMap.put("paGroupCode", PaGroup.FAPLE.code());
		
		List<AddrVO> AddrVOList= paFapleCommonMapper.selectAddrInsertTargetList(paramMap.get());
		
		for(AddrVO addrVO : AddrVOList) {
			try {
				if(addrVO == null) {
					result.setCode("200");
					result.setMessage("패션플러스 배송처 대상 없음");
					log.info("Status: 200");
					log.info("Message: 패션플러스 배송처 대상 없음");
					return result;
				}
				
				// Body
				ParamMap apiDataObject = new ParamMap();
				
				// addr 세팅	
				Addr add = new Addr();
				add.setSenderName((addrVO.getSenderName())); // SenderName 배송처명
				add.setFreeSalesPrice((addrVO.getFreeSalesPrice())); // FreeSalesPrice 무료배송 기준금액
				add.setExpressCode((addrVO.getExpressCode())); // ExpressCode 택배사코드
				add.setRetExpressCode((addrVO.getRetExpressCode())); // RetExpressCode 반품택배사코드
				add.setSenderAddr((addrVO.getSenderAddr())); // SenderAddr 배송처주소
				add.setReturnAddr1((addrVO.getReturnAddr1())); // Return_addr1 반품처주소
				add.setReturnAddr2((addrVO.getReturnAddr2())); // Return_addr2 반품처주소 상세
				add.setReturn_zipcode((addrVO.getReturn_zipcode())); // Return_Zipcode 반품처 우편번호
				add.setReturnt_tel((addrVO.getReturnt_tel())); // Return_Tel 반품처 연락처
				add.setSenderFee((addrVO.getSenderFee())); //SenderFee 일반 배송료
				add.setExtra_fee((addrVO.getExtra_fee())); // ExtraFee 도서 배송료(도서지역일때 부과하는 배송비 (추가배송비 개념 아님))
				add.setRetSenderFee(addrVO.getRetSenderFee()); // RetSenderFee 일반 반품배송료
				add.setRetExtraFee(addrVO.getRetExtraFee()); // RetExtraFee 도서 반품배송료(도서지역일때 부과하는 배송비 (추가배송비 개념 아님))
				
				apiDataObject.put("body", paFapleApiRequest.getBody(add)); 
				
				Map<String, Object> productMap = new HashMap<String, Object>();	
				productMap = paFapleConnectUtil.callPaFapleAPI(paramMap, apiDataObject , serviceLog);
				
				// 성공
				if(productMap.get("Status").equals("OK")) {
					log.info("Status:  "+ productMap.get("Status").toString());
					log.info("Message:  "+ productMap.get("Message").toString());
					
					result.setCode("200");
					result.setMessage(PaFapleApiRequest.API_SUCCESS_CODE);
					
					int senderId = Integer.parseInt(String.valueOf(productMap.get("SenderID"))) ;
										
					paFapleShipCost.setPaCode(addrVO.getPaCode());
					paFapleShipCost.setEntpCode(addrVO.getEntpCode());
					paFapleShipCost.setBrandCode(addrVO.getBrandCode());
					paFapleShipCost.setShipCostCode(addrVO.getShipCostCode());
					paFapleShipCost.setOrdCost(ComUtil.objToDouble(addrVO.getOrdCost()));
					paFapleShipCost.setReturnCost(ComUtil.objToDouble(addrVO.getReturnCost()));
					paFapleShipCost.setIslandCost(ComUtil.objToDouble(addrVO.getIslandCost()));
					paFapleShipCost.setIslandReturnCost(ComUtil.objToDouble(addrVO.getIslandReturnCost()));
					paFapleShipCost.setSenderId(senderId);
					
					paFapleCommonMapper.updateFapleSenderId(paFapleShipCost);
				}else { // 실패
					result.setCode("500");
					result.setMessage(PaFapleApiRequest.API_ERROR_CODE);
					log.info("Status:  "+ productMap.get("Status").toString());
					log.info("Message:  "+ productMap.get("Message").toString());
				}
				
			} catch (TransApiException ex) {
				result.setCode(ex.getCode());
				result.setMessage(ex.getMessage());
			} catch (Exception e) {
				result.setCode("500");
				result.setMessage(e.getMessage());
				log.error(e.getMessage(), e);
			}
		}

		log.info("===== 패션플러스 배송처 등록 End - =====");
		return result;
		
	}
	
	private ResponseMsg callUpdateAddr(String paCode, String entpCode, String procId, PaTransService serviceLog) {
		
		ResponseMsg result = new ResponseMsg("", "");
		
		log.info("===== 패션플러스 배송처 수정 Start -=====");
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("paCode", paCode);
		paramMap.put("entpCode", entpCode);
		paramMap.put("modifyId", procId);
		paramMap.put("apiCode", serviceLog.getServiceName());
		paramMap.put("paGroupCode", PaGroup.FAPLE.code());
		
		List<AddrVO> AddrVOList= paFapleCommonMapper.selectAddrUpdateTargetList(paramMap.get());
		
		for(AddrVO addrVO : AddrVOList) {
			try {
				if(addrVO == null) {
					result.setCode("200");
					result.setMessage("패션플러스 배송처 대상 없음");
					log.info("Status: 200");
					log.info("Message: 패션플러스 배송처 대상 없음");
					return result;
				}
				
				// Body
				ParamMap apiDataObject = new ParamMap();
				
				// addr 세팅	
				Addr add = new Addr();
				add.setSenderID((addrVO.getSenderID())); // SenderAddr 배송처주소
				add.setSenderAddr((addrVO.getSenderAddr())); // SenderAddr 배송처주소
				add.setReturnAddr1((addrVO.getReturnAddr1())); // Return_addr1 반품처주소
				add.setReturnAddr2((addrVO.getReturnAddr2())); // Return_addr2 반품처주소 상세
				add.setReturn_zipcode((addrVO.getReturn_zipcode())); // Return_Zipcode 반품처 우편번호
				add.setReturnt_tel((addrVO.getReturnt_tel())); // Return_Tel 반품처 연락처
				
				apiDataObject.put("body", paFapleApiRequest.getBody(add)); 
				
				Map<String, Object> productMap = new HashMap<String, Object>();	
				productMap = paFapleConnectUtil.callPaFapleAPI(paramMap, apiDataObject , serviceLog);
				
				// 성공
				if(productMap.get("Status").equals("OK")) {
					log.info("Status:  "+ productMap.get("Status").toString());
					log.info("Message:  "+ productMap.get("Message").toString());
					
					result.setCode("200");
					result.setMessage(PaFapleApiRequest.API_SUCCESS_CODE);
				}else { // 실패
					result.setCode("500");
					result.setMessage(PaFapleApiRequest.API_ERROR_CODE);
					log.info("Status:  "+ productMap.get("Status").toString());
					log.info("Message:  "+ productMap.get("Message").toString());
				}
				
			} catch (TransApiException ex) {
				result.setCode(ex.getCode());
				result.setMessage(ex.getMessage());
			} catch (Exception e) {
				result.setCode("500");
				result.setMessage(e.getMessage());
				log.error(e.getMessage(), e);
			}
		}

		log.info("===== 패션플러스 배송처 수정 End - =====");
		return result;
		
	}
	
	private ResponseMsg callDirectRegisterBrand(String paCode, String entpCode, String brandCode, String procId, PaTransService serviceLog) {
		
		ResponseMsg result = new ResponseMsg("", "");
		PaFapleShipCost paFapleShipCost = new PaFapleShipCost();
		
		log.info("===== 실적브랜드 등록 Start -=====");
		
		ParamMap paramMap = new ParamMap();
		paramMap.put("paCode", paCode);
		paramMap.put("entpCode", entpCode);
		paramMap.put("brandCode", brandCode);
		paramMap.put("modifyId", procId);
		paramMap.put("apiCode", serviceLog.getServiceName());
		paramMap.put("paGroupCode", PaGroup.FAPLE.code());
		
		List<SBrandVO> brandVOList= paFapleCommonMapper.selectbrandInsertTargetList(paramMap.get());
		
		for(SBrandVO brandVO : brandVOList) {
			try {
				if(brandVO == null) {
					result.setCode("200");
					result.setMessage("실적브랜드 등록 대상 없음");
					log.info("Status: 200");
					log.info("Message: 실적브랜드 등록 대상 없음");
					return result;
				}
				
				// Body
				ParamMap apiDataObject = new ParamMap();
				
				// brand 세팅
				SBrand brand = new SBrand();	
				brand.setSaleBrandName(brandVO.getSaleBrandName()); // sBrandname 실적브랜드명
				brand.setKindId(brandVO.getKindId()); // SBCategoryID 카테고리 ( API 코드 정의서 - > 실적카테고리) 
				brand.setDisBrandId(brandVO.getDisBrandId()); // DisBrandID 진열브랜드ID
				brand.setIsUse(brandVO.getIsUse()); // IsUse 사용여부(O/X)
				brand.setIsFpsrch(brandVO.getIsFpsrch()); // IsFpsrch  패플검색(O/X)
				brand.setStartDate(brandVO.getStartDate()); // StartDate 시작일자
				brand.setEndDate(brandVO.getEndDate()); // EndDate 	종료일자( '29991231' )
				brand.setSenderID(brandVO.getSenderID()); // 	SenderID 배송처ID
//				brand.setIsshowbrand(brandVO.getIsshowbrand()); // Isshowbrand 브랜드명노출(O/X)
//				brand.setIsSCM(brandVO.getIsSCM()); // IsSCM SCM 상품관리(O/X)
				brand.setIsusedItem(brandVO.getIsusedItem()); // Isused_item 중고상품 판매(O/X)
				brand.setProductCo(brandVO.getProductCo()); // Product_co 제조원
				
				apiDataObject.put("body", paFapleApiRequest.getBody(brand));
				
//				body Test
//				apiDataObject.put("body", "{\r\n" + 
//						"\"Isused_item\":\"X\",\r\n" + 
//						"\"sBrandname\":\"커터앤벅_E1_100001_001912_FR001\",\r\n" + 
//						"\"SBCategoryID\":1021,\r\n" + 
//						"\"DisBrandID\":55255,\r\n" + 
//						"\"IsUse\":\"O\",\r\n" + 
//						"\"IsFpsrch\":\"O\",\r\n" + 
//						"\"StartDate\":\"20250825\",\r\n" + 
//						"\"EndDate\":\"29991231\",\r\n" + 
//						"\"SenderID\":55008\r\n" + 
//						"}");
				
				Map<String, Object> productMap = new HashMap<String, Object>();	
				productMap = paFapleConnectUtil.callPaFapleAPI(paramMap, apiDataObject , serviceLog);
				
				// 성공
				if(productMap.get("Status").equals("OK")) {
					log.info("Status:  "+ productMap.get("Status").toString());
					log.info("Message:  "+ productMap.get("Message").toString());
					
					result.setCode("200");
					result.setMessage(PaFapleApiRequest.API_SUCCESS_CODE);
					
					String brandId = String.valueOf(productMap.get("brand_id")) ;
					
					paFapleShipCost.setPaCode(brandVO.getPaCode());
					paFapleShipCost.setEntpCode(brandVO.getEntpCode());
					paFapleShipCost.setBrandCode(brandVO.getBrandCode());
					paFapleShipCost.setShipCostCode(brandVO.getShipCostCode());
					paFapleShipCost.setOrdCost(ComUtil.objToDouble(brandVO.getOrdCost()));
					paFapleShipCost.setReturnCost(ComUtil.objToDouble(brandVO.getReturnCost()));
					paFapleShipCost.setIslandCost(ComUtil.objToDouble(brandVO.getIslandCost()));
					paFapleShipCost.setIslandReturnCost(ComUtil.objToDouble(brandVO.getIslandReturnCost()));
					paFapleShipCost.setBrandId(brandId);
					paFapleShipCost.setSenderId(brandVO.getSenderID());
					
					paFapleCommonMapper.updateFapleShipCost(paFapleShipCost);
				}else { // 실패
					result.setCode("500");
					result.setMessage(PaFapleApiRequest.API_ERROR_CODE);
					log.info("Status:  "+ productMap.get("Status").toString());
					log.info("Message:  "+ productMap.get("Message").toString());
				}
				
			} catch (TransApiException ex) {
				result.setCode(ex.getCode());
				result.setMessage(ex.getMessage());
			} catch (Exception e) {
				result.setCode("500");
				result.setMessage(e.getMessage());
				log.error(e.getMessage(), e);
			}
		}

		log.info("===== 실적브랜드 등록 End - =====");
		return result;
	}
	
}
