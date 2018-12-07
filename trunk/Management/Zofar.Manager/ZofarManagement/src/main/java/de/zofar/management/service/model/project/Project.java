package de.zofar.management.service.model.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.zofar.management.service.model.AbstractModel;


/**
 * @author dick
 * 
 */
public class Project extends AbstractModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 5333996914324145128L;

	private String name;

    private List<Survey> surveys;

    /**
     * 
     */
    public Project() {
        super();
    }

    /**
     * @param name
     */
    public Project(final String name) {
        super();
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }


    /**
     * @return the surveys
     */
    public List<Survey> getSurveys() {
        return surveys;
    }

    /**
     * java.util.
     * 
     * @param participants
     *            the participants to set
     */
    public void setSurveys(final List<Survey> surveys) {
        this.surveys = surveys;
    }

//    public void addSurveys(final Survey surveys) {
//        if (surveys == null) {
//            surys = new ArrayList<>();
//        }
//        participants.add(participant);
//        participant.setSurvey(this);
//    }



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
        final Project other = (Project) obj;
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
        return "Project [name=" + name + "]";
    }

}
