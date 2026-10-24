package sn.estm.service_constante.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
		constante.setDateCr(LocalDateTime.now());
		constante.setDateM(LocalDateTime.now());
		Constante constanteSave = constanteRepo.save(constante);
		return transfertConstanteAconstanteResponse(constanteSave);
	}

	public List<ConstanteReponse> listeDesConstantePatient() {

		List<Constante> constanteList = constanteRepo.findAll();
		return constanteList.stream().map(constante -> {
			ConstanteReponse.PatientDetails patientDetails = null;
			UUID pid = constante.getPid();
			PatientReponse patient = patientFeignClient.afficheUnPatient(pid);
			if (patient == null) {
				throw new RuntimeException("patient non trouvé");
			}
			patientDetails = new ConstanteReponse.PatientDetails(patient.getPnom(), patient.getPrenom());
			return transfertConstanteAConstanteResponses(constante, patientDetails);

		}).collect(Collectors.toList());

	}

	public ConstanteReponse obtenirUneConstante(Long constid) {
		Constante constante = constanteRepo.findById(constid)
				.orElseThrow(() -> new RuntimeException("La constante avec l'ID " + constid + " n'existe pas"));

		PatientReponse patient = patientFeignClient.afficheUnPatient(constante.getPid());
		if (patient == null) {
			throw new RuntimeException("patient non trouvé");
		}
		ConstanteReponse.PatientDetails patientDetails = new ConstanteReponse.PatientDetails(patient.getPnom(),
				patient.getPrenom());

		return transfertConstanteAConstanteResponses(constante, patientDetails);
	}

	public void suprimerConstante(Long cid) {
		if (constanteRepo.findById(cid).isPresent()) {
			constanteRepo.deleteById(cid);
		} else {
			throw new RuntimeException("La consultation du patient avec l'ID de consultation " + cid + " n'existe pas");
		}
	}

	public List<ConstanteReponse> listeDesConstantePatient(String motCle) {

		List<Constante> constantes = constanteRepo.findAll();

		// Sans mot-clé : afficher toutes les constantes
		if (motCle == null || motCle.trim().isEmpty()) {
			return constantes.stream().map(constante -> {
				PatientReponse patient = patientFeignClient.afficheUnPatient(constante.getPid());

				ConstanteReponse.PatientDetails details = patient == null ? null
						: new ConstanteReponse.PatientDetails(patient.getPnom(), patient.getPrenom());

				return transfertConstanteAConstanteResponses(constante, details);
			}).collect(Collectors.toList());
		}

		// Rechercher les patients dans patient-service
		List<PatientReponse> patients = patientFeignClient.rechercherPatients(motCle.trim());

		// Extraire les identifiants des patients trouvés
		List<UUID> pids = patients.stream().map(PatientReponse::getPid) // Adapter si le getter diffère
				.collect(Collectors.toList());

		// Conserver uniquement les constantes de ces patients
		return constantes.stream().filter(constante -> pids.contains(constante.getPid())).map(constante -> {
			PatientReponse patient = patients.stream().filter(p -> p.getPid().equals(constante.getPid())).findFirst()
					.orElse(null);

			ConstanteReponse.PatientDetails details = new ConstanteReponse.PatientDetails(patient.getPnom(),
					patient.getPrenom());

			return transfertConstanteAConstanteResponses(constante, details);
		}).collect(Collectors.toList());
	}

	public Optional<ConstanteReponse> miseAjourConstante(Long constid, ConstanteRequete constanteReq) {
		return constanteRepo.findById(constid).map(existeConstante -> {
			transferConstanteRequeteAConstante(existeConstante, constanteReq);
			existeConstante.setDateM(LocalDateTime.now());
			Constante constante = constanteRepo.save(existeConstante);
			return transfertConstanteAconstanteResponse(constante);
		});
	}

	public void transferConstanteRequeteAConstante(Constante constante, ConstanteRequete constanteReq) {
		constante.setPoids(constanteReq.getPoids());
		constante.setTaille(constanteReq.getTaille());
		constante.setTemp(constanteReq.getTemp());
		constante.setTension(constanteReq.getTension());

	}

	public ConstanteReponse transfertConstanteAconstanteResponse(Constante constante) {
		ConstanteReponse constanteReponse = new ConstanteReponse();
		constanteReponse.setConstid(constante.getConstid());
		constanteReponse.setBid(constante.getBid());
		constanteReponse.setPoids(constante.getPoids());
		constanteReponse.setTaille(constante.getTaille());
		constanteReponse.setTemp(constante.getTemp());
		constanteReponse.setTension(constante.getTension());
		constanteReponse.setDateCr(constante.getDateCr());
		constanteReponse.setDateM(constante.getDateM());
		return constanteReponse;
	}

	public ConstanteReponse transfertConstanteAConstanteResponses(Constante constante,
			ConstanteReponse.PatientDetails patientDetails) {
		ConstanteReponse constanteReponse = transfertConstanteAconstanteResponse(constante);
		constanteReponse.setPatientDetails(patientDetails);
		return constanteReponse;
	}
}
