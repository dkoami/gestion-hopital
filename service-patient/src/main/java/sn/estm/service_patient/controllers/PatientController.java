package sn.estm.service_patient.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.estm.service_patient.dto.PatientReponse;
import sn.estm.service_patient.dto.PatientRequete;
import sn.estm.service_patient.services.PatientService;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PatientController {

	private final PatientService patientService;
	
	@PostMapping("/ajout")
	public ResponseEntity<?> ajoutPatient(@Valid @RequestBody PatientRequete patientRequete){
		PatientReponse reponse= patientService.EnregistrePanient(patientRequete);
		if(reponse==null) {
			return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Un patient avec le nom '" + patientRequete.getPnom() 
                          + "' et le prénom '" + patientRequete.getPrenom() + "' existe déjà !");
		}
		return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reponse);
	}
	@PutMapping("/{pid}")
	public ResponseEntity<?> miseAjourPatient(@PathVariable UUID pid,
	                                          @Valid @RequestBody PatientRequete patientRequete) {
	    return patientService.miseAjourPatient(pid, patientRequete)
	            .<ResponseEntity<?>>map(ResponseEntity::ok)
	            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Patient non trouvé : " + pid));
	}
	@GetMapping("/{pid}")
	public ResponseEntity<?> afficheUnPatient(@PathVariable UUID pid){
		try {
	        PatientReponse patient = patientService.afficherUnPatient(pid);
	        return ResponseEntity.ok(patient);
	    } catch (RuntimeException ex) {
	        return ResponseEntity
	                .status(HttpStatus.NOT_FOUND)
	                .body(ex.getMessage());
	    }
	}
	@GetMapping
	public ResponseEntity<List<PatientReponse>> listePatient(){
		return ResponseEntity.ok(patientService.listePatients());
	}
	
	@GetMapping("/recherche")
    public List<PatientReponse> rechercherPatients(@RequestParam(name = "mc", defaultValue = "") String mc) {
        return patientService.rechercherPatients(mc);
    }
	
	@DeleteMapping("/{pid}")
	public ResponseEntity<String> supprimerPatient(@PathVariable UUID pid) {
	    try {
	        patientService.suprimerPatient(pid);
	        return ResponseEntity.ok("Patient supprimé avec succès");
	    } catch (RuntimeException ex) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
	    }
	}
}
