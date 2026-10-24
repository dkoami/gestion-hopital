package sn.estm.service_patient.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sn.estm.service_patient.dto.BonReponse;
import sn.estm.service_patient.dto.BonRequete;
import sn.estm.service_patient.entities.Bon;
import sn.estm.service_patient.entities.Patient;
import sn.estm.service_patient.entities.TypeConsultation;
import sn.estm.service_patient.repositories.BonRepository;
import sn.estm.service_patient.repositories.PatientRypository;

@Service
@RequiredArgsConstructor
public class BonService {
	 private final BonRepository bonRepo;
	 private final PatientRypository patientRepo;
	 
	public BonReponse creerBon(BonRequete bonRequete) {
		 Patient patient = patientRepo.findById(bonRequete.getPatient().getPid())
	                .orElseThrow(() -> new RuntimeException(
	                        "Patient non trouvé : " + bonRequete.getPatient().getPid()));
		 Bon bon = new Bon();
		 TransferBonRequeteAbon(bon, bonRequete,patient);
		 Bon saveBon = bonRepo.save(bon);
		 return TransfertBonABonResponse(saveBon);     	
	}
	public List<BonReponse> listeBons() {
        return bonRepo.findAll().stream()
                .map(this::TransfertBonABonResponse)
                .collect(Collectors.toList());
    }
	public void suprimerBon(Long bid) {
        if (bonRepo.findById(bid).isPresent()) {
            bonRepo.deleteById(bid);
        } else {
            throw new RuntimeException("Le bon avec l'ID " + bid + " n'existe pas");
        }
    }
    public BonReponse afficherUnBon(Long id) {
        return bonRepo.findById(id)
                .map(this::TransfertBonABonResponse)
                .orElseThrow(() -> new RuntimeException("Bon non trouvé : " + id));
    }
    public List<BonReponse> afficherListeBonPatient(UUID id) {
        List<Bon> listBon= bonRepo.findByPatientPid(id);
        return listBon.stream().map(bon->TransfertBonABonResponse( bon))
        		                .collect(Collectors.toList());
    }
    public Optional<BonReponse> miseAjourBon(Long bid, BonRequete bonRequete) {
    	 return bonRepo.findById(bid)
    	            .map(existeBon -> {
    	                Patient patient = patientRepo.findById(bonRequete.getPatient().getPid())
    	                        .orElseThrow(() -> new RuntimeException(
    	                                "Patient non trouvé : " + bonRequete.getPatient().getPid()));
    	                TransferBonRequeteAbon(existeBon, bonRequete,patient);
    	                existeBon.setDateM(LocalDateTime.now());
    	                Bon bon = bonRepo.save(existeBon);
    	                return TransfertBonABonResponse(bon);
    	            });
    }
	 public void TransferBonRequeteAbon(Bon bon, BonRequete bonRequete,Patient patient) {
	        TypeConsultation type = bonRequete.getTypeconsultation();
	        bon.setTypeConsultation(type);
	        bon.setPrix(determinerPrix(type));
	        bon.setNomPartiP(bonRequete.getNomPartiP());
	        bon.setDesignation(bonRequete.getDesignation());
	        bon.setDateCr(LocalDateTime.now());
	        bon.setDateM(LocalDateTime.now());
	        bon.setPatient(patient);
	    }
	 public BonReponse TransfertBonABonResponse(Bon bon){
		        BonReponse bonReponse = new BonReponse();
		        bonReponse.setBid(bon.getBid());
		        bonReponse.setPrix(bon.getPrix());
		        bonReponse.setNomPartiP(bon.getNomPartiP());
		        bonReponse.setDesignation(bon.getDesignation());
		        bonReponse.setPatient(bon.getPatient());
		        bonReponse.setTypeconsultation(bon.getTypeConsultation());
		        bonReponse.setDateCr(bon.getDateCr());
		        bonReponse.setDateM(bon.getDateM());
		        return bonReponse;
		    }
	 private Double determinerPrix(TypeConsultation type) {

		    return switch (type) {
		        case GENERALE -> 3000.0;
		        case SPECIALISTE -> 5000.0;
		        case URGENCE -> 5000.0;
		        case SUIVI -> 3000.0;
		    };
		}
}
