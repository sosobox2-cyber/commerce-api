package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for ManageQuestionAnswerRequestType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ManageQuestionAnswerRequestType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductRequestType">
 *       &lt;sequence>
 *         &lt;element name="SellerId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="QuestionAnswerId" type="{http://shopn.platform.nhncorp.com/}long"/>
 *         &lt;element name="Answered" type="{http://shopn.platform.nhncorp.com/}string"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ManageQuestionAnswerRequestType", propOrder = {
		"SellerId",
		"QuestionAnswerId",
		"Answer"
})
public class ManageQuestionAnswerRequestType 
	extends BaseProductRequestType
{

	@XmlElement(name = "SellerId", namespace = "", required = true)
	protected String SellerId;
	@XmlElement(name = "QuestionAnswerId", namespace = "", required = true)
	protected long QuestionAnswerId;
	@XmlElement(name = "Answer", namespace = "", required = true)
	protected String Answer;
	
	public String getSellerId() {
		return SellerId;
	}
	public void setSellerId(String sellerId) {
		SellerId = sellerId;
	}
	public long getQuestionAnswerId() {
		return QuestionAnswerId;
	}
	public void setQuestionAnswerId(long questionAnswerId) {
		QuestionAnswerId = questionAnswerId;
	}
	public String getAnswer() {
		return Answer;
	}
	public void setAnswer(String answer) {
		Answer = answer;
	}
}
