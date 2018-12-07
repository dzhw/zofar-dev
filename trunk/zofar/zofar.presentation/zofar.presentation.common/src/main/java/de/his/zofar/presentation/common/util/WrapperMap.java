package de.his.zofar.presentation.common.util;


import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Set;

/**
 * Pretty crank Hack for using parameterized value expression calls.
 * @author meisner
 * 
 */
@Deprecated
public class WrapperMap<K, V> extends AbstractMap<K, V> {

	private Object caller;
	private Method function;

	public WrapperMap(String functionStr, Object caller, Class<K> keyClass) {
		super();
		this.caller = caller;

		@SuppressWarnings("rawtypes")
		Class[] parameterTypes = new Class[1];
		parameterTypes[0] = keyClass;

		try {
			function = caller.getClass().getDeclaredMethod(functionStr, parameterTypes);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return null;
	}

	@Override
	public V get(Object key) {
		if (key == null) return null;
		if (caller == null) return null;
		if (function == null) return null;
		try {
			@SuppressWarnings("unchecked")
            V result = (V) function.invoke(caller, key);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.get(key);
	}
}
