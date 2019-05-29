package com.libriciel.Atteste.service;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.time.ZoneId;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.libriciel.Atteste.model.Certificat;

public class MailHandlerTest {

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
	public void testGetRemValidY() {
		System.out.println("Test 1 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 4, 1), LocalDate.now().plusYears(3));
		assertEquals(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[0], 2);
		assertEquals(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[1], 11);
		assertEquals(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[2], 30);
	}
	
	@Test
	public void testGetRemValidM() {
		System.out.println("Test 2 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 4, 1), LocalDate.now().plusMonths(4));
		assertEquals(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[0], 0);
		assertEquals(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[1], 3);
		assertEquals(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[2], 31);
	}
	
	@Test
	public void testGetRemValidD() {
		System.out.println("Test 3 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 4, 1), LocalDate.now().plusDays(8));
		assertEquals(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[0], 0);
		assertEquals(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[1], 0);
		assertEquals(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[2], 8);
	}
	
	@Test
	public void testGetRemValidYMD() {
		System.out.println("Test 4 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 4, 1), LocalDate.now().plusYears(1).plusMonths(11).minusDays(1));
		assertEquals(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[0], 1);
		assertEquals(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[1], 10);
		assertEquals(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[2], 30);
	}
	
	@Test
	public void testGetRemSameDate() {
		System.out.println("Test 5 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 5, 9), LocalDate.now());
		assertNull(mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()));

	}
	
	@Test
	public void testIsExpiredExp() {
		System.out.println("Test 6 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 1, 9), LocalDate.now().minusDays(6));
		assertTrue(mh.isExpired(c1));
	}
	
	@Test
	public void testIsExpiredNotExp() {
		System.out.println("Test 7 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 1, 9), LocalDate.now().plusDays(8));
		assertFalse(mh.isExpired(c1));
	}
	
	@Test
	public void testIsExpiredSame() {
		System.out.println("Test 8 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now());
		assertTrue(mh.isExpired(c1));
	}
	
	@Test
	public void testIsRedTrue() {
		System.out.println("Test 9 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusDays(23));
		assertTrue(mh.isRed(c1));
	}
	
	@Test
	public void testIsRedFalse() {
		System.out.println("Test 10 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusMonths(1).plusDays(1));
		assertFalse(mh.isRed(c1));
	}
	
	@Test
	public void testIsOrangeTrue() {
		System.out.println("Test 11 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusMonths(1).plusDays(1));
		assertTrue(mh.isOrange(c1));
	}
	
	@Test
	public void testIsOrangeFalse() {
		System.out.println("Test 12 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusYears(1));
		assertFalse(mh.isOrange(c1));
	}
	
	@Test
	public void testIsGreenTrue() {
		System.out.println("Test 13 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusYears(1));
		assertTrue(mh.isGreen(c1));
	}
	
	@Test
	public void testIsGreenFalse() {
		System.out.println("Test 14 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusMonths(1));
		assertFalse(mh.isGreen(c1));
	}
	
	@Test
	public void testGetCodeEXPIRED() {
		System.out.println("Test 15 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().minusMonths(1));
		assertEquals(mh.getCode(c1), "EXPIRED");
	}
	
	@Test
	public void testGetCodeRED() {
		System.out.println("Test 16 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusDays(10));
		assertEquals(mh.getCode(c1), "RED");
	}
	
	@Test
	public void testGetCodeORANGE() {
		System.out.println("Test 17 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusMonths(2));
		assertEquals(mh.getCode(c1), "ORANGE");
	}
	
	@Test
	public void testGetCodeGREEN() {
		System.out.println("Test 18 :");
		MailHandler mh = new MailHandler();
		Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusMonths(10));
		assertEquals(mh.getCode(c1), "GREEN");
	}
}
