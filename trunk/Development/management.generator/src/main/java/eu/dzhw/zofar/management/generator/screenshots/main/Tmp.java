package eu.dzhw.zofar.management.generator.screenshots.main;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.w3c.dom.Document;

import com.google.common.io.Files;

import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.files.FileClient;
import eu.dzhw.zofar.management.utils.images.ImageConverter;
import eu.dzhw.zofar.management.utils.xml.XmlClient;

public class Tmp {

	public static void main(String[] args) {

		final String surveyname = "xxxx";

		File screenshotParentDir = DirectoryClient.getInstance().createDir(DirectoryClient.getInstance().getHome(), "screenshots");
		File screenshotDir = DirectoryClient.getInstance().createDir(screenshotParentDir, surveyname);
		File svgDir = DirectoryClient.getInstance().createDir(screenshotDir, "svg");
		DirectoryClient.getInstance().cleanDirectory(svgDir);
		final List<File> screenshotList = DirectoryClient.getInstance().readDir(screenshotDir);

		for (final File file : screenshotList) {
			
			final String suffix = FileClient.getInstance().getSuffix(file);
			if(!suffix.equals("png"))continue;
			System.out.println("File : "+file.getAbsolutePath());
			
			BufferedImage img = null;
			try {
				img = ImageIO.read(file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (img != null) {
				final Document svgDoc = ImageConverter.getInstance().raster2svg(img);
				System.out.println("svgDoc : "+svgDoc);
				XmlClient.getInstance().saveDocument(svgDoc, FileClient.getInstance().createOrGetFile(file.getName(), ".svg", svgDir));
			}
		}
	}

}
