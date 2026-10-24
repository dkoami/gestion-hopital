package sn.estm.service_stock.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import sn.estm.service_stock.dto.VenteReponse;
import sn.estm.service_stock.servcies.VenteService;

@RestController
@RequestMapping("/api/vente")
@RequiredArgsConstructor
public class VenteController {

	private final VenteService venteService;

	@PostMapping
	public ResponseEntity<VenteReponse> CreerVente(HttpSession session) {
		VenteReponse reponse = venteService.CreerVente(session.getId());
		return ResponseEntity.ok(reponse);
	}

	@GetMapping("/liste")
	ResponseEntity<List<VenteReponse>> ListeVente() {
		List<VenteReponse> reponse = venteService.tousVente();
		return ResponseEntity.ok(reponse);
	}
}
