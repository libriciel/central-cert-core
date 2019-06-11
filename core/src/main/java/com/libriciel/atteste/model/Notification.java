package com.libriciel.atteste.model;

import java.time.LocalDate;
import java.time.ZoneId;
/**
 * @author tpapin
 * 
 * Classe définissant une notification
 */
public class Notification {
	private static final String VOTRECERTIFICAT = "Votre certificat \"";

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
			
			//Pour chaque information du distinguished number
			for(int i = 0; i < dn.length; i++) {
				//On vérifie s'il s'agit du Common Name
				if(dn[i].startsWith("CN=")) {
					//Si oui on le stocke dans une variable précédemment déclarée
					cn = dn[i].substring(3);
				}
			}
			
			//On créer le message
			StringBuilder sb = new StringBuilder();
			sb.append("cn :" + cn + " \n");
			sb.append("Not before :" + nb.toString() + " \n");
			sb.append("Not after :" + na.toString() + " \n");

			for(int i = 0; i < dn.length; i++) {
				sb.append(dn[i] + " \n");
			}
			sb.append(" \n");
			sb.append("Si vous ne souhaitez pas recevoir de mails pour ce certificat, rendez-vous sur ce lien : \n");

			//On instantie l'attribut message de la classe avec le message précédemment créé
			this.message = sb.toString();
			this.setObjAndMess(code, cn);
		}else {
			this.objet = "null";
			this.message = "null";
		}
	}
	
	private void setObjAndMess(String code, String cn) {
		if(code.equals("EXPIRED")) { //Si certificat expiré
			if(cn.equals("")) {
				this.objet = "Un de vos certificats à expiré";
			}else {
				this.objet = VOTRECERTIFICAT + cn + "\" a expiré";
			}
		}else if(code.equals("RED")) { //Si code rouge
			if(cn.equals("")) {
				this.objet = "Un de vos certificats expire bientôt";
			}else {
				this.objet = VOTRECERTIFICAT + cn + "\" expire bientôt";
			}
		}else if(code.equals("ORANGE")){ //si code orange
			if(cn.equals("")) {
				this.objet = "Un de vos certificats arrive à expiration";
			}else {
				this.objet = VOTRECERTIFICAT + cn + "\" arrive à expiration";
			}
		}else { // sinon (si code vert)
			this.objet = null;
			this.message = null;
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
