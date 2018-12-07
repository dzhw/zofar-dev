package eu.dzhw.zofar.management.generator.qml.calendar.main;

import eu.dzhw.zofar.management.generator.qml.calendar.CalendarGenerator;

public class CalendarTest {

	public static void main(String[] args) {
		final CalendarGenerator calendarGenerator = CalendarGenerator.getInstance();
		final String prefix = "mo";
		

		
//		final String[] colors = new String[20];
//		colors[0] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[0]";
//		colors[1] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[1]";
//		colors[2] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[2]";
//		colors[3] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[3]";
//		colors[4] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[4]";
//		colors[5] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[5]";
//		colors[6] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[6]";
//		colors[7] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[7]";
//		colors[8] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[8]";
//		colors[9] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[9]";
//		colors[10] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[10]";
//		colors[11] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[11]";
//		colors[12] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[12]";
//		colors[13] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[13]";
//		colors[14] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[14]";
//		colors[15] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[15]";
//		colors[16] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[16]";
//		colors[17] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[17]";
//		colors[18] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[18]";
//		colors[19] = "zofar.explode(zofar.explode(color1.value,';')[0],',')[19]";
		
//		final String[] pattern = new String[20];
//		pattern[0] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[0]";
//		pattern[1] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[1]";
//		pattern[2] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[2]";
//		pattern[3] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[3]";
//		pattern[4] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[4]";
//		pattern[5] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[5]";
//		pattern[6] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[6]";
//		pattern[7] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[7]";
//		pattern[8] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[8]";
//		pattern[9] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[9]";
//		pattern[10] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[10]";
//		pattern[11] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[11]";
//		pattern[12] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[12]";
//		pattern[13] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[13]";
//		pattern[14] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[14]";
//		pattern[15] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[15]";
//		pattern[16] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[16]";
//		pattern[17] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[17]";
//		pattern[18] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[18]";
//		pattern[19] = "zofar.explode(zofar.explode(color1.value,';')[1],',')[19]";
//		
		final String[] slots = new String[20];
		slots[0] = "Aktivität 1";
		slots[1] = "Aktivität 2";
		slots[2] = "Aktivität 3";
		slots[3] = "Aktivität 4";
		slots[4] = "Aktivität 5";
		slots[5] = "Aktivität 6";
		slots[6] = "Aktivität 7";
		slots[7] = "Aktivität 8";
		slots[8] = "Aktivität 9";
		slots[9] = "Aktivität 10";
		slots[10] = "Aktivität 11";
		slots[11] = "Aktivität 12";
		slots[12] = "Aktivität 13";
		slots[13] = "Aktivität 14";
		slots[14] = "Aktivität 15";
		slots[15] = "Aktivität 16";
		slots[16] = "Aktivität 17";
		slots[17] = "Aktivität 18";
		slots[18] = "Aktivität 19";
		slots[19] = "Aktivität 20";
		
		final String[] prefixes = new String[20];
		
		prefixes[0] = "test*akt1";
		prefixes[1] = "test*akt2";
		prefixes[2] = "test*akt3";
		prefixes[3] = "test*akt4";
		prefixes[4] = "test*akt5";
		prefixes[5] = "test*akt6";
		prefixes[6] = "test*akt7";
		prefixes[7] = "test*akt8";
		prefixes[8] = "test*akt9";
		prefixes[9] = "test*akt10";
		prefixes[10] = "test*akt11";
		prefixes[11] = "test*akt12";
		prefixes[12] = "test*akt13";
		prefixes[13] = "test*akt14";
		prefixes[14] = "test*akt15";
		prefixes[15] = "test*akt16";
		prefixes[16] = "test*akt17";
		prefixes[17] = "test*akt18";
		prefixes[18] = "test*akt19";
		prefixes[19] = "test*akt20";
		
		final String[] columns = new String[12];
		columns[0] = "Januar";
		columns[1] = "Februar";
		columns[2] = "März";
		columns[3] = "April";
		columns[4] = "Mai";
		columns[5] = "Juni";
		columns[6] = "Juli";
		columns[7] = "August";
		columns[8] = "Sept.";
		columns[9] = "Oktober";
		columns[10] = "November";
		columns[11] = "Dezember";

		final String[] rows = new String[4];
		rows[0] = "2013";
		rows[1] = "2014";
		rows[2] = "2015";
		rows[3] = "2016";

		
		try {
//			calendarGenerator.generateQML("calendar1",prefix, colors,pattern, slots, columns, rows);
			calendarGenerator.generateQML("calendar1",prefixes, null,null, slots, columns, rows);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
