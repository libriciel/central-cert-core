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

import org.junit.Assert;
import org.junit.Test;

public class MailTest {


    @Test public void testNoArgsContructor() {
        Mail mail = new Mail();

        Assert.assertEquals("", mail.getAdresse());
        Assert.assertFalse(mail.isNotifiable());
    }


    @Test public void testAllArgsConstructor() {
        Mail mail = new Mail("test@test.test", true);

        Assert.assertEquals("test@test.test", mail.getAdresse());
        Assert.assertTrue(mail.isNotifiable());
    }


    @Test public void testAccessors() {
        Mail mail = new Mail();

        mail.setAdresse("test@test.test");
        mail.setNotifiable(true);

        Assert.assertEquals("test@test.test", mail.getAdresse());
        Assert.assertTrue(mail.isNotifiable());
    }


}
