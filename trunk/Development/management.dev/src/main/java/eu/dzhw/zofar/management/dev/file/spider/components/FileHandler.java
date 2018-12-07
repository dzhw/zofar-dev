package eu.dzhw.zofar.management.dev.file.spider.components;

import java.io.File;

public abstract class FileHandler {
	
	public abstract void call(final File file,final String startTag,final String stopTag);
	
	protected String getSuffix(final File file){
		if(file == null)return null;
		if(!file.isFile())return null;
		int suffixIndex = (file.getName()).lastIndexOf('.');
		if(suffixIndex != -1){
			return file.getName().substring(suffixIndex+1);
		}
		return null;
	}

}
