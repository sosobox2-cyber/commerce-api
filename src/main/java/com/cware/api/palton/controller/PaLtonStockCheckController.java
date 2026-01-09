package com.cware.api.palton.controller;

import java.util.Map;
import java.util.Objects;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cware.framework.core.basic.AbstractController;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.pacommon.common.service.PaCommonService;
import com.cware.netshopping.palton.stockcheck.service.PaLtonStockCheckService;

import com.cware.netshopping.domain.PaLtonGoodsdtMappingVO;
import com.cware.netshopping.domain.PaLtonStockResponseVO;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value="/palton/stock", description="재고 체크")
@Controller("com.cware.api.palton.PaLtonStockCheckController")
@RequestMapping(value="/palton/stock")
public class PaLtonStockCheckController  extends AbstractController{

	@Resource(name = "common.system.systemService")
    private SystemService systemService;
	
	@Resource(name = "pacommon.common.pacommonService")
	private PaCommonService pacommonService;
	
    @Resource(name = "palton.stockcheck.PaLtonStockCheckService")
	private PaLtonStockCheckService paLtonStockCheckService;
	
    
	/**
	 * 롯데온 실시간재고 체크 API
	 * @param requestJson
	 * @return PaLtonStockResponseVO
	 */
	@ApiOperation(value = "롯데온 실시간재고 체크 API", notes = "롯데온 실시간재고 체크 API", httpMethod = "POST", produces = "application/json")
	@ApiResponses(value = { 
			   @ApiResponse(code = 200, message = "SUCCESS"), 
			   @ApiResponse(code = 999, message = "시스템 오류가 발생하였습니다") 
			   })
	@RequestMapping(value = "/stock-check", method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public PaLtonStockResponseVO stockCheck(HttpServletRequest request, @RequestBody Map<String, Object> requestJson) throws Exception{
		
		log.info("===== 롯데온 재고 체크 API start =====");
		PaLtonGoodsdtMappingVO goodsDtStockSelect = null; // 조회용
		PaLtonGoodsdtMappingVO goodsDtStockParam = new PaLtonGoodsdtMappingVO(); // param 객체
		PaLtonStockResponseVO ltonStockResponse = new PaLtonStockResponseVO(); // response 객체
		
		String spdNo  = StringUtils.hasText((String)requestJson.get("spdNo")) ? String.valueOf(String.valueOf(requestJson.get("spdNo"))) : ""; 	// 롯데온 상품번호
		String sitmNo = StringUtils.hasText((String)requestJson.get("sitmNo")) ? String.valueOf(requestJson.get("sitmNo")) : ""; 						// 롯데온 단품번호
        int orderQty =Objects.isNull(requestJson.get("ordQty")) ? 0 : Integer.parseInt(requestJson.get("ordQty").toString());                          // 롯데온 주문수량
		
		try {
			goodsDtStockParam.setSpdNo(spdNo);
			goodsDtStockParam.setPaOptionCode(sitmNo);
			
			log.info("===== 롯데온 재고 체크 API 조회 =====");
			goodsDtStockSelect = paLtonStockCheckService.selectStockCheck(goodsDtStockParam);
			
			if(goodsDtStockSelect == null){
				// 조회되는 내역이 없을때
				ltonStockResponse.setResultCode("200"); 										// 결과코드
				ltonStockResponse.setResultMessage("해당상품이 존재하지 않습니다."); // 결과메세지
				ltonStockResponse.setSpdNo(spdNo);  											// 롯데온 상품번호
				ltonStockResponse.setSitmNo(sitmNo); 											// 롯데온 단품번호
				ltonStockResponse.setStkQty(0);  													// 재고수량
				ltonStockResponse.setOrderYn("N"); 												// 주문가능여부				
            }else if(orderQty ==0 ) {
                // 주문수량이 입력되지 않거나 0일 때
                ltonStockResponse.setResultCode("200");
                ltonStockResponse.setResultMessage("주문수량을 입력하세요.");
                ltonStockResponse.setSpdNo(spdNo);
                ltonStockResponse.setSitmNo(sitmNo);
                ltonStockResponse.setStkQty(Integer.parseInt(goodsDtStockSelect.getNewTransQty()));
                ltonStockResponse.setOrderYn("N");
			}else if(orderQty > Integer.parseInt(goodsDtStockSelect.getNewTransQty()) ) {
				// 재고보다 더 많은 주문수량이 인입되었을때
				ltonStockResponse.setResultCode("200");
				ltonStockResponse.setResultMessage("재고보다 더 많은 주문수량입니다.");
				ltonStockResponse.setSpdNo(spdNo);
				ltonStockResponse.setSitmNo(sitmNo);
				ltonStockResponse.setStkQty(Integer.parseInt(goodsDtStockSelect.getNewTransQty()));
				ltonStockResponse.setOrderYn("N");
			}else {
				// 정상재고일때
				ltonStockResponse.setResultCode("200");
				ltonStockResponse.setResultMessage("SUCCESS");
				ltonStockResponse.setSpdNo(spdNo);
				ltonStockResponse.setSitmNo(sitmNo);
				ltonStockResponse.setStkQty(Integer.parseInt(goodsDtStockSelect.getNewTransQty()));
				ltonStockResponse.setOrderYn("Y");
			};
		} catch (Exception e) {
			// 에러
			ltonStockResponse.setResultCode("999");
			ltonStockResponse.setResultMessage("시스템 오류가 발생하였습니다");
			ltonStockResponse.setSpdNo(spdNo);
			ltonStockResponse.setSitmNo(sitmNo);
			ltonStockResponse.setStkQty(0);
			ltonStockResponse.setOrderYn("N");
		}
		
		log.info("===== 롯데온 재고 체크 API End =====");
		
		return ltonStockResponse;
	}
	
}
