package de.his.zofar.simulator.threading;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Pattern;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.his.zofar.simulator.io.record.RecordUtil;
import de.his.zofar.simulator.threading.components.CustomThreadPool;
import de.his.zofar.simulator.threading.components.TestThread;
import eu.dzhw.zofar.management.comm.db.postgresql.PostgresClient;
import eu.dzhw.zofar.management.utils.files.CSVClient;
import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class RefactoredThreadSuite implements Observer {

	private final String urlBase;
	private final String proxyUrl;
	private final int proxyPort;

	private final String targetProtocol;
	private final String targetUrl;
	private final int targetPort;

	private final List<String> tokens;
	private CustomThreadPool threadPool;

	private int runSynchron = 0;

	private int threadTimeOut = 0;
	private int minThreadTimeOut = 0;
	private int maxThreadTimeOut = 0;

	private int packetSize = -1;
	private int packetTimeOut = 0;

	private long actionDelayMin = 0;
	private long actionDelayMax = 0;
	
	private int pageCount = 0;

	private final String recordDir;
	private final String recordSuffix;

	private final HashMap<Object, HashMap<String, Object>> behaviours;
	private final HashMap<Object, String> cookies;
	private final HashMap<String, Integer> cookieCount;
	private final Map<String, Map<String,String>> typeMap;

	public RefactoredThreadSuite(final String proxyUrl, final int proxyPort,
			final String targetProtocol, final String targetUrl,
			final int targetPort, final String urlBase,
			final List<String> tokens, final String recordDir,
			final String recordSuffix,final Document qml) {
		super();
		this.proxyUrl = proxyUrl;
		this.proxyPort = proxyPort;

		this.targetUrl = targetUrl;
		this.targetProtocol = targetProtocol;

		this.targetPort = targetPort;
		this.tokens = tokens;
		this.urlBase = urlBase;

		this.recordDir = recordDir;
		this.recordSuffix = recordSuffix;

		behaviours = new HashMap<Object, HashMap<String, Object>>();
		cookies = new HashMap<Object, String>();
		cookieCount = new HashMap<String, Integer>();
		typeMap = new LinkedHashMap<String,Map<String,String>>();
		processQML(qml,typeMap);
		//System.exit(0);
	}
	
	private void processQML(final Document qml,Map<String, Map<String,String>> types){
		final XmlClient xmlClient = XmlClient.getInstance();
	    final NodeList usingNodes = xmlClient.getByXPath(qml.getDocumentElement(),"//*[@variable]");
	    Map<String,Integer> slotIds = new HashMap<String,Integer>();
	    int itemlft = 1;
	    for (int i = 0; i < usingNodes.getLength(); ++i) {
	    	Node e = (Node) usingNodes.item(i);

	    	final String nodeType = e.getNodeName();	
	    	String mode = "UNKOWN";
	    	String dependency = "NONE";
    	
	    	final String parentType = e.getParentNode().getNodeName();
	    	final String id = createPath(e);
	    	String outId = id+"";

	    	if(nodeType.equals("zofar:questionOpen")){
	    		mode = "OPEN";
	    		if(parentType.equals("zofar:body")){
	    			// Open Question
	    			outId = outId +":responsedomain:response:inputField";
	    		}
	    		else if(parentType.equals("zofar:answerOption")){
	    			// Attached Open
	    			outId = outId +":aoq";
	    			dependency = createPath(e.getParentNode());
	    		}
		    	else{
		    		System.err.println("node : "+nodeType+" parent : "+parentType);
		    	}
	    	}
	    	else if(nodeType.equals("zofar:question")){
	    		mode = "OPEN";
		    	if(parentType.equals("zofar:item")){
	    			// Open in Matrix
//		    		outId = outId +":responsedomain:response:inputField";
		    		outId = outId +":inputField";
	    		}
		    	else{
		    		System.err.println("node : "+nodeType+" parent : "+parentType);
		    	}
	    	}
	    	else if(nodeType.equals("zofar:attachedOpen")){
	    		mode = "OPEN";
		    	if(parentType.equals("zofar:item")){
	    			// Attached Open
		    		outId = outId +":aoq";
		    		dependency = createPath(e.getParentNode());
	    		}
		    	else{
		    		System.err.println("node : "+nodeType+" parent : "+parentType);
		    	}
	    	}
	    	else if(nodeType.equals("zofar:answerOption")){
	    		mode = "MC";
//		    	else{
//		    		System.out.println("node : "+nodeType+" parent : "+parentType);
//		    	}
	    	}
	    	else if(nodeType.equals("zofar:SlotItem")){
	    		mode = "CAL";
		    	if(parentType.equals("zofar:item")){
		    		String uid = "UNKOWN";
			    	final NamedNodeMap tmpAttributes = e.getAttributes();
			    	final Node uidNode = tmpAttributes.getNamedItem("slot");
			    	if(uidNode != null)uid = uidNode.getNodeValue();
			    	final String key = e.getParentNode().hashCode()+"";
			    	int itemId = -1;
			    	if(slotIds.containsKey(key))itemId = slotIds.get(key);
			    	else{
			    		slotIds.put(key,itemlft);
			    		itemId = itemlft;
			    		itemlft = itemlft + 1;
			    	}
		    		outId = outId +":item"+itemId+":tile:"+uid.toLowerCase()+"Check";
	    		}
		    	else{
		    		System.err.println("node : "+nodeType+" parent : "+parentType);
		    	}
	    	}
	    	else if(nodeType.equals("zofar:responseDomain")){
	    		mode = "SC";
		    	if(parentType.equals("zofar:questionSingleChoice")){
	    		}
		    	else if(parentType.equals("zofar:item")){
		    		String uid = "UNKOWN";
			    	final NamedNodeMap tmpAttributes = e.getAttributes();
			    	final Node uidNode = tmpAttributes.getNamedItem("uid");
			    	if(uidNode != null)uid = uidNode.getNodeValue();
			    	
		    		outId = outId +":"+uid;
	    		}
		    	else{
		    		System.err.println("node : "+nodeType+" parent : "+parentType);
		    	}
	    	}
	    	else if(nodeType.equals("zofar:left")){
	    		mode = "SC";
		    	if(parentType.equals("zofar:item")){
	    		}
		    	else{
		    		System.err.println("node : "+nodeType+" parent : "+parentType);
		    	}
	    	}
	    	else if(nodeType.equals("zofar:right")){
	    		mode = "SC";
		    	if(parentType.equals("zofar:item")){
	    		}
		    	else{
		    		System.err.println("node : "+nodeType+" parent : "+parentType);
		    	}
	    	}
	    	else{
	    		System.err.println("node : "+nodeType+" parent : "+parentType);
	    	}
	    	
	    	
	    	final NamedNodeMap nodeAttributes = e.getAttributes();
	    	
	    	final Node specialNode = nodeAttributes.getNamedItem("type");
	    	if(specialNode != null)mode = mode+"."+specialNode.getNodeValue();
	    	
	    	final Node maxNode = nodeAttributes.getNamedItem("maxLength");
	    	String max = "-1";
	    	if(maxNode != null)max = maxNode.getNodeValue();
	    	
	    	final Node minValueNode = nodeAttributes.getNamedItem("minValue");
	    	String minValue = "-1";
	    	if(minValueNode != null)minValue = minValueNode.getNodeValue();
	    	
	    	final Node maxValueNode = nodeAttributes.getNamedItem("maxValue");
	    	String maxValue = "-1";
	    	if(maxValueNode != null)maxValue = maxValueNode.getNodeValue();
	    	
	    	final Node variableNode = nodeAttributes.getNamedItem("variable");
	    	String variable = "UNKOWN";
	    	if(variableNode != null)variable = variableNode.getNodeValue();
	    	
	    	final Map<String,String> meta = new HashMap<String,String>();
	    	meta.put("VARIABLE", variable);
	    	meta.put("MODE", mode);
	    	meta.put("MAX", max);
	    	meta.put("MINVALUE", minValue);
	    	meta.put("MAXVALUE", maxValue);
	    	meta.put("TYPE", nodeType);
	    	meta.put("PARENT", parentType);
	    	meta.put("DEPENDENCY",dependency);
	    	
	    	types.put(outId,meta);
	    }	
	    
//	    for(Map.Entry<String, Map<String,String>> item : types.entrySet()){
//	    	System.out.println(item.getKey()+" => "+item.getValue());
//	    }
//	    System.out.println("\n******************************\n");
	}
	
	private String createPath(Node node){
    	String path = "";
    	String nodename=node.getNodeName();
    	while(nodename.startsWith("zofar")||nodename.startsWith("display")){
    		String uid = "";
	    	final NamedNodeMap parentAttributes = node.getAttributes();
	    	final Node parentUIDNode = parentAttributes.getNamedItem("uid");
	    	if(parentUIDNode != null)uid = parentUIDNode.getNodeValue();
	    	String separator = ":";
	    	if((path.equals(""))||(uid.equals("")))separator = "";
	    	boolean visible = true;
	    	if(nodename.equals("zofar:body"))visible = false;
	    	if(nodename.equals("zofar:responseDomain")&&node.getParentNode().getNodeName().equals("zofar:item"))visible = false;
	    	if(visible){
	    		path = uid+separator+path;
	    		if(nodename.equals("zofar:calendar"))path = uid+":"+path;
	    	}
	    	node = node.getParentNode();
	    	nodename=node.getNodeName();
    	}
		return path;
	}

	public void setPacketSize(final int packetSize) {
		this.packetSize = packetSize;
	}

	public void setPacketTimeOut(final int packetTimeOut) {
		this.packetTimeOut = packetTimeOut;
	}

	public void setThreadTimeOut(final int threadTimeOut) {
		this.threadTimeOut = threadTimeOut;
	}

	public int getThreadTimeOut() {
		if (this.maxThreadTimeOut > 0) {
			this.threadTimeOut = minThreadTimeOut
					+ (int) (Math.random() * ((maxThreadTimeOut - minThreadTimeOut) + 1));
		}
		return this.threadTimeOut;
	}

	public void setMinThreadTimeOut(final int min) {
		this.minThreadTimeOut = min;
	}

	public void setMaxThreadTimeOut(final int max) {
		this.maxThreadTimeOut = max;
	}

	public void setActionDelayMin(final long actionDelayMin) {
		this.actionDelayMin = actionDelayMin;
	}

	public void setActionDelayMax(final long actionDelayMax) {
		this.actionDelayMax = actionDelayMax;
	}

	public void setRunSynchron(final int runSynchron) {
		this.runSynchron = runSynchron;
	}
	
	public void setPageCount(final int pageCount) {
		this.pageCount = pageCount;
	}

	private TestThread createThread(final String proxyUrl, final int proxyPort,
			final String targetProtocol, final String targetUrl,
			final int targetPort, final String token, final String param) {
		final TestThread thread = new TestThread(proxyUrl, proxyPort,
				targetProtocol, targetUrl, targetPort, token, urlBase + token,
				RecordUtil.loadRecords(this.recordDir, token,
						this.recordSuffix, "::"),actionDelayMin,actionDelayMax,runSynchron,pageCount,typeMap);
		thread.addObserver(this);
		return thread;
	}

	public void exec() throws Exception {
		if (this.tokens == null)
			throw new Exception("Tokens are null");
		if (this.tokens.isEmpty())
			throw new Exception("Tokens are empty");

		final Iterator<String> tokenIt = this.tokens.iterator();
		final List<String> packet = new ArrayList<String>(this.packetSize);

		while (tokenIt.hasNext()) {
			final String token = tokenIt.next();
			packet.add(token);
			if (packet.size() >= this.packetSize) {
				execHelper(packet);
				packet.clear();
			}
		}
		if (packet.size() > 0) {
			execHelper(packet);
			packet.clear();
		}
	}

	public void execHelper(final List<String> packet) throws Exception {
		System.out.println("Execute packet");
		Thread.sleep((this.packetTimeOut * 1000L));
		final Iterator<String> tokenIt = packet.iterator();

		while (tokenIt.hasNext()) {
			final String token = tokenIt.next();
			final TestThread thread = createThread(proxyUrl, proxyPort,
					targetProtocol, targetUrl, targetPort, token, urlBase
							+ token);
			long startDelay = (((this.actionDelayMax+this.actionDelayMin)/2)*pageCount)/runSynchron;
			System.out.println(this+" (STARTUP) startup sleep for "+startDelay+" ms");
			Thread.sleep(startDelay);
			this.addTask(thread);
		}
		this.shutDown();
	}

	private void addTask(final TestThread task) {
		if (this.threadPool == null)
			threadPool = new CustomThreadPool(this.runSynchron);
		this.threadPool.runTask(task);
	}

	private void shutDown() {
		if (this.threadPool == null)
			threadPool = new CustomThreadPool(this.runSynchron);
		this.threadPool.shutdown();
		this.threadPool.isTerminated();
		this.threadPool = null;
		System.out.println("ThreadPool done");
	}

	private final DateFormat timestampToDate = new SimpleDateFormat(
			"dd.MM.yyyy HH:mm:ss.SSS");

	private String convertTimestampToDate(final Long time) {
		if (time == null)
			return null;
		if (time == 0L)
			return null;

		final Date date = new Date(time);
		if (date != null) {
			return timestampToDate.format(date);
		}
		return null;
	}

	private void output(final String thread, final String token,
			final String cookie, final String cookieCounter,
			final String worker, final String delay, final String slot1,
			final String slot2, final String slot3, final String slot4,
			final String slot5, final String slot6) {
		System.out.println(convertTimestampToDate(System.currentTimeMillis())
				+ ";" + thread + ";" + token + ";" + slot1 + ";" + slot2 + ";"
				+ slot3 + ";" + slot4 + ";" + slot5 + ";" + slot6 + ";"
				+ cookie + ";" + cookieCounter + ";" + worker + ";" + delay);
		System.out.flush();
	}
	
	private void toPersistenceLog(final String token, final String data) {
//		System.out.println("[PERSISTENCE] ("+token+") "+data);
		final File dir = DirectoryClient.getInstance().createDir(new File(this.recordDir), "persistence");
		final File file = FileClient.getInstance().createOrGetFile(token, ".csv", dir);
		try {
			FileClient.getInstance().writeToFile(file.getAbsolutePath(), data, false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void clearPersistence(){
		final File dir = DirectoryClient.getInstance().createDir(new File(this.recordDir), "persistence");
		DirectoryClient.getInstance().cleanDirectory(dir);
	}
	
	public boolean checkPersistence(final String db_location,final String db_port,final String db_name,final String db_user,final String db_password) throws Exception{
		final File dir = DirectoryClient.getInstance().createDir(new File(this.recordDir), "persistence");
		List<File> datasets = DirectoryClient.getInstance().readDir(dir, DirectoryClient.SortMode.UNSORTED);
		if(datasets != null){
			PostgresClient postgres = PostgresClient.getInstance();
			Connection connection = null;
			try{
				connection = postgres.getConnection(db_location, db_port, db_name, db_user, db_password);
			}
			catch(Exception e){
				throw new Exception("cannot establish connection : location="+db_location+" port="+db_port+" dbname="+db_name+" db_user="+db_user+" db_password="+db_password,e);
			}
			CSVClient csvClient = CSVClient.getInstance();
			List<String> headers = new LinkedList<String>();
			headers.add("VARIABLE");
			headers.add("DATA");
			headers.add("VERSION");
			if(connection != null){
				for(final File dataset : datasets){
					final String token = dataset.getName().replaceAll(Pattern.quote(".csv"), "");
					System.out.println("analyse dataset "+token);
					List<Map<String, String>> fromFile  = csvClient.loadCSV(dataset, headers, true);
					Map<String,String> fromFileData = new HashMap<String,String>();
					Map<String,Integer> fromFileVersion = new HashMap<String,Integer>();
					for(final Map<String,String> line :fromFile){
						final String variable = line.get("VARIABLE");
						final String tmpData =  line.get("DATA");
						final String version =  line.get("VERSION");
						fromFileData.put(variable, tmpData);
						fromFileVersion.put(variable, Integer.parseInt(version));
					}
					
					
					List<Map<String, String>> fromDB = null;
					try{
						fromDB = postgres.queryDb(connection, "select t1.variablename AS variable,t1.value as data,t1.version as version from surveydata AS t1,participant AS t2 where t1.participant_id=t2.id AND t2.token = '"+token+"';");
					}
					catch(Exception e){
						throw new Exception("cannot query db : location="+db_location+" port="+db_port+" dbname="+db_name+" db_user="+db_user+" db_password="+db_password,e);
					}
					
					Map<String,String> fromDBData = new HashMap<String,String>();
					Map<String,Integer> fromDBVersion = new HashMap<String,Integer>();
					
					for(final Map<String,String> line :fromDB){
						final String variable = line.get("variable");
						final String tmpData =  line.get("data");
						final String version =  line.get("version");
						fromDBData.put(variable, tmpData);
						fromDBVersion.put(variable, Integer.parseInt(version));
					}
					
//					System.out.println("fromFile : "+fromFileData);
//					System.out.println("fromDB : "+fromDBData);
					
					Set<String> variables = fromFileData.keySet();
					for(final String variable: variables){
						if(variable.equals("UNKOWN"))continue;
						final String dataFromFile = (fromFileData.get(variable)+"").trim();
						final String dataFromDB = (fromDBData.get(variable)+"").trim();
						
						if(!dataFromFile.equals(dataFromDB)){
							final Exception e = new Exception("["+token+"] Data not equals for variable "+variable+" ==> "+dataFromFile+" != "+dataFromDB);
							throw e;
						}
					}
				}
			}
			else throw new Exception("cannot establish connection : location="+db_location+" port="+db_port+" dbname="+db_name+" db_user="+db_user+" db_password="+db_password);
			return true;
		}
		else{
			throw new Exception("no persistence check data found at "+dir.getAbsolutePath());
		}
	}

	public void update(final Observable o, final Object arg) {
		if ((arg != null) && (arg instanceof Map)) {

			// retrieve informations
			final Map<String, Object> tmp = (Map<String, Object>) arg;

			Object thread = null;
			String status = null;
			HashMap<String, String> back = null;

			if (tmp != null) {
				if (tmp.containsKey("thread"))
					thread = tmp.get("thread");
				if (tmp.containsKey("status"))
					status = (String) tmp.get("status");
				if (tmp.containsKey("back"))
					back = (HashMap<String, String>) tmp.get("back");
			}

			if ((thread != null) && (back != null)) {
				if (back.containsKey("cookie")) {
					if (!cookies.containsKey(thread)) {
						Integer counter = 0;
						final String tmpCookie = back.get("cookie");
						if (cookieCount.containsKey(tmpCookie))
							counter = cookieCount.get(tmpCookie);
						counter = counter + 1;
						cookieCount.put(tmpCookie, counter);
					}
					cookies.put(thread, back.get("cookie"));
				}
			}

			String threadStr = "NULL";
			String tokenStr = "NULL";
			String statusStr = "NULL";
			String timeStr = "NULL";
			String posttimeStr = "NULL";
			String codeStr = "NULL";
			String vertexStr = "NULL";
			String cookieStr = "NULL";
			String cookieCountStr = "NULL";
			String workerStr = "NULL";

			if (thread != null) {
				threadStr = ((TestThread) thread).toString();
				tokenStr = ((TestThread) thread).getToken();
			}
			if (status != null)
				statusStr = status;
			if (back != null) {
				if (back.containsKey("time"))
					timeStr = back.get("time");
				if (back.containsKey("posttime"))
					posttimeStr = back.get("posttime");
				if (back.containsKey("code"))
					codeStr = back.get("code");
				if (back.containsKey("tag"))
					vertexStr = back.get("tag");
				if (back.containsKey("worker"))
					workerStr = back.get("worker");
			}

			if (cookies.containsKey(thread)) {
				cookieStr = cookies.get(thread);
				if (cookieCount.containsKey(cookieStr))
					cookieCountStr = cookieCount.get(cookieStr).intValue() + "";
			}
			final long delay = actionDelayMin
					+ (int) (Math.random() * ((actionDelayMax - actionDelayMin) + 1));

			this.output(threadStr, tokenStr, cookieStr, cookieCountStr,
					workerStr, delay + "", statusStr, codeStr, timeStr,
					vertexStr, posttimeStr, "NULL");

			// steering
			if ((status != null) && (thread != null)) {
				HashMap<String, Object> behaviour = null;
				if (behaviours.containsKey(thread))
					behaviour = behaviours.get(thread);
				if (behaviour == null)
					behaviour = new HashMap<String, Object>();

				Integer all = 0;
				if (behaviour.containsKey("all"))
					all = (Integer) behaviour.get("all");

				Integer steps = 0;
				if (behaviour.containsKey("steps"))
					steps = (Integer) behaviour.get("steps");

				Integer started = 0;
				if (behaviour.containsKey("started"))
					started = (Integer) behaviour.get("started");
				Integer previous = 0;
				if (behaviour.containsKey("previous"))
					previous = (Integer) behaviour.get("previous");

				Integer next = 0;
				if (behaviour.containsKey("next"))
					next = (Integer) behaviour.get("next");

				Stack<Object> stack = null;
				if (behaviour.containsKey("stack"))
					stack = (Stack<Object>) behaviour.get("stack");
				if (stack == null)
					stack = new Stack<Object>();

				// Steer behaviour
				int nextAction = -1;

				// Check for recorded Movement
				if ((thread != null)
						&& ((TestThread.class).isAssignableFrom(thread
								.getClass()))) {
					final TestThread testThread = (TestThread) thread;
					final Map<String, Object> recordData = testThread.getUnit()
							.getRecord(vertexStr);

					if ((recordData != null) && recordData.containsKey("dir")) {
						final String movement = (String) recordData.get("dir");
						if (movement.equals("FORWARD")) {
							// System.out.println("recorded movement : next");
							nextAction = 3;
						} else if (movement.equals("BACKWARD")) {
							// System.out.println("recorded movement : back");
							nextAction = 2;
						} else {
							// System.out.println("recorded movement : UNKOWN ==> unkown");
							nextAction = -1;
						}
					}
				}

				// Check loop
				boolean loop = false;

				Object top = null;
				if (!stack.isEmpty())
					top = stack.peek();
				if ((top != null) && (top.equals(vertexStr))) {
					final int stackSize = stack.size();
					if (stackSize >= 3) {
						loop = true;
						for (int a = 1; a <= 2; a++) {
							top = stack.elementAt((stackSize - 1) - a);

							if (!top.equals(vertexStr)) {
								loop = false;
								break;
							}
						}
					}
					if (!loop)
						stack.push(vertexStr);
				} else
					stack.push(vertexStr);

				// choose next action
				if (loop) {
					nextAction = 4;
				} else if ((codeStr != null) && (codeStr.equals("500"))) {
					nextAction = 5;
				} else if (nextAction == -1) {
					if (vertexStr == null)
						vertexStr = "UNKOWN";

					if (status.equals("started")) {
						started = started + 1;
						// nextAction = 3;
						if (vertexStr.equals("UNKOWN"))
							nextAction = -1;
						else if (vertexStr.equals("end")) {
							nextAction = -1;
						} else
							nextAction = 3;
					}
					if (status.equals("next")) {
						next = next + 1;
						if (vertexStr.equals("UNKOWN"))
							nextAction = -1;
						else if (vertexStr.equals("login")) {
							nextAction = 1;
						} else if (vertexStr.equals("end")) {
							nextAction = -1;
						} else
							nextAction = 3;
					}
					if (status.equals("previous")) {
						next = next + 1;
						if (vertexStr.equals("UNKOWN"))
							nextAction = -1;
						else if (vertexStr.equals("index")) {
							nextAction = 3;
							all = all + 1;
						} else
							nextAction = 2;
					}
					// }

				} else {
					// recorded action
					next = next + 1;
					all = all + 1;
				}

				// System.out.println("next Action : " + nextAction);

				steps = steps + 1;

				behaviour.put("started", started);
				behaviour.put("previous", previous);
				behaviour.put("next", next);
				behaviour.put("all", all);
				behaviour.put("steps", steps);
				behaviour.put("stack", stack);
				behaviours.put(thread, behaviour);

				try {
					switch (nextAction) {
					case 1:
						long startDelay = (((maxThreadTimeOut+minThreadTimeOut)/2)*pageCount)/runSynchron;
						System.out.println(this+" (Suite) startup sleep for "+startDelay+" ms");

						((TestThread) thread).startUnit(startDelay);
						break;
					case 2:
						((TestThread) thread).previousUnit(delay);
						break;
					case 3:
						((TestThread) thread).nextUnit(delay);
						break;
					case 4:
						this.output(threadStr, tokenStr, cookieStr,
								cookieCountStr, workerStr, "NULL", "NULL",
								"loop", steps + "", "NULL", "NULL", "NULL");
						this.toPersistenceLog(((TestThread) thread).getToken(),((TestThread) thread).getPersistenceLog());
						((TestThread) thread).interrupt();
						break;
					case 5:
						this.output(threadStr, tokenStr, cookieStr,
								cookieCountStr, workerStr, "NULL", "NULL",
								"restart", timeStr, codeStr, posttimeStr,
								"NULL");
						this.toPersistenceLog(((TestThread) thread).getToken(),((TestThread) thread).getPersistenceLog());
						((TestThread) thread).interrupt();
						this.threadPool.runTask((TestThread) thread);
						break;
					default:
						this.output(threadStr, tokenStr, cookieStr,
								cookieCountStr, workerStr, "NULL", "NULL",
								"finished", steps + "", "NULL", "NULL", "NULL");
						behaviours.remove(thread);
						this.toPersistenceLog(((TestThread) thread).getToken(),((TestThread) thread).getPersistenceLog());
						((TestThread) thread).interrupt();
						System.gc();
						break;
					}
				} catch (final Exception e) {
					e.printStackTrace();
					this.output(threadStr, tokenStr, cookieStr, cookieCountStr,
							workerStr, "NULL", "NULL", "ERROR", steps + "",
							"NULL", "NULL", "NULL");
					behaviours.remove(thread);
					this.toPersistenceLog(((TestThread) thread).getToken(),((TestThread) thread).getPersistenceLog());
					((TestThread) thread).interrupt();
					System.gc();
				}

			}
		}
	}

}
