package com.libriciel.centralcert.model;

import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Classe Certificat
 *
 * @author tpapin
 * Classe définissant un certificat
 * Implémentation dans la base de données via @Entity et @Table
 */
@Entity
@Table(name = "certificats")
public class Certificat {
	
	private static final String GREEN = "GREEN";
	
	/** 
	 * L'id du certificat
	 * Permet de différencier les certificats dans la base de données  
	 * Généré automatiquement à la création du certificat par spring
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int certificatId;

	/** 
	 * Date de mise en place du certificat
	 */
	@Column(name = "notBefore")
	private Date notBefore;

	/**
	 * Date de fin de validité du certificat
	 */
	@Column(name = "notAfter")
	private Date notAfter;

	/**
	 * Booléen qui dit si oui ou non le certificat est mis en favoris
	 */
	@Column(name = "favoris")
	private boolean favoris;
		
	/**
	 * Booléen qui dit si l'on doit notifier toutes les adresses du certificat ou non
	 */
	@Column(name = "notifyAll")
	private boolean notifyAll;
	
	/**
	 * Distinguished number du certificat
	 * Un string qui possède les détails d'un certificat
	 */
	@Column(name = "dn")
	private String dn;
	
	/**
	 * Permet de savoir quelle est la dernière notification envoyée
	 * 
	 *  GREEN si pas de notifications
	 *  ORANGE si notification ORANGE envoyée
	 *  RED si notification RED envoyée
	 *  EXPIRED si toutes notifications envoyées
	 */
	@Column(name = "notified")
	private String notified;
	
	/**
	 * Liste de mails ajoutés par l'utilisateurs
	 * Ce sont des adresses contactées par le mailing automatique
	 */
	@Embedded
	@ElementCollection(fetch = FetchType.EAGER)
	@Column(name = "additionnalMails")
	private List<Mail> additionnalMails;
	
	/**
	 * Créer un certificat nul
	 * 
	 * utilisé pour les tests
	 */
	public Certificat() {
		this.notBefore = null;
		this.notAfter = null;
		this.favoris = false;
		this.dn = null;
		this.notifyAll = false;
		this.notified = GREEN;
		this.additionnalMails = new ArrayList<>();
	}

	/**
	 * Créer un certificat à artir d'un objet X509Certificate
	 *
	 * @param cert un certificat X509
	 */
	public Certificat(X509Certificate cert) {
		this.notBefore = cert.getNotBefore();
		this.notAfter = cert.getNotAfter();
		this.favoris = false;
		this.dn = cert.getSubjectX500Principal().getName();
		this.notifyAll = false;
		this.notified = GREEN;
		this.additionnalMails = new ArrayList<>();
	}
	
	/**
	 * Créer un certificat avec deux dates (not before et not after)
	 * 
	 * utilisé pour les tests
	 *
	 * @param nb the nb
	 * @param na the na
	 */
	public Certificat (LocalDate nb, LocalDate na) {
		this.notBefore = Date.from(nb.atStartOfDay(ZoneId.systemDefault()).toInstant());
		this.notAfter = Date.from(na.atStartOfDay(ZoneId.systemDefault()).toInstant());
		this.favoris = false;
		this.dn = null;
		this.notifyAll = false;
		this.notified = GREEN;
		this.additionnalMails = new ArrayList<>();
	}

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
	 * Gets the dn.
	 *
	 * @return the dn
	 */
	public String getDN() {
		return this.dn;
	}

	/**
	 * Gets the additionnal mails.
	 *
	 * @return the additionnal mails
	 */
	public List<Mail> getAdditionnalMails(){
		return this.additionnalMails;
	}
	
	/**
	 * Checks if is favoris.
	 *
	 * @return true, if is favoris
	 */
	public boolean isFavoris() {
		return this.favoris;
	}
	
	/**
	 * Checks if is notify all.
	 *
	 * @return true, if is notify all
	 */
	public boolean isNotifyAll() {
		return this.notifyAll;
	}
	
	/**
	 * Gets the notified.
	 *
	 * @return the notified
	 */
	public String getNotified() {
		return this.notified;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(int id) {
		this.certificatId = id;
	}

	/**
	 * Sets the not before.
	 *
	 * @param d the new not before
	 */
	public void setNotBefore(Date d) {
		this.notBefore = d;
	}

	/**
	 * Sets the not after.
	 *
	 * @param d the new not after
	 */
	public void setNotAfter(Date d) {
		this.notAfter = d;
	}

	/**
	 * Sets the dn.
	 *
	 * @param dn the new dn
	 */
	public void setDN(String dn) {
		this.dn = dn;
	}

	/**
	 * Sets the additionnal mails.
	 *
	 * @param lm the new additionnal mails
	 */
	public void setAdditionnalMails(List<Mail> lm) {
		this.additionnalMails = lm;
	}
	
	/**
	 * Sets the favoris.
	 *
	 * @param favoris the new favoris
	 */
	public void setFavoris(boolean favoris) {
		this.favoris = favoris;
	}
	
	/**
	 * Sets the notify all.
	 *
	 * @param notifyAll the new notify all
	 */
	public void setNotifyAll(boolean notifyAll) {
		this.notifyAll = notifyAll;
	}
	
	/**
	 * Sets the notified.
	 *
	 * @param notified the new notified
	 */
	public void setNotified(String notified) {
		this.notified = notified;
	}

	/**
	 * Adds the mail.
	 *
	 * @param m the m
	 */
	public void addMail(Mail m) {
		this.additionnalMails.add(m);
	}

	/**
	 * Adds the mails.
	 *
	 * @param lm the lm
	 */
	public void addMails(List<Mail> lm) {
		this.additionnalMails.addAll(lm);
	}
}