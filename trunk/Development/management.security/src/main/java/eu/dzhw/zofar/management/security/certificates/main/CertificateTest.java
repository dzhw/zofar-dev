package eu.dzhw.zofar.management.security.certificates.main;

import java.io.File;
import java.security.KeyPair;
import java.security.cert.Certificate;

import org.apache.commons.io.FileUtils;

import eu.dzhw.zofar.management.security.certificates.CertificateClient;
@Deprecated
public class CertificateTest {

	public static void main(String[] args) {
		final CertificateClient certificateClient = CertificateClient.getInstance();
		
		final File tmpDir = FileUtils.getTempDirectory();
		final File certDir = new File(File.separator + "home"+File.separator+"hisuser"+File.separator+"Projekte"+File.separator+"SecuritySuite");

		long validFor = (long)1000*60*60*24*365;
		
		try {
		    KeyPair rootPair = certificateClient.createRSAKeyPair();
		    String subjectDN= "CN=Christian Meisner, OU=Infrastruktur und Methoden, O=Deutsches Zentrum f. Hochschul- und Wissenschaftsforschung GmbH, C=DE";
		    Certificate rootCert = certificateClient.createRootCertificate(subjectDN,rootPair, validFor);
		    certificateClient.saveCertificate(rootCert, new File(certDir, "parent.pem"));
		    certificateClient.savePrivateKey(rootPair.getPrivate(), new File(certDir, "private.pem"));
		} catch (Exception e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
//		KeyPair rootPair = null;
//		KeyPair intermediatedPair = null;
//		KeyPair endPair = null;
//		try {
//			rootPair = certificateClient.createRSAKeyPair();
//			intermediatedPair = certificateClient.createRSAKeyPair();
//			endPair = certificateClient.createRSAKeyPair();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		final File tmpDir = FileUtils.getTempDirectory();
////		final File certDir = new File(File.separator + "home" + File.separator	+ "hisuser" + File.separator + "ZofarExport");
//		final File endCertFile = new File(tmpDir,"export.pem");
//		
//		final String plainText="Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.";
//		
//		try {
//			Certificate rootCert = certificateClient.createRootCertificate("root ca",rootPair, 100000);
//			Certificate intermediatedCert = certificateClient.createIntermediatedCertificate("intermediated",intermediatedPair.getPublic(), rootPair.getPrivate(), rootCert, 100000);
//			Certificate endCert = certificateClient.createEndUserCertificate("end",endPair.getPublic(), intermediatedPair.getPrivate(),intermediatedCert, 100000);
////			System.out.println("endCert : "+endCert);
//			certificateClient.saveCertificate(endCert, endCertFile);
//			Certificate loaded = certificateClient.loadCertificate( endCertFile);
//			System.out.println("loaded : "+loaded);
//			
//			File encoded = certificateClient.encryptString(plainText, new File(tmpDir,"encodedText"), endCert);
//			File decoded = certificateClient.decryptFile(encoded, new File(tmpDir,"decodedText"), endPair.getPrivate(), endCert);
//			System.out.println(FileUtils.readFileToString(decoded));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

}
