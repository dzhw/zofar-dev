package de.his.zofar.persistence.surveyengine.entities;

import static javax.persistence.FetchType.EAGER;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OrderColumn;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import de.his.zofar.persistence.common.entities.BaseEntity;

/**
 * Entity which stores a sorted list of faces component uids belonging to one
 * parent uid.
 * 
 * @author Reitmann
 */
@Entity
@Table(name = "sorting", uniqueConstraints = { @UniqueConstraint(
        columnNames = { "participant_id", "parentuid" }) })
@SequenceGenerator(initialValue = 1, allocationSize = 1,
        name = "primary_key_generator", sequenceName = "SEQ_SORTING_ID")
public class Sorting extends BaseEntity {

    /**
     * The participant for which the faces components were sorted and rendered.
     */
    @ManyToOne(optional = false)
    private ParticipantEntity participant;

    /**
     * The uid of the parent component.
     */
    @Column(nullable = false)
    private String parentUID;

    /**
     * The ordered list of uids of faces components.
     */
    @ElementCollection(fetch = EAGER)
    @OrderColumn
    private List<String> sortedChildrenUIDs;

    public Sorting() {
        super();
    }

    public String getParentUID() {
        return this.parentUID;
    }

    public ParticipantEntity getParticipant() {
        return this.participant;
    }

    public List<String> getSortedChildrenUIDs() {
        return this.sortedChildrenUIDs;
    }

    public void setParentUID(final String parentUID) {
        this.parentUID = parentUID;
    }

    public void setParticipant(final ParticipantEntity participant) {
        this.participant = participant;
    }

    public void setSortedChildrenUIDs(final List<String> sortedChildrenUIDs) {
        this.sortedChildrenUIDs = sortedChildrenUIDs;
    }

}
