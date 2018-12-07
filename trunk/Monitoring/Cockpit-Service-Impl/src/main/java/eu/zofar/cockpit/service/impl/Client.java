package eu.zofar.cockpit.service.impl;

import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Properties;
import java.util.Set;

import javax.jws.WebService;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.zofar.cockpit.service.StatusService;
import eu.zofar.cockpit.service.transfer.ClientTransfer;
import eu.zofar.cockpit.service.transfer.Feedback;
import eu.zofar.cockpit.service.transfer.SystemInformations;
import eu.zofar.cockpit.utils.ConfigurationUtils;

@WebService(endpointInterface = "eu.zofar.cockpit.service.StatusService")
public class Client extends Observable implements Serializable, StatusService {
    
    private static final long serialVersionUID = 8651982821597446535L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);
    
    private SessionFactory sessionFactory;
    private Configuration configuration;
    
    private class CustomWork implements Work {
	private final Set<Map<String, Object>> back;
	private final String query;
	
	public CustomWork(final Set<Map<String, Object>> back, final String query) {
	    super();
	    this.back = back;
	    this.query = query;
	    
	}
	
	public Set<Map<String, Object>> getBack() {
	    return this.back;
	}
	
	@Override
	public void execute(final Connection connection) throws SQLException {
	    PreparedStatement st = null;
	    try {
		st = connection.prepareStatement(this.query);
		final ResultSet rs = st.executeQuery();
		
		if (rs != null) {
		    while (rs.next()) {
			final Map<String, Object> row = new HashMap<String, Object>();
			final int count = rs.getMetaData().getColumnCount();
			for (int a = 1; a <= count; a++) {
			    final String name = rs.getMetaData().getColumnName(a);
			    final String value = rs.getString(a);
			    row.put(name, value);
			}
			
			this.back.add(row);
		    }
		}
		
	    } finally {
		if (st != null)
		    st.close();
	    }
	}
    };
    
    public Client() {
	super();
    }
    
    public Client(Observer observer) {
	this();
	this.addObserver(observer);
	this.setChanged();
	this.notifyObservers("phases=5");
    }
    
    public Configuration getConfiguration() {
	if (this.configuration == null) {
	    this.configuration = new Configuration();
	    this.configuration.configure("database.cfg.xml");
	}
	return this.configuration;
    }
    
    private SessionFactory getSessionFactory(final Configuration conf) throws Throwable {
	final ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(conf.getProperties()).build();
	return conf.buildSessionFactory(serviceRegistry);
    }
    
    private Session getSession(final Configuration conf) throws Throwable {
	if (this.sessionFactory == null) {
	    this.sessionFactory = this.getSessionFactory(conf);
	}
	if (this.sessionFactory != null) {
	    return this.sessionFactory.openSession();
	}
	return null;
    }
    
    private String getDBMeta(final Configuration conf) {
	final Properties props = conf.getProperties();
	if ((props != null) && (props.containsKey("connection.url")))
	    return props.get("connection.url") + "";
	return null;
    }
    
    private Set<Map<String, Object>> queryDB(final String queryStr, final Configuration conf) throws Throwable {
	final Session session = this.getSession(conf);
	if (session != null) {
	    final Transaction transaction = session.beginTransaction();
	    try {
		final CustomWork work = new CustomWork(new LinkedHashSet<Map<String, Object>>(), queryStr);
		session.doWork(work);
		transaction.commit();
		return work.getBack();
	    } catch (final Throwable t) {
		transaction.rollback();
		throw t;
	    } finally {
		if ((session != null) && (session.isOpen()))
		    session.close();
	    }
	} else {
	    LOGGER.error("Creating Hibernate Session failed");
	}
	return null;
    }
    
    @Override
    public ClientTransfer status() throws Throwable {
	Configuration conf = getConfiguration();
	return status(conf, ConfigurationUtils.getInstance().getSurveyId());
    }
    
    public ClientTransfer status(final Configuration conf, String id) throws Throwable {
	final ClientTransfer back = new ClientTransfer();
	back.setId(id);
	back.setTimestamp(System.currentTimeMillis());
	try {
	    this.setChanged();
	    this.notifyObservers("history");
	    Set<Map<String, Object>> historyData = this.getHistoryData(conf);
	    Map<String, List<String>> structuredHistory = structureHistory(historyData);
	    this.setChanged();
	    this.notifyObservers("invited");
	    List<String> invited = this.getInvited(conf);
	    this.setChanged();
	    this.notifyObservers("participated");
	    back.setParticipated(this.participated(structuredHistory));
	    this.setChanged();
	    this.notifyObservers("finished");
	    back.setFinished(this.finished(structuredHistory));
	    this.setChanged();
	    this.notifyObservers("feedback");
	    back.setFeedback(this.feedback(invited, historyData));
	} catch (Throwable e) {
	    e.printStackTrace();
	}
	back.setExitPages(this.exitPages(conf));
	back.setSystem(this.serverHealth(conf));
	
	return back;
    }
    
    private Map<String, List<String>> structureHistory(Set<Map<String, Object>> historyData) {
	final Map<String, List<String>> structuredHistory = new LinkedHashMap<String, List<String>>();
	
	if (historyData != null) {
	    for (final Map<String, Object> row : historyData) {
		final String token = row.get("token") + "";
		final String page = row.get("page") + "";
		List<String> tmpHistory = null;
		if (structuredHistory.containsKey(token))
		    tmpHistory = structuredHistory.get(token);
		if (tmpHistory == null)
		    tmpHistory = new ArrayList<String>();
		tmpHistory.add(page);
		structuredHistory.put(token, tmpHistory);
	    }
	}
	return structuredHistory;
    }
    
    private Double finished(Map<String, List<String>> structuredHistory) {
	LOGGER.info("finished");
	try {
	    // Map<String,List<String>> structuredHistory =
	    // structureHistory(historyData);
	    double count = 0D;
	    for (final Map.Entry<String, List<String>> set : structuredHistory.entrySet()) {
		if (set.getValue().contains("/end.xhtml"))
		    count = count + 1D;
	    }
	    return count;
	} catch (final Throwable e) {
	    e.printStackTrace();
	}
	return 0D;
    }
    
    private Double participated(Map<String, List<String>> structuredHistory) {
	LOGGER.info("participated");
	try {
	    // Map<String,List<String>> structuredHistory =
	    // structureHistory(historyData);
	    double count = 0D;
	    for (final Map.Entry<String, List<String>> set : structuredHistory.entrySet()) {
		if ((!set.getValue().isEmpty()) && (!set.getValue().contains("/end.xhtml")))
		    count = count + 1D;
	    }
	    return count;
	} catch (final Throwable e) {
	    e.printStackTrace();
	}
	return 0D;
    }
    
    private String[][] exitPages(final Configuration conf) throws Throwable {
	LOGGER.info("exitPages");
	final Set<Map<String, Object>> result = this.queryDB("select count(result.id) AS exitcount,result.page as exitpage from (select t1.participant_id as id,(select t2.page from surveyhistory as t2 where t2.participant_id=t1.participant_id and t2.timestamp = max(t1.timestamp)) as page, max(t1.timestamp) as stamp from surveyhistory as t1 where t1.timestamp < (current_timestamp - interval '1 hour') group by t1.participant_id) as result group by result.page;", conf);
	if (result != null) {
	    final Map<String, String[]> unordered = new HashMap<String, String[]>();
	    Integer blankLft = Integer.MIN_VALUE;
//	    for (final Map<String, Object> page : result) {
//		final String[] item = new String[2];
//		String order = (page.get("exitpage") + "").replaceAll("[^\\d]", "");
//		
//		if (order.equals("")) {
//		    order = blankLft + "";
//		    blankLft++;
//		}
//		final Integer index = Integer.parseInt(order);
//		item[0] = page.get("exitpage") + "";
//		item[1] = page.get("exitcount") + "";
//		unordered.put(index, item);
//	    }
	    
	    for (final Map<String, Object> page : result) {
		final String[] item = new String[2];
		String order = (page.get("exitpage") + "").replaceAll("/page", "");
		order = order.replaceAll(".xhtml", "");
		
		if (order.equals("")) {
		    order = blankLft + "";
		    blankLft++;
		}
//		final Integer index = Integer.parseInt(order);
		item[0] = page.get("exitpage") + "";
		item[1] = page.get("exitcount") + "";
		unordered.put(order, item);
	    }
	    
	    final List<String> ordered = new ArrayList<String>(unordered.keySet());
	    Collections.sort(ordered);
	    final String[][] back = new String[result.size()][2];
	    int lft = 0;
	    for (final String key : ordered) {
		final String[] pair = unordered.get(key);
		back[lft] = pair;
		lft = lft + 1;
	    }
	    return back;
	}
	return new String[0][0];
    }
    
    private Set<Map<String, Object>> getHistoryData(final Configuration conf) throws Throwable {
	final Set<Map<String, Object>> result = this.queryDB("select t2.token as token,t1.page as page,t1.timestamp as stamp from surveyhistory as t1,participant as t2 where t1.participant_id=t2.id order by t2.token,t1.timestamp;", conf);
	return result;
    }
    
    private List<String> getInvited(final Configuration conf) throws Throwable {
	final Set<Map<String, Object>> result = this.queryDB("select token from participant order by id;", conf);
	final List<String> invited = new ArrayList<String>();
	
	if (result != null) {
	    for (final Map<String, Object> row : result) {
		final String token = row.get("token") + "";
		invited.add(token);
	    }
	}
	return invited;
    }
    
    private Feedback feedback(List<String> invited, Set<Map<String, Object>> historyData) throws Throwable {
	LOGGER.info("feedback");
	
	final Feedback back = new Feedback(invited, new Timestamp(0L));
	
	final Map<String, Map<Timestamp, String>> structuredHistory = new HashMap<String, Map<Timestamp, String>>();
	
	if (historyData != null) {
	    for (final Map<String, Object> row : historyData) {
		final String token = row.get("token") + "";
		final String page = row.get("page") + "";
		final String stampStr = row.get("stamp") + "";
		Map<Timestamp, String> individualHistory = null;
		if (structuredHistory.containsKey(token))
		    individualHistory = structuredHistory.get(token);
		if (individualHistory == null)
		    individualHistory = new LinkedHashMap<Timestamp, String>();
		final Timestamp stamp = Timestamp.valueOf(stampStr);
		individualHistory.put(stamp, page);
		structuredHistory.put(token, individualHistory);
	    }
	}
	
	final Calendar calendar = Calendar.getInstance();
	final Map<Timestamp, Map<String, Feedback.Status>> tmp = new HashMap<Timestamp, Map<String, Feedback.Status>>();
	
	final List<String> finished = new ArrayList<String>();
	
	for (final Map.Entry<String, Map<Timestamp, String>> entry : structuredHistory.entrySet()) {
	    final String token = entry.getKey();
	    final Map<Timestamp, String> history = entry.getValue();
	    for (final Map.Entry<Timestamp, String> historyEntry : history.entrySet()) {
		final Timestamp stamp = historyEntry.getKey();
		calendar.setTimeInMillis(stamp.getTime());
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		final Timestamp endOfWeek = new Timestamp(calendar.getTimeInMillis());
		
		final String page = historyEntry.getValue();
		
		Map<String, Feedback.Status> etappe = null;
		if (tmp.containsKey(endOfWeek))
		    etappe = tmp.get(endOfWeek);
		if (etappe == null)
		    etappe = new HashMap<String, Feedback.Status>();
		
		if (page.equals("/end.xhtml")) {
		    etappe.put(token, Feedback.Status.FINISHED);
		    finished.add(token);
		    break;
		} else {
		    if (!finished.contains(token)) {
			etappe.put(token, Feedback.Status.PARTICIPATED);
		    }
		}
		tmp.put(endOfWeek, etappe);
	    }
	}
	finished.clear();
	final List<String> participated = new ArrayList<String>();
	List<Timestamp> sort = new ArrayList<Timestamp>(tmp.keySet());
	Collections.sort(sort);
	for (final Timestamp stamp : sort) {
	    final Map<String, Feedback.Status> tokens = tmp.get(stamp);
	    for (Map.Entry<String, Feedback.Status> token : tokens.entrySet()) {
		if (token.getValue().equals(Feedback.Status.PARTICIPATED))
		    participated.add(token.getKey());
		if (token.getValue().equals(Feedback.Status.FINISHED))
		    finished.add(token.getKey());
	    }
	    for (final String item : invited) {
		if (!tokens.containsKey(item)) {
		    if (finished.contains(item))
			tokens.put(item, Feedback.Status.FINISHED);
		    else if (participated.contains(item))
			tokens.put(item, Feedback.Status.PARTICIPATED);
		    else
			tokens.put(item, Feedback.Status.INVITED);
		}
	    }
	    back.addFeedback(tokens, stamp);
	}
	return back;
    }
    
    private SystemInformations serverHealth(final Configuration conf) throws Throwable {
	final OperatingSystemMXBean os = ManagementFactory.getOperatingSystemMXBean();
	final MemoryMXBean memory = ManagementFactory.getMemoryMXBean();
	
	if ((os != null) && (memory != null)) {
	    final SystemInformations system = new SystemInformations();
	    system.setLoad(os.getSystemLoadAverage());
	    
	    final long heapLoad = (memory.getHeapMemoryUsage().getUsed()) / (memory.getHeapMemoryUsage().getMax());
	    
	    system.setUsedHeap(heapLoad);
	    system.setLocation(this.getDBMeta(conf));
	    return system;
	}
	return null;
    }
    
}
