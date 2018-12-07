package de.his.zofar.generator.maven.plugin.generator.page;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import com.sun.java.jsf.composite.common.SortType;
import com.sun.java.jsf.composite.display.DisplayCalendarType;
import com.sun.java.jsf.composite.display.DisplayReloadType;
import com.sun.java.jsf.composite.display.DisplayTableType;

import de.his.zofar.xml.questionnaire.DisplayType;
import de.his.zofar.xml.questionnaire.IdentificationalType;
import eu.dzhw.zofar.xml.display.DisplayTableBodyType;
import eu.dzhw.zofar.xml.display.DisplayTableHeaderType;
import eu.dzhw.zofar.xml.display.DisplayTableItemType;
import eu.dzhw.zofar.xml.display.DisplayTableRowType;
import eu.dzhw.zofar.xml.navigation.JumperType;

public class XhtmlDisplayCreator extends AbstractXhtmlElementCreator implements IXhtmlCreator {

	private static final Logger LOGGER = LoggerFactory.getLogger(XhtmlDisplayCreator.class);

	@Override
	public void addToSection(IdentificationalType source, com.sun.java.jsf.composite.container.SectionType target,
			boolean root) throws XmlException {
		if (source == null)
			return;
		if (!(DisplayType.class).isAssignableFrom(source.getClass()))
			return;

		final DisplayType display = (DisplayType) source;
		addContainer(display, target, root);

	}

	@Override
	public void addToSort(IdentificationalType source, SortType target) throws XmlException {
		if (source == null)
			return;
		if (!(DisplayType.class).isAssignableFrom(source.getClass()))
			return;

		final DisplayType display = (DisplayType) source;

		addContainer(display, target, false);
	}

	private void addContainer(final DisplayType display,
			final com.sun.java.jsf.composite.common.IdentificationalType target, final boolean root)
			throws XmlException {
		if (target == null)
			return;
		if (display == null)
			return;

		// display attributes
		final String uid = display.getUid();

		com.sun.java.jsf.composite.display.DisplayType displayContainer = null;
		if ((com.sun.java.jsf.composite.container.SectionType.class).isAssignableFrom(target.getClass())) {
			displayContainer = ((com.sun.java.jsf.composite.container.SectionType) target).addNewContainer();
		} else if ((com.sun.java.jsf.composite.common.SortType.class).isAssignableFrom(target.getClass())) {
			displayContainer = ((com.sun.java.jsf.composite.common.SortType) target).addNewContainer();
		}

		if (displayContainer == null)
			return;
		displayContainer.setId(uid);

		// //display children
		for (final XmlObject element : display.selectPath(SELECT_PATH_IN_ORDER)) {
			if ((eu.dzhw.zofar.xml.display.impl.DisplayTableTypeImpl.class).isAssignableFrom(element.getClass())) {
				addTable((eu.dzhw.zofar.xml.display.impl.DisplayTableTypeImpl) element, displayContainer);
			} else if ((eu.dzhw.zofar.xml.display.impl.DisplayTextTypeImpl.class)
					.isAssignableFrom(element.getClass())) {
				addText((eu.dzhw.zofar.xml.display.impl.DisplayTextTypeImpl) element, displayContainer);
			} else if ((eu.dzhw.zofar.xml.display.impl.DisplayReloadTypeImpl.class)
					.isAssignableFrom(element.getClass())) {
				addReload((eu.dzhw.zofar.xml.display.impl.DisplayReloadTypeImpl) element, displayContainer);
			} else if ((eu.dzhw.zofar.xml.display.impl.DisplayCalendarTypeImpl.class)
					.isAssignableFrom(element.getClass())) {
				addCalendar((eu.dzhw.zofar.xml.display.impl.DisplayCalendarTypeImpl) element, displayContainer);
			} else if ((IdentificationalType.class).isAssignableFrom(element.getClass())) {
				LOGGER.info("item {}", element.getClass());
			}
		}
	}

	private void addReload(final eu.dzhw.zofar.xml.display.impl.DisplayReloadTypeImpl source,
			final com.sun.java.jsf.composite.display.DisplayType displayContainer) throws XmlException {
		if (source == null)
			return;
		if (displayContainer == null)
			return;
		DisplayReloadType reloadBt = displayContainer.addNewReload();
		final String uid = source.getUid();
		reloadBt.setId(uid);

		if (source.isSetVisible()) {
			final String visible = source.getVisible();
			reloadBt.setRendered(createElExpression(visible));
		}

		reloadBt.setRecorderEnabled(createElExpression(source.getRecorderEnabled() + ""));

		final String text = source.getValue();
		String key = null;
		if (PageManager.getInstance().getMojo() != null) {
			key = PageManager.getInstance().getMojo().addTextToBundle(generateUid(source), text);
		}

		final String textValue = (key == null) ? text : createElExpression(key);

		reloadBt.setValue(textValue);
	}

	private void addTable(final eu.dzhw.zofar.xml.display.impl.DisplayTableTypeImpl table,
			final com.sun.java.jsf.composite.display.DisplayType displayContainer) {
		if (table == null)
			return;
		if (displayContainer == null)
			return;
		final DisplayTableType xhtmlTable = displayContainer.addNewTable();

		final String uid = table.getUid();

		if (table.isSetVisible()) {
			final String visible = table.getVisible();
			xhtmlTable.setRendered(createElExpression(visible));
		}

		xhtmlTable.setId(uid);

		final DisplayTableHeaderType header = table.getHeader();
		if (header != null) {
			final com.sun.java.jsf.composite.display.DisplayTableHeaderType xmhtlTableHeader = xhtmlTable
					.addNewHeader();
			final eu.dzhw.zofar.xml.display.DisplayTextType[] labels = header.getLabelArray();
			if (labels != null) {
				for (final eu.dzhw.zofar.xml.display.DisplayTextType label : labels) {
					addText(label, xmhtlTableHeader);
				}
			}
		}

		final DisplayTableBodyType body = table.getBody();
		if (body != null) {
			final com.sun.java.jsf.composite.display.DisplayTableBodyType xmhtlTableBody = xhtmlTable.addNewBody();
			final DisplayTableRowType[] rows = body.getRowArray();
			if (rows != null) {
				for (final DisplayTableRowType row : rows) {
					final com.sun.java.jsf.composite.display.DisplayTableRowType xmhtlTableBodyRow = xmhtlTableBody
							.addNewRow();

					final String rowUid = row.getUid();
					xmhtlTableBodyRow.setId(rowUid);

					if (row.isSetVisible()) {
						final String visible = row.getVisible();
						xmhtlTableBodyRow.setRendered(visible);
					}

					final DisplayTableItemType[] items = row.getItemArray();
					if (items != null) {
						for (final eu.dzhw.zofar.xml.display.DisplayTableItemType item : items) {
							final com.sun.java.jsf.composite.display.DisplayTableItemType xhtmlItem = xmhtlTableBodyRow
									.addNewItem();

							final String itemUid = item.getUid();
							xhtmlItem.setId(itemUid);

							for (final XmlObject element : item.selectPath(SELECT_PATH_IN_ORDER)) {
								if ((eu.dzhw.zofar.xml.display.DisplayTextType.class)
										.isAssignableFrom(element.getClass())) {
									final eu.dzhw.zofar.xml.display.DisplayTextType text = (eu.dzhw.zofar.xml.display.DisplayTextType) element;
									// xhtmlText.setId(text.getId());
									addText(text, xhtmlItem);

								} else if ((JumperType.class).isAssignableFrom(element.getClass())) {
									final JumperType jumper = (JumperType) element;
									final com.sun.java.jsf.composite.common.JumperType xhtmlJumper = xhtmlItem
											.addNewJumper();

									final String value = jumper.getValue();
									xhtmlJumper.setValue(value);

									final String target = jumper.getTarget();
									xhtmlJumper.setTargetPage(target);

									if (jumper.isSetVisible()) {
										final String visible = jumper.getVisible();
										xhtmlJumper.setVisible(visible);
									}

									if (jumper.isSetDisabled()) {
										final boolean disabled = jumper.getDisabled();
										xhtmlJumper.setDisabled(disabled);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	private void addText(final eu.dzhw.zofar.xml.display.DisplayTextType text,
			final com.sun.java.jsf.composite.display.DisplayTableHeaderType container) {
		if (text == null)
			return;
		if (container == null)
			return;
		final com.sun.java.jsf.composite.display.DisplayTextType xhtmlText = container.addNewText();
		configureText(text, xhtmlText);
	}

	private void addText(final eu.dzhw.zofar.xml.display.DisplayTextType text,
			final com.sun.java.jsf.composite.display.DisplayTableItemType container) {
		if (text == null)
			return;
		if (container == null)
			return;
		final com.sun.java.jsf.composite.display.DisplayTextType xhtmlText = container.addNewText();
		configureText(text, xhtmlText);
	}

	private void addText(final eu.dzhw.zofar.xml.display.DisplayTextType text,
			final com.sun.java.jsf.composite.display.DisplayType container) {
		if (text == null)
			return;
		if (container == null)
			return;
		final com.sun.java.jsf.composite.display.DisplayTextType xhtmlText = container.addNewText();
		configureText(text, xhtmlText);
	}

	private void configureText(final eu.dzhw.zofar.xml.display.DisplayTextType text,
			final com.sun.java.jsf.composite.display.DisplayTextType xhtmlText) {
		if (text == null)
			return;
		if (xhtmlText == null)
			return;

		final String uid = text.getUid();
		xhtmlText.setId(uid);

		if (text.isSetVisible()) {
			final String visible = text.getVisible();
			xhtmlText.setRendered(createElExpression(visible));
		}

		// if (text.isSetBlock()) {
		// final boolean block = text.getBlock();
		// xhtmlText.setBlock(block);
		// }

		Node node = text.getDomNode();
		try {
			createTextHelper(0, node, text, xhtmlText);
		} catch (XmlException e) {
			e.printStackTrace();
		}

	}

	private void addCalendar(final eu.dzhw.zofar.xml.display.DisplayCalendarType calendar,
			final com.sun.java.jsf.composite.display.DisplayType displayContainer) {
		if (calendar == null)
			return;
		if (displayContainer == null)
			return;
		final DisplayCalendarType xhtmlCalendar = displayContainer.addNewCalendar();
		final String uid = calendar.getUid();
		xhtmlCalendar.setId(uid);

		if (calendar.isSetVisible()) {
			final String visible = calendar.getVisible();
			xhtmlCalendar.setRendered(createElExpression(visible));
		}
		
		final String events = calendar.getEvents();
		xhtmlCalendar.setEvents(createElExpression(events));
		
		final String columns = calendar.getColumns();
		xhtmlCalendar.setColumns(createElExpression(columns));
		
		final String rows = calendar.getRows();
		xhtmlCalendar.setRows(createElExpression(rows));
		
		final String config = calendar.getConfig();
		try {
			xhtmlCalendar.setConfig(URLEncoder.encode(config,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final String data = calendar.getData();
		xhtmlCalendar.setData(createElExpression(data));

	}

}
