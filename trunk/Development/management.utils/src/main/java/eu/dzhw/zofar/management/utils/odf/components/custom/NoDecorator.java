package eu.dzhw.zofar.management.utils.odf.components.custom;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.odftoolkit.odfdom.dom.OdfContentDom;
import org.odftoolkit.odfdom.dom.element.style.StyleListLevelPropertiesElement;
import org.odftoolkit.odfdom.dom.element.style.StyleTextPropertiesElement;
import org.odftoolkit.odfdom.dom.element.text.TextListElement;
import org.odftoolkit.odfdom.dom.element.text.TextListItemElement;
import org.odftoolkit.odfdom.dom.element.text.TextListLevelStyleBulletElement;
import org.odftoolkit.odfdom.dom.element.text.TextPElement;
import org.odftoolkit.odfdom.dom.style.OdfStyleFamily;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeAutomaticStyles;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeStyles;
import org.odftoolkit.odfdom.incubator.doc.style.OdfStyle;
import org.odftoolkit.odfdom.incubator.doc.text.OdfTextListStyle;
import org.odftoolkit.simple.Document;
import org.odftoolkit.simple.text.list.BulletDecorator;
import org.odftoolkit.simple.text.list.List;
import org.odftoolkit.simple.text.list.ListItem;
import org.w3c.dom.Node;

public class NoDecorator implements org.odftoolkit.simple.text.list.ListDecorator {

//	private static String[] DEFAULT_TEXT_SPACE_BEFORE_ATTRIBUTES = { null, "0.401cm", "0.799cm", "1.2cm", "1.6cm", "2.001cm", "2.399cm", "2.8cm", "3.2cm", "3.601cm" };
//	private static String DEFAULT_TEXT_MIN_LABEL_WIDTH = "0.4cm";
//	private static String DEFAULT_FONT_NAME = "Tahoma";
//	private static String DEFAULT_BULLET_CHAR = " ";
//	private static String DEFAULT_NAME = "Simple_Default_Bullet_List";
//
//	private OdfTextListStyle listStyle;
//	private OdfStyle paragraphStyle;
//	private OdfOfficeAutomaticStyles styles;

	/**
	 * Constructor with Document.
	 * 
	 * @param doc
	 *            the Document which this BulletDecorator will be used on.
	 */
	public NoDecorator(Document doc) {
//		OdfContentDom contentDocument;
//		try {
////			contentDocument = doc.getContentDom();
////			styles = contentDocument.getAutomaticStyles();
////			OdfOfficeStyles documentStyles = doc.getDocumentStyles();
////			listStyle = styles.getListStyle(DEFAULT_NAME);
////			if (listStyle == null) {
//////				listStyle = styles.newListStyle();
//////				getOrCreateStyleByName(documentStyles, styles, "Bullet_20_Symbols", OdfStyleFamily.Text);
////				for (int i = 0; i < 10; i++) {
//////					TextListLevelStyleBulletElement listLevelElement = listStyle.newTextListLevelStyleBulletElement(DEFAULT_BULLET_CHAR, i + 1);
//////					listLevelElement.setTextStyleNameAttribute("Bullet_20_Symbols");
//////					StyleListLevelPropertiesElement styleListLevelPropertiesElement = listLevelElement.newStyleListLevelPropertiesElement();
//////					if (DEFAULT_TEXT_SPACE_BEFORE_ATTRIBUTES[i] != null) {
//////						styleListLevelPropertiesElement.setTextSpaceBeforeAttribute(DEFAULT_TEXT_SPACE_BEFORE_ATTRIBUTES[i]);
//////					}
//////					styleListLevelPropertiesElement.setTextMinLabelWidthAttribute(DEFAULT_TEXT_MIN_LABEL_WIDTH);
//////					StyleTextPropertiesElement styleTextPropertiesElement = listLevelElement.newStyleTextPropertiesElement("true");
//////					styleTextPropertiesElement.setStyleFontNameAttribute(DEFAULT_FONT_NAME);
////				}
////			}
////			paragraphStyle = styles.newStyle(OdfStyleFamily.Paragraph);
////			getOrCreateStyleByName(documentStyles, styles, "Default_20_Text", OdfStyleFamily.Paragraph);
////			paragraphStyle.setStyleParentStyleNameAttribute("Default_20_Text");
////			paragraphStyle.setStyleListStyleNameAttribute(listStyle.getStyleNameAttribute());
//		} catch (Exception e) {
//			Logger.getLogger(BulletDecorator.class.getName()).log(Level.SEVERE, null, e);
//		}
	}

	public void decorateList(List list) {
//		TextListElement listElement = list.getOdfElement();
//		listElement.setTextStyleNameAttribute(listStyle.getStyleNameAttribute());
	}

	public void decorateListItem(ListItem item) {
//		TextListItemElement listItemElement = item.getOdfElement();
//		Node child = listItemElement.getFirstChild();
//		while (child != null) {
//			if (child instanceof TextPElement) {
//				TextPElement pElement = (TextPElement) child;
//				pElement.setTextStyleNameAttribute(paragraphStyle.getStyleNameAttribute());
//			}
//			child = child.getNextSibling();
//		}
	}

	public ListType getListType() {
		return ListType.BULLET;
	}

//	private OdfStyle getOrCreateStyleByName(OdfOfficeStyles documentStyles, OdfOfficeAutomaticStyles styles, String styleName, OdfStyleFamily styleFamily) {
//		OdfStyle odfStyle = documentStyles.getStyle(styleName, styleFamily);
//		if (odfStyle == null) {
//			styles.getStyle(styleName, styleFamily);
//		}
//		if (odfStyle == null) {
//			odfStyle = styles.newStyle(styleFamily);
//			odfStyle.setStyleNameAttribute(styleName);
//			odfStyle.setStyleDisplayNameAttribute(styleName);
//		}
//		return odfStyle;
//	}

}
