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
 *         &lt;element ref="{}ini"/&gt;
 *         &lt;element ref="{}mod"/&gt;
 *         &lt;element ref="{}at"/&gt;
 *         &lt;element ref="{}pa"/&gt;
 *         &lt;element ref="{}bfmin"/&gt;
 *         &lt;element ref="{}bfakt"/&gt;
 *         &lt;element ref="{}bf"/&gt;
 *         &lt;element ref="{}typ"/&gt;
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
    "ini",
    "mod",
    "at",
    "pa",
    "bfmin",
    "bfakt",
    "bf",
    "typ"
})
@XmlRootElement(name = "schild")
public class Schild {

    @XmlElement(required = true)
    protected BigInteger nummer;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected BigInteger ini;
    @XmlElement(required = true)
    protected String mod;
    @XmlElement(required = true)
    protected String at;
    @XmlElement(required = true)
    protected String pa;
    @XmlElement(required = true)
    protected BigInteger bfmin;
    @XmlElement(required = true)
    protected BigInteger bfakt;
    @XmlElement(required = true)
    protected String bf;
    @XmlElement(required = true)
    protected String typ;

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
     * Gets the value of the ini property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getIni() {
        return ini;
    }

    /**
     * Sets the value of the ini property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setIni(BigInteger value) {
        this.ini = value;
    }

    /**
     * Gets the value of the mod property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMod() {
        return mod;
    }

    /**
     * Sets the value of the mod property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMod(String value) {
        this.mod = value;
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
     * Gets the value of the pa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPa() {
        return pa;
    }

    /**
     * Sets the value of the pa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPa(String value) {
        this.pa = value;
    }

    /**
     * Gets the value of the bfmin property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBfmin() {
        return bfmin;
    }

    /**
     * Sets the value of the bfmin property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBfmin(BigInteger value) {
        this.bfmin = value;
    }

    /**
     * Gets the value of the bfakt property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBfakt() {
        return bfakt;
    }

    /**
     * Sets the value of the bfakt property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBfakt(BigInteger value) {
        this.bfakt = value;
    }

    /**
     * Gets the value of the bf property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBf() {
        return bf;
    }

    /**
     * Sets the value of the bf property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBf(String value) {
        this.bf = value;
    }

    /**
     * Gets the value of the typ property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTyp() {
        return typ;
    }

    /**
     * Sets the value of the typ property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTyp(String value) {
        this.typ = value;
    }

}
