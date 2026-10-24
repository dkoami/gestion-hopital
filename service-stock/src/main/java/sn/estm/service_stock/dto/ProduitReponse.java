package sn.estm.service_stock.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_stock.entities.Categori;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduitReponse {
	 private Long prid;
	 private String nomMed;
	 private BigDecimal prix;
	 private Integer qte;
	 private Boolean active=true;
	 private CategoriReponse categori;
	 private LocalDateTime date;
}
