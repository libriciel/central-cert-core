package com.libriciel.Atteste.mails;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.libriciel.Atteste.BDD.certs.Certificat;
import com.libriciel.Atteste.BDD.certs.CertificatRepository;
import com.libriciel.Atteste.BDD.mails.Mail;
import com.libriciel.Atteste.BDD.notifs.Notification;

public class MailHandler {

	@Autowired
	public CertificatRepository cr;

	
	public List<Notification> certificatesToNotify(){
		List<Certificat> allCerts = cr.findAll();
		List<Notification> res = new ArrayList<Notification>();
		for(int i = 0; i< allCerts.size(); i++) {
			List <Notification> notifs = allCerts.get(i).getNotifications();
			
			for(int j = 0; j < notifs.size(); j++) {
				if(notifs.get(j).getNotBefore().compareTo(new Date()) <= 0 && !notifs.get(j).isActivated() && !notifs.get(j).isSeen()) {
					res.add(notifs.get(j));
				}
			}
		}
		return res;
	}
	
	public void sendMailsToAll() {
		List<Notification> notifications = this.certificatesToNotify();
		MailSender sender = new MailSender();

		for(int i = 0; i < notifications.size(); i++) {
			notifications.get(i).setActivated(true);
			cr.save(notifications.get(i).getCertificat());
			List <Mail> ml = notifications.get(i).getCertificat().getAdditionnalMails();
			for(int j = 0; j < ml.size(); j++) {
				sender.send(ml.get(j) , notifications.get(i));
			}
		}
	}
}
