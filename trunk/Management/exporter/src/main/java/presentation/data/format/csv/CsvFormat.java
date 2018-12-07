package presentation.data.format.csv;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import presentation.data.format.AbstractFormat;
import de.his.export.xml.export.DataType;
import de.his.export.xml.export.ParticipantType;

public class CsvFormat extends AbstractFormat {

	private static CsvFormat INSTANCE;

	private CsvFormat() {
		super();
	}

	public static CsvFormat getInstance() {
		if (INSTANCE == null)
			INSTANCE = new CsvFormat();
		return INSTANCE;
	}

	@Override
	public Object format(Set<ParticipantType> data) {
		return format(data, ';', '\n');
	}

	@Override
	public Object format(Set<ParticipantType> data, final char fieldSeparator,
			final char rowSeparator) {
		return format(data, fieldSeparator, rowSeparator, null);
	}

	@Override
	public Object format(Set<ParticipantType> data, final char fieldSeparator,
			final char rowSeparator, final Map<String, String> mapping) {
		return format(data,fieldSeparator,rowSeparator,mapping,false);
	}
	
	@Override
	public Object format(Set<ParticipantType> data, final char fieldSeparator,
			final char rowSeparator, final Map<String, String> mapping,final boolean ignoreFirst) {
		final Iterator<ParticipantType> rowIt = data.iterator();
		StringBuffer back = new StringBuffer();
		List<String> columns = null;
		while (rowIt.hasNext()) {
			final ParticipantType entry = rowIt.next();
			// convert data to map
			final Map<String, String> answerMap = new LinkedHashMap<String, String>();
			answerMap.put("id", entry.getId());
			for (final DataType inputDataEntry : entry.getDataset()
					.getDataArray()) {
				String value = inputDataEntry.getStringValue();
				final String type = inputDataEntry.getType();
				if(type.equals("string")){
					value = value.substring(0, Math.min(243, value.length()));
					value = stripBreaks("\""+value+"\"",fieldSeparator,rowSeparator);
				}
				
				answerMap.put(inputDataEntry.getVariable(),
						value);
			}

			if ((columns == null)) {
				columns = new ArrayList<String>();
				final Iterator<String> colIt = answerMap.keySet().iterator();
				while (colIt.hasNext()) {
					final String col = colIt.next();
					columns.add(col);
					if(!ignoreFirst){
					back.append("\"" + col + "\"");
					if (colIt.hasNext())
						back.append(fieldSeparator);
					}
				}
				if(!ignoreFirst)back.append(rowSeparator);
			}

			final Iterator<String> colIt = columns.iterator();
			while (colIt.hasNext()) {
				final String col = colIt.next();
				Object value = answerMap.get(col);
				if ((mapping != null) && (mapping.containsKey(value + "")))
					value = mapping.get(value + "");
				back.append(value);
				if (colIt.hasNext())
					back.append(fieldSeparator);
			}
			back.append(rowSeparator);
		}
		return back.toString();
	}

	private String stripBreaks(final String text,final char fieldSeparator,final char rowSeparator) {
		String back = text.replaceAll("\n", " ");
		back = back.replaceAll("\r", "");
		back = back.replaceAll("\t", " ");
		back = back.replaceAll(fieldSeparator+"", "#");
		back = back.replaceAll(rowSeparator+"", "#");
		back = back.replaceAll(" {2,}", " ");
		return back;
	}

	// @Override
	// public Object format(Set<ParticipantType> data,final char
	// fieldSeparator,final char rowSeparator) {
	// final Iterator<Map<String, Object>> rowIt = data.iterator();
	// StringBuffer back = new StringBuffer();
	// List<String> columns = null;
	// while (rowIt.hasNext()) {
	// final Map<String, Object> row = rowIt.next();
	// if(columns == null){
	// columns = new ArrayList<String>();
	// final Iterator<String> colIt = row.keySet().iterator();
	// while (colIt.hasNext()) {
	// final String col = colIt.next();
	// columns.add(col);
	// back.append(col);
	// if(colIt.hasNext())back.append(fieldSeparator);
	// }
	// back.append(rowSeparator);
	// }
	// final Iterator<String> colIt = columns.iterator();
	//
	// while (colIt.hasNext()) {
	// final String col = colIt.next();
	// final Object value = row.get(col);
	// back.append(value);
	// if(colIt.hasNext())back.append(fieldSeparator);
	// }
	// back.append(rowSeparator);
	// }
	// return back.toString();
	// }

}
