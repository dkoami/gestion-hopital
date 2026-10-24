package sn.estm.service_stock.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_stock.entities.Commande;
import sn.estm.service_stock.entities.Produit;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LigneCommandeDto {

	 private Long lic;	
	 private  Double prix;
	 private Integer qte;
	 private Double sousTotal;
	 private Produit produit;
	 private Commande commande;
}
