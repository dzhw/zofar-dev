package de.dzhw.manager.common.utils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.impl.values.XmlAnySimpleTypeImpl;
import org.apache.xmlbeans.impl.values.XmlComplexContentImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XmlBeanUtil implements Serializable{
	
	private static final long serialVersionUID = 5753650410389233962L;
	private static final Logger LOGGER = LoggerFactory
            .getLogger(XmlBeanUtil.class);
	
	private static final XmlBeanUtil INSTANCE = new XmlBeanUtil();

	private XmlBeanUtil() {
		super();
	}
	
	public static XmlBeanUtil getInstance(){
		return INSTANCE;
	}
	
    public Map<String, Method> findSetterMethods(final XmlObject xmlObject) {
        final Method[] methods = xmlObject.getClass().getMethods();

        final Map<String, Method> setMethods = new HashMap<String, Method>();

        if (methods != null) {
            for (final Method method : methods) {
                final Class<?> declaringClass = method.getDeclaringClass();
                if ((XmlComplexContentImpl.class
                        .isAssignableFrom(declaringClass) || XmlAnySimpleTypeImpl.class
                        .isAssignableFrom(declaringClass))
                        && isSetter(method)) {
                    setMethods.put(cleanMethodName("set",method.getName()), method);
                }
            }
        }

        return setMethods;
    }
    
    public Map<String, Method> findGetterMethods(final XmlObject xmlObject) {
        final Method[] methods = xmlObject.getClass().getMethods();

        final Map<String, Method> getMethods = new HashMap<String, Method>();

        if (methods != null) {
            for (final Method method : methods) {
                final Class<?> declaringClass = method.getDeclaringClass();
                if ((XmlComplexContentImpl.class
                        .isAssignableFrom(declaringClass) || XmlAnySimpleTypeImpl.class
                        .isAssignableFrom(declaringClass))
                        && isGetter(method)) {
                	getMethods.put(cleanMethodName("get",method.getName()), method);
                }
            }
        }

        return getMethods;
    }
    
    
    private String cleanMethodName(final String prefix ,final String name){
    	if(name == null)return null;
    	String back = name;
        if(back.startsWith(prefix))back = back.substring(prefix.length());
        final char c[] = back.toCharArray();
        c[0] = Character.toLowerCase(c[0]);
        back = new String( c );
        return back;
    }
    
    /**
     * checks whether a method is a setter method according to sun conventions.
     *
     * @param method
     * @return
     */
    private boolean isSetter(final Method method) {
        return Modifier.isPublic(method.getModifiers())
                && method.getReturnType().equals(void.class)
                && method.getParameterTypes().length == 1
                && method.getName().matches("^set[A-Z].*");
    }
    
    /**
     * checks whether a method is a setter method according to sun conventions.
     *
     * @param method
     * @return
     */
    private boolean isGetter(final Method method) {
        return Modifier.isPublic(method.getModifiers())
                && !(method.getReturnType().equals(void.class))
                && method.getParameterTypes().length == 0
                && method.getName().matches("^get[A-Z].*");
    }

}
