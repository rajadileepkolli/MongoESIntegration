package com.digitalbridge.request;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * <p>
 * Sample class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
@XmlRootElement
@XmlType(propOrder = { "username", "id" })
@JsonPropertyOrder({ "username", "id" })
public class Sample implements Serializable {
    private static final long serialVersionUID = -4802642363834483466L;

    // @XmlElementWrapper(name = "collectionWrapper")// used for List
    private Integer id;

    private String username;

    /**
     * <p>
     * Getter for the field <code>id</code>.
     * </p>
     *
     * @return a {@link java.lang.Integer} object.
     */
    @XmlElement
    public Integer getId() {
        return id;
    }

    /**
     * <p>
     * Setter for the field <code>id</code>.
     * </p>
     *
     * @param id a {@link java.lang.Integer} object.
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * <p>
     * Getter for the field <code>username</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    @XmlElement
    public String getUsername() {
        return username;
    }

    /**
     * <p>
     * Setter for the field <code>username</code>.
     * </p>
     *
     * @param username a {@link java.lang.String} object.
     */
    public void setUsername(String username) {
        this.username = username;
    }
}
