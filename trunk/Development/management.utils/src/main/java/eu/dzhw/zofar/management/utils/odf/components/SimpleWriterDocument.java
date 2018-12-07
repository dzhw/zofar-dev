package eu.dzhw.zofar.management.utils.odf.components;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.util.Set;
import java.util.UUID;

import org.odftoolkit.odfdom.dom.element.table.TableTableCellElementBase;
import org.odftoolkit.odfdom.pkg.OdfElement;
import org.odftoolkit.odfdom.type.Color;
import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.style.Font;
import org.odftoolkit.simple.style.MasterPage;
import org.odftoolkit.simple.style.StyleTypeDefinitions.FontStyle;
import org.odftoolkit.simple.table.Cell;
import org.odftoolkit.simple.table.Column;
import org.odftoolkit.simple.table.Row;
import org.odftoolkit.simple.table.Table;
import org.odftoolkit.simple.table.TableContainer;
import org.odftoolkit.simple.text.AbstractParagraphContainer;
import org.odftoolkit.simple.text.Paragraph;
import org.odftoolkit.simple.text.ParagraphContainer;
import org.odftoolkit.simple.text.list.ImageDecorator;
import org.odftoolkit.simple.text.list.List;
import org.odftoolkit.simple.text.list.ListContainer;
import org.odftoolkit.simple.text.list.ListItem;

public class SimpleWriterDocument {

	/** The doc. */
	private TextDocument doc;

	private static final Font DEFAULTFONT = new Font("Arial", FontStyle.REGULAR, 12);
	
	public static final Font TITLEFONT = new Font("Arial", FontStyle.ITALIC, 10,Color.BLUE);
	public static final Font INSTRUCTIONFONT = new Font("Arial", FontStyle.ITALIC, 10,Color.GREEN);
	public static final Font INTRODUCTIONFONT = new Font("Arial", FontStyle.ITALIC, 10,Color.AQUA);
	public static final Font TEXTFONT = new Font("Arial", FontStyle.ITALIC, 10,Color.YELLOW);
	
	public static final Font UNITHEADERFONT = new Font("Arial", FontStyle.ITALIC, 10,Color.GRAY);
	public static final Font UNITTITLEFONT = new Font("Arial", FontStyle.ITALIC, 10,Color.BLUE);
	public static final Font UNITINSTRUCTIONFONT = new Font("Arial", FontStyle.ITALIC, 10,Color.GREEN);
	public static final Font UNITINTRODUCTIONFONT = new Font("Arial", FontStyle.ITALIC, 10,Color.AQUA);
	
	private static final Font TABLEHEADERFONT = new Font("Arial", FontStyle.BOLD, 10);
	private static final Font TABLEBODYFONT = new Font("Arial", FontStyle.REGULAR, 10);
	private static final Font HEADER1FONT = new Font("Arial", FontStyle.BOLD, 16);
	private static final Font HEADER2FONT = new Font("Arial", FontStyle.BOLD, 14);
	private static final Font HEADER3FONT = new Font("Arial", FontStyle.BOLD, 12);
	
	/**
	 * Instantiates a new writer document.
	 * 
	 * @throws Exception
	 *             the exception
	 */
	public SimpleWriterDocument() throws Exception {
		super();
		this.doc = TextDocument.newTextDocument();
		initialiseDoc();
	}

	public SimpleWriterDocument(final String documentPath) throws Exception {
		this(documentPath, true);
	}

	public SimpleWriterDocument(final String documentPath, final boolean isResource) throws Exception {
		super();
		if (documentPath != null) {
			final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
			InputStream is = null;
			if (isResource)
				is = classloader.getResourceAsStream(documentPath);
			else
				is = new FileInputStream(new File(documentPath));
			this.doc = TextDocument.loadDocument(is);
		}
		initialiseDoc();
	}

	private void initialiseDoc() throws Exception {
		if (this.doc == null)
			return;

		MasterPage master1 = MasterPage.getOrCreateMasterPage(doc, "Landscape");
	}

	public Paragraph addHeading(String text) throws Exception {
		return headingHelper(text, 1, this.HEADER1FONT);
	}

	public Paragraph addSubHeading1(String text) throws Exception {
		return headingHelper(text, 2, this.HEADER2FONT);
	}

	public Paragraph addSubHeading2(String text) throws Exception {
		return headingHelper(text, 3, this.HEADER3FONT);
	}

	private Paragraph headingHelper(final String text, int level, final Font font) throws Exception {
		final Paragraph heading = this.addTextParagraph(text, null);
		heading.setFont(font);
		heading.applyHeading(true, level);
		return heading;
	}

	private ParagraphContainerImpl paragraphContainerImpl;
	TableTableCellElementBase mCellElement;

	private class ParagraphContainerImpl extends AbstractParagraphContainer {
		public OdfElement getParagraphContainerElement() {
			return mCellElement;
		}

		public Paragraph addParagraph(Cell cell, Paragraph paragraph) {
			cell.getOdfElement().appendChild(paragraph.getOdfElement());
			return paragraph;
		}
	}

	private ParagraphContainerImpl getParagraphContainerImpl() {
		if (paragraphContainerImpl == null)
			paragraphContainerImpl = new ParagraphContainerImpl();
		return paragraphContainerImpl;
	}

	public TextDocument getDoc() {
		return doc;
	}

	public Table addTable(String text, String[] columnLabels) throws Exception {
		return addTable(text, columnLabels, doc);
	}

	public Table addTable(String text, String[] columnLabels, final Object container) throws Exception {
		if (this.doc == null)
			return null;
		if ((TextDocument.class).isAssignableFrom(container.getClass()))
			this.addText(text);
		final Table table = createTable(columnLabels, container);
		return table;
	}

	// private double getStringWidth(final String text) {
	// AffineTransform affinetransform = new AffineTransform();
	// FontRenderContext frc = new FontRenderContext(affinetransform, true,
	// true);
	// java.awt.Font font = new java.awt.Font("Arial", java.awt.Font.PLAIN, 12);
	// int textwidth = (int) (font.getStringBounds(text, frc).getWidth());
	// return textwidth;
	// }

	private void optimizeAsRDC(Table table) {
		if (table == null)
			return;
		final int colCount = table.getColumnCount() - 1;
		final double tableWidth = table.getWidth();
		final double widestWidth = tableWidth * 0.7;
		final double colWidth = (tableWidth - widestWidth) / colCount;

		for (int a = 0; a < colCount; a++) {
			final Column col = table.getColumnByIndex(a);
			col.setWidth(colWidth);
		}
		table.getColumnByIndex(colCount).setWidth(widestWidth);
	}

	private void optimizeAsItemList(Table table) {
		if (table == null)
			return;
		final int colCount = table.getColumnCount() - 2;
		final double tableWidth = table.getWidth();
		final double labelWidth = tableWidth * 0.25;
		final double variableWidth = tableWidth * 0.15;

		final double colWidth = (tableWidth - labelWidth - variableWidth) / colCount;

		table.getColumnByIndex(0).setWidth(labelWidth);
//		System.out.println("0 = " + labelWidth);
		table.getColumnByIndex(1).setWidth(variableWidth);
//		System.out.println("1 = " + variableWidth);
		for (int a = 2; a <= colCount; a++) {
			final Column col = table.getColumnByIndex(a);
			col.setWidth(colWidth);
//			System.out.println(a + " = " + colWidth);
		}
	}
	
	private void optimizeAsDouble(Table table) {
		if (table == null)
			return;
		final int colCount = table.getColumnCount() - 1;
		final int labelIndex = (colCount / 2) + 1;
		final double tableWidth = table.getWidth();
//		System.out.println("tableWidth : "+tableWidth+" colCount : "+colCount);
		final double widestWidth = tableWidth * 0.25;
		final double colWidth = (tableWidth - widestWidth) / colCount;
		

		for (int a = 0; a < labelIndex  - 1; a++) {
			final Column col = table.getColumnByIndex(a);
			col.setWidth(colWidth);
		}	
		table.getColumnByIndex(labelIndex  - 1).setWidth(widestWidth);
		for (int a = labelIndex; a <= colCount; a++) {
			final Column col = table.getColumnByIndex(a);
			col.setWidth(colWidth);
		}	
	}
	
	

	public void optimize(Object table,final String parentType) throws Exception {
		if (table == null)
			return;
		
		if((Table.class).isAssignableFrom(table.getClass())){
			final Table tmp = (Table)table;
			if(parentType.equals("zofar:matrixDouble")){
				optimizeAsDouble(tmp);
			}
			else if (parentType.equals("zofar:multipleChoice")) {
				optimizeAsRDC(tmp);
			} else if (parentType.equals("zofar:questionSingleChoice")) {
				optimizeAsRDC(tmp);
			} else if (parentType.equals("zofar:matrixQuestionSingleChoice")) {
				optimizeAsItemList(tmp);
			} else if (parentType.equals("zofar:matrixQuestionOpen")) {
				optimizeAsItemList(tmp);
			}else if (parentType.equals("zofar:item")) {
//				optimizeAsItemList(tmp);
			}
			else{
				System.out.println("optimize Table for "+parentType);
			}
		}
	}

	public Row addRow(Table table) {
		if (table == null)
			return null;
		Row row = table.appendRow();
		return row;
	}

	public Paragraph addToCell(Cell cell, Paragraph paragraph) {
		Font font = TABLEBODYFONT;
		if(paragraph.getFont() != null) font = paragraph.getFont();
		
		return addToCell(cell, paragraph, font);
	}

	public Paragraph addToCell(Cell cell, Paragraph paragraph, final Font font) {
		paragraph.setFont(font);
		Paragraph para = getParagraphContainerImpl().addParagraph(cell, paragraph);
		return para;
	}

	public Table createTable(String[] columnLabels, final Object container) throws Exception {
		final Table table = Table.newTable((TableContainer) container, 1, columnLabels.length);
		table.setTableName(UUID.randomUUID().toString());

		final int colCount = columnLabels.length;
		for (int a = 0; a < colCount; a++) {
			final Paragraph textParagraph = createTextParagraph(columnLabels[a], null, table.getRowByIndex(0).getCellByIndex(a));
			addToCell(table.getRowByIndex(0).getCellByIndex(a), textParagraph, TABLEHEADERFONT);
		}
		return table;
	}

	public Paragraph addText(String text) throws Exception {
		return this.addTextParagraph(text, null);
	}

	public List addList(final String header, final Set<String> items, final ListContainer container) throws Exception {
		if (this.doc == null)
			return null;
		final URI imagePath = getClass().getClassLoader().getResource("images/clear.png").toURI();
		final List list = container.addList(new ImageDecorator(this.doc,imagePath));
		list.setHeader(header);
		if (items != null) {
			for (final String item : items) {
				final ListItem listItem = list.addItem(item);
			}
		}
		return list;
	}
	
	public Paragraph createTextParagraph(final String text, final String comment, final ParagraphContainer container) throws Exception {
		return this.createTextParagraph(text, comment, container, DEFAULTFONT);
	}

	public Paragraph createTextParagraph(final String text, final String comment, final ParagraphContainer container,final Font font) throws Exception {
		if (this.doc == null)
			return null;
		final Paragraph paragraph = Paragraph.newParagraph(container);
		if(font != null)paragraph.setFont(font);
		else paragraph.setFont(DEFAULTFONT);
		// paragraph.appendTextContent(text);
		paragraph.setTextContentNotCollapsed(text);
		if (comment != null) {
			paragraph.addComment(comment, "");
		}
		return paragraph;
	}

	private Paragraph addTextParagraph(final String text, final String comment) throws Exception {
		if (this.doc == null)
			return null;
		return createTextParagraph(text, comment, this.doc);
	}

	public void addPageBreak() {
		if (this.doc == null)
			return;
		this.doc.addPageBreak();
	}

	public Paragraph addTextLineBreak() throws Exception {
		if (this.doc == null)
			return null;
		final Paragraph paragraph = this.doc.addParagraph("");
		// WhitespaceProcessor.appendText(paragraph.getOdfElement(), "\n");
		return paragraph;
	}

	public void save(final String documentPath) throws Exception {
		if (this.doc == null)
			return;
		this.doc.save(documentPath);
	}
}
