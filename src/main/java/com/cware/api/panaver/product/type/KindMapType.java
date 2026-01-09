package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KindMapType", propOrder = {
	"kindType"
})
public class KindMapType {
	@XmlElement(name = "KindType")
    protected List<StringCodeType> kindType;
	
	public List<StringCodeType> getKindType() {
        if (kindType == null) {
        	kindType = new ArrayList<StringCodeType>();
        }
        return this.kindType;
    }
}
