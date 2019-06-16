/*
 * 
 */
package com.libriciel.centralcert.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.libriciel.centralcert.model.Certificat;

/**
 * @author tpapin
 * 
 * Interface permettant de communiquer avec la base de données
 */

@Repository
public interface CertificatRepository extends JpaRepository<Certificat, Integer> {

	// Auto-generated
	
}
