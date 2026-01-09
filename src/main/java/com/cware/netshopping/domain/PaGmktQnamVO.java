package com.cware.netshopping.domain;

import com.cware.netshopping.domain.model.Paqnam;

public class PaGmktQnamVO extends Paqnam{

    private static final long serialVersionUID = 1L;

    private String itemNo;
    
    private String title;
    
    private String procNote;

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProcNote() {
        return procNote;
    }

    public void setProcNote(String procNote) {
        this.procNote = procNote;
    }
    
    
}
