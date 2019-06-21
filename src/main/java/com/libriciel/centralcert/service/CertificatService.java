/*
 * central cert core
 * Copyright (C) 2018-2019 Libriciel-SCOP
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.libriciel.centralcert.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
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

@Slf4j
@RestController
public class CertificatService {


    /**
     * Check the validity of an URL
     */
    public static boolean isValidURL(String urlString) {
        try {
            URL url = new URL(urlString);
            url.toURI();
            return true;
        } catch (Exception exception) {
            return false;
        }
    }


    /**
     * Create an URL from a String
     */
    private static URL createURL(String url) {
        URL coUrl = null;

        try {
            coUrl = new URL(url);
        } catch (MalformedURLException e1) {
            log.error(e1.getLocalizedMessage());
        }

        return coUrl;
    }


    /**
     * Open an URLConnection from an URL
     */
    private static URLConnection openConnexion(URL coUrl) {
        URLConnection co = null;

        try {
            co = coUrl.openConnection();
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }

        return co;
    }


    /**
     * Open a connection from an HttpsURLConnection
     */
    private static void connectToURL(HttpsURLConnection httpsCo) {
        try {
            httpsCo.connect();
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
    }


    /**
     * Get a list of certificates from an HttpsURLConnection
     */
    private static Certificate[] getCertsFromCo(HttpsURLConnection httpsCo) {
        Certificate[] certs = null;
        try {
            certs = httpsCo.getServerCertificates();
        } catch (SSLPeerUnverifiedException e) {
            log.error(e.getLocalizedMessage());
        }
        return certs;
    }


    /**
     * Get a list of certificates from an URL String
     */
    public static X509Certificate[] getCertificateFromURL(String url) {

        if (!isValidURL(url)) {
            return new X509Certificate[0];
        }

        URLConnection co;
        HttpsURLConnection httpsCo;
        URL coUrl = createURL(url);

        if (coUrl == null) {
            return new X509Certificate[0];
        }

        co = openConnexion(coUrl);

        if (!(co instanceof HttpsURLConnection)) {
            return new X509Certificate[0];
        }

        httpsCo = (HttpsURLConnection) co;
        connectToURL(httpsCo);

        Certificate[] certs = getCertsFromCo(httpsCo);
        return (X509Certificate[]) certs;
    }


    /**
     * Get a certificate from a File
     */
    public static X509Certificate getCertificateFromToken(File f) {
        Certificate cert = null;

        try {
            FileInputStream file = new FileInputStream(f);
            cert = getCertsFiles(f, file);
        } catch (FileNotFoundException e1) {
            log.error(e1.getLocalizedMessage());
        }

        return (X509Certificate) cert;
    }


    /**
     * Get a certificate from a PKCS file
     */
    private static Certificate getFromPKCS(String mode, FileInputStream file) {
        Certificate cert = null;
        KeyStore ks = null;
        try {
            if (mode.equals("PKCS_7")) {
                ks = KeyStore.getInstance("PKCS7");
            } else {
                ks = KeyStore.getInstance("PKCS12");
            }
            if (ks != null) {
                ks.load(file, null);
                String alias = null;
                alias = ks.aliases().nextElement();
                if (ks.isCertificateEntry(alias)) {
                    cert = ks.getCertificate(alias);
                }
            }
        } catch (KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return cert;
    }


    /**
     * Get a certificate from all Files types
     */
    private static Certificate getCertsFiles(File f, FileInputStream file) {
        Certificate cert = null;
        CertificateFactory cf = null;
        if (file != null) {
            try {
                if (FilenameUtils.getExtension(f.getName()).contains("p12")
                        || FilenameUtils.getExtension(f.getName()).contains("pfx")) {
                    cert = getFromPKCS("PKCS_12", file);

                } else if (FilenameUtils.getExtension(f.getName()).contains("p7b")
                        || FilenameUtils.getExtension(f.getName()).contains("p7c")) {

                    cert = getFromPKCS("PKCS_7", file);
                } else {
                    cf = CertificateFactory.getInstance("X.509");
                    cert = cf.generateCertificate(file);
                }
            } catch (Exception e) {
                log.error(e.getLocalizedMessage());
            } finally {
                try {
                    file.close();
                } catch (IOException e) {
                    log.error(e.getLocalizedMessage());
                }
            }
        }
        return cert;
    }
}
