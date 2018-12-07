package eu.dzhw.zofar.management.dev.file.system;


@Deprecated
public class FileUtilities {

	private static FileUtilities INSTANCE;

	private FileUtilities() {
		super();
	}

	public static FileUtilities getInstance() {
		if (INSTANCE == null)
			INSTANCE = new FileUtilities();
		return INSTANCE;
	}


}
