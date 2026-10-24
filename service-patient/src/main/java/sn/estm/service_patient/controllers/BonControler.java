package sn.estm.service_patient.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.estm.service_patient.dto.BonReponse;
import sn.estm.service_patient.dto.BonRequete;
import sn.estm.service_patient.services.BonService;

@RestController
@RequestMapping("/api/bon")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class BonControler {

private final BonService bonService;
	
	@PostMapping("/ajout")
	public ResponseEntity<BonReponse> ajoutBon(@Valid @RequestBody BonRequete bonRequete){
		BonReponse reponse= bonService.creerBon(bonRequete);
		return ResponseEntity
				.status(HttpStatus.CREATED)
                .body(reponse);
	}
	@PutMapping("/{bid}")
	public ResponseEntity< BonReponse> miseAjourPatient(@PathVariable Long bid,@RequestBody BonRequete bonRequete) {
		
		return bonService.miseAjourBon(bid, bonRequete)
				.map(patientReponse->ResponseEntity.ok(patientReponse))
				.orElseThrow(() ->
                new RuntimeException("Patient non trouvé : " + bid));
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> afficheUnBon(@PathVariable Long id){
		try {
	        BonReponse bon = bonService.afficherUnBon(id);
	        return ResponseEntity.ok(bon);
	    } catch (RuntimeException ex) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(ex.getMessage());
	    }
	}
	@GetMapping
	public ResponseEntity<List<BonReponse>> listeBon(){
		return ResponseEntity.ok(bonService.listeBons());
	}
	@GetMapping("/patient/{pid}")
    public ResponseEntity<List<BonReponse>> afficherListeBonPatient(@PathVariable UUID pid) {
        List<BonReponse> listeBons = bonService.afficherListeBonPatient(pid);
        return ResponseEntity.ok(listeBons);
    }
	@DeleteMapping("/{pid}")
	public ResponseEntity<?> supprimerPatient(@PathVariable Long pid) {
	    bonService.suprimerBon(pid);
	    return ResponseEntity.ok("bon supprimé avec succès");
	}

}
