package de.his.zofar.service.common.mapper;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.dozer.DozerEventListener;
import org.dozer.event.DozerEvent;

import de.his.zofar.persistence.common.entities.BaseEntity;

/**
 * Dozer Event Listener which flushes the EntityManager before
 * an entity is mapped to a DTO.
 * 
 * @author Reitmann
 */
public class FlushingMappingListener implements DozerEventListener {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void mappingFinished(final DozerEvent event) {
        // Nothing todo
    }

    /**
     * Flush EntityManager before entity is mapped to DTO.
     */
    @Override
    public void mappingStarted(final DozerEvent event) {
        if (BaseEntity.class.isAssignableFrom(event.getSourceObject()
                .getClass())) {
            this.entityManager.flush();
        }
    }

    @Override
    public void postWritingDestinationValue(final DozerEvent event) {
        // Nothing todo
    }

    @Override
    public void preWritingDestinationValue(final DozerEvent event) {
        // Nothing todo
    }

}
