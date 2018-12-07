package de.his.zofar.service.common.util;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.AbstractMap;
import java.util.Set;

/**
 * Pretty crank Hack for using parameterized value expression calls.
 * @author meisner
 *
 */

public class FunctionWrapper<K, V> extends AbstractMap<K, V> implements Serializable {

	private static final long serialVersionUID = 643770370456857895L;
	private final Object caller;
    private transient Method function;

	public FunctionWrapper(final String functionStr, final Object caller, final Class<K> keyClass) {
		super();
		this.caller = caller;

		@SuppressWarnings("rawtypes")
        final
		Class[] parameterTypes = new Class[1];
		parameterTypes[0] = keyClass;

		try {
			function = caller.getClass().getDeclaredMethod(functionStr, parameterTypes);
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
		} catch (final SecurityException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return null;
	}

	@Override
	public V get(final Object key) {
		if (key == null) return null;
		if (caller == null) return null;
		if (function == null) return null;
		try {
			@SuppressWarnings("unchecked")
            final
            V result = (V) function.invoke(caller, key);
			return result;
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return super.get(key);
	}

}
