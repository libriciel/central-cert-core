/*
 * 
 */
package com.libriciel.atteste.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.libriciel.atteste.model.Certificat;

/**
 * @author tpapin
 * 
 * Interface permettant de communiquer avec la base de données
 */

@Repository
public interface CertificatRepository extends JpaRepository<Certificat, Integer> {

}
