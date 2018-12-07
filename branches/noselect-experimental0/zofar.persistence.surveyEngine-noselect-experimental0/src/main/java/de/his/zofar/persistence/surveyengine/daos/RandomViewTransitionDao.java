package de.his.zofar.persistence.surveyengine.daos;

import de.his.zofar.persistence.common.daos.GenericCRUDDao;
import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
import de.his.zofar.persistence.surveyengine.entities.RandomViewTransitionEntity;

public interface RandomViewTransitionDao extends
        GenericCRUDDao<RandomViewTransitionEntity> {

    /*
     * Overwritten to be able to monitor run times with Jamon.
     *
     * (non-Javadoc)
     *
     * @see org.springframework.data.repository.CrudRepository#save(S)
     */
    @Override
    @SuppressWarnings("unchecked")
    public RandomViewTransitionEntity save(
            RandomViewTransitionEntity randomViewTransition);

    public RandomViewTransitionEntity findByParticipantAndFromViewId(
            ParticipantEntity participant, String fromViewId);

}
