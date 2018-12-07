/**
 *
 */
package de.his.zofar.service.common.model;

import java.io.Serializable;


/**
 * @author Reitmann
 *
 */
// @DTOEntityMapping(entity = BaseEntity.class)
public abstract class BaseDTO implements Serializable{

    /**
     *
     */
    private static final long serialVersionUID = 1237540522009651391L;

    private Long id;

    private int version;

    public Long getId() {
        return this.id;
    }

    public int getVersion() {
        return this.version;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public void setVersion(final int version) {
        this.version = version;
    }

}
