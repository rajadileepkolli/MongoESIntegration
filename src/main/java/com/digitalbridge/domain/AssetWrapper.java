package com.digitalbridge.domain;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.digitalbridge.annotation.CascadeSaveList;
import com.digitalbridge.service.impl.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * AssetWrapper class.
 * <p>
 *
 * @author rajakolli
 * @version 1: 0
 */
@JsonAutoDetect
@Document(collection = "assetwrapper")
public class AssetWrapper {

    @Id
    private String id;

    @NotNull
    private String orgAssetId;

    @Field("aName")
    @Indexed(direction = IndexDirection.ASCENDING)
    @NotNull
    @Size(min = 2)
    private String assetName;

    @NotNull
    private String borough;

    @NotNull
    private String cuisine;

    private String building;

    /**
     * {@code location} is stored in GeoJSON format.
     * 
     * <pre>
     * <code>
     * {
     *   "type" : "Point",
     *   "coordinates" : [ x, y ]
     * }
     * </code>
     * </pre>
     */
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    private String street;

    private String zipcode;

    @CascadeSaveList
    @DBRef
    private List<Notes> notes;

    @Version
    private Long version;

    @CreatedBy
    private String createdBy;

    @CreatedDate
    private Date createdDate;

    @LastModifiedBy
    private String lastModifiedBy;

    @LastModifiedDate
    @Field("lDate")
    private Date lastmodifiedDate;

    /**
     * Getter for the field <code>id</code>.
     * <p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getId() {
        return id;
    }

    /**
     * Setter for the field <code>id</code>.
     * <p>
     *
     * @param id a {@link java.lang.String} object.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter for the field <code>orginalAssetId</code>.
     * <p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getOrgAssetId() {
        return orgAssetId;
    }

    /**
     * <p>
     * Setter for the field <code>orginalAssetId</code>.
     * </p>
     *
     * @param orgAssetId a {@link java.lang.String} object.
     */
    public void setOrgAssetId(String orgAssetId) {
        this.orgAssetId = orgAssetId;
    }

    /**
     * <p>
     * Getter for the field <code>assetName</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAssetName() {
        return assetName;
    }

    /**
     * <p>
     * Setter for the field <code>assetName</code>.
     * </p>
     *
     * @param assetName a {@link java.lang.String} object.
     */
    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    /**
     * <p>
     * Getter for the field <code>borough</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBorough() {
        return borough;
    }

    /**
     * <p>
     * Setter for the field <code>borough</code>.
     * </p>
     *
     * @param borough a {@link java.lang.String} object.
     */
    public void setBorough(String borough) {
        this.borough = borough;
    }

    /**
     * <p>
     * Getter for the field <code>cuisine</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCuisine() {
        return cuisine;
    }

    /**
     * <p>
     * Setter for the field <code>cuisine</code>.
     * </p>
     *
     * @param cuisine a {@link java.lang.String} object.
     */
    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    /**
     * <p>Getter for the field <code>building</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getBuilding() {
        return building;
    }

    /**
     * <p>Setter for the field <code>building</code>.</p>
     *
     * @param building a {@link java.lang.String} object.
     */
    public void setBuilding(String building) {
        this.building = building;
    }

    /**
     * <p>Getter for the field <code>location</code>.</p>
     *
     * @return a {@link org.springframework.data.mongodb.core.geo.GeoJsonPoint} object.
     */
    public GeoJsonPoint getLocation() {
        return location;
    }

    /**
     * <p>Setter for the field <code>location</code>.</p>
     *
     * @param location a {@link org.springframework.data.mongodb.core.geo.GeoJsonPoint} object.
     */
    public void setLocation(GeoJsonPoint location) {
        this.location = location;
    }

    /**
     * <p>Getter for the field <code>street</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getStreet() {
        return street;
    }

    /**
     * <p>Setter for the field <code>street</code>.</p>
     *
     * @param street a {@link java.lang.String} object.
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * <p>Getter for the field <code>zipcode</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * <p>Setter for the field <code>zipcode</code>.</p>
     *
     * @param zipcode a {@link java.lang.String} object.
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * <p>
     * Getter for the field <code>notes</code>.
     * </p>
     *
     * @return a {@link java.util.List} object.
     */
    public List<Notes> getNotes() {
        return notes;
    }

    /**
     * <p>
     * Setter for the field <code>notes</code>.
     * </p>
     *
     * @param notes a {@link java.util.List} object.
     */
    public void setNotes(List<Notes> notes) {
        this.notes = notes;
    }

    /**
     * <p>
     * Getter for the field <code>version</code>.
     * </p>
     *
     * @return a {@link java.lang.Long} object.
     */
    public Long getVersion() {
        return version;
    }

    /**
     * <p>
     * Setter for the field <code>version</code>.
     * </p>
     *
     * @param version a {@link java.lang.Long} object.
     */
    public void setVersion(Long version) {
        this.version = version;
    }

    /**
     * <p>
     * Getter for the field <code>createdBy</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * <p>
     * Setter for the field <code>createdBy</code>.
     * </p>
     *
     * @param createdBy a {@link java.lang.String} object.
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * <p>
     * Getter for the field <code>createdDate</code>.
     * </p>
     *
     * @return a {@link java.util.Date} object.
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * <p>
     * Setter for the field <code>createdDate</code>.
     * </p>
     *
     * @param createdDate a {@link java.util.Date} object.
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * <p>
     * Getter for the field <code>lastModifiedBy</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    /**
     * <p>
     * Setter for the field <code>lastModifiedBy</code>.
     * </p>
     *
     * @param lastModifiedBy a {@link java.lang.String} object.
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    /**
     * <p>
     * Getter for the field <code>lastmodifiedDate</code>.
     * </p>
     *
     * @return a {@link java.util.Date} object.
     */
    @JsonSerialize(using = JsonDateSerializer.class)
    public Date getLastmodifiedDate() {
        return lastmodifiedDate;
    }

    /**
     * <p>
     * Setter for the field <code>lastmodifiedDate</code>.
     * </p>
     *
     * @param lastmodifiedDate a {@link java.util.Date} object.
     */
    public void setLastmodifiedDate(Date lastmodifiedDate) {
        this.lastmodifiedDate = lastmodifiedDate;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
