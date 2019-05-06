package com.libriciel.Atteste.mails;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.libriciel.Atteste.BDD.certs.Certificat;
import com.libriciel.Atteste.BDD.certs.CertificatRepository;

@Component
public class MailHandler {

	@Autowired
	public CertificatRepository cr;
	
	public List<Certificat> certificatesToNotify(){
		List<Certificat> allCerts = cr.findAll();
		List<Certificat> res = new ArrayList<Certificat>();
		
		for(int i = 0; i< allCerts.size(); i++) {			
			for(int j = 0; j < allCerts.size(); j++) {
				if(allCerts.get(j).getNotBefore() != null && allCerts.get(j).getNotAfter() != null) {
					if(allCerts.get(j).getNotBefore().compareTo(new Date()) <= 0 && !allCerts.get(j).getNotified()) {
						res.add(allCerts.get(j));
					}
				}
			}
		}
		return res;
	}
	
	//@Scheduled(fixedRate = 5000)
	public void sendMailsToAll() {
		System.out.println("tick");
		
		List<Certificat> certs = this.certificatesToNotify();
		
		NotificationMailSender sender = new NotificationMailSender();

		for(int i = 0; i < certs.size(); i++) {
			certs.get(i).setNotified(false);
			this.cr.save(certs.get(i));
			for(int j = 0; j < certs.get(i).getAdditionnalMails().size(); j++) {
				if(!certs.get(i).isNotifyAll() && certs.get(i).getAdditionnalMails().get(j).isNotifiable()) {
					sender.send(certs.get(i).getAdditionnalMails().get(j));
					System.out.println("A mail was send");
				}
			}
		}
	}
}
