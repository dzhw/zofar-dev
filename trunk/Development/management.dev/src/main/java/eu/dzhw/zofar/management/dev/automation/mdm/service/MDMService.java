package eu.dzhw.zofar.management.dev.automation.mdm.service;

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
import eu.dzhw.zofar.management.dev.qml.QMLClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.files.PackagerClient;
import eu.dzhw.zofar.management.utils.objects.CollectionClient;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import eu.dzhw.zofar.management.utils.string.StringUtils;
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
		if (INSTANCE == null) {
			INSTANCE = new MDMService();
		}
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

	public Map<String, Object> buildUpInstrument(final String qmlPath, final File screenshotFile) throws Exception {
		final Map<String, Object> back = new LinkedHashMap<String, Object>();

		final Map<String, Map<String, Object>> screenshots = new HashMap<String, Map<String, Object>>();

		if (screenshotFile != null) {
			final List<File> files = PackagerClient.getInstance().extractZip(screenshotFile);
			final FileClient fileClient = FileClient.getInstance();
			if (files != null) {
				for (final File file : files) {
					String name = file.getName();
					final String suffix = fileClient.getSuffix(file);
					if (suffix != null) {
						name = name.replaceAll(Pattern.quote("." + suffix), "");
					}

					String[] nameParts = null;
					if (name != null) {
						nameParts = name.split("##");
					}
					if (nameParts != null) {
						String screenshotPage = "UNKOWN";
						String resolution = "UNKOWN";
						String language = "de";

						final int partCount = nameParts.length;
						// name
						if (partCount >= 1) {
							screenshotPage = nameParts[0];
							if (screenshotPage.indexOf('_') > -1) {
								screenshotPage = screenshotPage.substring(0, screenshotPage.indexOf('_'));
							}
						}
						// resolution
						if (partCount >= 2) {
							resolution = nameParts[1];
						}
						// language
						if (partCount >= 3) {
							language = nameParts[2];
						}

						System.out.println("PageName : " + screenshotPage);
						System.out.println("Resolution : " + resolution);
						System.out.println("Language : " + language);

						Map<String, Object> resolutionMap = null;
						if (screenshots.containsKey(screenshotPage)) {
							resolutionMap = screenshots.get(screenshotPage);
						}
						if (resolutionMap == null) {
							resolutionMap = new HashMap<String, Object>();
						}

						Map<String, Object> languageMap = null;
						if (resolutionMap.containsKey(resolution)) {
							languageMap = (Map<String, Object>) resolutionMap.get(resolution);
						}
						if (languageMap == null) {
							languageMap = new HashMap<String, Object>();
						}

						languageMap.put(language, file);
						resolutionMap.put(resolution, languageMap);

						screenshots.put(screenshotPage, resolutionMap);
					}
				}
			}
		}

		// Load QML

		final Map<String, Object> imagesBack = new LinkedHashMap<String, Object>();
		final XmlObject doc = docToQmlObject(XmlClient.getInstance().getDocument(qmlPath));
		final PageType[] pages = ((QuestionnaireDocument) doc).getQuestionnaire().getPageArray();
		if (pages != null) {
			final FileClient fileClient = FileClient.getInstance();

			for (final PageType page : pages) {
				final String pageID = page.getUid();
				Map<String, Object> images = null;
				if (screenshots.containsKey(pageID)) {
					images = screenshots.get(pageID);
				}

				if (images != null) {
					imagesBack.put(pageID, images);

				} else {
					System.out.println("no screenshot found for page: " + pageID);
				}
			}
		}
		back.put("qml", doc);
		back.put("images", imagesBack);
		return back;
	}

	public Map<String, Object> buildUpQuestion(final String qmlPath, final Map<String, Properties> translations,
			final File screenshotFile, final String dataAcquestionProject, final String surveyId,
			final String instrumentId, final String datasetId) throws Exception {
		final Map<String, Object> back = new LinkedHashMap<String, Object>();

		// // Import Screenshots
		// final Map<String, Map<String, Object>> questionScreenshots = new
		// HashMap<String, Map<String, Object>>();
		// final Map<String, Map<String, Object>> pageScreenshots = new HashMap<String,
		// Map<String, Object>>();
		final Map<String, Map<String, Object>> screenshots = new HashMap<String, Map<String, Object>>();
		//
		if (screenshotFile != null) {
			final List<File> files = PackagerClient.getInstance().extractZip(screenshotFile);
			final FileClient fileClient = FileClient.getInstance();
			if (files != null) {
				for (final File file : files) {
					String name = file.getName();
					final String suffix = fileClient.getSuffix(file);
					if (suffix != null) {
						name = name.replaceAll(Pattern.quote("." + suffix), "");
					}
					// System.out.println("filename : " + name);
					String[] nameParts = null;
					if (name != null) {
						nameParts = name.split("##");
					}
					if (nameParts != null) {
						String screenshotQuestion = "UNKOWN";
						String screenshotPage = "UNKOWN";
						String resolution = "UNKOWN";
						String language = "de";

						final int partCount = nameParts.length;
						// name
						if (partCount >= 1) {
							screenshotQuestion = nameParts[0];

							if (screenshotQuestion.indexOf('_') > -1) {
								screenshotPage = screenshotQuestion.substring(0, screenshotQuestion.indexOf('_'));
							} else {
								screenshotPage = screenshotQuestion;
							}
							System.out.println("pageName : " + screenshotPage);
						}
						// resolution
						if (partCount >= 2) {
							resolution = nameParts[1];
						}
						// language
						if (partCount >= 3) {
							language = nameParts[2];
						}

						// System.out.println("QuestionName : "+screenshotQuestion);
						// System.out.println("PageName : "+screenshotPage);
						// System.out.println("Resolution : "+resolution);
						// System.out.println("Language : "+language);

						Map<String, Object> resolutionMap = null;
						if (screenshots.containsKey(screenshotQuestion)) {
							resolutionMap = screenshots.get(screenshotQuestion);
						}
						if (resolutionMap == null) {
							resolutionMap = new HashMap<String, Object>();
						}

						Map<String, Object> languageMap = null;
						if (resolutionMap.containsKey(resolution)) {
							languageMap = (Map<String, Object>) resolutionMap.get(resolution);
						}
						if (languageMap == null) {
							languageMap = new HashMap<String, Object>();
						}

						languageMap.put(language, file);
						resolutionMap.put(resolution, languageMap);

						screenshots.put(screenshotQuestion, resolutionMap);
					}
				}
			}
		}

		// Load QML
		// System.out.println("qmlPath : "+qmlPath);
		final XmlObject doc = docToQmlObject(XmlClient.getInstance().getDocument(qmlPath));

		// Extract all Questions from QML-Document to an Map
		final Map<IdentificationalType, PageType> questionMap = new LinkedHashMap<IdentificationalType, PageType>();
		//
		final Map<XmlObject, Integer> instrumentPageIndexMap = new LinkedHashMap<XmlObject, Integer>();
		final Map<XmlObject, Integer> instrumentIndexMap = new LinkedHashMap<XmlObject, Integer>();
		final PageType[] pages = ((QuestionnaireDocument) doc).getQuestionnaire().getPageArray();
		final Map<PageType, List<IdentificationalType>> pageMap = new LinkedHashMap<PageType, List<IdentificationalType>>();
		if (pages != null) {
			Integer indexInInstrument = 1;
			Integer pageIndexInInstrument = 1;
			for (final PageType page : pages) {
				final List<IdentificationalType> pageQuestions = new ArrayList<IdentificationalType>();

				final List<XmlObject> questions = getQuestionsFromObj(page, false);
				if ((questions != null) && (!questions.isEmpty())) {
					for (final XmlObject question : questions) {
						if ((IdentificationalType.class).isAssignableFrom(question.getClass())) {
							final IdentificationalType tmp = (IdentificationalType) question;
							questionMap.put(tmp, page);
							pageQuestions.add(tmp);
							instrumentIndexMap.put(question, indexInInstrument);
							indexInInstrument = indexInInstrument + 1;
						}
					}
				}
				pageMap.put(page, pageQuestions);
				instrumentPageIndexMap.put(page, pageIndexInInstrument);
				pageIndexInInstrument = pageIndexInInstrument + 1;
			}
		}
		//
		final Map<String, Map<String, Object>> variablesBack = new LinkedHashMap<String, Map<String, Object>>();
		final Map<XmlObject, Map<String, Object>> questionsMap = new LinkedHashMap<XmlObject, Map<String, Object>>();
		final Map<String, Object> questionImagesBack = new LinkedHashMap<String, Object>();
		final Map<String, Object> pageImagesBack = new LinkedHashMap<String, Object>();

		final Map<XmlObject, Map<String, Object>> structuredQuestions = new HashMap<XmlObject, Map<String, Object>>();
		final Map<XmlObject, Map<String, Object>> processedQuestions = new HashMap<XmlObject, Map<String, Object>>();

		final Map<String, Integer> pageIndexMap = new HashMap<String, Integer>();
		final Map<String, String> keyMap = new HashMap<String, String>();
		for (final Map.Entry<IdentificationalType, PageType> questionPair : questionMap.entrySet()) {
			final IdentificationalType question = questionPair.getKey();
			final PageType page = questionPair.getValue();

			final String pageId = page.getUid();
			Integer pageIndex = 1;
			if (pageIndexMap.containsKey(pageId)) {
				pageIndex = pageIndexMap.get(pageId) + 1;
			}
			pageIndexMap.put(pageId, pageIndex);

			Map<String, Object> images = null;
			final String origKeyName = QMLClient.getInstance().getIdPath(question);

			String screenshotId = origKeyName;
			if (screenshotId.contains("_"))
				screenshotId = screenshotId.substring(0, screenshotId.indexOf("_"));

			if (screenshots.containsKey(screenshotId)) {
				images = screenshots.get(screenshotId);
			}

			// String keyName = origKeyName;
			String keyName = pageId + "." + pageIndex;
			keyName = keyName.replaceAll("page", "");
			keyName = StringUtils.getInstance().trim(keyName, MDMService.SMALL);

			keyMap.put(origKeyName, keyName);

			if (images != null) {
				final Map<String, Object> imageFolderForQuestion = new LinkedHashMap<String, Object>();
				int imageIndex = 0;
				for (final Map.Entry<String, Object> screenshotItem : images.entrySet()) {
					final String resolution = screenshotItem.getKey();
					final Map<String, File> resolutionImages = (Map<String, File>) screenshotItem.getValue();

					for (final Map.Entry<String, File> resolutionImageItem : resolutionImages.entrySet()) {
						final String language = resolutionImageItem.getKey();
						final File imageFile = resolutionImageItem.getValue();

						final String fileName = keyName + "_" + imageIndex;

						final Set<Object> imagePair = new LinkedHashSet<Object>();

						imagePair.add(imageFile);

						final String relativePath = "./" + fileName + "."
								+ FileClient.getInstance().getSuffix(imageFile);

						final Map<String, Object> questionImageMetadata = new LinkedHashMap<String, Object>();
						questionImageMetadata.put("imageType",
								FileClient.getInstance().getSuffix(imageFile).toUpperCase());
						questionImageMetadata.put("language", language);
						questionImageMetadata.put("file", relativePath);
						questionImageMetadata.put("resolution", resolution);
						questionImageMetadata.put("containsAnnotations", false);
						questionImageMetadata.put("indexInQuestion", imageIndex);
						questionImageMetadata.put("questionId", keyName);
						questionImageMetadata.put("dataAcquisitionProjectId", dataAcquestionProject);
						imagePair.add(JSON.createObject(questionImageMetadata));

						imageFolderForQuestion.put(fileName, imagePair);
						imageIndex = imageIndex + 1;
					}
				}
				questionImagesBack.put(keyName, imageFolderForQuestion);

			} else {
				System.out.println("no screenshot found for question: " + origKeyName);
			}

			final int index = instrumentIndexMap.get(question);

			Map<String, Set<String>> topic = getTopic(question, translations);

			final Map<String, Object> structuredData = getDataTable(doc, question, translations, index, questionMap,
					dataAcquestionProject, surveyId, instrumentId, datasetId, topic, keyName);

			structuredQuestions.put(question, structuredData);

			// add to JSON generate map
			@SuppressWarnings("unchecked")
			final Map<String, Object> questionData = (Map<String, Object>) structuredData.get("question");
			questionData.put("keyname", keyName);
			questionsMap.put(question, questionData);

			@SuppressWarnings("unchecked")
			final List<Map<String, Object>> variableData = (List<Map<String, Object>>) structuredData.get("variable");
			// variable merge
			if ((variableData != null) && (!variableData.isEmpty())) {
				final Map<String, Map<String, Object>> merged = variablesBack;
				for (final Map<String, Object> questionVar : variableData) {
					final String variable_id = (String) questionVar.get("variablen_id");

					// correct question number
					if (questionVar.containsKey("question_number")) {
						final String origQuestionName = questionVar.get("question_number") + "";
						if (keyMap.containsKey(origQuestionName))
							questionVar.put("question_number", keyMap.get(origQuestionName));
					}

					if (merged.containsKey(variable_id)) {
						final Map<String, Object> varFromMerged = merged.get(variable_id);
						for (final Map.Entry<String, Object> varFromQuestion : questionVar.entrySet()) {
							if (varFromMerged.containsKey(varFromQuestion.getKey())) {
								// detail merge
								final Object mergedValue = varFromMerged.get(varFromQuestion.getKey());

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
					} else {
						merged.put(variable_id, questionVar);
					}
				}
			}
		}

		// Correct SuccessorIDs and build JSONs
		final Map<String, JsonObject> questionsBack = new LinkedHashMap<String, JsonObject>();
		for (final Map.Entry<XmlObject, Map<String, Object>> questionsBackItem : questionsMap.entrySet()) {
			// final String keyName = questionsBackItem.getKey();
			final XmlObject question = questionsBackItem.getKey();

			final Map<String, Object> questionData = questionsBackItem.getValue();
			final String keyName = (String) questionData.get("keyname");
			questionData.remove("keyname");
			List<Object> nextPageList = null;
			if (questionData.containsKey("nextList")) {
				List<XmlObject> nextList = (List<XmlObject>) questionData.get("nextList");
				if ((nextList != null) && (!nextList.isEmpty())) {
					final List<Object> successors = new ArrayList<Object>();
					nextPageList = new ArrayList<Object>();
					for (final XmlObject next : nextList) {
						final PageType pageOfQuestion = questionMap.get(next);
						nextPageList.add(pageOfQuestion.getUid());
						if ((IdentificationalType.class).isAssignableFrom(next.getClass())) {
							final IdentificationalType xx = (IdentificationalType) next;
							String id = QMLClient.getInstance().getIdPath(xx);
							if (keyMap.containsKey(id))
								id = keyMap.get(id);
							successors.add(id);
						}
					}
					questionData.put("successorNumbers", successors);
				}
				questionData.remove("nextList");
			}
		
			// Build JSON
			final JsonObject question_json = JSON.createObject(questionData);
			questionsBack.put(keyName, question_json);
			
			if(nextPageList != null) {
				questionData.put("successorNumbers",nextPageList);
			}
			processedQuestions.put(question, questionData);

		}
		
		
		//preindexing pages 

		Map<String, String> pageKeynameMap = new HashMap<String, String>();
		Map<String, Integer> pagePageIndexMap = new HashMap<String, Integer>();
		for (final Map.Entry<PageType, List<IdentificationalType>> pageItem : pageMap.entrySet()) {
			final String origKeyName = pageItem.getKey().getUid();
			Integer pageIndex = 1;
			if (pagePageIndexMap.containsKey(origKeyName)) {
				pageIndex = pagePageIndexMap.get(origKeyName) + 1;
			}
			pagePageIndexMap.put(origKeyName, pageIndex);
			
			String keyName = origKeyName + "." + pageIndex;
			keyName = keyName.replaceAll("page", "");
			keyName = StringUtils.getInstance().trim(keyName, MDMService.SMALL);
			
			pageKeynameMap.put(origKeyName, keyName);
		}

		final Map<String, JsonObject> pagesBack = new LinkedHashMap<String, JsonObject>();
//		Map<String, Integer> pagePageIndexMap = new HashMap<String, Integer>();
		for (final Map.Entry<PageType, List<IdentificationalType>> pageItem : pageMap.entrySet()) {

			final List<IdentificationalType> pageQuestionsList = pageItem.getValue();

			// Map<String, Object> images = null;
			//
			// final String pageID = pageItem.getKey().getUid();
			// final String origKeyName = pageID;
			// String keyName = origKeyName;
			// if (screenshots.containsKey(origKeyName)) {
			// images = screenshots.get(origKeyName);
			// }
			// keyName = StringUtils.getInstance().trim(keyName, MDMService.SMALL);
			//

			final String pageId = pageItem.getKey().getUid();
			
			Map<String, Object> images = null;
			if (screenshots.containsKey(pageId)) {
				images = screenshots.get(pageId);
			}

//			String keyName = pageId + "." + pageIndex;
//			keyName = keyName.replaceAll("page", "");
//			keyName = StringUtils.getInstance().trim(keyName, MDMService.SMALL);
			
			final String keyName = pageKeynameMap.get(pageId);

			if (images != null) {
				System.out.println("screenshot found : " + keyName);

				final Map<String, Object> imageFolderForPage = new LinkedHashMap<String, Object>();
				int imageIndex = 0;
				for (final Map.Entry<String, Object> screenshotItem : images.entrySet()) {
					final String resolution = screenshotItem.getKey();
					final Map<String, File> resolutionImages = (Map<String, File>) screenshotItem.getValue();

					for (final Map.Entry<String, File> resolutionImageItem : resolutionImages.entrySet()) {
						final String language = resolutionImageItem.getKey();
						final File imageFile = resolutionImageItem.getValue();

						final String fileName = keyName + "_" + imageIndex;

						final Set<Object> imagePair = new LinkedHashSet<Object>();

						imagePair.add(imageFile);

						final String relativePath = "./" + fileName + "."
								+ FileClient.getInstance().getSuffix(imageFile);

						final Map<String, Object> questionImageMetadata = new LinkedHashMap<String, Object>();
						questionImageMetadata.put("imageType",
								FileClient.getInstance().getSuffix(imageFile).toUpperCase());
						questionImageMetadata.put("language", language);
						questionImageMetadata.put("file", relativePath);
						questionImageMetadata.put("resolution", resolution);
						questionImageMetadata.put("containsAnnotations", false);
						questionImageMetadata.put("indexInQuestion", imageIndex);
						questionImageMetadata.put("questionId", keyName);
						questionImageMetadata.put("dataAcquisitionProjectId", dataAcquestionProject);
						imagePair.add(JSON.createObject(questionImageMetadata));

						imageFolderForPage.put(fileName, imagePair);
						imageIndex = imageIndex + 1;
					}
				}
				pageImagesBack.put(keyName, imageFolderForPage);

			} else {
				System.out.println("no screenshot found for page: " + keyName);
			}

			// merge proccessed Questiondata for Page
			if ((pageQuestionsList != null) && (!pageQuestionsList.isEmpty())) {
				final Map<String, Object> pageData = new LinkedHashMap<String, Object>();
				final StringUtils strUtil = StringUtils.getInstance();

				final String trimmedQid = strUtil.trim(keyName, SMALL);
				final String pageIdStr = "que-" + dataAcquestionProject + "-ins" + instrumentId + "-" + trimmedQid
						+ "$";

				pageData.put("id", strUtil.trim(pageIdStr, MEDIUM));
				pageData.put("indexInInstrument", instrumentPageIndexMap.get(pageItem.getKey()));

				final Map<String, Object> technicalRepresentation = new LinkedHashMap<String, Object>();
				technicalRepresentation.put("type", "QML");
				technicalRepresentation.put("language", "XML");
				technicalRepresentation.put("source", XmlClient.getInstance().convert2String(pageItem.getKey()));
				pageData.put("technicalRepresentation", technicalRepresentation);

				final Map<String, Set<String>> additionals = getAdditional(pageItem.getKey(), translations);
				final Map<String, String> implodedAdditionals = new LinkedHashMap<String, String>();
				for (final Entry<String, Set<String>> item : additionals.entrySet()) {
					implodedAdditionals.put(item.getKey(),
							strUtil.trim(CollectionClient.getInstance().implode(item.getValue()), XLARGE));
				}

				pageData.put("additionalQuestionText", implodedAdditionals);

				for (final IdentificationalType questionObj : pageQuestionsList) {
					final Map<String, Object> processedData = processedQuestions.get(questionObj);

					// Merge to pageData
					if ((processedData != null) && (!processedData.isEmpty())) {
						for (final Map.Entry<String, Object> property : processedData.entrySet()) {
							final String propertyName = property.getKey();
							final Object propertyValue = property.getValue();

							if (propertyName.equals("id")) {
								continue;
							}
							if (propertyName.equals("indexInInstrument")) {
								continue;
							}
							if (propertyName.equals("technicalRepresentation")) {
								continue;
							}
							if (propertyName.equals("additionalQuestionText")) {
								continue;
							}

							final Class propertyClazz = propertyValue.getClass();
							if ((String.class).isAssignableFrom(propertyClazz)) {
								List<String> tmp = null;
								if (pageData.containsKey(propertyName)) {
									tmp = (List<String>) pageData.get(propertyName);
								}
								if (tmp == null) {
									tmp = new ArrayList<String>();
								}
								if (!tmp.contains(propertyValue)) {
									tmp.add((String) propertyValue);
								}
								pageData.put(propertyName, tmp);
							} else if ((Number.class).isAssignableFrom(propertyClazz)) {
								List<Number> tmp = null;
								if (pageData.containsKey(propertyName)) {
									tmp = (List<Number>) pageData.get(propertyName);
								}
								if (tmp == null) {
									tmp = new ArrayList<Number>();
								}
								if (!tmp.contains(propertyValue)) {
									tmp.add((Number) propertyValue);
								}
								pageData.put(propertyName, tmp);
							} else if ((Map.class).isAssignableFrom(propertyClazz)) {
								Map<String, List<Object>> tmp = null;
								if (pageData.containsKey(propertyName)) {
									tmp = (Map<String, List<Object>>) pageData.get(propertyName);
								}
								if (tmp == null) {
									tmp = new LinkedHashMap<String, List<Object>>();
								}

								final Map<String, Object> valueMap = (Map<String, Object>) propertyValue;
								for (final Map.Entry<String, Object> valueMapItem : valueMap.entrySet()) {
									List<Object> objects = null;
									if (tmp.containsKey(valueMapItem.getKey())) {
										objects = tmp.get(valueMapItem.getKey());
									}
									if (objects == null) {
										objects = new ArrayList<Object>();
									}

									objects.add(valueMapItem.getValue());

									tmp.put(valueMapItem.getKey(), objects);
								}
								pageData.put(propertyName, tmp);
							} else if ((List.class).isAssignableFrom(propertyClazz)) {
								List<Object> tmp = null;
								if (pageData.containsKey(propertyName)) {
									tmp = (List<Object>) pageData.get(propertyName);
								}
								if (tmp == null) {
									tmp = new ArrayList<Object>();
								}

								final List<Object> valueList = (List<Object>) propertyValue;
								for (final Object valueMapItem : valueList) {
									if (!tmp.contains(valueMapItem)) {
										tmp.add(valueMapItem);
									}
								}
								pageData.put(propertyName, tmp);
							} else {
								System.err.println("unhandeled format for " + propertyName + " : " + propertyClazz);
							}
						}
					}
				}

				// cleanup
				if ((pageData != null) && (!pageData.isEmpty())) {
					
					// escalade successors to page without loops
					if (pageData.containsKey("successorNumbers")) {
						
						final List<Object> correctedSuccessors = new ArrayList<Object>();
						final List<Object> successors = (List<Object>) pageData.get("successorNumbers");
						for (final Object successor : successors) {
							String correctedSuccessor = successor+"";

							//prevent loop to same page
							if(correctedSuccessor.equals(pageItem.getKey().getUid())) continue;
							
							if (pageKeynameMap.containsKey(correctedSuccessor))
								correctedSuccessor = pageKeynameMap.get(successor);

							correctedSuccessors.add(correctedSuccessor);
						}
						pageData.put("successorNumbers", correctedSuccessors);
					}

					final Set<String> toFilterStr = new HashSet<String>();
					toFilterStr.add("topic");
					toFilterStr.add("successorNumbers");

					final Set<String> toMergeStr = new HashSet<String>();
					// toMergeStr.add("additionalQuestionText");
					toMergeStr.add("questionText");
					toMergeStr.add("instruction");
					toMergeStr.add("introduction");
					toMergeStr.add("topic");

					for (final Map.Entry<String, Object> property : pageData.entrySet()) {
						final String propertyName = property.getKey();
						final Object propertyValue = property.getValue();
						final Class propertyClazz = propertyValue.getClass();

						// filter dublettes
						if (toFilterStr.contains(propertyName)) {
							if ((Map.class).isAssignableFrom(propertyClazz)) {
								final Map<String, Object> tmp = (Map<String, Object>) propertyValue;

								for (final Map.Entry<String, Object> tmpItem : tmp.entrySet()) {
									final String tmpKey = tmpItem.getKey();
									final Object tmpValue = tmpItem.getValue();

									if (tmpValue != null) {
										final List<Object> objects = (List<Object>) tmpValue;
										final List<String> filteredObjects = new ArrayList<String>();

										final Set<String> dubs = new HashSet<String>();

										for (final Object xxx : objects) {
											final String xxxx = xxx + "";
											if (dubs.contains(xxxx))
												continue;
											dubs.add(xxxx);
											filteredObjects.add(xxxx);

										}
										tmp.put(tmpKey, filteredObjects);
									}
								}
								pageData.put(propertyName, tmp);
							}
						}

						// merge Collections
						if (toMergeStr.contains(propertyName)) {
							if ((Map.class).isAssignableFrom(propertyClazz)) {
								final Map<String, Object> tmp = (Map<String, Object>) propertyValue;

								for (final Map.Entry<String, Object> tmpItem : tmp.entrySet()) {
									final String tmpKey = tmpItem.getKey();
									final Object tmpValue = tmpItem.getValue();

									if (tmpValue != null) {
										List<Object> objects = (List<Object>) tmpValue;
										final StringBuffer buffer = new StringBuffer();
										boolean first = true;
										for (final Object xxx : objects) {
											if (!first)
												buffer.append("\n");
											buffer.append(xxx + "");
											first = false;
										}
										tmp.put(tmpKey, buffer.toString());
									}
								}
								pageData.put(propertyName, tmp);
							}
						}

						if (propertyName.equals("type")) {
							Map<String, String> mixedType = new HashMap<String, String>();
							mixedType.put("de", "Itembatterie");
							mixedType.put("en", "Item Set");

							// final Map<String, String> typeMap = new LinkedHashMap<String, String>();
							// typeMap.put("de", questionTypeDE);
							// typeMap.put("en", questionTypeENG);
							// jsonQuestion.put("type", typeMap);

							if ((Map.class).isAssignableFrom(propertyClazz)) {
								final Map<String, Object> tmp = (Map<String, Object>) propertyValue;

								for (final Map.Entry<String, Object> tmpItem : tmp.entrySet()) {
									final String tmpKey = tmpItem.getKey();
									final Object tmpValue = tmpItem.getValue();

									if (tmpValue != null) {
										List<Object> objects = (List<Object>) tmpValue;
										String replacement = "";
										if (objects.size() == 1)
											replacement = objects.get(0) + "";
										else if (objects.size() > 1)
											replacement = mixedType.get(tmpKey);
										tmp.put(tmpKey, replacement);
									}
								}
								pageData.put(propertyName, tmp);
							}

						}
					}
				}
				// Generate JSONs
				final JsonObject page_json = JSON.createObject(pageData);
				// pagesBack.put(pageId, page_json);
				pagesBack.put(keyName, page_json);
			}

		}

		back.put("variable", variablesBack);
		back.put("question", questionsBack);
		back.put("images", questionImagesBack);
		back.put("pages", pagesBack);
		back.put("pageImages", pageImagesBack);

		return back;
	}

	// Convert XML-Document to QML-Document
	public QuestionnaireDocument docToQmlObject(final Document doc) throws Exception {
		final DOMSource domSource = new DOMSource(doc);
		final StringWriter writer = new StringWriter();
		final StreamResult result = new StreamResult(writer);
		final TransformerFactory tf = TransformerFactory.newInstance();
		final Transformer transformer = tf.newTransformer();
		transformer.transform(domSource, result);
		final String content = writer.toString();
		// System.out.println(content);
		final QuestionnaireDocument back = QuestionnaireDocument.Factory.parse(content);
		return back;
	}

	// Generate Component-Map from XmlObject by Reflection
	private Map<String, Object> getObjAsTable(final Object node) {
		if (node == null) {
			return null;
		}

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
							if (returnType.getName().contains("zofar")) {
								structuredData.put(name, getObjAsTable(value));
							} else {
								structuredData.put(name, value);
							}
						}
					}
				}
			}
		} else if ((Object[].class).isAssignableFrom(node.getClass())) {
			final Object[] tmp = (Object[]) node;
			final int arrayCount = tmp.length;
			final Object[] rebuildTmp = new Object[arrayCount];
			if (arrayCount > 0) {
				for (int a = 0; a < arrayCount; a++) {
					rebuildTmp[a] = getObjAsTable(tmp[a]);
				}
				structuredData.put("ARRAY", rebuildTmp);
			} else {
				structuredData.put("ARRAY", "NULL");
			}
		} else {
			structuredData.put("VALUE", node);
		}

		return structuredData;
	}

	final static int SMALL = 32 - 2;
	final static int MEDIUM = 512;
	final static int LARGE = 2048;
	final static int XLARGE = 1024000;

	// Compose Data for JSON-Objects and CSV
	private Map<String, Object> getDataTable(final XmlObject doc, final IdentificationalType node,
			final Map<String, Properties> translations, final int indexInInstrument,
			final Map<IdentificationalType, PageType> idMap1, final String dataAcquestionProject, final String surveyId,
			final String instrumentId, final String datasetId, final Map<String, Set<String>> topic,
			final String keyName) {
		if (node == null) {
			return null;
		}
		final Map<String, Object> structuredData = getObjAsTable(node);

		if (structuredData == null) {
			return null;
		}
		if (structuredData.isEmpty()) {
			return null;
		}

		// Question JSON

		final StringUtils strUtil = StringUtils.getInstance();

		final Map<String, Object> jsonQuestion = new LinkedHashMap<String, Object>();

		// final String qid = idMap.get(node);
		final String qid = QMLClient.getInstance().getIdPath(node);
		// final String idStr = "que-" + dataAcquestionProject + "-ins" + instrumentId +
		// "-" + strUtil.trim(qid, SMALL)
		// + "$".toLowerCase();

		// final String trimmedQid = strUtil.trim(qid, SMALL);

		final String trimmedQid = strUtil.trim(keyName, SMALL);

		final String idStr = "que-" + dataAcquestionProject + "-ins" + instrumentId + "-" + trimmedQid + "$";

		jsonQuestion.put("id", strUtil.trim(idStr, MEDIUM));
		jsonQuestion.put("number", trimmedQid);

		Map<String, Set<String>> questionTexts = null;
		Map<String, Set<String>> instructionTexts = null;
		Map<String, Set<String>> introductionTexts = null;

		String type = "UNKOWN";
		if (structuredData.containsKey("xmlObj")) {
			final XmlObject tmp = (XmlObject) structuredData.get("xmlObj");

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
					final XmlObject tmpParent = XmlClient.getInstance().getParent(parent);
					if (tmpParent == null) {
						break;
					}
					if (tmpParent.getDomNode().getNodeName().equals("zofar:section")) {
						break;
					}
					if (tmpParent.getDomNode().getNodeName().equals("zofar:page")) {
						break;
					}
					if (tmpParent.getDomNode().getNodeName().equals("zofar:body")) {
						break;
					}
					parent = tmpParent;
				}
			}

			if (parent != null) {
				technicalRepresentation.put("source", XmlClient.getInstance().convert2String(parent));
			} else {
				technicalRepresentation.put("source", XmlClient.getInstance().convert2String(tmp));
			}
			jsonQuestion.put("technicalRepresentation", technicalRepresentation);

			type = tmp.getDomNode().getNodeName();

			// final List<XmlObject> nextList = this.getNext(tmp, doc, translations);
			// if ((nextList != null) && (!nextList.isEmpty())) {
			// final List<Object> successors = new ArrayList<Object>();
			// for (final XmlObject next : nextList) {
			// if ((IdentificationalType.class).isAssignableFrom(next.getClass())) {
			// final IdentificationalType xx = (IdentificationalType) next;
			// final String id = QMLClient.getInstance().getIdPath(xx);
			// successors.add(id);
			// }
			// }
			// jsonQuestion.put("successorNumbers", successors);
			// }

			final List<XmlObject> nextList = this.getNext(tmp, doc, translations);
			if ((nextList != null) && (!nextList.isEmpty())) {
				jsonQuestion.put("nextList", nextList);
			}

			final Map<String, Set<String>> additionals = getAdditional(tmp, translations);
			final Map<String, String> implodedAdditionals = new LinkedHashMap<String, String>();
			for (final Entry<String, Set<String>> item : additionals.entrySet()) {
				implodedAdditionals.put(item.getKey(),
						strUtil.trim(CollectionClient.getInstance().implode(item.getValue()), XLARGE));
			}

			jsonQuestion.put("additionalQuestionText", implodedAdditionals);
		}

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
			questionTypeDE = "Matrix";
			questionTypeENG = "Grid";
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
		} else {
			System.out.println("Unhandled Type : " + type);
		}

		final Map<String, String> typeMap = new LinkedHashMap<String, String>();
		typeMap.put("de", questionTypeDE);
		typeMap.put("en", questionTypeENG);
		jsonQuestion.put("type", typeMap);

		final Map<String, Set<String>> emptyQuestionStr = new HashMap<String, Set<String>>();
		final Set<String> deQuestionSet = new HashSet<String>();
		deQuestionSet.add("Kein Fragetext vorhanden");
		emptyQuestionStr.put("de", deQuestionSet);

		final Set<String> enQuestionSet = new HashSet<String>();
		enQuestionSet.add("No questiontext available");
		emptyQuestionStr.put("en", enQuestionSet);

		final Map<String, String> implodedQuestionTexts = new LinkedHashMap<String, String>();
		for (final Entry<String, Set<String>> item : questionTexts.entrySet()) {
			String tmp = CollectionClient.getInstance().implode(emptyQuestionStr.get(item.getKey()));
			if (!item.getValue().isEmpty()) {
				tmp = strUtil.trim(CollectionClient.getInstance().implode(item.getValue()), LARGE);
			}
			if (tmp == null) {
				continue;
			}
			tmp = tmp.trim();
			if (tmp.equals("")) {
				continue;
			}
			implodedQuestionTexts.put(item.getKey(), tmp);
		}

		if (implodedQuestionTexts.isEmpty()) {
			for (final Entry<String, Set<String>> item : emptyQuestionStr.entrySet()) {
				implodedQuestionTexts.put(item.getKey(),
						strUtil.trim(CollectionClient.getInstance().implode(item.getValue()), LARGE));
			}
		}
		jsonQuestion.put("questionText", implodedQuestionTexts);

		final Map<String, Set<String>> emptyInstructionStr = new HashMap<String, Set<String>>();
		final Set<String> deInstructionSet = new HashSet<String>();
		deInstructionSet.add("Keine Anweisung vorhanden");
		emptyInstructionStr.put("de", deInstructionSet);

		final Set<String> enInstructionSet = new HashSet<String>();
		enInstructionSet.add("No instruction available");
		emptyInstructionStr.put("en", enInstructionSet);

		final Map<String, String> implodedInstructionTexts = new LinkedHashMap<String, String>();
		for (final Entry<String, Set<String>> item : instructionTexts.entrySet()) {
			String tmp = CollectionClient.getInstance().implode(emptyInstructionStr.get(item.getKey()));
			if (!item.getValue().isEmpty()) {
				tmp = strUtil.trim(CollectionClient.getInstance().implode(item.getValue()), LARGE);
			}
			if (tmp == null) {
				continue;
			}
			tmp = tmp.trim();
			if (tmp.equals("")) {
				continue;
			}
			implodedInstructionTexts.put(item.getKey(), tmp);
		}

		// if (instructionTexts.isEmpty()) {
		// for (final Entry<String, Set<String>> item : emptyInstructionStr.entrySet())
		// {
		// implodedInstructionTexts.put(item.getKey(),
		// strUtil.trim(CollectionClient.getInstance().implode(item.getValue()),
		// LARGE));
		// }
		// }
		if (!instructionTexts.isEmpty())
			jsonQuestion.put("instruction", implodedInstructionTexts);

		final Map<String, Set<String>> emptyIntroductionStr = new HashMap<String, Set<String>>();
		final Set<String> deIntroductionSet = new HashSet<String>();
		deIntroductionSet.add("Keine Anleitung vorhanden");
		emptyIntroductionStr.put("de", deIntroductionSet);

		final Set<String> enIntroductionSet = new HashSet<String>();
		enIntroductionSet.add("No introduction available");
		emptyIntroductionStr.put("en", enIntroductionSet);

		final Map<String, String> implodedIntroductionTexts = new LinkedHashMap<String, String>();

		for (final Entry<String, Set<String>> item : introductionTexts.entrySet()) {
			String tmp = CollectionClient.getInstance().implode(emptyIntroductionStr.get(item.getKey()));
			if (!item.getValue().isEmpty()) {
				tmp = strUtil.trim(CollectionClient.getInstance().implode(item.getValue()), LARGE);
			}
			if (tmp == null) {
				continue;
			}
			tmp = tmp.trim();
			if (tmp.equals("")) {
				continue;
			}
			implodedIntroductionTexts.put(item.getKey(), tmp);
		}

		// if (introductionTexts.isEmpty()) {
		// for (final Entry<String, Set<String>> item : emptyIntroductionStr.entrySet())
		// {
		// implodedIntroductionTexts.put(item.getKey(),
		// strUtil.trim(CollectionClient.getInstance().implode(item.getValue()),
		// LARGE));
		// }
		// }
		if (!introductionTexts.isEmpty())
			jsonQuestion.put("introduction", implodedIntroductionTexts);

		jsonQuestion.put("instrumentNumber", instrumentId);
		jsonQuestion.put("dataAcquisitionProjectId", dataAcquestionProject);
		jsonQuestion.put("studyId", surveyId);

		// final Map<String, String> topicMap = new LinkedHashMap<String, String>();
		// // topicMap.put("de", strUtil.trim("", LARGE));
		// // topicMap.put("en", strUtil.trim("", LARGE));
		// jsonQuestion.put("topic", topicMap);

		final Map<String, Set<String>> emptyTopicStr = new HashMap<String, Set<String>>();
		final Set<String> deTopicSet = new HashSet<String>();
		deTopicSet.add("Kein Thema vorhanden");
		emptyTopicStr.put("de", deTopicSet);

		final Set<String> enTopicSet = new HashSet<String>();
		enTopicSet.add("No topic available");
		emptyTopicStr.put("en", enTopicSet);

		final Map<String, String> implodedTopicTexts = new LinkedHashMap<String, String>();

		for (final Entry<String, Set<String>> item : topic.entrySet()) {
			String tmp = CollectionClient.getInstance().implode(emptyTopicStr.get(item.getKey()));
			if (!item.getValue().isEmpty()) {
				tmp = strUtil.trim(CollectionClient.getInstance().implode(item.getValue()), LARGE);
			}
			if (tmp == null) {
				continue;
			}
			tmp = tmp.trim();
			if (tmp.equals("")) {
				continue;
			}
			implodedTopicTexts.put(item.getKey(), tmp);
		}

		if (implodedTopicTexts.isEmpty()) {
			for (final Entry<String, Set<String>> item : emptyTopicStr.entrySet()) {
				implodedTopicTexts.put(item.getKey(),
						strUtil.trim(CollectionClient.getInstance().implode(item.getValue()), LARGE));
			}
		}
		jsonQuestion.put("topic", implodedTopicTexts);

		final Map<String, String> annotationsMap = new LinkedHashMap<String, String>();
		// annotationsMap.put("de", strUtil.trim("", LARGE));
		// annotationsMap.put("en", strUtil.trim("", LARGE));
		jsonQuestion.put("annotations", annotationsMap);

		jsonQuestion.put("indexInInstrument", indexInInstrument);

		final Map<String, Object> back = new HashMap<String, Object>();

		back.put("question", jsonQuestion);

		final List<XmlObject> variables = getVariablesFromObj(node);
		if (variables != null) {
			final List<Map<String, Object>> csvVariables = new ArrayList<Map<String, Object>>();
			for (final XmlObject variable : variables) {
				final Map<String, Object> csvVariable = new LinkedHashMap<String, Object>();

				csvVariable.put("variablen_id", variable.newCursor().getTextValue());

				csvVariable.put("question_number", qid);
				csvVariable.put("instrumentId", instrumentId);

				final Map<String, Set<String>> relatedStrings = getVariableRelated(variable, translations);
				if (relatedStrings != null) {
					if (relatedStrings.containsKey("de")) {
						csvVariable.put("relatedQuestionStrings.de",
								CollectionClient.getInstance().implode(relatedStrings.get("de")));
					}
					if (relatedStrings.containsKey("en")) {
						csvVariable.put("relatedQuestionStrings.en",
								CollectionClient.getInstance().implode(relatedStrings.get("en")));
					}
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
		final Map<String, Set<String>> back = new LinkedHashMap<String, Set<String>>();

		if (question == null) {
			return back;
		}

		final XmlClient xmlClient = XmlClient.getInstance();

		final XmlObject[] childs = xmlClient.getByXPath(question, "descendant::*");
		if (childs != null) {
			for (final XmlObject child : childs) {
				final Class<? extends XmlObject> clazz = child.getClass();
				if ((de.his.zofar.xml.questionnaire.TextType.class).isAssignableFrom(clazz)) {
					if (translations == null) {
						// Original
						final Set<String> langSet = new LinkedHashSet<String>();
						final String tmp = ReplaceClient.getInstance().cleanedString(child.newCursor().getTextValue());
						if ((tmp != null) && (!tmp.equals(""))) {
							langSet.add(tmp);
						}
						back.put("de", langSet);
					} else {
						Map<Node, String> translationUids = null;

						if ((IdentificationalType.class).isAssignableFrom(child.getClass())) {
							try {
								translationUids = getTranslationUids((IdentificationalType) child);
							} catch (final XmlException e) {
								e.printStackTrace();
							}
						}

						if (translationUids != null) {
							for (final Map.Entry<Node, String> translationUid : translationUids.entrySet()) {
								final String uidPath = translationUid.getValue();
								if ((translations != null) && (!translations.isEmpty())) {
									for (final Map.Entry<String, Properties> translation : translations.entrySet()) {
										if (translation.getValue().containsKey(uidPath)) {
											final String language = translation.getKey();
											final String translatedText = ReplaceClient.getInstance()
													.cleanedString(translation.getValue().getProperty(uidPath));

											Set<String> langSet = null;
											if (back.containsKey(language)) {
												langSet = back.get(language);
											}
											if (langSet == null) {
												langSet = new LinkedHashSet<String>();
											}
											final String tmp = translatedText;
											if ((tmp != null) && (!tmp.equals(""))) {
												langSet.add(tmp);
											}
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
						final Set<String> langSet = new LinkedHashSet<String>();
						final String tmp = ReplaceClient.getInstance().cleanedString(ao.getLabel2());
						if ((tmp != null) && (!tmp.equals(""))) {
							langSet.add(tmp);
						}
						back.put("de", langSet);
					} else {
						Map<Node, String> translationUids = null;

						if ((IdentificationalType.class).isAssignableFrom(child.getClass())) {
							try {
								translationUids = getTranslationUids((IdentificationalType) child);
							} catch (final XmlException e) {
								e.printStackTrace();
							}
						}

						if (translationUids != null) {
							for (final Map.Entry<Node, String> translationUid : translationUids.entrySet()) {
								final String uidPath = translationUid.getValue();
								if ((translations != null) && (!translations.isEmpty())) {
									for (final Map.Entry<String, Properties> translation : translations.entrySet()) {
										if (translation.getValue().containsKey(uidPath)) {
											final String language = translation.getKey();
											final String translatedText = ReplaceClient.getInstance()
													.cleanedString(translation.getValue().getProperty(uidPath));

											Set<String> langSet = null;
											if (back.containsKey(language)) {
												langSet = back.get(language);
											}
											if (langSet == null) {
												langSet = new LinkedHashSet<String>();
											}
											final String tmp = translatedText;
											if ((tmp != null) && (!tmp.equals(""))) {
												langSet.add(tmp);
											}
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
						final Set<String> langSet = new LinkedHashSet<String>();
						final String tmp = ReplaceClient.getInstance().cleanedString(calConfItem.getLabel());
						if ((tmp != null) && (!tmp.equals(""))) {
							langSet.add(tmp);
						}
						if (!back.containsKey("de")) {
							back.put("de", langSet);
						} else {
							back.get("de").addAll(langSet);
						}
					} else {
						final Set<String> langSet = new LinkedHashSet<String>();
						final String tmp = ReplaceClient.getInstance().cleanedString(calConfItem.getLabel());
						if ((tmp != null) && (!tmp.equals(""))) {
							langSet.add(tmp);
						}
						if (!back.containsKey("de")) {
							back.put("de", langSet);
						} else {
							back.get("de").addAll(langSet);
						}
					}
				} else if ((de.his.zofar.xml.questionnaire.CalendarType.class).isAssignableFrom(clazz)) {
					System.out.println("Additional CalendarType");
					final de.his.zofar.xml.questionnaire.CalendarType cal = (de.his.zofar.xml.questionnaire.CalendarType) child;
					if (translations == null) {
						// Original
						final Set<String> langSet = new LinkedHashSet<String>();
						final String columns = ReplaceClient.getInstance().cleanedString(cal.getColumns());
						if ((columns != null) && (!columns.equals(""))) {
							langSet.add(columns);
						}
						final String rows = ReplaceClient.getInstance().cleanedString(cal.getRows());
						if ((rows != null) && (!rows.equals(""))) {
							langSet.add(rows);
						}
						back.put("de", langSet);
					} else {
						final Set<String> langSet = new LinkedHashSet<String>();
						final String columns = ReplaceClient.getInstance().cleanedString(cal.getColumns());
						if ((columns != null) && (!columns.equals(""))) {
							langSet.add(columns);
						}
						final String rows = ReplaceClient.getInstance().cleanedString(cal.getRows());
						if ((rows != null) && (!rows.equals(""))) {
							langSet.add(rows);
						}
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
		final Map<String, Set<String>> back = new LinkedHashMap<String, Set<String>>();
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
				for (final Map.Entry<String, Set<String>> entry : parentMap.entrySet()) {
					Set<String> langSet = null;
					if (back.containsKey(entry.getKey())) {
						langSet = back.get(entry.getKey());
					}
					if (langSet == null) {
						langSet = new LinkedHashSet<String>();
					}
					langSet.addAll(entry.getValue());
					back.put(entry.getKey(), langSet);
				}
			}
		}
		if ((IdentificationalType.class).isAssignableFrom(xml.getClass())) {
			boolean recursive = false;
			if (xml.getDomNode().getNodeName().equals("zofar:questionOpen")) {
				recursive = true;
			}
			final Map<String, Set<String>> headerMap = getHeaderHelper(xml, fields, translations, recursive);
			if (headerMap != null) {
				for (final Map.Entry<String, Set<String>> entry : headerMap.entrySet()) {
					Set<String> langSet = null;
					if (back.containsKey(entry.getKey())) {
						langSet = back.get(entry.getKey());
					}
					if (langSet == null) {
						langSet = new LinkedHashSet<String>();
					}
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
		final Set<String> fields = new HashSet<String>();
		fields.add("question");
		final Map<String, Set<String>> back = this.retrieveHeaderField(question, fields, translations, false);
		return back;
	}

	// Get all Instruction Elements within a Header-Element without recursive
	// Child-Scan but with parental Escalation
	private Map<String, Set<String>> getInstructionHeader(final XmlObject question,
			final Map<String, Properties> translations) {
		final Set<String> fields = new HashSet<String>();
		fields.add("instruction");
		final Map<String, Set<String>> back = this.retrieveHeaderField(question, fields, translations, true);
		return back;
	}

	// Get all Introduction Elements within a Header-Element without recursive
	// Child-Scan but with parental Escalation
	private Map<String, Set<String>> getIntroductionHeader(final XmlObject question,
			final Map<String, Properties> translations) {
		final Set<String> fields = new HashSet<String>();
		fields.add("introduction");
		final Map<String, Set<String>> back = this.retrieveHeaderField(question, fields, translations, true);
		return back;
	}

	private Map<String, Set<String>> getTopic(final XmlObject question, final Map<String, Properties> translations) {
		final Set<String> fields = new HashSet<String>();
		fields.add("title");

		// Escalate to page

		XmlObject tmp = question;
		if (tmp != null) {
			String type = tmp.getDomNode().getNodeName();
			while (!type.equals("zofar:page")) {
				tmp = XmlClient.getInstance().getParent(tmp);
				type = tmp.getDomNode().getNodeName();
			}
			if (type.equals("zofar:page")) {
				return this.retrieveHeaderField(tmp, fields, translations, false);

			}
		}
		return null;
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
		if (node == null) {
			return back;
		}
		final XmlClient client = XmlClient.getInstance();

		final Map<String, Set<String>> parentMap = getHeaderHelper(client.getParent(node), fields, translations,
				recursive);

		// clean from dublettes
		for (final Map.Entry<String, Set<String>> item : parentMap.entrySet()) {
			final Set<String> existingSet = back.get(item.getKey());
			if (existingSet == null) {
				continue;
			}
			if (existingSet.isEmpty()) {
				continue;
			}
			final Set<String> currentSet = item.getValue();
			if (currentSet == null) {
				continue;
			}
			if (currentSet.isEmpty()) {
				continue;
			}
			final Iterator<String> it = currentSet.iterator();
			while (it.hasNext()) {
				if (existingSet.contains(it.next())) {
					it.remove();
				}
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
		if (recursive) {
			pattern = "descendant::*";
		}
		final XmlObject[] childs = XmlClient.getInstance().getByXPath(node, pattern);
		if (childs != null) {
			for (final XmlObject child : childs) {
				final boolean notinterrupt = ((child.getDomNode().getNodeName().equals("zofar:header"))
						|| (headerFields.contains(child.getDomNode().getNodeName().replace("zofar:", ""))));
				// if (!child.getDomNode().getNodeName().equals("zofar:header"))
				// continue;

				if (!notinterrupt) {
					continue;
				}

				final XmlObject[] headerItems = child.selectPath("descendant::*");
				for (final XmlObject headerItem : headerItems) {
					final String headerItemType = headerItem.getDomNode().getNodeName().replace("zofar:", "");
					if (!headerFields.contains(headerItemType)) {
						continue;
					}

					if (translations == null) {
						// Original
						final Set<String> langSet = new LinkedHashSet<String>();
						final String tmp = ReplaceClient.getInstance()
								.cleanedString(headerItem.newCursor().getTextValue());
						if ((tmp != null) && (!tmp.equals(""))) {
							langSet.add(tmp);
						}
						back.put("de", langSet);
					} else {
						Map<Node, String> translationUids = null;

						if ((IdentificationalType.class).isAssignableFrom(headerItem.getClass())) {
							try {
								translationUids = getTranslationUids((IdentificationalType) headerItem);
							} catch (final XmlException e) {
								e.printStackTrace();
							}
						}

						if (translationUids != null) {
							for (final Map.Entry<Node, String> translationUid : translationUids.entrySet()) {
								final String uidPath = translationUid.getValue();
								if ((translations != null) && (!translations.isEmpty())) {
									for (final Map.Entry<String, Properties> translation : translations.entrySet()) {
										if (translation.getValue().containsKey(uidPath)) {
											final String language = translation.getKey();
											final String translatedText = ReplaceClient.getInstance()
													.cleanedString(translation.getValue().getProperty(uidPath));

											Set<String> langSet = null;
											if (back.containsKey(language)) {
												langSet = back.get(language);
											}
											if (langSet == null) {
												langSet = new LinkedHashSet<String>();
											}
											final String tmp = translatedText;
											if ((tmp != null) && (!tmp.equals(""))) {
												langSet.add(tmp);
											}
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

			for (final XmlObject attribute : attributes) {
				final String attributeType = attribute.getDomNode().getNodeName();
				if (!headerFields.contains(attributeType)) {
					continue;
				}

				if (translations == null) {
					// Original
					final Set<String> langSet = new LinkedHashSet<String>();
					final String tmp = ReplaceClient.getInstance().cleanedString(attribute.newCursor().getTextValue());
					if ((tmp != null) && (!tmp.equals(""))) {
						langSet.add(tmp);
					}
					back.put("de", langSet);
				} else {
					Map<Node, String> translationUids = null;

					if ((IdentificationalType.class).isAssignableFrom(node.getClass())) {
						try {
							translationUids = getTranslationUids((IdentificationalType) node);
						} catch (final XmlException e) {
							e.printStackTrace();
						}
					}

					if (translationUids != null) {
						for (final Map.Entry<Node, String> translationUid : translationUids.entrySet()) {
							for (final Map.Entry<String, Properties> translation : translations.entrySet()) {
								if (translation.getValue().containsKey(translationUid.getValue())) {
									final String language = translation.getKey();
									final String translatedText = ReplaceClient.getInstance().cleanedString(
											translation.getValue().getProperty(translationUid.getValue()));
									Set<String> langSet = null;
									if (back.containsKey(language)) {
										langSet = back.get(language);
									}
									if (langSet == null) {
										langSet = new LinkedHashSet<String>();
									}
									final String tmp = translatedText;
									if ((tmp != null) && (!tmp.equals(""))) {
										langSet.add(tmp);
									}
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

	private Map<Node, String> getTranslationUids(final IdentificationalType source) throws XmlException {
		return getTranslationUids(0, source.getDomNode(), generateTranslationUidHelper(source));
	}

	private Map<Node, String> getTranslationUids(final int lft, final Node node, final String sourcePath)
			throws XmlException {
		if (node == null) {
			return null;
		}
		final String nodeName = node.getNodeName();
		final Map<Node, String> back = new HashMap<Node, String>();
		if (nodeName.equals("#text")) {
			final String uidPath = sourcePath + "_" + lft;
			back.put(node, uidPath);
		} else {
			final NodeList childs = node.getChildNodes();
			final int count = childs.getLength();
			for (int a = 0; a < count; a++) {
				final Node child = childs.item(a);
				back.putAll(getTranslationUids(lft + a, child, sourcePath));
			}

			final NamedNodeMap attributeChilds = node.getAttributes();
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
			if ((parentNode.getNodeType() == Node.ELEMENT_NODE) && parentNode.hasAttributes()) {
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

	private List<XmlObject> getNext(XmlObject current, final XmlObject xmlDoc,
			final Map<String, Properties> translations) {
		final List<XmlObject> back = new ArrayList<XmlObject>();
		if (current == null) {
			return back;
		}
		String currenttype = current.getDomNode().getNodeName();
		final XmlClient client = XmlClient.getInstance();

		if (currenttype.equals("zofar:transitions")) {
			current = client.getParent(current);
			currenttype = current.getDomNode().getNodeName();
		}

		if (currenttype.equals("zofar:page")) {
			// page switch
			final List<XmlObject> trans = getTransitionsFrom(current, xmlDoc);
			if (trans != null) {
				for (final XmlObject nextPage : trans) {
					final XmlObject qObj = getQuestionFromObj(nextPage, translations);
					if (qObj != null) {
						back.add(qObj);
					}
				}
			}
		} else {
			// final XmlClient client = XmlClient.getInstance();
			XmlObject sibling = client.getNextSibling(current);

			while (sibling != null) {
				final XmlObject siblingQuestion = getQuestionFromObj(sibling, translations);
				if (siblingQuestion != null) {
					back.add(siblingQuestion);
					break;
				} else {
					sibling = client.getNextSibling(sibling);
				}
			}

			if (sibling == null) {
				// back.addAll(getNext(client.getNextSibling(client.getParent(current)),
				// xmlDoc));
				XmlObject parent = client.getParent(current);
				if (parent != null) {
					XmlObject parentSibling = null;

					while (parentSibling == null) {
						if (parent == null) {
							// System.out.println("parent == null");
							break;
						}
						parent = client.getParent(parent);
						parentSibling = client.getNextSibling(parent);
					}
					
//					if (parentSibling != null) {
//						back.addAll(getNext(parentSibling, xmlDoc, translations));
//					}
					
					if ((parent != null)&&(parent.getDomNode().getNodeName().equals("zofar:page"))) {
						back.addAll(getNext(parent, xmlDoc, translations));
					}
					else if (parentSibling != null) {
						back.addAll(getNext(parentSibling, xmlDoc, translations));
					}
				}
			}
		}
		return back;
	}

	// GET Path of UIDs escalated to Top Element of Type IdentificationalType
	private String getUIDPath(final XmlObject child) {
		if (child == null) {
			return null;
		}
		XmlObject parent = child;
		String back = "";
		while (parent != null) {
			if ((IdentificationalType.class).isAssignableFrom(parent.getClass())) {
				if (!back.equals("")) {
					back = ":" + back;
				}
				back = ((IdentificationalType) parent).getUid() + back;
			}

			parent = XmlClient.getInstance().getParent(parent);
		}
		return back;
	}

	public boolean hasParentDEBUG(final Node node, final String parentName) {
		if (node == null) {
			return false;
		}
		String nodename = node.getNodeName();
		if (!nodename.startsWith("zofar:")) {
			nodename = "zofar:" + nodename;
		}

		if (nodename.equals(parentName)) {
			return true;
		}
		return hasParentDEBUG(node.getParentNode(), parentName);
	}

	private XmlObject getParentQuestion(final XmlObject child) {
		if (child == null) {
			return null;
		}
		final XmlClient client = XmlClient.getInstance();

		final List<XmlObject> questionPath = new ArrayList<XmlObject>();
		questionPath.add(child);
		XmlObject parent = client.getParent(child);
		while (parent != null) {
			if (!hasParentDEBUG(parent.getDomNode(), "zofar:page")) {
				break;
			}
			questionPath.add(parent);
			parent = client.getParent(parent);
		}
		Collections.reverse(questionPath);
		for (final XmlObject item : questionPath) {
			if ((AbstractQuestionType.class).isAssignableFrom(item.getClass())) {
				return item;
			}
			if ((AbstractMatrixType.class).isAssignableFrom(item.getClass())) {
				return item;
			}
			if ((CalendarType.class).isAssignableFrom(item.getClass())) {
				return item;
			}
		}

		return child;
	}

	private XmlObject getQuestionFromObj(final XmlObject obj, final Map<String, Properties> translations) {
		final List<XmlObject> tmp = getQuestionsFromObj(obj, true);
		if ((tmp != null) && (!tmp.isEmpty())) {
			XmlObject question = tmp.get(0);
			if ((question != null)) {
				Map<String, Set<String>> qheader = getQuestionHeader(question, translations);
				if ((qheader != null) && (!qheader.isEmpty()))
					return tmp.get(0);
			}

		}
		return null;
	}


	private List<XmlObject> getQuestionsFromObj(XmlObject obj, boolean successorRetrieval) {
		if (obj == null) {
			return null;
		}

		// System.out.println("Type : " + obj.getDomNode().getNodeName() + "
		// successorRetrieval : " + successorRetrieval);

		final XmlClient client = XmlClient.getInstance();
		String query = "descendant::*/@variable";
		if (successorRetrieval && (obj.getDomNode().getNodeName().equals("zofar:page"))) {
			query = "zofar:body/" + query;
		}

		// final XmlObject[] resultSet = client.getByXPath(obj,
		// "/zofar:body/descendant::*/@variable");
		final XmlObject[] resultSet = client.getByXPath(obj, query);
		if ((resultSet != null) && (resultSet.length > 0)) {
			final List<XmlObject> back = new ArrayList<XmlObject>();
			for (final XmlObject item : resultSet) {
				XmlObject parent = getParentQuestion(item);
				if (parent != null) {
					if (parent.getDomNode().getNodeName().equals("zofar:question")) {
						parent = getParentQuestion(parent);
					}
					if (parent.getDomNode().getNodeName().equals("question")) {
						parent = getParentQuestion(parent);
					}
					if ((!back.contains(parent))) {
						back.add(parent);
					}
				}
			}
			return back;
		}
		return null;
	}


	private List<XmlObject> getVariablesFromObj(final XmlObject obj) {
		if (obj == null) {
			return null;
		}
		final XmlClient client = XmlClient.getInstance();
		final XmlObject[] resultSet = client.getByXPath(obj, "descendant::*/@variable");
		if ((resultSet != null) && (resultSet.length > 0)) {
			final List<XmlObject> back = new ArrayList<XmlObject>();
			for (final XmlObject item : resultSet) {
				back.add(item);
			}
			return back;
		}
		return null;
	}

	private List<XmlObject> getTransitionsFrom(final XmlObject obj, final XmlObject xmlDoc) {
		if (obj == null) {
			return null;
		}
		if (!obj.getDomNode().getNodeName().equals("zofar:page")) {
			return null;
		}

		final XmlClient client = XmlClient.getInstance();
		final XmlObject[] resultSet = client.getByXPath(obj, "./zofar:transitions/*/@target");
		final List<XmlObject> back = new ArrayList<XmlObject>();
		if ((resultSet != null) && (resultSet.length > 0)) {
			for (final XmlObject found : resultSet) {
				final String targetUID = found.newCursor().getTextValue();
				final XmlObject[] pages = client.getByXPath(xmlDoc, "//*[@uid='" + targetUID + "']");
				if (pages != null) {
					for (final XmlObject page : pages) {
						if (page.getDomNode().getNodeName().equals("zofar:page")) {
							back.add(page);
						}
					}
				}
			}
		}
		return back;
	}
}
