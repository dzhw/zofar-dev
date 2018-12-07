package tests.integrity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import tests.common.AbstractVerificationTest;
import tests.common.MessageProvider;


public class PageIntegrity extends AbstractVerificationTest {

	private enum VertexState {
		unkown,unvisited, progress, visited
	}

	private Map<String, VertexState> vertices;
	private Map<String, Set<String>> edges;

	public PageIntegrity() {
		super();
	}

	private void initGraph() {
		vertices = new LinkedHashMap<String, VertexState>();
		edges = new LinkedHashMap<String, Set<String>>();
		final NodeList result = this.getByXPath("page");
		final int count = result.getLength();
		for (int i = 0; i < count; ++i) {
			final Node e = result.item(i);
			if(this.hasParent(e, "zofar:researchdata"))continue;
			final NamedNodeMap attributes = e.getAttributes();
			final Node name = attributes.getNamedItem("uid");
			vertices.put(name.getNodeValue(), VertexState.unkown);
			final NodeList transitionList = this.getByXPath(e, "transitions/*");
			final int count1 = transitionList.getLength();
			for (int j = 0; j < count1; ++j) {
				final Node transition = transitionList.item(j);
				if(this.hasParent(transition, "zofar:researchdata"))continue;
				final NamedNodeMap transitionAttributes = transition
						.getAttributes();
				final Node target = transitionAttributes.getNamedItem("target");
				Set<String> edgeSet = null;
				if (edges.containsKey(name.getNodeValue()))
					edgeSet = edges.get(name.getNodeValue());
				if (edgeSet == null)
					edgeSet = new LinkedHashSet<String>();
				edgeSet.add(target.getNodeValue());
				edges.put(name.getNodeValue(), edgeSet);
				 
			}
		}
	}
	
	private void resetGraph() {
		if(vertices == null) initGraph();
		Iterator<String> it = this.vertices.keySet().iterator();
		while (it.hasNext()) {
			vertices.put(it.next(), VertexState.unvisited);
		}
	}

	private void visitNodes(final String graphVertix)	{
		if(this.vertices.containsKey(graphVertix))this.vertices.put(graphVertix, VertexState.progress);
		Set<String> graphEdges = null;
		if(this.edges.containsKey(graphVertix))graphEdges = this.edges.get(graphVertix);
		if(graphEdges != null){
			final Iterator<String> it = graphEdges.iterator();
			while (it.hasNext()) {
				final String target = it.next();
				if (this.vertices.get(target) != VertexState.unvisited)
					continue;
				visitNodes(target);
			}
		}
		if(this.vertices.containsKey(graphVertix))this.vertices.put(graphVertix, VertexState.visited);
	}
	
	@Override
	@Before
	public void setUp() throws Exception {
		super.setUp();
		initGraph();
		
	}

	@Override
	@After
	public void tearDown() throws Exception {
		super.tearDown();
		vertices = null;
		edges = null;
	}

	@Test
	public void testLostPages() {
		resetGraph();
		MessageProvider.info(this, "test lost pages ...");
		visitNodes("index");
		final Iterator<String> it = vertices.keySet().iterator();
		final Set<String> lostPages = new HashSet<String>();
		while (it.hasNext()) {
			final String page = it.next();
			if (this.vertices.get(page) == VertexState.unvisited){
				lostPages.add(page);
			}
		}
		TestCase.assertTrue("Lost pages found "+lostPages,lostPages.isEmpty());
	}
	
	@Test
	public void testUnkownPages() {
		resetGraph();
		MessageProvider.info(this, "test unkown page targets ...");
		final Set<String> knownPages = vertices.keySet();
		final Set<String> unkownPages = new HashSet<String>();
		for(Map.Entry<String, Set<String>> edge: edges.entrySet()){
		    final String page = edge.getKey();
		    final Set<String> targets = edge.getValue();
//		    System.out.println("Page : "+page);
		    for(String target : targets){
			if(!knownPages.contains(target))unkownPages.add(target);
//			System.out.println("\ttarget : "+target);
		    }
		}
		
		
//		final Iterator<String> it = edges.keySet().iterator();
//		
//		while (it.hasNext()) {
//			final String edge = it.next();
//			System.out.println("edge : "+edge);
////			if (this.vertices.get(page) == VertexState.unvisited){
////				lostPages.add(page);
////			}
//		}
		TestCase.assertTrue("Unkown page targets found "+unkownPages,unkownPages.isEmpty());
	}

}
