package de.zofar.management.persistence.daos.survey;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.zofar.management.persistence.entities.survey.SurveyEntity;


/**
 * @author dick
 * 
 */
@Repository
public interface SurveyDao extends JpaRepository<SurveyEntity, Long> {

    List<SurveyEntity> findByNameLikeOrderByIdAsc(String name);

}

