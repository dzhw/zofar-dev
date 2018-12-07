package de.zofar.management.service.model;


import java.io.Serializable;

/**
 * @author dick
 * 
 */
public abstract class AbstractModel implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 9168410987056794307L;

    private Long id;

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            the id to set
     */
    public void setId(final Long id) {
        this.id = id;
    }



}

