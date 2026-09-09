package sn.estm.service_patient.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import sn.estm.service_patient.dto.PatientReponse;
import sn.estm.service_patient.dto.PatientRequete;
import sn.estm.service_patient.services.PatientService;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
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
	@PutMapping("/{id}")
	public ResponseEntity< PatientReponse> miseAjourPatient(@PathVariable UUID id,@RequestBody PatientRequete patientRequete) {
		
		return patientService.miseAjourPatient(id, patientRequete)
				.map(patientReponse->ResponseEntity.ok(patientReponse))
				.orElseThrow(() ->
                new RuntimeException("Patient non trouvé : " + id));
	}
	@GetMapping("/{id}")
	public ResponseEntity<?> afficheUnPatient(@PathVariable UUID id){
		try {
	        PatientReponse patient = patientService.afficherUnPatient(id);
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
	@DeleteMapping("/{pid}")
	public ResponseEntity<?> supprimerPatient(@PathVariable UUID pid) {
	    patientService.suprimerPatient(pid);
	    return ResponseEntity.ok("Patient supprimé avec succès");
	}
}
