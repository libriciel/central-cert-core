package com.libriciel.Atteste.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class MailTest {

	@Test
	public void testConstructeurVide() {
		Mail m = new Mail();
		assertTrue(m.isNotifiable());
		assertEquals(m.getAdresse(), "");
	}

	public void testConstructeurAdresse() {
		Mail m = new Mail("tes@test.test");
		assertTrue(m.isNotifiable());
		assertEquals(m.getAdresse(), "test@test.test");
	}
	
	public void testConstructeurAdresseNotifiable() {
		Mail m = new Mail("test@test.test", false);
		assertFalse(m.isNotifiable());
		assertEquals(m.getAdresse(), "test@test.test");
	}
}
