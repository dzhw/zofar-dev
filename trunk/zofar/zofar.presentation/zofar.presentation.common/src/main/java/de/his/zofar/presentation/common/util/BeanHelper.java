package de.his.zofar.presentation.common.util;

import javax.faces.context.FacesContext;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.jsf.FacesContextUtils;

/**
 * Help resolving bean instances from the FacesContext.
 * 
 * Injection should be preferred over this helper method, however since we need
 * a way to separate the session scoped beans from the singleton (service) beans
 * this helper is used in order to be able to call service methods from a
 * session scoped bean.
 * 
 * @author Reitmann
 */
public class BeanHelper {

    /**
     * Get a reference to the given bean from the FacesContext.
     * 
     * @param clazz
     *            The type of the bean
     * @return a reference to the given bean from the FacesContext.
     */
    public static <T> T findBean(final Class<T> beanClazz) {
        final WebApplicationContext webContext = FacesContextUtils
                .getRequiredWebApplicationContext(FacesContext
                        .getCurrentInstance());
        return webContext.getBean(beanClazz);
    }
}
