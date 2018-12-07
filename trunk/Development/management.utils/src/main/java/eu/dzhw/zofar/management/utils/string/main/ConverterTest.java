/*
 * Class to test ConvertClient
 */
package eu.dzhw.zofar.management.utils.string.main;

import java.nio.charset.Charset;

import eu.dzhw.zofar.management.utils.string.ConvertClient;
@Deprecated
public class ConverterTest {

	public static void main(String[] args) {
		ConvertClient client = ConvertClient.getInstance();
//		System.out.println(client.availableCharacterSets());
	    final String original = new String("äöüi".getBytes(Charset.forName("ISO-8859-1")));
		try {
			final String utf8 = client.convert(original, Charset.forName("ISO-8859-1"), Charset.forName("UTF-8"));
			System.out.println("iso88591 "+original);
			System.out.println("utf8 "+utf8);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
