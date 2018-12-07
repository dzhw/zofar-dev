package de.zofar.management.persistence.daos.project;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import de.zofar.management.persistence.entities.project.ProjectEntity;


/**
 * @author dick
 * 
 */
@Repository
public interface ProjectDao extends JpaRepository<ProjectEntity, Long> {

    List<ProjectEntity> findByNameLikeOrderByIdAsc(String name);

}

