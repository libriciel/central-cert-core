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

import lombok.Data;

import javax.persistence.*;
import java.security.cert.X509Certificate;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "certificats")
@Data
public class Certificat {

    private static final String GREEN = "GREEN";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int certificatId;

    @Column(name = "notBefore")
    private Date notBefore;

    @Column(name = "notAfter")
    private Date notAfter;

    @Column(name = "favoris")
    private boolean favoris;

    @Column(name = "notifyAll")
    private boolean notifyAll;

    @Column(name = "dn")
    private String dn;

    @Column(name = "notified")
    private String notified;

    @Embedded
    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "additionnalMails")
    private List<Mail> additionnalMails;

    public Certificat() {
        this.notBefore = null;
        this.notAfter = null;
        this.notified = GREEN;
        this.additionnalMails = new ArrayList<>();
    }

    public Certificat(X509Certificate cert) {
        this.notBefore = cert.getNotBefore();
        this.notAfter = cert.getNotAfter();
        this.favoris = false;
        this.dn = cert.getSubjectX500Principal().getName();
        this.notifyAll = false;
        this.notified = GREEN;
        this.additionnalMails = new ArrayList<>();
    }

    public Certificat(LocalDate nb, LocalDate na) {
        this.notBefore = Date.from(nb.atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.notAfter = Date.from(na.atStartOfDay(ZoneId.systemDefault()).toInstant());
        this.favoris = false;
        this.dn = null;
        this.notifyAll = false;
        this.notified = GREEN;
        this.additionnalMails = new ArrayList<>();
    }

}