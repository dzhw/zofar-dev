package eu.dzhw.zofar.management.utils.reflection;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

public class ReflectionClient {

	/** The Constant INSTANCE. */
	private static final ReflectionClient INSTANCE = new ReflectionClient();

	/** The Constant LOGGER. */
	private final static Logger LOGGER = LoggerFactory.getLogger(ReflectionClient.class);

	/**
	 * Instantiates a new loader client.
	 */
	private ReflectionClient() {
		super();
	}

	/**
	 * Gets the single instance of LoaderClient.
	 * 
	 * @return single instance of LoaderClient
	 */
	public static synchronized ReflectionClient getInstance() {
		return INSTANCE;
	}

	public List<String> getFields(final Class clazz) {
		if (clazz == null)
			return null;

		Field[] fields = FieldUtils.getAllFields(clazz);
		if (fields != null) {
			final List<String> back = new ArrayList<String>();
			for (final Field field : fields) {
				final Method getter = this.getGetter(clazz, field.getName());
				final Method setter = this.getSetter(clazz, field);

				// System.out.println("[DEBUG] field : "+field.getName()+" getter : "+getter+"
				// setter : "+setter);

				if ((getter != null) && (setter != null))
					back.add(field.getName());
			}
			return back;

		}
		return null;
	}

	// private List<Field> getFields(final Object obj) {
	// if (obj == null)
	// return null;
	//
	// Field[] fields = FieldUtils.getAllFields(obj.getClass());
	// if (fields != null)
	// return Arrays.asList(fields);
	// return null;
	// }

	private Method getGetter(final Class<? extends Object> clazz, final String name) {
		if (clazz == null)
			return null;
		if (name == null)
			return null;

		Method method = MethodUtils.getMatchingAccessibleMethod(clazz, "get" + StringUtils.capitalize(name),
				new Class[] {});
		if (method != null) {
			return method;
		}
		return null;
	}

	private Method getSetter(final Class<? extends Object> clazz, final Field field) {
		if (clazz == null)
			return null;
		if (field == null)
			return null;

		Method method = MethodUtils.getMatchingAccessibleMethod(clazz, "set" + StringUtils.capitalize(field.getName()),
				new Class[] { field.getType() });
		if (method != null) {
			return method;
		}
		return null;
	}

	public void set(final Object obj, final String field, final Object value)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		MethodUtils.invokeMethod(obj, "set" + StringUtils.capitalize(field), value);
	}

	public Object get(final Object obj, final String field)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return MethodUtils.invokeMethod(obj, "get" + StringUtils.capitalize(field), new Object[] {});
	}

	public Class<? extends Object> getPropertyClass(Class<? extends Object> clazz, String property) {
		final Method method = getGetter(clazz, property);
		if (method != null)
			return method.getReturnType();
		return null;
	}

	public <T> T cast(final Object obj, Class<T> clazz) {
		if (obj == null)
			return null;
		return clazz.cast(obj);
	}

	public <T> List<Class<? extends T>> getClassesByParentClass(final Package root, Class<T> parentClass)
			throws ClassNotFoundException {
		if (root == null)
			return null;
		if (parentClass == null)
			return null;

		// create scanner and disable default filters (that is the 'false' argument)
		final ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(
				false);
		// add include filters which matches all the classes (or use your own)
		provider.addIncludeFilter(new AssignableTypeFilter(parentClass));

		// get matching classes defined in the package
		final Set<BeanDefinition> classes = provider.findCandidateComponents(root.getName());

		final List<Class<? extends T>> back = new ArrayList<Class<? extends T>>();

		// this is how you can load the class type from BeanDefinition instance
		for (BeanDefinition bean : classes) {
			@SuppressWarnings("unchecked")
			Class<? extends T> clazz = (Class<? extends T>) Class.forName(bean.getBeanClassName());
			back.add(clazz);
		}

		return back;
	}

	public List<Class> getClassesFromJar(File jar) throws Exception {
		List<Class> classes = new ArrayList<Class>();
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(jar);
			Enumeration<JarEntry> en = jarFile.entries();
			while (en.hasMoreElements()) {
				JarEntry entry = en.nextElement();
				String entryName = entry.getName();
				if (entryName != null && entryName.endsWith(".class")) {
					try {
						Class<?> entryClass = Class
								.forName(entryName.substring(0, entryName.length() - 6).replace('/', '.'));
						if (entryClass != null)
							classes.add(entryClass);
					} catch (Throwable e) {

					}
				}
			}
			return classes;
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if (jarFile != null)
					jarFile.close();
			} catch (Exception e) {
				throw e;
			}
		}
	}
}
