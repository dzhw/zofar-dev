package de.zofar.management.service.model.project;


import de.zofar.management.service.model.AbstractModel;

/**
 * @author dick
 * 
 */
public class Survey extends AbstractModel {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7958785018120509359L;

	private String name;

    /**
     * 
     */
    public Survey() {
        super();
    }

    /**
     * @param name
     */
    public Survey(final String name) {
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
        final Survey other = (Survey) obj;
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
        return "Survey [name=" + name + "]";
    }

}
