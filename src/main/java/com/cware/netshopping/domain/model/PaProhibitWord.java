package com.cware.netshopping.domain.model;

import com.cware.framework.core.basic.AbstractModel;

public class PaProhibitWord extends AbstractModel {

    private static final long serialVersionUID = 1L;

    private long prohibitwordSeq;
    private String paGroupCode;
    private String prohibitedWord;
    private String paLgroup;
    private String paMgroup;
    private String paSgroup;
    private String paDgroup;
    
    public long getProhibitwordSeq() {
        return prohibitwordSeq;
    }
    public String getPaGroupCode() {
        return paGroupCode;
    }
    public String getProhibitedWord() {
        return prohibitedWord;
    }
    public String getPaLgroup() {
        return paLgroup;
    }
    public String getPaMgroup() {
        return paMgroup;
    }
    public String getPaSgroup() {
        return paSgroup;
    }
    public String getPaDgroup() {
        return paDgroup;
    }
    public void setProhibitwordSeq(long prohibitwordSeq) {
        this.prohibitwordSeq = prohibitwordSeq;
    }
    public void setPaGroupCode(String paGroupCode) {
        this.paGroupCode = paGroupCode;
    }
    public void setProhibitedWord(String prohibitedWord) {
        this.prohibitedWord = prohibitedWord;
    }
    public void setPaLgroup(String paLgroup) {
        this.paLgroup = paLgroup;
    }
    public void setPaMgroup(String paMgroup) {
        this.paMgroup = paMgroup;
    }
    public void setPaSgroup(String paSgroup) {
        this.paSgroup = paSgroup;
    }
    public void setPaDgroup(String paDgroup) {
        this.paDgroup = paDgroup;
    }
    
}
