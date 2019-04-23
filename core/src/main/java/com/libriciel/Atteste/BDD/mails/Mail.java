package com.libriciel.Atteste.BDD.mails;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Mail {
	//attributes
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String adresse;
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
