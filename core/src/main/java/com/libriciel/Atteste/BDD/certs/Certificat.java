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

	@Column(name = "favoris")
	private boolean favoris;
		
	@Column(name = "notifyAll")
	private boolean notifyAll;
	
	@Column(name = "dn")
	private String dn;
	
	@OneToMany(mappedBy = "certificat")
	private List<Notification> notifications = new ArrayList<Notification>();
	
	@ElementCollection
	@Column(name = "additionnalMails")
	private List<Mail> additionnalMails = new ArrayList<Mail>();
	
	//constructors
	public Certificat() {
		this.notBefore = null;
		this.notAfter = null;
		this.favoris = false;
		this.dn = null;
		this.notifyAll = false;
		this.additionnalMails = new ArrayList<Mail>();
		this.notifications = new ArrayList<Notification>();
	}

	public Certificat(X509Certificate cert) {
		this.notBefore = cert.getNotBefore();
		this.notAfter = cert.getNotAfter();
		this.favoris = false;
		this.dn = cert.getSubjectX500Principal().getName();
		this.notifyAll = false;
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

	public String getDN() {
		return this.dn;
	}

	public List<Mail> getAdditionnalMails(){
		return this.additionnalMails;
	}
	
	public List<Notification> getNotifications(){
		return this.notifications;
	}
	
	public boolean isFavoris() {
		return this.favoris;
	}
	
	public boolean isNotifyAll() {
		return this.notifyAll;
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

	public void setDN(String dn) {
		this.dn = dn;
	}

	public void setAdditionnalMails(List<Mail> lm) {
		this.additionnalMails = lm;
	}
	
	public void setNotifications(List<Notification> ln) {
		this.notifications = ln;
	}
	
	public void setFavoris(boolean favoris) {
		this.favoris = favoris;
	}
	
	public void setNotifyAll(boolean notifyAll) {
		this.notifyAll = notifyAll;
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