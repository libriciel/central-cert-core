/*
 *
 */
package com.libriciel.centralcert.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.libriciel.centralcert.repository.CertificatRepository;

/**
 * The Class AttesteCertificats.
 *
 * @author tpapin
 * 
 * The Class AttesteCertificats.
 */
@RestController
public class AttesteCertificats {
    private static Logger logger = Logger.getLogger("AttesteCertificats");
	/** 
	 * Repository permettant de faire le lien avec la base de données
	 */
	@Autowired
	public CertificatRepository cr;

	/**
	 * Vérifie la validité d'une URL
	 *
	 * @param urlString l'URL
	 * @return true, si l'URL est valide
	 * @return false sinon
	 */
	public static boolean isValidURL(String urlString){
		try{
			URL url = new URL(urlString);
			url.toURI();
			return true;
		} catch (Exception exception){
			return false;
		}
	}
	
	private static URL createURL(String url) {
		URL coUrl = null;

		try {
			coUrl = new URL(url);
		} catch (MalformedURLException e1) {
			logger.log(Level.SEVERE, e1.getMessage());
		}
		
		return coUrl;
	}
	
	private static URLConnection openConnexion(URL coUrl) {
		URLConnection co = null;

		try {
			co = coUrl.openConnection();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		
		return co;
	}
	
	private static void connectToURL(HttpsURLConnection httpsCo) {
		try {
			httpsCo.connect();
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	private static Certificate[] getCertsFromCo(HttpsURLConnection httpsCo) {
		Certificate[] certs = null;
		try {
			certs = httpsCo.getServerCertificates();
		} catch (SSLPeerUnverifiedException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return certs;
	}

	/**
	 * Gets the certificate from URL.
	 *
	 * @param url l'URL
	 * @return le certificat de l'URL
	 */
	public static X509Certificate[] getCertificateFromURL(String url){
		//si l'URL est valide
		if(isValidURL(url)) {
			
			//On instantie les variables nécéssaires à la mise en place d'une connection avec l'URL
			URLConnection co = null;
			HttpsURLConnection httpsCo = null;
			URL coUrl = null;
			
			//On essaye de créer une URL avec le string
			coUrl = createURL(url);
			
			//Si l'URL a bien été crée
			if(coUrl != null) {
				// on essaye d'ouvrir une connection sur l'URL
				co = openConnexion(coUrl);
			
				//si la connection est en HTTPS
				if(co instanceof HttpsURLConnection) {
					httpsCo = (HttpsURLConnection) co;
					
					//alors un certificat est présent et on essaye de se connecter à l'URL
					connectToURL(httpsCo);
										
					// on instantie un tableau de certificats X509
					Certificate[] certs = null;
						
					// on essaye de récupérer les certificats de l'adresse
					certs = getCertsFromCo(httpsCo);
						
					//on renvoit le tableau de certificats
					return (X509Certificate[]) certs;
					
				}else {
					//si la connection n'a pas été établie
					return new X509Certificate[0];
				}
				
			}else {
				//Si l'url n'a pas été crée
				return new X509Certificate[0];
			}
		}else {
			//Si l'url est invalide
			return new X509Certificate[0];
		}
	}
	
	/**
	 * Permet de récupérer un certificat depuis un token (fichier)
	 *
	 * @param f le fichier de certificat
	 * @return le certificat du fichier
	 */
	public static X509Certificate getCertificateFromToken(File f) {
		Certificate cert = null;

		
		//on essaye d'accéder au fichier
		try {
			FileInputStream file = new FileInputStream(f);
			//si le fichier est accessible
			cert = getCertsFiles(f, file);
		} catch (FileNotFoundException e1) {
			logger.log(Level.SEVERE, e1.getMessage());
		}
		
		
		//on renvoit le certificat
		return (X509Certificate) cert;
	}
	
	private static Certificate getFromPKCS(String mode, FileInputStream file) {
		Certificate cert = null;
		KeyStore ks = null;
		try {
			if(mode.equals("PKCS_7")) {
				ks = KeyStore.getInstance("PKCS7");
			}else {
				ks = KeyStore.getInstance("PKCS12");
			}
			if(ks != null) {
				ks.load(file, null);
				String alias = null;
				alias = ks.aliases().nextElement();
				if(ks.isCertificateEntry(alias)) {
					cert = ks.getCertificate(alias);
				}
			}
		} catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		return cert;
	}
	
	private static Certificate getCertsFiles(File f, FileInputStream file){
		Certificate cert = null;
		CertificateFactory cf = null;
		if(file != null) {
			try{
				if(FilenameUtils.getExtension(f.getName()).contains("p12")
					|| FilenameUtils.getExtension(f.getName()).contains("pfx")) {
					cert = getFromPKCS("PKCS_12", file);

				}else if(FilenameUtils.getExtension(f.getName()).contains("p7b")
						|| FilenameUtils.getExtension(f.getName()).contains("p7c")) {

					cert = getFromPKCS("PKCS_7", file);
				}else {
					cf = CertificateFactory.getInstance("X.509");
				    cert = cf.generateCertificate(file);
				}
			}catch(Exception e){
				logger.log(Level.SEVERE, e.getMessage());
			} finally {
				try {
					file.close();
				} catch (IOException e) {
					logger.log(Level.SEVERE, e.getMessage());
				}
			}
		}
	return cert;
	}
}
