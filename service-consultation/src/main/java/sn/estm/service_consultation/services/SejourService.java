package sn.estm.service_consultation.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sn.estm.service_consultation.Enums.StatutSejour;
import sn.estm.service_consultation.client.config.PatientFeignClient;
import sn.estm.service_consultation.clients.PatientReponse;
import sn.estm.service_consultation.dto.SejourReponse;
import sn.estm.service_consultation.dto.SejourRequete;
import sn.estm.service_consultation.entities.Consultation;
import sn.estm.service_consultation.entities.Sejour;
import sn.estm.service_consultation.exception.SejourDejaExistantException;
import sn.estm.service_consultation.repositories.ConsultationRepository;
import sn.estm.service_consultation.repositories.SejourRepository;

@Service
@RequiredArgsConstructor
public class SejourService {

	private final PatientFeignClient patientFeignClient;
	private final ConsultationRepository consultationRepo;
	private final SejourRepository sejourRepo;

	public SejourReponse creerSejour(SejourRequete sejourRequete) {

	    if (sejourRequete.getConsultation() == null
	            || sejourRequete.getConsultation().getCid() == null
	            || sejourRequete.getConsultation().getPid() == null) {
	        throw new RuntimeException("Consultation du patient introuvable.");
	    }

	    Long cid = sejourRequete.getConsultation().getCid();

	    if (sejourRepo.existsByConsultationCid(cid)) {
	        throw new SejourDejaExistantException(
	            "Un séjour existe déjà pour cette consultation."
	        );
	    }
	    Consultation consultation = consultationRepo.findById(cid)
	        .orElseThrow(() ->
	            new RuntimeException(
	                "Consultation introuvable avec l'id : " + cid
	            )
	        );

	    UUID pid = consultation.getPid();
	    PatientReponse patient = patientFeignClient.afficheUnPatient(pid);
	    if (patient == null) {
	        throw new RuntimeException("Patient non trouvé.");
	    }

	    Sejour sejour = new Sejour();
	    sejour.setConsultation(consultation);
	    sejour.setDateCr(LocalDate.now());
	    sejour.setDateSortie(null);
	    sejour.setStatut(StatutSejour.EN_COURS);

	    transferSejourRequeteAsejour(sejour, sejourRequete);
	    Sejour saveSejour = sejourRepo.save(sejour);
	    return transfertSejourAsejourResponse(saveSejour);
	}

	public List<SejourReponse> listeSejour() {
		return sejourRepo.findAll().stream().map(sejour -> {
			// Vérification de sécurité sur la consultation et le PID
			if (sejour.getConsultation() == null || sejour.getConsultation().getPid() == null) {
				throw new RuntimeException("Consultation ou patient non trouvé pour le séjour ID : " + sejour.getSid());
			}

			UUID pid = sejour.getConsultation().getPid();
			PatientReponse patient = patientFeignClient.afficheUnPatient(pid);

			SejourReponse.PatientDetails patientDetails = new SejourReponse.PatientDetails();
			patientDetails.setPnom(patient.getPnom());
			patientDetails.setPrenom(patient.getPrenom());

			return transfertSejourAsejourResponses(sejour, patientDetails);
		}).collect(Collectors.toList());
	}

	public SejourReponse afficherUnSejour(Long sid) {

		Sejour sejour = sejourRepo.findById(sid)
				.orElseThrow(() -> new RuntimeException("Le séjour avec l'ID " + sid + " n'existe pas."));

		if (sejour.getConsultation() == null || sejour.getConsultation().getPid() == null) {
			throw new RuntimeException("Consultation ou patient introuvable pour ce séjour.");
		}

		PatientReponse patient = patientFeignClient.afficheUnPatient(sejour.getConsultation().getPid());

		if (patient == null) {
			throw new RuntimeException("Patient introuvable.");
		}

		SejourReponse.PatientDetails patientDetails = new SejourReponse.PatientDetails();
		patientDetails.setPnom(patient.getPnom());
		patientDetails.setPrenom(patient.getPrenom());

		return transfertSejourAsejourResponses(sejour, patientDetails);
	}

	public SejourReponse terminerSejour(Long sid) {

		Sejour sejour = sejourRepo.findById(sid)
				.orElseThrow(() -> new RuntimeException("Le séjour avec l'ID " + sid + " n'existe pas."));

		if (sejour.getStatut() == StatutSejour.TERMINE) {
			throw new RuntimeException("Ce séjour est déjà terminé.");
		}

		if (sejour.getStatut() == StatutSejour.ANNULE) {
			throw new RuntimeException("Impossible de terminer un séjour annulé.");
		}

		LocalDate maintenant = LocalDate.now();

		sejour.setStatut(StatutSejour.TERMINE);
		sejour.setDateSortie(maintenant);

		Sejour sejourEnregistre = sejourRepo.save(sejour);

		return transfertSejourAsejourResponse(sejourEnregistre);
	}

	 /**
     * Annuler un séjour qui n'est pas encore terminé.
     */
    public SejourReponse annulerSejour(Long sid) {

        Sejour sejour = sejourRepo.findById(sid)
                .orElseThrow(() -> new RuntimeException(
                        "Le séjour avec l'ID " + sid + " n'existe pas."));

        if (sejour.getStatut() == StatutSejour.TERMINE) {
            throw new RuntimeException(
                    "Impossible d'annuler un séjour déjà terminé.");
        }

        if (sejour.getStatut() == StatutSejour.ANNULE) {
            throw new RuntimeException(
                    "Ce séjour est déjà annulé.");
        }

        sejour.setStatut(StatutSejour.ANNULE);
        Sejour sejourEnregistre = sejourRepo.save(sejour);

        return transfertSejourAsejourResponse(sejourEnregistre);
    }

    

    
	public void suprimerSejour(long sid) {
		if (sejourRepo.findById(sid).isPresent()) {
			sejourRepo.deleteById(sid);
		} else {
			throw new RuntimeException("Le sejour avec l'ID " + sid + " n'existe pas");
		}
	}

	public void transferSejourRequeteAsejour(Sejour sejour, SejourRequete sejourReq) {
		sejour.setDateCr(LocalDate.now());
		sejour.setConsultation(sejour.getConsultation());
	}

	public SejourReponse transfertSejourAsejourResponse(Sejour sejour) {
		SejourReponse sejourReponse = new SejourReponse();
		sejourReponse.setCid(sejour.getConsultation().getCid());
		sejourReponse.setSid(sejour.getSid());
		sejourReponse.setDateCr(sejour.getDateCr());
		sejourReponse.setDateSortie(sejour.getDateSortie());
		sejourReponse.setStatut(sejour.getStatut());
		return sejourReponse;
	}

	public SejourReponse transfertSejourAsejourResponses(Sejour sejour, SejourReponse.PatientDetails patientDetails) {

		SejourReponse sejourReponse = new SejourReponse();
		sejourReponse.setCid(sejour.getConsultation().getCid());
		sejourReponse.setSid(sejour.getSid());
		sejourReponse.setDateCr(sejour.getDateCr());
		sejourReponse.setDateSortie(sejour.getDateSortie());
		sejourReponse.setStatut(sejour.getStatut());
		sejourReponse.setPatientDetails(patientDetails);
		return sejourReponse;

	}
}
