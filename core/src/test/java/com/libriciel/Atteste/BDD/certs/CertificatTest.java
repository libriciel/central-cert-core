package com.libriciel.Atteste.BDD.certs;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.libriciel.Atteste.BDD.mails.Mail;


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
		assertFalse(c.getNotified());
		assertEquals(c.getAdditionnalMails(), new ArrayList<Mail>());
	}
}
