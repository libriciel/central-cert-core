package com.libriciel.centralcert.model;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author tpapin
 * 
 * Classe définissant un mail
 */
@Embeddable
public class Mail {
	
	/** 
	 * L'adresse mail
	 * ne peut pas être null
	 */
	@NotNull
	@Size(max=100)
	private String adresse;
	
	/**
	 * Un booléen permettant de svaoir si l'adresse doit être contactée par le mailing automatique ou non
	 * 
	 * ne peut pas être null
	 */
	@NotNull
	private boolean notifiable;
	
	/**
	 * Instantiates a new mail.
	 */
	public Mail() {
		this.adresse = "";
		this.notifiable = true;
	}
	
	/**
	 * Instantiates a new mail.
	 *
	 * @param adresse the adresse
	 */
	public Mail(String adresse) {
		this.adresse = adresse;
		this.notifiable = true;
	}
	
	/**
	 * Cérer un Mail
	 *
	 * @param adresse l'adresse
	 * @param notifiable un booléen
	 */
	public Mail(String adresse, boolean notifiable) {
		this.adresse = adresse;
		this.notifiable = notifiable;
	}
	
	/**
	 * Gets the adresse.
	 *
	 * @return the adresse
	 */
	public String getAdresse() {
		return adresse;
	}
	
	/**
	 * Checks if is notifiable.
	 *
	 * @return true, if is notifiable
	 */
	public boolean isNotifiable() {
		return notifiable;
	}
	
	/**
	 * Sets the adresse.
	 *
	 * @param adresse the new adresse
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	/**
	 * Sets the notifiable.
	 *
	 * @param notifiable the new notifiable
	 */
	public void setNotifiable(boolean notifiable) {
		this.notifiable = notifiable;
	}
}
