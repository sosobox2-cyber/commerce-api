package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for QuestionAnswerReturnType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="QuestionAnswerReturnType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="QuestionAnswerId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="CreateDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Question" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Answer" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="Answered" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ProductId" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="ProductName" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="WriterId" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "QuestionAnswerReturnType", propOrder = {
	    "questionAnswerId",
	    "createDate",
	    "question",
	    "answer",
	    "answered",
	    "productId",
	    "productName",
	    "writerId",
})
public class QuestionAnswerReturnType {

    @XmlElement(name = "QuestionAnswerId")
    protected long questionAnswerId;
    
    @XmlElement(name = "CreateDate")
    protected String createDate;
    
    @XmlElement(name = "Question")
    protected String question;
    
    @XmlElement(name = "Answer")
    protected String answer;
    
    @XmlElement(name = "Answered")
    protected String answered;
    
    @XmlElement(name = "ProductId")
    protected long productId;
    
    @XmlElement(name = "ProductName")
    protected String productName;
    
    @XmlElement(name = "WriterId")
    protected String writerId;
    
	public long getQuestionAnswerId() {
		return questionAnswerId;
	}
	public void setQuestionAnswerId(long questionAnswerId) {
		this.questionAnswerId = questionAnswerId;
	}
	public String getCreateDate() {
		return createDate;
	}
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public String getAnswered() {
		return answered;
	}
	public void setAnswered(String answered) {
		this.answered = answered;
	}
	public long getProductId() {
		return productId;
	}
	public void setProductId(long productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getWriterId() {
		return writerId;
	}
	public void setWriterId(String writerId) {
		this.writerId = writerId;
	}
}
