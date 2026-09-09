package sn.estm.service_constante.controllers;

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
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import sn.estm.service_constante.dto.ConstanteReponse;
import sn.estm.service_constante.dto.ConstanteRequete;
import sn.estm.service_constante.services.ConstanteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/constante")
public class ConstanteController {

	private final ConstanteService constanteService;

	@PostMapping("/{pid}")
	public ResponseEntity<ConstanteReponse> ajoutConstante(@PathVariable UUID pid,
			@Valid @RequestBody ConstanteRequete contanteRequete) {
		ConstanteReponse constante = constanteService.CreerConstante(pid,contanteRequete);
	    if(constante==null) {
	    	ResponseEntity.noContent().build();
	    }
		return ResponseEntity.status(HttpStatus.CREATED).body(constante);
	}

	@GetMapping("/liste")
	public ResponseEntity<?> listerToutesLesConstante() {
		List<ConstanteReponse> constante = constanteService.listeDesConstantePatient();
		if (constante.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(constante);
	}

	@DeleteMapping("/{constid}")
	public ResponseEntity<?> supprimerConsultation(@PathVariable Long constid) {
		constanteService.suprimerConstante(constid);
		return ResponseEntity.ok("constante supprimé avec succès");
	}
}
