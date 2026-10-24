package sn.estm.service_consultation.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import sn.estm.service_consultation.Enums.StatutRendezVous;
import sn.estm.service_consultation.client.config.PatientFeignClient;
import sn.estm.service_consultation.clients.PatientReponse;
import sn.estm.service_consultation.dto.RendezVousReponse;
import sn.estm.service_consultation.dto.RendezVousReponse.PatientDetails;
import sn.estm.service_consultation.dto.RendezVousRequete;
import sn.estm.service_consultation.entities.Consultation;
import sn.estm.service_consultation.entities.RendezVous;
import sn.estm.service_consultation.repositories.ConsultationRepository;
import sn.estm.service_consultation.repositories.RendezVousRepository;

@Service
@RequiredArgsConstructor
@Transactional
public class RendezVousService {

    private final RendezVousRepository rendezVousRepo;
    private final ConsultationRepository consultationRepo;
    private final PatientFeignClient patientFeignClient;
    
    public RendezVousReponse creerRendenzVouz(RendezVousRequete rendezVousReq) {
        if (rendezVousReq == null || rendezVousReq.getConsultation() == null || rendezVousReq.getConsultation().getPid() == null) {
            throw new RuntimeException("Le patient doit être consulté");
        }
        
        UUID pid = rendezVousReq.getConsultation().getPid();
        Consultation consultation = consultationRepo.findByPid(pid);
        if (consultation == null) {
            throw new RuntimeException("La consultation n'est pas effectuée");
        }
        
        PatientReponse patient = patientFeignClient.afficheUnPatient(pid);
        if (patient == null) {
            throw new RuntimeException("Patient non trouvé");
        }
        
        RendezVous rendezVous = new RendezVous();
        rendezVous.setStatus(StatutRendezVous.CONFIRME);
        rendezVous.setConsultation(consultation);
        transferRendezVousRequeteARendezVous(rendezVous, rendezVousReq);
        
        RendezVous saveRendezVous = rendezVousRepo.save(rendezVous);
        
        PatientDetails patientDetails = new PatientDetails();
        patientDetails.setPnom(patient.getPnom());
        patientDetails.setPrenom(patient.getPrenom());

        return transfertRendezVouzArendezVousResponses(saveRendezVous, patientDetails);
    }
    
    @Scheduled(cron = "0 */15 * * * *")
    public void mettreAJourStatutsAutomatiquement() {
        LocalDateTime maintenant = LocalDateTime.now();
        
        List<RendezVous> rendezVousExpires = rendezVousRepo
                .findByStatusAndFinBefore(StatutRendezVous.CONFIRME, maintenant);

        for (RendezVous rdv : rendezVousExpires) {
            rdv.setStatus(StatutRendezVous.TERMINE);
        }

        if (!rendezVousExpires.isEmpty()) {
            rendezVousRepo.saveAll(rendezVousExpires);
        }
    }

    public RendezVousReponse changerStatut(Long rid, StatutRendezVous nouveauStatut) {
        RendezVous rendezVous = rendezVousRepo.findById(rid)
                .orElseThrow(() -> new RuntimeException("Rendez-vous non trouvé avec l'id : " + rid));

        validerChangementStatut(rendezVous.getStatus(), nouveauStatut);

        rendezVous.setStatus(nouveauStatut);
        RendezVous updated = rendezVousRepo.save(rendezVous);

        PatientReponse patient = patientFeignClient.afficheUnPatient(updated.getConsultation().getPid());
        PatientDetails patientDetails = new PatientDetails();
        patientDetails.setPnom(patient.getPnom());
        patientDetails.setPrenom(patient.getPrenom());

        return transfertRendezVouzArendezVousResponses(updated, patientDetails);
    }

    private void validerChangementStatut(StatutRendezVous statutActuel, StatutRendezVous nouveauStatut) {
        if (statutActuel == StatutRendezVous.TERMINE) {
            throw new RuntimeException("Un rendez-vous déjà terminé ne peut plus modifier son statut.");
        }
        if (statutActuel == StatutRendezVous.ANNULE) {
            throw new RuntimeException("Un rendez-vous annulé ne peut pas être réactivé ou terminé.");
        }
    }
    @Transactional
    public List<RendezVousReponse> listerRendezVous() {
        return rendezVousRepo.findAll().stream().map(rdv -> {
            PatientReponse p = patientFeignClient.afficheUnPatient(rdv.getConsultation().getPid());
            PatientDetails d = new PatientDetails();
            d.setPnom(p.getPnom());
            d.setPrenom(p.getPrenom());
            return transfertRendezVouzArendezVousResponses(rdv, d);
        }).toList();
    }

    public void transferRendezVousRequeteARendezVous(RendezVous rendezVous, RendezVousRequete rendezVousReq) {
        rendezVous.setCouleur(rendezVousReq.getCouleur());
        rendezVous.setDebut(rendezVousReq.getDebut());
        rendezVous.setFin(rendezVousReq.getFin());
        rendezVous.setTexte(rendezVousReq.getTexte());
    }

    public RendezVousReponse transfertRendezVouzArendezVousResponse(RendezVous rendezVous) {
        RendezVousReponse rendezVousReponse = new RendezVousReponse();
        rendezVousReponse.setConsultation(rendezVous.getConsultation());
        rendezVousReponse.setCouleur(rendezVous.getCouleur());
        rendezVousReponse.setDebut(rendezVous.getDebut());
        rendezVousReponse.setFin(rendezVous.getFin());
        rendezVousReponse.setTexte(rendezVous.getTexte());
        rendezVousReponse.setStatus(rendezVous.getStatus());
        return rendezVousReponse;
    }   

    public RendezVousReponse transfertRendezVouzArendezVousResponses(RendezVous rendezVous, RendezVousReponse.PatientDetails patientDetails) {
        RendezVousReponse rendezVousReponse = transfertRendezVouzArendezVousResponse(rendezVous);
        rendezVousReponse.setPatientDetails(patientDetails);
        return rendezVousReponse;
    }
}