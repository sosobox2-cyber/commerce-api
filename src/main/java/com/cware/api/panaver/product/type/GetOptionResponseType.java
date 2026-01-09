package com.cware.api.panaver.product.type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>GetOptionResponseType complex type에 대한 Java 클래스입니다.
 * 
 * <p>다음 스키마 단편이 이 클래스에 포함되는 필요한 콘텐츠를 지정합니다.
 * 
 * <pre>
 * &lt;complexType name="GetOptionResponseType"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://shopn.platform.nhncorp.com/}BaseProductResponseType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="Option" type="{http://shopn.platform.nhncorp.com/}OptionType" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GetOptionResponseType", propOrder = {
    "option"
})
public class GetOptionResponseType
    extends BaseProductResponseType
{

    @XmlElement(name = "Option", namespace = "")
    protected OptionType option;

    /**
     * option 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link OptionType }
     *     
     */
    public OptionType getOption() {
        return option;
    }

    /**
     * option 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link OptionType }
     *     
     */
    public void setOption(OptionType value) {
        this.option = value;
    }

}
