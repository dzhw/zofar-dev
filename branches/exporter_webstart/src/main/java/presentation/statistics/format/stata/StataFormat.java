package presentation.statistics.format.stata;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.utils.string.ReplaceClient;
import model.ValueEntry;
import presentation.statistics.format.AbstractFormat;
import service.statistics.StatisticService.TYPE;
import utils.StringUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class StataFormat.
 */
public class StataFormat extends AbstractFormat {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LoggerFactory.getLogger(StataFormat.class);

	/** The instance. */
	private static StataFormat INSTANCE;

	public enum FORMATTYPE {
		data, history
	};

	/** The Constant BREAK. */
	private static final String BREAK = "\n";

	/** The type map. */
	private final Map<String, String> typeMap;

	/**
	 * The Enum OUTPUT.
	 */
	public enum OUTPUT {

		/** The instruction. */
		instruction,
		/** The data. */
		data
	}

	/**
	 * Instantiates a new stata format.
	 */
	private StataFormat() {
		super();
		this.typeMap = new HashMap<String, String>();
		this.typeMap.put("singleChoice", "int");
		this.typeMap.put("multipleChoice", "byte");
		this.typeMap.put("open", "str244");
		this.typeMap.put("preload", "str244");
	}

	/**
	 * Gets the single instance of StataFormat.
	 * 
	 * @return single instance of StataFormat
	 */
	public static StataFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new StataFormat();
		return INSTANCE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.statistics.format.AbstractFormat#format(java.util.Map)
	 */
	@Override
	public Object format(final Map<TYPE, Object> structure, final String surveyName, final String datasetName,
			final boolean limitLabels) {
		return this.format(structure, null, surveyName, datasetName, limitLabels);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see presentation.statistics.format.AbstractFormat#format(java.util.Map,
	 * java.util.Map)
	 */
	@Override
	public Object format(final Map<TYPE, Object> structure, final Map<String, String> mapping, final String surveyName,
			final String datasetName, final boolean limitLabels) {
		 if (structure == null)
		 return null;
		 if (structure.isEmpty())
		 return null;
		
		 final StringBuffer dataDoBuffer = new StringBuffer();
		 final StringBuffer historyDoBuffer = new StringBuffer();
		 if (structure != null) {
//				writeDataDoOLDSTRUCTURE(dataDoBuffer, structure, mapping, surveyName, datasetName,limitLabels);
				writeDataDo(dataDoBuffer, structure, mapping, surveyName, datasetName,limitLabels);
		 }
		 writeHistoryDo(historyDoBuffer, surveyName, datasetName);
		
		 final Map<FORMATTYPE, String> doBack = new HashMap<FORMATTYPE,
		 String>();
		 doBack.put(FORMATTYPE.data, dataDoBuffer.toString());
		 doBack.put(FORMATTYPE.history, historyDoBuffer.toString());
		
		 return doBack;
	}

//	public Object formatOLD(final Map<TYPE, Object> structure, final Map<String, String> mapping,
//			final boolean limitLabels) {
//		if (structure == null)
//			return null;
//		if (structure.isEmpty())
//			return null;
//
//		// @SuppressWarnings("unchecked")
//		// final Map<TYPE,Object> structure = (Map<TYPE,Object>)
//		// data.get(TYPE.instruction);
//		StringBuffer doBuffer = new StringBuffer();
//		if (structure != null) {
//			doBuffer.append("// Dieses Skript wurde erzeugt vom Zofar Online Survey System" + BREAK);
//			doBuffer.append("version 10" + BREAK);
//			doBuffer.append("#delimit ;" + BREAK);
//			doBuffer.append("clear;" + BREAK);
//			doBuffer.append(BREAK);
//
//			@SuppressWarnings("unchecked")
//			final Map<String, String> types = (Map<String, String>) structure.get(TYPE.types);
//
//			if (types != null) {
//				doBuffer.append("//Variablenmetadaten" + BREAK);
//				doBuffer.append("infile" + BREAK);
//				for (final Map.Entry<String, String> entry : types.entrySet()) {
//					doBuffer.append(this.typeMap.get(entry.getValue()) + " " + entry.getKey() + BREAK);
//				}
//				doBuffer.append("using data.csv;" + BREAK);
//			}
//
//			@SuppressWarnings("unchecked")
//			final Map<String, Map<String, ValueEntry>> options = (Map<String, Map<String, ValueEntry>>) structure
//					.get(TYPE.options);
//			if (options != null) {
//				doBuffer.append(BREAK);
//				doBuffer.append("//Labelsets" + BREAK);
//				for (final Map.Entry<String, Map<String, ValueEntry>> entry : options.entrySet()) {
//					final String variableName = entry.getKey();
//					final Map<String, ValueEntry> optionSet = entry.getValue();
//					if ((optionSet != null) && (!optionSet.isEmpty())) {
//						doBuffer.append("label define " + variableName + "_labelset" + BREAK);
//						for (final Map.Entry<String, ValueEntry> option : optionSet.entrySet()) {
//							final String label = StringUtils.getInstance().cleanedString(option.getValue().getLabel());
//							Object value = option.getValue().getValue();
//							if ((mapping != null) && (mapping.containsKey(value + "")))
//								value = mapping.get(value + "");
//							doBuffer.append(value + " \"" + label + "\"" + BREAK);
//						}
//						doBuffer.append(";" + BREAK);
//						doBuffer.append("label values " + variableName + " " + variableName + "_labelset;" + BREAK);
//						doBuffer.append(BREAK);
//					}
//				}
//			}
//		}
//
////		@SuppressWarnings("unchecked")
////		final Set<ParticipantType> dataSet = (Set<ParticipantType>) structure.get(TYPE.data);
////		StringBuffer csvBuffer = new StringBuffer();
////		if (dataSet != null) {
////			csvBuffer = writeDataDoHeader(csvBuffer);
////			csvBuffer.append(CsvFormat.getInstance().format(dataSet, ',', ' '));
////		}
//
//		Map<OUTPUT, String> back = new HashMap<OUTPUT, String>();
//		back.put(OUTPUT.instruction, doBuffer.toString());
//		// back.put(OUTPUT.data, csvBuffer.toString());
//
//		return back;
//	}

	/**
	 * Write do header.
	 * 
	 * @param buffer
	 *            the buffer
	 * @return the string buffer
	 */
	private StringBuffer writeDataDoHeader(final StringBuffer buffer, final Map<String, String> mapping,
			final String surveyName, final String datasetName) {

		final DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		buffer.append("****************************************************************************" + BREAK);
		buffer.append("// Dieses Skript wurde erzeugt vom Zofar Online Survey System" + BREAK);
		buffer.append("// Es dient lediglich als Beispiel und sollte ggf. den eigenen Bedürfnissen angepasst werden."
				+ BREAK);
		buffer.append("// Das Skript wurde für Stata 14 erstellt." + BREAK);
		buffer.append("// Eine Kompatibilität mit älteren Stata-Versionen kann nicht gewährleistet werden." + BREAK);
		buffer.append("****************************************************************************" + BREAK);
		buffer.append("** Projekt/ Studie: " + surveyName + "" + BREAK);
		buffer.append("** Erstelldatum: " + format.format(new Date()) + "" + BREAK);
		buffer.append("** Datensatz: " + datasetName + "" + BREAK);
		buffer.append("****************************************************************************" + BREAK);
		buffer.append("** Glossar Missing-Werte" + BREAK);
		// buffer.append("** " + mapping.get("DEFAULT") + " : Missing für nicht
		// definierte Variablen" + BREAK);
		// buffer.append("** " + mapping.get("UNSELECTED") + " : filterbedingtes
		// Missing - ausgeblendet (Seite gesehen, aber Item nicht)" + BREAK);
		// buffer.append("** " + mapping.get("NOT ANSWERED") + " : verweigert
		// (Item gesehen, aber nicht beantwortet)" + BREAK);
		// buffer.append("** " + mapping.get("NOTVISITED") + " : filterbedingtes
		// Missing - überfiltert (Seite nicht gesehen)" + BREAK);
		buffer.append("** " + mapping.get("DEFAULT")
				+ " : voreingestellte Missing-Werte, insbesondere bei technischen Variablen" + BREAK);
		buffer.append("** " + mapping.get("UNSELECTED")
				+ " : Item wurde gemäß Fragebogensteuerung nicht angezeigt oder befindet sich auf der Seite des Befragungsabbruches"
				+ BREAK);
		buffer.append("** " + mapping.get("NOT ANSWERED") + " : Item wurde gesehen, aber nicht beantwortet" + BREAK);
		buffer.append("** " + mapping.get("NOTVISITED")
				+ " : Seite, auf der sich das Item befindet, wurde gemäß Fragebogensteuerung oder aufgrund eines vorherigen Befragungsabbruches nicht besucht"
				+ BREAK);
		buffer.append("** " + mapping.get("INITFORGET1") + " : Variable wurde nicht erhoben ("
				+ mapping.get("UNSELECTED") + " oder " + mapping.get("NOTVISITED")
				+ "), jedoch für die Fragebogensteuerung verwendet" + BREAK);
		buffer.append("*************************************************************************" + BREAK);
		buffer.append("*************************************************************************" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("version 14\t\t\t// Festlegung der Stata-Version" + BREAK);
		buffer.append("set more off\t\t\t// Anzeige wird nicht unterbrochen" + BREAK);
		buffer.append("clear\t\t\t// löscht die Daten im Memory" + BREAK);

		buffer.append("" + BREAK);

		buffer.append("*____________Daten importieren____________________" + BREAK);
		buffer.append(
				"import delimited \"..\\..\\csv\\data.csv\", bindquote(strict) clear\t\t\t//Achtung! Pfad gilt nur für die aktuelle Ordnerstruktur!"
						+ BREAK);
		buffer.append("" + BREAK);
		return buffer;
	}

	private StringBuffer writeDataDo(final StringBuffer buffer, final Map<TYPE, Object> structure,
			final Map<String, String> mapping, final String surveyName, final String datasetName,
			final boolean limitLabels) {
		this.writeDataDoHeader(buffer, mapping, surveyName, datasetName);
		@SuppressWarnings("unchecked")
		final Map<String, Map<String, ValueEntry>> options = (Map<String, Map<String, ValueEntry>>) structure
				.get(TYPE.options);
		if (options != null) {
			buffer.append(BREAK);
			// doBuffer.append("//Labelsets" + BREAK);
			buffer.append("*____________Wertelabels festlegen & zuweisen____________________" + BREAK);

			for (final Map.Entry<String, Map<String, ValueEntry>> entry : options.entrySet()) {
				final String variableName = entry.getKey().toLowerCase();
				final Map<String, ValueEntry> optionSet = entry.getValue();
				if ((optionSet != null) && (!optionSet.isEmpty())) {
					buffer.append("label define " + variableName + "_labelset" + " ");
					for (final Map.Entry<String, ValueEntry> option : optionSet.entrySet()) {
						String label = ReplaceClient.getInstance().cleanedString(option.getValue().getLabel());
						if (limitLabels) {
							label = label.substring(0, Math.min(243, label.length()));
						}
						Object value = option.getValue().getValue();
						if ((mapping != null) && (mapping.containsKey(value + "")))
							value = mapping.get(value + "");
						buffer.append(value + " \"" + ("[" + value + "] " + label).trim() + "\"" + " ");
					}
					buffer.append("" + BREAK);
					buffer.append("label values " + variableName + " " + variableName + "_labelset" + BREAK);
					buffer.append(BREAK);
				}
			}
		}

		buffer.append("*____________Variablenlabels importieren____________________" + BREAK);
		@SuppressWarnings("unchecked")
		final Map<String, Map<String, Object>> variableLabels = (Map<String, Map<String, Object>>) structure
				.get(TYPE.variables);
		if (variableLabels != null) {
			for (final Map.Entry<String, Map<String, Object>> entry : variableLabels.entrySet()) {
				final String label = entry.getValue().get("header") + "";
				final String labelTeaser = label.substring(0, Math.min(label.length(), 79));
				buffer.append("label var " + entry.getKey().toLowerCase() + " \"" + labelTeaser + "\"" + BREAK);
			}
		}
		buffer.append(BREAK + "*_____________Arbeitsdatensatz importieren____________________" + BREAK);
		// doBuffer.append("saveold \"${workdir}arbeitsdaten.dta\",
		// replace"+BREAK);
		buffer.append("saveold \"..\\..\\csv\\arbeitsdaten.dta\", replace" + BREAK);
		return buffer;
	}
	
	private StringBuffer writeDataDoOLDSTRUCTURE(final StringBuffer buffer, final Map<TYPE, Object> structure,
			final Map<String, String> mapping, final String surveyName, final String datasetName,
			final boolean limitLabels) {
//		this.writeDataDoHeader(buffer, mapping, surveyName, datasetName);
		
		buffer.append("// Dieses Skript wurde erzeugt vom Zofar Online Survey System" + BREAK);
		buffer.append("version 10" + BREAK);
		buffer.append("#delimit ;" + BREAK);
		buffer.append("clear;" + BREAK);
		buffer.append(BREAK);
		
		@SuppressWarnings("unchecked")
		final Map<String, String> types = (Map<String, String>) structure.get(TYPE.types);

		if (types != null) {
			buffer.append("//Variablenmetadaten" + BREAK);
			buffer.append("infile" + BREAK);
			for (final Map.Entry<String, String> entry : types.entrySet()) {
				buffer.append(this.typeMap.get(entry.getValue()) + " " + entry.getKey() + BREAK);
			}
			buffer.append("using data.csv;" + BREAK);
		}
		
		@SuppressWarnings("unchecked")
		final Map<String, Map<String, ValueEntry>> options = (Map<String, Map<String, ValueEntry>>) structure
				.get(TYPE.options);
		if (options != null) {
			buffer.append(BREAK);
			buffer.append("//Labelsets" + BREAK);
			for (final Map.Entry<String, Map<String, ValueEntry>> entry : options.entrySet()) {
				final String variableName = entry.getKey();
				final Map<String, ValueEntry> optionSet = entry.getValue();
				if ((optionSet != null) && (!optionSet.isEmpty())) {
					buffer.append("label define " + variableName + "_labelset" + BREAK);
					for (final Map.Entry<String, ValueEntry> option : optionSet.entrySet()) {
						final String label = StringUtils.getInstance().cleanedString(option.getValue().getLabel());
						Object value = option.getValue().getValue();
						if ((mapping != null) && (mapping.containsKey(value + "")))
							value = mapping.get(value + "");
						buffer.append(value + " \"" + label + "\"" + BREAK);
					}
					buffer.append(";" + BREAK);
					buffer.append("label values " + variableName + " " + variableName + "_labelset;" + BREAK);
					buffer.append(BREAK);
				}
			}
		}
		return buffer;
	}

	/**
	 * Write do header.
	 * 
	 * @param buffer
	 *            the buffer
	 * @return the string buffer
	 */
	private StringBuffer writeHistoryDoHeader(final StringBuffer buffer, final String surveyName,
			final String datasetName) {
		final DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		buffer.append("****************************************************************************" + BREAK);
		buffer.append("// Dieses Skript wurde erzeugt vom Zofar Online Survey System" + BREAK);
		buffer.append("// Es dient lediglich als Beispiel und sollte ggf. den eigenen Bedürfnissen angepasst werden."
				+ BREAK);
		buffer.append("// Das Skript wurde für Stata 14 erstellt." + BREAK);
		buffer.append("// Eine Kompatibilität mit älteren Stata-Versionen kann nicht gewährleistet werden." + BREAK);
		buffer.append("****************************************************************************" + BREAK);
		buffer.append("** Projekt/ Studie: " + surveyName + "" + BREAK);
		buffer.append("** Erstellung: automatische Erstellung durch Zofar" + BREAK);
		buffer.append("** Erstelldatum: " + format.format(new Date()) + "" + BREAK);
		buffer.append("** Rohdaten: history.csv " + BREAK);
		buffer.append("** Datensatz-Ergebnis: " + BREAK);
		buffer.append("************** 1: history-arbeitsdaten.dta" + BREAK);
		buffer.append("************** 2: history-wide.dta" + BREAK);
		buffer.append("************** 3: history-wide-collapse.dta" + BREAK);
		buffer.append("** Log File:   history-aufbereitung.smcl" + BREAK);
		buffer.append("*************************************************************************" + BREAK);
		buffer.append("*************************************************************************" + BREAK);

		buffer.append("" + BREAK);
		return buffer;
	}

	private StringBuffer writeHistoryDo(final StringBuffer buffer, final String surveyName, final String datasetName) {
		writeHistoryDoHeader(buffer, surveyName, datasetName);
		buffer.append("version 14\t\t\t\t\t\t// Festlegung der Stata-Version" + BREAK);
		buffer.append("set more off\t\t\t\t\t// Anzeige wird nicht unterbrochen" + BREAK);
		buffer.append("clear\t\t\t\t\t\t\t// löscht die Daten im Memory" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("log using history-datenaufbereitung.smcl, replace\t// Log-file erstellen" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*________Rohdaten importieren___________________" + BREAK);
		buffer.append(
				"import delimited \"..\\..\\csv\\history.csv\", bindquote(strict) clear\t\t\t//Achtung! Pfad gilt nur für die aktuelle Ordnerstruktur und muss evtl. angepasst werden!"
						+ BREAK);
		buffer.append("" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*_________Zeitstempel in numerische Variable umwandeln_______" + BREAK);
		buffer.append("gen double seiteneing=clock(timestamp, \"YMDhms\", 2020)" + BREAK);
		buffer.append("format seiteneing %-tc" + BREAK);
		buffer.append("label var seiteneing \"Zeitstempel für Seiteneingang\"" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*________Berechnung der Verweildauer pro Seite (in Millisekunden)____" + BREAK);
		buffer.append("//Achtung: automatischer SessionTimeout i.d.R. nach einer halben Stunde" + BREAK);
		buffer.append("sort participant_id id, stable" + BREAK);
		buffer.append("gen verwdauer = seiteneing[_n+1]-seiteneing if participant_id==participant_id[_n+1]" + BREAK);
		buffer.append("label var verwdauer \"Verweildauer pro Seite in Millisekunden\"" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*__________Fragebogenseiten nummerieren___________" + BREAK);
		buffer.append("// Alle Seiten mit nichtnumerischer Bezeichnung (zusätzlich zu \"index\" und \"end\")" + BREAK);
		buffer.append("// müssen manuell nachkodiert werden (siehe replace-command)" + BREAK);
		buffer.append("destring page, gen(seitennr) ignore(\"page\") force" + BREAK);
		buffer.append("replace seitennr= 0 if page==\"index\"" + BREAK);
		buffer.append("replace seitennr= 1000 if page==\"end\"" + BREAK);
		buffer.append("label var seitennr \"Fragebogenseite\"" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*__________Seiten der Befragten nummerieren______" + BREAK);
		buffer.append("sort participant_id id, stable" + BREAK);
		buffer.append("by participant_id: gen seitenaufr_nr=_n" + BREAK);
		buffer.append("label var seitenaufr_nr \"Nummer des Seitenaufrufes im Verlauf der Befragung\"" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*_______überflüssige Variablen löschen_________" + BREAK);
		buffer.append("drop id timestamp" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*__________Arbeitsdatensatz speichern_____________" + BREAK);
		buffer.append("save \"..\\..\\csv\\history-arbeitsdaten.dta\", replace" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*************************************************************************" + BREAK);
		buffer.append("*************************************************************************" + BREAK);
		buffer.append("************ Datensatz umwandeln in ein breites Format ******************" + BREAK);
		buffer.append("// Datensatz zeigt individuelle Verläufe der Personen durch den Fragebogen" + BREAK);
		buffer.append("use \"..\\..\\csv\\history-arbeitsdaten.dta\", clear" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*________überflüssige Variablen löschen___________" + BREAK);
		buffer.append("drop seitennr" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*___________reshape ohne collapse_________________" + BREAK);
		buffer.append("reshape wide seiteneing verwdauer page, i(participant_id token) j(seitenaufr_nr)" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*________Datensatz speichern___________" + BREAK);
		buffer.append("save \"..\\..\\csv\\history-wide.dta\", replace" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*************************************************************************" + BREAK);
		buffer.append("*************************************************************************" + BREAK);
		buffer.append("****** Datensatz umwandeln in ein breites Format mit aggregierten Daten **" + BREAK);
		buffer.append("// Datensatz zeigt Verbleibdauer auf jeder Seite " + BREAK);
		buffer.append("// Variablen enthalten Informationen über den Verbleib auf den Seiten" + BREAK);
		buffer.append("// !Achtung: bei mehrmaligem Laden der Seite wird der Verbleib summiert!" + BREAK);
		buffer.append("// Variablennummern entsprechen Seitennummern, mit folgenden Ausnahmen: " + BREAK);
		buffer.append("// \t\t\t\t0: \t\tindex" + BREAK);
		buffer.append("//\t\t\t\t1000:\tend" + BREAK);
		buffer.append("//\t\t\t\t..." + BREAK);
		buffer.append("" + BREAK);
		buffer.append("use \"..\\..\\csv\\history-arbeitsdaten.dta\", clear" + BREAK);
		// buffer.append("" + BREAK);
		// buffer.append("*_________Kontrollvariable erstellen__________" +
		// BREAK);
		// buffer.append("// Variable gibt die Anzahl der Seitenaufrufe pro
		// Person und Seite an" + BREAK);
		// buffer.append("sort participant_id seitennr, stable" + BREAK);
		// buffer.append("bysort participant_id seitennr: egen
		// anzseitenaufr=count(seitennr)" + BREAK);
		// buffer.append("list participant_id token seiteneing seite
		// anzseitenaufr if anzseitenaufr>1" + BREAK);
		// buffer.append("label var anzseitenaufr \"Anzahl der Seitenaufrufe pro
		// Seite\"" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*________Datensatz aggregieren____________________" + BREAK);
		buffer.append("// Aufsummieren der Verweildauer bei mehrmaligem Besuch der Seite" + BREAK);
		buffer.append(
				"collapse (sum) verwdauer (first) token seitennr seiteneing anzseitenaufr (last) seitenaufr_nr, by(participant_id page)"
						+ BREAK);
		buffer.append("" + BREAK);
		buffer.append("*________überflüssige Variablen löschen___________" + BREAK);
		// buffer.append("drop seitenaufr_nr page seiteneing anzseitenaufr" +
		// BREAK);
		buffer.append("drop seitenaufr_nr page seiteneing" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("rename verwdauer verw_page" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("reshape wide verw_page, i(participant_id token) j(seitennr)" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("*________Datensatz speichern___________" + BREAK);
		buffer.append("save \"..\\..\\csv\\history-wide-collapse.dta\", replace" + BREAK);
		buffer.append("" + BREAK);
		buffer.append("log close" + BREAK);
		buffer.append("" + BREAK);
		return buffer;
	}
}
