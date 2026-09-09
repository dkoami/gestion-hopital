package sn.estm.service_constante.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sn.estm.service_constante.client.BonReponse;
import sn.estm.service_constante.client.PatientReponse;
import sn.estm.service_constante.client.config.PatientFeignClient;
import sn.estm.service_constante.dto.ConstanteReponse;
import sn.estm.service_constante.dto.ConstanteRequete;
import sn.estm.service_constante.entities.Constante;
import sn.estm.service_constante.repositories.ConstanteRepository;


@Service
@RequiredArgsConstructor
public class ConstanteService {

	private final ConstanteRepository constanteRepo;
	private final PatientFeignClient patientFeignClient;

	public ConstanteReponse CreerConstante(UUID pid, ConstanteRequete constanteReq) {
		// Récupération des bons du patient via Feign Client
		List<BonReponse> bonliste = patientFeignClient.afficherListeBonPatient(pid);

		if (bonliste == null || bonliste.isEmpty()) {
			throw new RuntimeException("Aucun bon trouvé pour ce patient.");
		}
		Long bonIdLibre = null;
		// verifier si le dernier bon de consultation acheté est utilise pour la
		// la prise de constante
		for (BonReponse bon : bonliste) {
			boolean existBonConsult = constanteRepo.existsByBid(bon.getBid());
			if (!existBonConsult) {
				bonIdLibre = bon.getBid();
			}
		}

		if (bonIdLibre == null) {
			throw new RuntimeException("Tous les bons de ce patient ont déjà été utilisés.");
		}
		// Création et sauvegarde de la constante
		Constante constante = new Constante();
		constante.setBid(bonIdLibre);
		constante.setPid(pid);
		transferConstanteRequeteAConstante(constante, constanteReq);
		Constante constanteSave= constanteRepo.save(constante);
		return transfertConstanteAconstanteResponse( constanteSave);
	}

	public List<ConstanteReponse> listeDesConstantePatient() {

		List<Constante> constanteList = constanteRepo.findAll();
		return constanteList.stream().map(constante->{
			ConstanteReponse.PatientDetails patientDetails= null;
			UUID pid= constante.getPid();
			PatientReponse patient=patientFeignClient.afficheUnPatient(pid);
			if(patient==null) {
				throw new RuntimeException("patient non trouvé");
			}
		 patientDetails= new ConstanteReponse.PatientDetails(patient.getPnom(),patient.getPrenom());
		 return transfertConstanteAConstanteResponses(constante,patientDetails);
		 
		}).collect(Collectors.toList());		
			
	}

	public void suprimerConstante(Long cid) {
		if (constanteRepo.findById(cid).isPresent()) {
			constanteRepo.deleteById(cid);
		} else {
			throw new RuntimeException("La consultation du patient avec l'ID de consultation " + cid + " n'existe pas");
		}
	}

	public void transferConstanteRequeteAConstante(Constante constante, ConstanteRequete constanteReq) {
		constante.setPoids(constanteReq.getPoids());
		constante.setTaille(constanteReq.getTaille());
		constante.setTemp(constanteReq.getTemp());
		constante.setTesion(constanteReq.getTesion());
		constante.setDateCr(LocalDateTime.now());
		constante.setDateM(LocalDateTime.now());
		
	}

	public ConstanteReponse transfertConstanteAconstanteResponse(Constante constante) {
		ConstanteReponse constanteReponse = new ConstanteReponse();
		constanteReponse.setConstid(constante.getConstid());
		constanteReponse.setBid(constante.getBid());
		constanteReponse.setPoids(constante.getPoids());
		constanteReponse.setTaille(constante.getTaille());
		constanteReponse.setTemp(constante.getTemp());
		constanteReponse.setTesion(constante.getTesion());
		constanteReponse.setDateCr(LocalDateTime.now());
		constanteReponse.setDateM(LocalDateTime.now());
		return constanteReponse;
	}

	public ConstanteReponse transfertConstanteAConstanteResponses(Constante constante,
	    ConstanteReponse.PatientDetails patientDetails) {
		ConstanteReponse constanteReponse = transfertConstanteAconstanteResponse(constante);
		constanteReponse.setPatientDetails(patientDetails);
		return constanteReponse;
	}
}
