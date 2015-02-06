//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.06 at 07:32:24 PM CET 
//


package jaxbGenerated.datenxml;

import java.math.BigDecimal;
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
 *         &lt;element ref="{}name"/&gt;
 *         &lt;element ref="{}anzahl"/&gt;
 *         &lt;element ref="{}grundlage"/&gt;
 *         &lt;element ref="{}gewicht"/&gt;
 *         &lt;element ref="{}gesamtgewicht"/&gt;
 *         &lt;element ref="{}angelegt"/&gt;
 *         &lt;element ref="{}angelegt1"/&gt;
 *         &lt;element ref="{}angelegt2"/&gt;
 *         &lt;element ref="{}einzelpreis"/&gt;
 *         &lt;element ref="{}gesamtpreis"/&gt;
 *         &lt;element ref="{}quelle"/&gt;
 *         &lt;element ref="{}details" minOccurs="0"/&gt;
 *         &lt;element ref="{}arten"/&gt;
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
    "name",
    "anzahl",
    "grundlage",
    "gewicht",
    "gesamtgewicht",
    "angelegt",
    "angelegt1",
    "angelegt2",
    "einzelpreis",
    "gesamtpreis",
    "quelle",
    "details",
    "arten"
})
@XmlRootElement(name = "gegenstand")
public class Gegenstand {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected BigInteger anzahl;
    @XmlElement(required = true)
    protected String grundlage;
    @XmlElement(required = true)
    protected BigDecimal gewicht;
    @XmlElement(required = true)
    protected BigDecimal gesamtgewicht;
    protected boolean angelegt;
    protected boolean angelegt1;
    protected boolean angelegt2;
    @XmlElement(required = true)
    protected BigInteger einzelpreis;
    @XmlElement(required = true)
    protected BigInteger gesamtpreis;
    @XmlElement(required = true)
    protected Quelle quelle;
    protected Details details;
    @XmlElement(required = true)
    protected String arten;

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
     * Gets the value of the anzahl property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAnzahl() {
        return anzahl;
    }

    /**
     * Sets the value of the anzahl property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAnzahl(BigInteger value) {
        this.anzahl = value;
    }

    /**
     * Gets the value of the grundlage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGrundlage() {
        return grundlage;
    }

    /**
     * Sets the value of the grundlage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGrundlage(String value) {
        this.grundlage = value;
    }

    /**
     * Gets the value of the gewicht property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGewicht() {
        return gewicht;
    }

    /**
     * Sets the value of the gewicht property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGewicht(BigDecimal value) {
        this.gewicht = value;
    }

    /**
     * Gets the value of the gesamtgewicht property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getGesamtgewicht() {
        return gesamtgewicht;
    }

    /**
     * Sets the value of the gesamtgewicht property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setGesamtgewicht(BigDecimal value) {
        this.gesamtgewicht = value;
    }

    /**
     * Gets the value of the angelegt property.
     * 
     */
    public boolean isAngelegt() {
        return angelegt;
    }

    /**
     * Sets the value of the angelegt property.
     * 
     */
    public void setAngelegt(boolean value) {
        this.angelegt = value;
    }

    /**
     * Gets the value of the angelegt1 property.
     * 
     */
    public boolean isAngelegt1() {
        return angelegt1;
    }

    /**
     * Sets the value of the angelegt1 property.
     * 
     */
    public void setAngelegt1(boolean value) {
        this.angelegt1 = value;
    }

    /**
     * Gets the value of the angelegt2 property.
     * 
     */
    public boolean isAngelegt2() {
        return angelegt2;
    }

    /**
     * Sets the value of the angelegt2 property.
     * 
     */
    public void setAngelegt2(boolean value) {
        this.angelegt2 = value;
    }

    /**
     * Gets the value of the einzelpreis property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEinzelpreis() {
        return einzelpreis;
    }

    /**
     * Sets the value of the einzelpreis property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEinzelpreis(BigInteger value) {
        this.einzelpreis = value;
    }

    /**
     * Gets the value of the gesamtpreis property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGesamtpreis() {
        return gesamtpreis;
    }

    /**
     * Sets the value of the gesamtpreis property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGesamtpreis(BigInteger value) {
        this.gesamtpreis = value;
    }

    /**
     * Gets the value of the quelle property.
     * 
     * @return
     *     possible object is
     *     {@link Quelle }
     *     
     */
    public Quelle getQuelle() {
        return quelle;
    }

    /**
     * Sets the value of the quelle property.
     * 
     * @param value
     *     allowed object is
     *     {@link Quelle }
     *     
     */
    public void setQuelle(Quelle value) {
        this.quelle = value;
    }

    /**
     * Gets the value of the details property.
     * 
     * @return
     *     possible object is
     *     {@link Details }
     *     
     */
    public Details getDetails() {
        return details;
    }

    /**
     * Sets the value of the details property.
     * 
     * @param value
     *     allowed object is
     *     {@link Details }
     *     
     */
    public void setDetails(Details value) {
        this.details = value;
    }

    /**
     * Gets the value of the arten property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getArten() {
        return arten;
    }

    /**
     * Sets the value of the arten property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setArten(String value) {
        this.arten = value;
    }

}
