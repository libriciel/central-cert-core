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

package com.libriciel.centralcert.model;

import com.libriciel.centralcert.service.CertificatService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

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
        assertNull(c.getDn());
        assertFalse(c.isNotifyAll());
        assertEquals("GREEN", c.getNotified());
        assertEquals(c.getAdditionnalMails(), new ArrayList<>());
    }

    @Test
    public void testConstructeurX509() {
        System.out.println("Test du constructeur vide :");
        X509Certificate c1 = CertificatService.getCertificateFromURL("https://www.libriciel.fr/")[0];
        Certificat c = new Certificat(c1);
        assertEquals(c1.getNotAfter(), c.getNotAfter());
        assertEquals(c1.getNotBefore(), c.getNotBefore());
        assertFalse(c.isFavoris());
        assertEquals(c1.getSubjectX500Principal().getName(), c.getDn());
        assertFalse(c.isNotifyAll());
        assertEquals("GREEN", c.getNotified());
        assertEquals(c.getAdditionnalMails(), new ArrayList<>());
    }

    @Test
    public void testId() {
        Certificat c = new Certificat();
        c.setCertificatId(200);
        assertEquals(200, c.getCertificatId());
    }

    @Test
    public void testNB() {
        Certificat c = new Certificat();
        c.setNotBefore(new Date());
        assertEquals(new Date(), c.getNotBefore());
    }

    @Test
    public void testNA() {
        Certificat c = new Certificat();
        c.setNotAfter(new Date());
        assertEquals(new Date(), c.getNotAfter());
    }

    @Test
    public void testDN() {
        Certificat c = new Certificat();
        c.setDn("test");
        assertEquals("test", c.getDn());
    }

    @Test
    public void testAddMail() {
        Certificat c = new Certificat();
        c.setAdditionnalMails(new ArrayList<>());
        assertEquals(new ArrayList<>(), c.getAdditionnalMails());
    }

    @Test
    public void testFavoris() {
        Certificat c = new Certificat();
        c.setFavoris(true);
        assertTrue(c.isFavoris());
    }

    @Test
    public void testNotified() {
        Certificat c = new Certificat();
        c.setNotified("ORANGE");
        assertEquals("ORANGE", c.getNotified());
    }

    @Test
    public void addMail() {
        Certificat c = new Certificat();
        Mail m = new Mail();
        assertTrue(c.getAdditionnalMails().isEmpty());
        c.addMail(m);
        assertFalse(c.getAdditionnalMails().isEmpty());
    }

    @Test
    public void addMails() {
        Certificat c = new Certificat();
        Mail m1 = new Mail();
        Mail m2 = new Mail();
        List<Mail> lm = new ArrayList<>();
        lm.add(m1);
        lm.add(m2);
        assertTrue(c.getAdditionnalMails().isEmpty());
        c.addMails(lm);
        assertEquals(2, c.getAdditionnalMails().size());
    }

    @Test
    public void testNotifyAll() {
        Certificat c = new Certificat();
        assertFalse(c.isNotifyAll());
        c.setNotifyAll(true);
        assertTrue(c.isNotifyAll());
    }
}
