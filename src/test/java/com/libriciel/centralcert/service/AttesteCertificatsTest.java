/*
 * central cert core
 * Copyright (C) 2018-2019 Libriciel-SCOP
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
