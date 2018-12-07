package eu.dzhw.zofar.management.utils.odf.main;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;

import eu.dzhw.zofar.management.utils.files.DirectoryClient;
import eu.dzhw.zofar.management.utils.odf.TextClient;
import eu.dzhw.zofar.management.utils.odf.components.WriterDocument;

public class PDFWrapper {

	public static void main(String[] args) {
		final String surveyname = "showroom";
		
		File screenshotParentDir = DirectoryClient.getInstance().createDir(DirectoryClient.getInstance().getHome(),"screenshots");
		File screenshotDir = DirectoryClient.getInstance().createDir(screenshotParentDir,surveyname);

		
		final TextClient textClient = TextClient.getInstance();
		try {
//			final WriterDocument doc = textClient.loadDocument("Template.odt");
			final WriterDocument doc = textClient.createDocument();
			doc.setLandscape(false);

			textClient.addTOC(doc, "Seiten");
			textClient.addBreak(doc);
			List<File> files = DirectoryClient.getInstance().readDir(screenshotDir);

			for(final File file:files){
				String ext = FilenameUtils.getExtension(file.getAbsolutePath());
				if(!ext.equals("png"))continue;
				String name = file.getName().replaceAll("xxx_"+surveyname+"_", "");
				name = name.replaceAll("_html", "");
				name = name.replaceAll(Pattern.quote("."+ext), "");
				
				textClient.addBreak(doc);
				textClient.addHeading(doc, name);
				textClient.addImage(doc,file,1000,700);
//				textClient.addBreak(doc);
			}
			textClient.saveDocument(doc, "xxxx/screenshots/"+surveyname+"/"+surveyname+".odf");


		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

}
