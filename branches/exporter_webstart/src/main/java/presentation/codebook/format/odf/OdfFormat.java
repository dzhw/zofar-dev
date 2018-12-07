package presentation.codebook.format.odf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;

import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.odf.components.SimpleWriterDocument;
import eu.dzhw.zofar.management.utils.odf.components.WriterDocument;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import model.HeaderEntry;
import model.ValueEntry;
import presentation.codebook.format.AbstractFormat;
import presentation.codebook.format.odf.renderer.ODFRenderer;

// TODO: Auto-generated Javadoc
/**
 * The Class OdfFormat.
 */
public class OdfFormat extends AbstractFormat {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(OdfFormat.class);

	/** The instance. */
	private static OdfFormat INSTANCE;

	/**
	 * The Enum FORMAT.
	 */
	public enum FORMAT {

		/** The odf. */
		odf,
		/** The pdf. */
		pdf
	}

	/**
	 * Instantiates a new odf format.
	 */
	private OdfFormat() {
		super();
	}

	/**
	 * Gets the single instance of OdfFormat.
	 * 
	 * @return single instance of OdfFormat
	 */
	public static AbstractFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new OdfFormat();
		return INSTANCE;
	}

	private Map<FORMAT, File> buildExportNew(final Map<String, Set<Node>> data, final String odfpath) throws Exception {
//		final WriterDocument doc = new WriterDocument("MasterTemplate.odt");
		final SimpleWriterDocument doc = new SimpleWriterDocument("MasterTemplate.odt");
		final Map<String, Object> attachements = new LinkedHashMap<String, Object>();
		for (final Map.Entry<String, Set<Node>> pageItem : data.entrySet()) {
			String pageId = pageItem.getKey();
//			if(!pageId.equals("page10"))continue;
			Set<Node> pageSet = pageItem.getValue();

//			System.out.println("Page : " + pageId);
			doc.addHeading(pageId);

			for (final Node question : pageSet) {
				final ODFRenderer renderer = ODFRenderer.getInstance(question);
				if(renderer != null){
					renderer.render(pageId, question,question, doc, attachements,null);
					doc.addTextLineBreak();
				}
			}
			doc.addPageBreak();
//			break;
		}
		
		if(!attachements.isEmpty()){
			boolean first = true;
			doc.addHeading("Anhänge");
			for (final Map.Entry<String, Object> attachement : attachements.entrySet()) {
				if((Node.class).isAssignableFrom(attachement.getValue().getClass())){
					final Node attachementNode = (Node)attachement.getValue();
					if(!first)doc.addPageBreak();
					
					doc.addSubHeading1("Anhang "+attachement.getKey());
					doc.addTextLineBreak();
					final ODFRenderer renderer = ODFRenderer.getInstance(attachementNode);
					if(renderer != null){
						renderer.render("Anhänge", attachementNode,attachementNode, doc, attachements,null,true);
					}
					first = false;
				}
			}
		}
		doc.save(odfpath);
		Map<FORMAT, File> back = new HashMap<FORMAT, File>();
		back.put(FORMAT.odf, new File(odfpath));
		return back;
	}



//	private Set<Node> getAttachedQuestions(final Node parentNode) {
//		if (parentNode == null)
//			return null;
//
//		final NodeList attachedOpens = XmlClient.getInstance().getByXPath(parentNode, "questionOpen");
//		if (attachedOpens != null) {
//			final Set<Node> attachedOpenSet = new LinkedHashSet<Node>();
//			final int attachedOpenCount = attachedOpens.getLength();
//
//			if (attachedOpenCount > 0) {
//				for (int b = 0; b < attachedOpenCount; ++b) {
//					final Node item = attachedOpens.item(b);
//					attachedOpenSet.add(item);
//				}
//				return attachedOpenSet;
//			}
//		}
//
//		return null;
//	}
//
//
//
//	private String getValue(final Node parentNode) {
//		if (parentNode == null)
//			return null;
//
//		final NamedNodeMap nodeAttributes = parentNode.getAttributes();
//		if (nodeAttributes != null) {
//			final Node uidNode = nodeAttributes.getNamedItem("value");
//			if (uidNode != null)
//				return uidNode.getTextContent();
//		}
//		return null;
//	}
//
//
//
//	private Set<Node> getRDCItems(final Node parentNode) {
//		if (parentNode == null)
//			return null;
//		final NodeList rdcs = XmlClient.getInstance().getByXPath(parentNode, "./responseDomain");
//		if (rdcs != null) {
//			final int rdcCount = rdcs.getLength();
//			for (int a = 0; a < rdcCount; a++) {
//				final Node rdc = rdcs.item(a);
//				final NodeList matrixItems = XmlClient.getInstance().getByXPath(rdc, "item");
//				if (matrixItems != null) {
//					final Set<Node> itemSet = new LinkedHashSet<Node>();
//					final int itemCount = matrixItems.getLength();
//
//					if (itemCount > 0) {
//						// System.out.println("item Count : "+itemCount);
//						for (int b = 0; b < itemCount; b++) {
//							final Node item = matrixItems.item(b);
//							itemSet.add(item);
//						}
//						return itemSet;
//					}
//				}
//				final NodeList answerOptions = XmlClient.getInstance().getByXPath(rdc, "answerOption");
//				if (answerOptions != null) {
//					final Set<Node> aoSet = new LinkedHashSet<Node>();
//					final int aoCount = answerOptions.getLength();
//
//					if (aoCount > 0) {
//						// System.out.println("ao Count : "+aoCount);
//						for (int b = 0; b < aoCount; b++) {
//							final Node ao = answerOptions.item(b);
//							aoSet.add(ao);
//						}
//						return aoSet;
//					}
//				}
//			}
//		}
//		return null;
//	}
//
//	private Object renderOptions(final Node question, final SimpleWriterDocument doc, final Map<String, Object> attachements) throws Exception {
//		if (question == null)
//			return null;
//		Set<Node> rdcItems = this.getRDCItems(question);
//		if (rdcItems != null) {
//			final String[] columnLabels = new String[5];
//			
//			int type = 0;
//
//			final int count = rdcItems.size();
//
//			final String[][] data = new String[count][columnLabels.length];
//
//			int lft = 0;
//			for (final Node item : rdcItems) {
//				final String itemType = item.getNodeName();
//				if (itemType.equals("zofar:answerOption")) {
//					type = 1;
//					final Set<String> headers = this.getHeader(item);
//					final String variable = this.getVariable(item);
//					final String uid = this.getUID(item);
//					final String value = this.getValue(item);
//
//					String headerStr = "";
//					for (final String header : headers) {
//						headerStr = headerStr + " " + header;
//					}
//
//					data[lft][0] = "";
//					if (uid != null)
//						data[lft][0] = " " + uid + "";
//
//					data[lft][1] = "";
//					if (variable != null)
//						data[lft][1] = " " + variable + "";
//
//					data[lft][2] = "";
//					if (value != null)
//						data[lft][2] = " " + value + "";
//
//					data[lft][3] = headerStr;
//
//				} else if (itemType.equals("zofar:item")) {
//					type = 2;
//				}
//				
//				
//
//				final Set<Node> attachedItems = getAttachedQuestions(item);
//				if ((attachedItems != null) && (attachedItems.size() > 0)) {
//					String attachedItemStr = "";
//					for (final Node attachedItem : attachedItems) {
//						final String variable = this.getVariable(attachedItem);
//						final Set<String> headers = this.getHeader(attachedItem);
//						String headerStr = "";
//						for (final String header : headers) {
//							headerStr = headerStr + " " + header;
//						}
//						
//						if (variable != null)attachedItemStr = attachedItemStr +" ("+ variable+ ") ";
//						attachedItemStr = attachedItemStr + headerStr+ "";
//					}
//
//					data[lft][4] = attachedItemStr.trim();
//				}
//				lft = lft + 1;
//			}
//			
//			String title = "";
//			
//			if(type == 1){
//				title = "Optionen";
//				columnLabels[0] = "UID";
//				columnLabels[1] = "Variable";
//				columnLabels[2] = "Value";
//				columnLabels[3] = "Label";
//				columnLabels[4] = "Attached Open";
//			}
//			else if (type == 2){
//				title = "Matrix Items";
//				columnLabels[0] = "UID";
//				columnLabels[1] = "Variable";
//				columnLabels[2] = "";
//				columnLabels[3] = "Matrix Item";
//				columnLabels[4] = "Attached Open";
//			}
//			
//			
//			doc.addTable(title, columnLabels, data);
//		}
//		return null;
//	}
//
//	private Object renderQuestion(final String pageId, final Node question, final SimpleWriterDocument doc, final Map<String, Object> attachements) throws Exception {
//		if (question == null)
//			return null;
//		final String questionType = question.getNodeName();
//		renderHeader(question, doc);
//		if (questionType.equals("zofar:questionSingleChoice")) {
//			renderOptions(question, doc, attachements);
//		} else if (questionType.equals("zofar:multipleChoice")) {
//			renderOptions(question, doc, attachements);
//		} else if (questionType.equals("zofar:matrixQuestionSingleChoice")) {
//			renderOptions(question, doc, attachements);
//		} else if (questionType.equals("zofar:matrixMultipleChoice")) {
//			renderOptions(question, doc, attachements);
//		} else if (questionType.equals("zofar:matrixQuestionOpen")) {
//			renderOptions(question, doc, attachements);
//		} 
//		
//		else {
//			System.err.println("unrendered Question type : " + questionType);
//		}
//		return null;
//	}

	/**
	 * Builds the export.
	 * 
	 * @param data
	 *            the data
	 * @param path
	 *            the path
	 * @param mapping
	 *            the mapping
	 * @return the file
	 * @throws Exception
	 *             the exception
	 */
	private Map<FORMAT, File> buildExport(final Map<HeaderEntry, Map<String, ValueEntry>> data, final String odfpath, final String pdfpath, final Map<String, String> mapping) throws Exception {
		final WriterDocument doc = new WriterDocument("MasterTemplate.odt");

		// doc.addHeading("Missing-Codes");
		// final List<String[]> mappingTableData = new ArrayList<String[]>();
		//
		// final String[] mappingColumns = new String[2];
		// mappingColumns[0] = "Missing";
		// mappingColumns[1] = "Wert";
		//
		// for(Map.Entry<String, String> mappingItem:mapping.entrySet()){
		// final String[] mappingRowData = new String[2];
		// mappingRowData[0] = mappingItem.getKey();
		// mappingRowData[1] = mappingItem.getValue();
		// mappingTableData.add(mappingRowData);
		// }
		//// LOGGER.info("table1 = [{}]
		// {}",Arrays.asList(mappingColumns),Arrays.asList(mappingTableData));
		// doc.addTable("Missing Codes", mappingColumns, mappingTableData);

		final Set<HeaderEntry> headers = data.keySet();
		final Iterator<HeaderEntry> headerIt = headers.iterator();

		while (headerIt.hasNext()) {
			final HeaderEntry header = headerIt.next();
			final Map<String, ValueEntry> options = data.get(header);

			doc.addHeading(header.getVariable());

			final Set<String> headerSet = header.getText();
			for (final String item : headerSet) {
				doc.addText(item);
			}

			final List<String[]> tableData = new ArrayList<String[]>();

			boolean openFlag = false;
			for (final Map.Entry<String, ValueEntry> item : options.entrySet()) {
				final String uid = item.getKey();
				final ValueEntry valueEntry = item.getValue();

				final String label = ReplaceClient.getInstance().cleanedString(valueEntry.getLabel());
				String value = valueEntry.getValue() + "";

				if (value.equals("OPEN")) {
					openFlag = true;
				} else {
					final boolean missing = valueEntry.isMissing();
					final String[] rowData = new String[3];
					rowData[0] = uid;
					if (mapping.containsKey(value))
						value = mapping.get(value);
					rowData[1] = value;
					if (missing)
						rowData[2] = "[MISSING] " + label;
					else
						rowData[2] = label;
					tableData.add(rowData);
				}
			}

			if (openFlag) {
				doc.addText("Offene Angabe");
			} else {
				final String[] columns = new String[3];
				columns[0] = "ID";
				columns[1] = "Wert";
				columns[2] = "Label";
				// LOGGER.info("table2 = [{}]
				// {}",Arrays.asList(columns),Arrays.asList(tableData));
				doc.addTable("Antwort Optionen", columns, tableData);
			}
			doc.addPageBreak();
		}
		doc.save(odfpath);
		// doc.exportAsPdf(pdfpath);

		Map<FORMAT, File> back = new HashMap<FORMAT, File>();
		back.put(FORMAT.odf, new File(odfpath));
		// back.put(FORMAT.pdf, new File(pdfpath));
		return back;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.codebook.format.AbstractFormat#format(java.util.Map,
	 * java.util.Map)
	 */
	@Override
	public Object format(final Map<HeaderEntry, Map<String, ValueEntry>> data, final Map<String, String> mapping) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		try {
			return this.buildExport(data, FileClient.getInstance().createTempFile("codebook", "odf").getAbsolutePath(), FileClient.getInstance().createTempFile("codebook", "pdf").getAbsolutePath(), mapping);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * presentation.codebook.format.AbstractFormat#formatVariableList(java.util
	 * .Map, java.util.Map)
	 */
	@Override
	public Object formatVariableList(final Map<HeaderEntry, Map<String, ValueEntry>> data, final Map<String, String> mapping) throws Exception {
		throw new Exception("Not implemented yet");
	}

	@Override
	public Map<FORMAT, File> formatNew(Map<String, Set<Node>> data) {
		if (data == null)
			return null;
		if (data.isEmpty())
			return null;

		try {
			return this.buildExportNew(data, FileClient.getInstance().createTempFile("codebook", "odf").getAbsolutePath());
		} catch (final Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
