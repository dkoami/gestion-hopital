package sn.estm.service_stock.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sn.estm.service_stock.dto.GrossisteReponse;
import sn.estm.service_stock.dto.GrossisteRequete;
import sn.estm.service_stock.servcies.GrossisteService;

@RestController
@RequestMapping("/api/grossiste")
@RequiredArgsConstructor
public class GrossisteController {

	private final GrossisteService grossisteService;
	
	@GetMapping("/ajout")
	public ResponseEntity<GrossisteReponse> addPGrossiste(@RequestBody GrossisteRequete grossisteRequete){
		GrossisteReponse reponse= grossisteService.ajouterGrossiste(grossisteRequete);
		return ResponseEntity.status(HttpStatus.CREATED).body(reponse);
		
	}
	@GetMapping("/liste")
	public ResponseEntity<List<GrossisteReponse>> listGrossiste(){
		List<GrossisteReponse> listeRep= grossisteService.listeGrosiste();
		return ResponseEntity.ok(listeRep);		
	}
	
	@PutMapping("{gid}")
	public ResponseEntity<GrossisteReponse> updateGrossiste(@RequestParam("gid") Long gid,@RequestBody GrossisteRequete grossisteRequete){
		GrossisteReponse reponse= grossisteService.updateGrossiste(gid, grossisteRequete);
		return ResponseEntity.ok(reponse);
		
	}
}
