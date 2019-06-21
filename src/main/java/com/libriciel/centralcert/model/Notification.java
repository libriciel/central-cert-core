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
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.ZoneId;

@Data
@NoArgsConstructor
public class Notification {


    private String objet;

    private String message;


    public Notification(Certificat c, String code) {
        if (c != null) {

            // Conversion date to localDate

            LocalDate nb = c.getNotBefore().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate na = c.getNotAfter().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            // Getting DN informations
            // TODO : Change this with a proper Bouncy parse

            String[] dn = c.getDn().split(",");

            String cn = "";
            for (int i = 0; i < dn.length; i++) {
                if (dn[i].startsWith("CN=")) {
                    cn = dn[i].substring(3);
                }
            }

            StringBuilder sb = new StringBuilder();
            sb.append("cn :").append(cn).append(" \n");
            sb.append("Not before :").append(nb.toString()).append(" \n");
            sb.append("Not after :").append(na.toString()).append(" \n");

            for (String s : dn) {
                sb.append(s).append(" \n");
            }

            sb.append(" \n");
            sb.append("Si vous ne souhaitez pas recevoir de mails pour ce certificat, rendez-vous sur ce lien : \n");

            this.message = sb.toString();
            this.setObjAndMess(code, cn);
        } else {
            this.objet = "null";
            this.message = "null";
        }
    }

    private void setObjAndMess(String code, String cn) {

        switch (code) {

            case "EXPIRED":
                if (cn.equals("")) {
                    this.objet = "Un de vos certificats à expiré";
                } else {
                    this.objet = "Votre certificat \"" + cn + "\" a expiré";
                }
                break;

            case "RED":
                if (cn.equals("")) {
                    this.objet = "Un de vos certificats expire bientôt";
                } else {
                    this.objet = "Votre certificat \"" + cn + "\" expire bientôt";
                }
                break;

            case "ORANGE":
                if (cn.equals("")) {
                    this.objet = "Un de vos certificats arrive à expiration";
                } else {
                    this.objet = "Votre certificat \"" + cn + "\" arrive à expiration";
                }
                break;

            default:
                this.objet = null;
                this.message = null;
                break;
        }
    }

}
