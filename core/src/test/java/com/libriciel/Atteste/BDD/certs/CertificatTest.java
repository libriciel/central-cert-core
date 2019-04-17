package com.libriciel.Atteste.BDD.certs;

import static org.junit.Assert.*;

import java.util.Date;

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
	}
	
	@Test
	public void testContructeurNotID() {
		System.out.println("Test du constructeur sans ID :");

		Date d1 = new Date();
		Date d2 = new Date();
		Certificat c = new Certificat(d1, d2);
		
		assertEquals(c.getNotBefore(), d1);
		assertEquals(c.getNotAfter(), d2);
	}
	
	@Test
	public void testConstructeurID() {
		System.out.println("Test du constructeur avec ID :");

		Date d1 = new Date();
		Date d2 = new Date();
		Certificat c = new Certificat(100, d1, d2);
		
		assertEquals(c.getId() , 100);
		assertEquals(c.getNotBefore(), d1);
		assertEquals(c.getNotAfter(), d2);
	}
}
