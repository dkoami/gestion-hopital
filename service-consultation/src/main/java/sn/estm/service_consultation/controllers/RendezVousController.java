package sn.estm.service_consultation.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.estm.service_consultation.Enums.StatutRendezVous;
import sn.estm.service_consultation.dto.RendezVousReponse;
import sn.estm.service_consultation.dto.RendezVousRequete;
import sn.estm.service_consultation.services.RendezVousService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/rendez-vous")
public class RendezVousController {

    private final RendezVousService rendezVousService;

	@PostMapping()
	public ResponseEntity<RendezVousReponse> ajoutRendezVous(@Valid @RequestBody RendezVousRequete rendezRequete) {
		RendezVousReponse rendezVous= rendezVousService.creerRendenzVouz(rendezRequete);
		return ResponseEntity.status(HttpStatus.CREATED).body(rendezVous);
	}
	@GetMapping("/liste")
	public ResponseEntity<List<RendezVousReponse>> listerRendezVous() {
	    return ResponseEntity.ok(rendezVousService.listerRendezVous());
	}
	@PatchMapping("/{id}/statut")
    public ResponseEntity<RendezVousReponse> changerStatut(
            @PathVariable("id") Long id,
            @RequestParam("nouveauStatut") StatutRendezVous nouveauStatut) {
        RendezVousReponse reponse = rendezVousService.changerStatut(id, nouveauStatut);
        return ResponseEntity.ok(reponse);
    }
	
}
