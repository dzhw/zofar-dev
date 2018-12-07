package de.zofar.management.service.model.search;

import java.io.Serializable;

/**
 * @author dick
 * 
 */
public class SurveySearch implements Serializable {


    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            the name to set
     */
    public void setName(final String name) {
        this.name = name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SurveySearch [name=" + name + "]";
    }

}

