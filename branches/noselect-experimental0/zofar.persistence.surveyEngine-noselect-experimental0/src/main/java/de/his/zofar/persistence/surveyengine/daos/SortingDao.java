package de.his.zofar.persistence.surveyengine.daos;

import java.util.List;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
import de.his.zofar.persistence.surveyengine.entities.QSorting;
import de.his.zofar.persistence.surveyengine.entities.Sorting;

/**
 * Dao for access to so the entities representing sorted faces components.
 *
 * @author Reitmann
 */
public interface SortingDao extends GenericCRUDDao<Sorting> {
    public static QSorting sorting = QSorting.sorting;

    public List<Sorting> findByParticipant(ParticipantEntity participant);

    public Sorting findByParticipantAndParentUID(ParticipantEntity participant,
            String parentUID);

    /*
     * Overwritten to be able to monitor run times with Jamon.
     *
     * (non-Javadoc)
     *
     * @see org.springframework.data.repository.CrudRepository#save(S)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Sorting save(Sorting sorting);
}
