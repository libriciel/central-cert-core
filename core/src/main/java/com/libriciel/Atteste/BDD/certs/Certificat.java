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
import javax.persistence.Table;
import javax.security.auth.x500.X500Principal;

import com.libriciel.Atteste.BDD.mails.Mail;

@Entity
@Table(name = "certificats")
public class Certificat {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int certificatId;

	@Column(name = "notBefore")
	private Date notBefore;

	@Column(name = "notAfter")
	private Date notAfter;

	@Column(name= "CN")
	private String CN = "";

	@Column(name= "O")
	private String O = "";

	@Column(name= "OU")
	private String OU = "";

	@Column(name= "L")
	private String L = "";

	@Column(name= "ST")
	private String ST = "";

	@Column(name= "C")
	private String C = "";

	@Column(name= "T")
	private String T = "";

	@Column(name= "DC")
	private String DC = "";

	@Column(name= "STREET")
	private String STREET = "";

	@Column(name= "PC")
	private String PC = "";

	@ElementCollection
	@Column(name = "mails")
	private List<Mail> mails = new ArrayList<Mail>();

	@ElementCollection
	@Column(name = "additionnalMails")
	private List<Mail> additionnalMails = new ArrayList<Mail>();

	//constructors
	public Certificat(Date notBefore, Date notAfter) {
		this.notBefore = notBefore;
		this.notAfter = notAfter;
	}

	public Certificat(int certificatId, Date notBefore, Date notAfter) {
		this.certificatId = certificatId;
		this.notBefore = notBefore;
		this.notAfter = notAfter;
	}

	public Certificat() {
		this.notBefore = null;
		this.notAfter = null;
	}

	public Certificat(X509Certificate cert) {
		this.notBefore = cert.getNotBefore();
		this.notAfter = cert.getNotAfter();
		X500Principal pr = cert.getSubjectX500Principal();
		String[] dn = pr.getName().split(",");
		List<String> l = new ArrayList<String>();
		for(int i = 0; i < dn.length; i++) {
			l.add(dn[i]);
		}
		for(int i = 0; i < l.size(); i++) {
			if(l.get(i).startsWith("CN=")) { //TYPE
				this.CN = l.get(i).substring(3);
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.3=")) { // OID
				this.CN = l.get(i).substring(8);
				l.remove(i);
			}else if(l.get(i).startsWith("E=")) { //TYPE
				this.additionnalMails.add(l.get(i).substring(2));
				l.remove(i);
			}else if(l.get(i).startsWith("MAIL=")) { //TYPE
				this.additionnalMails.add(l.get(i).substring(5));
				l.remove(i);
			}else if(l.get(i).startsWith("1.2.840.113549.1.9.1=")) { // OID
				this.additionnalMails.add(l.get(i).substring(21));
				l.remove(i);
			}else if(l.get(i).startsWith("O=")) { //TYPE
				this.O = l.get(i).substring(2);
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.10=")) { //OID
				this.O = l.get(i).substring(9);
				l.remove(i);
			}else if(l.get(i).startsWith("OU=")) { //TYPE
				this.OU = l.get(i).substring(3);
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.11=")) { //OID
				this.OU = l.get(i).substring(9);
				l.remove(i);
			}else if(l.get(i).startsWith("L=")) { //TYPE
				this.L = l.get(i).substring(2);
				l.remove(i);
			}else if(l.get(i).startsWith("ST=")) { //TYPE
				this.ST = l.get(i).substring(3);
				l.remove(i);
			}else if(l.get(i).startsWith("SP=")) { //TYPE
				this.ST = l.get(i).substring(3);
				l.remove(i);
			}else if(l.get(i).startsWith("S=")) { //TYPE
				this.ST = l.get(i).substring(2);
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.8=")) { //OID
				this.ST = l.get(i).substring(8);
				l.remove(i);
			}else if(l.get(i).startsWith("C=")) { //TYPE
				this.C = l.get(i).substring(2);
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.6=")) { //OID
				this.C = l.get(i).substring(8);
				l.remove(i);
			}else if(l.get(i).startsWith("T=")) { //TYPE
				this.T = l.get(i).substring(2);
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.12=")) { //OID
				this.T = l.get(i).substring(9);
				l.remove(i);
			}else if(l.get(i).startsWith("DC=")) { //TYPE
				this.DC = l.get(i).substring(2);
				l.remove(i);
			}else if(l.get(i).startsWith("0.9.2342.19200300.100.1.25=")) { //OID
				this.DC = l.get(i).substring(26);
				l.remove(i);
			}else if(l.get(i).startsWith("STREET=")) { //TYPE
				this.STREET = l.get(i).substring(2);
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.9=")) { //OID
				this.STREET = l.get(i).substring(8);
				l.remove(i);
			}else if(l.get(i).startsWith("PC=")) { //TYPE
				this.PC = l.get(i).substring(2);
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.17=")) { //OID
				this.PC = l.get(i).substring(9);
				l.remove(i);
			}else {
				System.out.println("Unknown DN attribute : " + l.get(i));
			}
		}
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

	public String getCN() {
		return this.CN;
	}

	public String getO() {
		return this.O;
	}

	public String getOU() {
		return this.OU;
	}

	public String getL() {
		return this.L;
	}

	public String getST() {
		return this.ST;
	}

	public String getC() {
		return this.C;
	}

	public String getT() {
		return this.T;
	}

	public String getDC() {
		return this.DC;
	}

	public String getSTREET() {
		return this.STREET;
	}

	public String getPC() {
		return this.PC;
	}

	public List<String> getMails(){
		return this.mails;
	}

	public List<String> getAdditionnalMails(){
		return this.additionnalMails;
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

	public void setCN(String CN) {
		this.CN = CN;
	}

	public void setO(String O) {
		this.O = O;
	}

	public void setOU(String OU) {
		this.OU = OU;
	}

	public void setL(String L) {
		this.L = L;
	}

	public void setST(String ST) {
		this.ST = ST;
	}

	public void setC(String C) {
		this.C = C;
	}

	public void setT(String T) {
		this.T = T;
	}

	public void setDC(String DC) {
		this.DC = DC;
	}

	public void setSTREET(String STREET) {
		this.STREET = STREET;
	}

	public void setPC(String PC) {
		this.PC = PC;
	}

	public void setMails(List<String> lm) {
		this.mails = lm;
	}

	public void setAdditionnalMails(List<String> lm) {
		this.additionnalMails = lm;
	}

	//methods
	public void addMail(String m) {
		this.additionnalMails.add(m);
	}

	public void addMails(List<String> ml) {
		this.additionnalMails.addAll(ml);
	}
}
