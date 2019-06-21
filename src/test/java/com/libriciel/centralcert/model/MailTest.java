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

import com.libriciel.centralcert.model.Mail;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MailTest {

    @Test
    public void testConstructeurVide() {
        Mail m = new Mail();
        assertTrue(m.isNotifiable());
        Assert.assertEquals("", m.getAdresse());
    }

    public void testConstructeurAdresse() {
        Mail m = new Mail("test@test.test");
        Assert.assertEquals("test@test.test", m.getAdresse());
        assertTrue(m.isNotifiable());

    }

    public void testConstructeurAdresseNotifiable() {
        Mail m = new Mail("test@test.test", false);
        Assert.assertEquals("test@test.test", m.getAdresse());
        assertFalse(m.isNotifiable());
    }

    @Test
    public void testGettersNot() {
        Mail m = new Mail("test@test.test", false);
        Assert.assertEquals("test@test.test", m.getAdresse());
        assertFalse(m.isNotifiable());
    }

    @Test
    public void testSettersNot() {
        Mail m = new Mail("test@test.test", false);
        Assert.assertEquals("test@test.test", m.getAdresse());
        assertFalse(m.isNotifiable());
        m.setAdresse("test2@test.test");
        m.setNotifiable(true);
        Assert.assertEquals("test2@test.test", m.getAdresse());
        assertTrue(m.isNotifiable());
    }

    @Test
    public void testGetters() {
        Mail m = new Mail("test@test.test");
        Assert.assertEquals("test@test.test", m.getAdresse());
        assertTrue(m.isNotifiable());
    }

    @Test
    public void testSetters() {
        Mail m = new Mail("test@test.test");
        Assert.assertEquals("test@test.test", m.getAdresse());
        assertTrue(m.isNotifiable());
        m.setAdresse("test2@test.test");
        m.setNotifiable(false);
        Assert.assertEquals("test2@test.test", m.getAdresse());
        assertFalse(m.isNotifiable());
    }
}
