package com.cware.api.panaver.controller;

import java.security.Security;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.api.pacommon.message.pacommon.ResponseMsg;
import com.cware.api.panaver.product.type.AddressBookService;
import com.cware.api.panaver.product.type.AddressBookServicePortType;
import com.cware.api.panaver.product.type.CategoryListType;
import com.cware.api.panaver.product.type.CertificationInfoType;
import com.cware.api.panaver.product.type.GetAddressBookListRequestType;
import com.cware.api.panaver.product.type.GetAddressBookListResponseType;
import com.cware.api.panaver.product.type.GetAddressBookReturnType;
import com.cware.api.panaver.product.type.GetAllCategoryListRequestType;
import com.cware.api.panaver.product.type.GetAllCategoryListResponseType;
import com.cware.api.panaver.product.type.GetAllOriginAreaListRequestType;
import com.cware.api.panaver.product.type.GetAllOriginAreaListResponseType;
import com.cware.api.panaver.product.type.GetCategoryInfoRequestType;
import com.cware.api.panaver.product.type.GetCategoryInfoResponseType;
import com.cware.api.panaver.product.type.ProductService;
import com.cware.api.panaver.product.type.ProductServicePortType;
import com.cware.api.panaver.product.type.StringCodeType;
import com.cware.framework.core.basic.AbstractController;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaNaverOrigin;
import com.cware.netshopping.domain.model.Panaverentpaddressmoment;
import com.cware.netshopping.domain.model.Panavergoodskinds;
import com.cware.netshopping.domain.model.Panaverkindscerti;
import com.cware.netshopping.domain.model.Panaverkindscertikinds;
import com.cware.netshopping.domain.model.Panaverkindsexcept;
import com.cware.netshopping.pagmkt.util.PaGmktCommonUtil;
import com.cware.netshopping.panaver.common.service.PaNaverCommonService;


@Controller("com.cware.api.panaver.PaNaverCommonController")
@RequestMapping(value="/panaver/common")
public class PaNaverCommonController extends AbstractController{
	
	@Resource(name = "common.system.systemService")
    private SystemService systemService;
		
	@Resource(name = "panaver.common.paNaverCommonService")
	private PaNaverCommonService paNaverCommonService;

	@Resource(name = "com.cware.netshopping.pagmkt.util.PaGmktCommonUtil")
	private PaGmktCommonUtil CommonUtil;
	
	static { 
		Security.addProvider(new BouncyCastleProvider());
	}
	
	
	/**
	 * 원산지 조회, 매핑
	 * @return ResponseEntity
	 * @throws Exception
	 * 
	 */
	@RequestMapping(value = "/origin-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> originListNaver(HttpServletRequest request1) throws Exception{
		ParamMap paramMap = new ParamMap();
		
		Timestamp date = systemService.getSysdate();
		
		String apiCode = "IF_PANAVERAPI_00_001";
		String duplicateCheck = "";
		List<PaNaverOrigin> paNaverOriginList = new ArrayList<PaNaverOrigin>();
		PaNaverOrigin paNaverOrigin = null;	
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PANAVER");
		
		log.info("===== 네이버 원산지조회 API Start=====");
		
		try{
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) 원산지 정보 요청
			GetAllOriginAreaListRequestType request = new GetAllOriginAreaListRequestType();
			request.setRequestID(UUID.randomUUID().toString()); 
			request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
			request.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("ProductService", "GetAllOriginAreaList"));
			
			ProductService productService = new ProductService();
			ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
			GetAllOriginAreaListResponseType response = port.getAllOriginAreaList(request);
			
			if ("SUCCESS".equals(response.getResponseType())) {
				for(StringCodeType originArea : response.getOriginAreaList().get(0).getOriginArea()){
					paNaverOrigin = new PaNaverOrigin();
					paNaverOrigin.setOrignCode(originArea.getCode());
					paNaverOrigin.setOrignName(originArea.getName());
					paNaverOrigin.setInsertId("SYSTEM");
					paNaverOrigin.setInsertDate(date);
					
					paNaverOriginList.add(paNaverOrigin);
				}
			}
			
			paNaverCommonService.saveMappingPaNaverOriginTx(paNaverOriginList);
			
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
			
		} catch (Exception e) {
			CommonUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request1);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 원산지 조회 API END =====");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/goodskinds-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsKindsListNaver(HttpServletRequest request1) throws Exception{
		ParamMap paramMap = new ParamMap();
		
		String apiCode = "IF_PANAVERAPI_00_003";
		String duplicateCheck = "";
		List<Panavergoodskinds> paNaverGoodsKindsList = new ArrayList<Panavergoodskinds>();
		List<PaGoodsKinds> paGoodsKindsList = new ArrayList<PaGoodsKinds>(); 
		Panavergoodskinds paNaverGoodsKinds = null;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PANAVER");
		Timestamp date = systemService.getSysdate();
		
		try{
			//= 중복 실행 Check
			//log.info("01.API 중복실행검사");
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			//= Step 1) 원산지 정보 요청
			GetAllCategoryListRequestType request = new GetAllCategoryListRequestType();
			request.setRequestID(UUID.randomUUID().toString()); 
			request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
			
			request.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("ProductService", "GetAllCategoryList"));
	 		request.setLast("N");

			// ManageProduct 호출
			ProductService productService = new ProductService();
			ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
			GetAllCategoryListResponseType response = port.getAllCategoryList(request);		
			
			if ("SUCCESS".equals(response.getResponseType())){
				String lgroup = "";
				String lgroupName = "";
				String mgroup = "";
				String mgroupName = "";
				String sgroup = "";
				String sgroupName = "";
				String dgroup = "";
				String dgroupName = "";
				PaGoodsKinds paGoodsKinds = new PaGoodsKinds();
				
				for(CategoryListType category : response.getCategoryList().get(0).getCategory()){
					paNaverGoodsKinds = new Panavergoodskinds();
					paNaverGoodsKinds.setCategoryId(category.getId());
					paNaverGoodsKinds.setCategoryName(category.getName());
					paNaverGoodsKinds.setCategoryFullName(category.getCategoryName());
					paNaverGoodsKinds.setLast(category.getLast());
					paNaverGoodsKinds.setInsertId("SYSTEM");
					paNaverGoodsKinds.setInsertDate(date);
					
					paNaverGoodsKindsList.add(paNaverGoodsKinds);
					
					if(category.getLast().equals("N") ){
						if(category.getCategoryName().split(">").length == 1){//대분류
							lgroup = category.getId();
							lgroupName = category.getName();
						}else if(category.getCategoryName().split(">").length == 2 ){//중분류
							mgroup = category.getId();
							mgroupName = category.getName();
						}
						else if(category.getCategoryName().split(">").length == 3 ){//소분류
							sgroup = category.getId();
							sgroupName = category.getName();
						}
					}else{
						dgroup = "";
						dgroupName = "";
						if(category.getCategoryName().split(">").length == 3 ){//소분류
							sgroup = category.getId();
							sgroupName = category.getName();
						}else{
							dgroup = category.getId();
							dgroupName = category.getName();
						}
						
						paGoodsKinds = new PaGoodsKinds();
						
						paGoodsKinds.setPaGroupCode("04");
						paGoodsKinds.setPaLmsdKey(category.getId());
						
						paGoodsKinds.setPaLgroup(lgroup);
						paGoodsKinds.setPaMgroup(mgroup);
						paGoodsKinds.setPaSgroup(sgroup);
						paGoodsKinds.setPaDgroup(dgroup);
						paGoodsKinds.setPaLgroupName(lgroupName);
						paGoodsKinds.setPaMgroupName(mgroupName);
						paGoodsKinds.setPaSgroupName(sgroupName);
						paGoodsKinds.setPaDgroupName(dgroupName);
						paGoodsKinds.setUseYn("1");
						paGoodsKinds.setInsertDate(date);
						paGoodsKinds.setInsertId("SYSTEM");
						paGoodsKinds.setModifyDate(date);
						paGoodsKinds.setModifyId("SYSTEM");
						
						paGoodsKindsList.add(paGoodsKinds);
					}
				}
			}
			paNaverCommonService.savePaNaverGoodsKindsTx(paNaverGoodsKindsList,paGoodsKindsList);
				
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
			
		} catch (Exception e) {
			CommonUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);

		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request1);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 카테고리 조회 END =====");
		}
		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/goodskinds-info", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> goodsKindsInfoNaver(HttpServletRequest request1) throws Exception{
		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PANAVERAPI_00_002";
		String duplicateCheck = "";
		List<Panaverkindsexcept> paNaverKindsExceptList = new ArrayList<Panaverkindsexcept>();
		List<Panaverkindscerti> paNaverKindsCertiList = new ArrayList<Panaverkindscerti>();
		List<Panaverkindscertikinds> paNaverKindsCertiKindsList = new ArrayList<Panaverkindscertikinds>();
		List<Panavergoodskinds> paNaverGoodsKindsList = new ArrayList<Panavergoodskinds>();
		Panaverkindsexcept paNaverKindsExcept = null;
		Panaverkindscerti paNaverKindsCerti = null;
		Panaverkindscertikinds paNaverKindsCertiKinds = null;
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PANAVER");	
		Timestamp date = systemService.getSysdate();
		
		try{
			paNaverGoodsKindsList = paNaverCommonService.selectPaNaverCategoryInfo();
			
			for(Panavergoodskinds categoryId : paNaverGoodsKindsList){
				GetCategoryInfoRequestType request = new GetCategoryInfoRequestType();
				request.setRequestID(UUID.randomUUID().toString());
				request.setVersion(ConfigUtil.getString("PANAVER_SERVICE_VERSION"));
				request.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("ProductService", "GetCategoryInfo"));
				
				ProductService productService = new ProductService();
				ProductServicePortType port = productService.getProductServiceSOAP12PortHttp();
				request.setCategoryId(categoryId.getCategoryId());
				GetCategoryInfoResponseType response = port.getCategoryInfo(request);
				if("SUCCESS".equals(response.getResponseType())){
					if(response.getCategory().get(0).getExceptionalCategoryList().size() > 0){
						for(StringCodeType codeMap : response.getCategory().get(0).getExceptionalCategoryList().get(0).getModel()){
							paNaverKindsExcept = new Panaverkindsexcept();
							paNaverKindsExcept.setCategoryId(categoryId.getCategoryId());
							paNaverKindsExcept.setExceptCode(codeMap.getCode());
							paNaverKindsExcept.setExceptName(codeMap.getName());
							paNaverKindsExcept.setInsertId("SYSTEM");
							paNaverKindsExcept.setInsertDate(date);
						
							paNaverKindsExceptList.add(paNaverKindsExcept);
						}
					}
							
					for(CertificationInfoType certificationInfoType: response.getCategory().get(0).getCertificationInfoList().get(0).getCertificationInfo()){
						paNaverKindsCerti = new Panaverkindscerti();
						paNaverKindsCerti.setCategoryId(categoryId.getCategoryId());
						paNaverKindsCerti.setCertiCode(certificationInfoType.getCode());
						paNaverKindsCerti.setCertiName(certificationInfoType.getName());
						paNaverKindsCerti.setInsertId("SYSTEM");
						paNaverKindsCerti.setInsertDate(date);
					
						paNaverKindsCertiList.add(paNaverKindsCerti);

						for(int i=0; i<certificationInfoType.getKindTypeList().size(); i++){
							paNaverKindsCertiKinds = new Panaverkindscertikinds();
							paNaverKindsCertiKinds.setCategoryId(categoryId.getCategoryId());
							paNaverKindsCertiKinds.setCertiCode(certificationInfoType.getCode());
							paNaverKindsCertiKinds.setCertiKindCode(certificationInfoType.getKindTypeList().get(i).getKindType().get(0).getCode());
							paNaverKindsCertiKinds.setCertiKindName(certificationInfoType.getKindTypeList().get(i).getKindType().get(0).getName());
							paNaverKindsCertiKinds.setInsertId("SYSTEM");
							paNaverKindsCertiKinds.setInsertDate(date);
						
							paNaverKindsCertiKindsList.add(paNaverKindsCertiKinds);
						}
					}
				}
			}
			
			paNaverCommonService.savePaNaverCategoryInfoTx(paNaverKindsExceptList, paNaverGoodsKindsList, paNaverKindsCertiList, paNaverKindsCertiKindsList);
				
			paramMap.put("code", "200");
			paramMap.put("message", "OK");
		} catch (Exception e) {
			CommonUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally {
			try{
				CommonUtil.dealSuccess(paramMap, request1);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}			
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("카테고리별 상세 정보 테이블 저장 완료");
		}		
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/entp-address-list", method = RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<?> addressBooksList(HttpServletRequest httpServletRequest,
			@RequestParam(value="page", required=false, defaultValue = "1") int page) throws Exception{
		ParamMap paramMap = new ParamMap();
		String apiCode = "IF_PANAVERAPI_00_004";
		String duplicateCheck = "";
		String rtnMsg = Constants.SAVE_SUCCESS;
		
		List<Panaverentpaddressmoment> paNaverEntpAddressList = new ArrayList<Panaverentpaddressmoment>(); 
		Panaverentpaddressmoment panaverentpaddressmoment = null; 
		
		paramMap.put("apiCode", apiCode);
		paramMap.put("startDate", systemService.getSysdatetimeToString());
		paramMap.put("siteGb", "PANAVER");
		boolean isRetrieve = false;
		
		log.info("===== 주소록 조회 API Start =====");
		
		try{
			duplicateCheck = systemService.checkCloseHistoryTx("start", apiCode);
			if(duplicateCheck.equals("1")) throw processException("msg.batch_process_duplicated", new String[] {apiCode});
			
			GetAddressBookListRequestType request = new GetAddressBookListRequestType();
			request.setPage(page);
			request.setRequestID(UUID.randomUUID().toString());
			request.setVersion(ConfigUtil.getString("PANAVER_GOODS_VERSION"));
			request.setSellerId(ConfigUtil.getString("PANAVER_MALL_ID"));
			request.setAccessCredentials(ComUtil.paNaverCreateAccessCredentials("AddressBookService", "GetAddressBookList"));
			
			AddressBookService addressBookService = new AddressBookService();
			AddressBookServicePortType port = addressBookService.getAddressBookServiceSOAP12PortHttp();
			GetAddressBookListResponseType response = port.getAddressBookList(request);
			
			if("SUCCESS".equals(response.getResponseType())){
				for(GetAddressBookReturnType addressList : response.getAddressBookList().get(0).getaddressBook()){
					panaverentpaddressmoment = new Panaverentpaddressmoment();
					panaverentpaddressmoment.setAddressId(addressList.getAddressId());
					panaverentpaddressmoment.setName(addressList.getName());
					panaverentpaddressmoment.setAddressType(addressList.getAddressType());
					panaverentpaddressmoment.setPostalCode(addressList.getPostalCode());
					panaverentpaddressmoment.setBaseAddress(addressList.getBaseAddress());
					panaverentpaddressmoment.setDetailAddress(addressList.getDetailAddress());
					panaverentpaddressmoment.setFullAddress(addressList.getFullAddress());
					panaverentpaddressmoment.setPhoneNumber1(addressList.getPhoneNumber1());
					panaverentpaddressmoment.setPhoneNumber2(addressList.getPhoneNumber2());
					panaverentpaddressmoment.setHasLocation(addressList.getHasLocation());
					panaverentpaddressmoment.setRoadNameAddress(addressList.getRoadNameAddress());
					panaverentpaddressmoment.setOverseasAddress(addressList.getOverseasAddress());

					paNaverEntpAddressList.add(panaverentpaddressmoment);
				}
				rtnMsg = paNaverCommonService.savePaNaverAddressListTx(paNaverEntpAddressList);

				if(Constants.SAVE_SUCCESS.equals(rtnMsg)){
					paramMap.put("code", "200");
					paramMap.put("message","OK");
				}else{
					paramMap.put("code", "500");
					paramMap.put("message",rtnMsg);
					return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
				}

				if(response.getAddressBookList().get(0).getaddressBook().size() >= 100){
					page += 1;
					isRetrieve = true;
				}
			}else{
				paramMap.put("code","500");
				paramMap.put("message", getMessage("errors.exist",new String[] { "orderConfirmList" }));
				return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
			}
		}catch(Exception e){
			CommonUtil.dealException(e, paramMap);
			log.error(paramMap.getString("message"), e);
			return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.INTERNAL_SERVER_ERROR);
		}finally{
			try{
				CommonUtil.dealSuccess(paramMap, httpServletRequest);
			}catch(Exception e){
				log.error("ApiTracking Insert Error : "+e.getMessage());
			}
			if(duplicateCheck.equals("0")) systemService.checkCloseHistoryTx("end", paramMap.getString("apiCode"));
			log.info("===== 주소록 조회 API End =====");
			if(isRetrieve) addressBooksList(httpServletRequest, page);
		}
		return new ResponseEntity<ResponseMsg>(new ResponseMsg(paramMap.getString("code"), paramMap.getString("message")), HttpStatus.OK);
	}
}
	
	
	

