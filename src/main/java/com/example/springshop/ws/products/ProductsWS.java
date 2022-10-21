package com.example.springshop.ws.products;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "productsWS", propOrder = {
        "id",
        "title",
        "price"
})
public class ProductsWS {

    protected long id;
    @XmlElement(required = true)
    protected String title;
    protected double price;

    /**
     * Gets the value of the id property.
     *
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     *
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Gets the value of the title property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setTitle(String value) {
        this.title = value;
    }

    /**
     * Gets the value of the price property.
     *
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the value of the price property.
     *
     */
    public void setPrice(double value) {
        this.price = value;
    }

}
