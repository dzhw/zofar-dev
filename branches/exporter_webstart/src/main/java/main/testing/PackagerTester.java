package main.testing;

import java.io.File;
import java.io.IOException;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;

import eu.dzhw.zofar.management.security.certificates.CertificateClient;
import eu.dzhw.zofar.management.utils.files.PackagerClient;

// TODO: Auto-generated Javadoc
/**
 * The Class PackagerTester.
 */
@Deprecated
public class PackagerTester {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 */
	public static void main(final String[] args) {

		final Map<String, Object> packageObj = new HashMap<String, Object>();
		final String certDir = File.separator + "xxx" + File.separator + "xxx" + File.separator + "xxx" + File.separator;
		final String encryptCertFilePath = certDir + "openssl" + File.separator + "export.pem";
		final String howtoFilePath = certDir + File.separator + "Package" + File.separator + "Commands.txt";
		final String installerPath = certDir + File.separator + "Package" + File.separator + "Installer";

		final Map<String, Object> howtoPackage = new HashMap<String, Object>();
		howtoPackage.put("Commands.txt", new File(howtoFilePath));
		final Map<String, Object> installerPackage = new HashMap<String, Object>();
		installerPackage.put("OpenSSL_win.exe", new File(installerPath + File.separator + "Win32OpenSSL_Light-1_0_1h.exe"));
		howtoPackage.put("Installer", installerPackage);
		packageObj.put("HOWTO", howtoPackage);

		final Map<String, Object> cerPackage = new HashMap<String, Object>();
		cerPackage.put("export.pem", new File(encryptCertFilePath));
		packageObj.put("Certificates", cerPackage);

		final Map<String, Object> docPackage = new HashMap<String, Object>();
		docPackage.put("doc.txt", "Documentation File");
		packageObj.put("Doc", docPackage);

		final Map<String, Object> csvPackage = new HashMap<String, Object>();
		final CertificateClient certTools = CertificateClient.getInstance();
		try {
			final Certificate cert = certTools.loadCertificate(new File(encryptCertFilePath));
			csvPackage.put("data.csv.encrypted", certTools.encryptString("CVS Data", File.createTempFile("data.csv.encrypted", ""), cert));

		} catch (final Exception e2) {
			e2.printStackTrace();
		}
		packageObj.put("csv", csvPackage);

		@SuppressWarnings("unchecked")
		final Object exportStata = "DO File";
		final Map<String, Object> intructionPackage = new HashMap<String, Object>();
		intructionPackage.put("stata.do", exportStata);
		packageObj.put("import", intructionPackage);

		try {
			final File zip = new File(certDir + "test.zip");
			PackagerClient.getInstance().packageZip(zip, packageObj);
		} catch (final IOException e) {
			e.printStackTrace();
		}

	}

}
