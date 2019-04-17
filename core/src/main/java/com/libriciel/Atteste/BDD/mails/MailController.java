/*
 *
 */
package com.libriciel.Atteste.BDD.mails;

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

/**
 * The Class MailController.
 */
@RestController
public class MailController {

	@Autowired
	MailRepository repository;

	@PostMapping("/api/mail/save")
	public void save(@RequestBody Mail mail) {
		if(mail != null) {
			repository.save(mail);
		}
	}
	
	@PostMapping("/api/mail/saveAll")
	public void saveAll(@RequestBody List<Mail> mail) {
		if(mail != null) {
			for(int i = 0; i < mail.size(); i++){
				if(mail.get(i) == null) {
					mail.remove(i);
				}
			}
			repository.saveAll(mail);
		}
	}
	
	@PostMapping("/api/mail/create")
	public Mail create(@RequestBody String mail) {
		return repository.save(new Mail(mail));
	}
	
	@GetMapping("/api/mail/select")
	public Mail select(@RequestParam("id") int id){
		Optional<Mail> opt = repository.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}else {
			return null;
		}
	}
	
	@GetMapping("/api/mail/selectAll")
	public List<Mail> selectAll(){
		List<Mail> res = new ArrayList<Mail>();
		repository.findAll().iterator().forEachRemaining(res::add);
		return res;
	}
	
	@DeleteMapping("/api/mail/delete")
	public void delete(@RequestParam("id") int id) {
		repository.deleteById(id);
	}
	
	@DeleteMapping("/api/mail/deleteAll")
	public void deleteAll() {
		repository.deleteAllInBatch();
	}

	
	
	@PutMapping("/api/mail/update")
	public void update(@RequestBody Mail mail) {
		Optional<Mail> opt = repository.findById(mail.getId());
		Mail m;
		if(opt.isPresent()) {
			m = opt.get();
			m.setAdresseMail(mail.getAdresseMail());
			repository.save(m);
		}else {
			repository.save(mail);
		}
	}
	
	@PutMapping("/api/mail/updateAll")
	public void updateAll(@RequestBody List<Mail> mail) {
		for(int i = 0; i < mail.size(); i++) {
			Optional<Mail> opt = repository.findById(mail.get(i).getId());
			Mail m;
			if(opt.isPresent()) {
				m = opt.get();
				m.setAdresseMail(mail.get(i).getAdresseMail());
				repository.save(m);
			}else {
				repository.save(mail.get(i));
			}
		}
	}

}
