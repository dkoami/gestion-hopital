package sn.estm.service_stock.controllers;

import java.util.List;
import java.util.UUID;

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
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import sn.estm.service_stock.dto.CategoriReponse;
import sn.estm.service_stock.dto.CategoriRequete;
import sn.estm.service_stock.dto.ProduitReponse;
import sn.estm.service_stock.dto.ProduitRequete;
import sn.estm.service_stock.servcies.ProduitService;


@RestController
@RequestMapping("/api/produit")
@RequiredArgsConstructor
@Slf4j
public class ProduitController {
	
	private final ProduitService produitService;
	
	@PostMapping("ajout")
	public ResponseEntity<ProduitReponse> ajoutProduit(
			@Valid @RequestBody ProduitRequete produitRequete) {
		ProduitReponse reponse= produitService.ajoutProduit(produitRequete);

		return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
	}

	@GetMapping("/liste")
	public ResponseEntity<?> listerToutesLesProduits() {
		List<ProduitReponse> produits = produitService.listeProduits();
		return ResponseEntity.ok(produits);
	}
	@PutMapping("/{prid}")
    public ResponseEntity<ProduitReponse> miseAjourCategori(@PathVariable("prid") Long prid, @Valid @RequestBody ProduitRequete produitRequete) {
        ProduitReponse reponse = produitService.miseAjourProduit(prid, produitRequete);
        return ResponseEntity.ok(reponse);
    }

	@DeleteMapping("/{prid}")
	public ResponseEntity<Void> updateProduct(@PathVariable Long prid) {
	     boolean sup= produitService.suprimerProduit(prid);
	           return sup? ResponseEntity.noContent().build():ResponseEntity.notFound().build();
	}
	@GetMapping("/recherche")
	public ResponseEntity<List<ProduitReponse>> searchProducts(@RequestParam String keyword) {
	   return ResponseEntity.ok(produitService.rechercheProduits(keyword));
	}
}
