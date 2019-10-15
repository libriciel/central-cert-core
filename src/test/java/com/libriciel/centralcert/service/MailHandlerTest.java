/*
 * Central-Cert Core
 * Copyright (C) 2019 Libriciel-SCOP
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


public class MailHandlerTest {

    @Test
    public void testGetRemValidY() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 4, 1), LocalDate.now().plusYears(3));
        Assert.assertEquals(2, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[0]);
        Assert.assertEquals(11, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[1]);
        Assert.assertEquals(30, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[2]);
    }

    @Test
    public void testGetRemValidM() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 4, 1), LocalDate.now().plusMonths(4));
        Assert.assertEquals(0, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[0]);
        Assert.assertEquals(3, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[1]);
        Assert.assertEquals(31, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[2]);
    }

    @Test
    public void testGetRemValidD() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 4, 1), LocalDate.now().plusDays(8));
        Assert.assertEquals(0, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[0]);
        Assert.assertEquals(0, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[1]);
        Assert.assertEquals(8, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[2]);
    }

    @Test
    public void testGetRemValidYMD() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 4, 1), LocalDate.now().plusYears(1).plusMonths(11).minusDays(1));
        Assert.assertEquals(1, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[0]);
        Assert.assertEquals(10, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[1]);
        Assert.assertEquals(30, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())[2]);
    }

    @Test
    public void testGetRemSameDate() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 5, 9), LocalDate.now());
        Assert.assertEquals(0, mh.getRem(c1.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).length);
    }

    @Test
    public void testIsExpiredExp() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 1, 9), LocalDate.now().minusDays(6));
        assertTrue(mh.isExpired(c1));
    }

    @Test
    public void testIsExpiredNotExp() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 1, 9), LocalDate.now().plusDays(8));
        assertFalse(mh.isExpired(c1));
    }

    @Test
    public void testIsExpiredSame() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now());
        assertTrue(mh.isExpired(c1));
    }

    @Test
    public void testIsRedTrue() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusDays(23));
        assertTrue(mh.isRed(c1));
    }

    @Test
    public void testIsRedFalse() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusMonths(1).plusDays(1));
        assertFalse(mh.isRed(c1));
    }

    @Test
    public void testIsOrangeTrue() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusMonths(1).plusDays(1));
        assertTrue(mh.isOrange(c1));
    }

    @Test
    public void testIsOrangeFalse() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusYears(1));
        assertFalse(mh.isOrange(c1));
    }

    @Test
    public void testIsGreenTrue() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusYears(1));
        assertTrue(mh.isGreen(c1));
    }

    @Test
    public void testIsGreenFalse() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusMonths(1));
        assertFalse(mh.isGreen(c1));
    }

    @Test
    public void testGetCodeEXPIRED() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().minusMonths(1));
        Assert.assertEquals("EXPIRED", mh.getCode(c1));
    }

    @Test
    public void testGetCodeRED() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusDays(10));
        Assert.assertEquals("RED", mh.getCode(c1));
    }

    @Test
    public void testGetCodeORANGE() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusMonths(2));
        Assert.assertEquals("ORANGE", mh.getCode(c1));
    }

    @Test
    public void testGetCodeGREEN() {
        MailHandler mh = new MailHandler();
        Certificat c1 = new Certificat(LocalDate.of(2019, 3, 9), LocalDate.now().plusMonths(10));
        Assert.assertEquals("GREEN", mh.getCode(c1));
    }

}
