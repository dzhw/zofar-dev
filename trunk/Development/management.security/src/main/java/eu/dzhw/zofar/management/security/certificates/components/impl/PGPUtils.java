/*
 * PGP Implementation of eu.dzhw.zofar.management.security.certificates.components.CertificateUtils
 */
package eu.dzhw.zofar.management.security.certificates.components.impl;

import java.security.KeyPair;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSecretKey;

import eu.dzhw.zofar.management.security.certificates.components.CertificateUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class PGPUtils.
 */
public class PGPUtils extends CertificateUtils {

	/** The instance. */
	private static PGPUtils INSTANCE;

	/**
	 * Instantiates a new PGP utils.
	 */
	private PGPUtils() {
		super();
	}

	/**
	 * Gets the single instance of PGPUtils.
	 * 
	 * @return single instance of PGPUtils
	 */
	public static PGPUtils getInstance() {
		if (INSTANCE == null)
			INSTANCE = new PGPUtils();
		return INSTANCE;
	}

	/* (non-Javadoc)
	 * @see eu.dzhw.zofar.management.security.certificates.components.CertificateUtils#createRootCertificate(java.lang.String, java.security.KeyPair, int)
	 */
	@Override
	public Certificate createRootCertificate(String subjectDN, KeyPair pair,
		long validity) {
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.dzhw.zofar.management.security.certificates.components.CertificateUtils#createDerivedCert(java.lang.String, java.security.PublicKey, java.security.PrivateKey, java.security.cert.Certificate, int, eu.dzhw.zofar.management.security.certificates.components.CertificateUtils.TYPE)
	 */
	@Override
	public Certificate createDerivedCert(String subjectDN, PublicKey pubKey,
			PrivateKey caKey, Certificate caCert, long validity, TYPE type) throws Exception {
		throw new Exception("Not implemented yet");
	}

	/* (non-Javadoc)
	 * @see eu.dzhw.zofar.management.security.certificates.components.CertificateUtils#toCertificate(java.lang.Object)
	 */
	@Override
	protected Certificate toCertificate(Object certificate) throws Exception {
		throw new Exception("Not implemented yet");
	}

	/* (non-Javadoc)
	 * @see eu.dzhw.zofar.management.security.certificates.components.CertificateUtils#toPublicKey(java.lang.Object)
	 */
	@Override
	protected PublicKey toPublicKey(Object publicKey) {
		Security.addProvider(new BouncyCastleProvider());
		try {
			return ((PGPPublicKey)publicKey).getKey("BC");
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (PGPException e) {
			e.printStackTrace();
		}		
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.dzhw.zofar.management.security.certificates.components.CertificateUtils#toPrivateKey(java.lang.Object, java.lang.String)
	 */
	@Override
	protected PrivateKey toPrivateKey(Object privateKey,String passphrase) {
		Security.addProvider(new BouncyCastleProvider());
		try {
			PGPPrivateKey pKey = ((PGPSecretKey)privateKey).extractPrivateKey(passphrase.toCharArray(), "BC");
			return pKey.getKey();
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		} catch (PGPException e) {
			e.printStackTrace();
		}		
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.dzhw.zofar.management.security.certificates.components.CertificateUtils#fromCertificate(java.security.cert.Certificate)
	 */
	@Override
	protected Object fromCertificate(Certificate certificate) {
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.dzhw.zofar.management.security.certificates.components.CertificateUtils#fromPublicKey(java.security.PublicKey)
	 */
	@Override
	protected Object fromPublicKey(PublicKey publicKey) {
		Security.addProvider(new BouncyCastleProvider());
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see eu.dzhw.zofar.management.security.certificates.components.CertificateUtils#fromPrivateKey(java.security.PrivateKey)
	 */
	@Override
	protected Object fromPrivateKey(PrivateKey privateKey) {
		Security.addProvider(new BouncyCastleProvider());
		// TODO Auto-generated method stub
		return null;
	}
}
