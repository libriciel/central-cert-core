package com.libriciel.Atteste.model;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
		assertEquals(c.getNotified(), "GREEN");
		assertEquals(c.getAdditionnalMails(), new ArrayList<Mail>());
	}
}
