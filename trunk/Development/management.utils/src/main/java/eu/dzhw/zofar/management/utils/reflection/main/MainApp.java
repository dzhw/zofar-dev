package eu.dzhw.zofar.management.utils.reflection.main;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;

import eu.dzhw.zofar.management.utils.reflection.ReflectionClient;

public class MainApp {

	public static void main(String[] args) {
		final ReflectionClient refClient = ReflectionClient.getInstance();
		TestObj obj = new TestObj(System.currentTimeMillis());
		obj.setId(1L);
		obj.setValue("Value");
		obj.setTimestamp(new Date());

		List<String> fields = refClient.getFields(obj.getClass());

		if (fields != null) {
			for (final String field : fields) {
				System.out.println("Field : " + field);
				if (field.equals("value")){
					try {
						refClient.set(obj, field, "bla");
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

		if (fields != null) {
			for (final String field : fields) {
				try {
					System.out.println("Field : " + field + " value : " + refClient.get(obj, field));
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
