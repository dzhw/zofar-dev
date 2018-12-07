package eu.dzhw.zofar.management.utils.files;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSVClient {
	/** The Constant INSTANCE. */
	private static final CSVClient INSTANCE = new CSVClient();

	/** The Constant LOGGER. */
	final static Logger LOGGER = LoggerFactory.getLogger(CSVClient.class);

	/**
	 * Instantiates a new cvs client.
	 */
	private CSVClient() {
		super();
	}

	/**
	 * Gets the single instance of CVSClient.
	 * 
	 * @return single instance of CVSClient
	 */
	public static synchronized CSVClient getInstance() {
		return INSTANCE;
	}

	public ArrayList<String> getCSVHeaders(final File cvs) throws IOException {
		return getCSVHeaders(cvs, ';', '\'');
	}

	public ArrayList<String> getCSVHeaders(final File cvs, final Character delimiter, final Character quote) throws IOException {
		final Reader in = new FileReader(cvs);
		final CSVFormat format = CSVFormat.EXCEL.withDelimiter(delimiter).withQuote(quote);
		final CSVParser parser = new CSVParser(in, format);
		final List<CSVRecord> list = parser.getRecords();
		ArrayList<String> headers = null;
		for (final CSVRecord record : list) {
			Iterator<String> it = record.iterator();
			headers = new ArrayList<String>();
			while (it.hasNext()) {
				headers.add(it.next());
			}
			break;
		}
		parser.close();
		return headers;
	}

	public List<Map<String, String>> loadCSV(final File cvs, List<String> headers, final boolean skipFirst) throws IOException {
		return this.loadCSV(cvs, headers, skipFirst, ';', '\'');
	}

	public List<Map<String, String>> loadCSV(final File cvs, List<String> headers, final boolean skipFirst, final Character delimiter, final Character quote) throws IOException {
		final List<Map<String, String>> back = new ArrayList<Map<String, String>>();
		final Reader in = new FileReader(cvs);
		final CSVFormat format = CSVFormat.EXCEL.withDelimiter(delimiter).withQuote(quote);

		final CSVParser parser = new CSVParser(in, format);
		final List<CSVRecord> list = parser.getRecords();
		boolean first = true;
		boolean skip = false;
		for (final CSVRecord record : list) {

			if (first && skipFirst)
				skip = true;
			first = false;
			if (skip) {
				skip = false;
				continue;
			}
			final Map<String, String> recordMap = new LinkedHashMap<String, String>();

			final Iterator<String> headerIt = headers.iterator();
			int index = 0;
			while (headerIt.hasNext()) {
				final String col = headerIt.next();

				String value = "";
				if (index < record.size())
					value = record.get(index);

				recordMap.put(col, value);
				index++;
			}

			back.add(recordMap);
		}
		parser.close();
		return back;
	}

	public void saveCSV(final File cvs, final List<String> headers, final List<Map<String, String>> data) throws Exception {
		this.saveCSV(cvs, headers, data, ';', '\'');
	}

	public void saveCSV(final File csv, final List<String> headers, final List<Map<String, String>> data, final char delimiter, final char quote) throws Exception {
		FileClient.getInstance().writeToFile(csv.getAbsolutePath(), "", false);
		final CSVFormat format = CSVFormat.EXCEL.withDelimiter(delimiter).withQuote(quote).withQuoteMode(QuoteMode.ALL).withRecordSeparator('\n').withIgnoreEmptyLines(false);
		FileWriter fileWriter = new FileWriter(csv);
		final CSVPrinter csvFilePrinter = new CSVPrinter(fileWriter, format);
		try {
			csvFilePrinter.printRecord(headers);
			for (final Map<String, String> row : data) {
				final List<String> rowData = new ArrayList<String>();
				for (final String header : headers) {
					rowData.add(row.get(header));
				}
				csvFilePrinter.printRecord(rowData);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvFilePrinter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
				e.printStackTrace();
			}
		}
	}
	
	public String saveCSVAsString(final List<String> headers, final List<Map<String, String>> data, final char delimiter, final char quote) throws Exception {
		final CSVFormat format = CSVFormat.EXCEL.withDelimiter(delimiter).withQuote(quote).withQuoteMode(QuoteMode.ALL).withRecordSeparator('\n').withIgnoreEmptyLines(false);
		StringWriter stringWriter = new StringWriter();
		final CSVPrinter csvPrinter = new CSVPrinter(stringWriter, format);
		try {
			csvPrinter.printRecord(headers);
			for (final Map<String, String> row : data) {
				final List<String> rowData = new ArrayList<String>();
				for (final String header : headers) {
					rowData.add(row.get(header));
				}
				csvPrinter.printRecord(rowData);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				stringWriter.flush();
				stringWriter.close();
				csvPrinter.close();
			} catch (IOException e) {
				System.out.println("Error while flushing/closing fileWriter/csvPrinter !!!");
				e.printStackTrace();
			}
		}
		return stringWriter.toString();
	}

}
