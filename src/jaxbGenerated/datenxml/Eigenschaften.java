//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.5-b10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.11.27 at 08:14:21 AM CET 
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
 *         &lt;element ref="{}geschwindigkeit"/>
 *         &lt;element ref="{}mut"/>
 *         &lt;element ref="{}klugheit"/>
 *         &lt;element ref="{}intuition"/>
 *         &lt;element ref="{}charisma"/>
 *         &lt;element ref="{}fingerfertigkeit"/>
 *         &lt;element ref="{}gewandtheit"/>
 *         &lt;element ref="{}konstitution"/>
 *         &lt;element ref="{}koerperkraft"/>
 *         &lt;element ref="{}sozialstatus"/>
 *         &lt;element ref="{}astralenergie"/>
 *         &lt;element ref="{}attacke"/>
 *         &lt;element ref="{}ausdauer"/>
 *         &lt;element ref="{}fernkampf-basis"/>
 *         &lt;element ref="{}initiative"/>
 *         &lt;element ref="{}karmaenergie"/>
 *         &lt;element ref="{}lebensenergie"/>
 *         &lt;element ref="{}magieresistenz"/>
 *         &lt;element ref="{}parade"/>
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
    "geschwindigkeit",
    "mut",
    "klugheit",
    "intuition",
    "charisma",
    "fingerfertigkeit",
    "gewandtheit",
    "konstitution",
    "koerperkraft",
    "sozialstatus",
    "astralenergie",
    "attacke",
    "ausdauer",
    "fernkampfBasis",
    "initiative",
    "karmaenergie",
    "lebensenergie",
    "magieresistenz",
    "parade"
})
@XmlRootElement(name = "eigenschaften")
public class Eigenschaften {

    @XmlElement(required = true)
    protected Eigenschaftswerte geschwindigkeit;
    @XmlElement(required = true)
    protected Eigenschaftswerte mut;
    @XmlElement(required = true)
    protected Eigenschaftswerte klugheit;
    @XmlElement(required = true)
    protected Eigenschaftswerte intuition;
    @XmlElement(required = true)
    protected Eigenschaftswerte charisma;
    @XmlElement(required = true)
    protected Eigenschaftswerte fingerfertigkeit;
    @XmlElement(required = true)
    protected Eigenschaftswerte gewandtheit;
    @XmlElement(required = true)
    protected Eigenschaftswerte konstitution;
    @XmlElement(required = true)
    protected Eigenschaftswerte koerperkraft;
    @XmlElement(required = true)
    protected Eigenschaftswerte sozialstatus;
    @XmlElement(required = true)
    protected Eigenschaftswerteastrale astralenergie;
    @XmlElement(required = true)
    protected Eigenschaftswerte attacke;
    @XmlElement(required = true)
    protected Eigenschaftswertezukaufbar503325 ausdauer;
    @XmlElement(name = "fernkampf-basis", required = true)
    protected Eigenschaftswerte fernkampfBasis;
    @XmlElement(required = true)
    protected Eigenschaftswerte initiative;
    @XmlElement(required = true)
    protected Eigenschaftswertezukaufbar karmaenergie;
    @XmlElement(required = true)
    protected Eigenschaftswertezukaufbar503325 lebensenergie;
    @XmlElement(required = true)
    protected Eigenschaftswertezukaufbar magieresistenz;
    @XmlElement(required = true)
    protected Eigenschaftswerte parade;

    /**
     * Gets the value of the geschwindigkeit property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getGeschwindigkeit() {
        return geschwindigkeit;
    }

    /**
     * Sets the value of the geschwindigkeit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setGeschwindigkeit(Eigenschaftswerte value) {
        this.geschwindigkeit = value;
    }

    /**
     * Gets the value of the mut property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getMut() {
        return mut;
    }

    /**
     * Sets the value of the mut property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setMut(Eigenschaftswerte value) {
        this.mut = value;
    }

    /**
     * Gets the value of the klugheit property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getKlugheit() {
        return klugheit;
    }

    /**
     * Sets the value of the klugheit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setKlugheit(Eigenschaftswerte value) {
        this.klugheit = value;
    }

    /**
     * Gets the value of the intuition property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getIntuition() {
        return intuition;
    }

    /**
     * Sets the value of the intuition property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setIntuition(Eigenschaftswerte value) {
        this.intuition = value;
    }

    /**
     * Gets the value of the charisma property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getCharisma() {
        return charisma;
    }

    /**
     * Sets the value of the charisma property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setCharisma(Eigenschaftswerte value) {
        this.charisma = value;
    }

    /**
     * Gets the value of the fingerfertigkeit property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getFingerfertigkeit() {
        return fingerfertigkeit;
    }

    /**
     * Sets the value of the fingerfertigkeit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setFingerfertigkeit(Eigenschaftswerte value) {
        this.fingerfertigkeit = value;
    }

    /**
     * Gets the value of the gewandtheit property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getGewandtheit() {
        return gewandtheit;
    }

    /**
     * Sets the value of the gewandtheit property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setGewandtheit(Eigenschaftswerte value) {
        this.gewandtheit = value;
    }

    /**
     * Gets the value of the konstitution property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getKonstitution() {
        return konstitution;
    }

    /**
     * Sets the value of the konstitution property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setKonstitution(Eigenschaftswerte value) {
        this.konstitution = value;
    }

    /**
     * Gets the value of the koerperkraft property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getKoerperkraft() {
        return koerperkraft;
    }

    /**
     * Sets the value of the koerperkraft property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setKoerperkraft(Eigenschaftswerte value) {
        this.koerperkraft = value;
    }

    /**
     * Gets the value of the sozialstatus property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getSozialstatus() {
        return sozialstatus;
    }

    /**
     * Sets the value of the sozialstatus property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setSozialstatus(Eigenschaftswerte value) {
        this.sozialstatus = value;
    }

    /**
     * Gets the value of the astralenergie property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerteastrale }
     *     
     */
    public Eigenschaftswerteastrale getAstralenergie() {
        return astralenergie;
    }

    /**
     * Sets the value of the astralenergie property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerteastrale }
     *     
     */
    public void setAstralenergie(Eigenschaftswerteastrale value) {
        this.astralenergie = value;
    }

    /**
     * Gets the value of the attacke property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getAttacke() {
        return attacke;
    }

    /**
     * Sets the value of the attacke property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setAttacke(Eigenschaftswerte value) {
        this.attacke = value;
    }

    /**
     * Gets the value of the ausdauer property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswertezukaufbar503325 }
     *     
     */
    public Eigenschaftswertezukaufbar503325 getAusdauer() {
        return ausdauer;
    }

    /**
     * Sets the value of the ausdauer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswertezukaufbar503325 }
     *     
     */
    public void setAusdauer(Eigenschaftswertezukaufbar503325 value) {
        this.ausdauer = value;
    }

    /**
     * Gets the value of the fernkampfBasis property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getFernkampfBasis() {
        return fernkampfBasis;
    }

    /**
     * Sets the value of the fernkampfBasis property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setFernkampfBasis(Eigenschaftswerte value) {
        this.fernkampfBasis = value;
    }

    /**
     * Gets the value of the initiative property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getInitiative() {
        return initiative;
    }

    /**
     * Sets the value of the initiative property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setInitiative(Eigenschaftswerte value) {
        this.initiative = value;
    }

    /**
     * Gets the value of the karmaenergie property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswertezukaufbar }
     *     
     */
    public Eigenschaftswertezukaufbar getKarmaenergie() {
        return karmaenergie;
    }

    /**
     * Sets the value of the karmaenergie property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswertezukaufbar }
     *     
     */
    public void setKarmaenergie(Eigenschaftswertezukaufbar value) {
        this.karmaenergie = value;
    }

    /**
     * Gets the value of the lebensenergie property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswertezukaufbar503325 }
     *     
     */
    public Eigenschaftswertezukaufbar503325 getLebensenergie() {
        return lebensenergie;
    }

    /**
     * Sets the value of the lebensenergie property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswertezukaufbar503325 }
     *     
     */
    public void setLebensenergie(Eigenschaftswertezukaufbar503325 value) {
        this.lebensenergie = value;
    }

    /**
     * Gets the value of the magieresistenz property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswertezukaufbar }
     *     
     */
    public Eigenschaftswertezukaufbar getMagieresistenz() {
        return magieresistenz;
    }

    /**
     * Sets the value of the magieresistenz property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswertezukaufbar }
     *     
     */
    public void setMagieresistenz(Eigenschaftswertezukaufbar value) {
        this.magieresistenz = value;
    }

    /**
     * Gets the value of the parade property.
     * 
     * @return
     *     possible object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public Eigenschaftswerte getParade() {
        return parade;
    }

    /**
     * Sets the value of the parade property.
     * 
     * @param value
     *     allowed object is
     *     {@link Eigenschaftswerte }
     *     
     */
    public void setParade(Eigenschaftswerte value) {
        this.parade = value;
    }

}
