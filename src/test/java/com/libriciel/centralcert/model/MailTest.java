package com.libriciel.centralcert.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.libriciel.centralcert.model.Mail;

public class MailTest {

	@Test
	public void testConstructeurVide() {
		Mail m = new Mail();
		assertTrue(m.isNotifiable());
		assertEquals("", m.getAdresse());
	}

	public void testConstructeurAdresse() {
		Mail m = new Mail("test@test.test");
		assertEquals("test@test.test", m.getAdresse());
		assertTrue(m.isNotifiable());

	}
	
	public void testConstructeurAdresseNotifiable() {
		Mail m = new Mail("test@test.test", false);
		assertEquals("test@test.test", m.getAdresse());
		assertFalse(m.isNotifiable());
	}
	
	@Test
	public void testGettersNot() {
		Mail m = new Mail("test@test.test", false);
		assertEquals("test@test.test", m.getAdresse());
		assertFalse(m.isNotifiable());
	}
	
	@Test
	public void testSettersNot() {
		Mail m = new Mail("test@test.test", false);
		assertEquals("test@test.test", m.getAdresse());
		assertFalse(m.isNotifiable());
		m.setAdresse("test2@test.test");
		m.setNotifiable(true);
		assertEquals("test2@test.test", m.getAdresse());
		assertTrue(m.isNotifiable());
	}

	@Test
	public void testGetters() {
		Mail m = new Mail("test@test.test");
		assertEquals("test@test.test", m.getAdresse());
		assertTrue(m.isNotifiable());
	}
	
	@Test
	public void testSetters() {
		Mail m = new Mail("test@test.test");
		assertEquals("test@test.test", m.getAdresse());
		assertTrue(m.isNotifiable());
		m.setAdresse("test2@test.test");
		m.setNotifiable(false);
		assertEquals("test2@test.test", m.getAdresse());
		assertFalse(m.isNotifiable());
	}
}
