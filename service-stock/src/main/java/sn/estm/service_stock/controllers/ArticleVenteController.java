package sn.estm.service_stock.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import sn.estm.service_stock.dto.ArticleVenteDto;
import sn.estm.service_stock.dto.ArticleVenteReponse;
import sn.estm.service_stock.servcies.ArticleVenteService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/article")
public class ArticleVenteController {

	private final ArticleVenteService articleVenteService;

	@PostMapping("/ajout")
	public ResponseEntity<ArticleVenteReponse> ajoutPanier(@RequestBody ArticleVenteDto articleDto,
			HttpSession session) {
		articleDto.setSessionId(session.getId());
		ArticleVenteReponse reponse = articleVenteService.AjoutArticle(articleDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
	}

	@GetMapping("/liste")
	public ResponseEntity<List<ArticleVenteReponse>> listePanier(HttpSession session) {
		List<ArticleVenteReponse> panier = articleVenteService.listArticles(session.getId());
		return ResponseEntity.ok(panier);
	}
	
	@GetMapping("/panier")
	public ResponseEntity<List<ArticleVenteReponse>> tousArticle(HttpSession session) {
		List<ArticleVenteReponse> panier = articleVenteService.panierListe(session.getId());
		return ResponseEntity.ok(panier);
	}
	@DeleteMapping("/panier/{aid}")
	public void supprimer(@PathVariable Long aid, HttpSession session) {
		articleVenteService.suprimer(aid, session.getId());
	}
}
