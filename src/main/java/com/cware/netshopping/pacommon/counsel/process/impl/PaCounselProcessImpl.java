package com.cware.netshopping.pacommon.counsel.process.impl;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractService;
import com.cware.framework.core.basic.ParamMap;
import com.cware.netshopping.common.Constants;
import com.cware.netshopping.common.system.repository.SystemDAO;
import com.cware.netshopping.common.util.ComUtil;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Custcounseldt;
import com.cware.netshopping.domain.model.Custcounselm;
import com.cware.netshopping.domain.model.Paqnamoment;
import com.cware.netshopping.pa11st.counsel.repository.Pa11stCounselDAO;
import com.cware.netshopping.pacommon.counsel.process.PaCounselProcess;
import com.cware.netshopping.pacommon.counsel.repository.PaCounselDAO;
import com.cware.netshopping.pacopn.counsel.repository.PaCopnCounselDAO;
import com.cware.netshopping.pafaple.repository.PaFapleCounselDAO;
import com.cware.netshopping.pagmkt.counsel.repository.PaGmktCounselDAO;
import com.cware.netshopping.pahalf.counsel.repository.PaHalfCounselDAO;
import com.cware.netshopping.paintp.counsel.repository.PaIntpCounselDAO;
import com.cware.netshopping.palton.counsel.repository.PaLtonCounselDAO;
import com.cware.netshopping.panaver.counsel.repository.PaNaverCounselDAO;
import com.cware.netshopping.paqeen.repository.PaQeenCounselDAO;
import com.cware.netshopping.patdeal.repository.PaTdealCounselDAO;
import com.cware.netshopping.patmon.counsel.repository.PaTmonCounselDAO;
import com.cware.netshopping.pawemp.counsel.repository.PaWempCounselDAO;

@Service("pacommon.counsel.paCounselProcess")
public class PaCounselProcessImpl extends AbstractService implements PaCounselProcess {

    @Resource(name = "pacommon.counsel.paCounselDAO")
    private PaCounselDAO paCounselDAO;
    
    @Resource(name = "common.system.systemDAO")
    private SystemDAO systemDAO;

    @Resource(name = "pagmkt.counsel.paGmktCounselDAO")
    private PaGmktCounselDAO paGmktCounselDAO;
    
    @Resource(name = "pa11st.counsel.pa11stCounselDAO")
    private Pa11stCounselDAO pa11stCounselDAO;
    
    @Resource(name = "panaver.counsel.paNaverCounselDAO")
    private PaNaverCounselDAO paNaverCounselDAO;
    
    @Resource(name = "pacopn.counsel.paCopnCounselDAO")
    private PaCopnCounselDAO paCopnCounselDAO; 
    
    @Resource(name = "pawemp.counsel.paWempCounselDAO")
    private PaWempCounselDAO paWempCounselDAO;
    
    @Resource(name = "paintp.counsel.paIntpCounselDAO")
    private PaIntpCounselDAO paIntpCounselDAO;
    
    @Resource(name = "palton.counsel.paLtonCounselDAO")
    private PaLtonCounselDAO paLtonCounselDAO;
    
    @Resource(name = "patmon.counsel.paTmonCounselDAO")
    private PaTmonCounselDAO paTmonCounselDAO;
    
    @Resource(name = "pahalf.counsel.paHalfCounselDAO")
    private PaHalfCounselDAO paHalfCounselDAO;
    
    @Resource(name = "patdeal.counsel.paTdealCounselDAO")
    private PaTdealCounselDAO paTdealCounselDAO;
    
    @Resource(name = "pafaple.counsel.paFapleCounselDAO")
    private PaFapleCounselDAO paFapleCounselDAO;
    
    @Resource(name = "paqeen.counsel.paQeenCounselDAO")
    private PaQeenCounselDAO paQeenCounselDAO;
    
    @Override
    public String savePaQna(List<Paqnamoment> paQnaMomentList, String msgGb)
    		throws Exception {

    	String rtnMsg = Constants.SAVE_SUCCESS;
    	int totalExecutedRtn = 0;
    	int executedRtn = 0;

    	List<PaqnamVO> paqnaList = null;
    	Custcounselm custcounselm = null;
    	Custcounseldt custcounseldt = null;
    	String counselSeq = "";
    	String paCode = "";
    	String siteGb = "";

    	try {	
    		if (paQnaMomentList.size() > 0) {
    			paCode = paQnaMomentList.get(0).getPaCode();
    			siteGb = paQnaMomentList.get(0).getInsertId().toString();
    		}
    		String subQuestMoment = "";
    		for(Paqnamoment paqnamoment : paQnaMomentList){
    			if(paqnamoment.getPaGroupCode().equals("04") &&  paqnamoment.getMsgGb().equals("30")){
    				subQuestMoment = ComUtil.subStringBytes(paqnamoment.getQuestComment(), 2000); // tcustcounseldt 2000까지 허용
    				paqnamoment.setQuestComment(subQuestMoment);
    				executedRtn = paNaverCounselDAO.insertPaQnaMoment(paqnamoment);
    			} else if((paCode.equals("51")||paCode.equals("52"))&&(msgGb.equals("40")||msgGb.equals("50"))){
    				subQuestMoment = ComUtil.subStringBytes(paqnamoment.getQuestComment(), 2000);
    				paqnamoment.setQuestComment(subQuestMoment);
    				executedRtn = paCopnCounselDAO.insertPaQnaMoment(paqnamoment);
    			} else{
    				subQuestMoment = ComUtil.subStringBytes(paqnamoment.getQuestComment(), 2000);
    				paqnamoment.setQuestComment(subQuestMoment);
    				executedRtn = paCounselDAO.insertPaQnaMoment(paqnamoment);
    			}

    			ParamMap paramMap = new ParamMap();
    			paramMap.put("program_id","11stCounsel");
    			int procCnt = systemDAO.selectPaprocCheck(paramMap);
    			//procCnt가 1일때만 tempTable insert되도록, TCODE[V001]
    			if(procCnt == 1){
    				paCounselDAO.insertPaQnaMomentTemp(paqnamoment); //= tpaqnamoment insert dup orcale에러로 temp 테이블 생성 2019.01.08 thjeon 
    			}
    			if (executedRtn < 0) {
    				log.info("tpaqnamoment insert fail");
    				throw processException("msg.cannot_save", new String[] { "TPAQNAMOMENT INSERT" });
    			}

    			totalExecutedRtn += executedRtn;
    		}

    		if(totalExecutedRtn > 0){
    			//log.info("tpaqnamoment insert success");
    			
    			if("08".equals(paQnaMomentList.get(0).getPaGroupCode()) && "10".equals(msgGb)) { //롯데온 판매자연락
    	    		for(Paqnamoment paqnamoment : paQnaMomentList){
    	    			log.info("롯데온 인입데이터 PA_COUNSEL_NO: {} , 문의일시 : {}" ,paqnamoment.getPaCounselNo(), paqnamoment.getCounselDate() );
    	    			boolean duple = false;
    	    			List<HashMap<String, Object>> paqnamomentMapList = paCounselDAO.selectPaQnaM(paqnamoment);
    	    			
    	    			if(paqnamomentMapList.size() > 0) {
    	    				for(HashMap<String, Object> paqnamomentMap : paqnamomentMapList) {
    	    					Timestamp  counselDate = (Timestamp) paqnamomentMap.get("COUNSEL_DATE");
    	    					log.info("롯데온 기존데이터 PA_COUNSEL_NO: {} , 문의일시 : {}" ,paqnamomentMap.get("PA_COUNSEL_NO"), counselDate );
    	    					if(counselDate.equals(paqnamoment.getCounselDate())) {
    	    						log.info("롯데온 PA_COUNSEL_NO: {} , 문의일시 : {} - 중복 데이터" ,paqnamomentMap.get("PA_COUNSEL_NO"), counselDate );
    	    						duple = true;
    	    					}
    	    				}
    	    			}
    	    			if(duple == false) {
    	    				executedRtn = paCounselDAO.insertPaQnaMByPaCounselNo(paqnamoment);
    	    			}
    	    		}
    				
    			}else {
    				//= tpaqnam insert
    				executedRtn = paCounselDAO.insertPaQnaM(paQnaMomentList.get(0));
    				
    				if (executedRtn < 0) {
    					log.info("tpaqnam insert fail");
    					throw processException("msg.cannot_save", new String[] { "TPAQNAM INSERT" });
    				}
    			}

    			//= tpaqnadt insert
				
				executedRtn = paCounselDAO.insertPaQnaDt(paQnaMomentList.get(0));

				if (executedRtn < 0) {
					log.info("tpaqnadt insert fail");
					throw processException("msg.cannot_save", new String[] { "TPAQNADT INSERT" });
				}

    			if(executedRtn > 0){

    				switch(paCode){
    				case Constants.PA_11ST_BROAD_CODE : 
    				case Constants.PA_11ST_ONLINE_CODE : 
    					if(msgGb.equals("00")){ //일반메세지
    						paqnaList = pa11stCounselDAO.selectPa11stQna(); break;
    					}else{ //긴급메세지
    						paqnaList = pa11stCounselDAO.selectPa11stUrgentQna(); break;
    					}
    				case Constants.PA_GMKT_BROAD_CODE :
    				case Constants.PA_GMKT_ONLINE_CODE :
    					if(msgGb.equals("00") || msgGb.equals("20")){ //일반메세지(00), 쪽지(20)
    						paqnaList = paGmktCounselDAO.selectPaGmktQna(siteGb); break;
    					}else{ //긴급메세지(10)
    						paqnaList = paGmktCounselDAO.selectPaGmktUrgentQna(siteGb); break;
    					}
    				case Constants.PA_NAVER_CODE :
    					if(msgGb.equals("00")){//상품
    						paqnaList = paNaverCounselDAO.selectPaNaverQna(); break;
    					}else{//판매자(30)
    						paqnaList = paNaverCounselDAO.selectPaNaverCustQna(); break;
    					}
    				case Constants.PA_COPN_BROAD_CODE :
    				case Constants.PA_COPN_ONLINE_CODE :
    					if(msgGb.equals("00")){//상품
    						paqnaList = paCopnCounselDAO.selectPaCopnQna(); break;
    					}else{//쿠팡 콜센터 (40)
    						paqnaList = paCopnCounselDAO.selectPaCopnCallQna(); break;
    					}
    				case Constants.PA_WEMP_BROAD_CODE :
    				case Constants.PA_WEMP_ONLINE_CODE :
    					if(msgGb.equals("00")) {
    						paqnaList = paWempCounselDAO.selectPaWempQna(); break;
    					} else if(msgGb.equals("10")){
    						paqnaList = paWempCounselDAO.selectPaWempNotice(); break;
    					}
    				case Constants.PA_INTP_BROAD_CODE :
    				case Constants.PA_INTP_ONLINE_CODE :
    					if(msgGb.equals("00")) {
    						paqnaList = paIntpCounselDAO.selectPaIntpQna(msgGb); break;
    					}else if(msgGb.equals("10")){
    						paqnaList = paIntpCounselDAO.selectPaIntpUrgentQna(msgGb); break;
    					}
    				case Constants.PA_LTON_BROAD_CODE :
    				case Constants.PA_LTON_ONLINE_CODE :
    					if(msgGb.equals("00")) {
    						paqnaList = paLtonCounselDAO.selectPaLtonQna(); break;
    					} else if(msgGb.equals("10")){
    						paqnaList = paLtonCounselDAO.selectPaLtonSellerContract(); break;
    					} else if(msgGb.equals("20")) {
    						paqnaList = paLtonCounselDAO.selectSellerInquiryList(); break;
    					}
    				case Constants.PA_TMON_BROAD_CODE :
    				case Constants.PA_TMON_ONLINE_CODE :
    					paqnaList = paTmonCounselDAO.selectPaTmonQna(msgGb); break;
    				case Constants.PA_HALF_BROAD_CODE :
 		    	    case Constants.PA_HALF_ONLINE_CODE :
 		    	    	if(msgGb.equals("00")) {
     						paqnaList = paHalfCounselDAO.selectPaHalfQna(); break;
     					} else if(msgGb.equals("10")){ //CS목록조회
     						paqnaList = paHalfCounselDAO.selectPaHalfCs(); break;
     					}	
 		    	    case Constants.PA_TDEAL_BROAD_CODE :
 		    	    case Constants.PA_TDEAL_ONLINE_CODE :
 		    	    	if(msgGb.equals("00")) {//상품
 		    	    		paqnaList = paTdealCounselDAO.selectPaTdealQna(); break;
 		    	    	} else if(msgGb.equals("10")){ //CS목록조회
     						paqnaList = paTdealCounselDAO.selectPaTdealTaskMessage(); break;
     					}	
 		    	    case Constants.PA_FAPLE_BROAD_CODE :
 		    	    case Constants.PA_FAPLE_ONLINE_CODE :
 		    	    	if(msgGb.equals("00")) {//상품
 		    	    		paqnaList = paFapleCounselDAO.selectPaFapleQna(); break;
 		    	    	} else if(msgGb.equals("10")){ //CS목록조회
 		    	    		paqnaList = paFapleCounselDAO.selectPaFapleCs(); break;
 		    	    	}	
 		    	    case Constants.PA_QEEN_BROAD_CODE :
 		    	    case Constants.PA_QEEN_ONLINE_CODE :
 		    	    	paqnaList = paQeenCounselDAO.selectPaQeenCsQna(); break;
    				}
    				//네이버 조회 쿼리 추가e
    				// 상품Q&A , 판매자 문의 쿼리 분리할지, 통합할지 확인

    				for(PaqnamVO paqnam : paqnaList){
    					HashMap<String, Object> hashMap = new HashMap<String, Object>();
    					hashMap.put("sequence_type", "COUNSEL_SEQ");

    					counselSeq = (String)systemDAO.selectSequenceNo(hashMap);
    					if (counselSeq.equals("")) {
    						throw processException("msg.cannot_create", new String[] { "COUNSEL_SEQ" });
    					}

    					custcounselm = new Custcounselm();
    					custcounselm.setCounselSeq(counselSeq);
    					custcounselm.setCustNo(paqnam.getCustNo());
    					if(Constants.PA_COUNSEL_GB_GOODS.equals(paqnam.getCounselGb()) && !paCode.equals("41")){
    						custcounselm.setDoFlag("25");
    					}else {
    						custcounselm.setDoFlag("10");
    					}
    					custcounselm.setRefNo1(paqnam.getRefNo());
    					custcounselm.setRefNo2("");
    					custcounselm.setRefNo3("");
    					custcounselm.setRefNo4("");
    					custcounselm.setCsLgroup("60");
    					if(paCode.equals("11") || paCode.equals("12")) {
    						custcounselm.setCsMgroup("01");
    						if(Constants.PA_COUNSEL_GB_GOODS.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("01");
    							custcounselm.setCsLmsCode("600101");
    						} else if (Constants.PA_COUNSEL_GB_DELIVERY.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("02");
    							custcounselm.setCsLmsCode("600102");
    						} else if (Constants.PA_COUNSEL_GB_CANCEL.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("03");
    							custcounselm.setCsLmsCode("600103");
    						} else if (Constants.PA_COUNSEL_GB_EXCHANGE.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("04");
    							custcounselm.setCsLmsCode("600104");
    						} else if (Constants.PA_COUNSEL_GB_ETC.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("05");
    							custcounselm.setCsLmsCode("600105");
    						} else if (Constants.PA_COUNSEL_GB_ALIMI.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("07");
    							custcounselm.setCsLmsCode("600107");
    						} else {
    							custcounselm.setCsSgroup("05");
    							custcounselm.setCsLmsCode("600105");
    							paqnam.setTitle("미정의 분류");
    						}
    					} else if(siteGb.equals("PAG") & (paCode.equals("21") || paCode.equals("22"))) {
    						custcounselm.setCsMgroup("02");
    						if(Constants.PA_GMKT_COUNSEL_GB_GOODS.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("01");
    							custcounselm.setCsLmsCode("600201");
    						} else if (Constants.PA_GMKT_COUNSEL_GB_DELIVERY.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("02");
    							custcounselm.setCsLmsCode("600202");
    						} else if (Constants.PA_GMKT_COUNSEL_GB_RETURN.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("03");
    							custcounselm.setCsLmsCode("600203");
    						} else if (Constants.PA_GMKT_COUNSEL_GB_CANCEL.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("04");
    							custcounselm.setCsLmsCode("600204");
    						} else if (Constants.PA_GMKT_COUNSEL_GB_EXCHANGE.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("05");
    							custcounselm.setCsLmsCode("600205");
    						} else if (Constants.PA_GMKT_COUNSEL_GB_ETC.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("06");
    							custcounselm.setCsLmsCode("600206");
    						} else if (Constants.PA_GMKT_COUNSEL_GB_ALIMI.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("08");
    							custcounselm.setCsLmsCode("600208");
    						} else {
    							custcounselm.setCsSgroup("06");
    							custcounselm.setCsLmsCode("600206");
    							paqnam.setTitle("미정의 분류");
    						}
    					} else if(siteGb.equals("PAA") & (paCode.equals("21") || paCode.equals("22"))) {
    						custcounselm.setCsMgroup("03");
    						if(Constants.PA_IAC_COUNSEL_GB_GOODS.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("01");
    							custcounselm.setCsLmsCode("600301");
    							paqnam.setTitle("상품 문의입니다");
    						} else if (Constants.PA_IAC_COUNSEL_GB_DELIVERY.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("02");
    							custcounselm.setCsLmsCode("600302");
    							paqnam.setTitle("배송 문의입니다");
    						} else if (Constants.PA_IAC_COUNSEL_GB_RETURN.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("03");
    							custcounselm.setCsLmsCode("600303");
    							paqnam.setTitle("환불 문의입니다");
    						} else if (Constants.PA_IAC_COUNSEL_GB_EXCHANGE.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("05");
    							custcounselm.setCsLmsCode("600305");
    							paqnam.setTitle("교환 문의입니다");
    						} else if (Constants.PA_IAC_COUNSEL_GB_ETC.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("05");
    							custcounselm.setCsLmsCode("600306");
    							paqnam.setTitle("기타 문의입니다");
    						} else if (Constants.PA_IAC_COUNSEL_GB_ALIMI.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("08");
    							custcounselm.setCsLmsCode("600308");
    							paqnam.setTitle("긴급 알리미");
    						} else {
    							custcounselm.setCsSgroup("05");
    							custcounselm.setCsLmsCode("600306");
    							paqnam.setTitle("미정의 분류");
    						}
    						// 옥션쪽지
    						if( "20".equals(paqnam.getMsgGb()) ) {
    							paqnam.setTitle("옥션쪽지");
    						}
    					} else if(paCode.equals("41")){
    						custcounselm.setCsMgroup("04");
    						if(Constants.PA_NAVER_COUNSEL_GB_GOODS.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("01");
    							custcounselm.setCsLmsCode("600401");
    							paqnam.setTitle("상품 문의입니다");
    						}else if(Constants.PA_NAVER_COUNSEL_GB_DELIVERY.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("02");
    							custcounselm.setCsLmsCode("600402");
    							paqnam.setTitle("배송 문의입니다");
    						}else if(Constants.PA_NAVER_COUNSEL_GB_RETURN.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("03");
    							custcounselm.setCsLmsCode("600403");
    							paqnam.setTitle("반품 문의입니다");
    						}else if(Constants.PA_NAVER_COUNSEL_GB_EXCHANGE.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("04");
    							custcounselm.setCsLmsCode("600404");
    							paqnam.setTitle("교환 문의입니다");
    						}else if(Constants.PA_NAVER_COUNSEL_GB_REFUND.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("05");
    							custcounselm.setCsLmsCode("600405");
    							paqnam.setTitle("환불 문의입니다");
    						}else if(Constants.PA_NAVER_COUNSEL_GB_ETC.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("06");
    							custcounselm.setCsLmsCode("600406");
    							paqnam.setTitle("기타 문의입니다");
    						}//네이버 처리 부분 추가
    					} else if (paCode.equals("51") || paCode.equals("52")){//쿠팡
    						custcounselm.setCsMgroup("05");
    						if(Constants.PA_COPN_COUNSEL_GB_GOODS.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("01");
    							custcounselm.setCsLmsCode("600501");
    						} else if("40".equals(paqnam.getMsgGb().toString())){
    							custcounselm.setCsSgroup("02");
    							custcounselm.setCsLmsCode("600502");
    						} else {
    							custcounselm.setCsSgroup("03");
    							custcounselm.setCsLmsCode("600503");
    						}
    					} else if (paCode.equals("61") || paCode.equals("62")){//위메프
    						custcounselm.setCsMgroup("06");
    						if(Constants.PA_WEMP_COUNSEL_GB_GOODS.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("01");
    							custcounselm.setCsLmsCode("600601");
    							paqnam.setTitle("상품 문의입니다");
    						} else if ("10".equals(paqnam.getMsgGb().toString())){
    							custcounselm.setCsSgroup("02");
    							custcounselm.setCsLmsCode("600602");
    							paqnam.setTitle("위메프 공지사항");
    						}
    					} else if (paCode.equals("71") || paCode.equals("72")){//인터파크
    						custcounselm.setCsMgroup("07");
    						if(Constants.PA_INTP_COUNSEL_GB_GOODS.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("01");
    							custcounselm.setCsLmsCode("600701");
    						}else if("10".equals(paqnam.getMsgGb().toString())){
    							custcounselm.setCsSgroup("02");
    							custcounselm.setCsLmsCode("600702");
    							paqnam.setTitle("인터파크 긴급알리미");
    						}
    					} else if (paCode.equals("81") || paCode.equals("82")){//롯데온
    						custcounselm.setCsMgroup("08");
    						if("00".equals(paqnam.getMsgGb().toString())) {
    							if("01".equals(paqnam.getToken())) {
    								custcounselm.setCsSgroup("01");
        							custcounselm.setCsLmsCode("600801");
        							paqnam.setTitle("상품QnA[사이즈/용량]");
    							}else if("02".equals(paqnam.getToken())) {
    								custcounselm.setCsSgroup("02");
        							custcounselm.setCsLmsCode("600802");
        							paqnam.setTitle("상품QnA[디자인/색상]");
    							}else if("03".equals(paqnam.getToken())) {
    								custcounselm.setCsSgroup("03");
        							custcounselm.setCsLmsCode("600803");
        							paqnam.setTitle("상품QnA[상품정보]");
    							}else if("04".equals(paqnam.getToken())) {
    								custcounselm.setCsSgroup("04");
        							custcounselm.setCsLmsCode("600804");
        							paqnam.setTitle("상품QnA[사용설명]");
    							}else if("05".equals(paqnam.getToken())) {
    								custcounselm.setCsSgroup("05");
        							custcounselm.setCsLmsCode("600805");
        							paqnam.setTitle("상품QnA[구성품]");
    							}else {
    								custcounselm.setCsSgroup("06");
        							custcounselm.setCsLmsCode("600806");
        							paqnam.setTitle("상품QnA[기타]");
    							}
    						}else if("10".equals(paqnam.getMsgGb().toString())){
    							custcounselm.setCsSgroup("07");
    							custcounselm.setCsLmsCode("600807");
    							paqnam.setTitle("롯데온문의[판매자 연락]");
    						}else if("20".equals(paqnam.getMsgGb().toString())){
    							custcounselm.setCsSgroup("08");
    							custcounselm.setCsLmsCode("600808");
    							paqnam.setTitle("고객문의[판매자 문의]");
    						}
    					} else if (paCode.equals("91") || paCode.equals("92")){//티몬
    						custcounselm.setCsMgroup("09");
    						custcounselm.setCsSgroup(paqnam.getCsSgroup());
							custcounselm.setCsLmsCode(paqnam.getCsLmsCode());
    					} else if(paCode.equals("C1") || paCode.equals("C2")) {
    						custcounselm.setCsMgroup("12");
    						custcounselm.setCsSgroup(paqnam.getCsSgroup());
							custcounselm.setCsLmsCode(paqnam.getCsLmsCode());
    					} else if(paCode.equals("D1") || paCode.equals("D2")) { //티딜
    						custcounselm.setCsMgroup("13");
							if(Constants.PA_TDEAL_COUNSEL_GB_PRODUCT.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("01");
    							custcounselm.setCsLmsCode("601301");
    							paqnam.setTitle("상품 문의입니다");
    						}else if(Constants.PA_TDEAL_COUNSEL_GB_DELIVERY.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("02");
    							custcounselm.setCsLmsCode("601302");
    							paqnam.setTitle("배송 문의입니다");
    						}else if(Constants.PA_TDEAL_COUNSEL_GB_CANCEL.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("03");
    							custcounselm.setCsLmsCode("601303");
    							paqnam.setTitle("취소 문의입니다");
    						}else if(Constants.PA_TDEAL_COUNSEL_GB_RETURN.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("04");
    							custcounselm.setCsLmsCode("601304");
    							paqnam.setTitle("반품 문의입니다");
    						}else if(Constants.PA_TDEAL_COUNSEL_GB_EXCHANGE.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("05");
    							custcounselm.setCsLmsCode("601305");
    							paqnam.setTitle("교환 문의입니다");
    						}else if(Constants.PA_TDEAL_COUNSEL_GB_REFUND.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("06");
    							custcounselm.setCsLmsCode("601306");
    							paqnam.setTitle("환불 문의입니다");
    						}else if(Constants.PA_TDEAL_COUNSEL_GB_ALIMI.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("08");
    							custcounselm.setCsLmsCode("601308");
    							paqnam.setTitle("티딜 업무 메세지 입니다.");
    						}else {
    							custcounselm.setCsSgroup("07");
    							custcounselm.setCsLmsCode("601307");
    							paqnam.setTitle("기타 문의입니다");
    						}
    						
    					} else if(paCode.equals("E1") || paCode.equals("E2")) {
    						custcounselm.setCsMgroup("14");
    						
    						
    						if(Constants.PA_FAPLE_COUNSEL_GB_PRODUCT.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("01");
    							custcounselm.setCsLmsCode("601401");
    							paqnam.setTitle("패션플러스 상품 문의입니다");
    						}else if(Constants.PA_FAPLE_COUNSEL_GB_DELIVERY.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("02");
    							custcounselm.setCsLmsCode("601402");
    							paqnam.setTitle("패션플러스 배송 문의입니다");
    						}else if(Constants.PA_FAPLE_COUNSEL_GB_CLAIM.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("03");
    							custcounselm.setCsLmsCode("601403");
    							paqnam.setTitle("패션플러스 클레임 문의입니다");
    						}else if(Constants.PA_FAPLE_COUNSEL_GB_REFUND.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("05");
    							custcounselm.setCsLmsCode("601405");
    							paqnam.setTitle("패션플러스 환불 문의입니다");
    						}else if(Constants.PA_FAPLE_COUNSEL_GB_ALIMI.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("06");
    							custcounselm.setCsLmsCode("601406");
    							paqnam.setTitle("패션플러스 CS알리미 메세지");
    						}else {
    							custcounselm.setCsSgroup("07");
    							custcounselm.setCsLmsCode("601407");
    							paqnam.setTitle("패션플러스 기타문의 입니다.");
    						}
    					}else if(paCode.equals("F1") || paCode.equals("F2")) {
    						custcounselm.setCsMgroup("15");
    						
    						if(Constants.PA_QEEN_COUNSEL_GB_PRODUCT.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("01");
    							custcounselm.setCsLmsCode("601501");
    							paqnam.setTitle("퀸잇 상품 문의입니다");
    						}else if(Constants.PA_QEEN_COUNSEL_GB_DELIVERY.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("02");
    							custcounselm.setCsLmsCode("601502");
    							paqnam.setTitle("퀸잇 배송 문의입니다");
    						}else if(Constants.PA_QEEN_COUNSEL_GB_CANCEL.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("03");
    							custcounselm.setCsLmsCode("601503");
    							paqnam.setTitle("퀸잇 취소 문의입니다");
    						}else if(Constants.PA_QEEN_COUNSEL_GB_RETURN.equals(paqnam.getCounselGb())){
    							custcounselm.setCsSgroup("04");
    							custcounselm.setCsLmsCode("601504");
    							paqnam.setTitle("퀸잇 반품 문의입니다");
    						}else if(Constants.PA_QEEN_COUNSEL_GB_EXCHANGE.equals(paqnam.getCounselGb())) {
    							custcounselm.setCsSgroup("05");
    							custcounselm.setCsLmsCode("601505");
    							paqnam.setTitle("퀸잇 교환 문의");
    						}else {
    							custcounselm.setCsSgroup("06");
    							custcounselm.setCsLmsCode("601506");
    							paqnam.setTitle("퀸잇 기타문의 입니다.");
    						}
    					}
    					
    					custcounselm.setOutLgroupCode("99");
    					custcounselm.setOutMgroupCode("99");
    					custcounselm.setGoodsCode(paqnam.getGoodsCode());
    					custcounselm.setGoodsdtCode("");
    					custcounselm.setClaimNo(counselSeq);
    					custcounselm.setTel("");
    					custcounselm.setDdd("");
    					custcounselm.setTel1("");
    					custcounselm.setTel2("");
    					custcounselm.setTel3("");
    					custcounselm.setWildYn("0");
    					custcounselm.setQuickYn("0");
    					custcounselm.setQuickEndYn("0");
    					custcounselm.setHcReqDate(null);
    					custcounselm.setRemark("");
    					custcounselm.setRefId1("");
    					custcounselm.setCsSendYn("1");
    					custcounselm.setSendEntpCode(paqnam.getEntpCode());
    					custcounselm.setCounselMedia(paqnam.getPaCode());
    					custcounselm.setInsertId(paqnam.getInsertId());
    					custcounselm.setInsertDate(paqnam.getInsertDate());
    					custcounselm.setProcId(paqnam.getModifyId());
    					custcounselm.setProcDate(paqnam.getModifyDate());

    					executedRtn = paCounselDAO.insertCounselCustcounselm(custcounselm);
    					if (executedRtn < 1) {
    						//= 오류
    						throw processException("msg.cannot_save", new String[] { "TCUSTCOUNSELM INSERT" });
    					}
    					/*접수 데이터 생성*/
    					custcounseldt = new Custcounseldt();
    					custcounseldt.setCounselSeq(counselSeq);
    					custcounseldt.setCounselDtSeq("100");
    					custcounseldt.setDoFlag("10");
    					custcounseldt.setTitle(paqnam.getTitle());
    					custcounseldt.setDisplayYn("");

    					String note = ComUtil.text2db("["+paqnam.getTitle()+"]"+paqnam.getProcNote());
    					int len = note.getBytes("UTF-8").length;
    					if(len > 2000) {
    						custcounseldt.setProcNote( ComUtil.subStringBytes(note, 1950) + "[내용잘림]");
    					} else {
    						custcounseldt.setProcNote(note);
    					}

    					custcounseldt.setProcDate(paqnam.getInsertDate());
    					custcounseldt.setProcId(paqnam.getInsertId());
    					executedRtn = paCounselDAO.insertCounselCustcounseldt(custcounseldt);
    					if (executedRtn != 1) {
    						throw processException("msg.cannot_save", new String[] { "TCUSTCOUNSELDT insert" });
    					}

    					if(Constants.PA_COUNSEL_GB_GOODS.equals(paqnam.getCounselGb()) && !paCode.equals("41")){
    						/*업체이관 데이터 생성*/
    						custcounseldt.setCounselDtSeq("101");
    						custcounseldt.setDoFlag("25");
    						custcounseldt.setDisplayYn("1");
    						custcounseldt.setReceiverSeq("0000000001");
    						executedRtn = paCounselDAO.insertCounselCustcounseldt(custcounseldt);
    						if (executedRtn != 1) {
    							throw processException("msg.cannot_save", new String[] { "TCUSTCOUNSELDT insert" });
    						}
    					}

    					paqnam.setCounselSeq(counselSeq);
    					executedRtn = paCounselDAO.updatePaQnaCounselSeq(paqnam);
    					if (executedRtn != 1) {
    						throw processException("msg.cannot_save", new String[] { "TPAQNAM update" });
    					}
    				}//for end
    			}//if end
    		} else {
    			log.info("tpaqnamoment insert fail");
    			throw processException("msg.cannot_save", new String[] { "TPAQNAMOMENT INSERT" });
    		}

    	} catch (Exception e) {
    		rtnMsg = e.getMessage();
    		throw e;
    	}

    	return rtnMsg;
    }
}
