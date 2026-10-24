package sn.estm.service_stock.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_stock.entities.Produit;
import sn.estm.service_stock.entities.Vente;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArticleVenteReponse {

	 private Long aid;
	 private String sessionId;
	 private BigDecimal prix;
	 private Integer qte;
	 private BigDecimal sousTotal;
	 private Vente vente;
	 private ProduitReponse produitdto;
}
