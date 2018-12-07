/*
 * Class of Methods to load and copy Objects
 */
package eu.dzhw.zofar.management.utils.objects;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class LoaderClient.
 */
public class LoaderClient {

	/** The Constant INSTANCE. */
	private static final LoaderClient INSTANCE = new LoaderClient();

	/** The Constant LOGGER. */
	private final static Logger LOGGER = LoggerFactory.getLogger(LoaderClient.class);

	/**
	 * Instantiates a new loader client.
	 */
	private LoaderClient() {
		super();
	}

	/**
	 * Gets the single instance of LoaderClient.
	 * 
	 * @return single instance of LoaderClient
	 */
	public static synchronized LoaderClient getInstance() {
		return INSTANCE;
	}

	/**
	 * Copy object.
	 * 
	 * @param o
	 *            the o
	 * @return the object
	 * @throws Exception
	 *             the exception
	 */
	public Object copyObject(final Object o) throws Exception {
		return CloneClient.getInstance().cloneObj(o);
	}

	/**
	 * Serialize Object to file.
	 * 
	 * @param filename
	 *            the filename
	 * @param obj
	 *            the obj
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void serialize(final String filename, final Object obj) throws IOException {
		// System.out.println("Serialize : "+obj+" to "+filename);
		final File file = new File(filename);
		if ((file.exists()) && (file.isFile()) && (file.canWrite())) {
			file.delete();
			file.createNewFile();
		}
		final ObjectOutputStream objOut = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)));
		objOut.writeObject(obj);
		objOut.close();
	}

	/**
	 * Deserialize Object from file.
	 * 
	 * @param filename
	 *            the filename
	 * @return the object
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException
	 *             the class not found exception
	 */
	public Object deserialize(final String filename) throws IOException, ClassNotFoundException {
		// System.out.println("Deserialize : Object from "+filename);
		if ((filename == null) || (filename.equals("")))
			return null;
		final ObjectInputStream objIn = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
		final Object back = objIn.readObject();
		objIn.close();
		return back;
	}

	/**
	 * Load raw data.
	 * 
	 * @param filename
	 *            the filename
	 * @return the byte[]
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public byte[] loadRawData(final String filename) throws IOException {
		if ((filename == null) || (filename.equals("")))
			return null;
		final File file = new File(filename);
		final InputStream is = new FileInputStream(file);
		final long length = file.length();
		if (length > Integer.MAX_VALUE) {
			System.out.println("File is too big");
			is.close();
			return null;
		}
		// Create the byte array to hold the data
		final byte[] back = new byte[(int) length];

		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < back.length && (numRead = is.read(back, offset, back.length - offset)) >= 0) {
			offset += numRead;
		}

		// Ensure all the bytes have been read in
		if (offset < back.length) {
			is.close();
			throw new IOException("Could not completely read file " + file.getName());
		}

		// Close the input stream and return bytes
		is.close();
		return back;
	}

}
