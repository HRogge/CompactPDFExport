//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.01.11 at 08:41:59 PM CET 
//


package jaxbGenerated.datenxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}f0"/>
 *         &lt;element ref="{}f1"/>
 *         &lt;element ref="{}f2"/>
 *         &lt;element ref="{}f3"/>
 *         &lt;element ref="{}f4"/>
 *         &lt;element ref="{}f5"/>
 *         &lt;element ref="{}text"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "f0",
    "f1",
    "f2",
    "f3",
    "f4",
    "f5",
    "text"
})
@XmlRootElement(name = "familie")
public class Familie {

    @XmlElement(required = true)
    protected String f0;
    @XmlElement(required = true)
    protected String f1;
    @XmlElement(required = true)
    protected String f2;
    @XmlElement(required = true)
    protected String f3;
    @XmlElement(required = true)
    protected String f4;
    @XmlElement(required = true)
    protected String f5;
    @XmlElement(required = true)
    protected String text;

    /**
     * Gets the value of the f0 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getF0() {
        return f0;
    }

    /**
     * Sets the value of the f0 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setF0(String value) {
        this.f0 = value;
    }

    /**
     * Gets the value of the f1 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getF1() {
        return f1;
    }

    /**
     * Sets the value of the f1 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setF1(String value) {
        this.f1 = value;
    }

    /**
     * Gets the value of the f2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getF2() {
        return f2;
    }

    /**
     * Sets the value of the f2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setF2(String value) {
        this.f2 = value;
    }

    /**
     * Gets the value of the f3 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getF3() {
        return f3;
    }

    /**
     * Sets the value of the f3 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setF3(String value) {
        this.f3 = value;
    }

    /**
     * Gets the value of the f4 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getF4() {
        return f4;
    }

    /**
     * Sets the value of the f4 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setF4(String value) {
        this.f4 = value;
    }

    /**
     * Gets the value of the f5 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getF5() {
        return f5;
    }

    /**
     * Sets the value of the f5 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setF5(String value) {
        this.f5 = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setText(String value) {
        this.text = value;
    }

}
