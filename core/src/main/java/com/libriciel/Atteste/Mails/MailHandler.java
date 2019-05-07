package com.libriciel.Atteste.Mails;


import java.time.LocalDate;
import java.time.ZoneId;
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
	
	public int[] getRemTime(Certificat c) {
		int[] res = new int[3];
		
		LocalDate nb = c.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate na = c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		if(na.isAfter(nb) && na.isAfter(LocalDate.now())) {
			int nb_y = nb.getYear();
			int na_y = na.getYear();
			
			int nb_m = nb.getMonthValue() + 1;
			int na_m = na.getMonthValue() + 1;
			
			int nb_d = nb.getDayOfMonth();
			int na_d = na.getDayOfMonth();
			
			int y = na_y - nb_y;
			
			int m = 0;
			if(na_m >= nb_m){
				m = na_m - nb_m;
			}else{
				m = 12 + na_m - nb_m;
			}

			int d = 0;
			if(na_d >= nb_d){
				d = na_d - nb_d;
			}else{
				if(nb_m == 1){
					if(nb.isLeapYear() == true){
						d = 29 + na_d - nb_d;
					}else{
						d = 28 + na_d - nb_d;
					}
				}else if(nb_m == 0
		          || nb_m == 2
		          || nb_m == 4
		          || nb_m == 6
		          || nb_m == 7
		          || nb_m == 9
		          || nb_m == 11){
					d = 31 + na_d - nb_d;
				}else{
					d = 30 + na_d - nb_d;
				}		
			}
			res[0] = y;
			res[1] = m;
			res[2] = d;
		}else {
			res[0] = -1;
			res[1] = -1;
			res[2] = -1;
		}
		
		return res;

	}
	
	public boolean isExpired(Certificat c) {
		int[] rem = this.getRemTime(c);
		
		if(rem[0] != -1 && rem[1] != -1 && rem[2] != -1) {
			return false;
		}else {
			return true;
		}
	}
	
	public boolean isOrange(Certificat c) {
		int[] rem = this.getRemTime(c);
		
		if(rem[0] != -1 && rem[1] != -1 && rem[2] != -1) {
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
		int[] rem = this.getRemTime(c);
		
		if(rem[0] != -1 && rem[1] != -1 && rem[2] != -1) {
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
					sender.send(new Notification(certs.get(i), this.getCode(certs.get(i))), certs.get(i).getAdditionnalMails().get(j));
					System.out.println("A mail was send");
				}
			}
		}
	}
}