package eu.dzhw.zofar.management.dev.file.spider.components.impl;

import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.dev.file.spider.components.FileHandler;
import eu.dzhw.zofar.management.utils.string.ReplaceClient;

public class AddTextHandler extends FileHandler {
	
	final static Logger LOGGER = LoggerFactory.getLogger(AddTextHandler.class);
	private final String text;
	private final String suffix;



	public AddTextHandler(final String text,final String suffix) {
		super();
		this.text = text;
		this.suffix = suffix;
	}

	public void call(final File file,final String startTag,final String stopTag) {
		if(file == null)return;
		if(file.isDirectory())return;
		if(!file.canRead())return;
		if(!file.canWrite())return;
		if(!this.getSuffix(file).equals(this.suffix))return;
		try {
			String oldContent = FileUtils.readFileToString(file, "UTF-8");
			if(oldContent != null){
				if((startTag != null)&&(!startTag.equals(""))&&(stopTag != null)&&(!stopTag.equals(""))){
					//remove old header
					oldContent = ReplaceClient.getInstance().replaceBetweenTags(oldContent, startTag, stopTag,"");
					//remove leading breaks 
					oldContent = ReplaceClient.getInstance().stripLeadingBreaks(oldContent);
				}	
				final String newContent = startTag+"\n"+text+"\n"+stopTag+"\n"+oldContent;
				LOGGER.info("write header to {}",file.getAbsolutePath());
				FileUtils.writeStringToFile(file, newContent , "UTF-8");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
