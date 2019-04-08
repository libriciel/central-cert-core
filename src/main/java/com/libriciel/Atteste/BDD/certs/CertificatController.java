/*
 * 
 */
package com.libriciel.Atteste.BDD.certs;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The Class CertificatController.
 */
@RestController
public class CertificatController {
	
	/** The repository. */
	@Autowired
	CertificatRepository repository;
	
	/**
	 * Save.
	 *
	 * @param certificat the c
	 */
	@GetMapping("/saveCertif")
	public void save(@RequestParam("Certificat") Certificat certificat) {
		repository.save(certificat);
	}
	
	/**
	 * Select.
	 *
	 * @param id the id
	 * @return the certificat
	 */
	@GetMapping("/selectCertif")
	public Certificat select(@RequestParam("CertificatID") int id) {
		return repository.findById(id).get();
	}
	
	/**
	 * Select all.
	 *
	 * @return the list
	 */
	@GetMapping("/selectAllCertif")
	public List<Certificat> selectAll(){
		List<Certificat> res = new ArrayList<Certificat>();
		repository.findAll().iterator().forEachRemaining(res::add);
		return res;
	}
	
	/**
	 * Delete all.
	 */
	@RequestMapping("/deleteAllCertif")
	public void deleteAll() {
		repository.deleteAllInBatch();
	}
}
