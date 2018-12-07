package eu.zofar.cockpit.master.ui;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.inject.Inject;

import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import eu.zofar.cockpit.master.objects.SurveyEntry;

public class UserOutputBean implements Serializable, ActionListener {

	private static final long serialVersionUID = 3302649876019419512L;
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserOutputBean.class);

	@Inject
	private ClientManager clientManager;
	private List<SurveyEntry> surveys;

	public UserOutputBean() {
		super();
		LOGGER.info("constructor");
	}

	@PostConstruct
	public void init() {
		LOGGER.info("init");
		this.surveys = new ArrayList<SurveyEntry>();
	}

	public ClientManager getClientManager() {
		return this.clientManager;
	}

	public void setClientManager(final ClientManager clientManager) {
		this.clientManager = clientManager;
	}

	private Map<String, Object> getUser() {
		final Authentication authentication = SecurityContextHolder
				.getContext().getAuthentication();
		if (authentication != null) {
			final Map<String, Object> back = new HashMap<String, Object>();
			back.put("name", authentication.getName());
			final List<String> roles = new ArrayList<String>();
			for (final GrantedAuthority authority : authentication
					.getAuthorities()) {
				final String role = authority.getAuthority();
				if (role.equals("ROLE_USER"))
					continue;
				if (role.equals("ROLE_ANONYMOUS"))
					continue;
				roles.add(role);
			}
			back.put("roles", roles);
			return back;
		}
		return null;
	}

	public String getUserName() {
		final Map<String, Object> user = this.getUser();
		if (user != null)
			return (String) user.get("name");
		return null;
	}

	public void setUserName(final String userName) {
		// this.user = userName;
	}

	public String convertTime(final long time) {
		final Date date = new Date(time);
		final Format format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		return format.format(date);
	}

	public String convertMemoryUsage(final long mem) {
		final NumberFormat format = new DecimalFormat("#,##,###,##");
		return format.format(mem / 1024) + " MB";
	}

	public List<Entry<String, String>> convertExitPages(final String[][] data) {
		final Map<String, String> back = new LinkedHashMap<String, String>();
		if (data != null) {
			final int count = data.length;
			for (int a = 0; a < count; a++) {
				back.put(data[a][0], data[a][1]);
			}
		}
		return new ArrayList<Entry<String, String>>(back.entrySet());
	}

	private String toNumber(final String page) {
		String number = page.replaceAll("[^\\d]", "");
		if (number.equals("")) {
			if (page.contains("index"))
				number = "i";
			else if (page.contains("end"))
				number = "e";
		}
		return number;
	}

	public BarChartModel convertExitPagesAsGraph(final String[][] data) {
		final BarChartModel model = new BarChartModel();
		model.setLegendCols(4);
		model.setBarMargin(5);
		model.setBarPadding(5);
		model.setLegendPosition("n");
		model.setStacked(true);

		final ChartSeries pages = new ChartSeries();
		pages.setLabel("Exit Pages");

		if (data != null) {
			final int count = data.length;
			for (int a = 0; a < count; a++) {
				pages.set(this.toNumber(data[a][0]),
						Integer.parseInt(data[a][1]));
			}
		}

		model.addSeries(pages);

		return model;
	}

	// public void convertExitPagesAsSvg(OutputStream out, Object data) throws
	// IOException {
	//
	// if(data != null){
	// LOGGER.info("Type : {}",data.getClass());
	// if((String[][].class).isAssignableFrom(data.getClass())){
	// DefaultCategoryDataset dataset = new DefaultCategoryDataset();
	// final String[][] tmp = (String[][])data;
	// final int count = tmp.length;
	// for(int a=0;a<count;a++){
	// if((String[].class).isAssignableFrom(tmp[a].getClass())){
	// String[] item = (String[])tmp[a];
	// dataset.addValue(Integer.parseInt(item[1]), "Count", item[0]);
	// }
	//
	// }
	// JFreeChart chart = ChartFactory.createBarChart(
	// "", // chart title
	// "Exit Pages", // domain axis label
	// "Count", // range axis label
	// dataset, // data
	// PlotOrientation.VERTICAL, // orientation
	// false, // include legend
	// true, // tooltips
	// false // URLs
	// );
	// BufferedImage bufferedImage = chart.createBufferedImage(1300, 500);
	// ImageIO.write(bufferedImage, "gif", out);
	// }
	// }
	// // dataset.setValue("Item1", 10);
	// // dataset.setValue("Item2", 15);
	// // dataset.setValue("Item3", 8);
	// // dataset.setValue("Item4", 12.5);
	// // dataset.setValue("Item5", 30);
	// // JFreeChart chart = ChartFactory.createPieChart("Sales", dataset, true,
	// true, Locale.ENGLISH);
	// // BufferedImage bufferedImage = chart.createBufferedImage(300, 300);
	// // ImageIO.write(bufferedImage, "gif", out);
	// }

	// public List<Entry<String, String>> convertExitPagesAsSvg(final String[][]
	// data){
	// final Map<String, String> back = new LinkedHashMap<String, String>();
	// if(data != null){
	// final int count = data.length;
	// for(int a=0;a<count;a++){
	// back.put(data[a][0],data[a][1]);
	// }
	// }
	// return new ArrayList<Entry<String, String>>(back.entrySet());
	// }

	public List<SurveyEntry> getSurveys() {
		return this.surveys;
	}

	public void setSurveys(final List<String> surveys) {
		// this.surveys = surveys;
	}

	@Override
	public void processAction(final ActionEvent event)
			throws AbortProcessingException {
		LOGGER.info("triggered Update");
		final Map<String, Object> user = this.getUser();
		this.surveys.clear();
		if (user != null) {
			final List<String> roles = (List<String>) user.get("roles");
			final List<SurveyEntry> results = this.clientManager
					.getSurveys(roles);
			this.surveys.addAll(results);
		}

	}
}
