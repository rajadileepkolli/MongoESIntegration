package com.digitalbridge.request;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@XmlRootElement
@XmlType(propOrder = { "username", "id" })
@JsonPropertyOrder({ "username", "id" })
public class Sample implements Serializable
{
    private static final long serialVersionUID = -4802642363834483466L;

    // @XmlElementWrapper(name = "collectionWrapper")// used for List
    private Integer id;

    private String username;

    @XmlElement
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    @XmlElement
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }
}
