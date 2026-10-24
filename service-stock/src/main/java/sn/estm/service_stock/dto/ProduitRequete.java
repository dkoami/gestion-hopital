package sn.estm.service_stock.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_stock.entities.Categori;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProduitRequete {
	
	@NotBlank(message="la designation ne doit pa etre vide ")
	 private String nomMed;
	@NotNull(message = "Le prix est obligatoire")
	@Positive(message = "Le prix doit être un nombre positif")
	 private BigDecimal prix;
	@NotNull(message = "Le quantité est obligatoire")
	@Positive(message = "La dquantité être un nombre positif et superieur à zero")
	private Integer qte;
	private Boolean active=true;
	private Categori categori;
	private LocalDateTime date;
}
