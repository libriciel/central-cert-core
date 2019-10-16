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
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@PreAuthorize("hasRole('user')")
public class CertificatController {

    public static final String CERTIFICATE_TYPE_X509 = "X.509";


    private final CertificatRepository repository;


    // <editor-fold desc="Beans">


    public CertificatController(CertificatRepository repository) {this.repository = repository;}


    // </editor-fold desc="Beans">


    @PostMapping("/certificat/save")
    public void save(@RequestBody Certificat certificat) {
        if (certificat == null) {
            return;
        }

        log.info("save : " + certificat);
        repository.save(certificat);
    }


    @PostMapping("/certificat/saveAll")
    public void saveAll(@RequestBody List<Certificat> certList) {
        log.info("saveAll ? " + certList);
        if (certList == null) {
            return;
        }

        List<Certificat> certificatesCleanedList = certList.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        log.info("saveAll : " + certificatesCleanedList);
        repository.saveAll(certificatesCleanedList);
    }


    /**
     * Get one certificate
     */
    @GetMapping("/certificat/select")
    public Certificat select(@RequestParam("id") int id) {
        Certificat result = repository.findById(id).orElse(null);
        log.info("get " + id + ":" + result);
        return result;
    }


    /**
     * Get all certificates
     */
    @GetMapping("/certificat/selectAll")
    public List<Certificat> selectAll() {
        List<Certificat> result = repository.findAll();
        log.info("getAll : " + result);
        return result;
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

        for (X509Certificate cert : certs) {
            res.add(new Certificat(cert));
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
    private void closeFile(FileOutputStream fos) {
        try {
            fos.close();
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }


    @DeleteMapping("/certificat/delete")
    public void delete(@RequestParam("id") int id) {
        repository.deleteById(id);
    }


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
        for (Certificat certificat : certificats) {
            Optional<Certificat> cert = repository.findById(certificat.getCertificatId());
            Certificat c;
            if (cert.isPresent()) {
                c = cert.get();
                c.setNotBefore(certificat.getNotBefore());
                c.setNotAfter(certificat.getNotAfter());
                c.setDn(certificat.getDn());
                c.setNotifyAll(certificat.isNotifyAll());
                c.setNotified(certificat.getNotified());
                c.setAdditionnalMails(certificat.getAdditionnalMails());
                repository.save(c);
            } else {
                repository.save(certificat);
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
