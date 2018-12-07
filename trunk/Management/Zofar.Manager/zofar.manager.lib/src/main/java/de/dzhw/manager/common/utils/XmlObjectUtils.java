package de.dzhw.manager.common.utils;

import java.io.Serializable;
import java.lang.reflect.Field;

import org.apache.xmlbeans.SchemaType;
import org.apache.xmlbeans.SchemaTypeLoader;
import org.apache.xmlbeans.XmlBeans;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.apache.xmlbeans.impl.schema.SchemaTypeLoaderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlObjectUtils implements Serializable {

	private static final long serialVersionUID = 3903384647745735454L;

	private static final Logger LOGGER = LoggerFactory
			.getLogger(XmlObjectUtils.class);

	private static final XmlObjectUtils INSTANCE = new XmlObjectUtils();

	private XmlObjectUtils() {
		super();
	}

	public static XmlObjectUtils getInstance() {
		return INSTANCE;
	}

	public <T extends XmlObject> T importXml(final SchemaType type,
			final String xmlCode) {
		try {
			return importXmlWithException(type,xmlCode);
		} catch (XmlException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends XmlObject> T importXmlWithException(final SchemaType type,
			final String xmlCode) throws XmlException {
			final XmlObject back = XmlObject.Factory.parse(xmlCode);
			return (T) type.getJavaClass().cast(back.changeType(type));
	}

	public SchemaType retrieveSchemaType(final Class<? extends XmlObject> clazz) {
		SchemaType type = null;
		try {
			Field field = clazz.getField("type");
			if ((field != null)
					&& (field.getType().isAssignableFrom(SchemaType.class))
					&& (java.lang.reflect.Modifier.isStatic(field
							.getModifiers()))
					&& (java.lang.reflect.Modifier
							.isFinal(field.getModifiers()))) {
				type = (SchemaType) field.get(clazz);
			}
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return type;
	}

	public <T extends XmlObject> T importXml(
			final Class<? extends XmlObject> clazz, final String xmlCode) {
		SchemaType type = retrieveSchemaType(clazz);
		// try {
		// Field field = clazz.getField("type");
		// if ((field != null)
		// && (field.getType().isAssignableFrom(SchemaType.class))
		// && (java.lang.reflect.Modifier.isStatic(field
		// .getModifiers()))
		// && (java.lang.reflect.Modifier
		// .isFinal(field.getModifiers()))) {
		// type = (SchemaType) field.get(clazz);
		// }
		// } catch (IllegalArgumentException e) {
		// e.printStackTrace();
		// } catch (IllegalAccessException e) {
		// e.printStackTrace();
		// } catch (NoSuchFieldException e) {
		// e.printStackTrace();
		// } catch (SecurityException e) {
		// e.printStackTrace();
		// }

		if (type != null)
			return importXml(type, xmlCode);
		return null;
	}

	public String export(final XmlObject object) {
		return object.toString();
	}

	@SuppressWarnings("unchecked")
	public <T extends XmlObject> T copy(T object) {
		return (T) object.copy();
	}

	public boolean validate(final SchemaType type, final String xmlCode) {
		try {
			final XmlObject tmp = this.importXmlWithException(type, xmlCode);
			return true;
		} catch (XmlException e) {
			e.printStackTrace();
		}
		return false;
	}

}
