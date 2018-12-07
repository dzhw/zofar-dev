package de.his.zofar.persistence.survey.entities;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import de.his.zofar.persistence.common.entities.BaseEntity;

@Entity
@SequenceGenerator(initialValue = 1, name = "primary_key_generator",
sequenceName = "SEQ_SURVEY_ID")
public class SurveyEntity extends BaseEntity {

    @Column(unique = true, nullable = false)
    private String name;

    @OneToMany(mappedBy = "survey", cascade = ALL)
    private List<ParticipantEntity> participants;

    @Enumerated(STRING)
    @Basic(optional = false)
    private SurveyState state;

    public SurveyEntity() {
        super();
    }

    public SurveyEntity(final String name) {
        super();
        this.name = name;
        this.state = SurveyState.CREATED;
    }

    public void addParticipant(final ParticipantEntity participant) {
        if (this.participants == null) {
            this.participants = new ArrayList<ParticipantEntity>();
        }
        this.participants.add(participant);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SurveyEntity other = (SurveyEntity) obj;
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public String getName() {
        return this.name;
    }

    public List<ParticipantEntity> getParticipants() {
        return this.participants;
    }

    public SurveyState getState() {
        return this.state;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        return result;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setParticipants(final List<ParticipantEntity> participants) {
        this.participants = participants;
    }

    public void setState(final SurveyState state) {
        if (this.state != null) {
            this.state.isValidSuccessor(state);
        }
        this.state = state;
    }

}