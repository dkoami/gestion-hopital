package sn.estm.service_stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_stock.entities.Commande;
import sn.estm.service_stock.entities.Produit;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LigneCommandeReponse {


	 private Long lic;	
	 private  Double prix;
	 private Integer qte;
	 private Double sousTotal;
	 private Produit produit;
	 private Commande commande;
}
