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
 *         &lt;element ref="{}nummer"/&gt;
 *         &lt;element ref="{}name"/&gt;
 *         &lt;element ref="{}spalte2"/&gt;
 *         &lt;element ref="{}reichweite"/&gt;
 *         &lt;element ref="{}tp"/&gt;
 *         &lt;element ref="{}tpmod"/&gt;
 *         &lt;element ref="{}at"/&gt;
 *         &lt;element ref="{}ladezeit"/&gt;
 *         &lt;element ref="{}kampftalent"/&gt;
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
    "nummer",
    "name",
    "spalte2",
    "reichweite",
    "tp",
    "tpmod",
    "at",
    "ladezeit",
    "kampftalent"
})
@XmlRootElement(name = "fernkampfwaffe")
public class Fernkampfwaffe {

    @XmlElement(required = true)
    protected BigInteger nummer;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String spalte2;
    @XmlElement(required = true)
    protected String reichweite;
    @XmlElement(required = true)
    protected String tp;
    @XmlElement(required = true)
    protected String tpmod;
    @XmlElement(required = true)
    protected String at;
    @XmlElement(required = true)
    protected BigInteger ladezeit;
    @XmlElement(required = true)
    protected String kampftalent;

    /**
     * Gets the value of the nummer property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNummer() {
        return nummer;
    }

    /**
     * Sets the value of the nummer property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNummer(BigInteger value) {
        this.nummer = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the spalte2 property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpalte2() {
        return spalte2;
    }

    /**
     * Sets the value of the spalte2 property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpalte2(String value) {
        this.spalte2 = value;
    }

    /**
     * Gets the value of the reichweite property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReichweite() {
        return reichweite;
    }

    /**
     * Sets the value of the reichweite property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReichweite(String value) {
        this.reichweite = value;
    }

    /**
     * Gets the value of the tp property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTp() {
        return tp;
    }

    /**
     * Sets the value of the tp property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTp(String value) {
        this.tp = value;
    }

    /**
     * Gets the value of the tpmod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTpmod() {
        return tpmod;
    }

    /**
     * Sets the value of the tpmod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTpmod(String value) {
        this.tpmod = value;
    }

    /**
     * Gets the value of the at property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAt() {
        return at;
    }

    /**
     * Sets the value of the at property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAt(String value) {
        this.at = value;
    }

    /**
     * Gets the value of the ladezeit property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLadezeit() {
        return ladezeit;
    }

    /**
     * Sets the value of the ladezeit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLadezeit(BigInteger value) {
        this.ladezeit = value;
    }

    /**
     * Gets the value of the kampftalent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKampftalent() {
        return kampftalent;
    }

    /**
     * Sets the value of the kampftalent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKampftalent(String value) {
        this.kampftalent = value;
    }

}
