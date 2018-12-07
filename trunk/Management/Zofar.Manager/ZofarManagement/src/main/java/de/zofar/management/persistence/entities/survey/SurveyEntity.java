package de.zofar.management.persistence.entities.survey;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Id;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import de.zofar.management.persistence.entities.AbstractEntity;


/**
 * @author dick
 * The persistent class for the survey database table.
 * 
 */
@Entity
@Table(name = "survey")
@SequenceGenerator(initialValue = 1, allocationSize = 1, name = "primary_key_generator", sequenceName = "seq_survey_id")
public class SurveyEntity extends AbstractEntity {

    @Column(nullable = false, unique = true)
    private String name;



    /**
     * 
     */
    public SurveyEntity() {
        super();
    }

    /**
     * @param name
     */
    public SurveyEntity(final String name) {
        super();
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

   
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SurveyEntity other = (SurveyEntity) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "SurveyEntity [name=" + name + "]";
    }

}

