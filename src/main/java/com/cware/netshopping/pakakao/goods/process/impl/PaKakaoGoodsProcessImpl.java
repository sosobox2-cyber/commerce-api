package com.cware.netshopping.pakakao.goods.process.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.domain.PaKakaoGoodsVO;
import com.cware.netshopping.domain.PaKakaoTalkDealVO;
import com.cware.netshopping.domain.model.PaGoodsdtMapping;
import com.cware.netshopping.domain.model.PaKakaoGoodsImage;
import com.cware.netshopping.domain.model.PaKakaoTalkDeal;
import com.cware.netshopping.pakakao.goods.process.PaKakaoGoodsProcess;
import com.cware.netshopping.pakakao.goods.repository.PaKakaoGoodsDAO;

@Service("pakakao.goods.paKakaoGoodsProcess")
public class PaKakaoGoodsProcessImpl extends AbstractService implements PaKakaoGoodsProcess {

	@Resource(name = "pakakao.goods.paKakaoGoodsDAO")
	private PaKakaoGoodsDAO paKakaoGoodsDAO;

	@Override
	public PaKakaoGoodsVO selectPaKakaoGoodsInfo(ParamMap paramMap) throws Exception {
		return paKakaoGoodsDAO.selectPaKakaoGoodsInfo(paramMap);
	}
	
	@Override
	public List<PaKakaoGoodsVO> selectPaKakaoGoodsInfoList(ParamMap paramMap) throws Exception {
		return paKakaoGoodsDAO.selectPaKakaoGoodsInfoList(paramMap);
	}

	@Override
	public List<PaKakaoGoodsImage> selectKakaoGoodsImage(ParamMap paramMap) throws Exception {
		return paKakaoGoodsDAO.selectKakaoGoodsImage(paramMap);
	}

	@Override
	public int updatePakakaoGoodsImage(PaKakaoGoodsImage kakaoImage) throws Exception {
		return paKakaoGoodsDAO.updatePakakaoGoodsImage(kakaoImage);
	}
	
	@Override
	public int updatePakakaoGoodsImageCheck(PaKakaoGoodsImage kakaoImage) throws Exception {
		
		int executedRtn = paKakaoGoodsDAO.updatePakakaoGoodsImageCheck(kakaoImage);
		
		if("30".equals(kakaoImage.getUploadStatus())) {
			paKakaoGoodsDAO.updatePakakaoGoodsForImage(kakaoImage);
		}
		
		return executedRtn;
	}
	
	@Override
	public List<PaGoodsdtMapping> selectPaGoodsdtInfoList(ParamMap paramMap) throws Exception {
		return paKakaoGoodsDAO.selectPaGoodsdtInfoList(paramMap);
	}
	
	@Override
	public String savePaKakaoGoods(PaKakaoGoodsVO paKakaoGoods, List<PaGoodsdtMapping> goodsdtMapping, PaKakaoTalkDealVO dealData) throws Exception {
		String rtnMsg = Constants.SAVE_SUCCESS;
		int executedRtn = 0;

		try {
			executedRtn = paKakaoGoodsDAO.updatePaKakaoGoods(paKakaoGoods);
			if(executedRtn < 0) {
				log.info("TPAKAKAOGOODS update fail");
				throw processException("msg.cannot_save", new String[] { "TPAKAKAOGOODS UPDATE" });
			}
			
			if(goodsdtMapping != null && goodsdtMapping.size() > 0) {
				for(int i = 0; i < goodsdtMapping.size(); i++) {
					goodsdtMapping.get(i).setModifyId(paKakaoGoods.getModifyId());
					goodsdtMapping.get(i).setModifyDate(paKakaoGoods.getModifyDate());
					
					executedRtn = paKakaoGoodsDAO.updatePaKakaoGoodsdt(goodsdtMapping.get(i));
					if(executedRtn < 0) {
						log.info("TPAGOODSDTMAPPING UPDATE FAIL");
						throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
					}
				}
			}
			
			executedRtn = paKakaoGoodsDAO.updatePaGoodsImage(paKakaoGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSIMAGE update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSIMAGE UPDATE" });
			}
			
			executedRtn = paKakaoGoodsDAO.updatePaGoodsPriceApply(paKakaoGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSPRICE update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICE UPDATE" });
			}
			
			executedRtn = paKakaoGoodsDAO.updatePaGoodsTarget(paKakaoGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSTARGET update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSTARGET UPDATE" });
			}
			
			executedRtn = paKakaoGoodsDAO.updatePaGoodsOffer(paKakaoGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSOFFER update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSOFFER UPDATE" });
			}
			
			//딜 최초 적용 시 TRANS_BDATE UPDATE처리
			if ( dealData != null) {
				if(dealData.getTransBdate() == null) {
					dealData.setModifyId(paKakaoGoods.getModifyId());
					dealData.setModifyDate(paKakaoGoods.getModifyDate());
					executedRtn = paKakaoGoodsDAO.updatePaKakaoTalkDeal(dealData);
					if(executedRtn < 0) {
						log.info("TPAKAKAOTALKDEAL update fail");
						throw processException("msg.cannot_save", new String[] { "TPAKAKAOTALKDEAL UPDATE" });
					}
				}
			} else {
				//톡딜 아닐 시 만약 톡딜 살아있는 톡딜 있으면 전부 종료처리, 없으면 UPDATE 안함.
				executedRtn = paKakaoGoodsDAO.updatePaKakaoTalkDealEnd(paKakaoGoods);
				if(executedRtn < 0) {
					log.info("TPAKAKAOTALKDEAL update fail");
					throw processException("msg.cannot_save", new String[] { "TPAKAKAOTALKDEAL UPDATE" });
				}
			}
			/* TPAGOODSPRICEAPPLY로 전환 처리 22/03/31 by jchoi
			executedRtn = paKakaoGoodsDAO.updatePaGoodsPrice(paKakaoGoods);
			if(executedRtn < 0) {
				log.info("TPAGOODSPRICE update fail");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSPRICE UPDATE" });
			}
			
			executedRtn = paKakaoGoodsDAO.updatePaPromoGoodsPrice(paKakaoGoods);
			if(executedRtn < 0) {
				log.info("TPAPROMOGOODSPRICE update fail");
				throw processException("msg.cannot_save", new String[] { "TPAPROMOGOODSPRICE UPDATE" });
			}
			*/
		} catch(Exception e) {
			rtnMsg = e.getMessage();
			throw e;
		}
		return rtnMsg;
		
	}

	@Override
	public List<PaKakaoGoodsVO> selectPaKakaoSellStatusList(ParamMap paramMap) throws Exception {
		return paKakaoGoodsDAO.selectPaKakaoSellStatusList(paramMap);
	}

	@Override
	public int updatePakakaoGoodsSellStatus(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return paKakaoGoodsDAO.updatePakakaoGoodsSellStatus(paKakaoGoods);
	}
	
	@Override
	public List<PaKakaoGoodsVO> selectPaGoodsdtStockList(ParamMap paramMap) throws Exception {
		return paKakaoGoodsDAO.selectPaGoodsdtStockList(paramMap);
	}
	
	@Override
	public List<PaGoodsdtMapping> selectPaGoodsdtStockMappingList(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return paKakaoGoodsDAO.selectPaGoodsdtStockMappingList(paKakaoGoods);
	}
	
	public void updatePaGoodsdtMappingQtyTx(List<PaGoodsdtMapping> paGoodsdtList) throws Exception {
		
		for(PaGoodsdtMapping paGoodsdtMappingVO : paGoodsdtList) {
			int executedRtn = paKakaoGoodsDAO.updatePaKakaoGoodsdt(paGoodsdtMappingVO);
			if(executedRtn < 0) {
				log.info("TPAGOODSDTMAPPING UPDATE FAIL");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
			}
		}
		
	}

	@Override
	public List<PaKakaoGoodsVO> selectPaGoodsdtMappingId(ParamMap paramMap) throws Exception {
		return paKakaoGoodsDAO.selectPaGoodsdtMappingId(paramMap);
	}
	
	@Override
	public List<PaGoodsdtMapping> selectPaGoodsdtMappingIdList(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		return paKakaoGoodsDAO.selectPaGoodsdtMappingIdList(paKakaoGoods);
	}

	@Override
	public void updatePaKakaoGoodsdtMappingId(List<PaGoodsdtMapping> paGoodsdtMappingList) throws Exception {
		
		for(PaGoodsdtMapping paGoodsdtMapping : paGoodsdtMappingList) {
			int executedRtn = paKakaoGoodsDAO.updatePaKakaoGoodsdtMappingId(paGoodsdtMapping);
			if(executedRtn < 0) {
				log.info("TPAGOODSDTMAPPING UPDATE FAIL");
				throw processException("msg.cannot_save", new String[] { "TPAGOODSDTMAPPING UPDATE" });
			}
		}
		
	}

	@Override
	public void savePaKakaoGoodsError(PaKakaoGoodsVO paKakaoGoods) throws Exception {
		
		int executedRtn = paKakaoGoodsDAO.updatePaKakaoGoodsError(paKakaoGoods);
		
		if (executedRtn < 0) {
			throw processException("msg.cannot_save", new String[] { "TPAKAKAOGOODS UPDATE" });
		}
	}

	@Override
	public HashMap<String, Object> selectPaNoticeData(PaKakaoGoodsVO vo) throws Exception {
		return paKakaoGoodsDAO.selectPaNoticeData(vo);
	}

	@Override
	public List<PaKakaoTalkDealVO> selectPaKakaoDealInfoList(ParamMap paramMap) throws Exception {
		return paKakaoGoodsDAO.selectPaKakaoDealInfoList(paramMap);
	}
	
	@Override
	public void updatePaKakaoTalkDealId(PaKakaoTalkDealVO dealData) throws Exception {
		int executedRtn = 0;
		executedRtn = paKakaoGoodsDAO.updatePaKakaoTalkDealId(dealData);
		if (executedRtn < 0) {
			log.info("TPAKAKAOTALKDEAL UPDATE FAIL");
			throw processException("msg.cannot_save", new String[] { "TPAKAKAOTALKDEAL UPDATE" });
		}
	}

	@Override
	public PaKakaoTalkDeal selectPaKakaoDealInfo(ParamMap paramMap) throws Exception {
		return paKakaoGoodsDAO.selectPaKakaoDealInfo(paramMap);
	}

	@Override
	public String selectKakaoOrderGoodsDtName(Map<String, Object> map) throws Exception {
		return paKakaoGoodsDAO.selectKakaoOrderGoodsDtName(map);
	}

}
