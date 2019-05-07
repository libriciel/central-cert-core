package com.libriciel.Atteste.Mails;

import java.time.LocalDate;
import java.time.ZoneId;

import com.libriciel.Atteste.BDD.certs.Certificat;

public class Notification {
	private String objet;
	private String message;
	
	//constructors
	public Notification(Certificat c, String code) {
		if(c != null) {
			LocalDate nb = c.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate na = c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			String[] dn = c.getDN().split(",");
			String cn = "";
			String mess = "";
			for(int i = 0; i < dn.length; i++) {
				if(dn[i].startsWith("DN=")) {
					cn = dn[i].substring(3);
				}
			}
			mess += "cn :" + cn + " \n";
			mess += "Not before :" + nb.toString() + " \n";
			mess += "Not after :" + na.toString() + " \n";
			for(int i = 0; i < dn.length; i++) {
				mess += dn[i] + " \n";
			}
			this.message = mess;
			if(code == "EXPIRED") {
				if(cn == "") {
					this.objet = "Un de vos certificats à expiré";
				}else {
					this.objet = "Votre certificat \"" + cn + "\" a expiré";
				}
			}else if(code == "RED") {
				if(cn == "") {
					this.objet = "Un de vos certificats expire bientôt";
				}else {
					this.objet = "Votre certificat \"" + cn + "\" expire bientôt";
				}
			}else if(code == "ORANGE"){
				if(cn == "") {
					this.objet = "Un de vos certificats arrive à expiration";
				}else {
					this.objet = "Votre certificat \"" + cn + "\" arrive à expiration";
				}
			}else {
				this.objet = null;
				this.message = null;
			}
		}
	}

	//getters
	public String getObjet() {
		return objet;
	}

	public String getMessage() {
		return message;
	}
	
	//setters
	public void setObjet(String objet) {
		this.objet = objet;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
