package com.libriciel.Atteste.BDD.mails;

public class Mail {
	private String adresse;
	private boolean notifiable;
	
	public String getAdresse() {
		return adresse;
	}
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	public boolean isNotifiable() {
		return notifiable;
	}
	public void setNotifiable(boolean notifiable) {
		this.notifiable = notifiable;
	}
}
