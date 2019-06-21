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

import com.libriciel.centralcert.model.Certificat;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MailHandlerTest {

    @Test
    public void testGetRemValidY() {
        System.out.println("Test 1 :");
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 4, 1), LocalDate.now().plusYears(3));
        Assert.assertEquals(2, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[0]);
        Assert.assertEquals(11, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[1]);
        Assert.assertEquals(30, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[2]);
    }

    @Test
    public void testGetRemValidM() {
        System.out.println("Test 2 :");
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 4, 1), LocalDate.now().plusMonths(4));
        Assert.assertEquals(0, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[0]);
        Assert.assertEquals(3, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[1]);
        Assert.assertEquals(31, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[2]);
    }

    @Test
    public void testGetRemValidD() {
        System.out.println("Test 3 :");
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 4, 1), LocalDate.now().plusDays(8));
        Assert.assertEquals(0, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[0]);
        Assert.assertEquals(0, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[1]);
        Assert.assertEquals(8, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[2]);
    }

    @Test
    public void testGetRemValidYMD() {
        System.out.println("Test 4 :");
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 4, 1), LocalDate.now().plusYears(1).plusMonths(11).minusDays(1));
        Assert.assertEquals(1, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[0]);
        Assert.assertEquals(10, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[1]);
        Assert.assertEquals(30, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[2]);
    }

    @Test
    public void testGetRemSameDate() {
        System.out.println("Test 5 :");
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 5, 9), LocalDate.now());
        Assert.assertEquals(0, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).length);
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
        Assert.assertEquals("EXPIRED", mh.getCode(c1));
    }

    @Test
    public void testGetCodeRED() {
        System.out.println("Test 16 :");
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusDays(10));
        Assert.assertEquals("RED", mh.getCode(c1));
    }

    @Test
    public void testGetCodeORANGE() {
        System.out.println("Test 17 :");
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusMonths(2));
        Assert.assertEquals("ORANGE", mh.getCode(c1));
    }

    @Test
    public void testGetCodeGREEN() {
        System.out.println("Test 18 :");
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusMonths(10));
        Assert.assertEquals("GREEN", mh.getCode(c1));
    }

}
