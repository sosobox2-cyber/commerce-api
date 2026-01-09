
package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for GetQuestionAnswerListResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GetQuestionAnswerListResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="QuestionAnswerList" type="{http://shopn.platform.nhncorp.com/}QuestionAnswerReturnMapType" minOccurs="0"/>
 *         &lt;element name="Page" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="TotalPage" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetQuestionAnswerListResponseType", propOrder = {
    "questionAnswerList",
    "page",
    "totalPage"
})
public class GetQuestionAnswerListResponseType extends BaseProductResponseType {
	
	@XmlElement(name = "QuestionAnswerList", namespace = "")
    protected List<QuestionAnswerReturnMapType> questionAnswerList;
	@XmlElement(name = "Page", namespace = "")
    protected int page;
	@XmlElement(name = "TotalPage", namespace = "")
    protected int totalPage;
	
	public List<QuestionAnswerReturnMapType> getQuestionAnswerList() {
		if (questionAnswerList == null) {
			questionAnswerList = new ArrayList<QuestionAnswerReturnMapType>();
        }
		return this.questionAnswerList;
	}

	public int getPage() {
		return page;
	}

	public int getTotalPage() {
		return totalPage;
	}
	

}
