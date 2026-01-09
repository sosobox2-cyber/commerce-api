package com.cware.api.panaver.product.type;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for QuestionAnswerReturnMapType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuestionAnswerReturnMapType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="QuestionAnswerList" type="{http://shopn.platform.nhncorp.com/}QuestionAnswerReturnType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestionAnswerReturnMapType", propOrder = {
	    "questionAnswerList"
})
public class QuestionAnswerReturnMapType {
	
	@XmlElement(name = "QuestionAnswerList")
    protected List<QuestionAnswerReturnType> questionAnswerList;
	
	public List<QuestionAnswerReturnType> getQuestionAnswer() {
        if (questionAnswerList == null) {
        	questionAnswerList = new ArrayList<QuestionAnswerReturnType>();
        }
        return this.questionAnswerList;
    }
}
