//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-b10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.27 at 08:14:21 AM CET 
//


package jaxbGenerated.datenxml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element ref="{}tier" minOccurs="0"/>
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
    "tier"
})
@XmlRootElement(name = "details")
public class Details {

    protected Tier tier;

    /**
     * Gets the value of the tier property.
     * 
     * @return
     *     possible object is
     *     {@link Tier }
     *     
     */
    public Tier getTier() {
        return tier;
    }

    /**
     * Sets the value of the tier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Tier }
     *     
     */
    public void setTier(Tier value) {
        this.tier = value;
    }

}