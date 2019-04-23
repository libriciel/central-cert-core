package com.libriciel.Atteste.BDD.certs;

import java.util.ArrayList;
import java.util.List;

public class DistinguishedNumber {
	
	private String cn = "pas d'informations";
	private String mail = "pas d'informations";
	private String o = "pas d'informations";
	private String ou = "pas d'informations";
	private String l = "pas d'informations";
	private String st = "pas d'informations";
	private String c = "pas d'informations";
	private String t = "pas d'informations";
	private String dc = "pas d'informations";
	private String street = "pas d'informations";
	private String pc = "pas d'informations";

	public DistinguishedNumber(String str) {

		String[] dn = str.split(",");
		List<String> l = new ArrayList<String>();
		for(int i = 0; i < dn.length; i++) {
			l.add(dn[i]);
		}
		
		for(int i = 0; i < l.size(); i++) {
			if(l.get(i).startsWith("CN=")) { //TYPE
				this.setCn(l.get(i).substring(3));
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.3=")) { // OID
				this.setCn(l.get(i).substring(8));
				l.remove(i);
			}else if(l.get(i).startsWith("E=")) { //TYPE
				this.setMail(l.get(i).substring(2));
				l.remove(i);
			}else if(l.get(i).startsWith("MAIL=")) { //TYPE
				this.setMail(l.get(i).substring(5));
				l.remove(i);
			}else if(l.get(i).startsWith("1.2.840.113549.1.9.1=")) { // OID
				this.setMail(l.get(i).substring(21));
				l.remove(i);
			}else if(l.get(i).startsWith("O=")) { //TYPE
				this.setO(l.get(i).substring(2));
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.10=")) { //OID
				this.setO(l.get(i).substring(9));
				l.remove(i);
			}else if(l.get(i).startsWith("OU=")) { //TYPE
				this.setOu(l.get(i).substring(3));
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.11=")) { //OID
				this.setOu(l.get(i).substring(9));
				l.remove(i);
			}else if(l.get(i).startsWith("L=")) { //TYPE
				this.setL(l.get(i).substring(2));
				l.remove(i);
			}else if(l.get(i).startsWith("ST=")) { //TYPE
				this.setSt(l.get(i).substring(3));
				l.remove(i);
			}else if(l.get(i).startsWith("SP=")) { //TYPE
				this.setSt(l.get(i).substring(3));
				l.remove(i);
			}else if(l.get(i).startsWith("S=")) { //TYPE
				this.setSt(l.get(i).substring(2));
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.8=")) { //OID
				this.setSt(l.get(i).substring(8));
				l.remove(i);
			}else if(l.get(i).startsWith("C=")) { //TYPE
				this.setC(l.get(i).substring(2));
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.6=")) { //OID
				this.setC(l.get(i).substring(8));
				l.remove(i);
			}else if(l.get(i).startsWith("T=")) { //TYPE
				this.setT(l.get(i).substring(2));
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.12=")) { //OID
				this.setT(l.get(i).substring(9));
				l.remove(i);
			}else if(l.get(i).startsWith("DC=")) { //TYPE
				this.setDc(l.get(i).substring(2));
				l.remove(i);
			}else if(l.get(i).startsWith("0.9.2342.19200300.100.1.25=")) { //OID
				this.setDc(l.get(i).substring(26));
				l.remove(i);
			}else if(l.get(i).startsWith("STREET=")) { //TYPE
				this.setStreet(l.get(i).substring(2));
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.9=")) { //OID
				this.setStreet(l.get(i).substring(8));
				l.remove(i);
			}else if(l.get(i).startsWith("PC=")) { //TYPE
				this.setPc(l.get(i).substring(2));
				l.remove(i);
			}else if(l.get(i).startsWith("2.5.4.17=")) { //OID
				this.setPc(l.get(i).substring(9));
				l.remove(i);
			}
		}
	}

	public String getCn() {
		return cn;
	}

	public void setCn(String cn) {
		this.cn = cn;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getO() {
		return o;
	}

	public void setO(String o) {
		this.o = o;
	}

	public String getOu() {
		return ou;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}

	public String getSt() {
		return st;
	}

	public void setSt(String st) {
		this.st = st;
	}

	public String getL() {
		return l;
	}

	public void setL(String l) {
		this.l = l;
	}

	public String getC() {
		return c;
	}

	public void setC(String c) {
		this.c = c;
	}

	public String getT() {
		return t;
	}

	public void setT(String t) {
		this.t = t;
	}

	public String getDc() {
		return dc;
	}

	public void setDc(String dc) {
		this.dc = dc;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPc() {
		return pc;
	}

	public void setPc(String pc) {
		this.pc = pc;
	}
}
	
	