package com.libriciel.atteste.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.libriciel.atteste.model.Certificat;
import com.libriciel.atteste.repository.CertificatRepository;

/**
 * @author tpapin
 * 
 * Controlleur de la classe certificat
 * Permet de communiquer avec l'API REST spring depuis l'application angular
 */
@RestController
@PreAuthorize("hasRole('user')")
public class CertificatController {
    private static Logger logger = Logger.getLogger("logg");

	/** Le repository. 
	 * 
	 * Permet de communiquer avec la base de données
	 */
	@Autowired
	CertificatRepository repository;

	/**
	 * Permet d'enregistrer un certificat dans la base de données
	 *
	 * @param certificat le certificat
	 */
	@PostMapping("/api/certificat/save")
	public void save(@RequestBody Certificat certificat) {
		if(certificat != null) {
			repository.save(certificat);
		}
	}
	
	/**
	 * Permet de sauvegarder plusieurs certificats d'un coup
	 *
	 * @param certificat une liste de certificats
	 */
	@PostMapping("/api/certificat/saveAll")
	public void saveAll(@RequestBody List<Certificat> certificat) {
		if(certificat != null) {
			for(int i = 0; i < certificat.size(); i++){
				if(certificat.get(i) == null) {
					certificat.remove(i);
				}
			}
			repository.saveAll(certificat);
		}
	}
	
	/**
	 * Permet de récupérer un certificat de la base de données grace à son ID
	 *
	 * @param id l'id
	 * @return le certificat
	 */
	@GetMapping("/api/certificat/select")
	public Certificat select(@RequestParam("id") int id){
		Optional<Certificat> opt = repository.findById(id);
		if(opt.isPresent()) {
			return opt.get();
		}else {
			return null;
		}
	}
	
	/**
	 * Permet de récupérer la liste de tous les certificats de la base de données
	 *
	 * @return la liste des certificats
	 */
	@GetMapping("/api/certificat/selectAll")
	public List<Certificat> selectAll(){
		List<Certificat> res = new ArrayList<>();
		repository.findAll().iterator().forEachRemaining(res::add);
		return res;
	}

	/**
	 * Permet de récuperer les certificats d'une url
	 *
	 * @param url l'url
	 * @return la liste des certificats présents dans l'URL
	 */
	@GetMapping("/api/certificat/selectFromURL")
	public List<Certificat> selectFromUrl(@RequestParam("URL") String url) {
		List<Certificat> res = new ArrayList<>();
		X509Certificate[] certs = new X509Certificate[0];
		try {
			certs = AttesteCertificats.getCertificateFromURL(url);
		} catch (Exception e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
		if(certs.length != 0) {
			for(int i = 0; i < certs.length; i++) {
				res.add(new Certificat(certs[i]));
			}
		}
		return res;
	}
	
	/**
	 * Permet de récupérer le certificat d'un fichier
	 *
	 * @param file le fichier
	 * @return le certificat du fichier
	 */
	@PostMapping(value = "/api/certificat/selectFromFile", consumes = MediaType.ALL_VALUE)
	public Certificat selectFromFile(@RequestParam("file") MultipartFile file) {
		try {
			File convFile = new File(file.getOriginalFilename());
			boolean isCreated = convFile.createNewFile();
			if(isCreated) {
				FileOutputStream fos = new FileOutputStream(convFile);
				return getCertFromMultipart(fos, file, convFile);
			}
		} catch (FileNotFoundException e2) {
			logger.log(Level.SEVERE, e2.getMessage());
		} catch (IOException e3) {
			logger.log(Level.SEVERE, e3.getMessage());
		}
		return null;
	}
	
	/**
	 * Permet récupérer des certificats d'un fichier multipart
	 *
	 * Permet d'alléger la méthode selectFromFile
	 * 
	 * @return un certificat
	 */
	public Certificat getCertFromMultipart(FileOutputStream fos, MultipartFile file, File convFile) {
		try {
			fos.write(file.getBytes());
			
			if(AttesteCertificats.getCertificateFromToken(convFile) != null) {
				return new Certificat(AttesteCertificats.getCertificateFromToken(convFile));
		    }else {
		    	return null;
		    }
		} catch (IOException e1) {
			logger.log(Level.SEVERE, e1.getMessage());
		} finally {
			closeFile(fos);
		}
		return null;
	}
	
	/**
	 * Permet de fermer un fichier
	 *
	 * Permet d'alléger la méthode selectFromFile
	 */
	public void closeFile(FileOutputStream fos) {
		try {
			fos.close();
		} catch (IOException e) {
			logger.log(Level.SEVERE, e.getMessage());
		}
	}
	
	/**
	 * permet de supprimer un certificat de la base de données via son ID
	 *
	 * @param id l'ID
	 */
	@DeleteMapping("/api/certificat/delete")
	public void delete(@RequestParam("id") int id) {
		repository.deleteById(id);
	}
	
	/**
	 * Permet de supprimer tous les certificats de la base de données.
	 */
	@DeleteMapping("/api/certificat/deleteAll")
	public void deleteAll() {
		repository.deleteAll();
	}

	
	
	/**
	 * Update.
	 *
	 * @param certificat the certificat
	 */
	@PutMapping("/api/certificat/update")
	public void update(@RequestBody Certificat certificat) {
		Optional<Certificat> cert = repository.findById(certificat.getId());
		Certificat c;
		if(cert.isPresent()) {
			c = cert.get();
			c.setNotBefore(certificat.getNotBefore());
			c.setNotAfter(certificat.getNotAfter());
			c.setDN(certificat.getDN());
			c.setNotifyAll(certificat.isNotifyAll());
			c.setAdditionnalMails(certificat.getAdditionnalMails());
			c.setNotified(certificat.getNotified());
			repository.save(c);
		}else {
			repository.save(certificat);
		}
	}
	
	/**
	 * Update all.
	 *
	 * @param certificats la liste de certificats
	 */
	@PutMapping("/api/certificat/updateAll")
	public void updateAll(@RequestBody List<Certificat> certificats) {
		for(int i = 0; i < certificats.size(); i++) {
			Optional<Certificat> cert = repository.findById(certificats.get(i).getId());
			Certificat c;
			if(cert.isPresent()) {
				c = cert.get();
				c.setNotBefore(certificats.get(i).getNotBefore());
				c.setNotAfter(certificats.get(i).getNotAfter());
				c.setDN(certificats.get(i).getDN());
				c.setNotifyAll(certificats.get(i).isNotifyAll());
				c.setNotified(certificats.get(i).getNotified());
				c.setAdditionnalMails(certificats.get(i).getAdditionnalMails());
				repository.save(c);
			}else {
				repository.save(certificats.get(i));
			}
		}
	}

	/**
	 * Permet de reset l'envoi de mail sur une adresse
	 *
	 * @param id l'ID du certificat concerné
	 * @param addMail l'adresse mail concernée
	 */
	@PostMapping("/api/certificat/resetMail")
	public void resetMail(@RequestParam("id") int id, @RequestParam("addMail") String addMail) {
		Optional<Certificat> optCert = repository.findById(id);
		if(optCert.isPresent()) {
			Certificat c = optCert.get();
			for(int i = 0; i < c.getAdditionnalMails().size(); i++) {
				if(c.getAdditionnalMails().get(i).getAdresse().equals(addMail)) {
					c.getAdditionnalMails().get(i).setNotifiable(false);
				}
			}
			repository.save(c);
		}
	}
}
