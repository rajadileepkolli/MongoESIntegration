package com.digitalbridge.response;

/**
 * <p>
 * ElasticSearchStats class.
 * </p>
 *
 * @author rajakolli
 * @version 1:0
 */
public class ElasticSearchStats {

    private String docCount;
    private String docDeleted;
    private String storeSizeInBytes;

    /**
     * <p>
     * Getter for the field <code>docCount</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDocCount() {
        return docCount;
    }

    /**
     * <p>
     * Setter for the field <code>docCount</code>.
     * </p>
     *
     * @param docCount a {@link java.lang.String} object.
     */
    public void setDocCount(String docCount) {
        this.docCount = docCount;
    }

    /**
     * <p>
     * Getter for the field <code>docDeleted</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getDocDeleted() {
        return docDeleted;
    }

    /**
     * <p>
     * Setter for the field <code>docDeleted</code>.
     * </p>
     *
     * @param docDeleted a {@link java.lang.String} object.
     */
    public void setDocDeleted(String docDeleted) {
        this.docDeleted = docDeleted;
    }

    /**
     * <p>
     * Getter for the field <code>storeSizeInBytes</code>.
     * </p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getStoreSizeInBytes() {
        return storeSizeInBytes;
    }

    /**
     * <p>
     * Setter for the field <code>storeSizeInBytes</code>.
     * </p>
     *
     * @param storeSizeInBytes a {@link java.lang.String} object.
     */
    public void setStoreSizeInBytes(String storeSizeInBytes) {
        this.storeSizeInBytes = storeSizeInBytes;
    }

}
