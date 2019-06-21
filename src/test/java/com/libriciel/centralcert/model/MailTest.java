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

    @Test public void testConstructorAndAccessors() {

        // Empty constructor

        Mail m = new Mail();

        Assert.assertEquals("", m.getAdresse());
        Assert.assertFalse(m.isNotifiable());

        // Accessors

        m.setAdresse("test@test.test");
        m.setNotifiable(true);

        Assert.assertEquals("test@test.test", m.getAdresse());
        Assert.assertTrue(m.isNotifiable());

        // All args constructor

        m = new Mail("test@test.test", true);

        Assert.assertEquals("test@test.test", m.getAdresse());
        Assert.assertTrue(m.isNotifiable());
    }

}
