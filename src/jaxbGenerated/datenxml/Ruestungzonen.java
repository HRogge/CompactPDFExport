//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-b10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.27 at 08:14:21 AM CET 
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
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element ref="{}kopf"/>
 *         &lt;element ref="{}brust"/>
 *         &lt;element ref="{}ruecken"/>
 *         &lt;element ref="{}gesamt"/>
 *         &lt;element ref="{}bauch"/>
 *         &lt;element ref="{}linkerarm"/>
 *         &lt;element ref="{}rechterarm"/>
 *         &lt;element ref="{}linkesbein"/>
 *         &lt;element ref="{}rechtesbein"/>
 *         &lt;element ref="{}gesamtschutz"/>
 *         &lt;element ref="{}gesamtzonenschutz"/>
 *         &lt;element ref="{}behinderung"/>
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
    "kopf",
    "brust",
    "ruecken",
    "gesamt",
    "bauch",
    "linkerarm",
    "rechterarm",
    "linkesbein",
    "rechtesbein",
    "gesamtschutz",
    "gesamtzonenschutz",
    "behinderung"
})
@XmlRootElement(name = "ruestungzonen")
public class Ruestungzonen {

    @XmlElement(required = true)
    protected BigInteger kopf;
    @XmlElement(required = true)
    protected BigInteger brust;
    @XmlElement(required = true)
    protected BigInteger ruecken;
    @XmlElement(required = true)
    protected BigInteger gesamt;
    @XmlElement(required = true)
    protected BigInteger bauch;
    @XmlElement(required = true)
    protected BigInteger linkerarm;
    @XmlElement(required = true)
    protected BigInteger rechterarm;
    @XmlElement(required = true)
    protected BigInteger linkesbein;
    @XmlElement(required = true)
    protected BigInteger rechtesbein;
    @XmlElement(required = true)
    protected BigInteger gesamtschutz;
    @XmlElement(required = true)
    protected BigInteger gesamtzonenschutz;
    @XmlElement(required = true)
    protected String behinderung;

    /**
     * Gets the value of the kopf property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getKopf() {
        return kopf;
    }

    /**
     * Sets the value of the kopf property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setKopf(BigInteger value) {
        this.kopf = value;
    }

    /**
     * Gets the value of the brust property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBrust() {
        return brust;
    }

    /**
     * Sets the value of the brust property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBrust(BigInteger value) {
        this.brust = value;
    }

    /**
     * Gets the value of the ruecken property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRuecken() {
        return ruecken;
    }

    /**
     * Sets the value of the ruecken property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRuecken(BigInteger value) {
        this.ruecken = value;
    }

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
     * Gets the value of the bauch property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getBauch() {
        return bauch;
    }

    /**
     * Sets the value of the bauch property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setBauch(BigInteger value) {
        this.bauch = value;
    }

    /**
     * Gets the value of the linkerarm property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLinkerarm() {
        return linkerarm;
    }

    /**
     * Sets the value of the linkerarm property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLinkerarm(BigInteger value) {
        this.linkerarm = value;
    }

    /**
     * Gets the value of the rechterarm property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRechterarm() {
        return rechterarm;
    }

    /**
     * Sets the value of the rechterarm property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRechterarm(BigInteger value) {
        this.rechterarm = value;
    }

    /**
     * Gets the value of the linkesbein property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLinkesbein() {
        return linkesbein;
    }

    /**
     * Sets the value of the linkesbein property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLinkesbein(BigInteger value) {
        this.linkesbein = value;
    }

    /**
     * Gets the value of the rechtesbein property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getRechtesbein() {
        return rechtesbein;
    }

    /**
     * Sets the value of the rechtesbein property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setRechtesbein(BigInteger value) {
        this.rechtesbein = value;
    }

    /**
     * Gets the value of the gesamtschutz property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGesamtschutz() {
        return gesamtschutz;
    }

    /**
     * Sets the value of the gesamtschutz property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGesamtschutz(BigInteger value) {
        this.gesamtschutz = value;
    }

    /**
     * Gets the value of the gesamtzonenschutz property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGesamtzonenschutz() {
        return gesamtzonenschutz;
    }

    /**
     * Sets the value of the gesamtzonenschutz property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGesamtzonenschutz(BigInteger value) {
        this.gesamtzonenschutz = value;
    }

    /**
     * Gets the value of the behinderung property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBehinderung() {
        return behinderung;
    }

    /**
     * Sets the value of the behinderung property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBehinderung(String value) {
        this.behinderung = value;
    }

}
