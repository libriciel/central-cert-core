package com.libriciel.Atteste.BDD.mails;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Mail {
	//attributes
	@NotNull
	@Size(max=100)
	private String adresse;
	
	@NotNull
	private boolean notifiable;
	
	//constructors
	public Mail() {
		this.adresse = "";
		this.notifiable = true;
	}
	
	public Mail(String adresse) {
		this.adresse = adresse;
		this.notifiable = true;
	}
	
	public Mail(String adresse, boolean notifiable) {
		this.adresse = adresse;
		this.notifiable = notifiable;
	}
	
	//getters
	public String getAdresse() {
		return adresse;
	}
	
	public boolean isNotifiable() {
		return notifiable;
	}
	
	//setters
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	public void setNotifiable(boolean notifiable) {
		this.notifiable = notifiable;
	}
}
