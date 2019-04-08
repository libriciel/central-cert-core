/* Classe définissant un certificat
 * 
 */
package com.libriciel.Atteste.BDD.certs;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.libriciel.Atteste.BDD.mails.Mail;

/**
 * Classe Certificat.
 */
@Entity
@Table(name = "certificats")
public class Certificat {
	
	/** ID du certificat
	 * PRIMARY KEY
	 * AUTO INCREMENT
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int certificatId;
	
	/** Date de mise en validité du certificat. */
	@Column(name = "notBefore")
	private Date notBefore;
	
	/** Date de fin de validité du certificat. */
	@Column(name = "notAfter")
	private Date notAfter;
	
	/** Liste des mails du certificat. */
	@ManyToMany
	@JoinTable(name = "carnetadresses",
		joinColumns = { @JoinColumn(name = "certificatId") },
		inverseJoinColumns = { @JoinColumn(name = "mailId") }
	)
	@JsonIgnore
	private List<Mail> mails = new ArrayList<Mail>();
	
	//constructors

	/**
	 * Instantiates a new certificat.
	 *
	 * @param notBefore the not before
	 * @param notAfter the not after
	 */
	public Certificat(Date notBefore, Date notAfter) {
		this.notBefore = notBefore;
		this.notAfter = notAfter;
	}
	
	/**
	 * Instantiates a new certificat.
	 *
	 * @param certificatId the certificat id
	 * @param notBefore the not before
	 * @param notAfter the not after
	 */
	public Certificat(int certificatId, Date notBefore, Date notAfter) {
		this.certificatId = certificatId;
		this.notBefore = notBefore;
		this.notAfter = notAfter;
	}
	
	/**
	 * Instantiates a new certificat.
	 */
	public Certificat() {
		this.notBefore = null;
		this.notAfter = null;
	}
	
	public Certificat(X509Certificate cert) {
		this.notBefore = cert.getNotBefore();
		this.notAfter = cert.getNotAfter();
	}
	
	//getters
	
	/**
	 * Gets the not before.
	 *
	 * @return the not before
	 */
	public Date getNotBefore() {
		return this.notBefore;
	}
	
	/**
	 * Gets the not after.
	 *
	 * @return the not after
	 */
	public Date getNotAfter() {
		return this.notAfter;
	}
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public int getId() {
		return this.certificatId;
	}
	
	/**
	 * Gets the mails.
	 *
	 * @return the mails
	 */
	public List<Mail> getMails(){
		return this.mails;
	}
	
	//methods
	
	/**
	 * Adds the mail.
	 *
	 * @param m the mail
	 */
	public void addMail(Mail m) {
		this.mails.add(m);
	}
	
	public void addMails(List<Mail> ml) {
		this.mails.addAll(ml);
	}
}
