//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.06 at 07:32:24 PM CET 
//


package jaxbGenerated.datenxml;

import java.math.BigInteger;
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
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}gesamt"/&gt;
 *         &lt;element ref="{}frei"/&gt;
 *         &lt;element ref="{}genutzt"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "gesamt",
    "frei",
    "genutzt"
})
@XmlRootElement(name = "ap")
public class Ap {

    @XmlElement(required = true)
    protected BigInteger gesamt;
    @XmlElement(required = true)
    protected BigInteger frei;
    @XmlElement(required = true)
    protected BigInteger genutzt;

    /**
     * Gets the value of the gesamt property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGesamt() {
        return gesamt;
    }

    /**
     * Sets the value of the gesamt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGesamt(BigInteger value) {
        this.gesamt = value;
    }

    /**
     * Gets the value of the frei property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getFrei() {
        return frei;
    }

    /**
     * Sets the value of the frei property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setFrei(BigInteger value) {
        this.frei = value;
    }

    /**
     * Gets the value of the genutzt property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGenutzt() {
        return genutzt;
    }

    /**
     * Sets the value of the genutzt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGenutzt(BigInteger value) {
        this.genutzt = value;
    }

}
