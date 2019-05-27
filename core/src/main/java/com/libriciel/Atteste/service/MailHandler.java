package com.libriciel.Atteste.service;


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

import com.libriciel.Atteste.model.Certificat;
import com.libriciel.Atteste.model.Mail;
import com.libriciel.Atteste.model.Notification;
import com.libriciel.Atteste.repository.CertificatRepository;

/**
 * Classe permettant de gérer l'envoi de mails
 */
@Service
public class MailHandler {

	/**
	 * Repository permettant d'accéder à la base de donénes
	 */
	@Autowired
	public CertificatRepository cr;
	
	/**
	 * Interface permettant d'envoyer des mails
	 */
	@Autowired
	public JavaMailSender mailSender;
	
	/**
	 * Permet d'envoyer un mail
	 *
	 * @param n la notification
	 * @param m le mail
	 * 
	 * envoit un mail avec le sinformations de la notification à l'adresse du mail si le mail est notifiable
	 */
	public void sendMail(Notification n, Mail m) {
		//Création d'un message mail
		SimpleMailMessage message = new SimpleMailMessage();
		
		//instantiation du destinataire
		message.setTo(m.getAdresse());
		
		//instantiation de l'objet
		message.setSubject(n.getObjet());
		
		//instantiation du message
		message.setText(n.getMessage());
		
		//envoi du mail
		mailSender.send(message);
	}
	
	/**
	 * Vérifie si l'on doit notifier le mail avec un code donné
	 *
	 * @param c le certificat
	 * @param code le code
	 * @return true, si l'on doit notifier l'adresse
	 */
	public static boolean canNotify(Certificat c, String code){
		String certCode = c.getNotified();
		
		if(code == "ORANGE") {
			if(certCode != "GREEN") {
				return false;
			}else {
				return true;
			}
		}else if(code == "RED") {
			if(certCode != "ORANGE") {
				return false;
			}else {
				return true;
			}
		}else if(code == "EXPIRED"){
			if(certCode != "RED") {
				return false;
			}else {
				return true;
			}
		}else {
			return false;
		}
	}
	
	/**
	 * Renvoit la liste des certificats à qui l'on doit envoyer des mails
	 *
	 * @return la liste de certificats
	 */
	public List<Certificat> certificatesToNotify(){
		
		//récupère tous les certificats de la base de donénes
		List<Certificat> allCerts = cr.findAll();
	
		//On instantie la liste résultat
		List<Certificat> res = new ArrayList<Certificat>();
		
		//pour chaque certificat
		for(int j = 0; j < allCerts.size(); j++) {
			
			//si le certifica est expiré, qu'il est en code rouge ou orange
			if((this.isExpired(allCerts.get(j)) 
				|| this.isOrange(allCerts.get(j)) 
				|| this.isRed(allCerts.get(j)))) {
				//si l'on doit notifier le certificat
				if(canNotify(allCerts.get(j), this.getCode(allCerts.get(j)))) {
					//on ajoute le certificat à la liste résultat
					res.add(allCerts.get(j));
				}
			}
		}
		
		//on renvoit la liste résultat
		return res;
	}
	
	/**
	 * Récupère le temps restant à un certificat avant d'expirer
	 *
	 * @param after the after
	 * @return un tableau sous la forme [années restantes, mois restants, jours restants]
	 */
	public int[] getRem(LocalDate after) {
		//instantiation de la date actuelle
		LocalDate now = LocalDate.now();
		
		//instantiation du tableau résultat
		int[] res = new int[3];
		
		//si le certificat n'est pas expiré
		if(after.isAfter(now)) {
			
			//instantiation de la date d'expiration moins la date actuelle
			after = after.minus(Period.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth()));
			res[0] = after.getYear();
			res[1] = after.getMonthValue();
			res[2] = after.getDayOfMonth();
			
			//correction de bug
			if(res[0] == -1) {
				res[0] = 0;
				res[1] -= 12;
			}
			
			//renvoit du temps restant
			return res;
		}else {
			//si le certificat est expiré
			return null;
		}
	}
	 
	/**
	 * Vérifie si un certificat est expiré ou non
	 *
	 * @param c le certificat
	 * @return true, si le certificat est expiré
	 * @return false, si le certificat n'est pas expiré
	 */
	public boolean isExpired(Certificat c) {
		int[] rem = this.getRem(c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
		
		if(rem != null) {
			return false;
		}else {
			return true;
		}
	}
	
	/**
	 * Vérifie si le certificat est en code ORANGE (moins de 3 mois restants)
	 *
	 * @param c le certificat
	 * @return true, si le certificat est en code ORANGE 
	 */
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
	
	/**
	 * Vérifie si le certificat est en code RED (moins d'1 mois restants)
	 *
	 * @param c le certificat
	 * @return true, si le certificat est en code RED 
	 */
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
	
	/**
	 * Vérifie si le certificat est en code GREEN (plus de 3 mois restants)
	 *
	 * @param c le certificat
	 * @return true, si le certificat est en code GREEN 
	 */
	public boolean isGreen(Certificat c) {
		return !this.isOrange(c) && !this.isRed(c);
	}
	
	/**
	 * Récupère ke code d'un certificat
	 *
	 * @param c le certificat
	 * @return le code
	 */
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
	
	/**
	 * Envoit un mail à chaque adresse devant être notifiée
	 * 
	 * Se lance automatique toutes les deux heures (annotation @scheduled, 7200000 ms = 2 h)
	 */
	@Scheduled(fixedRate = 7200000)
	public void sendMailsToAll() {
		System.out.println("tick");
		
		//instantiation de la liste des certificats à notifier
		List<Certificat> certs = this.certificatesToNotify();
		
		//pour chaque certificat à notifier
		for(int i = 0; i < certs.size(); i++) {
			
			//si toutes les adresses doivent être notifiées
			if(certs.get(i).isNotifyAll()) {
				//pour chaque adresse du certificat
				for(int j = 0; j < certs.get(i).getAdditionnalMails().size(); j++) {
					//on envoit un mail à l'adresse en instantiant une notification avec les données du certificat
					this.sendMail(new Notification(certs.get(i), this.getCode(certs.get(i))), certs.get(i).getAdditionnalMails().get(j));
					System.out.println("A mail was send");
				}
			//sinon
			}else {
				//pour chaque adresse du certificat
				for(int j = 0; j < certs.get(i).getAdditionnalMails().size(); j++) {
					//si l'adresse doit être notifiée
					if(certs.get(i).getAdditionnalMails().get(j).isNotifiable()) {
						//on envoit un mail à l'adresse en instantiant une notification avec les données du certificat
						this.sendMail(new Notification(certs.get(i), this.getCode(certs.get(i))), certs.get(i).getAdditionnalMails().get(j));
						System.out.println("A mail was send");
					}
				}
			}
			
			//on change l'attribut "isNotified" du certificat afin de ne pas envoyer deux fois la même notification
			certs.get(i).setNotified(this.getCode(certs.get(i)));
			
			//on sauvegarde la liste des certificats dans la base de données
			this.cr.save(certs.get(i));
		}
	}
}