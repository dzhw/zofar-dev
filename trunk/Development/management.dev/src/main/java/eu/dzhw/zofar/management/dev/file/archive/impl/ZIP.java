package eu.dzhw.zofar.management.dev.file.archive.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.dzhw.zofar.management.dev.file.archive.Archiver;
import eu.dzhw.zofar.management.utils.files.FileClient;
@Deprecated
public class ZIP implements Archiver {
	
	final static Logger LOGGER = LoggerFactory.getLogger(ZIP.class);

	public ZIP() {
		super();
	}

	public List<File> unpack(File archiveFile, File directory) throws Exception {
		throw new Exception("not implemented yet");
	}
	
	public void pack(final File file, final Map<String, Object> packageObj)
			throws IOException {
		if (file == null)
			return;
		if (packageObj == null)
			return;
		if (!file.exists())
			file.createNewFile();

		ZipOutputStream zos = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			zos = new ZipOutputStream(fos);
			// Add Data
			packageZipHelper("", "ExportData", packageObj, zos);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (zos != null)
				zos.close();
			if (fos != null)
				fos.close();
		}
	}
	
	private void packageZipHelper(final String parent, final String name,
			Object content, ZipOutputStream zos) throws IOException {
		if (parent == null)
			return;
		if (name == null)
			return;
		if (content == null)
			return;
		
		if ((String.class).isAssignableFrom(content.getClass())) {
			File tmp = FileClient.getInstance().createTextFile(name, content);
			LOGGER.info("Text file : {}",tmp);
			packageZipHelper(parent, name, tmp, zos);
		} else if ((File.class).isAssignableFrom(content.getClass())) {
			// File
			try {
				File file = new File(name);
				File tmp = (File)content;
				if(tmp.isFile()){
					FileClient.getInstance().copyFileUsingStream((File)content,file);
					addToZipFile(parent,file, zos);
				}
				else{
					LOGGER.info("{} is directory",name);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if ((Map.class).isAssignableFrom(content.getClass())) {
			// Directory
			try {
				addToZipFile(parent+"/"+name,new File(name), zos);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			@SuppressWarnings("unchecked")
			final Map<String, Object> subDir = (Map<String, Object>) content;
			// Iterate Map
			for (Map.Entry<String, Object> entry : subDir.entrySet()) {
				packageZipHelper(parent+"/"+name, entry.getKey(), entry.getValue(), zos);
			}


		}
	}
	
	private void addToZipFile(String parent,File file, ZipOutputStream zos)
			throws FileNotFoundException, IOException {
		if (file == null)return;
		if (!file.exists())return;
		LOGGER.info("Writing '{}' to zip file",parent+"/"+file.getName());
		

		if(file.isFile()){
			FileInputStream fis = new FileInputStream(file);
			ZipEntry zipEntry = new ZipEntry(parent+"/"+file.getName());
			zos.putNextEntry(zipEntry);
			byte[] bytes = new byte[1024];
			int length;
			while ((length = fis.read(bytes)) >= 0) {
				zos.write(bytes, 0, length);
			}
			zos.closeEntry();
			fis.close();
		}
		else if(file.isDirectory()){
			ZipEntry zipEntry = new ZipEntry(parent+"/"+file.getName());
			zos.putNextEntry(zipEntry);
			zos.closeEntry();
            File[] files = file.listFiles();
            final int count = files.length;
            for (int i = 0; i < count; i++) {
                LOGGER.info("Adding: {}" , files[i]);
                addToZipFile(parent+"/"+file.getName(),files[i],zos);
            }
			
		}
		
	}

}
