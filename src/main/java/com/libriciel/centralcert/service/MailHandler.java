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
import com.libriciel.centralcert.model.Mail;
import com.libriciel.centralcert.model.Notification;
import com.libriciel.centralcert.repository.CertificatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class MailHandler {

    @Autowired private CertificatRepository cr;
    @Autowired private JavaMailSender mailSender;


    /**
     * Send a mail
     */
    public void sendMail(Notification n, Mail m) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(m.getAdresse());
        message.setSubject(n.getObjet());
        message.setText(n.getMessage());
        mailSender.send(message);
    }


    /**
     * To know if we have to notify a certificate
     */
    public boolean notifyCODE(Certificat c) {
        String certCode = c.getNotified();
        String code = this.getCode(c);
        return !code.equals(certCode);
    }


    /**
     * Get the list of certificates to notify
     */
    public List<Certificat> certificatesToNotify() {
        List<Certificat> allCerts = cr.findAll();

        List<Certificat> res = new ArrayList<>();

        for (int j = 0; j < allCerts.size(); j++) {
            if ((this.isExpired(allCerts.get(j))
                    || this.isOrange(allCerts.get(j))
                    || this.isRed(allCerts.get(j)))
                    && (this.notifyCODE(allCerts.get(j)))) {
                res.add(allCerts.get(j));
            }
        }

        return res;
    }


    /**
     * Get the remaining time between a date and now
     */
    public static int[] getRem(LocalDate after) {
        //instantiation de la date actuelle
        LocalDate now = LocalDate.now();

        //instantiation du tableau résultat
        int[] res = new int[3];

        //si le certificat n'est pas expiré
        if (after.isAfter(now)) {

            //instantiation de la date d'expiration moins la date actuelle
            after = after.minus(Period.of(now.getYear(), now.getMonthValue(), now.getDayOfMonth()));
            res[0] = after.getYear();
            res[1] = after.getMonthValue();
            res[2] = after.getDayOfMonth();

            //correction de bug
            if (res[0] == -1) {
                res[0] = 0;
                res[1] -= 12;
            }

            //renvoit du temps restant
            return res;
        } else {
            //si le certificat est expiré
            return new int[0];
        }
    }


    /**
     * Check if a certificate is expired
     */
    public static boolean isExpired(Certificat c) {
        int[] rem = getRem(c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        return rem.length == 0;
    }


    /**
     * Check if the certificate is ORANGE
     */
    public static boolean isOrange(Certificat c) {
        int[] rem = getRem(c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        if (rem.length != 0) {
            if (rem[0] == 0 && rem[1] <= 3) {
                return rem[1] >= 1;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * check if the certificate is RED
     */
    public static boolean isRed(Certificat c) {
        int[] rem = getRem(c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

        if (rem.length != 0) {
            return rem[0] == 0 && rem[1] == 0;
        } else {
            return false;
        }
    }


    /**
     * Check if the certificate is GREEN
     */
    public static boolean isGreen(Certificat c) {
        return !isOrange(c) && !isRed(c);
    }


    /**
     * Get the certificate code
     */
    public static String getCode(Certificat c) {
        if (isExpired(c)) {
            return "EXPIRED";
        } else if (isRed(c)) {
            return "RED";
        } else if (isOrange(c)) {
            return "ORANGE";
        } else if (isGreen(c)) {
            return "GREEN";
        } else {
            return "";
        }
    }


    /**
     * Every two hours
     * Send mails to notifiable certificates
     */
    @Scheduled(fixedRate = 7200000)
    public void sendMailsToAll() {
        Notification n = null;
        List<Certificat> certs = this.certificatesToNotify();

        for (int i = 0; i < certs.size(); i++) {
            if (certs.get(i).isNotifyAll()) {
                for (int j = 0; j < certs.get(i).getAdditionnalMails().size(); j++) {
                    n = new Notification(certs.get(i), getCode(certs.get(i)));
                    String url = n.getMessage() + "http://192.168.1.189/resetMail?id=" + certs.get(i).getCertificatId() + "&addMail=" + certs.get(i).getAdditionnalMails().get(j).getAdresse();
                    n.setMessage(url);
                    this.sendMail(n, certs.get(i).getAdditionnalMails().get(j));
                }
            } else {
                for (int j = 0; j < certs.get(i).getAdditionnalMails().size(); j++) {
                    if (certs.get(i).getAdditionnalMails().get(j).isNotifiable()) {
                        n = new Notification(certs.get(i), getCode(certs.get(i)));
                        String url = n.getMessage() + "http://192.168.1.189/resetMail?id=" + certs.get(i).getCertificatId() + "&addMail=" + certs.get(i).getAdditionnalMails().get(j).getAdresse();
                        n.setMessage(url);
                        this.sendMail(n, certs.get(i).getAdditionnalMails().get(j));
                    }
                }
            }

            certs.get(i).setNotified(getCode(certs.get(i)));

            this.cr.save(certs.get(i));
        }
    }
}