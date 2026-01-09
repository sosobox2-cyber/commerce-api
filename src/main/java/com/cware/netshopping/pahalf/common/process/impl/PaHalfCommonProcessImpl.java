package com.cware.netshopping.pahalf.common.process.impl;

import java.util.HashMap;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.service.SystemService;
import com.cware.netshopping.domain.PaHalfShipInfoVO;
import com.cware.netshopping.domain.model.PaGoodsKinds;
import com.cware.netshopping.domain.model.PaHalfBrand;
import com.cware.netshopping.domain.model.PaOfferCode;
import com.cware.netshopping.pacommon.common.repository.PaCommonDAO;
import com.cware.netshopping.pahalf.common.process.PaHalfCommonProcess;
import com.cware.netshopping.pahalf.common.repository.PaHalfCommonDAO;
import com.cware.netshopping.pahalf.util.PaHalfComUtill;

@Service("pahalf.common.paHalfCommonProcess")
public class PaHalfCommonProcessImpl extends AbstractService implements PaHalfCommonProcess {

	@Resource(name = "pacommon.common.paCommonDAO")
	private PaCommonDAO paCommonDAO;
	
	@Resource(name = "pahalf.common.paHalfCommonDAO")
	private PaHalfCommonDAO paHalfCommonDAO; 

	@Resource(name = "common.system.systemService")
	private SystemService systemService;

	@Override
	public String savePaHalfBrand(List<PaHalfBrand> paHalfBrandList) throws Exception {
		String rtnMsg = "";
		int failCount = 0;
		int totalCount = paHalfBrandList.size();
		
		for(PaHalfBrand paHalfBrand : paHalfBrandList) {
			try {
				paHalfCommonDAO.insertPaHalfBrand(paHalfBrand);
			}
			catch(Exception e) {
				log.info(PaHalfComUtill.getErrorMessage(e));
				failCount++;
			}
		}
			
		rtnMsg = "전체 건수 : " + totalCount + "건, 실패 건수 : " + failCount +"건";

		return rtnMsg;

	}
	
	@Override
	public String saveStdItemList(List<PaGoodsKinds> stdItemList) throws Exception {
		String rtnMsg = "";
		int executedRtn = 0;
		int failCount = 0;
		int totalCount = stdItemList.size();
			
			for(PaGoodsKinds goodsKinds : stdItemList) {
				try {
				executedRtn = paHalfCommonDAO.insertStdItemList(goodsKinds);
				if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAGOODSKINDS INSERT" });
			
				}
				 catch(Exception e) {
					 log.info(PaHalfComUtill.getErrorMessage(e));
					 failCount++;
				}
			}
		
		rtnMsg = "전체 건수 : " + totalCount + "건, 실패 건수 : " + failCount +"건";
		
		return rtnMsg;
	}

	@Override
	public String savePaHalfOfferCode(List<PaOfferCode> paOfferCodeList) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;
		
		try {
			for(PaOfferCode paOffer : paOfferCodeList) {
				executedRtn = paHalfCommonDAO.insertPaHalfOfferCode(paOffer);
				if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAOFFERCODE INSERT" });
			
			}
			
		} catch(Exception e) {
			rtnMsg = PaHalfComUtill.getErrorMessage(e);
		}
		
		return rtnMsg;
	}

	@Override
	public void setPaShipCostInfo(PaHalfShipInfoVO paHalfShipInfo, ParamMap apiDataMap) throws Exception {	
		if (paHalfShipInfo == null) throw processException("errors.process_fail", new String[] {"배승템플릿 데이터 조회 실패 : selectHalfShipCostInfo"} );
		
		double jejuAmt   =  paHalfShipInfo.getJejuCost() 	- paHalfShipInfo.getOrdCost();
		double isLandAmt =  paHalfShipInfo.getIslandCost() 	- paHalfShipInfo.getOrdCost();
		double exchAmt	 =  paHalfShipInfo.getChangeCost()  - paHalfShipInfo.getReturnCost();
		String useAddAmt =  "N";
		if(isLandAmt > 0 || jejuAmt > 0 ) useAddAmt = "Y";
		
		String dlvTmpltType = "";
		if("2".equals(paHalfShipInfo.getShipCostReceipt())) { //착불배송비
			dlvTmpltType = "4";
		}else if("ID".equals(paHalfShipInfo.getShipCostFlag())) {
			dlvTmpltType = "3";
		}else if("FR".equals(paHalfShipInfo.getShipCostFlag())) {
			dlvTmpltType = "2";
		}else {
			dlvTmpltType = "1";
		}
		
		apiDataMap.put("dlvTmpltSeq"		, paHalfShipInfo.getPaShipCostCode() != null ? paHalfShipInfo.getPaShipCostCode() : "" ); //배송템플릿 번호
		apiDataMap.put("dlvTmpltNm"			, paHalfShipInfo.getEntpCode()+ paHalfShipInfo.getShipManSeq() + paHalfShipInfo.getReturnManSeq()+paHalfShipInfo.getShipCostCode()); //배송탬플릿 이름
		
		
		apiDataMap.put("dlvCmpnyCd"			, "39"); //배송택배사 코드
		apiDataMap.put("dlvCmpnyCrctCd"		, "999999999"); //배송택배사 계약코드 '0000'
		
		
		apiDataMap.put("dlvAmt"				, paHalfShipInfo.getOrdCost()); //배송비
		apiDataMap.put("freeDlvMinAmt"		, paHalfShipInfo.getShipCostBaseAmt()); //무료배송 하한금액
		apiDataMap.put("rtrnAmt"			, paHalfShipInfo.getReturnCost()); //반품비
		apiDataMap.put("rtrnCmpnyCd"		, "39"); //반품택배사
		apiDataMap.put("rtrnCmpnyCrctCd"	, "999999999"); //반품택배사 계약코드 ''
		apiDataMap.put("dlvCmpnyAcctCd"		, ""); //택배사 거래처 코드
		
		apiDataMap.put("rtrnBalnTypCd"		, "01"); //반품정산구분 01- 신용, 02- 선불 (01고정)
		apiDataMap.put("rtripDlvFeeYn"		, paHalfShipInfo.getShipCostCode().contains("PL") ? "Y":"N"); //왕복 배송비 부과여부
		apiDataMap.put("rtrnBoxSpecCd"		, "01"); //반품박스 규격 -  소(2kg)
		apiDataMap.put("rtrnBoxUnitCost"	, 0); //반품 배송박스규격 계약단가
		apiDataMap.put("telNo"				, paHalfShipInfo.getRetHp()); //전화번호 ( 회수지 담당자 핸드폰 번호 )
		apiDataMap.put("rtrnZipCd"			, paHalfShipInfo.getRetPostNo()); //반품주소우편번호
		apiDataMap.put("rtrnAddr"			, paHalfShipInfo.getRetAddr()); //반품주소
		apiDataMap.put("rtrnAddrDtl"		, paHalfShipInfo.getRetAddrDt()); //반품주소 상세
		apiDataMap.put("dlvTmpltType"		, dlvTmpltType); // 배송비 유형 ( CN_조건부 - 1, FR_무료- 2 , ID_고정배송비 - 3 , 착불 - 4)
		apiDataMap.put("shippingZipCd"		, paHalfShipInfo.getPostNo()); //출고 주소 우편번호
		
		apiDataMap.put("shippingAddr"		, paHalfShipInfo.getAddr()); //출고 주소
		apiDataMap.put("shippingAddrDtl"	, paHalfShipInfo.getAddrDt()); //출고 주소 상세
		apiDataMap.put("jejuYn"				, useAddAmt); //제주 도서산간 추가배송비 설정 여부
		apiDataMap.put("baseDlvYn"			, "N"); //대표 배송템플릿 지정여부 'N' 고정
		
		apiDataMap.put("codDlvAmt"   		, paHalfShipInfo.getOrdCost()); //착불 배송비
		
		apiDataMap.put("outskrtsAmtJeju"	, jejuAmt 	> 0 ? jejuAmt 	: 0); //제주 배송비
		apiDataMap.put("outskrtsAmtIsland"	, isLandAmt > 0 ? isLandAmt : 0); //도서산간 배송비		
		apiDataMap.put("exchngAmt"			, exchAmt	> 0 ? exchAmt	: 0);//교환비 편도
		
		apiDataMap.put("outskrtsDeliveryYn", "1".equals(paHalfShipInfo.getNoShipJejuIsland())? "Y" : "N");
		
	}

	@Override
	public int insertTpaHalfShipInfo(ParamMap paramMap) throws Exception {
		int cnt = paHalfCommonDAO.selectNullShipCostCode(paramMap);
		if(cnt > 0) return 0;
		
		int executedRtn = paHalfCommonDAO.insertTpaHalfShipInfo(paramMap);
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAHALFSHIPINFO INSERT" });		
		
		return executedRtn;
	}

	@Override
	public int updateTpaHalfShipInfo(PaHalfShipInfoVO halfShipCostInfo) throws Exception {
			
		int	executedRtn = paHalfCommonDAO.updateTpaHalfShipInfo(halfShipCostInfo);
		//if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPAHALFSHIPINFO UPDATE" });
		
		executedRtn = paHalfCommonDAO.insertPaHalfShipInfoHistory(halfShipCostInfo);
		if(executedRtn < 1) throw processException("msg.cannot_save", new String[] { "TPAHALFSHIPINFOHISTORY INSERT" });
		
		return executedRtn;
	}

	@Override
	public PaHalfShipInfoVO selectHalfShipCostInfo(ParamMap paramMap) throws Exception {
		return paHalfCommonDAO.selectHalfShipCostInfo(paramMap);
	}

	@Override
	public List<HashMap<String, Object>> selectPaHalfSlipInfoList(ParamMap paramMap) throws Exception {
		return paHalfCommonDAO.selectPaHalfSlipInfoList(paramMap);
	}

	@Override
	public HashMap<String, Object> selectPaShipCostCode(ParamMap paramMap) throws Exception {
		return paHalfCommonDAO.selectPaShipCostCode(paramMap);
	}


	@Override
	public void insertPaHalfBrandMapping() throws Exception {
		int	executedRtn = paHalfCommonDAO.insertPaHalfBrandMapping();
		if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPAHALFBRANDMAPPING INSERT" });
	}

	@Override
	public List<HashMap<String, Object>> selectBrandFilterGoods() throws Exception {
		return paHalfCommonDAO.selectBrandFilterGoods();
	}

	@Override
	public void savePaHalfGoodsTarget(List<HashMap<String, Object>> brandFilterGoodsList) throws Exception {
		int executedRtn = 0;
		int failCnt = 0;
		int totalCnt = brandFilterGoodsList.size();
		for(HashMap<String, Object> brandFilterGoods : brandFilterGoodsList) {
			try {
				brandFilterGoods.put("PA_GROUP_CODE", "12");
				brandFilterGoods.put("INSERT_ID"    , "PAHALF");
				brandFilterGoods.put("MODIFY_ID"    , "PAHALF");

				executedRtn = paHalfCommonDAO.insertPaHalfGoodsTarget(brandFilterGoods);
				if(executedRtn < 0) throw processException("msg.cannot_save", new String[] { "TPAGOODSTARGET INSERT" });
			}
			catch(Exception e) {
				log.info("{} GOODS_CODE : {} {}", "하프클럽 신규 브랜드 상품 타겟 등록 오류",brandFilterGoods.get("GOODS_CODE"), PaHalfComUtill.getErrorMessage(e));
				failCnt++;
			}
		}
		if(failCnt > 0 ) {
			throw processException("errors.detail", new String[] {"하프클럽 신규 브랜드 상품 타겟 등록 오류  - 실패:" + failCnt + "건 전체:" + totalCnt + "건"});
		}
		
	}
	 
}
