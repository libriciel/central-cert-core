package com.libriciel.Atteste.Mails;


import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.libriciel.Atteste.BDD.certs.Certificat;
import com.libriciel.Atteste.BDD.certs.CertificatRepository;
import com.libriciel.Atteste.BDD.mails.Mail;

@Service
public class MailHandler {

	@Autowired
	public CertificatRepository cr;
	
	@Autowired
	public JavaMailSender mailSender;
	
	public void sendMail(Notification n, Mail m) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(m.getAdresse());
		message.setSubject(n.getObjet());
		message.setText(n.getMessage());
		
		mailSender.send(message);
	}
	
	public void sendMailTest() {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("tilopapin@gmail.com");
		message.setSubject("test");
		message.setText("get spammed ?");
		
		mailSender.send(message);
	}
	
	public List<Certificat> certificatesToNotify(){
		List<Certificat> allCerts = cr.findAll();
		List<Certificat> res = new ArrayList<Certificat>();
		
		for(int j = 0; j < allCerts.size(); j++) {
			if((this.isExpired(allCerts.get(j)) 
				|| this.isOrange(allCerts.get(j)) 
				|| this.isRed(allCerts.get(j)))
				&& !allCerts.get(j).getNotified()) {
				
				res.add(allCerts.get(j));
			}
		}
		return res;
	}
	
	public int[] getRem(LocalDate after) {
		LocalDate now = LocalDate.now();
		int[] res = new int[3];
		
		if(after.isAfter(now)) {
			after = after.minus(Period.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth()));
			res[0] = after.getYear();
			res[1] = after.getMonthValue();
			res[2] = after.getDayOfMonth();
			if(res[0] == -1) {
				res[0] = 0;
				res[1] -= 12;
			}
			return res;
		}else {
			return null;
		}
	}
	 
	public boolean isExpired(Certificat c) {
		int[] rem = this.getRem(c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		
		if(rem != null) {
			return false;
		}else {
			return true;
		}
	}
	
	public boolean isOrange(Certificat c) {
		int[] rem = this.getRem(c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		
		if(rem != null) {
			if(rem[0] == 0 && rem[1] <= 3) {
				if(rem[1] >= 1) {
					return true;
				}else {
					return false;
				}
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	public boolean isRed(Certificat c) {
		int[] rem = this.getRem(c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		
		if(rem != null) {
			if(rem[0] == 0 && rem[1] == 0) {
				return true;
			}else {
				return false;
			}
		}else {
			return false;
		}
	}
	
	public boolean isGreen(Certificat c) {
		return !this.isOrange(c) && !this.isRed(c);
	}
	
	public String getCode(Certificat c){
		if(this.isExpired(c)) {
			return "EXPIRED";
		}else if(this.isRed(c)) {
			return "RED";
		}else if(this.isOrange(c)) {
			return "ORANGE";
		}else if(this.isGreen(c)){
			return "GREEN";
		}else {
			return "";
		}
	}
	
	//à remplacer par un CRON fonctionnel
	//@Scheduled(fixedRate = 5000)
	@Scheduled( cron = "0 50 17 * * ?")
	public void sendMailsToAll() {
		System.out.println("tick");
		
		List<Certificat> certs = this.certificatesToNotify();
		
		for(int i = 0; i < certs.size(); i++) {
			if(certs.get(i).isNotifyAll()) {
				for(int j = 0; j < certs.get(i).getAdditionnalMails().size(); j++) {
					this.sendMail(new Notification(certs.get(i), this.getCode(certs.get(i))), certs.get(i).getAdditionnalMails().get(j));
					System.out.println("A mail was send");
				}
			}else {
				for(int j = 0; j < certs.get(i).getAdditionnalMails().size(); j++) {
					if(certs.get(i).getAdditionnalMails().get(j).isNotifiable()) {
						this.sendMail(new Notification(certs.get(i), this.getCode(certs.get(i))), certs.get(i).getAdditionnalMails().get(j));
						System.out.println("A mail was send");
					}
				}
			}
			
			certs.get(i).setNotified(true);
			this.cr.save(certs.get(i));
		}
	}
}