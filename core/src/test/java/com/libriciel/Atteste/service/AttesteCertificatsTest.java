package com.libriciel.Atteste.service;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.libriciel.atteste.service.AttesteCertificats;

public class AttesteCertificatsTest {
	
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
	public void testIsValidURL() {
		System.out.println("Test de la méthode isValidURL()");
		assertFalse(AttesteCertificats.isValidURL(""));
		assertFalse(AttesteCertificats.isValidURL("https/github.com/"));
		assertTrue(AttesteCertificats.isValidURL("https://github.com/"));
	}
	
	@Test
	public void testInvalidUrl() {
		assertEquals(0, AttesteCertificats.getCertificateFromURL("https/github.com/").length);
	}
	
	@Test
	public void testGetFromTokenNotNull() {
		File f = new File("src/test/java/com/libriciel/Atteste/service/alice.crt");
		assertNotNull(AttesteCertificats.getCertificateFromToken(f));
	}
	
	@Test
	public void testGetFromTokenNull() {
		File f = new File("src/test/java/com/libriciel/Atteste/service/1571753451.p12");
		assertNull(AttesteCertificats.getCertificateFromToken(f));
	}
}
