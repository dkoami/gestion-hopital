package sn.estm.service_patient.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.estm.service_patient.entities.Adresse;
import sn.estm.service_patient.entities.Patient;
import sn.estm.service_patient.repositories.PatientRypository;
import sn.estm.service_patient.dto.PatientReponse;
import sn.estm.service_patient.dto.PatientRequete;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientService {
   private final PatientRypository patientRepo;
   
   
	public PatientReponse EnregistrePanient(PatientRequete patientRequete) {
		int numAleatoire = 1000 + (int)(Math.random() * 9000);
        boolean existe = patientRepo.existsByPnomIgnoreCaseAndPrenomIgnoreCase(
            patientRequete.getPnom(), 
            patientRequete.getPrenom()
        );
        if (existe) {
        	return null;
        }
        else {
        	Patient patient = new Patient();
            TransferPatientRequeteApatient( patient,patientRequete);
            patient.setNum(numAleatoire);
            Patient savePatient = patientRepo.save(patient);
            return TrasferPatientApatientReponse(savePatient);
        }
	}
	
	public Optional<PatientReponse>miseAjourPatient(UUID id, PatientRequete patientRequete) {
	    return patientRepo.findById(id)
	            .map(existePatient -> {
	                TransferPatientRequeteApatient(existePatient, patientRequete);
	                existePatient.setDateM(LocalDateTime.now());
	                Patient patient = patientRepo.save(existePatient);
	                return TrasferPatientApatientReponse(patient);
	            });
	}
	
	
	public List<PatientReponse> listePatients(){
		return patientRepo.findAll().stream()
				.map(this::TrasferPatientApatientReponse)
				.collect(Collectors.toList());
	}
	public PatientReponse afficherUnPatient(UUID id) {
		return patientRepo.findById(id)
				.map(this::TrasferPatientApatientReponse)
				.orElseThrow(()-> new RuntimeException("Patient non trouvé :" + id));
		
	}
	public List<PatientReponse> rechercherPatients(String motCle) {
	    if (motCle == null || motCle.trim().isEmpty()) {
	        return listePatients(); // Si le mot-clé est vide, on renvoie tous les patients
	    }
	    
	    return patientRepo.chercherParMotCle(motCle.trim()).stream()
	            .map(this::TrasferPatientApatientReponse)
	            .collect(Collectors.toList());
	}
	public void TransferPatientRequeteApatient( Patient patient,PatientRequete patientRequete) {
	    patient.setPnom(patientRequete.getPnom());
	    patient.setPrenom(patientRequete.getPrenom());
	    patient.setAge(patientRequete.getAge());
	    patient.setProfession(patientRequete.getProfession());
	    patient.setSexe(patientRequete.getSexe());
	    // Vérifier si une adresse est envoyée dans la requête
	    if (patientRequete.getAdresseDto() != null) {
	        if (patient.getAdresse() != null) {
	            // L'adresse existe déjà :
	            // on modifie seulement ses champs
	            patient.getAdresse().setVille(patientRequete.getAdresseDto().getVille());
	            patient.getAdresse().setQuart(patientRequete.getAdresseDto().getQuart());
	        } else {
	            // Le patient n'a pas encore d'adresse :
	            // on crée une nouvelle adresse
	            Adresse adresse = new Adresse();
	            adresse.setVille(patientRequete.getAdresseDto().getVille());
	            adresse.setQuart(patientRequete.getAdresseDto().getQuart());
	            patient.setAdresse(adresse);
	        }
	    }
	}
	public PatientReponse TrasferPatientApatientReponse(Patient patient) {
		PatientReponse patientReponse= new PatientReponse();
		patientReponse.setPid(patient.getPid());
		patientReponse.setPnom(patient.getPnom());
		patientReponse.setPrenom(patient.getPrenom());
		patientReponse.setAge(patient.getAge());
		patientReponse.setAdresse(patient.getAdresse());
		patientReponse.setProfession(patient.getProfession());
		patientReponse.setSexe(patient.getSexe());
		patientReponse.setNum(patient.getNum());
		patientReponse.setDateCr(patient.getDateCr());
		patientReponse.setDateM(patient.getDateM());
		return patientReponse;
	}
	 public void suprimerPatient(UUID pid) {
	        if (patientRepo.findById(pid).isPresent()) {
	            patientRepo.deleteById(pid);
	        } else {
	        	 throw new RuntimeException(
	        	            "Le patient avec l'ID " + pid + " n'existe pas"
	        	        );
	        }
	    }
}
