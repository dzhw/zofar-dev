package de.zofar.management.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;


/**
 * @author dick
 */
@Service
public abstract class AbstractService {


    /**
     * map entity page to model page.
     * 
     * @param <E>
     *            the entity class
     * @param <T>
     *            the model class
     * 
     * @param entityPage
     * @param targetClass
     * @param pageable
     * @return
     */
//    public <E, T> Page<T> mapPage(final Page<E> entityPage,
//            final Class<T> targetClass, final Pageable pageable) {
//        final List<T> content = new ArrayList<>();
//
//        for (final E entity : entityPage.getContent()) {
//            content.add(mapper.map(entity, targetClass));
//        }
//
//        final Page<T> mapped = new PageDto<>(content, pageable,
//                entityPage.getTotalElements());
//
//        return mapped;
//    }

    public <T, U> List<U> mapList(final List<T> source, final Class<U> destType) {

        final List<U> dest = new ArrayList<U>();

        for (final T element : source) {
//            dest.add(mapper.map(element, destType));
        }

        return dest;
    }

}
