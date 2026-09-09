package sn.estm.service_patient.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_patient.entities.Patient;

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
	private Patient patient;
	private LocalDateTime dateCr;
	private LocalDateTime dateM;

}
