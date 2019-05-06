/*
 *
 */
package com.libriciel.Atteste.Url;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.libriciel.Atteste.BDD.certs.CertificatRepository;
/**
 * The Class AttesteCertificats.
 */
@RestController
public class AttesteCertificats {

	@Autowired
	public CertificatRepository cr;

	@Autowired

	/**
	 * Sets the connection.
	 *
	 * @param url the url
	 * @return the https URL connection
	 */
	public static HttpsURLConnection setHttpsonnection(URL url) {
		HttpsURLConnection co = null;
		try {
			co = (HttpsURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			co.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return co;
	}

	public static HttpURLConnection setHttpConnection(URL url) {
		HttpURLConnection co = null;
		try {
			co = (HttpURLConnection) url.openConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			co.connect();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return co;
	}

	public static boolean isValidURL(String urlString){
		try{
			URL url = new URL(urlString);
			url.toURI();
			return true;
		} catch (Exception exception){
			return false;
		}
	}

	public static X509Certificate[] getCertificateFromURL(String url) {
		if(isValidURL(url)) {
			URLConnection co = null;
			HttpsURLConnection httpsCo = null;
			HttpURLConnection httpCo = null;
			URL coUrl = null;
			try {
				coUrl = new URL(url);
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}

			try {
				co = coUrl.openConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if(co instanceof HttpsURLConnection) {
				httpsCo = (HttpsURLConnection) co;
				try {
					httpsCo.connect();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if(co instanceof HttpURLConnection) {
				httpCo = (HttpURLConnection) co;
			}

			if(httpsCo != null) {
				Certificate[] certs = null;
				try {
					certs = httpsCo.getServerCertificates();
				} catch (SSLPeerUnverifiedException e) {
					e.printStackTrace();
				}
				return (X509Certificate[]) certs;
			}else if(httpCo != null) {
				//http so no certificates
				return null;
			}else {
				//error
				return null;
			}
		}else {
			return null;
		}
	}
	
	public static X509Certificate getCertificateFromToken(File f) {
		FileInputStream file = null;
		Certificate cert = null;
		
		try {
			file = new FileInputStream(f);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		
		try{
			if(file != null) {
				CertificateFactory cf = CertificateFactory.getInstance("X.509");
			    cert = cf.generateCertificate(file);
			}
		}catch(Exception e){
		    e.printStackTrace();
		}
		
		return (X509Certificate) cert;
	}

}
