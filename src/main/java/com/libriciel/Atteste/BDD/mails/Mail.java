/*
 * 
 */
package com.libriciel.Atteste.BDD.mails;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.libriciel.Atteste.BDD.certs.Certificat;

/**
 * The Class Mail.
 */
@Entity
@Table(name = "mails")
public class Mail {
	
	/** The mail id. */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int mailId;
	
	/** The adresse mail. */
	@Column(name = "adresseMail")
	private String adresseMail;
	
	/** The certificates. */
	@ManyToMany(mappedBy = "mails")
	@JsonIgnore
	private List<Certificat> certificates = new ArrayList<Certificat>();
	
	/**
	 * Instantiates a new mail.
	 *
	 * @param adresseMail the adresse mail
	 */
	public Mail(String adresseMail) {
		this.adresseMail = adresseMail;
	}
	
	/**
	 * Instantiates a new mail.
	 *
	 * @param mailId the mail id
	 * @param adresseMail the adresse mail
	 */
	public Mail(int mailId, String adresseMail) {
		this.mailId = mailId;
		this.adresseMail = adresseMail;
	}
	
	/**
	 * Instantiates a new mail.
	 */
	public Mail() {
		this.adresseMail = null;
	}
	
	/**
	 * Gets the certificats.
	 *
	 * @return the certificats
	 */
	public List<Certificat> getCertificats() {
		return this.certificates;
	}
	
	/**
	 * Gets the adresse mail.
	 *
	 * @return the adresse mail
	 */
	public String getAdresseMail() {
		return this.adresseMail;
	}
	
	public void addCertificat(Certificat c) {
		this.certificates.add(c);
	}
	
	public void addCertificats(List<Certificat> cl) {
		this.certificates.addAll(cl);
	}
	
	public int getId() {
		return this.mailId;
	}
}
