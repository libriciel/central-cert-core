/* Classe définissant un certificat
 *
 */
package com.libriciel.Atteste.BDD.certs;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.libriciel.Atteste.BDD.mails.Mail;
import com.libriciel.Atteste.BDD.notifs.Notification;

@Entity
@Table(name = "certificats")
public class Certificat {
	//attributes
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int certificatId;

	@Column(name = "notBefore")
	private Date notBefore;

	@Column(name = "notAfter")
	private Date notAfter;

	@Column(name= "DN")
	private DistinguishedNumber DN;

	@OneToMany(mappedBy = "certificat")
	private List<Notification> notifications = new ArrayList<Notification>();
	
	@ElementCollection
	@Column(name = "additionnalMails")
	private List<Mail> additionnalMails = new ArrayList<Mail>();

	//constructors
	public Certificat() {
		this.notBefore = null;
		this.notAfter = null;
		this.DN = null;
		this.additionnalMails = new ArrayList<Mail>();
		this.notifications = new ArrayList<Notification>();
	}

	public Certificat(X509Certificate cert) {
		this.notBefore = cert.getNotBefore();
		this.notAfter = cert.getNotAfter();
		this.DN = new DistinguishedNumber(cert.getSubjectX500Principal().getName());
		this.additionnalMails = new ArrayList<Mail>();
		this.notifications = new ArrayList<Notification>();
	}

	//getters
	public Date getNotBefore() {
		return this.notBefore;
	}

	public Date getNotAfter() {
		return this.notAfter;
	}

	public int getId() {
		return this.certificatId;
	}

	public DistinguishedNumber getDN() {
		return this.DN;
	}

	public List<Mail> getAdditionnalMails(){
		return this.additionnalMails;
	}
	
	public List<Notification> getNotifications(){
		return this.notifications;
	}

	//setters
	public void setId(int id) {
		this.certificatId = id;
	}

	public void setNotBefore(Date d) {
		this.notBefore = d;
	}

	public void setNotAfter(Date d) {
		this.notAfter = d;
	}

	public void setDN(DistinguishedNumber DN) {
		this.DN = DN;
	}

	public void setAdditionnalMails(List<Mail> lm) {
		this.additionnalMails = lm;
	}
	
	public void setNotifications(List<Notification> ln) {
		this.notifications = ln;
	}

	//methods
	public void addMail(Mail m) {
		this.additionnalMails.add(m);
	}

	public void addMails(List<Mail> lm) {
		this.additionnalMails.addAll(lm);
	}
	
	public void addNotification(Notification n) {
		this.notifications.add(n);
	}
	
	public void addNotifications(List<Notification> ln) {
		this.notifications.addAll(ln);
	}
}