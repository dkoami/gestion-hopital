package sn.estm.service_stock.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import sn.estm.service_stock.dto.CategoriReponse;
import sn.estm.service_stock.dto.CategoriRequete;
import sn.estm.service_stock.servcies.CategoriService;

@RestController
@RequestMapping("/api/categori")
@RequiredArgsConstructor
public class CategoriController {

private final CategoriService categoriService;
	
	@PostMapping("/ajout")
	public ResponseEntity<CategoriReponse> ajoutCategori(
			@Valid @RequestBody CategoriRequete categoriRequete) {
		CategoriReponse reponse= categoriService.ajoutCategori( categoriRequete);
		return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
	}
	@PutMapping("/{caid}")
    public ResponseEntity<CategoriReponse> miseAjourCategori(@PathVariable("caid") Long caid, @Valid @RequestBody CategoriRequete categoriRequete) {
        CategoriReponse reponse = categoriService.miseAjourCategori(caid, categoriRequete);
        return ResponseEntity.ok(reponse);
    }
	@GetMapping("/liste")
	public ResponseEntity<?> listerToutesLesCategori() {
		List<CategoriReponse> categori = categoriService.listeCategori();
		return ResponseEntity.ok(categori);
	}
	@DeleteMapping("/{caid}")
	public ResponseEntity<Void> suprimer(@PathVariable Long caid) {
	     categoriService.suprimerCategori(caid);
	     return ResponseEntity.noContent().build();
	}
	@GetMapping("/recherche")
	public ResponseEntity<List<CategoriReponse>> rechercheCategori(@RequestParam String keyword) {
	   return ResponseEntity.ok(categoriService.rechercheCategori(keyword));
	}
}
