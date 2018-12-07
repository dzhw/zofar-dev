/**
 *
 */
package de.his.zofar.service.surveyengine.mapper;

import org.dozer.loader.api.BeanMappingBuilder;
import org.dozer.loader.api.FieldsMappingOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.his.zofar.persistence.surveyengine.entities.ParticipantEntity;
import de.his.zofar.persistence.surveyengine.entities.SurveyDataEntity;
import de.his.zofar.service.surveyengine.model.Participant;
import de.his.zofar.service.surveyengine.model.SurveyData;

/**
 * @author le
 *
 */
public class PersistentMapMapper extends BeanMappingBuilder {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(PersistentMapMapper.class);

    /*
     * (non-Javadoc)
     *
     * @see org.dozer.loader.api.BeanMappingBuilder#configure()
     */
    @Override
    protected void configure() {
        LOGGER.info("adding custom mapper for persistent maps on participant entity");
        mapping(ParticipantEntity.class, Participant.class).fields(
                "surveyData", "surveyData",
                FieldsMappingOptions.hintA(SurveyDataEntity.class),
                FieldsMappingOptions.hintB(SurveyData.class));
    }

}
