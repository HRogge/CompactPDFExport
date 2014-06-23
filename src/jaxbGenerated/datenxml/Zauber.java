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
 *         &lt;element ref="{}name"/>
 *         &lt;element ref="{}variante"/>
 *         &lt;element ref="{}namemitvariante"/>
 *         &lt;element ref="{}nameausfuehrlich"/>
 *         &lt;element ref="{}wert"/>
 *         &lt;element ref="{}spezialisierungen"/>
 *         &lt;element ref="{}probe"/>
 *         &lt;element ref="{}probenwerte"/>
 *         &lt;element ref="{}bereich"/>
 *         &lt;element ref="{}komplexität"/>
 *         &lt;element ref="{}lernkomplexität"/>
 *         &lt;element ref="{}hauszauber"/>
 *         &lt;element ref="{}hauszauberformatiert"/>
 *         &lt;element ref="{}repräsentation"/>
 *         &lt;element ref="{}merkmale"/>
 *         &lt;element ref="{}zauberdauer"/>
 *         &lt;element ref="{}kosten"/>
 *         &lt;element ref="{}reichweite"/>
 *         &lt;element ref="{}wirkungsdauer"/>
 *         &lt;element ref="{}anmerkung"/>
 *         &lt;element ref="{}quelle"/>
 *         &lt;element ref="{}kontrollwert"/>
 *         &lt;element ref="{}mr"/>
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
    "name",
    "variante",
    "namemitvariante",
    "nameausfuehrlich",
    "wert",
    "spezialisierungen",
    "probe",
    "probenwerte",
    "bereich",
    "komplexit\u00e4t",
    "lernkomplexit\u00e4t",
    "hauszauber",
    "hauszauberformatiert",
    "repr\u00e4sentation",
    "merkmale",
    "zauberdauer",
    "kosten",
    "reichweite",
    "wirkungsdauer",
    "anmerkung",
    "quelle",
    "kontrollwert",
    "mr"
})
@XmlRootElement(name = "zauber")
public class Zauber {

    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected String variante;
    @XmlElement(required = true)
    protected String namemitvariante;
    @XmlElement(required = true)
    protected String nameausfuehrlich;
    @XmlElement(required = true)
    protected BigInteger wert;
    @XmlElement(required = true)
    protected String spezialisierungen;
    @XmlElement(required = true)
    protected String probe;
    @XmlElement(required = true)
    protected String probenwerte;
    @XmlElement(required = true)
    protected String bereich;
    @XmlElement(required = true)
    protected String komplexität;
    @XmlElement(required = true)
    protected String lernkomplexität;
    protected boolean hauszauber;
    @XmlElement(required = true)
    protected String hauszauberformatiert;
    @XmlElement(required = true)
    protected String repräsentation;
    @XmlElement(required = true)
    protected String merkmale;
    @XmlElement(required = true)
    protected String zauberdauer;
    @XmlElement(required = true)
    protected String kosten;
    @XmlElement(required = true)
    protected String reichweite;
    @XmlElement(required = true)
    protected String wirkungsdauer;
    @XmlElement(required = true)
    protected String anmerkung;
    @XmlElement(required = true)
    protected Quelle quelle;
    @XmlElement(required = true)
    protected String kontrollwert;
    @XmlElement(required = true)
    protected String mr;

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
     * Gets the value of the variante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getVariante() {
        return variante;
    }

    /**
     * Sets the value of the variante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setVariante(String value) {
        this.variante = value;
    }

    /**
     * Gets the value of the namemitvariante property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNamemitvariante() {
        return namemitvariante;
    }

    /**
     * Sets the value of the namemitvariante property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNamemitvariante(String value) {
        this.namemitvariante = value;
    }

    /**
     * Gets the value of the nameausfuehrlich property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNameausfuehrlich() {
        return nameausfuehrlich;
    }

    /**
     * Sets the value of the nameausfuehrlich property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNameausfuehrlich(String value) {
        this.nameausfuehrlich = value;
    }

    /**
     * Gets the value of the wert property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getWert() {
        return wert;
    }

    /**
     * Sets the value of the wert property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setWert(BigInteger value) {
        this.wert = value;
    }

    /**
     * Gets the value of the spezialisierungen property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSpezialisierungen() {
        return spezialisierungen;
    }

    /**
     * Sets the value of the spezialisierungen property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSpezialisierungen(String value) {
        this.spezialisierungen = value;
    }

    /**
     * Gets the value of the probe property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbe() {
        return probe;
    }

    /**
     * Sets the value of the probe property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbe(String value) {
        this.probe = value;
    }

    /**
     * Gets the value of the probenwerte property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProbenwerte() {
        return probenwerte;
    }

    /**
     * Sets the value of the probenwerte property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProbenwerte(String value) {
        this.probenwerte = value;
    }

    /**
     * Gets the value of the bereich property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBereich() {
        return bereich;
    }

    /**
     * Sets the value of the bereich property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBereich(String value) {
        this.bereich = value;
    }

    /**
     * Gets the value of the komplexität property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKomplexität() {
        return komplexität;
    }

    /**
     * Sets the value of the komplexität property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKomplexität(String value) {
        this.komplexität = value;
    }

    /**
     * Gets the value of the lernkomplexität property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLernkomplexität() {
        return lernkomplexität;
    }

    /**
     * Sets the value of the lernkomplexität property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLernkomplexität(String value) {
        this.lernkomplexität = value;
    }

    /**
     * Gets the value of the hauszauber property.
     * 
     */
    public boolean isHauszauber() {
        return hauszauber;
    }

    /**
     * Sets the value of the hauszauber property.
     * 
     */
    public void setHauszauber(boolean value) {
        this.hauszauber = value;
    }

    /**
     * Gets the value of the hauszauberformatiert property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getHauszauberformatiert() {
        return hauszauberformatiert;
    }

    /**
     * Sets the value of the hauszauberformatiert property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setHauszauberformatiert(String value) {
        this.hauszauberformatiert = value;
    }

    /**
     * Gets the value of the repräsentation property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRepräsentation() {
        return repräsentation;
    }

    /**
     * Sets the value of the repräsentation property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRepräsentation(String value) {
        this.repräsentation = value;
    }

    /**
     * Gets the value of the merkmale property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerkmale() {
        return merkmale;
    }

    /**
     * Sets the value of the merkmale property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerkmale(String value) {
        this.merkmale = value;
    }

    /**
     * Gets the value of the zauberdauer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getZauberdauer() {
        return zauberdauer;
    }

    /**
     * Sets the value of the zauberdauer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setZauberdauer(String value) {
        this.zauberdauer = value;
    }

    /**
     * Gets the value of the kosten property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKosten() {
        return kosten;
    }

    /**
     * Sets the value of the kosten property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKosten(String value) {
        this.kosten = value;
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
     * Gets the value of the wirkungsdauer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWirkungsdauer() {
        return wirkungsdauer;
    }

    /**
     * Sets the value of the wirkungsdauer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWirkungsdauer(String value) {
        this.wirkungsdauer = value;
    }

    /**
     * Gets the value of the anmerkung property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAnmerkung() {
        return anmerkung;
    }

    /**
     * Sets the value of the anmerkung property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAnmerkung(String value) {
        this.anmerkung = value;
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
     * Gets the value of the kontrollwert property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKontrollwert() {
        return kontrollwert;
    }

    /**
     * Sets the value of the kontrollwert property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKontrollwert(String value) {
        this.kontrollwert = value;
    }

    /**
     * Gets the value of the mr property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMr() {
        return mr;
    }

    /**
     * Sets the value of the mr property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMr(String value) {
        this.mr = value;
    }

}
