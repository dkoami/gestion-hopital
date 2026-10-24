package sn.estm.service_consultation.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.estm.service_consultation.dto.ConsultationReponse;
import sn.estm.service_consultation.dto.ConsultationRequete;
import sn.estm.service_consultation.entities.Consultation;
import sn.estm.service_consultation.services.ConsultationService;

@RestController
@RequestMapping("/api/consultation")
@RequiredArgsConstructor
public class ConsultationController {

    private final ConsultationService consultationService;

    @PostMapping("/{pid}")
    public ResponseEntity<ConsultationReponse> ajoutConsultation(
            @PathVariable UUID pid,
            @Valid @RequestBody ConsultationRequete consultationRequete) {
        Consultation consultationSauvegardee = consultationService.Traitement(pid, consultationRequete);
        ConsultationReponse response = consultationService
                .TransfertConsultationAConsultationResponse(consultationSauvegardee);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/liste")
    public ResponseEntity<?> listerToutesLesConsultations() {
        List<ConsultationReponse> consultations = consultationService.listeDesPatientConsulter();
        if (consultations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(consultations);
    }

    @GetMapping("/recherche")
    public ResponseEntity<List<ConsultationReponse>> rechercherConsultations(
            @RequestParam(required = false) String mc) {
        List<ConsultationReponse> consultations = consultationService.rechercherConsultations(mc);
        if (consultations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(consultations);
    }

    @DeleteMapping("/{cid}")
    public ResponseEntity<?> supprimerConsultation(@PathVariable Long cid) {
        consultationService.suprimerConsultation(cid);
        return ResponseEntity.ok("consultation supprimé avec succès");
    }
}