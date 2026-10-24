package sn.estm.service_consultation.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import sn.estm.service_consultation.client.config.PatientFeignClient;
import sn.estm.service_consultation.clients.BonReponse;
import sn.estm.service_consultation.clients.PatientReponse;
import sn.estm.service_consultation.dto.ConsultationReponse;
import sn.estm.service_consultation.dto.ConsultationRequete;
import sn.estm.service_consultation.entities.Consultation;
import sn.estm.service_consultation.repositories.ConsultationRepository;

@Service
@RequiredArgsConstructor
public class ConsultationService {
	private final ConsultationRepository consultationRepo;
	private final PatientFeignClient patientFeignClient;

	public Consultation Traitement(UUID pid, ConsultationRequete consultationReq) {
		// Récupération des bons du patient via Feign Client
		List<BonReponse> bonReponse = patientFeignClient.afficherListeBonPatient(pid);

		if (bonReponse == null || bonReponse.isEmpty()) {
			throw new RuntimeException("Aucun bon trouvé pour ce patient.");
		}
		Long bonIdLibre = null;
		// verifier si le dernier bon de consultation acheté est utilise pour la
		// consultation
		for (BonReponse bon : bonReponse) {
			boolean existBonConsult = consultationRepo.existsByBid(bon.getBid());
			if (!existBonConsult) {
				bonIdLibre = bon.getBid();
			}
		}

		if (bonIdLibre == null) {
			throw new RuntimeException("Tous les bons de ce patient ont déjà été utilisés.");
		}
		// Création et sauvegarde de la consultation
		Consultation consultation = new Consultation();
		consultation.setBid(bonIdLibre);
		consultation.setPid(pid);
		transferConsultationRequeteAconsultation(consultation, consultationReq);

		return consultationRepo.save(consultation);
	}

	public List<ConsultationReponse> listeDesPatientConsulter() {

		List<Consultation> consultationList = consultationRepo.findAll();
		return consultationList.stream().map(consul -> {
			ConsultationReponse.PatientDetails patientDetails = null;
			try {
				PatientReponse patient = patientFeignClient.afficheUnPatient(consul.getPid());
				if (patient != null) {
					patientDetails = new ConsultationReponse.PatientDetails(patient.getPnom(), patient.getPrenom(),patient.getNum());
				}
			} catch (FeignException ex) {
				patientDetails = null;
				System.err.println("Erreur d'appel pour le patient dont  pid=" + consul.getPid() + " : " + ex.getMessage());
				ex.printStackTrace();
			}
			return TransfertConsultationAConsultationResponses(consul, patientDetails);
		}).collect(Collectors.toList());
	}

	@Transactional
	public List<ConsultationReponse> rechercherConsultations(String mc) {
		List<Consultation> consultations;
		
		if (mc == null || mc.trim().isEmpty()) {
			consultations = consultationRepo.findAll();
		} else {
			mc = mc.trim();
			try {
				List<PatientReponse> patients = patientFeignClient.rechercherPatients(mc);
				List<UUID> pids = new ArrayList<>();
				if (patients != null) {
					for (PatientReponse patient : patients) {
						if (patient.getPid() != null && !pids.contains(patient.getPid())) {
							pids.add(patient.getPid());
						}
					}
				}
				if (pids.isEmpty()) {
					consultations = consultationRepo.rechercherSansPatients(mc);
				} else {
					consultations = consultationRepo.rechercherAvecPatients(pids, mc);
				}
			} catch (FeignException e) {
				consultations = consultationRepo.rechercherSansPatients(mc);
			}
		}

		// Convertir avec la récupération des détails du patient
		return consultations.stream().map(consul -> {
			ConsultationReponse.PatientDetails patientDetails = null;
			try {
				PatientReponse patient = patientFeignClient.afficheUnPatient(consul.getPid());
				if (patient != null) {
					patientDetails = new ConsultationReponse.PatientDetails(patient.getPnom(), patient.getPrenom(),patient.getNum());
				}
			} catch (FeignException ex) {
				patientDetails = null;
				System.err.println("Erreur d'appel pour le patient dont pid=" + consul.getPid() + " : " + ex.getMessage());
			}
			return TransfertConsultationAConsultationResponses(consul, patientDetails);
		}).collect(Collectors.toList());
	}
		public void suprimerConsultation(Long cid) {
		if (consultationRepo.findById(cid).isPresent()) {
			consultationRepo.deleteById(cid);
		} else {
			throw new RuntimeException("La consultation du patient avec l'ID de consultation " + cid + " n'existe pas");
		}
	}

	public void transferConsultationRequeteAconsultation(Consultation consultation, ConsultationRequete consultationReq) {
		consultation.setDiagn(consultationReq.getDiagn());
		consultation.setSymp(consultationReq.getSymp());
	}

	public ConsultationReponse TransfertConsultationAConsultationResponse(Consultation consultation) {
		ConsultationReponse consultationReponse = new ConsultationReponse();
		consultationReponse.setCid(consultation.getCid());
		consultationReponse.setPid(consultation.getPid());
		consultationReponse.setBid(consultation.getBid());
		consultationReponse.setDiagn(consultation.getDiagn());
		consultationReponse.setSymp(consultation.getSymp());
		consultationReponse.setPatientDetails(null);
		return consultationReponse;
	}

	public ConsultationReponse TransfertConsultationAConsultationResponses(Consultation consultation,
			ConsultationReponse.PatientDetails patientDetails) {
		ConsultationReponse consultationReponse = TransfertConsultationAConsultationResponse(consultation);
		consultationReponse.setPatientDetails(patientDetails);
		return consultationReponse;
	}
}
