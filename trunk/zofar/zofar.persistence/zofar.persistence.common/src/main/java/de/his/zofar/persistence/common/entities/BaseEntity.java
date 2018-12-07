package de.his.zofar.persistence.common.entities;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "primary_key_generator")
    private Long id;

    @Version
    private int version;

    // @Temporal(TIMESTAMP)
    // private Date modifiedAt;

    // private String modifiedBy;

    public Long getId() {
        return this.id;
    }

    // public Date getModifiedAt() {
    // return this.modifiedAt;
    // }
    //
    // public String getModifiedBy() {
    // return this.modifiedBy;
    // }

    public int getVersion() {
        return this.version;
    }

    // @PrePersist
    // @PreUpdate
    // public void modify() {
    // this.setModifiedAt(new Date());
    // }

    public void setId(final Long id) {
        this.id = id;
    }

    // public void setModifiedAt(final Date modifiedAt) {
    // this.modifiedAt = modifiedAt;
    // }
    //
    // public void setModifiedBy(final String modifiedBy) {
    // this.modifiedBy = modifiedBy;
    // }

    public void setVersion(final int version) {
        this.version = version;
    }
}
