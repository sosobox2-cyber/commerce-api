package com.cware.netshopping.pa11st.goods.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.domain.Pa11stGoodsPriceVO;
import com.cware.netshopping.domain.Pa11stGoodsVO;
import com.cware.netshopping.domain.Pa11stGoodsdtMappingVO;
import com.cware.netshopping.domain.PaGmktGoodsPriceVO;
import com.cware.netshopping.domain.PaGmktGoodsVO;
import com.cware.netshopping.domain.PaGoodsPriceVO;
import com.cware.netshopping.domain.model.Pa11stPolicy;
import com.cware.netshopping.domain.model.PaEntpSlip;
import com.cware.netshopping.domain.model.PaGoodsAuthYnLog;
import com.cware.netshopping.domain.model.PaGoodsOffer;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaPromoTarget;
import com.cware.netshopping.domain.model.PaSaleNoGoods;

@Service("pa11st.goods.pa11stGoodsDAO")
public class Pa11stGoodsDAO extends AbstractPaDAO{
	
	/**
	 * 11번가 상품등록 - 상품출고지 조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	public PaEntpSlip selectPa11stEntpSlip(ParamMap paramMap) throws Exception{
		paramMap.put("paAddrGb", "30");//출고
		return (PaEntpSlip)selectByPk("pa11st.goods.selectPa11stEntpSlip", paramMap.get());
	}
	
	/**
	 * 11번가 상품등록 - 상품회수지 조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	public PaEntpSlip selectPa11stReturnSlip(ParamMap paramMap) throws Exception{
		paramMap.put("paAddrGb", "20");//회수
		return (PaEntpSlip)selectByPk("pa11st.goods.selectPa11stEntpSlip", paramMap.get());
	}
	
	/**
	 * 11번가 상품등록 - 상품정보 조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	public Pa11stGoodsVO selectPa11stGoodsInfo(ParamMap paramMap) throws Exception{
		return (Pa11stGoodsVO)selectByPk("pa11st.goods.selectPa11stGoodsInfo", paramMap.get());
	}
	
	
	/**
	 * 11번가 상품등록 - 상품 단품정보 조회
	 * @param paramMap
	 * @returnList<PaGoodsdtMapping>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsdtMapping> selectPa11stGoodsdtInfoList(ParamMap paramMap) throws Exception{
		return list("pa11st.goods.selectPa11stGoodsdtInfoList", paramMap.get());
	}
	
	/**
	 * 11번가 상품등록 - 상품정보고시조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsOffer> selectPa11stGoodsOfferList(ParamMap paramMap) throws Exception{
		return list("pa11st.goods.selectPa11stGoodsOfferList", paramMap.get());
	}
	
	/**
	 * 11번가 상품등록 - 11번가 상품정보 저장
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePa11stGoods(Pa11stGoodsVO pa11stGoods) throws Exception{
		return update("pa11st.goods.updatePa11stGoods", pa11stGoods);
	}
	
	/**
	 * 11번가 상품등록 - 11번가 상품정보 저장
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePa11stGoodsdt(PaGoodsdtMapping paGoodsdtMapping) throws Exception{
		return update("pa11st.goods.updatePa11stGoodsdt", paGoodsdtMapping);
	}
	
	/**
	 * 11번가 상품등록 - 11번가 상품정보 저장
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePa11stGoodsdtQty(Pa11stGoodsdtMappingVO paGoodsdtMappingVO) throws Exception{
		return update("pa11st.goods.updatePa11stGoodsdtQty", paGoodsdtMappingVO);
	}
	/**
	 * 11번가 상품등록 - 11번가 상품정보 저장
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePa11stGoodsImage(Pa11stGoodsVO pa11stGoods) throws Exception{
		return update("pa11st.goods.updatePa11stGoodsImage", pa11stGoods);
	}
	
	/**
	 * 11번가 상품등록 - 11번가 상품정보 저장
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePa11stGoodsPrice(Pa11stGoodsVO pa11stGoods) throws Exception{
		return update("pa11st.goods.updatePa11stGoodsPrice", pa11stGoods);
	}
	
	/**
	 * 11번가 상품등록 - 11번가 상품정보 저장
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePa11stCustShipCost(Pa11stGoodsVO pa11stGoods) throws Exception{
		return update("pa11st.goods.updatePa11stCustShipCost", pa11stGoods);
	}
	
	/**
	 * 11번가 상품등록 - 11번가 상품정보 저장
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePa11stGoodsOffer(Pa11stGoodsVO pa11stGoods) throws Exception{
		return update("pa11st.goods.updatePa11stGoodsOffer", pa11stGoods);
	}
	
	
	/**
	 * 11번가 상품등록 - 상품출고지 조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaEntpSlip> selectPa11stEntpSlipList(ParamMap paramMap) throws Exception{
		paramMap.put("paAddrGb", "30");//출고
		return list("pa11st.goods.selectPa11stEntpSlip", paramMap.get());
	}
	
	/**
	 * 11번가 상품등록 - 상품회수지 조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaEntpSlip> selectPa11stReturnSlipList(ParamMap paramMap) throws Exception{
		paramMap.put("paAddrGb", "20");//회수
		return list("pa11st.goods.selectPa11stEntpSlip", paramMap.get());
	}
	
	/**
	 * 11번가 상품등록 - 상품수정대상list조회
	 * @param paramMap
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsVO> selectPa11stGoodsInfoList(ParamMap paramMap) throws Exception{
		return list("pa11st.goods.selectPa11stGoodsInfo", paramMap.get());
	}
	
	/**
	 * 11번가 상품가격 수정 - 수정대상조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaGoodsPriceVO> selectPa11stGoodsPriceList(ParamMap paramMap) throws Exception{
		return list("pa11st.goods.selectPa11stGoodsPrice", paramMap.get());
	}

	/**
	 * 11번가 상품옵션 수정 - 수정대상조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsdtMappingVO> selectPa11stGoodsdtMappingList(ParamMap paramMap) throws Exception{
		return list("pa11st.goods.selectPa11stGoodsdtMappingList", paramMap.get());
	}

	/**
	 * 11번가 상품상세설명 수정 - 상품상세설명 수정list조회
	 * @param ParamMap
	 * @return ParamMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectPa11stGoodsDescribe(ParamMap paramMap) throws Exception{
		return (HashMap<String, String>) selectByPk("pa11st.goods.selectPa11stGoodsDescribe",paramMap.get());
	}

	/**
	 * 11번가 상품재고번호저장
	 * @param List<Pa11stGoodsdtMappingVO>
	 * @return
	 * @throws Exception
	 */
	public int updatePa11stGoodsdtMappingPaOptionCode(Pa11stGoodsdtMappingVO paGoodsdtMapping) throws Exception{
		return update("pa11st.goods.updatePa11stGoodsdtMappingPaOptionCode", paGoodsdtMapping);
	}
	
	/**
	 * 11번가 판매중지 - 11번가상품코드조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	public Pa11stGoodsVO selectPa11stGoodsProductNo(ParamMap paramMap) throws Exception{
		return (Pa11stGoodsVO)selectByPk("pa11st.goods.selectPa11stGoodsProductNo", paramMap.get());
	}
	
	/**
	 * 11번가 상품재고수정 - 상품재고 수정list조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsdtMappingVO> selectPa11stGoodsdtMappingStockList(ParamMap paramMap) throws Exception{
		return list("pa11st.goods.selectPa11stGoodsdtMappingStockList", paramMap.get());
	}		

	/**
	 * 11번가 재고동기화 - 상품재고 조회 대상 조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsdtMappingVO> selectPa11stGoodsDtStockList() throws Exception{
		return list("pa11st.goods.selectPa11stGoodsDtStockList", null);
	}
	
	/**
	 * 11번가 재고동기화 - 품절조회(판매중지처리대상)
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsVO> selectedSoldOutPa11stGoodsList() throws Exception{
		return list("pa11st.goods.selectedSoldOutPa11stGoodsList", null);
	}		
	public int updateSoldOutTransSaleYn(Pa11stGoodsVO Pa11stGoods) throws Exception{
		return update("pa11st.goods.updateSoldOutTransSaleYn", Pa11stGoods);
	}		
	
	/**
	 * 11번가 재고동기화 - 판매중지처리 후 재고 update
	 * @param Pa11stGoodsVO
	 * @return
	 * @throws Exception
	 */
	public int updatePa11stGoodsdtMappingQty(Pa11stGoodsVO pa11stGoods) throws Exception{
		return update("pa11st.goods.updatePa11stGoodsdtMappingQty", pa11stGoods);
	}
	
	public int execSP_PAGOODS_SYNC_PROC(HashMap<String,Object> resultMap) throws Exception {
		return update("pa11st.goods.execSP_PAGOODS_SYNC_PROC", resultMap);
	}

	/**
	 * 11번가 판매상태 폐기, 판매중지상품 조회 -판매중지처리대상
	 * @param ParamMap
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsVO> selectPa11stGoodsSaleStopList(ParamMap paramMap) throws Exception{
		return list("pa11st.goods.selectPa11stGoodsSaleStopList", paramMap.get());
	}		
	
	/**
	 * 11번가 판매중지해제처리대상
	 * @param ParamMap
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsVO> selectPa11stGoodsSaleRestartList(ParamMap paramMap) throws Exception{
		return list("pa11st.goods.selectPa11stGoodsSaleRestartList", paramMap.get());
	}	
	
	public int updatePaGoodsTarget(Pa11stGoodsVO pa11stGoods) throws Exception{
		return update("pa11st.goods.updatePaGoodsTarget", pa11stGoods);
	}
	
	public int updatePa11stGoodsFail(Pa11stGoodsVO pa11stGoods) throws Exception{
		return update("pa11st.goods.updatePa11stGoodsFail", pa11stGoods);
	}
	//상품 중지, 해제용도의 transTargetYn을 바꾸어줌
	public int updateTransSaleYn(ParamMap paramMap) throws Exception{
		return update("pa11st.goods.updateTransSaleYn",paramMap.get());
	}
	public String selectPa11stGoodsDesc(String goodsCode) throws Exception{
		return (String)selectByPk("pa11st.goods.selectPa11stGoodsDesc",goodsCode);
	}
	
	public int selectPa11stGoodsDescCnt998(ParamMap paramMap) throws Exception{
		return (int)selectByPk("pa11st.goods.selectPa11stGoodsDescCnt998",paramMap.get());
	}
	public int selectPa11stGoodsDescCnt999(ParamMap paramMap) throws Exception{
		return (int)selectByPk("pa11st.goods.selectPa11stGoodsDescCnt999",paramMap.get());
	}
	public String selectPa11stGoodsDesc998FR(ParamMap paramMap) throws Exception{
		return (String)selectByPk("pa11st.goods.selectPa11stGoodsDesc998FR",paramMap.get());
	}
	public String selectPa11stGoodsDesc998TO(ParamMap paramMap) throws Exception{
		return (String)selectByPk("pa11st.goods.selectPa11stGoodsDesc998TO",paramMap.get());
	}
	public String selectPa11stGoodsDesc999FR(ParamMap paramMap) throws Exception{
		return (String)selectByPk("pa11st.goods.selectPa11stGoodsDesc999FR",paramMap.get());
	}
	public String selectPa11stGoodsDesc999TO(ParamMap paramMap) throws Exception{
		return (String)selectByPk("pa11st.goods.selectPa11stGoodsDesc999TO",paramMap.get());
	}
	public int selectPa11stGoodsShipCnt(String goodsCode) throws Exception{
		return (int)selectByPk("pa11st.goods.selectPa11stGoodsShipCnt",goodsCode);
	}
	
	public int insertPaSaleNoGoods(PaSaleNoGoods paSaleNoGoods) throws Exception {
		return insert("pa11st.goods.insertPaSaleNoGoods", paSaleNoGoods);
	}
	public int insertStopMonitering(Pa11stGoodsVO pa11stGoods) throws Exception {
		return insert("pa11st.goods.insertStopMonitering", pa11stGoods);
	}
	public int insertRestartMonitering(Pa11stGoodsVO pa11stGoods) throws Exception {
		return insert("pa11st.goods.insertRestartMonitering", pa11stGoods);
	}
	public int updateShipCostTransSaleYn(Pa11stGoodsVO Pa11stGoods) throws Exception{
		return update("pa11st.goods.updateShipCostTransSaleYn", Pa11stGoods);
	}
	/**
	 * 11번가 상품옵션 수정 -  단품매핑불가 주문list조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsdtMappingVO> selectPa11stOrderMappingList(ParamMap paramMap) throws Exception{
		return list("pa11st.goods.selectPa11stOrderMappingList", paramMap.get());
	}
	
	/**
	 * 11번가 상품옵션 수정 -  단품매핑불가 상품재고번호등록전삭제
	 * @param 
	 * @return Integer
	 * @throws Exception
	 */
	public int deleteGoodsdtMappingOrder(Pa11stGoodsdtMappingVO paGoodsdtMapping) throws Exception{
		return delete("pa11st.goods.deleteGoodsdtMappingOrder", paGoodsdtMapping);
	}
	
	public int updateTpaMonitering4WriteInfo(Pa11stGoodsdtMappingVO paGoodsdtMapping) throws Exception{
		return delete("pa11st.goods.updateTpaMonitering4WriteInfo", paGoodsdtMapping);
	}
	
	
	
	/**
	 * 11번가 상품옵션 수정 -  단품매핑불가 상품재고번호등록
	 * @param 
	 * @return Integer
	 * @throws Exception
	 */
	public int insertGoodsdtMappingOrder(Pa11stGoodsdtMappingVO paGoodsdtMapping) throws Exception {
		return insert("pa11st.goods.insertGoodsdtMappingOrder", paGoodsdtMapping);
	}
	
	public int insertCnCostMonitering(Pa11stGoodsVO pa11stGoods) throws Exception {
		return insert("pa11st.goods.insertCnCostMonitering", pa11stGoods);
	}
	
	public int updateCnShipCostByMonitering(Pa11stGoodsVO pa11stGoods) throws Exception {
		return insert("pa11st.goods.updateCnShipCostByMonitering", pa11stGoods);
	}
	
	public int updatePaGoodsModifyFail(Pa11stGoodsVO pa11stGoods) throws Exception {
		return insert("pa11st.goods.updatePaGoodsModifyFail", pa11stGoods);
	}
	
	public List<HashMap<String,Object>> selectPaPromoTarget(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pa11st.goods.selectPaPromoTarget", paramMap.get());
	}
	public int updatePaPromoTargetCalc(PaPromoTarget paPromoTarget) throws Exception{
		return update("pa11st.goods.updatePaPromoTargetCalc", paPromoTarget);
	}
	public int updatePaPromoTarget(Pa11stGoodsVO pa11stGoods) throws Exception{
		return update("pa11st.goods.updatePaPromoTarget", pa11stGoods);
	}
	public Pa11stGoodsPriceVO selectPa11stGoodsPriceOne(Pa11stGoodsVO pa11stGoods) throws Exception{
		return (Pa11stGoodsPriceVO) selectByPk("pa11st.goods.selectPa11stGoodsPriceOne", pa11stGoods);
	}
	public int insertPa11stGoodsPrice(Pa11stGoodsPriceVO pa11stGoodsPriceVO) throws Exception{
		return insert("pa11st.goods.insertPa11stGoodsPrice", pa11stGoodsPriceVO);
	}
	public int updatePa11stGoodsPriceForPromo(Pa11stGoodsPriceVO pa11stGoodsPriceVO) throws Exception{
	    return update("pa11st.goods.updatePa11stGoodsPriceForPromo", pa11stGoodsPriceVO);
	}
	/**
	 * 11번가 발송 예정일 관련 예외 대상 업체 조회
	 * @param String
	 * @return HashMap<String,String>
	 * @throws Exception
	 */
	public HashMap<String, String> selectExceptEntpFor11stPolicy(String entpCode) throws Exception{
		return (HashMap<String,String>) selectByPk("pa11st.goods.selectExceptEntpFor11stPolicy",entpCode);
	}
	
	/**
	 * 11번가 발송 예정일 관련 예외 대상 카테고리 조회
	 * @param String
	 * @return HashMap<String,String>
	 * @throws Exception
	 */
	public HashMap<String, String> selectExceptCategoryFor11stPolicy(String lmsdCode) throws Exception{
		return (HashMap<String,String>) selectByPk("pa11st.goods.selectExceptCategoryFor11stPolicy",lmsdCode);
	}

	/**
	 * 11번가 발송 예정일 템플릿 조회
	 * @param Pa11stPolicy
	 * @return HashMap<String,String>
	 * @throws Exception
	 */
	public HashMap<String, String> selectPolicyFor11stPolicy(Pa11stPolicy pa11stPolicy) throws Exception{
		return (HashMap<String,String>) selectByPk("pa11st.goods.selectPolicyFor11stPolicy",pa11stPolicy);
	}
	
	/** paGoodsTarget 자동 재입점 여부(AUTO_YN) UPDATE*/
	public int updatePaGoodsTargetForAuthYn(Pa11stGoodsVO pa11stGoods) throws Exception{
		return update("pa11st.goods.updatePaGoodsTargetForAuthYn",pa11stGoods);
	}
	/** paGoodsTarget 자동 재입점 여부(AUTO_YN) LOG성 데이터 INSERT */
	public int insertPaGoodsAuthYnLog(PaGoodsAuthYnLog paGoodsAuthYnLog) throws Exception{
		return insert("pa11st.goods.insertPaGoodsAuthYnLog", paGoodsAuthYnLog);
	}
	public int updatePaStatus90(ParamMap paramMap) throws Exception{
		return update("pa11st.goods.updatePaStatus90",paramMap.get());
	}
	
	/**
	 * vodUrl 신규 리스트
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsVO> selectVodUrlInsertList() throws Exception{
		return list("pa11st.goods.selectVodUrlInsertList", null);
	}
	
	/**
	 * vodUrl 업데이트 리스트(픽캐스트)
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsVO> selectPickVodUrlUpdateList() throws Exception{
		return list("pa11st.goods.selectPickVodUrlUpdateList", null);
	}
	
	/**
	 * vodUrl 업데이트 리스트(goods)
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsVO> selectGoodsVodUrlUpdateList() throws Exception{
		return list("pa11st.goods.selectGoodsVodUrlUpdateList", null);
	}
	
	/**
	 * vodUrl 연동 대상 조회
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsVO> selectPa11stVodUrlTransList() throws Exception{
		return list("pa11st.goods.selectPa11stVodUrlTransList", null);
	}
	
	/**
	 * PAGOODSVODURL INSERT
	 * @param hashMap
	 * @return int
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int insertPaGoodsVodUrl(HashMap<String, Object> pickcast) throws Exception{
		return insert("pa11st.goods.insertPaGoodsVodUrl", pickcast);
	}
	
	/**
	 * PAGOODSVODURL UPDATE
	 * @param hashMap
	 * @return int
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int updatePaGoodsVodUrl(HashMap<String, Object> pickcast) throws Exception{
		return update("pa11st.goods.updatePaGoodsVodUrl",pickcast);
	}
	
	/**
	 * vodUrl 연동 성공 update
	 * @param Pa11stGoodsVO
	 * @return int
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int updatePaGoodsVodUrlTransYn(Pa11stGoodsVO pa11stGoods) throws Exception{
		return update("pa11st.goods.updatePaGoodsVodUrlTransYn", pa11stGoods);
	}
	
	/**
	 * vodUrl 연동 실패 update
	 * @param ParamMap
	 * @return int
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public int updatePaGoodsVodUrlFailMsg(ParamMap paramMap) throws Exception{
		return update("pa11st.goods.updatePaGoodsVodUrlFailMsg", paramMap.get());
	}
	
	public int updatePaGoodsDtMappingTransYn(ParamMap paramMap) throws Exception{
		return update("pa11st.goods.updatePaGoodsDtMappingTransYn",paramMap.get());
	}
	
	public int updatePa11stGoodsTransYn(ParamMap paramMap) throws Exception{
		return update("pa11st.goods.updatePa11stGoodsTransYn",paramMap.get());
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, String> selectPa11stGoodsNoticeYn(ParamMap paramMap) throws Exception{
		return (HashMap<String, String>) selectByPk("pa11st.goods.selectPa11stGoodsNoticeYn", paramMap.get());
	}
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 정보 조회
	 * @param paramMap
	 * @return HashMap
	 * @throws Exception
	 */
	public HashMap<String,Object> selectPa11stAlcoutDealInfo(ParamMap paramMap) throws Exception{
		return (HashMap<String,Object>)selectByPk("pa11st.goods.selectPa11stAlcoutDealInfo", paramMap.get());
	}
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 대표 상품정보 조회
	 * @param paramMap
	 * @return Pa11stGoodsVO
	 * @throws Exception
	 */
	public Pa11stGoodsVO selectPa11stAlcoutDealGoodsInfo(ParamMap paramMap) throws Exception{
		return (Pa11stGoodsVO)selectByPk("pa11st.goods.selectPa11stAlcoutDealGoodsInfo", paramMap.get());
	}
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜상품정보 조회
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectAlcoutDealGoodsList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pa11st.goods.selectAlcoutDealGoodsList", paramMap.get());
	}
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 단품정보 조회
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectPa11stAlcoutDealGoodsdtInfoList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pa11st.goods.selectPa11stAlcoutDealGoodsdtInfoList", paramMap.get());
	}
	
	/**
	 * 11번가 상품등록 -REQ_PRM_041 11번가 제휴OUT 딜 프로모션대상상품 조회
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	/*public List<HashMap<String,Object>> selectPaPromoTargetGoodsList(ParamMap paramMap) throws Exception{
	-주석 열기전에 반드시 프로모션 기준변경에 부합한지 확인할것!! 
	-AS-IS : 최신순 프로모션 연동  , TO-BE 혜택이 가장 강한(높은가격) 프로모션 연동
		return (List<HashMap<String,Object>>) list("pa11st.goods.selectPaPromoTargetGoodsList", paramMap.get());
	} */
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 정보 저장
	 * @param pa11stGoods
	 * @return
	 * @throws Exception
	 */
	public int updatePa11stAlcoutDeal(HashMap<String,Object> alcoutDealInfo) throws Exception{
		return update("pa11st.goods.updatePa11stAlcoutDeal", alcoutDealInfo);
	}
	
	/**
	 * 11번가 REQ_PRM_041 11번가 제휴OUT 딜 상품재고번호저장
	 * @param HashMap
	 * @return
	 * @throws Exception
	 */
	public int updatePa11stAlcoutDealGoodsdtMappingPaOptionCode(HashMap<String,Object> alcoutDealGoodsMapping) throws Exception{
		return update("pa11st.goods.updatePa11stAlcoutDealGoodsdtMappingPaOptionCode", alcoutDealGoodsMapping);
	}
	
	/**
	 * 11번가 REQ_PRM_041 11번가 제휴OUT 딜 연동실패 저장
	 * @param HashMap
	 * @return
	 * @throws Exception
	 */
	public int savePa11stAlcoutDealGoodsFail(HashMap<String,Object> alcoutDealInfo) throws Exception{
		return update("pa11st.goods.updatePa11stAlcoutDealGoodsFail", alcoutDealInfo);
	}
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 수정대상 딜 목록
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	public List<HashMap<String,Object>> selectPa11stModifyAlcoutDealList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pa11st.goods.selectPa11stModifyAlcoutDealList", paramMap.get());
	}
	
	/**
	 * 11번가 상품상세설명 수정 - REQ_PRM_041 11번가 제휴OUT 딜 상품상세설명 수정 list조회
	 * @param HashMap
	 * @return HashMap
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPa11stAlcoutDealGoodsDescribeModify(HashMap<String,Object> alcoutDealInfo) throws Exception{
		return (List<HashMap<String, String>>) list("pa11st.goods.selectPa11stAlcoutDealGoodsDescribeModify", alcoutDealInfo);
	}
	
	/**
	 * REQ_PRM_041 11번가 제휴OUT 딜 vodUrl 연동 대상 조회
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsVO> selectPa11stAlcoutDealVodUrlTransList() throws Exception{
		return list("pa11st.goods.selectPa11stAlcoutDealVodUrlTransList", null);
	}
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 단품 추가 정보 조회
	 * @param paramMap
	 * @return List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HashMap<String,Object>> selectPa11stNotExistsGoodsdtList(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pa11st.goods.selectPa11stNotExistsGoodsdtList", paramMap.get());
	}
	
	/**
	 * 11번가 상품등록 - REQ_PRM_041 11번가 제휴OUT 딜 단품 추가
	 * @param hashMap
	 * @return int
	 * @throws Exception
	 */
	public int insertPa11stNotExistsGoodsdt(HashMap<String, Object> pa11stNotExistsGoodsdt) throws Exception{
		return insert("pa11st.goods.insertPa11stNotExistsGoodsdt", pa11stNotExistsGoodsdt);
	}
	
	/**
	 * REQ_PRM_041 11번가 제휴OUT 딜 기술서 조회
	 * @return List<Pa11stGoodsVO>
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pa11stGoodsVO> selectPa11stAlcoutDealGoodsDescribe(ParamMap paramMap) throws Exception{
		return list("pa11st.goods.selectPa11stAlcoutDealGoodsDescribe", paramMap.get());
	}

	public int saveAlcoutDealPriceLog(List<HashMap> alcoutDealPriceLogList) throws Exception{
		return insert("pa11st.goods.mergeAlcoutDealPriceLog", alcoutDealPriceLogList);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectPaPromoDeleteTarget(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pa11st.goods.selectPaPromoDeleteTarget", paramMap.get());

	}
	public int insertNewPaPromoTarget(Pa11stGoodsVO pa11stGoods) throws Exception{
		return insert("pa11st.goods.insertNewPaPromoTarget", pa11stGoods);
	}
	
	public int updateTPapromoTarget4ExceptMemo(HashMap<String, Object> promo) throws Exception{
		return update("pa11st.goods.updateTPapromoTarget4ExceptMemo", promo);
	}

	@SuppressWarnings("unchecked")
	public HashMap<String, Object> selectPaPromoTargetOne(ParamMap paramMap) throws Exception {
		return (HashMap<String, Object>)selectByPk("pa11st.goods.selectPaPromoTargetOne", paramMap.get());
	}

	public int insertNewPaPromoTargetAmt0(Pa11stGoodsVO pa11stGoods) throws Exception {
		return insert("pa11st.goods.insertNewPaPromoTargetAmt0", pa11stGoods);
	}
	
	public int selectOutDealGoodsCheck(Pa11stGoodsVO asyncPa11stGoods) throws Exception{
		return (int)selectByPk("pa11st.goods.selectOutDealGoodsCheck", asyncPa11stGoods);
	}
	
	public int updateOutDealGoodsTarget(Pa11stGoodsVO asyncPa11stGoods) throws Exception{
		return update("pa11st.goods.updateOutDealGoodsTarget", asyncPa11stGoods);
	}

	public int selectPa11stGoodsModifyCheck(Pa11stGoodsVO pa11stGoods) throws Exception{
		return (int)selectByPk("pa11st.goods.selectPa11stGoodsModifyCheck", pa11stGoods);
	}

	public int updateMassTargetYn(Pa11stGoodsVO asyncPa11stGoods) throws Exception{
		return update("pa11st.goods.updateMassTargetYn", asyncPa11stGoods);
	}
	public int updateMassTargetYnByEpCode(HashMap<String, String> massMap) {
		return update("pa11st.goods.updateMassTargetYnByEpCode", massMap);
	}
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> selectPaGoodsTrans(ParamMap paramMap) throws Exception{
		return list("pa11st.goods.selectPaGoodsTrans", paramMap.get());
	}
	public int updatePa11stGoodsFailInsert(HashMap<String, String> paGoods) throws Exception {
		return update("pa11st.goods.updatePa11stGoodsFailInsert", paGoods);
	}

	public int updatePa11stGoodsdtTarget(String goodsCode) throws Exception {
		return update("pa11st.goods.updatePa11stGoodsdtTarget", goodsCode);
	}

	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectPa11stModifyAlcoutDealTarget(ParamMap paramMap) throws Exception {
		return list("pa11st.goods.selectPa11stModifyAlcoutDealTarget", paramMap.get());
	}

	public int insertAlcoutDealPriceLog(HashMap<String, Object> logMap) throws Exception {
		return update("pa11st.goods.insertAlcoutDealPriceLog", logMap);
	}

	public String selectMaxRetentionSeq(ParamMap paramMap) throws Exception {
		return (String)selectByPk("pa11st.goods.selectMaxRetentionSeq", paramMap.get());
	}

	public int selectCheckGoodsInsertStatus(Pa11stGoodsVO pa11stGoods) throws Exception {
		return (int)selectByPk("pa11st.goods.selectCheckGoodsInsertStatus", pa11stGoods);
	}
	
	public List<HashMap<String,Object>> selectPaGoodsPriceApply(ParamMap paramMap) throws Exception{
		return (List<HashMap<String,Object>>) list("pa11st.goods.selectPaGoodsPriceApply", paramMap.get());
	}
}