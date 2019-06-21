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

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertNull;

public class NotificationTest {

    @Test
    public void testVide() {
        Notification n = new Notification();
        assertNull(n.getObjet());
        assertNull(n.getMessage());
    }

    @Test
    public void testDNNullExpired() {
        Certificat c = new Certificat();
        c.setNotBefore(new Date());
        c.setNotAfter(new Date());
        c.setDn("");
        LocalDate nb = c.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate na = c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Notification n = new Notification(c, "EXPIRED");
        Assert.assertEquals("Un de vos certificats à expiré", n.getObjet());

        Assert.assertEquals("cn : \nNot before :" + nb.toString() + " \nNot after :" + na.toString() + " \n \n \nSi vous ne souhaitez pas recevoir de mails pour ce certificat, rendez-vous sur ce lien : \n", n.getMessage());
    }

    @Test
    public void testDNNotNullExpired() {
        Certificat c = new Certificat();
        c.setNotBefore(new Date());
        c.setNotAfter(new Date());
        c.setDn("CN=TEST");
        LocalDate nb = c.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate na = c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Notification n = new Notification(c, "EXPIRED");
        Assert.assertEquals("Votre certificat \"TEST\" a expiré", n.getObjet());

        Assert.assertEquals("cn :TEST \nNot before :" + nb.toString() + " \nNot after :" + na.toString() + " \nCN=TEST \n \nSi vous ne souhaitez pas recevoir de mails pour ce certificat, rendez-vous sur ce lien : \n", n.getMessage());
    }

    @Test
    public void testDNNullRED() {
        Certificat c = new Certificat();
        c.setNotBefore(new Date());
        c.setNotAfter(new Date());
        c.setDn("");
        LocalDate nb = c.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate na = c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Notification n = new Notification(c, "RED");
        Assert.assertEquals("Un de vos certificats expire bientôt", n.getObjet());

        Assert.assertEquals("cn : \nNot before :" + nb.toString() + " \nNot after :" + na.toString() + " \n \n \nSi vous ne souhaitez pas recevoir de mails pour ce certificat, rendez-vous sur ce lien : \n", n.getMessage());
    }

    @Test
    public void testDNNotNullRED() {
        Certificat c = new Certificat();
        c.setNotBefore(new Date());
        c.setNotAfter(new Date());
        c.setDn("CN=TEST");
        LocalDate nb = c.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate na = c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Notification n = new Notification(c, "RED");
        Assert.assertEquals("Votre certificat \"TEST\" expire bientôt", n.getObjet());

        Assert.assertEquals("cn :TEST \nNot before :" + nb.toString() + " \nNot after :" + na.toString() + " \nCN=TEST \n \nSi vous ne souhaitez pas recevoir de mails pour ce certificat, rendez-vous sur ce lien : \n", n.getMessage());
    }

    @Test
    public void testDNNullORANGE() {
        Certificat c = new Certificat();
        c.setNotBefore(new Date());
        c.setNotAfter(new Date());
        c.setDn("");
        LocalDate nb = c.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate na = c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Notification n = new Notification(c, "ORANGE");
        Assert.assertEquals("Un de vos certificats arrive à expiration", n.getObjet());

        Assert.assertEquals("cn : \nNot before :" + nb.toString() + " \nNot after :" + na.toString() + " \n \n \nSi vous ne souhaitez pas recevoir de mails pour ce certificat, rendez-vous sur ce lien : \n", n.getMessage());
    }

    @Test
    public void testDNNotNullORANGE() {
        Certificat c = new Certificat();
        c.setNotBefore(new Date());
        c.setNotAfter(new Date());
        c.setDn("CN=TEST");
        LocalDate nb = c.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate na = c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        Notification n = new Notification(c, "ORANGE");
        Assert.assertEquals("Votre certificat \"TEST\" arrive à expiration", n.getObjet());

        Assert.assertEquals("cn :TEST \nNot before :" + nb.toString() + " \nNot after :" + na.toString() + " \nCN=TEST \n \nSi vous ne souhaitez pas recevoir de mails pour ce certificat, rendez-vous sur ce lien : \n", n.getMessage());
    }

    @Test
    public void testDNNullNULL() {
        Certificat c = new Certificat();
        c.setNotBefore(new Date());
        c.setNotAfter(new Date());
        c.setDn("");
        Notification n = new Notification(c, "GREEN");
        assertNull(n.getObjet());

        assertNull(n.getMessage());
    }

    @Test
    public void testDNNotNullONULL() {
        Certificat c = new Certificat();
        c.setNotBefore(new Date());
        c.setNotAfter(new Date());
        c.setDn("CN=TEST");
        Notification n = new Notification(c, "GREEN");
        assertNull(n.getObjet());

        assertNull(n.getMessage());
    }

    @Test
    public void testGetter() {
        Notification n = new Notification(null, "EXPIRED");
        Assert.assertEquals("null", n.getObjet());
        Assert.assertEquals("null", n.getMessage());
    }

    @Test
    public void testSetter() {
        Notification n = new Notification(null, "EXPIRED");
        Assert.assertEquals("null", n.getObjet());
        Assert.assertEquals("null", n.getMessage());

        n.setObjet("TEST");
        n.setMessage("TEST");
        Assert.assertEquals("TEST", n.getObjet());
        Assert.assertEquals("TEST", n.getMessage());
    }

}
