package eu.dzhw.zofar.management.utils.images.qr.main;

import java.awt.image.BufferedImage;
import java.util.List;

import eu.dzhw.zofar.management.utils.images.ImageConverter;
import eu.dzhw.zofar.management.utils.images.qr.QRGenerator;
import eu.dzhw.zofar.management.utils.string.StringUtils;

public class QRTest {

	public static void main(final String[] args) {
		final QRGenerator qr = QRGenerator.getInstance();
		final ImageConverter converter = ImageConverter.getInstance();
//		String hexCode = "A0";
		String hexCode = "FE";
		final BufferedImage result = qr.generate("http://www.xxx.de",25);
		final List<String> asAscii = converter.toAsciiList(result, StringUtils.getInstance().getCharacterByHex(hexCode));
		int lft = 1;
		for(final String line : asAscii){
			System.out.println(lft+":\t"+line);
			lft = lft + 1;
		}
	}
	
	

}
