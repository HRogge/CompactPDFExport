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
import javax.xml.bind.annotation.XmlAttribute;
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
 *         &lt;element ref="{}raufen"/&gt;
 *         &lt;element ref="{}ringen"/&gt;
 *         &lt;element ref="{}ausweichen"/&gt;
 *         &lt;element ref="{}ausweichenausweichenmod"/&gt;
 *         &lt;element ref="{}ausweichenakrobatikmod"/&gt;
 *         &lt;element ref="{}ausweichenmod"/&gt;
 *         &lt;element ref="{}geschwindigkeitinklbe"/&gt;
 *         &lt;element ref="{}ini"/&gt;
 *         &lt;choice&gt;
 *           &lt;element ref="{}ruestungeinfach"/&gt;
 *           &lt;element ref="{}ruestungzonen"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element ref="{}nahkampfwaffen"/&gt;
 *         &lt;element ref="{}fernkampfwaffen"/&gt;
 *         &lt;element ref="{}schilder"/&gt;
 *         &lt;element ref="{}ruestungen"/&gt;
 *       &lt;/sequence&gt;
 *       &lt;attribute name="inbenutzung" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="nr" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" /&gt;
 *       &lt;attribute name="tzm" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *       &lt;attribute name="defaultrsmodel" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" /&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "raufen",
    "ringen",
    "ausweichen",
    "ausweichenausweichenmod",
    "ausweichenakrobatikmod",
    "ausweichenmod",
    "geschwindigkeitinklbe",
    "ini",
    "ruestungeinfach",
    "ruestungzonen",
    "nahkampfwaffen",
    "fernkampfwaffen",
    "schilder",
    "ruestungen"
})
@XmlRootElement(name = "kampfset")
public class Kampfset {

    @XmlElement(required = true)
    protected Raufen raufen;
    @XmlElement(required = true)
    protected Ringen ringen;
    @XmlElement(required = true)
    protected BigInteger ausweichen;
    @XmlElement(required = true)
    protected BigInteger ausweichenausweichenmod;
    @XmlElement(required = true)
    protected BigInteger ausweichenakrobatikmod;
    @XmlElement(required = true)
    protected BigInteger ausweichenmod;
    @XmlElement(required = true)
    protected BigInteger geschwindigkeitinklbe;
    @XmlElement(required = true)
    protected BigInteger ini;
    protected Ruestungeinfach ruestungeinfach;
    protected Ruestungzonen ruestungzonen;
    @XmlElement(required = true)
    protected Nahkampfwaffen nahkampfwaffen;
    @XmlElement(required = true)
    protected Fernkampfwaffen fernkampfwaffen;
    @XmlElement(required = true)
    protected Schilder schilder;
    @XmlElement(required = true)
    protected Ruestungen ruestungen;
    @XmlAttribute(name = "inbenutzung", required = true)
    protected boolean inbenutzung;
    @XmlAttribute(name = "nr", required = true)
    protected BigInteger nr;
    @XmlAttribute(name = "tzm", required = true)
    protected boolean tzm;
    @XmlAttribute(name = "defaultrsmodel", required = true)
    protected boolean defaultrsmodel;

    /**
     * Gets the value of the raufen property.
     * 
     * @return
     *     possible object is
     *     {@link Raufen }
     *     
     */
    public Raufen getRaufen() {
        return raufen;
    }

    /**
     * Sets the value of the raufen property.
     * 
     * @param value
     *     allowed object is
     *     {@link Raufen }
     *     
     */
    public void setRaufen(Raufen value) {
        this.raufen = value;
    }

    /**
     * Gets the value of the ringen property.
     * 
     * @return
     *     possible object is
     *     {@link Ringen }
     *     
     */
    public Ringen getRingen() {
        return ringen;
    }

    /**
     * Sets the value of the ringen property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ringen }
     *     
     */
    public void setRingen(Ringen value) {
        this.ringen = value;
    }

    /**
     * Gets the value of the ausweichen property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAusweichen() {
        return ausweichen;
    }

    /**
     * Sets the value of the ausweichen property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAusweichen(BigInteger value) {
        this.ausweichen = value;
    }

    /**
     * Gets the value of the ausweichenausweichenmod property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAusweichenausweichenmod() {
        return ausweichenausweichenmod;
    }

    /**
     * Sets the value of the ausweichenausweichenmod property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAusweichenausweichenmod(BigInteger value) {
        this.ausweichenausweichenmod = value;
    }

    /**
     * Gets the value of the ausweichenakrobatikmod property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAusweichenakrobatikmod() {
        return ausweichenakrobatikmod;
    }

    /**
     * Sets the value of the ausweichenakrobatikmod property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAusweichenakrobatikmod(BigInteger value) {
        this.ausweichenakrobatikmod = value;
    }

    /**
     * Gets the value of the ausweichenmod property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getAusweichenmod() {
        return ausweichenmod;
    }

    /**
     * Sets the value of the ausweichenmod property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setAusweichenmod(BigInteger value) {
        this.ausweichenmod = value;
    }

    /**
     * Gets the value of the geschwindigkeitinklbe property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getGeschwindigkeitinklbe() {
        return geschwindigkeitinklbe;
    }

    /**
     * Sets the value of the geschwindigkeitinklbe property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setGeschwindigkeitinklbe(BigInteger value) {
        this.geschwindigkeitinklbe = value;
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
     * Gets the value of the ruestungeinfach property.
     * 
     * @return
     *     possible object is
     *     {@link Ruestungeinfach }
     *     
     */
    public Ruestungeinfach getRuestungeinfach() {
        return ruestungeinfach;
    }

    /**
     * Sets the value of the ruestungeinfach property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ruestungeinfach }
     *     
     */
    public void setRuestungeinfach(Ruestungeinfach value) {
        this.ruestungeinfach = value;
    }

    /**
     * Gets the value of the ruestungzonen property.
     * 
     * @return
     *     possible object is
     *     {@link Ruestungzonen }
     *     
     */
    public Ruestungzonen getRuestungzonen() {
        return ruestungzonen;
    }

    /**
     * Sets the value of the ruestungzonen property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ruestungzonen }
     *     
     */
    public void setRuestungzonen(Ruestungzonen value) {
        this.ruestungzonen = value;
    }

    /**
     * Gets the value of the nahkampfwaffen property.
     * 
     * @return
     *     possible object is
     *     {@link Nahkampfwaffen }
     *     
     */
    public Nahkampfwaffen getNahkampfwaffen() {
        return nahkampfwaffen;
    }

    /**
     * Sets the value of the nahkampfwaffen property.
     * 
     * @param value
     *     allowed object is
     *     {@link Nahkampfwaffen }
     *     
     */
    public void setNahkampfwaffen(Nahkampfwaffen value) {
        this.nahkampfwaffen = value;
    }

    /**
     * Gets the value of the fernkampfwaffen property.
     * 
     * @return
     *     possible object is
     *     {@link Fernkampfwaffen }
     *     
     */
    public Fernkampfwaffen getFernkampfwaffen() {
        return fernkampfwaffen;
    }

    /**
     * Sets the value of the fernkampfwaffen property.
     * 
     * @param value
     *     allowed object is
     *     {@link Fernkampfwaffen }
     *     
     */
    public void setFernkampfwaffen(Fernkampfwaffen value) {
        this.fernkampfwaffen = value;
    }

    /**
     * Gets the value of the schilder property.
     * 
     * @return
     *     possible object is
     *     {@link Schilder }
     *     
     */
    public Schilder getSchilder() {
        return schilder;
    }

    /**
     * Sets the value of the schilder property.
     * 
     * @param value
     *     allowed object is
     *     {@link Schilder }
     *     
     */
    public void setSchilder(Schilder value) {
        this.schilder = value;
    }

    /**
     * Gets the value of the ruestungen property.
     * 
     * @return
     *     possible object is
     *     {@link Ruestungen }
     *     
     */
    public Ruestungen getRuestungen() {
        return ruestungen;
    }

    /**
     * Sets the value of the ruestungen property.
     * 
     * @param value
     *     allowed object is
     *     {@link Ruestungen }
     *     
     */
    public void setRuestungen(Ruestungen value) {
        this.ruestungen = value;
    }

    /**
     * Gets the value of the inbenutzung property.
     * 
     */
    public boolean isInbenutzung() {
        return inbenutzung;
    }

    /**
     * Sets the value of the inbenutzung property.
     * 
     */
    public void setInbenutzung(boolean value) {
        this.inbenutzung = value;
    }

    /**
     * Gets the value of the nr property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNr() {
        return nr;
    }

    /**
     * Sets the value of the nr property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNr(BigInteger value) {
        this.nr = value;
    }

    /**
     * Gets the value of the tzm property.
     * 
     */
    public boolean isTzm() {
        return tzm;
    }

    /**
     * Sets the value of the tzm property.
     * 
     */
    public void setTzm(boolean value) {
        this.tzm = value;
    }

    /**
     * Gets the value of the defaultrsmodel property.
     * 
     */
    public boolean isDefaultrsmodel() {
        return defaultrsmodel;
    }

    /**
     * Sets the value of the defaultrsmodel property.
     * 
     */
    public void setDefaultrsmodel(boolean value) {
        this.defaultrsmodel = value;
    }

}
