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

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
public class Mail {

	@NotNull
	@Size(max=100)
	private String adresse = "";

	@NotNull
	private boolean notifiable = false;

	public Mail() {
	
	}
	
	public Mail(String adresse) {
		this.adresse = adresse;
	}
	
	public Mail(String adresse, boolean notifiable) {
		this.adresse = adresse;
		this.notifiable = notifiable;
	}
	
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}
	
	public void setNotifiable(boolean notifiable) {
		this.notifiable = notifiable;
	}
	
	public String getAdresse() {
		return this.adresse;
	}
	
	public boolean isNotifiable() {
		return this.notifiable;
	}
}
