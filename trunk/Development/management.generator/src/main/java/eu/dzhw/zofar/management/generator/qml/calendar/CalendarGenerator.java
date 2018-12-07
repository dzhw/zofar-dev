package eu.dzhw.zofar.management.generator.qml.calendar;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CalendarGenerator {

	private static final CalendarGenerator INSTANCE = new CalendarGenerator();

	private static final Logger LOGGER = LoggerFactory
			.getLogger(CalendarGenerator.class);

	private CalendarGenerator() {
		super();
	}

	public static CalendarGenerator getInstance() {
		return INSTANCE;
	}

	public void generate(final String prefix, final String[] colors,
			final String[] slots, final String[] columns, final String[] rows)
			throws Exception {
		final int rowCount = rows.length;
		final int columnCount = columns.length;
		final int slotCount = slots.length;

		int lft = 1;
		final StringBuffer calendarBuffer = new StringBuffer();
		final StringBuffer variableBuffer = new StringBuffer();
		
		calendarBuffer.append("<calendar:calendar legendLayout=\"lineDirection\" behaviour=\"drag\" labelAll=\"false\"\n");
		calendarBuffer.append("rows=\"");
		
		calendarBuffer.append("\n");
//		System.out.print("<calendar:calendar legendLayout=\"lineDirection\" behaviour=\"drag\" labelAll=\"false\"");

//		System.out.print(" rows=\"");
		for (int a = 0; a < rowCount; a++) {
			calendarBuffer.append(rows[a]);
//			System.out.print(rows[a]);
			if (a < rowCount - 1){
				calendarBuffer.append(",");
//				System.out.print(",");
			}
				
		}
//		System.out.print("\" ");
		calendarBuffer.append("\" ");

//		System.out.print(" columns=\"");
		calendarBuffer.append(" columns=\"");
		
		for (int b = 0; b < columnCount; b++) {
//			System.out.print(columns[b]);
			calendarBuffer.append(columns[b]);
			if (b < columnCount - 1){
//				System.out.print(",");
				calendarBuffer.append(",");
			}
				
		}
//		System.out.print("\" ");
		calendarBuffer.append("\" ");

		for (int c = 1; c <= slotCount; c++) {
//			System.out.print("slot" + c + "Label=\""
//					+ slots[((c - 1) % slots.length)] + "\" slot" + c
//					+ "Color=\"" + colors[((c - 1) % colors.length)] + "\"");
			calendarBuffer.append("slot" + c + "Label=\""
					+ slots[((c - 1) % slots.length)] + "\" slot" + c
					+ "Color=\"" + colors[((c - 1) % colors.length)] + "\"");
			if (c < slotCount){
//				System.out.print(" ");
				calendarBuffer.append(" ");
			}
				
		}

//		System.out.println(">");
		calendarBuffer.append(">\n");
		// Items
		for (int a = 1; a <= rowCount; a++) {
			for (int b = 1; b <= columnCount; b++) {
//				System.out.print("<calendar:calendarItem label=\"" + lft
//						+ "\" rendered=\"#{true}\" ");
				calendarBuffer.append("<calendar:calendarItem label=\"" + lft
						+ "\" rendered=\"#{true}\" ");
				// System.out.print("<calendar:calendarItem rendered=\"#{true}\"");
				for (int c = 1; c <= slotCount; c++) {
					final String varname = prefix + "X" + a + "X" + b + "X" + c;
					variableBuffer.append("<zofar:variable name=\""+varname+"\" type=\"boolean\" />\n");
//					System.out.print("slot" + c + "=\"#{" + varname + "}\"");
					calendarBuffer.append("slot" + c + "=\"#{" + varname + "}\"");
					if (c < slotCount){
//						System.out.print(" ");
						calendarBuffer.append(" ");
					}
						
				}
//				System.out.println("/>");
				calendarBuffer.append("/>\n");
				lft++;
			}
		}
//		System.out.println("</calendar:calendar>");
		calendarBuffer.append("</calendar:calendar>\n");
		
		System.out.println("Variables:\n");
		System.out.println(variableBuffer.toString());
		System.out.println("Calendar:\n");
		System.out.println(calendarBuffer.toString());
	}

	public void generateQML(final String uid, final String[] prefixes,
			final String[] colors, final String[] pattern,
			final String[] slots, final String[] columns, final String[] rows)
			throws Exception {
		final int rowCount = rows.length;
		final int columnCount = columns.length;
		final int slotCount = slots.length;
		final int prefixesCount = prefixes.length;
		
		int[] counters = new int[prefixesCount];
		for(int a=0;a<prefixesCount;a++)counters[a] = 1;
		
		if(prefixesCount < slotCount) throw new Exception("less prefixes than slots");

//		int lft = 1;
		final StringBuffer calendarBuffer = new StringBuffer();
		final StringBuffer variableBuffer = new StringBuffer();
		// System.out.print("<calendar:calendar legendLayout=\"lineDirection\" behaviour=\"drag\" labelAll=\"false\"");
//		System.out
//				.print("<zofar:calendar uid=\""
//						+ uid
//						+ "\" behaviour=\"move\" legendLayout=\"pageDirection\" legendPosition=\"left\" showIndicator=\"false\" showLegendIcon=\"true\"");
		calendarBuffer.append("<zofar:calendar uid=\""
				+ uid
				+ "\" behaviour=\"move\" legendLayout=\"pageDirection\" legendPosition=\"left\" showIndicator=\"false\" showLegendIcon=\"true\"");
//		System.out.print(" rows=\"");
		calendarBuffer.append(" rows=\"");
		for (int a = 0; a < rowCount; a++) {
//			System.out.print(rows[a]);
			calendarBuffer.append(rows[a]);
			if (a < rowCount - 1){
//				System.out.print(",");
				calendarBuffer.append(",");
			}
		}
//		System.out.print("\" ");
		calendarBuffer.append("\" ");

//		System.out.print(" columns=\"");
		calendarBuffer.append(" columns=\"");
		for (int b = 0; b < columnCount; b++) {
//			System.out.print(columns[b]);
			calendarBuffer.append(columns[b]);
			if (b < columnCount - 1){
//				System.out.print(",");
				calendarBuffer.append(",");
			}
		}
//		System.out.print("\" ");
		calendarBuffer.append("\" ");
//		System.out.println(">");
		calendarBuffer.append(">\n");

//		System.out.println("<zofar:configuration>");
		calendarBuffer.append("<zofar:configuration>\n");
		for (int c = 1; c <= slotCount; c++) {
			// System.out.print("slot"+c+"Label=\""+slots[((c-1) %
			// slots.length)]+"\" slot"+c+"Color=\""+colors[((c-1) %
			// colors.length)]+"\"");
			String tmpPattern = ""; 
			if((pattern != null)&&(pattern.length > ((c-1) % pattern.length))) tmpPattern = "pattern=\""+pattern[((c-1) % pattern.length)]+"\" ";
			String tmpColor = ""; 
			if((colors != null)&&(colors.length > ((c-1) % colors.length))) tmpColor = "color=\""+colors[((c-1) % colors.length)]+"\" ";

			
//			System.out.println("<zofar:SlotConfiguration slot=\"Slot"+c+ "\" label=\""+slots[((c-1) % slots.length)]+"\""+tmpColor+""+tmpPattern+"/>");
			calendarBuffer.append("<zofar:SlotConfiguration slot=\"Slot"+c+ "\" label=\""+slots[((c-1) % slots.length)]+"\""+tmpColor+""+tmpPattern+"/>\n");
		}
//		System.out.println("</zofar:configuration>");
		calendarBuffer.append("</zofar:configuration>\n");

		// Items
		for (int a = 1; a <= rowCount; a++) {
			for (int b = 1; b <= columnCount; b++) {
//				System.out.println("<zofar:item visible=\"true\">");
				calendarBuffer.append("<zofar:item visible=\"true\">\n");

				for (int c = 1; c <= slotCount; c++) {
					final int currentLft = counters[c-1]; 
					String currentStr = currentLft+"";
					if(currentLft < 10) currentStr = "0"+currentStr;
					counters[c-1] = counters[c-1] + 1;
					
					final String prefix = prefixes[c-1];
					
//					final String varname = prefix + "X" + a + "X" + b + "X" + c;
					final String varname = prefix.replaceAll(Pattern.quote("*"), currentStr+"");
					
					variableBuffer.append("<zofar:variable name=\""+varname+"\" type=\"boolean\" />\n");
//					System.out.println("<zofar:SlotItem variable=\""+varname+"\" slot=\"Slot" + c + "\" />");
					calendarBuffer.append("<zofar:SlotItem variable=\""+varname+"\" slot=\"Slot" + c + "\" />\n");
				}
//				System.out.println("</zofar:item>");
				calendarBuffer.append("</zofar:item>\n");
//				lft++;
			}
		}
//		System.out.println("</zofar:calendar>");
		calendarBuffer.append("</zofar:calendar>\n");
		System.out.println("Variables:\n");
		System.out.println(variableBuffer.toString());
		System.out.println("Calendar:\n");
		System.out.println(calendarBuffer.toString());
	}

}
