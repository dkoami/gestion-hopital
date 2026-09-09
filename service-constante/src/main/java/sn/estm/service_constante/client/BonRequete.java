package sn.estm.service_constante.client;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonRequete {

	@NotNull(message = "Le prix est obligatoire")
	@Positive(message = "Le prix doit être un nombre positif")
	private Integer prix;
	@NotBlank(message="le nom du parti prenant ne doit pa etre vide ")
	private String nomPartiP;
	@NotBlank(message="la designation ne doit pa etre vide ")
	private String designation;
	private UUID patient;
	private LocalDateTime dateCr;
	private LocalDateTime dateM;

}
