package sn.estm.service_consultation.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.estm.service_consultation.client.config.PatientFeignClient;
import sn.estm.service_consultation.clients.PatientReponse;
import sn.estm.service_consultation.dto.MedicamentReponse;
import sn.estm.service_consultation.dto.MedicamentRequete;
import sn.estm.service_consultation.entities.Consultation;
import sn.estm.service_consultation.entities.Medicament;
import sn.estm.service_consultation.repositories.ConsultationRepository;
import sn.estm.service_consultation.repositories.MedicamentRepository;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MedicamentService {

	private final MedicamentRepository medicamentRepo;
	private final ConsultationRepository consultationRepo;
	private final PatientFeignClient patientFeignClient;

	public MedicamentReponse creerMedicament(MedicamentRequete medicamentRequete) {
		if (medicamentRequete.getConsultation() == null || medicamentRequete.getConsultation().getPid() == null) {
			throw new RuntimeException("Le pid de la consultation est obligatoire");
		}

		UUID pid = medicamentRequete.getConsultation().getPid();
		Consultation consultation = consultationRepo.findByPid(pid);

		if (consultation == null) {
			log.warn("Consultation introuvable pour pid={}", pid);
			throw new RuntimeException("Consultation non trouvée");
		}

		PatientReponse patient = patientFeignClient.afficheUnPatient(pid);

		Medicament medicament = new Medicament();
		medicament.setConsultation(consultation);
		transferMedicamentRequeteAMedicament( medicament,  medicamentRequete);
		Medicament saveMedicament = medicamentRepo.save(medicament);

		MedicamentReponse.PatientDetails patientDetails = new MedicamentReponse.PatientDetails();
		if (patient != null) {
			patientDetails.setPnom(patient.getPnom());
			patientDetails.setPrenom(patient.getPrenom());
		}
		return transfertmedicamentAmedicamentResponses(saveMedicament, patientDetails);
	}

	public List<MedicamentReponse> listeMedicamentPatient() {
		List<Medicament> medicamentListe = medicamentRepo.findAll();
		return medicamentListe.stream().map((medicament -> {
			Long cid = medicament.getConsultation().getCid();
			Consultation consultation = consultationRepo.findById(cid)
					.orElseThrow(() -> new RuntimeException("Consultation introuvable"));
			UUID pid = consultation.getPid();
			PatientReponse patient = patientFeignClient.afficheUnPatient(pid);
			if (patient == null) {
				log.warn("patient introuvable ", pid);
				throw new RuntimeException("patient non trouvé");
			}
			MedicamentReponse.PatientDetails patientDetails = new MedicamentReponse.PatientDetails();
			if (patient != null) {
				patientDetails.setPnom(patient.getPnom());
				patientDetails.setPrenom(patient.getPrenom());
			}
			return transfertmedicamentAmedicamentResponses(medicament, patientDetails);
		})).collect(Collectors.toList());

	}
	public List<MedicamentReponse> ListeDateMedicament(LocalDate date) {
		return medicamentRepo.findByDateCrDateOnly(date).stream().map(medicament -> {
			Long cid = medicament.getConsultation().getCid();
			Consultation consultation = consultationRepo.findById(cid)
					.orElseThrow(() -> new RuntimeException("Consultation introuvable"));
			UUID pid = consultation.getPid();
			PatientReponse patient = patientFeignClient.afficheUnPatient(pid);
			if (patient == null) {
				log.warn("patient introuvable ", pid);
				throw new RuntimeException("patient non trouvé");
			}

			MedicamentReponse.PatientDetails patientDetails = new MedicamentReponse.PatientDetails();
			if (patient != null) {
				patientDetails.setPnom(patient.getPnom());
				patientDetails.setPrenom(patient.getPrenom());
			}
			return transfertmedicamentAmedicamentResponses(medicament, patientDetails);
		}).collect(Collectors.toList());
	}

	public List<MedicamentReponse> rechercherMedicaments(String mc) {
		List<Medicament> medicaments;

		if (mc == null || mc.isBlank()) {
		    medicaments = medicamentRepo.findAll();
		} else {
		    medicaments = medicamentRepo.rechercherParMotCle(mc.trim());
		}

	    // Un seul appel Feign par patient
	    Map<UUID, MedicamentReponse.PatientDetails> cache = new HashMap<>();

	    return medicaments.stream().map(medicament -> {
	        UUID pid = medicament.getConsultation().getPid();
	        MedicamentReponse.PatientDetails details =
	                cache.computeIfAbsent(pid, this::chargerPatientDetails);
	        return transfertmedicamentAmedicamentResponses(medicament, details);
	    }).collect(Collectors.toList());
	}

	private MedicamentReponse.PatientDetails chargerPatientDetails(UUID pid) {
	    MedicamentReponse.PatientDetails details = new MedicamentReponse.PatientDetails();
	    try {
	        PatientReponse patient = patientFeignClient.afficheUnPatient(pid);
	        if (patient != null) {
	            details.setPnom(patient.getPnom());
	            details.setPrenom(patient.getPrenom());
	        }
	    } catch (Exception e) {
	        log.warn("Impossible de charger le patient pid={}", pid, e);
	    }
	    return details;
	}
	public void suprimerConsultation(long cid) {
		if (medicamentRepo.findById(cid).isPresent()) {
			medicamentRepo.deleteById(cid);
		} else {
			throw new RuntimeException("Le patient avec l'ID " + cid + " n'existe pas");
		}
	}

	public void transferMedicamentRequeteAMedicament(Medicament medicament, MedicamentRequete medicamentReq) {

		medicament.setMedNom(medicamentReq.getMedNom());
		medicament.setDose(medicamentReq.getDose());
		medicament.setDateCr(LocalDateTime.now());
		medicament.setDateM(LocalDateTime.now());
	}

	public MedicamentReponse transfertmedicamentAmedicamentResponse(Medicament medicament) {
		MedicamentReponse medicamentReponse = new MedicamentReponse();
		medicamentReponse.setMedNom(medicament.getMedNom());
		medicamentReponse.setDose(medicament.getDose());
		medicamentReponse.setDateCr(medicament.getDateCr());
		medicamentReponse.setDateM(medicament.getDateM());
		medicamentReponse.setCid(medicament.getConsultation().getCid());
		return medicamentReponse;

	}

	public MedicamentReponse transfertmedicamentAmedicamentResponses(Medicament medicament,
		MedicamentReponse.PatientDetails patientDetails) {
		MedicamentReponse medicamentReponse = new MedicamentReponse();
		medicamentReponse.setMid(medicament.getMid());
		medicamentReponse.setMedNom(medicament.getMedNom());
		medicamentReponse.setDose(medicament.getDose());
		medicamentReponse.setDateCr(medicament.getDateCr());
		medicamentReponse.setDateM(medicament.getDateM());
		medicamentReponse.setCid(medicament.getConsultation().getCid());
		medicamentReponse.setPatientDetails(patientDetails);
		return medicamentReponse;

	}
}
