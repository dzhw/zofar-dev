package service.mdm;

import java.io.File;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Pattern;

import javax.json.JsonObject;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.his.zofar.xml.questionnaire.AbstractMatrixType;
import de.his.zofar.xml.questionnaire.AbstractQuestionType;
import de.his.zofar.xml.questionnaire.CalendarType;
import de.his.zofar.xml.questionnaire.IdentificationalType;
import de.his.zofar.xml.questionnaire.PageType;
import de.his.zofar.xml.questionnaire.QuestionnaireDocument;
import eu.dzhw.zofar.management.comm.json.JSONClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.files.PackagerClient;
import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class MDMService extends Observable {

	/** The instance. */
	private static MDMService INSTANCE;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(MDMService.class);

	private final JSONClient JSON;

	/**
	 * Instantiates a new JSON service.
	 */
	private MDMService() {
		super();
		JSON = JSONClient.getInstance();
	}

	/**
	 * Gets the single instance of JSONService.
	 * 
	 * @return single instance of JSONService
	 */
	public static MDMService getInstance() {
		if (INSTANCE == null)
			INSTANCE = new MDMService();
		return INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Observable#addObserver(java.util.Observer)
	 */
	@Override
	public synchronized void addObserver(final Observer o) {
		super.addObserver(o);
		this.setChanged();
		this.notifyObservers("phases=1");
	}

	// private List<IdentificationalType> extractQuestions(XmlObject parent) {
	// List<IdentificationalType> questions = new
	// ArrayList<IdentificationalType>();
	// for (final XmlObject element : parent.selectPath("./*")) {
	// if ((AbstractQuestionType.class).isAssignableFrom(element.getClass())) {
	// questions.add((AbstractQuestionType) element);
	// } else if
	// ((AbstractMatrixType.class).isAssignableFrom(element.getClass())) {
	// questions.add((AbstractMatrixType) element);
	// } else {
	// questions.addAll(extractQuestions(element));
	// }
	// }
	// return questions;
	// }

	public Map<String, Object> buildUp(final String qmlPath, final Map<String, Properties> translations,
			final File imagesDirectory, final File screenshotFile, final String dataAcquestionProject,
			final String surveyId, final String instrumentId, final String datasetId) throws Exception {
		final Map<String, Object> back = new LinkedHashMap<String, Object>();

		// // Import Translation
		//
		// Map<String, Properties> translations = null;
		//
		// if (translationPath != null) {
		// Importer transImporter = new Importer(new File(translationPath));
		// translations = transImporter.loadAsMap();
		// }

		// Import Screenshots

		Map<String, Map<String, Object>> screenshots = new HashMap<String, Map<String, Object>>();

		if (screenshotFile != null) {
			List<File> files = PackagerClient.getInstance().extractZip(screenshotFile);
			final FileClient fileClient = FileClient.getInstance();
			if (files != null) {
				for (final File file : files) {
					String name = file.getName();
					final String suffix = fileClient.getSuffix(file);
					if (suffix != null) {
						name = name.replaceAll(Pattern.quote("." + suffix), "");
					}
					System.out.println("filename : "+name);
					final String[] nameParts = name.split("##");
					if ((nameParts != null) && (nameParts.length >= 2)) {
						final String questionName = nameParts[0];
						System.out.println("questionName : "+questionName);
						final String resolution = nameParts[1];
						String language = "de";
						if (nameParts.length >= 3)
							language = nameParts[2];

						Map<String, Object> resolutionMap = null;
						if (screenshots.containsKey(questionName))
							resolutionMap = screenshots.get(questionName);
						if (resolutionMap == null)
							resolutionMap = new HashMap<String, Object>();

						Map<String, Object> languageMap = null;
						if (resolutionMap.containsKey(resolution))
							languageMap = (Map<String, Object>) resolutionMap.get(resolution);
						if (languageMap == null)
							languageMap = new HashMap<String, Object>();

						languageMap.put(language, file);

						resolutionMap.put(resolution, languageMap);
						screenshots.put(questionName, resolutionMap);
					}
				}
			}
		}

		// for(Map.Entry<String, Map<String, Object>> screenshotItem :
		// screenshots.entrySet()) {
		// System.out.println("SCREENSHOT : "+screenshotItem);
		// }
		// System.exit(1);
		// Load QML
		XmlObject doc = docToQmlObject(XmlClient.getInstance().getDocument(qmlPath));

		// Extract all Questions from QML-Document to an Map
		final Map<XmlObject, String> questionMap = new LinkedHashMap<XmlObject, String>();
		final Map<XmlObject, Integer> instrumentIndexMap = new LinkedHashMap<XmlObject, Integer>();
		final PageType[] pages = ((QuestionnaireDocument) doc).getQuestionnaire().getPageArray();
		if (pages != null) {
			Integer indexInInstrument = 1;
			for (PageType page : pages) {
				final String pageID = page.getUid();
				List<XmlObject> questions = getQuestionsFromObj(page);
				if ((questions != null) && (!questions.isEmpty())) {
					final Map<String, Integer> indexMap = new LinkedHashMap<String, Integer>();

					for (final XmlObject question : questions) {
						if ((IdentificationalType.class).isAssignableFrom(question.getClass())) {
							final IdentificationalType tmp = (IdentificationalType) question;

							String pagename = pageID + "_" + tmp.getUid();
							int lft = 0;
							if (indexMap.containsKey(pagename))
								lft = indexMap.get(pagename);
							indexMap.put(pagename, lft + 1);
							if (lft > 0)
								pagename = pagename + "_" + lft;
							questionMap.put(question, pagename);
							instrumentIndexMap.put(question, indexInInstrument);
							indexInInstrument = indexInInstrument + 1;
						}
					}
				}
			}
		}

		Map<String, Map<String, Object>> variablesBack = new LinkedHashMap<String, Map<String, Object>>();
		Map<String, JsonObject> questionsBack = new LinkedHashMap<String, JsonObject>();
		Map<String, Object> imagesBack = new LinkedHashMap<String, Object>();

		for (Map.Entry<XmlObject, String> questionPair : questionMap.entrySet()) {
			final String name = questionPair.getValue();
//			final String keyName = name;
			final String keyName = name.substring(0, name.lastIndexOf('_'));

			System.out.println("keyname : "+keyName);
			
			final XmlObject questionObj = questionPair.getKey();
			final int index = instrumentIndexMap.get(questionObj);

			final Map<String, Object> structuredData = getDataTable(doc, questionObj, translations, index, questionMap,
					dataAcquestionProject, surveyId, instrumentId, datasetId);

			Map<String, Object> questionData = (Map<String, Object>) structuredData.get("question");
			List<Map<String, Object>> variableData = (List<Map<String, Object>>) structuredData.get("variable");

			if (screenshots.containsKey(keyName)) {
				System.out.println("screenshot found : "+keyName);
				final Map<String, Object> images = screenshots.get(keyName);
				final Map<String, Object> imageFolderForQuestion = new LinkedHashMap<String, Object>();
				int imageIndex = 0;
				for (Map.Entry<String, Object> screenshotItem : images.entrySet()) {
					final String resolution = screenshotItem.getKey();
					final Map<String, File> resolutionImages = (Map<String, File>) screenshotItem.getValue();

					for (Map.Entry<String, File> resolutionImageItem : resolutionImages.entrySet()) {
						final String language = resolutionImageItem.getKey();
						final File imageFile = resolutionImageItem.getValue();

						final String fileName = name + "_" + imageIndex;

						final Set<Object> imagePair = new LinkedHashSet<Object>();

						imagePair.add(imageFile);

						// imagesBack.put(name, imageFile);
						// String relativePath = "./" + imagesDirectory.getName() + "/" +
						// imageFile.getName();
						String relativePath = "./" + fileName + "." + FileClient.getInstance().getSuffix(imageFile);

						final Map<String, Object> questionImageMetadata = new LinkedHashMap<String, Object>();
						questionImageMetadata.put("imageType",
								FileClient.getInstance().getSuffix(imageFile).toUpperCase());
						questionImageMetadata.put("language", language);
						questionImageMetadata.put("file", relativePath);
						questionImageMetadata.put("resolution", resolution);
						questionImageMetadata.put("containsAnnotations", false);
						questionImageMetadata.put("indexInQuestion", imageIndex);
						questionImageMetadata.put("questionId", name);
						questionImageMetadata.put("dataAcquisitionProjectId", dataAcquestionProject);
						imagePair.add(JSON.createObject(questionImageMetadata));

						imageFolderForQuestion.put(fileName, imagePair);
						imageIndex = imageIndex + 1;
					}
				}
				imagesBack.put(name, imageFolderForQuestion);
			}

			// final File imageFile = (File) questionData.get("image");
			// imagesBack.put(name, imageFile);
			// String relativePath = "./"+imagesDirectory.getName()+"/"+imageFile.getName();
			// questionData.put("image",relativePath);

			// final Map<String, Object> questionImage = (Map<String, Object>)
			// questionData.get("questionImageMetadata");
			// // questionImageMetadata.put("imageType", "PNG");
			// // questionImageMetadata.put("language", "de");
			// // questionImageMetadata.put("file", screenshots.get(qid));
			// // questionImageMetadata.put("containsAnnotations", "false");
			// // questionImageMetadata.put("indexInQuestion", "0");
			// // questionImageMetadata.put("questionId", idStr);
			// // questionImageMetadata.put("dataAcquisitionProjectId",
			// dataAcquestionProject);
			// //
			// // jsonQuestion.put("questionImageMetadata", questionImageMetadata);
			//
			// final File imageFile = (File) questionImage.get("file");
			// imagesBack.put(name, imageFile);
			// String relativePath = "./" + imagesDirectory.getName() + "/" +
			// imageFile.getName();
			//
			// questionImage.put("fileName", relativePath);
			//
			// questionImage.remove("file");
			//
			// questionData.put("questionImageMetadata", questionImage);
			// questionData.remove("id");

			// Generate JSONs
			final JsonObject question_json = JSON.createObject(questionData);
			questionsBack.put(name, question_json);

			// variable merge
			if ((variableData != null) && (!variableData.isEmpty())) {
				final Map<String, Map<String, Object>> merged = (Map<String, Map<String, Object>>) variablesBack;
				for (final Map<String, Object> questionVar : variableData) {
					final String variable_id = (String) questionVar.get("variablen_id");
					if (merged.containsKey(variable_id)) {
						Map<String, Object> varFromMerged = merged.get(variable_id);
						for (final Map.Entry<String, Object> varFromQuestion : questionVar.entrySet()) {
							if (varFromMerged.containsKey(varFromQuestion.getKey())) {
								// detail merge
								Object mergedValue = varFromMerged.get(varFromQuestion.getKey());
								final Object questionValue = varFromQuestion.getValue();
								if (questionValue == null) {

								} else if (mergedValue == null) {
									varFromMerged.put(varFromQuestion.getKey(), questionValue);
								} else {
									varFromMerged.put(varFromQuestion.getKey(), mergedValue + " # " + questionValue);
								}
							} else {
								varFromMerged.put(varFromQuestion.getKey(), varFromQuestion.getValue());
							}
						}
						merged.put(variable_id, varFromMerged);
					} else
						merged.put(variable_id, questionVar);
				}
			}
		}

		back.put("variable", variablesBack);
		back.put("question", questionsBack);
		back.put("images", imagesBack);
		return back;
	}

	// Convert XML-Document to QML-Document
	private QuestionnaireDocument docToQmlObject(final Document doc) throws Exception {
		DOMSource domSource = new DOMSource(doc);
		StringWriter writer = new StringWriter();
		StreamResult result = new StreamResult(writer);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		final QuestionnaireDocument back = QuestionnaireDocument.Factory.parse(writer.toString());
		return back;
	}

	// Generate Component-Map from XmlObject by Reflection
	private Map<String, Object> getObjAsTable(final Object node) {
		if (node == null)
			return null;

		final Map<String, Object> structuredData = new LinkedHashMap<String, Object>();
		if ((XmlObject.class).isAssignableFrom(node.getClass())) {
			final XmlObject tmp = (XmlObject) node;
			structuredData.put("xmlObj", tmp);

			final Method[] methods = tmp.getClass().getMethods();
			if (methods != null) {
				final int methodCount = methods.length;
				for (int a = 0; a < methodCount; a++) {
					final Method method = methods[a];

					final String modifier = Modifier.toString(method.getModifiers());
					final Class<?> declaringClass = method.getDeclaringClass();
					final Class<?>[] parameterTypes = method.getParameterTypes();

					if ((declaringClass.getName().contains("zofar")) && (method.getName().startsWith("get"))
							&& (parameterTypes.length == 0)) {
						final String name = method.getName().replaceFirst("get", "").toLowerCase();

						final Class<?> returnType = method.getReturnType();
						Object value = null;
						try {
							value = method.invoke(node, new Object[0]);
						} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
							LOGGER.error("[{}] {}",
									node.getClass().getSimpleName() + "," + declaringClass.getSimpleName(),
									modifier + " " + returnType.getSimpleName() + " " + name + " ==> " + value + " ## "
											+ e.getMessage());
						}

						if (value != null) {
							if (returnType.getName().contains("zofar"))
								structuredData.put(name, getObjAsTable(value));
							else
								structuredData.put(name, value);
						}
					}
				}
			}
		} else if ((Object[].class).isAssignableFrom(node.getClass())) {
			Object[] tmp = (Object[]) node;
			final int arrayCount = tmp.length;
			Object[] rebuildTmp = new Object[arrayCount];
			if (arrayCount > 0) {
				for (int a = 0; a < arrayCount; a++)
					rebuildTmp[a] = getObjAsTable(tmp[a]);
				structuredData.put("ARRAY", rebuildTmp);
			} else
				structuredData.put("ARRAY", "NULL");
		} else {
			structuredData.put("VALUE", node);
		}

		return structuredData;
	}

	// Compose Data for JSON-Objects and CSV
	private Map<String, Object> getDataTable(final XmlObject doc, final XmlObject node,
			final Map<String, Properties> translations, final int indexInInstrument, final Map<XmlObject, String> idMap,
			final String dataAcquestionProject, final String surveyId, final String instrumentId,
			final String datasetId) {
		if (node == null)
			return null;
		final Map<String, Object> structuredData = getObjAsTable(node);

		if (structuredData == null)
			return null;
		if (structuredData.isEmpty())
			return null;

		// Question JSON

		// "id": Mandatory, Max Length MEDIUM, Alphanumeric (with German Umlauts and ß)
		// + "-", Pattern ("que-" + dataAcquisitionProjectId + "-ins" + instrumentNumber
		// + "-" + number + "$") The number can contain dots
		// "number": Mandatory, Max Length SMALL, unique within instrument

		// "questionText": Mandatory, Max Length LARGE
		// "instruction": Max Length LARGE
		// "introduction": Max Length LARGE
		// "type":{
		// "de": "Einfachnennung" or "Offen" or "Mehrfachnennung" or "Itembatterie" or
		// "Matrix",
		// "en": "Single Choice" or "Open" or "Multiple Choice" or "Item Set" or "Grid"
		// }
		// "questionImageMetadata": {
		// "imageType" : "PNG"
		// "language" : ["de","en",...]
		// "fileName": Mandatory, MAX LENGTH MEDIUM
		// "containsAnnotations": [true,false]
		// "indexInQuestion": Mandatory
		// "questionId": Mandatory
		// "dataAcquisitionProjectId": Mandatory
		// }
		// "instrumentNumber": Mandatory
		// "dataAcquisitionProjectId": Mandatory
		// "studyId": Mandatory
		//
		// "additionalQuestionText" : Max Length X-LARGE

		// "topic": Max Length LARGE
		// "annotations": Max Length LARGE

		// "indexInInstrument": Mandatory

		final Map<String, Object> jsonQuestion = new LinkedHashMap<String, Object>();

		final String qid = idMap.get(node);
		final String idStr = "que-" + dataAcquestionProject + "-ins" + instrumentId + "-" + qid + "$";
		jsonQuestion.put("id", idStr);
		jsonQuestion.put("number", qid);

		jsonQuestion.put("instrumentNumber", instrumentId);
		jsonQuestion.put("dataAcquisitionProjectId", dataAcquestionProject);
		jsonQuestion.put("studyId", surveyId);

		// jsonQuestion.put("topic", "??");

		final Map<String, String> topicMap = new LinkedHashMap<String, String>();
		// topicMap.put("de", "");
		// topicMap.put("en", "");
		jsonQuestion.put("topic", topicMap);

		// jsonQuestion.put("annotations", "??");
		final Map<String, String> annotationsMap = new LinkedHashMap<String, String>();
		// annotationsMap.put("de", "");
		// annotationsMap.put("en", "");
		jsonQuestion.put("annotations", annotationsMap);

		jsonQuestion.put("indexInInstrument", indexInInstrument);

		Map<String, Set<String>> questionTexts = null;
		Map<String, Set<String>> instructionTexts = null;
		Map<String, Set<String>> introductionTexts = null;

		String type = "UNKOWN";
		if (structuredData.containsKey("xmlObj")) {
			XmlObject tmp = (XmlObject) structuredData.get("xmlObj");

			questionTexts = getQuestionHeader(tmp, translations);
			instructionTexts = getInstructionHeader(tmp, translations);
			introductionTexts = getIntroductionHeader(tmp, translations);

			final Map<String, Object> technicalRepresentation = new LinkedHashMap<String, Object>();
			technicalRepresentation.put("type", "QML");
			technicalRepresentation.put("language", "XML");

			XmlObject parent = null;
			if ((AbstractQuestionType.class).isAssignableFrom(tmp.getClass())) {
			} else if ((AbstractMatrixType.class).isAssignableFrom(tmp.getClass())) {

			} else {
				parent = XmlClient.getInstance().getParent(tmp);
				while (parent != null) {
					XmlObject tmpParent = XmlClient.getInstance().getParent(parent);
					if (tmpParent == null)
						break;
					if (tmpParent.getDomNode().getNodeName().equals("zofar:section"))
						break;
					if (tmpParent.getDomNode().getNodeName().equals("zofar:page"))
						break;
					if (tmpParent.getDomNode().getNodeName().equals("zofar:body"))
						break;
					parent = tmpParent;
				}
			}

			if (parent != null) {
				technicalRepresentation.put("source", XmlClient.getInstance().convert2String(parent));
				// if(questionTexts.isEmpty())questionTexts = getQuestionHeader(parent,
				// translations);
			} else {
				technicalRepresentation.put("source", XmlClient.getInstance().convert2String(tmp));
			}
			jsonQuestion.put("technicalRepresentation", technicalRepresentation);

			type = tmp.getDomNode().getNodeName();

			final List<XmlObject> nextList = this.getNext(tmp, doc);
			if ((nextList != null) && (!nextList.isEmpty())) {
				final List<Object> successors = new ArrayList<Object>();
				for (XmlObject next : nextList) {
					if ((IdentificationalType.class).isAssignableFrom(next.getClass())) {
						final IdentificationalType xx = (IdentificationalType) next;
						if (!idMap.containsKey(xx))
							System.err.println("XX " + getUIDPath(xx) + "(" + next.getClass().getName() + ") ");
						else {
							final String id = idMap.get(next);
							successors.add(id);
						}
					}
				}
				jsonQuestion.put("successorNumbers", successors);
				// jsonQuestion.put("successorNumbers", successors);
			}

			Map<String, Set<String>> additionals = getAdditional(tmp, translations);
			Map<String, String> implodedAdditionals = new LinkedHashMap<String, String>();
			for (Entry<String, Set<String>> item : additionals.entrySet()) {
				implodedAdditionals.put(item.getKey(), CollectionClient.getInstance().implode(item.getValue()));
			}

			jsonQuestion.put("additionalQuestionText", implodedAdditionals);
		}

		// "de": "Einfachnennung" or "Offen" or "Mehrfachnennung" or "Itembatterie" or
		// "Matrix",
		// "en": "Single Choice" or "Open" or "Multiple Choice" or "Item Set" or "Grid"
		String questionTypeDE = "UNBEKANNT";
		String questionTypeENG = "UNKOWN";

		if (type.equals("zofar:multipleChoice")) {
			// multiple choice
			questionTypeDE = "Mehrfachnennung";
			questionTypeENG = "Multiple Choice";
		} else if (type.equals("zofar:questionSingleChoice")) {
			// single choice
			questionTypeDE = "Einfachnennung";
			questionTypeENG = "Single Choice";
		} else if (type.equals("zofar:questionOpen")) {
			// open
			questionTypeDE = "Offen";
			questionTypeENG = "Open";
		} else if (type.equals("zofar:matrixQuestionSingleChoice")) {
			// single choice
			questionTypeDE = "Matrix";
			questionTypeENG = "Grid";
		} else if (type.equals("zofar:matrixQuestionOpen")) {
			// open
			questionTypeDE = "Matrix";
			questionTypeENG = "Grid";
		} else if (type.equals("zofar:matrixDouble")) {
			questionTypeDE = "Mixed";
			questionTypeENG = "mixed";
		} else if (type.equals("zofar:matrixQuestionMixed")) {
			// Mixed
			questionTypeDE = "Itembatterie";
			questionTypeENG = "Item Set";
		} else if (type.equals("zofar:matrixMultipleChoice")) {
			// multiple choice
			questionTypeDE = "Matrix";
			questionTypeENG = "Grid";
		} else if (type.equals("zofar:comparison")) {
			// Vergleich
			questionTypeDE = "Itembatterie";
			questionTypeENG = "Item Set";
		} else if (type.equals("zofar:calendar")) {
			// calendar
			questionTypeDE = "Itembatterie";
			questionTypeENG = "Item Set";
		}

		else {
			System.out.println("Unhandled Type : " + type);
		}

		final Map<String, String> typeMap = new LinkedHashMap<String, String>();
		typeMap.put("de", questionTypeDE);
		typeMap.put("en", questionTypeENG);
		jsonQuestion.put("type", typeMap);

		// final String emptyStr = "EMPTY";

		final Map<String, Set<String>> emptyStr = new HashMap<String, Set<String>>();
		final Set<String> deSet = new HashSet<String>();
		deSet.add("Kein Fragetext vorhanden");
		emptyStr.put("de", deSet);

		final Set<String> enSet = new HashSet<String>();
		enSet.add("No questiontext available");
		emptyStr.put("en", enSet);

		Map<String, String> implodedQuestionTexts = new LinkedHashMap<String, String>();
		for (Entry<String, Set<String>> item : questionTexts.entrySet()) {
			String tmp = CollectionClient.getInstance().implode(emptyStr.get(item.getKey()));
			if (!item.getValue().isEmpty())
				tmp = CollectionClient.getInstance().implode(item.getValue());
			if (tmp == null)
				continue;
			tmp = tmp.trim();
			if (tmp.equals(""))
				continue;
			implodedQuestionTexts.put(item.getKey(), tmp);
		}

		if (implodedQuestionTexts.isEmpty()) {
			for (Entry<String, Set<String>> item : emptyStr.entrySet()) {
				implodedQuestionTexts.put(item.getKey(), CollectionClient.getInstance().implode(item.getValue()));
			}
		}

		jsonQuestion.put("questionText", implodedQuestionTexts);

		// [TODO] Concat Strings by \n
		
		if ((instructionTexts != null)&&(!instructionTexts.isEmpty())) {
			if (instructionTexts.containsKey("de"))
				jsonQuestion.put("instruction.de",
						CollectionClient.getInstance().implode(instructionTexts.get("de")));
			if (instructionTexts.containsKey("en"))
				jsonQuestion.put("instruction.en",
						CollectionClient.getInstance().implode(instructionTexts.get("en")));
		}

//		jsonQuestion.put("instruction", instructionTexts);
		
		if ((introductionTexts != null)&&(!introductionTexts.isEmpty())) {
			if (introductionTexts.containsKey("de"))
				jsonQuestion.put("introduction.de",
						CollectionClient.getInstance().implode(introductionTexts.get("de")));
			if (introductionTexts.containsKey("en"))
				jsonQuestion.put("introduction.en",
						CollectionClient.getInstance().implode(introductionTexts.get("en")));
		}
		
//		jsonQuestion.put("introduction", introductionTexts);

		Map<String, Object> back = new HashMap<String, Object>();

		back.put("question", jsonQuestion);

		final List<XmlObject> variables = getVariablesFromObj(node);
		if (variables != null) {
			final List<Map<String, Object>> csvVariables = new ArrayList<Map<String, Object>>();
			for (final XmlObject variable : variables) {
				final Map<String, Object> csvVariable = new LinkedHashMap<String, Object>();

				csvVariable.put("variablen_id", variable.newCursor().getTextValue());

				csvVariable.put("question_number", qid);
				csvVariable.put("instrumentId", instrumentId);

				Map<String, Set<String>> relatedStrings = getVariableRelated(variable, translations);
				if (relatedStrings != null) {
					if (relatedStrings.containsKey("de"))
						csvVariable.put("relatedQuestionStrings.de",
								CollectionClient.getInstance().implode(relatedStrings.get("de")));
					if (relatedStrings.containsKey("en"))
						csvVariable.put("relatedQuestionStrings.en",
								CollectionClient.getInstance().implode(relatedStrings.get("en")));
				}
				csvVariables.add(csvVariable);
			}
			back.put("variable", csvVariables);
		}

		return back;
	}

	// Generate String including all Texts in Question for JSON
	private Map<String, Set<String>> getAdditional(final XmlObject question,
			final Map<String, Properties> translations) {
		Map<String, Set<String>> back = new LinkedHashMap<String, Set<String>>();

		if (question == null)
			return back;

		final XmlClient xmlClient = XmlClient.getInstance();

		final XmlObject[] childs = xmlClient.getByXPath(question, "descendant::*");
		if (childs != null) {
			for (XmlObject child : childs) {
				final Class<? extends XmlObject> clazz = child.getClass();
				if ((de.his.zofar.xml.questionnaire.TextType.class).isAssignableFrom(clazz)) {
					if (translations == null) {
						// Original
						Set<String> langSet = new LinkedHashSet<String>();
						final String tmp = ReplaceClient.getInstance().cleanedString(child.newCursor().getTextValue());
						if ((tmp != null) && (!tmp.equals("")))
							langSet.add(tmp);
						back.put("de", langSet);
					} else {
						Map<Node, String> translationUids = null;

						if ((IdentificationalType.class).isAssignableFrom(child.getClass())) {
							try {
								translationUids = getTranslationUids((IdentificationalType) child);
							} catch (XmlException e) {
								e.printStackTrace();
							}
						}

						if (translationUids != null) {
							for (Map.Entry<Node, String> translationUid : translationUids.entrySet()) {
								final String uidPath = translationUid.getValue();
								if ((translations != null) && (!translations.isEmpty())) {
									for (final Map.Entry<String, Properties> translation : translations.entrySet()) {
										if (translation.getValue().containsKey(uidPath)) {
											final String language = translation.getKey();
											final String translatedText = ReplaceClient.getInstance()
													.cleanedString(translation.getValue().getProperty(uidPath));

											Set<String> langSet = null;
											if (back.containsKey(language))
												langSet = back.get(language);
											if (langSet == null)
												langSet = new LinkedHashSet<String>();
											final String tmp = translatedText;
											if ((tmp != null) && (!tmp.equals("")))
												langSet.add(tmp);
											back.put(language, langSet);
										}
									}
								}
							}
						}
					}
				} else if ((de.his.zofar.xml.questionnaire.AbstractLabeledAnswerOptionType.class)
						.isAssignableFrom(clazz)) {
					final de.his.zofar.xml.questionnaire.AbstractLabeledAnswerOptionType ao = (de.his.zofar.xml.questionnaire.AbstractLabeledAnswerOptionType) child;
					if (translations == null) {
						// Original
						Set<String> langSet = new LinkedHashSet<String>();
						final String tmp = ReplaceClient.getInstance().cleanedString(ao.getLabel2());
						if ((tmp != null) && (!tmp.equals("")))
							langSet.add(tmp);
						back.put("de", langSet);
					} else {
						Map<Node, String> translationUids = null;

						if ((IdentificationalType.class).isAssignableFrom(child.getClass())) {
							try {
								translationUids = getTranslationUids((IdentificationalType) child);
							} catch (XmlException e) {
								e.printStackTrace();
							}
						}

						if (translationUids != null) {
							for (Map.Entry<Node, String> translationUid : translationUids.entrySet()) {
								final String uidPath = translationUid.getValue();
								if ((translations != null) && (!translations.isEmpty())) {
									for (final Map.Entry<String, Properties> translation : translations.entrySet()) {
										if (translation.getValue().containsKey(uidPath)) {
											final String language = translation.getKey();
											final String translatedText = ReplaceClient.getInstance()
													.cleanedString(translation.getValue().getProperty(uidPath));

											Set<String> langSet = null;
											if (back.containsKey(language))
												langSet = back.get(language);
											if (langSet == null)
												langSet = new LinkedHashSet<String>();
											final String tmp = translatedText;
											if ((tmp != null) && (!tmp.equals("")))
												langSet.add(tmp);
											back.put(language, langSet);
										}
									}
								}
							}
						}
					}
				} else if ((de.his.zofar.xml.questionnaire.CalendarConfigurationItemType.class)
						.isAssignableFrom(clazz)) {
					System.out.println("Additional CalendarConfigurationItemType");
					final de.his.zofar.xml.questionnaire.CalendarConfigurationItemType calConfItem = (de.his.zofar.xml.questionnaire.CalendarConfigurationItemType) child;
					if (translations == null) {
						// Original
						Set<String> langSet = new LinkedHashSet<String>();
						final String tmp = ReplaceClient.getInstance().cleanedString(calConfItem.getLabel());
						if ((tmp != null) && (!tmp.equals("")))
							langSet.add(tmp);
						if (!back.containsKey("de"))
							back.put("de", langSet);
						else
							back.get("de").addAll(langSet);
					} else {
						Set<String> langSet = new LinkedHashSet<String>();
						final String tmp = ReplaceClient.getInstance().cleanedString(calConfItem.getLabel());
						if ((tmp != null) && (!tmp.equals("")))
							langSet.add(tmp);
						if (!back.containsKey("de"))
							back.put("de", langSet);
						else
							back.get("de").addAll(langSet);
					}
				} else if ((de.his.zofar.xml.questionnaire.CalendarType.class).isAssignableFrom(clazz)) {
					System.out.println("Additional CalendarType");
					final de.his.zofar.xml.questionnaire.CalendarType cal = (de.his.zofar.xml.questionnaire.CalendarType) child;
					if (translations == null) {
						// Original
						Set<String> langSet = new LinkedHashSet<String>();
						final String columns = ReplaceClient.getInstance().cleanedString(cal.getColumns());
						if ((columns != null) && (!columns.equals("")))
							langSet.add(columns);
						final String rows = ReplaceClient.getInstance().cleanedString(cal.getRows());
						if ((rows != null) && (!rows.equals("")))
							langSet.add(rows);
						back.put("de", langSet);
					} else {
						Set<String> langSet = new LinkedHashSet<String>();
						final String columns = ReplaceClient.getInstance().cleanedString(cal.getColumns());
						if ((columns != null) && (!columns.equals("")))
							langSet.add(columns);
						final String rows = ReplaceClient.getInstance().cleanedString(cal.getRows());
						if ((rows != null) && (!rows.equals("")))
							langSet.add(rows);
						back.put("de", langSet);
					}
				}
			}
		}
		return back;
	}

	// Generate String including all Variable related Texts in Question for CSV
	private Map<String, Set<String>> getVariableRelated(final XmlObject xml,
			final Map<String, Properties> translations) {
		Map<String, Set<String>> back = new LinkedHashMap<String, Set<String>>();
		final Set<String> fields = new HashSet<String>();
		fields.add("question");
		fields.add("label");
		fields.add("prefix");
		fields.add("postfix");
		fields.add("title");
		fields.add("introduction");

		final XmlObject parent = XmlClient.getInstance().getParent(xml);
		if (parent != null) {
			final Map<String, Set<String>> parentMap = getVariableRelated(parent, translations);
			if (parentMap != null) {
				for (Map.Entry<String, Set<String>> entry : parentMap.entrySet()) {
					Set<String> langSet = null;
					if (back.containsKey(entry.getKey()))
						langSet = back.get(entry.getKey());
					if (langSet == null)
						langSet = new LinkedHashSet<String>();
					langSet.addAll(entry.getValue());
					back.put(entry.getKey(), langSet);
				}
			}
		}
		if ((IdentificationalType.class).isAssignableFrom(xml.getClass())) {
			boolean recursive = false;
			if (xml.getDomNode().getNodeName().equals("zofar:questionOpen"))
				recursive = true;
			final Map<String, Set<String>> headerMap = getHeaderHelper(xml, fields, translations, recursive);
			if (headerMap != null) {
				for (Map.Entry<String, Set<String>> entry : headerMap.entrySet()) {
					Set<String> langSet = null;
					if (back.containsKey(entry.getKey()))
						langSet = back.get(entry.getKey());
					if (langSet == null)
						langSet = new LinkedHashSet<String>();
					langSet.addAll(entry.getValue());
					back.put(entry.getKey(), langSet);
				}
			}
		}
		return back;
	}

	// Get the Question Elements within a Header-Element without recursive
	// Child-Scan and parental Escalation
	private Map<String, Set<String>> getQuestionHeader(final XmlObject question,
			final Map<String, Properties> translations) {
		Set<String> fields = new HashSet<String>();
		fields.add("question");
		Map<String, Set<String>> back = this.retrieveHeaderField(question, fields, translations, false);
		return back;
	}

	// Get all Instruction Elements within a Header-Element without recursive
	// Child-Scan but with parental Escalation
	private Map<String, Set<String>> getInstructionHeader(final XmlObject question,
			final Map<String, Properties> translations) {
		Set<String> fields = new HashSet<String>();
		fields.add("instruction");
		return getHeaderHelper(question, fields, translations, false);
	}

	// Get all Introduction Elements within a Header-Element without recursive
	// Child-Scan but with parental Escalation
	private Map<String, Set<String>> getIntroductionHeader(final XmlObject question,
			final Map<String, Properties> translations) {
		Set<String> fields = new HashSet<String>();
		fields.add("introduction");
		return getHeaderHelper(question, fields, translations, false);
	}

	/**
	 * Gets the questions helper.
	 * 
	 * @param node
	 *            the node
	 * @return the questions helper
	 */
	// private Map<String, Set<String>> getHeaderHelper(final XmlObject node, final
	// Set<String> fields, final Map<String, Properties> translations) {
	// return this.getHeaderHelper(node, fields, translations, false);
	// }

	private Map<String, Set<String>> getHeaderHelper(final XmlObject node, final Set<String> fields,
			final Map<String, Properties> translations, final boolean recursive) {
		final Map<String, Set<String>> back = new LinkedHashMap<String, Set<String>>();
		if (node == null)
			return back;
		final XmlClient client = XmlClient.getInstance();

		Map<String, Set<String>> parentMap = getHeaderHelper(client.getParent(node), fields, translations, recursive);

		// clean from dublettes
		for (Map.Entry<String, Set<String>> item : parentMap.entrySet()) {
			final Set<String> existingSet = back.get(item.getKey());
			if (existingSet == null)
				continue;
			if (existingSet.isEmpty())
				continue;
			Set<String> currentSet = item.getValue();
			if (currentSet == null)
				continue;
			if (currentSet.isEmpty())
				continue;
			final Iterator<String> it = currentSet.iterator();
			while (it.hasNext()) {
				if (existingSet.contains(it.next()))
					it.remove();
			}
			item.setValue(currentSet);
		}

		back.putAll(parentMap);

		back.putAll(retrieveHeaderField(node, fields, translations, recursive));
		return back;
	}

	/**
	 * Retrieve header.
	 * 
	 * @param node
	 *            the parent node
	 * @return the sets the
	 */
	private Map<String, Set<String>> retrieveHeaderField(final XmlObject node, final Set<String> headerFields,
			final Map<String, Properties> translations, final boolean recursive) {
		final Map<String, Set<String>> back = new LinkedHashMap<String, Set<String>>();

		String pattern = "./*";
		if (recursive)
			pattern = "descendant::*";
		final XmlObject[] childs = XmlClient.getInstance().getByXPath(node, pattern);
		if (childs != null) {
			for (XmlObject child : childs) {
				boolean notinterrupt = ((child.getDomNode().getNodeName().equals("zofar:header"))
						|| (headerFields.contains(child.getDomNode().getNodeName().replace("zofar:", ""))));
				// if (!child.getDomNode().getNodeName().equals("zofar:header"))
				// continue;

				if (!notinterrupt)
					continue;

				final XmlObject[] headerItems = child.selectPath("descendant::*");
				for (XmlObject headerItem : headerItems) {
					final String headerItemType = headerItem.getDomNode().getNodeName().replace("zofar:", "");
					if (!headerFields.contains(headerItemType))
						continue;

					if (translations == null) {
						// Original
						Set<String> langSet = new LinkedHashSet<String>();
						final String tmp = ReplaceClient.getInstance()
								.cleanedString(headerItem.newCursor().getTextValue());
						if ((tmp != null) && (!tmp.equals("")))
							langSet.add(tmp);
						back.put("de", langSet);
					} else {
						Map<Node, String> translationUids = null;

						if ((IdentificationalType.class).isAssignableFrom(headerItem.getClass())) {
							try {
								translationUids = getTranslationUids((IdentificationalType) headerItem);
							} catch (XmlException e) {
								e.printStackTrace();
							}
						}

						if (translationUids != null) {
							for (Map.Entry<Node, String> translationUid : translationUids.entrySet()) {
								final String uidPath = translationUid.getValue();
								if ((translations != null) && (!translations.isEmpty())) {
									for (final Map.Entry<String, Properties> translation : translations.entrySet()) {
										if (translation.getValue().containsKey(uidPath)) {
											final String language = translation.getKey();
											final String translatedText = ReplaceClient.getInstance()
													.cleanedString(translation.getValue().getProperty(uidPath));

											Set<String> langSet = null;
											if (back.containsKey(language))
												langSet = back.get(language);
											if (langSet == null)
												langSet = new LinkedHashSet<String>();
											final String tmp = translatedText;
											if ((tmp != null) && (!tmp.equals("")))
												langSet.add(tmp);
											back.put(language, langSet);
										}
									}
								}
							}
						}
					}
				}
			}
		}

		final XmlObject[] attributes = XmlClient.getInstance().getByXPath(node, "./@*");
		if (attributes != null) {

			for (XmlObject attribute : attributes) {
				final String attributeType = attribute.getDomNode().getNodeName();
				if (!headerFields.contains(attributeType))
					continue;

				if (translations == null) {
					// Original
					Set<String> langSet = new LinkedHashSet<String>();
					final String tmp = ReplaceClient.getInstance().cleanedString(attribute.newCursor().getTextValue());
					if ((tmp != null) && (!tmp.equals("")))
						langSet.add(tmp);
					back.put("de", langSet);
				} else {
					Map<Node, String> translationUids = null;

					if ((IdentificationalType.class).isAssignableFrom(node.getClass())) {
						try {
							translationUids = getTranslationUids((IdentificationalType) node);
						} catch (XmlException e) {
							e.printStackTrace();
						}
					}

					if (translationUids != null) {
						for (Map.Entry<Node, String> translationUid : translationUids.entrySet()) {
							for (final Map.Entry<String, Properties> translation : translations.entrySet()) {
								if (translation.getValue().containsKey(translationUid.getValue())) {
									final String language = translation.getKey();
									final String translatedText = ReplaceClient.getInstance().cleanedString(
											translation.getValue().getProperty(translationUid.getValue()));
									Set<String> langSet = null;
									if (back.containsKey(language))
										langSet = back.get(language);
									if (langSet == null)
										langSet = new LinkedHashSet<String>();
									final String tmp = translatedText;
									if ((tmp != null) && (!tmp.equals("")))
										langSet.add(tmp);
									back.put(language, langSet);
								}
							}
						}
					}
				}
			}
		}
		return back;
	}

	// private Map<String, Set<String>> retrieveHeaderField(final XmlObject
	// node, final Set<String> headerFields, final Map<String, Properties>
	// translations) {
	// final Map<String, Set<String>> back = new LinkedHashMap<String,
	// Set<String>>();
	// final XmlObject[] childs = XmlClient.getInstance().getByXPath(node,
	// "./*");
	// if (childs != null) {
	// for (XmlObject child : childs) {
	// if (!child.getDomNode().getNodeName().equals("zofar:header"))
	// continue;
	// final XmlObject[] headerItems = child.selectPath("./*");
	// for (XmlObject headerItem : headerItems) {
	// final String headerItemType =
	// headerItem.getDomNode().getNodeName().replace("zofar:", "");
	// if (!headerFields.contains(headerItemType))
	// continue;
	//
	// if (translations == null) {
	// // Original
	// Set<String> langSet = new LinkedHashSet<String>();
	// final String tmp =
	// ReplaceClient.getInstance().cleanedString(headerItem.newCursor().getTextValue());
	// if ((tmp != null) && (!tmp.equals("")))
	// langSet.add(tmp);
	// back.put("de", langSet);
	// } else {
	// Map<Node, String> translationUids = null;
	//
	// if ((IdentificationalType.class).isAssignableFrom(headerItem.getClass()))
	// {
	// try {
	// translationUids = getTranslationUids((IdentificationalType) headerItem);
	// } catch (XmlException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// if (translationUids != null) {
	// for (Map.Entry<Node, String> translationUid : translationUids.entrySet())
	// {
	// final String uidPath = translationUid.getValue();
	// if ((translations != null) && (!translations.isEmpty())) {
	// for (final Map.Entry<String, Properties> translation :
	// translations.entrySet()) {
	// if (translation.getValue().containsKey(uidPath)) {
	// final String language = translation.getKey();
	// final String translatedText =
	// ReplaceClient.getInstance().cleanedString(translation.getValue().getProperty(uidPath));
	//
	// Set<String> langSet = null;
	// if (back.containsKey(language))
	// langSet = back.get(language);
	// if (langSet == null)
	// langSet = new LinkedHashSet<String>();
	// final String tmp = translatedText;
	// if ((tmp != null) && (!tmp.equals("")))
	// langSet.add(tmp);
	// back.put(language, langSet);
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	//
	// final XmlObject[] attributes = XmlClient.getInstance().getByXPath(node,
	// "./@*");
	// if (attributes != null) {
	//
	// for (XmlObject attribute : attributes) {
	// final String attributeType = attribute.getDomNode().getNodeName();
	// if (!headerFields.contains(attributeType))
	// continue;
	//
	// if (translations == null) {
	// // Original
	// Set<String> langSet = new LinkedHashSet<String>();
	// final String tmp =
	// ReplaceClient.getInstance().cleanedString(attribute.newCursor().getTextValue());
	// if ((tmp != null) && (!tmp.equals("")))
	// langSet.add(tmp);
	// back.put("de", langSet);
	// } else {
	// Map<Node, String> translationUids = null;
	//
	// if ((IdentificationalType.class).isAssignableFrom(node.getClass())) {
	// try {
	// translationUids = getTranslationUids((IdentificationalType) node);
	// } catch (XmlException e) {
	// e.printStackTrace();
	// }
	// }
	//
	// if (translationUids != null) {
	// for (Map.Entry<Node, String> translationUid : translationUids.entrySet())
	// {
	// for (final Map.Entry<String, Properties> translation :
	// translations.entrySet()) {
	// if (translation.getValue().containsKey(translationUid.getValue())) {
	// final String language = translation.getKey();
	// final String translatedText =
	// ReplaceClient.getInstance().cleanedString(translation.getValue().getProperty(translationUid.getValue()));
	// Set<String> langSet = null;
	// if (back.containsKey(language))
	// langSet = back.get(language);
	// if (langSet == null)
	// langSet = new LinkedHashSet<String>();
	// final String tmp = translatedText;
	// if ((tmp != null) && (!tmp.equals("")))
	// langSet.add(tmp);
	// back.put(language, langSet);
	// }
	// }
	// }
	// }
	// }
	// }
	// }
	// return back;
	// }

	private Map<Node, String> getTranslationUids(final IdentificationalType source) throws XmlException {
		return getTranslationUids(0, source.getDomNode(), generateTranslationUidHelper(source));
	}

	private Map<Node, String> getTranslationUids(int lft, final Node node, final String sourcePath)
			throws XmlException {
		if (node == null)
			return null;
		final String nodeName = node.getNodeName();
		final Map<Node, String> back = new HashMap<Node, String>();
		if (nodeName.equals("#text")) {
			final String uidPath = sourcePath + "_" + lft;
			back.put(node, uidPath);
		} else {
			NodeList childs = node.getChildNodes();
			final int count = childs.getLength();
			for (int a = 0; a < count; a++) {
				final Node child = childs.item(a);
				back.putAll(getTranslationUids(lft + a, child, sourcePath));
			}

			NamedNodeMap attributeChilds = node.getAttributes();
			if (attributeChilds != null) {
				final int attributeCount = attributeChilds.getLength();
				for (int a = 0; a < attributeCount; a++) {
					final Node attribute = attributeChilds.item(a);
					final String attributeName = attribute.getNodeName();
					if (attributeName.equals("label")) {
						back.put(attribute, sourcePath + UID_DELIMITER + attributeName);
					}
				}
			}
		}
		return back;
	}

	private static final String UID_DELIMITER = ".";

	private String generateTranslationUidHelper(final IdentificationalType identificational) throws XmlException {
		final String uidAttribute = "uid";
		final StringBuilder completeUid = new StringBuilder(identificational.getUid().trim());

		Node parentNode = identificational.getDomNode().getParentNode();
		while (parentNode != null) {
			if (parentNode.getNodeType() == Node.ELEMENT_NODE && parentNode.hasAttributes()) {
				final Node uidNode = parentNode.getAttributes().getNamedItem(uidAttribute);
				if (uidNode != null) {
					final String parentUid = uidNode.getNodeValue().trim();
					completeUid.insert(0, parentUid + UID_DELIMITER);
				}
			}
			parentNode = parentNode.getParentNode();
		}

		return completeUid.toString();
	}

	private List<XmlObject> getNext(final XmlObject current, XmlObject xmlDoc) {
		final List<XmlObject> back = new ArrayList<XmlObject>();
		if (current == null)
			return back;
		final String type = current.getDomNode().getNodeName();
//		System.out.println("type : " + type);
//		String pageId = "UNKOWN";
//		if (type.equals("zofar:page"))
//			pageId = XmlClient.getInstance().getAttribute(current, "uid");
//		else {
//			final XmlObject page = XmlClient.getInstance().getParent(current, "zofar:page");
//			if (page != null)
//				pageId = XmlClient.getInstance().getAttribute(page, "uid");
//		}
//
//		if (!pageId.equals("page5"))
//			return back;

		if (type.equals("zofar:page")) {
			// page switch
			List<XmlObject> trans = getTransitionsFrom(current, xmlDoc);
			if (trans != null) {
				for (final XmlObject nextPage : trans) {
					XmlObject qObj = getQuestionFromObj(nextPage);
					if (qObj != null) {
						back.add(qObj);
					}
				}
			}
		} else {
			final XmlClient client = XmlClient.getInstance();
			XmlObject sibling = client.getNextSibling(current);

			while (sibling != null) {
				XmlObject siblingQuestion = getQuestionFromObj(sibling);
				if (siblingQuestion != null) {
					back.add(siblingQuestion);
					break;
				} else {
					sibling = client.getNextSibling(sibling);
				}
			}
			
			if(sibling == null) {
				back.addAll(getNext(client.getNextSibling(client.getParent(current)), xmlDoc));
			}

			// if (sibling != null) {
			// XmlObject siblingQuestion = getQuestionFromObj(sibling);
			// if (siblingQuestion != null) {
			// back.add(siblingQuestion);
			// } else {
			// XmlObject next = client.getNextSibling(sibling);
			// if (next == null)
			// next = client.getParent(sibling);
			// else {
			//
			// }
			//// back.addAll(getNext(next, xmlDoc));
			// }
			// } else {
			// back.addAll(getNext(client.getParent(current), xmlDoc));
			// }

		}
		return back;
	}

	// private List<XmlObject> getNext(final XmlObject current, XmlObject xmlDoc) {
	// final List<XmlObject> back = new ArrayList<XmlObject>();
	// if (current == null)
	// return back;
	// final String type = current.getDomNode().getNodeName();
	//
	// String pageId = "UNKOWN";
	// if (type.equals("zofar:page"))
	// pageId = XmlClient.getInstance().getAttribute(current, "uid");
	// else {
	// final XmlObject page = XmlClient.getInstance().getParent(current,
	// "zofar:page");
	// if (page != null)
	// pageId = XmlClient.getInstance().getAttribute(page, "uid");
	// }
	// System.out.println("pageId : " + pageId);
	//
	// if(!pageId.equals("page5"))return back;
	//
	// if (type.equals("zofar:page")) {
	// // page switch
	// List<XmlObject> trans = getTransitionsFrom(current, xmlDoc);
	// if (trans != null) {
	// for (final XmlObject nextPage : trans) {
	// XmlObject qObj = getQuestionFromObj(nextPage);
	// if (qObj != null) {
	// back.add(qObj);
	// }
	// }
	// }
	// } else {
	// final XmlClient client = XmlClient.getInstance();
	// final XmlObject sibling = client.getNextSibling(current);
	// if (sibling != null) {
	// XmlObject siblingQuestion = getQuestionFromObj(sibling);
	// if (siblingQuestion != null) {
	// back.add(siblingQuestion);
	// } else {
	// XmlObject next = client.getNextSibling(sibling);
	// if (next == null)
	// next = client.getParent(sibling);
	// back.addAll(getNext(next, xmlDoc));
	// }
	// } else {
	// back.addAll(getNext(client.getParent(current), xmlDoc));
	// }
	// }
	// return back;
	// }

	// private List<XmlObject> getNext(final XmlObject current, XmlObject
	// xmlDoc) {
	// if (current == null)
	// return null;
	// final XmlClient client = XmlClient.getInstance();
	// List<XmlObject> back = new ArrayList<XmlObject>();
	//
	// XmlObject tmp = current;
	//
	// XmlObject item = client.getNextSibling(tmp);
	// while (tmp != null) {
	// final String tmpType = tmp.getDomNode().getNodeName();
	// System.out.println("tmpType : "+tmpType);
	// if (tmpType.equals("zofar:page")) {
	// // page switch
	// List<XmlObject> trans = getTransitionsFrom(tmp, xmlDoc);
	// if (trans != null) {
	// for (final XmlObject nextPage : trans) {
	// System.out.println("next1 : "+nextPage.getClass().getName());
	//// XmlObject qObj = getQuestionObjOnPage(nextPage, false);
	//// if (qObj != null) {
	//// back.add(qObj);
	//// }
	// }
	// }
	// break;
	// } else {
	// item = client.getNextSibling(tmp);
	// if (item != null) {
	// System.out.println("next2 : "+item.getClass().getName());
	// // XmlObject qObj = getQuestionObjOnPage(item, false);
	// // if (qObj != null) {
	// // back.add(qObj);
	// // break;
	// // }
	// }
	// tmp = client.getParent(tmp);
	// }
	// }
	// return back;
	// }

	// GET Path of UIDs escalated to Top Element of Type IdentificationalType
	private String getUIDPath(final XmlObject child) {
		if (child == null)
			return null;
		XmlObject parent = child;
		String back = "";
		while (parent != null) {
			if ((IdentificationalType.class).isAssignableFrom(parent.getClass())) {
				if (!back.equals(""))
					back = ":" + back;
				back = ((IdentificationalType) parent).getUid() + back;
			}

			parent = XmlClient.getInstance().getParent(parent);
		}
		return back;
	}

	// private XmlObject getVariableObjOnPage(XmlObject obj, final boolean last)
	// {
	// if (obj == null)
	// return null;
	// final XmlClient client = XmlClient.getInstance();
	// XmlObject[] resultSet = client.getByXPath(obj,
	// "descendant::*/@variable");
	// if ((resultSet != null) && (resultSet.length > 0)) {
	// final int count = resultSet.length;
	// if (last)
	// return resultSet[count - 1];
	// else
	// return resultSet[0];
	// }
	// return null;
	// }

	// private XmlObject getParentQuestion(final XmlObject child) {
	// if (child == null)
	// return null;
	// XmlObject parent = XmlClient.getInstance().getParent(child);
	// while (parent != null) {
	// System.out.println("parent type : "+parent.getDomNode().getNodeName()+"
	// ("+getUIDPath(parent)+")");
	// //skip Item
	// if(parent.getDomNode().getNodeName().equals("zofar:item")){
	// parent = XmlClient.getInstance().getParent(parent);
	// if(parent == null)break;
	// }
	//
	// if ((AbstractQuestionType.class).isAssignableFrom(parent.getClass()))
	// return parent;
	// else if ((AbstractMatrixType.class).isAssignableFrom(parent.getClass()))
	// return parent;
	// else{
	// System.out.println("## type : "+parent.getDomNode().getNodeName()+"
	// ("+getUIDPath(parent)+")");
	// parent = XmlClient.getInstance().getParent(parent);
	//// System.out.println("next parent type :
	// "+parent.getDomNode().getNodeName()+"
	// ("+parent.getClass().getName()+")");
	// }
	// }
	// return child;
	// }

	// private XmlObject getParentQuestion(final XmlObject child) {
	// if (child == null)
	// return null;
	// XmlClient client = XmlClient.getInstance();
	// XmlObject parent = client.getParent(child);
	// while (parent != null) {
	// final String type = parent.getDomNode().getNodeName();
	// if (!client.hasParent(parent.getDomNode(), "zofar:page"))
	// return null;
	// if (type.equals("zofar:item")) {
	// parent = XmlClient.getInstance().getParent(parent);
	// if (parent == null)
	// break;
	// }
	// if ((AbstractQuestionType.class).isAssignableFrom(parent.getClass()))
	// return parent;
	// if ((AbstractMatrixType.class).isAssignableFrom(parent.getClass()))
	// return parent;
	//
	// parent = client.getParent(parent);
	// }
	// return child;
	// }

	public boolean hasParentDEBUG(final Node node, final String parentName) {
		if (node == null)
			return false;
		String nodename = node.getNodeName();
		if (!nodename.startsWith("zofar:"))
			nodename = "zofar:" + nodename;

		if (nodename.equals(parentName))
			return true;
		return hasParentDEBUG(node.getParentNode(), parentName);
	}

	private XmlObject getParentQuestion(final XmlObject child) {
		if (child == null)
			return null;
		final XmlClient client = XmlClient.getInstance();

		final List<XmlObject> questionPath = new ArrayList<XmlObject>();
		questionPath.add(child);
		XmlObject parent = client.getParent(child);
		while (parent != null) {
			if (!hasParentDEBUG(parent.getDomNode(), "zofar:page"))
				break;
			questionPath.add(parent);
			parent = client.getParent(parent);
		}
		Collections.reverse(questionPath);
		for (final XmlObject item : questionPath) {
			if ((AbstractQuestionType.class).isAssignableFrom(item.getClass()))
				return item;
			if ((AbstractMatrixType.class).isAssignableFrom(item.getClass()))
				return item;
			if ((CalendarType.class).isAssignableFrom(item.getClass()))
				return item;
		}

		return child;
	}

	private XmlObject getQuestionFromObj(XmlObject obj) {
		List<XmlObject> tmp = getQuestionsFromObj(obj);
		if ((tmp != null) && (!tmp.isEmpty()))
			return tmp.get(0);
		return null;
	}

	// private List<XmlObject> getQuestionsFromObj(XmlObject obj) {
	// if (obj == null)
	// return null;
	// final XmlClient client = XmlClient.getInstance();
	// XmlObject[] resultSet = client.getByXPath(obj,
	// "descendant::*/@variable");
	// if ((resultSet != null) && (resultSet.length > 0)) {
	// List<XmlObject> back = new ArrayList<XmlObject>();
	// for (final XmlObject item : resultSet) {
	// XmlObject parent = getParentQuestion(item);
	// if (parent != null) {
	// if (parent.getDomNode().getNodeName().equals("zofar:question"))
	// parent = getParentQuestion(parent);
	// if ((!back.contains(parent)))
	// back.add(parent);
	// }
	// }
	// return back;
	// }
	// return null;
	// }

	private List<XmlObject> getQuestionsFromObj(XmlObject obj) {
		if (obj == null)
			return null;
		final XmlClient client = XmlClient.getInstance();
		XmlObject[] resultSet = client.getByXPath(obj, "descendant::*/@variable");
		if ((resultSet != null) && (resultSet.length > 0)) {
			List<XmlObject> back = new ArrayList<XmlObject>();
			for (final XmlObject item : resultSet) {
				XmlObject parent = getParentQuestion(item);
				if (parent != null) {
					if (parent.getDomNode().getNodeName().equals("zofar:question"))
						parent = getParentQuestion(parent);
					if (parent.getDomNode().getNodeName().equals("question"))
						parent = getParentQuestion(parent);
					if ((!back.contains(parent))) {
						back.add(parent);
					}
				}
			}
			return back;
		}
		return null;
	}

	// private List<String> getVariablesFromObj(XmlObject obj) {
	// if (obj == null)
	// return null;
	// final XmlClient client = XmlClient.getInstance();
	// XmlObject[] resultSet = client.getByXPath(obj,
	// "descendant::*/@variable");
	// if ((resultSet != null) && (resultSet.length > 0)) {
	// List<String> back = new ArrayList<String>();
	// for (final XmlObject item : resultSet) {
	// back.add(item.getDomNode().getNodeValue());
	// }
	// return back;
	// }
	// return null;
	// }

	private List<XmlObject> getVariablesFromObj(XmlObject obj) {
		if (obj == null)
			return null;
		final XmlClient client = XmlClient.getInstance();
		XmlObject[] resultSet = client.getByXPath(obj, "descendant::*/@variable");
		if ((resultSet != null) && (resultSet.length > 0)) {
			List<XmlObject> back = new ArrayList<XmlObject>();
			for (final XmlObject item : resultSet) {
				back.add(item);
			}
			return back;
		}
		return null;
	}

	private List<XmlObject> getTransitionsFrom(XmlObject obj, XmlObject xmlDoc) {
		if (obj == null)
			return null;
		if (!obj.getDomNode().getNodeName().equals("zofar:page"))
			return null;

		final XmlClient client = XmlClient.getInstance();
		XmlObject[] resultSet = client.getByXPath(obj, "./zofar:transitions/*/@target");
		final List<XmlObject> back = new ArrayList<XmlObject>();
		if ((resultSet != null) && (resultSet.length > 0)) {
			for (final XmlObject found : resultSet) {
				final String targetUID = found.newCursor().getTextValue();
				XmlObject[] pages = client.getByXPath(xmlDoc, "//*[@uid='" + targetUID + "']");
				if (pages != null) {
					for (XmlObject page : pages) {
						if (page.getDomNode().getNodeName().equals("zofar:page"))
							back.add(page);
					}
				}
			}
		}
		return back;
	}
}
