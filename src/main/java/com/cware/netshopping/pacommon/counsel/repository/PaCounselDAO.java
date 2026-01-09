package com.cware.netshopping.pacommon.counsel.repository;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cware.framework.core.basic.AbstractPaDAO;
import com.cware.netshopping.domain.PaqnamVO;
import com.cware.netshopping.domain.model.Custcounseldt;
import com.cware.netshopping.domain.model.Custcounselm;
import com.cware.netshopping.domain.model.Paqnamoment;


@Service("pacommon.counsel.paCounselDAO")
public class PaCounselDAO extends AbstractPaDAO {

    /**
     * TPAQNAMOMENT INSERT
     * @param Paqnamoment
     * @return Integer
     * @throws Exception
     */
    public int insertPaQnaMoment(Paqnamoment paqnamoment) throws Exception{
	return insert("pacommon.pacounsel.insertPaQnaMoment", paqnamoment);
    }
    public int insertPaQnaMomentTemp(Paqnamoment paqnamoment) throws Exception{
	return insert("pacommon.pacounsel.insertPaQnaMomentTemp", paqnamoment);
    }
    /**
     * TPAQNAM INSERT
     * @param Paqnamoment
     * @return Integer
     * @throws Exception
     */
    public int insertPaQnaM(Paqnamoment paqnamoment) throws Exception{
	return insert("pacommon.pacounsel.insertPaQnaM", paqnamoment);
    }
    
    /**
     * TPAQNADT INSERT
     * @param Paqnamoment
     * @return Integer
     * @throws Exception
     */
    public int insertPaQnaDt(Paqnamoment paqnamoment) throws Exception{
	return insert("pacommon.pacounsel.insertPaQnaDt", paqnamoment);
    }
    
    /**
     * 제휴사 - 고객상담M INSERT 
     * @param Custcounselm
     * @return Integer
     * @throws Exception
     */
    public int insertCounselCustcounselm(Custcounselm custcounselm) throws Exception{
	return insert("pacommon.pacounsel.insertCounselCustcounselm", custcounselm);
    }
    
    /**
     * 제휴사 - 고객상담DT INSERT 
     * @param Custcounselm
     * @return Integer
     * @throws Exception
     */
    public int insertCounselCustcounseldt(Custcounseldt custcounseldt) throws Exception{
	return insert("pacommon.pacounsel.insertCounselCustcounseldt", custcounseldt);
    }
    
    /**
     * 제휴사 - 고객상담순번 UPDATE 
     * @param PaqnamVO
     * @return Integer
     * @throws Exception 
     */
    public int updatePaQnaCounselSeq(PaqnamVO paqnamVO) throws Exception{
	return update("pacommon.pacounsel.updatePaQnaCounselSeq", paqnamVO);
    }
    
    /**
	 * paCounselSeq 생성
	 * @return
	 */
	public String selectPaCounselNewSeq() {
		return (String) selectByPk("pacommon.pacounsel.selectPaCounselNewSeq", null);
	}
	
	@SuppressWarnings("unchecked")
	public List<HashMap<String, Object>> selectPaQnaM(Paqnamoment paqnamoment) {
		return list("pacommon.pacounsel.selectPaQnaM", paqnamoment);
	}
	public int insertPaQnaMByPaCounselNo(Paqnamoment paqnamoment) {
		return insert("pacommon.pacounsel.insertPaQnaMByPaCounselNo", paqnamoment);
		
	}

}
