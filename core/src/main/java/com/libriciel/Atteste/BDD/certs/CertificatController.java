/*
 *
 */
package com.libriciel.Atteste.BDD.certs;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.libriciel.Atteste.Url.AttesteCertificats;

@RestController
public class CertificatController {

	@Autowired
	CertificatRepository repository;

	@PostMapping("/api/certificat/save")
	public void save(@RequestBody Certificat certificat) {
		if(certificat != null) {
			repository.save(certificat);
		}
	}
	
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
	
	@GetMapping("/api/certificat/select")
	public Certificat select(@RequestParam("id") int id){
		Optional<Certificat> opt = repository.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}else {
			return null;
		}
	}
	
	@GetMapping("/api/certificat/selectAll")
	public List<Certificat> selectAll(){
		List<Certificat> res = new ArrayList<Certificat>();
		repository.findAll().iterator().forEachRemaining(res::add);
		return res;
	}

	@GetMapping("/api/certificat/selectFromURL")
	public List<Certificat> selectFromUrl(@RequestParam("URL") String url) {
		List<Certificat> res = new ArrayList<Certificat>();
		X509Certificate[] certs = null;
		try {
			certs = AttesteCertificats.getCertificateFromURL(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(certs != null) {
			for(int i = 0; i < certs.length; i++) {
				res.add(new Certificat(certs[i]));
			}
		}
		return res;
	}
	
	@PostMapping(value = "/api/certificat/selectFromFile", consumes = MediaType.ALL_VALUE)
	public Certificat selectFromFile(@RequestParam("file") MultipartFile file) {
		File convFile = new File(file.getOriginalFilename());
	    try {
			convFile.createNewFile();
			FileOutputStream fos = new FileOutputStream(convFile);
		    fos.write(file.getBytes());
		    fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new Certificat(AttesteCertificats.getCertificateFromToken(convFile));
	}
	
	
	@DeleteMapping("/api/certificat/delete")
	public void delete(@RequestParam("id") int id) {
		repository.deleteById(id);
	}
	
	@DeleteMapping("/api/certificat/deleteAll")
	public void deleteAll() {
		repository.deleteAll();
	}

	
	
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
}
