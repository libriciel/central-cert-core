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

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AttesteCertificatsTest {

    @Test
    public void testIsValidURL() {
        System.out.println("Test de la méthode isValidURL()");
        assertFalse(CertificatService.isValidURL(""));
        assertFalse(CertificatService.isValidURL("https/github.com/"));
        assertTrue(CertificatService.isValidURL("https://github.com/"));
    }

    @Test
    public void testInvalidUrl() {
        Assert.assertEquals(0, CertificatService.getCertificateFromURL("https/github.com/").length);
    }

}
