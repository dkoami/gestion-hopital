package sn.estm.service_consultation.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sn.estm.service_consultation.dto.SejourReponse;
import sn.estm.service_consultation.dto.SejourRequete;
import sn.estm.service_consultation.services.SejourService;

@RestController
@RequestMapping("/api/sejour")
@RequiredArgsConstructor
public class SejourController {

	private final SejourService sejourService;
	@PostMapping()
	public ResponseEntity<SejourReponse> ajoutSejour( @RequestBody SejourRequete sejourRequete) {
		
	    SejourReponse sejour = sejourService.creerSejour(sejourRequete);
		return ResponseEntity.status(HttpStatus.CREATED).body(sejour);
	}
	@GetMapping("/liste")
	public ResponseEntity<?> listerToutesLesSejour() {
		List<SejourReponse> sejour = sejourService.listeSejour();
		if (sejour.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(sejour);
	}
	@PatchMapping("/{sid}/terminer")
    public ResponseEntity<SejourReponse> terminerSejour(@PathVariable Long sid) {
		
        return ResponseEntity.ok(sejourService.terminerSejour(sid));
    }

    @PatchMapping("/{sid}/annuler")
    public ResponseEntity<SejourReponse> annulerSejour(@PathVariable Long sid) {
        return ResponseEntity.ok(sejourService.annulerSejour(sid));
    }
	@DeleteMapping("/{sid}")
	public ResponseEntity<?> supprimerPatient(@PathVariable Long sid) {
		sejourService.suprimerSejour(sid);
	    return ResponseEntity.ok("Medicament prescrit supprimé avec succès");
	}
}
