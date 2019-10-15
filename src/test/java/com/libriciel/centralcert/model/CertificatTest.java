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

package com.libriciel.centralcert.model;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

import static com.libriciel.centralcert.service.CertificatController.CERTIFICATE_TYPE_X509;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class CertificatTest {

    private static final String KEYSTORE_JKS_PUBLIC_KEY = "" +
            "MIIFgDCCBGigAwIBAgISA8hL9iGr+5DUjnZl0wc56TNKMA0GCSqGSIb3DQEBCwUA" +
            "MEoxCzAJBgNVBAYTAlVTMRYwFAYDVQQKEw1MZXQncyBFbmNyeXB0MSMwIQYDVQQD" +
            "ExpMZXQncyBFbmNyeXB0IEF1dGhvcml0eSBYMzAeFw0xOTA1MTcxMjQ1MjRaFw0x" +
            "OTA4MTUxMjQ1MjRaMBcxFTATBgNVBAMTDGxpYnJpY2llbC5mcjCCASIwDQYJKoZI" +
            "hvcNAQEBBQADggEPADCCAQoCggEBAMYoRUYialFgIpNAbkAb7+LgDAmdvY1uyfHB" +
            "4O666x9vKK98B4oV3c0U8FDfOPFB+zzc1CcQ+aIqdc1ts1rgHfWZ5Ot/dQW3rmPS" +
            "jxpB8XRaS2qtekfXq15ttte6JqZyHQsIoESZ5Rg9p32M4NdOO3iQDM8Y7emZcqTP" +
            "8PgOk/flvakgP/99fvQrs/19zVJHsY+O7QmTm/pfHl01PGQ6A31gbhLTFriQ7ato" +
            "7gQpa09dQAAUfIkGiFv7YCrjYFijmuWSVVu2oEinUm+B9Lj4E4j1rljyBOW0LV1m" +
            "o84/DKo/2BAQ9abHT1iNW9lZHVZwJbtLG2We4nGAeZkV1/tsYZcCAwEAAaOCApEw" +
            "ggKNMA4GA1UdDwEB/wQEAwIFoDAdBgNVHSUEFjAUBggrBgEFBQcDAQYIKwYBBQUH" +
            "AwIwDAYDVR0TAQH/BAIwADAdBgNVHQ4EFgQUO+F3VrQogaNYrHDDYfPCUH5rZB0w" +
            "HwYDVR0jBBgwFoAUqEpqYwR93brm0Tm3pkVl7/Oo7KEwbwYIKwYBBQUHAQEEYzBh" +
            "MC4GCCsGAQUFBzABhiJodHRwOi8vb2NzcC5pbnQteDMubGV0c2VuY3J5cHQub3Jn" +
            "MC8GCCsGAQUFBzAChiNodHRwOi8vY2VydC5pbnQteDMubGV0c2VuY3J5cHQub3Jn" +
            "LzBGBgNVHREEPzA9ggxsaWJyaWNpZWwuZnKCG2xpYnJpY2llbC50ZXN0LmxpYnJp" +
            "Y2llbC5mcoIQd3d3LmxpYnJpY2llbC5mcjBMBgNVHSAERTBDMAgGBmeBDAECATA3" +
            "BgsrBgEEAYLfEwEBATAoMCYGCCsGAQUFBwIBFhpodHRwOi8vY3BzLmxldHNlbmNy" +
            "eXB0Lm9yZzCCAQUGCisGAQQB1nkCBAIEgfYEgfMA8QB3AHR+2oMxrTMQkSGcziVP" +
            "QnDCv/1eQiAIxjc1eeYQe8xWAAABasYKTPcAAAQDAEgwRgIhALLhFeLAd3YqZ92F" +
            "O3esxxW+EeMUL1yP+aN6zfjprea+AiEA5fliePTTl5AsPPktY1kYBkupHHNhMwjd" +
            "JJv0sFA3czwAdgApPFGWVMg5ZbqqUPxYB9S3b79Yeily3KTDDPTlRUf0eAAAAWrG" +
            "CkzqAAAEAwBHMEUCIQCE9jfFJrhi2I/gCSn4JFC4fRugXIi9EEwckHE8OIKVOAIg" +
            "IbmPqMv/VBFgOhuHpu0d3q0yxk0O50KFGWaXWQtYZvUwDQYJKoZIhvcNAQELBQAD" +
            "ggEBAERXn1VJwFibHDTtodXq50y36yoJ58c1fV9B+NlMzorBzOaIsa5OwDDij+JB" +
            "55Hu8/MKzBJOOk/0MEF7xY7EG4aQEXY19XqiN4veuck/KV1YmodtFwKC3jGSFyPJ" +
            "9p+2dZdZ9EwTTMIs0VMzPa4u96ViGBAszVReGrdhaihX1POlIE7J7kawvlDP5X4u" +
            "Zfrf7is+oFIEhAVsiPkkNszEj7uzSlbyKduuC1ztYjs8tK7qurDTXB6BaPbYABPs" +
            "15Yy9m4OByk9D/mARi8r7/pNzqvxIeRrr8b4lEkzfx4z1trxBg6EehtBBMEBJ9LM" +
            "/rPnFRV0Y2OMq5pgoycY39ipnlY=";


    @Test public void testNoArgsContructor() {

        Certificat cert = new Certificat();

        Assert.assertEquals(0, cert.getCertificatId());
        Assert.assertNull(cert.getNotBefore());
        Assert.assertNull(cert.getNotAfter());
        Assert.assertFalse(cert.isFavoris());
        Assert.assertFalse(cert.isNotifyAll());
        Assert.assertNull(cert.getDn());
        Assert.assertEquals("GREEN", cert.getNotified());
        Assert.assertEquals(new ArrayList<>(), cert.getAdditionnalMails());
    }


    @Test public void testX509Constructor() throws Exception {

        byte[] publicKeyDer = Base64.getDecoder().decode(KEYSTORE_JKS_PUBLIC_KEY);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(publicKeyDer);
        Security.addProvider(new BouncyCastleProvider());
        CertificateFactory certFactory = CertificateFactory.getInstance(CERTIFICATE_TYPE_X509, BouncyCastleProvider.PROVIDER_NAME);
        X509Certificate x509cert = (X509Certificate) certFactory.generateCertificate(inputStream);

        Certificat cert = new Certificat(x509cert);
        assertEquals(1565873124000L, cert.getNotAfter().getTime());
        assertEquals(1558097124000L, cert.getNotBefore().getTime());
        assertFalse(cert.isFavoris());
        assertEquals("CN=libriciel.fr", cert.getDn());
        assertFalse(cert.isNotifyAll());
        assertEquals("GREEN", cert.getNotified());
        assertEquals(cert.getAdditionnalMails(), new ArrayList<>());
    }


    @Test public void testAccessors() {

        Certificat cert = new Certificat();

        cert.setCertificatId(200);
        cert.setNotBefore(new Date(5000L));
        cert.setNotAfter(new Date(10000L));
        cert.setFavoris(true);
        cert.setNotifyAll(true);
        cert.setDn("TEST DN");
        cert.setNotified("ORANGE");
        cert.setAdditionnalMails(Arrays.asList(new Mail(), new Mail()));

        Assert.assertEquals(200, cert.getCertificatId());
        Assert.assertEquals(5000L, cert.getNotBefore().getTime());
        Assert.assertEquals(10000L, cert.getNotAfter().getTime());
        Assert.assertTrue(cert.isFavoris());
        Assert.assertTrue(cert.isNotifyAll());
        Assert.assertEquals("TEST DN", cert.getDn());
        Assert.assertEquals("ORANGE", cert.getNotified());
        Assert.assertEquals(2, cert.getAdditionnalMails().size());
    }

}
