/*
 * 
 */
package com.libriciel.Atteste.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.libriciel.Atteste.model.Certificat;

/**
 * @author tpapin
 * 
 * Interface permettant de communiquer avec la base de données
 */

@Repository
public interface CertificatRepository extends JpaRepository<Certificat, Integer> {

}
