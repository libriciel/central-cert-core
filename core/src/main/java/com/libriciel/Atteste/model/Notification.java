package com.libriciel.Atteste.model;

import java.time.LocalDate;
import java.time.ZoneId;
/**
 * @author tpapin
 * 
 * Classe définissant une notification
 */
public class Notification {
	
	/**
	 * L'objet de la notification
	 */
	private String objet;
	
	/**
	 * Le corps (message) de la notification
	 */
	private String message;
	
	public Notification() {
		this.objet = null;
		this.message = null;
	}
	
	/**
	 * Créer une notification
	 *
	 * @param c le certificat
	 * @param code le code
	 * 
	 * le code de la notification est : GREEN, ORANGE, RED ou EXPIRED
	 * Il définit le type de notification à envoyer
	 */
	public Notification(Certificat c, String code) {
		if(c != null) {
			//on récupère les dates des certificats
			LocalDate nb = c.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate na = c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			//on sépare les informations du distinguished number afin de former le corps du message
			String[] dn = c.getDN().split(",");
			
			//on instantie des variables
			//Common Name (sujet) du certificat
			String cn = "";
			
			//Corps du message
			String mess = "";
			
			//Pour chaque information du distinguished number
			for(int i = 0; i < dn.length; i++) {
				//On vérifie s'il s'agit du Common Name
				if(dn[i].startsWith("CN=")) {
					//Si oui on le stocke dans une variable précédemment déclarée
					cn = dn[i].substring(3);
				}
			}
			
			//On créer le message
			mess += "cn :" + cn + " \n";
			mess += "Not before :" + nb.toString() + " \n";
			mess += "Not after :" + na.toString() + " \n";
			for(int i = 0; i < dn.length; i++) {
				mess += dn[i] + " \n";
			}
			
			mess += " \n";
			mess += "Si vous ne souhaitez pas recevoir de mails pour ce certificat, rendez-vous sur ce lien : \n";
			
			//On instantie l'attribut message de la classe avec le message précédemment créé
			this.message = mess;
			
			//En fonction du code
			if(code == "EXPIRED") { //Si certificat expiré
				if(cn == "") {
					this.objet = "Un de vos certificats à expiré";
				}else {
					this.objet = "Votre certificat \"" + cn + "\" a expiré";
				}
			}else if(code == "RED") { //Si code rouge
				if(cn == "") {
					this.objet = "Un de vos certificats expire bientôt";
				}else {
					this.objet = "Votre certificat \"" + cn + "\" expire bientôt";
				}
			}else if(code == "ORANGE"){ //si code orange
				if(cn == "") {
					this.objet = "Un de vos certificats arrive à expiration";
				}else {
					this.objet = "Votre certificat \"" + cn + "\" arrive à expiration";
				}
			}else { // sinon (si code vert)
				this.objet = null;
				this.message = null;
			}
		}
	}

	/**
	 * Gets the objet.
	 *
	 * @return the objet
	 */
	public String getObjet() {
		return objet;
	}

	/**
	 * Gets the message.
	 *
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}
	
	/**
	 * Sets the objet.
	 *
	 * @param objet the new objet
	 */
	public void setObjet(String objet) {
		this.objet = objet;
	}

	/**
	 * Sets the message.
	 *
	 * @param message the new message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
}
