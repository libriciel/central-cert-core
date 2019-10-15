/*
 * Central-Cert Core
 * Copyright (C) 2019 Libriciel-SCOP
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

import com.libriciel.centralcert.model.Certificat;
import com.libriciel.centralcert.repository.CertificatRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@PreAuthorize("hasRole('user')")
public class CertificatController {

    public static final String CERTIFICATE_TYPE_X509 = "X.509";


    private final CertificatRepository repository;


    // <editor-fold desc="Beans">

    public CertificatController(CertificatRepository repository) {this.repository = repository;}

    // </editor-fold desc="Beans">


    /**
     * Save one certificate
     */
    @PostMapping("/certificat/save")
    public void save(@RequestBody Certificat certificat) {
        if (certificat != null) {
            repository.save(certificat);
        }
    }


    /**
     * Save a list of certificates
     */
    @PostMapping("/certificat/saveAll")
    public void saveAll(@RequestBody List<Certificat> certificat) {
        if (certificat != null) {
            for (int i = 0; i < certificat.size(); i++) {
                if (certificat.get(i) == null) {
                    certificat.remove(i);
                }
            }
            repository.saveAll(certificat);
        }
    }


    /**
     * Get one certificate
     */
    @GetMapping("/certificat/select")
    public Certificat select(@RequestParam("id") int id) {
        Optional<Certificat> opt = repository.findById(id);
        if (opt.isPresent()) {
            return opt.get();
        } else {
            return null;
        }
    }


    /**
     * Get all certificates
     */
    @GetMapping("/certificat/selectAll")
    public List<Certificat> selectAll() {
        List<Certificat> res = new ArrayList<>();
        repository.findAll().iterator().forEachRemaining(res::add);
        return res;
    }


    /**
     * Get certificates from an URL
     */
    @GetMapping("/certificat/selectFromURL")
    public List<Certificat> selectFromUrl(@RequestParam("URL") String url) {
        List<Certificat> res = new ArrayList<>();
        X509Certificate[] certs = new X509Certificate[0];
        try {
            certs = CertificatService.getCertificateFromURL(url);
        } catch (Exception e) {
            log.error(e.getLocalizedMessage());
        }
        if (certs.length != 0) {
            for (int i = 0; i < certs.length; i++) {
                res.add(new Certificat(certs[i]));
            }
        }
        return res;
    }


    /**
     * Get a certificate from a file
     */
    @PostMapping(value = "/certificat/selectFromFile", consumes = MediaType.ALL_VALUE)
    public Certificat selectFromFile(@RequestParam("file") MultipartFile file) {
        try {
            File convFile = new File(file.getOriginalFilename());
            boolean isCreated = convFile.createNewFile();
            if (isCreated) {
                FileOutputStream fos = new FileOutputStream(convFile);
                try {
                    fos.write(file.getBytes());

                    if (CertificatService.getCertificateFromToken(convFile) != null) {
                        return new Certificat(CertificatService.getCertificateFromToken(convFile));
                    } else {
                        return null;
                    }
                } catch (IOException e) {
                    log.error(e.getLocalizedMessage());
                } finally {
                    closeFile(fos);
                }
            } else {
                return null;
            }
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
        return null;
    }


    /**
     * close a file
     */
    public void closeFile(FileOutputStream fos) {
        try {
            fos.close();
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }


    /**
     * Delete a certificate
     */
    @DeleteMapping("/certificat/delete")
    public void delete(@RequestParam("id") int id) {
        repository.deleteById(id);
    }


    /**
     * Delete all certificates
     */
    @DeleteMapping("/certificat/deleteAll")
    public void deleteAll() {
        repository.deleteAll();
    }


    /**
     * Update one certificate
     */
    @PutMapping("/certificat/update")
    public void update(@RequestBody Certificat certificat) {
        Optional<Certificat> cert = repository.findById(certificat.getCertificatId());
        Certificat c;
        if (cert.isPresent()) {
            c = cert.get();
            c.setNotBefore(certificat.getNotBefore());
            c.setNotAfter(certificat.getNotAfter());
            c.setDn(certificat.getDn());
            c.setNotifyAll(certificat.isNotifyAll());
            c.setAdditionnalMails(certificat.getAdditionnalMails());
            c.setNotified(certificat.getNotified());
            repository.save(c);
        } else {
            repository.save(certificat);
        }
    }


    /**
     * Update a list of certificates
     */
    @PutMapping("/certificat/updateAll")
    public void updateAll(@RequestBody List<Certificat> certificats) {
        for (int i = 0; i < certificats.size(); i++) {
            Optional<Certificat> cert = repository.findById(certificats.get(i).getCertificatId());
            Certificat c;
            if (cert.isPresent()) {
                c = cert.get();
                c.setNotBefore(certificats.get(i).getNotBefore());
                c.setNotAfter(certificats.get(i).getNotAfter());
                c.setDn(certificats.get(i).getDn());
                c.setNotifyAll(certificats.get(i).isNotifyAll());
                c.setNotified(certificats.get(i).getNotified());
                c.setAdditionnalMails(certificats.get(i).getAdditionnalMails());
                repository.save(c);
            } else {
                repository.save(certificats.get(i));
            }
        }
    }


    /**
     * Block the mails for an adress
     */
    @GetMapping("/certificat/resetMail")
    public void resetMail(@RequestParam("id") int id, @RequestParam("addMail") String addMail) {
        Optional<Certificat> optCert = repository.findById(id);
        if (optCert.isPresent()) {
            Certificat c = optCert.get();
            for (int i = 0; i < c.getAdditionnalMails().size(); i++) {
                if (c.getAdditionnalMails().get(i).getAdresse().equals(addMail)) {
                    c.getAdditionnalMails().get(i).setNotifiable(false);
                }
            }
            repository.save(c);
        }
    }
}
