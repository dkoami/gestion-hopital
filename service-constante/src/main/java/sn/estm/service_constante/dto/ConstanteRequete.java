package sn.estm.service_constante.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstanteRequete {
	private Long constid;
	
	@NotNull(message = "Le poids est obligatoire")
	@Positive(message = "Le poids doit être un nombre positif")
	private Integer poids;
	
	@NotNull(message = "La taille est obligatoire")
	@Positive(message = "La taille doit être un nombre positif")
	private Integer taille;
	
	@NotNull(message = "La tension est obligatoire")
	@Positive(message = "La tension doit être un nombre positif")
	private Integer tension;
	
	@NotNull(message = "La temperature est obligatoire")
	@Positive(message = "La température doit être un nombre positif")
	private Integer temp;
	
	private LocalDateTime dateCr;
	private LocalDateTime dateM;
}
