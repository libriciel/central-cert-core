package com.libriciel.centralcert.service;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.libriciel.centralcert.service.CertificatService;

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
		assertFalse(CertificatService.isValidURL(""));
		assertFalse(CertificatService.isValidURL("https/github.com/"));
		assertTrue(CertificatService.isValidURL("https://github.com/"));
	}
	
	@Test
	public void testInvalidUrl() {
		assertEquals(0, CertificatService.getCertificateFromURL("https/github.com/").length);
	}
}
