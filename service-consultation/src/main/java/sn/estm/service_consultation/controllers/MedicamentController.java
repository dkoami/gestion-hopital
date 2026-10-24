package sn.estm.service_consultation.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
import sn.estm.service_consultation.dto.MedicamentReponse;
import sn.estm.service_consultation.dto.MedicamentRequete;
import sn.estm.service_consultation.services.MedicamentService;

@RestController
@RequestMapping("/api/medicament")
@RequiredArgsConstructor
public class MedicamentController {

	private final MedicamentService medicamentService;
	
	@PostMapping("/ajout")
	public ResponseEntity<MedicamentReponse> ajoutMedicament(@Valid @RequestBody MedicamentRequete medicamentRequete) {
		MedicamentReponse medicament = medicamentService.creerMedicament(medicamentRequete);
		return ResponseEntity.status(HttpStatus.CREATED).body(medicament);
	}
	@GetMapping("/liste")
	public ResponseEntity<?> listerToutesMedicamentPatient() {
		List<MedicamentReponse> medicamentReponse = medicamentService.listeMedicamentPatient();
		if (medicamentReponse .isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(medicamentReponse);
	}
	@GetMapping("/date")
	public ResponseEntity<List<MedicamentReponse>> DateMedicament(@RequestParam("date") LocalDate dateCr){
		List<MedicamentReponse> reponseMed= medicamentService.ListeDateMedicament(dateCr);
		if(reponseMed  .isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(reponseMed);
	}
	@GetMapping("/recherche")
	public List<MedicamentReponse> rechercherMedicaments(@RequestParam("mc") String mc) {
	    return medicamentService.rechercherMedicaments(mc);
	}
	@DeleteMapping("/{mid}")
	public ResponseEntity<?> supprimerPatient(@PathVariable Long mid) {
		medicamentService.suprimerConsultation(mid);
	    return ResponseEntity.ok("Medicament prescrit supprimé avec succès");
	}

}
