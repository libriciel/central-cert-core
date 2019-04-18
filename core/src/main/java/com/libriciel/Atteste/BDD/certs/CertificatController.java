/*
 *
 */
package com.libriciel.Atteste.BDD.certs;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
		X509Certificate[] certs = AttesteCertificats.getCertificateFromURL(url);
		if(certs != null) {
			for(int i = 0; i < certs.length; i++) {
				res.add(new Certificat(certs[i]));
			}
		}
		return res;
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
			c.setCN(certificat.getCN());
			c.setO(certificat.getO());
			c.setOU(certificat.getOU());
			c.setL(certificat.getL());
			c.setST(certificat.getST());
			c.setC(certificat.getC());
			c.setT(certificat.getT());
			c.setDC(certificat.getDC());
			c.setSTREET(certificat.getSTREET());
			c.setPC(certificat.getPC());
			c.setAdditionnalMails(certificat.getAdditionnalMails());
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
				c.setCN(certificats.get(i).getCN());
				c.setO(certificats.get(i).getO());
				c.setOU(certificats.get(i).getOU());
				c.setL(certificats.get(i).getL());
				c.setST(certificats.get(i).getST());
				c.setC(certificats.get(i).getC());
				c.setT(certificats.get(i).getT());
				c.setDC(certificats.get(i).getDC());
				c.setSTREET(certificats.get(i).getSTREET());
				c.setPC(certificats.get(i).getPC());
				c.setAdditionnalMails(certificats.get(i).getAdditionnalMails());
				repository.save(c);
			}else {
				repository.save(certificats.get(i));
			}
		}
	}
}
