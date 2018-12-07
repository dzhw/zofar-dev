package eu.dzhw.zofar.management.dev.file.archive;

import java.io.File;
import java.util.List;
import java.util.Map;
@Deprecated
public interface Archiver {
	public List<File> unpack(final File archiveFile, final File directory) throws Exception;
	public void pack(final File file, final Map<String, Object> packageObj)throws Exception;
}
