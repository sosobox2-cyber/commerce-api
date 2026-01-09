package com.cware.netshopping.paintp.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

import com.cware.framework.common.exception.ProcessException;
import com.cware.framework.core.basic.AbstractProcess;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.common.util.ConfigUtil;
import com.cware.netshopping.common.util.DateUtil;
import com.cware.netshopping.domain.model.PaIntpClaimlist;
import com.cware.netshopping.domain.model.PaIntpOrderlist;
import com.cware.netshopping.domain.model.PaRequestMap;
import com.cware.netshopping.domain.paintp.xml.PaIntpCancelReqProductVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpCancelReqVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpClaimProductVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpClaimVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpDeliveryDetailVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpDeliveryVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpOrderProductVO;
import com.cware.netshopping.domain.paintp.xml.PaIntpOrderVO;

/**
 * Common method
 *
 * @version 1.0, 2005/02/03
 * @author kim Sungtaek <webzest@commerceware.co.kr>
 */
@Component("com.cware.netshopping.paintp.util.PaIntpComUtil")
public class PaIntpComUtil extends AbstractProcess {

	@Autowired
	private SystemService systemService;

	/*
	 * 인터파크 xml make
	 * 
	 * @param Document
	 * 
	 * @return String
	 */
	public String mapToXml(Map<String, String> xmlMap) throws TransformerException, ParserConfigurationException {
		String rtnVal;
		DocumentBuilder docBuilder;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		
		try {
			docBuilder = docFactory.newDocumentBuilder();
			// 루트 엘리먼트
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("result");
			doc.appendChild(rootElement);
			
			// item 엘리먼트
			Element item = doc.createElement("item");
			rootElement.appendChild(item);

			for (String key : xmlMap.keySet()) {

				if (!"apiCode".equals(key) || !"paCode".equals(key)) {
					Element node = doc.createElement(key);
					
					String val = xmlMap.get(key).replaceAll("(\r\n|\r|\n|\n\r)", " ");
					
					if (val.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
						node.appendChild(doc.createCDATASection(xmlMap.get(key)));
					} else {
						node.appendChild(doc.createTextNode(xmlMap.get(key)));
					}

					item.appendChild(node);
				}
			}

			// XML 파일로 쓰기
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();

			transformer.setOutputProperty(OutputKeys.ENCODING, "EUC_KR");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);

			// XML 문자열로 변환하기!
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			
			StreamResult result = new StreamResult(out);
			
			// 출력 시 문자코드: EUC-KR
			transformer.setOutputProperty(OutputKeys.ENCODING, "EUC-KR");
			// 들여 쓰기 있음
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);
			
			rtnVal =  new String(out.toByteArray(), Charset.forName("EUC-KR"));
			
		}catch (Exception e) {
			rtnVal = null;
			// TODO Auto-generated catch block
			log.error(e.getMessage());
		}

		return rtnVal;
	}

	/**
	 * 인터파크 Connection Setting(default)
	 * 
	 * @param apiUrl
	 * @param apiKey
	 * @param requestType
	 * @param parameter
	 * @return
	 * @throws Exception
	 */
	public ParamMap paIntpConnectionSetting(HashMap<String, String> apiInfo, String requestType, Map<String, String> param) throws Exception {
		
		HttpClient client = HttpClientBuilder.create().build(); // HttpClient 생성
		String[] apiKeys = null;
		apiKeys = apiInfo.get(apiInfo.get("paName")).split(";");
		String urlType = ConfigUtil.getString("PAINTP_GOODS_COM_BASE_URL");
		String citeKey = apiKeys[0];
		String secretKey = apiKeys[1];
		ParamMap returnMap = null;
		ParamMap pMap = null;
		StringBuilder sb = new StringBuilder(apiInfo.get("API_URL"));
		String sBody = "";
		RequestConfig requestConfig = null;
		final StopWatch watch = new StopWatch();
		requestConfig = RequestConfig.custom()
				.setSocketTimeout(30000)
				.setConnectTimeout(30000)
				.setConnectionRequestTimeout(30000).build();
		
		sb.append("&");
		if (param != null && param.size() > 0) {
			for (Map.Entry<String, String> entry : param.entrySet()) {
				if (!"dataUrl".equals(entry.getKey())) 
				{	
					sb.append(entry.getKey()).append("=").append(URLEncoder.encode(entry.getValue(), "EUC-KR")).append("&");
				} else {
					sb.append("dataUrl").append("=").append(URLEncoder.encode(ConfigUtil.getString("PAINTP_GOODS_XML_URL") + entry.getValue(), "EUC-KR")).append("&");
				}
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		String url = sb.toString();
		url = url.replace("{citeKey}", URLEncoder.encode(citeKey, "EUC-KR"));
		url = url.replace("{secretKey}", URLEncoder.encode(secretKey, "EUC-KR"));
		
		watch.start();
		HttpGet getRequest = new HttpGet(urlType + url); // GET 메소드 URL 생성
		getRequest.setConfig(requestConfig);
		HttpResponse response = client.execute(getRequest);
		watch.stop();
		try {

			if (response != null) {
				returnMap = new ParamMap();
				pMap = new ParamMap();
				HttpEntity entity = response.getEntity();
				InputStream body = entity.getContent();
				sBody = inputStreamtoStr(body);
				
				pMap.put("code", response.getStatusLine().getStatusCode());
				pMap.put("data", convertStringToXMLDocument(sBody));
			}
		} catch (Exception e) {
			sBody = String.format("HttpClient error: %s", e.getMessage()); 
		}
		finally {
			try {
				
				returnMap.put("paCode", apiInfo.get("paCode"));
				returnMap.put("apiCode", apiInfo.get("apiInfo"));
				returnMap.put("method", requestType);
				returnMap.put("url", url);
				returnMap.put("result", sBody);
				returnMap.put("body", ComUtil.NVL(apiInfo.get("body"), ""));
				returnMap.put("remark", String.format("%.6f", watch.getTotalTimeSeconds()));
				
				savePaRequestMap(returnMap);
				
			}
			catch ( Exception e ) {
				//ignore
				log.warn("TPAREQUESTMAP insert fail, " + e);
			}
		}
		return pMap;
	}

	public void savePaRequestMap(ParamMap paramMap) throws Exception {
		PaRequestMap paRequestMap = new PaRequestMap();
		paRequestMap.setPaCode(paramMap.getString("paCode"));
		paRequestMap.setReqApiCode(paramMap.getString("apiCode"));
		paRequestMap.setReqUrl("[" + paramMap.getString("method") + "]" + paramMap.getString("url"));
		paRequestMap.setReqHeader("{Content-Type=[text/xml]}");
		paRequestMap.setRequestMap(paramMap.getString("body"));
		paRequestMap.setResponseMap(paramMap.getString("result"));
		paRequestMap.setRemark(paramMap.getString("remark"));

		systemService.insertPaRequestMapTx(paRequestMap);
	}

	public String inputStreamtoStr(InputStream is) throws IOException {
		/*int i;
		StringBuffer buffer = new StringBuffer();
		byte[] b = new byte[4096];
		while ((i = is.read(b)) != -1) {
			buffer.append(new String(b, 0, i));
		}
		String str = buffer.toString();
*/
		BufferedReader rd = new BufferedReader(new InputStreamReader(is, "MS949"));
		
		String line;
		StringBuffer buffer = new StringBuffer(); 
		while((line = rd.readLine()) != null) {
			buffer.append(line);
			buffer.append('\r');
		}
		rd.close();
		String str = buffer.toString();
		
		return str;
	}

	public Document convertStringToXMLDocument(String xmlString) {
		// Parser that produces DOM object trees from XML content
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// API to obtain DOM Document instance
		DocumentBuilder builder = null;
		try {
			// Create DocumentBuilder with default configuration
			builder = factory.newDocumentBuilder();

			// Parse the content to Document object
			Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
			return doc;
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	/**
	 * object mapper (PaIntpOrderVO to PaIntpOrderlist)
	 * @param vo		PaIntpOrderVO
	 * @param prdIndex	PaIntpProductVO(상품정보) index of list
	 * @return
	 * @throws Exception 
	 */
	public PaIntpOrderlist convert(PaIntpOrderVO vo, int prdIndex) throws Exception {
		PaIntpOrderlist orderlist = new PaIntpOrderlist();
		if (vo != null) {
			if (prdIndex >= vo.getPrdList().size()) {
				return null;
			}
			PaIntpOrderProductVO product = vo.getPrdList().get(prdIndex);
			
			orderlist.setOrdNo(vo.getOrdNo());
			orderlist.setOrdSeq(product.getOrdSeq());
			orderlist.setOptPrdTp(product.getOptPrdTp());
			if ( StringUtils.hasText(vo.getOrderDts())) {
				orderlist.setOrderDts(DateUtil.toTimestamp(vo.getOrderDts(), DateUtil.DEFAULT_JAVA_DATE_FORMAT));
			}
			if ( StringUtils.hasText(vo.getPayDts())) {
				orderlist.setPayDt(DateUtil.toTimestamp(vo.getPayDts(), DateUtil.DEFAULT_JAVA_DATE_FORMAT));
			}
			orderlist.setOptParentSeq(product.getOptParentSeq());
			orderlist.setPrdNo(product.getPrdNo());
			orderlist.setOptPrdNo(product.getOptPrdNo());
			orderlist.setEntrPrdNo(product.getEntrPrdNo());
			orderlist.setOptNo(product.getOptNo());
			orderlist.setOptNm(product.getOptNm());
			orderlist.setSelOptNm(product.getSelOptNm());
			orderlist.setInOptNm(product.getInOptNm());
			orderlist.setSaleUnitcost(product.getSaleUnitcost());
			orderlist.setPreUseUnitcost(product.getPreUseUnitcost());
			orderlist.setPreUseAmt(product.getPreUseAmt());
			orderlist.setOrdQty(product.getOrdQty());
			orderlist.setOrdAmt(product.getOrdAmt());
			orderlist.setCurrentState(product.getCurrentState());
			if (StringUtils.hasText(product.getOrdclmStatDts())) {
				orderlist.setOrdclmStatDt(DateUtil.toTimestamp(product.getOrdclmStatDts(), DateUtil.DEFAULT_JAVA_DATE_FORMAT));
			}
			orderlist.setDcCouponAmt(product.getDcCouponAmt());
			orderlist.setEntrDcCouponAmt(product.getEntrDcCouponAmt());
			orderlist.setTotDcCouponAmt(product.getTotDcCouponAmt());
			orderlist.setEntrDisUnitCost(product.getEntrDisUnitCost());
			orderlist.setIpointDcUnitcost(product.getIpointDcUnitcost());
			orderlist.setRealSaleUnitcost(product.getRealSaleUnitcost());
			if (StringUtils.hasText(product.getIsCollected())) {
				orderlist.setIsCollected("Y".equalsIgnoreCase(product.getIsCollected()) ? "1": "0");	// Y/N-> 1/0
			}
			orderlist.setDelvsetlSeq(product.getDelvsetlSeq());
			orderlist.setOptPrdNo(product.getOptPrdNo());
			orderlist.setSupplyEntrNo(product.getSupplyEntrNo());
			orderlist.setSupplyCtrtSeq(product.getSupplyCtrtSeq());
			orderlist.setPrdNm(product.getPrdNm());
			if (StringUtils.hasText(product.getAbroadBsYn())) {
				orderlist.setAbroadBsYn("Y".equalsIgnoreCase(product.getAbroadBsYn()) ? "1" : "0");
			}
			
			PaIntpDeliveryDetailVO delvDetail = null;
			for (PaIntpDeliveryDetailVO detail : vo.getDeliveryDetailList()) {
				if (detail.getDelvsetlSeq().equals(product.getDelvsetlSeq())) {
					delvDetail = detail;
					break;
				}
			}
			orderlist.setDelvAmt(delvDetail.getDelvAmt());
			orderlist.setAddDelvAmt(delvDetail.getAddDelvAmt());
			orderlist.setDelvsetlSeq(delvDetail.getDelvsetlSeq());
			
			PaIntpDeliveryVO delv = null;
			for (PaIntpDeliveryVO deliInfo : vo.getDelivList()) {
				if (deliInfo.getSupplyCtrtSeq().equals(product.getSupplyCtrtSeq())) {
					delv = deliInfo;
					break;
				}
			}
			orderlist.setSupplyEntrNo(delv.getSupplyEntrNo());
			orderlist.setDelAmt(delv.getDelAmt());
			orderlist.setSupplyCtrtSeq(delv.getSupplyCtrtSeq());
			orderlist.setAddDelvAmt(delv.getAddDelAmt());
			orderlist.setInitialDelvAmt(delv.getInitialDelvAmt());
			
			orderlist.setOrdNm(vo.getOrdNm());
			orderlist.setMobileTel(vo.getMobileTel());
			orderlist.setTel(vo.getTel());
			orderlist.setEmail(vo.getEmail());
			orderlist.setRcvrNm(vo.getRcvrNm());
			orderlist.setDelvComment(vo.getDeliComment());
			orderlist.setResidentNo(vo.getResidentNo());
			orderlist.setDeliAddr1(vo.getDeliAddr1());
			orderlist.setDeliAddr1Doro(vo.getDeliAddr1Doro());
			orderlist.setDeliAddr2(vo.getDeliAddr2());
			orderlist.setDeliAddr2Doro(vo.getDeliAddr2Doro());
			orderlist.setOrderDt(DateUtil.toTimestamp(vo.getOrderDt(), DateUtil.GENERAL_DATE_FORMAT));
			orderlist.setDeliMobile(vo.getDeliMobile());
			orderlist.setMemId(vo.getMemId());
			orderlist.setDelZip(vo.getDelZip());
			orderlist.setDelZipDoro(vo.getDelZipDoro());
			orderlist.setPayRefMthdTp(vo.getPayRefMthdTp());
			orderlist.setOrdclmCrtTp(vo.getOrdclmCrtTp());
			orderlist.setDeliTel(vo.getDeliTel());
		}
		return orderlist;
	}
	
	/**
	 * XML object(PaIntpCancelReqProductVO) to VO(PaIntpOrderlist)
	 * @param vo		PaIntpCancelReqVO
	 * @param prdIndex	상품(PaIntpCancelReqProductVO) index
	 * @return			PaIntpOrderlist
	 */
	public PaIntpOrderlist convert(PaIntpCancelReqVO vo, int prdIndex) throws Exception{
		PaIntpOrderlist orderlist = new PaIntpOrderlist();
		
		if (vo != null) {
			if (prdIndex >= vo.getProductList().size()) {
				return null;
			}
			PaIntpCancelReqProductVO product = vo.getProductList().get(prdIndex);
			orderlist.setOrdNo(vo.getOrdNo());
			orderlist.setClmreqSeq(vo.getClmreqSeq());
			if (StringUtils.hasText(vo.getClmreqDts())) {
				orderlist.setClmreqDts(DateUtil.toTimestamp(vo.getClmreqDts(), DateUtil.DEFAULT_JAVA_DATE_FORMAT));
			}
			orderlist.setClmreqTpnm(vo.getClmreqTpnm());
			orderlist.setClmreqTp(vo.getClmreqTp());
			orderlist.setRtnMthdTpnm(vo.getReturnMthdTpnm());
			orderlist.setRtnMthdTp(vo.getReturnMthdTp());
			orderlist.setFrtnCpTp("Y".equalsIgnoreCase(vo.getFrtnCpTp()) ? "1" : "0");
			orderlist.setOrdSeq(product.getOrdSeq());
			orderlist.setEntrPrdNo(product.getEntrPrdNo());	//입력되지 않을 수 있음
			orderlist.setOptNo(product.getOptNo());		//입력되지 않을 수 있음
			orderlist.setOptPrdTp(product.getOptPrdTp());
			orderlist.setOptParentSeq(product.getOptParentSeq());
			orderlist.setPrdNo(product.getPrdNo());
			orderlist.setOptPrdNo(product.getOptPrdNo());
			orderlist.setClmreqStatnm(product.getClmreqStatnm());
			orderlist.setClmreqStat(product.getClmreqStat());
			orderlist.setClmreqQty(product.getClmreqQty());
			if (StringUtils.hasText(product.getClmreqCnclDts())) {
				orderlist.setClmreqCnclDts(DateUtil.toTimestamp(product.getClmreqCnclDts(), DateUtil.GENERAL_DATE_FORMAT));
			}
			orderlist.setClmreqRsnTpnm(product.getClmreqRsnTpnm());
			orderlist.setClmreqRsnTp(product.getClmreqRsnTp());
			orderlist.setClmreqRsnDtl(product.getClmreqRsnDtl());
			orderlist.setSupplyEntrNo(product.getSupplyEntrNo());
			orderlist.setSupplyCtrtSeq(product.getSupplyCtrtSeq());
			if (StringUtils.hasText(vo.getClmreqDt())) {
				orderlist.setClmreqDt(DateUtil.toTimestamp(vo.getClmreqDt(), DateUtil.GENERAL_DATE_FORMAT));
			}
		}
		return orderlist;
	}
	
	/**
	 * object mapper (PaIntpClaimVO to PaIntpClaimlist)
	 * @param vo		PaIntpClaimVO
	 * @param prdIndex	PaIntpClaimProductVO(상품정보) index of list
	 * @return
	 */
	public PaIntpClaimlist convert(PaIntpClaimVO vo, int prdIndex) {
		PaIntpClaimlist claimlist = new PaIntpClaimlist();
		if (vo != null) {
			if (prdIndex >= vo.getClaimPrdList().size()) {
				return null;
			}
			PaIntpClaimProductVO product = vo.getClaimPrdList().get(prdIndex);
			
			claimlist.setPaCode				(vo.getPaCode());
			claimlist.setPaOrderGb			(vo.getPaOrderGb());
			claimlist.setOrdNo				(vo.getOrdNo());
			claimlist.setClmNo				(vo.getClmNo());
			claimlist.setClmSeq				(vo.getClmSeq());
			claimlist.setClmCrtTpNm			(vo.getClmCrtTpNm());
			claimlist.setClmCrtTp			(vo.getClmCrtTp());
			claimlist.setClmRegTp			(vo.getClmRegTp());
			claimlist.setClmRcvrNm			(vo.getClmRcvrNm());
			claimlist.setClmRcvrTel			(vo.getClmRcvrTel());
			claimlist.setClmRcvrMobile		(vo.getClmRcvrMobile());
			claimlist.setClmRcvrZip			(vo.getClmRcvrZip());
			claimlist.setClmRcvrAddr1		(vo.getClmRcvrAddr1());
			claimlist.setClmRcvrAddr2		(vo.getClmRcvrAddr2());
			claimlist.setClmRcvrZipDoro		(vo.getClmRcvrZipDoro());
			claimlist.setClmRcvrAddr1Doro	(vo.getClmRcvrAddr1Doro());
			claimlist.setClmRcvrAddr2Doro	(vo.getClmRcvrAddr2Doro());
			claimlist.setRtnInvoNo			(vo.getRtnInvoNo());
			claimlist.setRtnDelvEntrNo		(vo.getRtnDelvEntrNo());
			claimlist.setRtnRespTp			(vo.getRtnRespTp());
			claimlist.setRtnRespTpNm		(vo.getRtnRespTpNm());			
			claimlist.setOrdSeq				(product.getOrdSeq());
			claimlist.setOptPrdTp			(product.getOptPrdTp());
			claimlist.setOptPrdNo			(product.getOptPrdNo());
			claimlist.setOptParentSeq		(product.getOptParentSeq());
			claimlist.setPrdNo				(product.getPrdNo());
			claimlist.setEntrPrdNo			(product.getEntrPrdNo());
			claimlist.setOptNo				(product.getOptNo());
			claimlist.setClmQty				(product.getClmQty());
			claimlist.setCurrentClmPrdStatNm(product.getCurrentClmPrdStatNm());
			claimlist.setCurrentClmPrdStat	(product.getCurrentClmPrdStat());			
			if ( StringUtils.hasText(product.getClmDt())) {
				try {
					claimlist.setClmDt			(DateUtil.toTimestamp(product.getClmDt(),	DateUtil.getFormatStringWithDate(product.getClmDt()) ));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			claimlist.setClmRsnTp			(product.getClmRsnTp());
			claimlist.setClmRsnTpNm			(product.getClmRsnTpNm());
			claimlist.setClmRsnDtl			(product.getClmRsnDtl());
			claimlist.setCostRespTp			(product.getCostRespTp());
			claimlist.setCostRespTpNm		(product.getCostRespTpNm());
			claimlist.setCostPayMthdTp		(product.getCostPayMthdTp());
			claimlist.setCostPayMthdTpNm	(product.getCostPayMthdTpNm());
			claimlist.setRtHdelvAmt			(vo.getRtHdelvAmt());
			claimlist.setIntialDelvAmt		(vo.getInitialDelvAmt());
		}
		return claimlist;
	}
	
	public PaIntpOrderlist convert2(PaIntpClaimVO vo, int prdIndex) {
		PaIntpOrderlist orderlist = new PaIntpOrderlist();

		if(vo == null) return orderlist;
		if (prdIndex >= vo.getClaimPrdList().size()) return null;
		PaIntpClaimProductVO product = vo.getClaimPrdList().get(prdIndex);
		
		
		orderlist.setPaOrderGb		("20");		
		orderlist.setOrdNo			(vo.getOrdNo());
		orderlist.setOrdSeq			(product.getOrdSeq());
		
		orderlist.setClmreqSeq		(vo.getClmNo());
		orderlist.setClmreqQty		(product.getClmQty());
		orderlist.setOrdSeq			(product.getOrdSeq());
		orderlist.setClmreqStat		("1");
		orderlist.setProcFlag		("10");
		orderlist.setClmreqRsnTp	(product.getClmRsnTp());
		orderlist.setSupplyEntrNo	(product.getSupplyEntrNo());
		orderlist.setSupplyCtrtSeq	(product.getSupplyCtrtSeq());
		orderlist.setOrdSeq			(product.getOrdSeq());
		orderlist.setEntrPrdNo		(product.getEntrPrdNo());	//입력되지 않을 수 있음
		orderlist.setOptNo			(product.getOptNo());		//입력되지 않을 수 있음
		orderlist.setOptPrdTp		(product.getOptPrdTp());
		orderlist.setOptParentSeq	(product.getOptParentSeq());
		orderlist.setPrdNo			(product.getPrdNo());
		orderlist.setOptPrdNo		(product.getOptPrdNo());
		orderlist.setFrtnCpTp		("Y".equalsIgnoreCase(vo.getFrtnCpTp()) ? "1" : "0");
		
		try {
			orderlist.setClmreqDt			(DateUtil.toTimestamp(product.getClmDt(),	"yyyyMMdd" ));
			orderlist.setClmreqDts			(DateUtil.toTimestamp(product.getClmDt(),	DateUtil.getFormatStringWithDate(product.getClmDt()) ));  //클레임요청일자(yyyyMMddHHmmss)
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		orderlist.setClmreqTpnm			(vo.getClmCrtTpNm());
		orderlist.setClmreqTp			("1");   		//클레임요청구분
		orderlist.setClmreqStatnm		("요청(직권)");  					// 상품별 클레임요청상태 - 요청, 요청철회
		orderlist.setClmreqRsnDtl		(product.getClmRsnTpNm());  // 클레임요청상세사유
		orderlist.setClmreqRsnTpnm		(product.getCurrentClmPrdStatNm());   //	클레임요청사유 - 고객변심, 배송지연, 이중주문, 재주문, 서비스 불만
		
		return orderlist;
	}
	
	/**
	 * 인터파크 주문/취소/CS API 호출, method = GET
	 * @param apiInfo		API 정보
	 * @param params		API 파라미터 정보
	 * @param responseType	response 타입 클래스
	 * @return				결과 Object
	 * @throws RestClientException
	 * @throws Exception
	 */
	public <T> T apiGetObjectByProgramId(HashMap<String, String> apiInfo, Map<String, ?> params, Class<T> responseType, String paCode) throws RestClientException, Exception {
		return apiGetObjectByUrl(apiInfo, params, responseType, paCode);
	}

	/**
	 * 인터파크 주문/취소/CS API 호출, method = GET
	 * @param apiInfo		API 정보
	 * @param url			API url
	 * @param params		API 파라미터 정보
	 * @param responseType	response 타입 클래스
	 * @return				결과 Object
	 * @throws RestClientException
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <T> T apiGetObjectByUrl(HashMap<String, String> apiInfo, final Map<String, ?> params, Class<T> responseType, String paCode) throws RestClientException, Exception {
		String url = apiInfo.get("API_URL").toString();
		if (!StringUtils.hasLength(url)) {
			throw new RestClientException("url is empty");
		}
		String path = url;
		Map<String, Object> queryParams = new HashMap<>(params);
		
		if (url.indexOf("?") >= 0) {
			final String[] splitUrl = url.split("\\?");
			path = splitUrl[0];
			final String[] urlParams = splitUrl[1].split("\\&");
			for (String urlParam: urlParams) {
				final String[] queryParam = urlParam.split("\\=");
				queryParams.put(queryParam[0], queryParam[1]);
			}
		}
		UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(ConfigUtil.getString(Constants.PA_INTP_COM_URL)).path(path);
		
		if ( params != null ) {
			for (Map.Entry<String, ?> entry : queryParams.entrySet()) {
				builder = builder.queryParam(entry.getKey(), entry.getValue());
			}
		}
		final URI targetUrl = builder.build().toUri();
		final StopWatch watch = new StopWatch();
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
		factory.setConnectTimeout	(30000); //타임아웃 설정 30초
		factory.setReadTimeout		(30000); //타임아웃 설정 30초
		
		final RestTemplate restTemplate = new RestTemplate(factory);
		byte[] body = null;
		String bodyString = "";
		T resultObj = null;

		try {
			watch.start();
			log.info("[인터파크API] API url:" + targetUrl.toString());
			body = restTemplate.getForObject(targetUrl, byte[].class);
			watch.stop();
			
			if (body != null && body.length > 0) {
				bodyString = new String(body, "euc-kr");
				for ( HttpMessageConverter converter : restTemplate.getMessageConverters()) {
					if (converter.canWrite(responseType, MediaType.TEXT_XML)) {
						HttpInputMessage input = new ClientHttpInputMessage(body);
						resultObj = (T) converter.read(responseType, input);
						break;
					}
				}
			}
		} 
		catch ( Exception e ) {
			bodyString = String.format("HttpClient error: %s", e.getMessage()); 
			throw new ProcessException(e.getMessage());
		}
		finally {
			try {
				String apiUrl = targetUrl.toString();
				PaRequestMap requestMap = new PaRequestMap();
				requestMap.setPaCode(paCode);
				requestMap.setReqApiCode(apiInfo.get("apiInfo").toString());
				requestMap.setReqUrl(String.format("[GET]%s", apiUrl.substring(0, apiUrl.indexOf("?"))));
				requestMap.setReqHeader("{Content-Type=[text/xml]}");
				requestMap.setRequestMap(apiUrl.substring(apiUrl.indexOf("?") + 1));
				requestMap.setResponseMap(bodyString);
				requestMap.setRemark(String.format("%.6f", watch.getTotalTimeSeconds()));
				
				systemService.insertPaRequestMapTx(requestMap);
			}
			catch ( Exception e ) {
				//ignore
				log.warn("TPAREQUESTMAP insert fail, " + e);
			}
		}
		
		return resultObj;
	}
	
	private static class ClientHttpInputMessage implements HttpInputMessage {
		
		private byte[] body;
		
		public ClientHttpInputMessage(byte[] body) {
			this.body = body;
		}
		
		@Override
		public HttpHeaders getHeaders() {
			return null;
		}
		
		@Override
		public InputStream getBody() throws IOException {
			return new ByteArrayInputStream(body);
		}
	}
} // end of class
