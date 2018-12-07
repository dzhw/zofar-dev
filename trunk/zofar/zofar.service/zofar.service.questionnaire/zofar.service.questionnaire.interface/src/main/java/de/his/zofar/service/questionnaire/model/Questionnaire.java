/**
 * 
 */
package de.his.zofar.service.questionnaire.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.his.zofar.service.common.model.BaseDTO;


/**
 * @author le
 * 
 */
public class Questionnaire extends BaseDTO implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 8614891896303984065L;
    private UUID uuid;
    private List<UUID> pageUuids;
    private UUID firstPageUuid;

    /**
     * 
     */
    public Questionnaire() {
        super();
        uuid = UUID.randomUUID();
    }

    /**
     * @return the uuid
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * @param uuid
     *            the uuid to set
     */
    public void setUuid(final UUID name) {
        this.uuid = name;
    }

    /**
     * @return the pageUuids
     */
    public List<UUID> getPageUuids() {
        return pageUuids;
    }

    /**
     * @param pageUuids
     *            the pageUuids to set
     */
    public void setPageUuids(final List<UUID> pages) {
        // first page in list is the first page
        if (pages != null && pages.size() > 0) {
            firstPageUuid = pages.get(0);
        }
        this.pageUuids = pages;
    }

    /**
     * @param page
     */
    public void addPage(final QuestionnairePage page) {
        if (pageUuids == null) {
            pageUuids = new ArrayList<UUID>();
        }
        // the first page to add is the first page
        if (pageUuids.size() == 0) {
            firstPageUuid = page.getUuid();
        }
        pageUuids.add(page.getUuid());

        // set back reference
        page.setQuestionnaire(this);
    }

    /**
     * @return the firstPageUuid
     */
    public UUID getFirstPageUuid() {
        return firstPageUuid;
    }

    /**
     * @param firstPageUuid
     *            the firstPageUuid to set
     */
    public void setFirstPageUuid(final UUID firstPage) {
        this.firstPageUuid = firstPage;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Questionnaire other = (Questionnaire) obj;
        if (uuid == null) {
            if (other.uuid != null)
                return false;
        } else if (!uuid.equals(other.uuid))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "QuestionnaireDTO [uuid=" + uuid + "]";
    }
}
