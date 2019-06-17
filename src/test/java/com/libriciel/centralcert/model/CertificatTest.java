package com.libriciel.centralcert.model;

import static org.junit.Assert.*;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.libriciel.centralcert.model.Certificat;
import com.libriciel.centralcert.model.Mail;
import com.libriciel.centralcert.service.CertificatService;

public class CertificatTest {

	@Before
	public void before() {
		System.out.println("New Test :---------->");
	}
	
	@After
	public void after() {
		System.out.println("OK");
		System.out.println("End of test <---:");
	}

	@Test
	public void testConstructeurVide() {
		System.out.println("Test du constructeur vide :");
		
		Certificat c = new Certificat();
		assertNull(c.getNotAfter());
		assertNull(c.getNotBefore());
		assertFalse(c.isFavoris());
		assertNull(c.getDN());
		assertFalse(c.isNotifyAll());
		assertEquals("GREEN", c.getNotified());
		assertEquals(c.getAdditionnalMails(), new ArrayList<>());
	}
	
	@Test
	public void testConstructeurX509() {
		System.out.println("Test du constructeur vide :");
		X509Certificate c1 = CertificatService.getCertificateFromURL("https://www.libriciel.fr/")[0];
		Certificat c = new Certificat(c1);
		assertEquals(c1.getNotAfter(), c.getNotAfter());
		assertEquals(c1.getNotBefore(), c.getNotBefore());
		assertFalse(c.isFavoris());
		assertEquals(c1.getSubjectX500Principal().getName(), c.getDN());
		assertFalse(c.isNotifyAll());
		assertEquals("GREEN", c.getNotified());
		assertEquals(c.getAdditionnalMails(), new ArrayList<>());
	}
	
	@Test
	public void testId() {
		Certificat c = new Certificat();
		c.setId(200);
		assertEquals(200, c.getId());
	}
	
	@Test
	public void testNB() {
		Certificat c = new Certificat();
		c.setNotBefore(new Date());
		assertEquals(new Date(), c.getNotBefore());
	}
	
	@Test
	public void testNA() {
		Certificat c = new Certificat();
		c.setNotAfter(new Date());
		assertEquals(new Date(), c.getNotAfter());
	}
	
	@Test
	public void testDN() {
		Certificat c = new Certificat();
		c.setDN("test");
		assertEquals("test", c.getDN());
	}
	
	@Test
	public void testAddMail() {
		Certificat c = new Certificat();
		c.setAdditionnalMails(new ArrayList());
		assertEquals(new ArrayList(), c.getAdditionnalMails());
	}
	
	@Test
	public void testFavoris() {
		Certificat c = new Certificat();
		c.setFavoris(true);
		assertTrue(c.isFavoris());
	}
	
	@Test
	public void testNotified() {
		Certificat c = new Certificat();
		c.setNotified("ORANGE");
		assertEquals("ORANGE", c.getNotified());
	}
	
	@Test
	public void addMail() {
		Certificat c = new Certificat();
		Mail m = new Mail();
		assertTrue(c.getAdditionnalMails().isEmpty());
		c.addMail(m);
		assertFalse(c.getAdditionnalMails().isEmpty());
	}
	
	@Test
	public void addMails() {
		Certificat c = new Certificat();
		Mail m1 = new Mail();
		Mail m2 = new Mail();
		List<Mail> lm = new ArrayList<>();
		lm.add(m1);
		lm.add(m2);
		assertTrue(c.getAdditionnalMails().isEmpty());
		c.addMails(lm);
		assertEquals(2, c.getAdditionnalMails().size());
	}
	
	@Test
	public void testNotifyAll() {
		Certificat c = new Certificat();
		assertFalse(c.isNotifyAll());
		c.setNotifyAll(true);
		assertTrue(c.isNotifyAll());
	}
}
