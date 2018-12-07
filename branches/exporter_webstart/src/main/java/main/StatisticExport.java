package main;

import java.io.File;
import java.io.IOException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.w3c.dom.NodeList;

import model.HeaderEntry;
import model.TransitionEntry;
import model.ValueEntry;
import presentation.statistics.format.stata.StataFormat.FORMATTYPE;
import service.counting.CountingService;
import service.data.DataService;
import service.statistics.StatisticService;
import service.statistics.StatisticService.TYPE;
import de.his.export.xml.export.ParticipantType;
import eu.dzhw.zofar.management.security.certificates.CertificateClient;
import eu.dzhw.zofar.management.utils.files.PackagerClient;
import eu.dzhw.zofar.management.utils.system.ConfigurationUtils;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

// TODO: Auto-generated Javadoc
/**
 * The Class StatisticExport.
 */
@Deprecated
public class StatisticExport {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws Throwable 
	 */
	public static void main(final String[] args) throws Throwable {
		final Properties system = ConfigurationUtils.getInstance().getConfiguration("System.properties");

		final Map<String, String> mapping = new HashMap<String, String>();
		mapping.put("true", "1");
		mapping.put("false", "0");
		mapping.put("DEFAULT", "-4");
		mapping.put("NOT ANSWERED", "-1");
		mapping.put("UNSELECTED", "-99");
		mapping.put("EMPTY", "-99");
		mapping.put("UNSET", "-99");
		mapping.put("NOT CALCULATED", "-99");
		
		//globals
		final NodeList usingNodes = XmlClient.getInstance().getByXPath(system.getProperty("qml.location"), "//*[@variable]");
		final Map<String, String> missings = DataService.getInstance().getMissings(system.getProperty("qml.location"),usingNodes);
		final Map<String, Map<String, String>> values = DataService.getInstance().getValues(system.getProperty("qml.location"),usingNodes);
		final Map<String, List<String>> variablePages = DataService.getInstance().retrievePages(system.getProperty("qml.location"),usingNodes);
		final Map<String, List<List<String>>> variableVisibles = DataService.getInstance().retrieveVisibles(system.getProperty("qml.location"),usingNodes);
		final Map<String, String> types = DataService.getInstance().getVariableTypes(system.getProperty("qml.location"),usingNodes);
		final Map<String, List<String>> pageTree = DataService.getInstance().pageTree(system.getProperty("qml.location"));
		final Map<String, Set<String>> questions = CountingService.getInstance().getQuestions(system.getProperty("qml.location"), new ArrayList<String>(),usingNodes);

		final StatisticService service = StatisticService.getInstance();
		final Map<TYPE, Object> data = service.create(DataService.getInstance().getConfiguration(),false,false,usingNodes,missings,values,variablePages,variableVisibles,types,pageTree,questions);

		System.out.println("Data composed");

		if (data != null) {

			final Map<String, Object> packageObj = new HashMap<String, Object>();
			final String dir = File.separator + "home" + File.separator + "hisuser" + File.separator + "ZofarExport" + File.separator;
			final String encryptCertFilePath = dir + "openssl" + File.separator + "export.pem";
			final String howtoFilePath = dir + File.separator + "Package" + File.separator + "Commands.txt";
			final String installerPath = dir + File.separator + "Package" + File.separator + "Installer";

			final Map<String, Object> howtoPackage = new HashMap<String, Object>();
			howtoPackage.put("Commands.txt", new File(howtoFilePath));
			final Map<String, Object> installerPackage = new HashMap<String, Object>();
			installerPackage.put("OpenSSL_win.exe", new File(installerPath + File.separator + "Win32OpenSSL_Light-1_0_1h.exe"));
			howtoPackage.put("Installer", installerPackage);
			packageObj.put("HOWTO", howtoPackage);

			final Map<String, Object> cerPackage = new HashMap<String, Object>();
			cerPackage.put("export.pem", new File(encryptCertFilePath));
			packageObj.put("Certificates", cerPackage);

			final Map<String, Object> docPackage = new HashMap<String, Object>();
			docPackage.put("doc.txt", "Documentation File");
			packageObj.put("Doc", docPackage);

			final Map<String, Object> codebookPackage = new HashMap<String, Object>();

			try {
				final Object odfCodebook = presentation.codebook.format.FormatFactory.getInstance().getFormat(presentation.codebook.format.FormatFactory.FORMAT.odf)
						.format((Map<HeaderEntry, Map<String, ValueEntry>>) data.get(TYPE.codebook), mapping);
				final Object txtCodebook = presentation.codebook.format.FormatFactory.getInstance().getFormat(presentation.codebook.format.FormatFactory.FORMAT.txt)
						.format((Map<HeaderEntry, Map<String, ValueEntry>>) data.get(TYPE.codebook), mapping);
				final Object xmlCodebook = presentation.codebook.format.FormatFactory.getInstance().getFormat(presentation.codebook.format.FormatFactory.FORMAT.xml)
						.format((Map<HeaderEntry, Map<String, ValueEntry>>) data.get(TYPE.codebook), mapping);
				codebookPackage.put("codebook.odf", odfCodebook);
				codebookPackage.put("codebook.txt", txtCodebook);
				codebookPackage.put("codebook.xml", xmlCodebook);

			} catch (final Exception e2) {
				e2.printStackTrace();
			}
			packageObj.put("codebook", codebookPackage);

			final Map<String, Object> navigationPackage = new HashMap<String, Object>();

			try {
				final Object txtTransitions = presentation.navigation.format.FormatFactory.getInstance().getFormat(presentation.navigation.format.FormatFactory.FORMAT.txt)
						.format((Map<String, Set<TransitionEntry>>) data.get(TYPE.transition));
				final Object xmlTransitions = presentation.navigation.format.FormatFactory.getInstance().getFormat(presentation.navigation.format.FormatFactory.FORMAT.xml)
						.format((Map<String, Set<TransitionEntry>>) data.get(TYPE.transition));
				navigationPackage.put("transitions.txt", txtTransitions);
				navigationPackage.put("transitions.xml", xmlTransitions);
			} catch (final Exception e2) {
				e2.printStackTrace();
			}
			packageObj.put("navigation", navigationPackage);

			final Map<String, Object> countingPackage = new HashMap<String, Object>();

//			try {
//				final Object odfCounting = presentation.counting.format.FormatFactory.getInstance().getFormat(presentation.counting.format.FormatFactory.FORMAT.odf)
//						.format((Map<HeaderEntry, Map<ValueEntry, Integer>>) data.get(TYPE.counting));
//				final Object txtCounting = presentation.counting.format.FormatFactory.getInstance().getFormat(presentation.counting.format.FormatFactory.FORMAT.txt)
//						.format((Map<HeaderEntry, Map<ValueEntry, Integer>>) data.get(TYPE.counting));
//				final Object xmlCounting = presentation.counting.format.FormatFactory.getInstance().getFormat(presentation.counting.format.FormatFactory.FORMAT.xml)
//						.format((Map<HeaderEntry, Map<ValueEntry, Integer>>) data.get(TYPE.counting));
//
//				countingPackage.put("counting.odf", odfCounting);
//				countingPackage.put("counting.txt", txtCounting);
//				countingPackage.put("counting.xml", xmlCounting);
//			} catch (final Exception e2) {
//				e2.printStackTrace();
//			}
			packageObj.put("counting", countingPackage);

			@SuppressWarnings("unchecked")
			final Map<String,Object> exportCsvData = (Map<String, Object>) presentation.data.format.FormatFactory.getInstance().getFormat(presentation.data.format.FormatFactory.FORMAT.csv)
					.format((Set<ParticipantType>) data.get(TYPE.data), ',', '\n', mapping, false,false);
			// Object exportXmlData = presentation.data.format.FormatFactory
			// .getInstance().getFormat(presentation.data.format.FormatFactory.FORMAT.xml).format((Set<ParticipantType>)
			// data.get(TYPE.data), ',', '\n', mapping,true);

			final Map<String, Object> csvPackage = new HashMap<String, Object>();
			final CertificateClient certTools = CertificateClient.getInstance();
			try {
				final Certificate cert = certTools.loadCertificate(new File(encryptCertFilePath));
				csvPackage.put("data.csv.encrypted", certTools.encryptString(exportCsvData.get("data") + "", File.createTempFile("data.csv.encrypted", ""), cert));
				csvPackage.put("data.csv", exportCsvData.get("data") + "");
				// csvPackage.put("data.xml.encrypted",certTools.encryptString(exportXmlData+"",File.createTempFile("data.xml.encrypted",""),
				// cert));
				// csvPackage.put("data.xml",exportXmlData+"");

			} catch (final Exception e2) {
				e2.printStackTrace();
			}
			packageObj.put("csv", csvPackage);

			final Map<String, Object> feedbackPackage = new HashMap<String, Object>();
			try {
				final Object txtExitPages = presentation.feedback.format.FormatFactory.getInstance().getFormat(presentation.feedback.format.FormatFactory.FORMAT.txt)
						.formatExits((Map<String, Integer>) data.get(TYPE.feedback));
				final Object txtCountsPages = presentation.feedback.format.FormatFactory.getInstance().getFormat(presentation.feedback.format.FormatFactory.FORMAT.txt)
						.formatCounts((Map<String, Integer>) data.get(TYPE.feedback));
				feedbackPackage.put("exitPages.txt", txtExitPages);
				feedbackPackage.put("feedback.txt", txtCountsPages);
			} catch (final Exception e2) {
				e2.printStackTrace();
			}
			packageObj.put("feedback", feedbackPackage);

			@SuppressWarnings("unchecked")
			final Map<FORMATTYPE,Object> exportStata = (Map<FORMATTYPE,Object>)presentation.statistics.format.FormatFactory.getInstance().getFormat(presentation.statistics.format.FormatFactory.FORMAT.stata)
					.format((Map<TYPE, Object>) data.get(TYPE.instruction), mapping,"surveyname","datasetName",false);

			final Map<String, Object> intructionPackage = new HashMap<String, Object>();

			final Map<String, Object> stataPackage = new HashMap<String, Object>();
			stataPackage.put("data.do", exportStata.get(FORMATTYPE.data));
			stataPackage.put("history.do", exportStata.get(FORMATTYPE.history));
			intructionPackage.put("Stata", stataPackage);

			final Map<String, Object> qmlPackage = new HashMap<String, Object>();
			qmlPackage.put("questionnaire.xml", new File(system.getProperty("qml.location")));
			intructionPackage.put("QML", qmlPackage);

			packageObj.put("import", intructionPackage);

			try {
				final File zip = new File(dir + "test.zip");
				PackagerClient.getInstance().packageZip(zip, packageObj);
			} catch (final IOException e) {
				e.printStackTrace();
			}

		}

	}

}
