package sn.estm.service_consultation.dto;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_consultation.entities.Consultation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicamentRequete {
		@NotBlank(message = "le medicament n'est pas defini")
		private String medNom;
		@NotBlank(message = "la dose n'est pas defini")
		private String dose;
		private Consultation consultation;
		private LocalDateTime dateCr;
}
