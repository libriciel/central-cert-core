/*
 * 
 */
package com.libriciel.Atteste.BDD.mails;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.libriciel.Atteste.BDD.certs.Certificat;
import com.libriciel.Atteste.BDD.certs.CertificatRepository;

/**
 * The Class MailController.
 */
@RestController
public class MailController {
	
	/** The repository. */
	@Autowired
	MailRepository repository;
	
	@Autowired
	CertificatRepository certificatRepository;
	
	/**
	 * Save.
	 *
	 * @param adresseMail the adresse mail
	 * @return the mail
	 */
	@GetMapping("/saveMail")
	public Mail save(@RequestParam("adresseMail") String adresseMail) {
		return repository.save(new Mail(adresseMail));
	}
	
	/**
	 * Select.
	 *
	 * @param id the id
	 * @return the mail
	 */
	@GetMapping("/selectMail")
	public Mail select(@RequestParam("MailID") int id) {
		return repository.findById(id).get();
	}
	
	@GetMapping("/selectAllMail")
	public List<Mail> selectAll(){
		return repository.findAll();
	}
	
	@GetMapping("/addMail")
	public void addMail(@RequestParam("MailID") int mailId, @RequestParam("CertificatID") int certificatId){
		Optional<Mail> m = repository.findById(mailId);
		Optional<Certificat> c = certificatRepository.findById(certificatId);
		if(m.isPresent() && c.isPresent()) {
			Mail mail = m.get();
			Certificat certif = c.get();
			mail.addCertificat(certif);
			
			certif.addMail(mail);
			
			certificatRepository.save(certif);
			repository.save(mail);
		}
	}
	
	/**
	 * Delete all.
	 */
	@RequestMapping("/deleteAllMail")
	public void deleteAll() {
		repository.deleteAllInBatch();
	}
}
