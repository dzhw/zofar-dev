package eu.dzhw.zofar.testing.condition;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import eu.dzhw.zofar.testing.condition.components.FunctionProvider;
import eu.dzhw.zofar.testing.condition.qml.QmlClient;
import eu.dzhw.zofar.testing.condition.term.TermAnalyzerClient;
import eu.dzhw.zofar.testing.condition.term.elements.Element;
import eu.dzhw.zofar.testing.condition.term.elements.ElementVector;

public class ConditionAnalyzerClient implements Serializable {

	private static final long serialVersionUID = -3671488413331767377L;
	private final static ConditionAnalyzerClient INSTANCE = new ConditionAnalyzerClient();

	private ConditionAnalyzerClient() {
		super();
	}

	public static ConditionAnalyzerClient getInstance() {
		return INSTANCE;
	}
	
	public void checkAccessibility(final File qmlFile)throws Exception{
		if(qmlFile == null)throw new Exception("File is null");
		if(!qmlFile.canRead())throw new Exception("cannot read File "+qmlFile.getAbsolutePath());

		final QmlClient qmlClient = QmlClient.getInstance();
		
		Map<String, ElementVector> pageInitials = qmlClient.retrievePageInitials(qmlFile);
		Map<String, Set<Map<String, String>>> pageTree = qmlClient.getCompletePageTree(qmlFile);
		
		Map<String, Map<String, Map<String,ElementVector>>> contextTree = analyseQML(pageTree, pageInitials,true);
	}
	
	public Map<String,String> vectorMap(final File qmlFile)throws Exception{
		if(qmlFile == null)throw new Exception("File is null");
		if(!qmlFile.canRead())throw new Exception("cannot read File "+qmlFile.getAbsolutePath());

		final QmlClient qmlClient = QmlClient.getInstance();
		
		Map<String, ElementVector> pageInitials = qmlClient.retrievePageInitials(qmlFile);
		Map<String, Set<Map<String, String>>> pageTree = qmlClient.getCompletePageTree(qmlFile);
		
		Map<String, Map<String, Map<String,ElementVector>>> contextTree = analyseQML(pageTree, pageInitials,false);
		
		if(contextTree == null)throw new Exception("Analyse failed");
		if(contextTree.isEmpty())throw new Exception("Analyse result is empty");
		
		Map<String,String> back = new HashMap<String,String>();
		
		 // Show Context Tree
		final StringBuffer treeBuffer = new StringBuffer();
		for (final String contextPage : contextTree.keySet()) {
			final Map<String, Map<String,ElementVector>> contextMap = contextTree.get(contextPage);
			treeBuffer.append("Source : " + contextPage+"\n");
			for (final Map.Entry<String, Map<String,ElementVector>> target : contextMap.entrySet()) {
				final String targetPage = target.getKey();
				final Map<String,ElementVector> vector = target.getValue();
				treeBuffer.append("\tTarget : " + targetPage+"\n");
				for (final Map.Entry<String,ElementVector> vectorItem : vector.entrySet()) {
					treeBuffer.append("\t\t ("+vectorItem.getKey()+") => " + "{"+showVector(vectorItem.getValue())+"}"+"\n");

				}
			}
		}
		
		back.put("tree",treeBuffer.toString());
		
		//Show Matrix
		final Map<String,Map<String,Set<Map<String,Object>>>> matrix = new LinkedHashMap<String,Map<String,Set<Map<String,Object>>>>();
		
		for (final Map.Entry<String, Set<Map<String, String>>> treeItem : pageTree.entrySet()) {
			final String sourcePage = treeItem.getKey();
			Map<String,Set<Map<String,Object>>> row = null;
			if(matrix.containsKey(sourcePage)) row = matrix.get(sourcePage);
			if(row == null) row = new LinkedHashMap<String,Set<Map<String,Object>>>(); 
			
			final Set<Map<String, String>> transitions = treeItem.getValue();
			for (Map<String, String> target : transitions) {
				final String targetPage = target.get("target");
				final String condition = target.get("condition");
				
				final ElementVector context = contextTree.get(sourcePage).get(targetPage).get(condition);
				
				Set<Map<String,Object>> cell = null;
				if(row.containsKey(targetPage)) cell = row.get(targetPage);
				if(cell == null) cell = new LinkedHashSet<Map<String,Object>>(); 
				
				final Map<String,Object> tupel = new LinkedHashMap<String,Object>();
				tupel.put("condition", condition);
				tupel.put("context", context);

				cell.add(tupel);
				
				row.put(targetPage, cell);
			}
			
			matrix.put(sourcePage, row);
		}
		
		if(!matrix.isEmpty()){
			final StringBuffer matrixBuffer = new StringBuffer();
			final Set<String> pageSet = matrix.keySet();
			
			//add Header
			matrixBuffer.append("'Source/Target';");
			for(final String page:pageSet){
				matrixBuffer.append("'"+page+"';");
			}
			matrixBuffer.append("\n");

			for(final String rowPage:pageSet){
				matrixBuffer.append("'"+rowPage+"';");
				Map<String, Set<Map<String, Object>>> row = matrix.get(rowPage);
				for(final String colPage:pageSet){
					if(row.containsKey(colPage)){
						Set<Map<String,Object>> content = row.get(colPage);
						if(!content.isEmpty()){
							final StringBuffer cellBuffer = new StringBuffer();
							for(final Map<String,Object> item : content){
								final ElementVector context = (ElementVector) item.get("context");
								final Map<String, Set<Element>> tupels = context.getTupels();
								final StringBuffer contextBuffer = new StringBuffer();
								if (tupels != null) {
									for (Map.Entry<String, Set<Element>> tupel : tupels.entrySet()) {
										String variable = tupel.getKey();
										if(variable.endsWith(".value"))variable = variable.substring(0, variable.length()-6);
										if(variable.equals("true"))continue;
										if(variable.equals("false"))continue;
										if(variable.equals("zofar"))continue;

										contextBuffer.append(""+variable+" = [");
										boolean first2 = true;
										for (Element element : tupel.getValue()) {
											if(!first2)contextBuffer.append(" , ");
											contextBuffer.append("" + element.getValue());
											first2 = false;
										}
										contextBuffer.append("]\n");
									}
								}
								cellBuffer.append("Condition : "+((String)item.get("condition")).replace("'","\'")+"\n");
								cellBuffer.append(""+contextBuffer.toString()+"\n");
								cellBuffer.append("\n");
							
							}
							matrixBuffer.append("'"+cellBuffer.toString()+"';");
						}
						else matrixBuffer.append("'';");
					}
					else matrixBuffer.append("'';");
				}

				matrixBuffer.append("\n");
			}
			back.put("matrix",matrixBuffer.toString());	
		}
		
		return back;
	}
	
	private Map<String, Map<String, Map<String,ElementVector>>> analyseQML(
			final Map<String, Set<Map<String, String>>> pageTree, final Map<String, ElementVector> pageInitials,final boolean escalateExceptions) throws Exception {
		Map<String, Map<String, Map<String,ElementVector>>> back = new LinkedHashMap<String, Map<String, Map<String,ElementVector>>>();
		final Map<String, ElementVector> contextMap = new LinkedHashMap<String, ElementVector>();
		for (final Map.Entry<String, Set<Map<String, String>>> treeItem : pageTree.entrySet()) {
			final String sourcePage = treeItem.getKey();

			ElementVector sourceContext = null;
			if (contextMap.containsKey(sourcePage)) {
				sourceContext = contextMap.get(sourcePage);
			}

			if (sourceContext == null) {
				sourceContext = pageInitials.get(sourcePage);
				contextMap.put(sourcePage, sourceContext);
			} else {
				// merge initial vector items if they don't already exist
				// anywhere because of conditions that aim on page reload
				final ElementVector pageInitial = pageInitials.get(sourcePage);
				if (pageInitial != null) {
					final Map<String, Set<Element>> initialTupels = pageInitial.getTupels();
					final Map<String, Set<Element>> sourceTupels = sourceContext.getTupels();

					for (Map.Entry<String, Set<Element>> initialItem : initialTupels.entrySet()) {
						final String variable = initialItem.getKey();

						Set<Element> initialSet = initialItem.getValue();
						Set<Element> sourceSet = sourceTupels.get(variable);

						if (sourceSet == null) {
							sourceContext.getTupels().put(variable, initialSet);
						}
					}
				}
			}

			ElementVector orderedContext = sourceContext;

			final Set<Map<String, String>> transitions = treeItem.getValue();

			for (Map<String, String> target : transitions) {
				final String targetPage = target.get("target");
				final String condition = target.get("condition");

				try {
					ElementVector targetContext = analyzeCondition(condition, orderedContext,escalateExceptions);
					contextMap.put(targetPage, targetContext);

					// remove targetContext content from orderedContext
					if (targetContext != null) {
						final Map<String, Set<Element>> targetTupels = targetContext.getTupels();
						final Map<String, Set<Element>> sourceTupels = sourceContext.getTupels();

						for (Map.Entry<String, Set<Element>> targetItem : targetTupels.entrySet()) {
							final String variable = targetItem.getKey();
							Set<Element> targetSet = targetItem.getValue();
							Set<Element> sourceSet = sourceTupels.get(variable);
							Set<Element> differenceSet = new LinkedHashSet<Element>();

							if (sourceSet != null) {
								if (!sourceSet.equals(targetSet)) {
									for (final Element sourceElement : sourceSet) {
										if (targetSet.contains(sourceElement)) differenceSet.add(sourceElement);
									}
								}
							}

							if (!differenceSet.isEmpty()) {
								orderedContext.getTupels().get(variable).removeAll(differenceSet);
							}
						}
					}

					Map<String, Map<String,ElementVector>> fromBack = null;
					if (back.containsKey(sourcePage))
						fromBack = back.get(sourcePage);
					if (fromBack == null) {
						fromBack = new LinkedHashMap<String, Map<String,ElementVector>>();
					}
					Map<String,ElementVector> fromToSet = null;
					if (fromBack.containsKey(targetPage))
						fromToSet = fromBack.get(targetPage);
					if (fromToSet == null) {
						fromToSet = new LinkedHashMap<String,ElementVector>();
					}

					fromToSet.put(condition,targetContext);
					fromBack.put(targetPage, fromToSet);
					back.put(sourcePage, fromBack);

				} catch (Exception e) {
					throw new Exception("" + sourcePage+" =("+condition+") => " + targetPage+"\n"+"Source Context: "+showVector(sourceContext)+"\n"+"Ordered Context: "+showVector(orderedContext)+"\n"+"Error : "+e.getMessage(),e);
				}
			}
		}
		return back;
	}
	
	private ElementVector analyzeCondition(final String condition,final ElementVector contextVector,final boolean escalateExceptions) throws Exception{
		final Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("zofar", FunctionProvider.getInstance());
		final TermAnalyzerClient analyzer = TermAnalyzerClient.getInstance();
		ElementVector back = contextVector;
		try{
			Map<String, ElementVector> result = analyzer.analyze(condition, properties, back,escalateExceptions);
			if (result != null) {
				for (Entry<String, ElementVector> possibles : result.entrySet()) {
					back = analyzer.mergeVectors(possibles.getValue(), back);
				}
			}
		}
		catch(final Exception e){
			throw e;
		}

		return back;
	}
	
	private String showVector(final ElementVector vector){
		final Map<String, Set<Element>> tupels = vector.getTupels();
		final StringBuffer back = new StringBuffer();
		if (tupels != null) {
			boolean first1 = true;
			for (Map.Entry<String, Set<Element>> tupel : tupels.entrySet()) {
				if(!first1)back.append(" , ");
				String variable = tupel.getKey();
				if(variable.endsWith(".value"))variable = variable.substring(0, variable.length()-6);
				
				back.append(""+variable+" = [");
				boolean first2 = true;
				for (Element element : tupel.getValue()) {
					if(!first2)back.append(" , ");
					back.append("" + element.getValue());
					first2 = false;
				}
				back.append("]");

				first1 = false;
			}
		}
		return back.toString();
	}
}
