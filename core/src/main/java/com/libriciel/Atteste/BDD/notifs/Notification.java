package com.libriciel.Atteste.BDD.notifs;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.libriciel.Atteste.BDD.certs.Certificat;

@Entity
public class Notification {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "certificat")
	private Certificat certificat;
	
	private String objet;
	private String message;
	private Date notBefore;
	private boolean activated;
	private boolean seen;
	
	public Notification() {
		this.setObjet("");
		this.setMessage("");
		this.notBefore = null;
		this.setActivated(false);
		this.setSeen(false);
	}
	
	public Notification(String objet, String message) {
		this.setObjet(objet);
		this.setMessage(message);
		this.notBefore = null;
		this.setActivated(false);
		this.setSeen(false);
	}

	//getters
	public int getId() {
		return this.id;
	}
	
	public String getObjet() {
		return objet;
	}

	public String getMessage() {
		return message;
	}
	
	public boolean isActivated() {
		return activated;
	}
	
	public boolean isSeen() {
		return seen;
	}

	public Date getNotBefore() {
		return notBefore;
	}
	
	public Certificat getCertificat() {
		return this.certificat;
	}
	
	//setters
	public void setId(int id) {
		this.id = id;
	}
	
	public void setObjet(String objet) {
		this.objet = objet;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}

	public void setActivated(boolean activated) {
		this.activated = activated;
	}

	public void setSeen(boolean seen) {
		this.seen = seen;
	}

	public void setNotBefore(Date notBefore) {
		this.notBefore = notBefore;
	}
}
