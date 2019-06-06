/*
 *
 */
package com.libriciel.Atteste.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.libriciel.Atteste.repository.CertificatRepository;

/**
 * The Class AttesteCertificats.
 *
 * @author tpapin
 * 
 * The Class AttesteCertificats.
 */
@RestController
public class AttesteCertificats {

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
			HttpURLConnection httpCo = null;
			URL coUrl = null;
			
			//On essaye de créer une URL avec le string
			try {
				coUrl = new URL(url);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			
			//Si l'URL a bien été crée
			if(coUrl != null) {
				
				// on essaye d'ouvrir une connection sur l'URL
				try {
					co = coUrl.openConnection();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
				//si la connection est en HTTPS
				if(co instanceof HttpsURLConnection) {
					httpsCo = (HttpsURLConnection) co;
					
					//alors un certificat est présent et on essaye de se connecter à l'URL
					try {
						httpsCo.connect();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}else if(co instanceof HttpURLConnection) {
					//si la connection est en HTTP
					httpCo = (HttpURLConnection) co;
				}else {
					//si la connection n'a pas été établie
					return null;
				}
				
				//si la connection en HTTPS à bien été établie
				if(httpsCo != null) {
					
					// on instantie un tableau de certificats X509
					Certificate[] certs = null;
					
					// on essaye de récupérer les certificats de l'adresse
					try {
						certs = httpsCo.getServerCertificates();
					} catch (SSLPeerUnverifiedException e) {
						e.printStackTrace();
					}
					
					//on renvoit le tableau de certificats
					return (X509Certificate[]) certs;
				}else if(httpCo != null) {
					//http so no certificates
					return null;
				}else {
					//error
					return null;
				}
			}else {
				//Si l'url n'a pas été crée
				return null;
			}
		}else {
			//Si l'url est invalide
			return null;
		}
	}
	
	/**
	 * Permet de récupérer un certificat depuis un token (fichier)
	 *
	 * @param f le fichier de certificat
	 * @return le certificat du fichier
	 */
	public static X509Certificate getCertificateFromToken(File f) {
		FileInputStream file = null;
		Certificate cert = null;
		CertificateFactory cf = null;
		
		//on essaye d'accéder au fichier
		try {
			file = new FileInputStream(f);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		//si le fichier est accessible
		try{
			if(file != null) {
				System.out.println(FilenameUtils.getExtension(f.getName()));
				if(FilenameUtils.getExtension(f.getName()).contains("p12")
					|| FilenameUtils.getExtension(f.getName()).contains("pfx")) {
					KeyStore ks = KeyStore.getInstance("PKCS12");
					ks.load(file, null);
					String alias = ks.aliases().nextElement();
					if(ks.isCertificateEntry(alias)) {
						cert = ks.getCertificate(alias);
					}
				}else if(FilenameUtils.getExtension(f.getName()).contains("p7b")
						|| FilenameUtils.getExtension(f.getName()).contains("p7c")) {
					KeyStore ks = KeyStore.getInstance("PKCS7");
					ks.load(file, null);
					String alias = ks.aliases().nextElement();
					if(ks.isCertificateEntry(alias)) {
						cert = ks.getCertificate(alias);
					}
				}else {
					cf = CertificateFactory.getInstance("X.509");
				    cert = cf.generateCertificate(file);
				}
			}
		}catch(Exception e){
		    e.printStackTrace();
		}
		
		//on renvoit le certificat
		return (X509Certificate) cert;
	}
}
