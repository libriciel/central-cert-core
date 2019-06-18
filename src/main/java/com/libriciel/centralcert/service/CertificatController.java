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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.libriciel.centralcert.model.Certificat;
import com.libriciel.centralcert.repository.CertificatRepository;

@RestController
@PreAuthorize("hasRole('user')")
public class CertificatController {
    private static Logger logger = Logger.getLogger("logg");

	@Autowired
	CertificatRepository repository;

	/**
	 * Save one certificate
	 */
	@PostMapping("/api/certificat/save")
	public void save(@RequestBody Certificat certificat) {
		if(certificat != null) {
			repository.save(certificat);
		}
	}
	
	/**
	 * Save a list of certificates
	 */
	@PostMapping("/api/certificat/saveAll")
	public void saveAll(@RequestBody List<Certificat> certificat) {
		if(certificat != null) {
			for(int i = 0; i < certificat.size(); i++){
				if(certificat.get(i) == null) {
					certificat.remove(i);
				}
			}
			repository.saveAll(certificat);
		}
	}
	
	/**
	 * Get one certificate
	 */
	@GetMapping("/api/certificat/select")
	public Certificat select(@RequestParam("id") int id){
		Optional<Certificat> opt = repository.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}else {
			return null;
		}
	}
	
	/**
	 * Get all certificates
	 */
	@GetMapping("/api/certificat/selectAll")
	public List<Certificat> selectAll(){
		List<Certificat> res = new ArrayList<>();
		repository.findAll().iterator().forEachRemaining(res::add);
		return res;
	}

	/**
	 * Get certificates from an URL
	 */
	@GetMapping("/api/certificat/selectFromURL")
	public List<Certificat> selectFromUrl(@RequestParam("URL") String url) {
		List<Certificat> res = new ArrayList<>();
		X509Certificate[] certs = new X509Certificate[0];
		try {
			certs = CertificatService.getCertificateFromURL(url);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		if(certs.length != 0) {
			for(int i = 0; i < certs.length; i++) {
				res.add(new Certificat(certs[i]));
			}
		}
		return res;
	}
	
	/**
	 * Get a certificate from a file
	 */
	@PostMapping(value = "/api/certificat/selectFromFile", consumes = MediaType.ALL_VALUE)
	public Certificat selectFromFile(@RequestParam("file") MultipartFile file) {
		try {
			File convFile = new File(file.getOriginalFilename());
			boolean isCreated = convFile.createNewFile();
			if(isCreated) {
				FileOutputStream fos = new FileOutputStream(convFile);
				try {
					fos.write(file.getBytes());
					
					if(CertificatService.getCertificateFromToken(convFile) != null) {
						return new Certificat(CertificatService.getCertificateFromToken(convFile));
				    }else {
				    	return null;
				    }
				} catch (IOException e1) {
					logger.log(Level.SEVERE, e1.getMessage());
				} finally {
					closeFile(fos);
				}			
			}else {
				return null;
			}
		} catch (FileNotFoundException e2) {
			logger.log(Level.SEVERE, e2.getMessage());
		} catch (IOException e3) {
			logger.log(Level.SEVERE, e3.getMessage());
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
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	/**
	 * Delete a certificate
	 */
	@DeleteMapping("/api/certificat/delete")
	public void delete(@RequestParam("id") int id) {
		repository.deleteById(id);
	}
	
	/**
	 * Delete all certificates
	 */
	@DeleteMapping("/api/certificat/deleteAll")
	public void deleteAll() {
		repository.deleteAll();
	}

	
	
	/**
	 Update one certificate
	 */
	@PutMapping("/api/certificat/update")
	public void update(@RequestBody Certificat certificat) {
		Optional<Certificat> cert = repository.findById(certificat.getId());
		Certificat c;
		if(cert.isPresent()) {
			c = cert.get();
			c.setNotBefore(certificat.getNotBefore());
			c.setNotAfter(certificat.getNotAfter());
			c.setDN(certificat.getDN());
			c.setNotifyAll(certificat.isNotifyAll());
			c.setAdditionnalMails(certificat.getAdditionnalMails());
			c.setNotified(certificat.getNotified());
			repository.save(c);
		}else {
			repository.save(certificat);
		}
	}
	
	/**
	 * Update a list of certificates
	 */
	@PutMapping("/api/certificat/updateAll")
	public void updateAll(@RequestBody List<Certificat> certificats) {
		for(int i = 0; i < certificats.size(); i++) {
			Optional<Certificat> cert = repository.findById(certificats.get(i).getId());
			Certificat c;
			if(cert.isPresent()) {
				c = cert.get();
				c.setNotBefore(certificats.get(i).getNotBefore());
				c.setNotAfter(certificats.get(i).getNotAfter());
				c.setDN(certificats.get(i).getDN());
				c.setNotifyAll(certificats.get(i).isNotifyAll());
				c.setNotified(certificats.get(i).getNotified());
				c.setAdditionnalMails(certificats.get(i).getAdditionnalMails());
				repository.save(c);
			}else {
				repository.save(certificats.get(i));
			}
		}
	}

	/**
	 * Block the mails for an adress
	 */
	@GetMapping("/api/certificat/resetMail")
	public void resetMail(@RequestParam("id") int id, @RequestParam("addMail") String addMail) {
		Optional<Certificat> optCert = repository.findById(id);
		if(optCert.isPresent()) {
			Certificat c = optCert.get();
			for(int i = 0; i < c.getAdditionnalMails().size(); i++) {
				if(c.getAdditionnalMails().get(i).getAdresse().equals(addMail)) {
					c.getAdditionnalMails().get(i).setNotifiable(false);
				}
			}
			repository.save(c);
		}
	}
}