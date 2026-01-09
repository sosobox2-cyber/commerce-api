package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ManageQuestionAnswerResponseType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ManageQuestionAnswerResponseType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType">
 *       &lt;sequence>
 *         &lt;element name="QuestionAnswerId" type="{http://shopn.platform.nhncorp.com/}long"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManageQuestionAnswerResponseType", propOrder = {
	"QuestionAnswerId"
})
	
public class ManageQuestionAnswerResponseType extends BaseProductResponseType{

	@XmlElement(name = "QuestionAnswerId", namespace = "")
	protected long QuestionAnswerId;

	public long getQuestionAnswerId() {
		return QuestionAnswerId;
	}

	public void setQuestionAnswerId(long questionAnswerId) {
		QuestionAnswerId = questionAnswerId;
	}
}
