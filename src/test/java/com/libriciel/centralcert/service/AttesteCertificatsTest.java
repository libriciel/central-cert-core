package com.libriciel.centralcert.service;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.libriciel.centralcert.service.AttesteCertificats;

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
}
