package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Code;


public class CodeVO extends Code implements Comparable<Object> {

	private static final long serialVersionUID = 1L;	
		
	//= compare
    private CodeVO oldObject = null;
    public int compareTo(Object o){
        int  rtn = 0;
        oldObject = (CodeVO)o;		
    	rtn = (this.getCodeLgroup()+this.getCodeMgroup()).compareTo(oldObject.getCodeLgroup()+oldObject.getCodeMgroup());
    	return rtn; 
    }
    
	private String code;
	private String codeLgroup;
	private String codeMgroup;
	private String codeSgroup;
	private String codeName;
	private String codeLname;
	private String codeMname;
	private String codeGroup;
	private String remark;
	private String useYn;
	
	public String getCodeSgroup() {
		return codeSgroup;
	}
	public void setCodeSgroup(String codeSgroup) {
		this.codeSgroup = codeSgroup;
	}
	public String getUseYn() {
		return useYn;
	}
	public void setUseYn(String useYn) {
		this.useYn = useYn;
	}
	public String getCodeMgroup() {
		return codeMgroup;
	}
	public void setCodeMgroup(String codeMgroup) {
		this.codeMgroup = codeMgroup;
	}
	public String getCodeName() {
		return codeName;
	}
	public void setCodeName(String codeName) {
		this.codeName = codeName;
	}
	public String getCodeLname() {
		return codeLname;
	}
	public void setCodeLname(String codeLname) {
		this.codeLname = codeLname;
	}
	public String getCodeMname() {
		return codeMname;
	}
	public void setCodeMname(String codeMname) {
		this.codeMname = codeMname;
	}
	public String getCodeGroup() {
		return codeGroup;
	}
	public void setCodeGroup(String codeGroup) {
		this.codeGroup = codeGroup;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getCodeLgroup() {
		return codeLgroup;
	}
	public void setCodeLgroup(String codeLgroup) {
		this.codeLgroup = codeLgroup;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
}