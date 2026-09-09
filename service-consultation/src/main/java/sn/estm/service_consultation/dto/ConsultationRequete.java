package sn.estm.service_consultation.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_consultation.entities.Consultation;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ConsultationRequete {
	private UUID pid;
	@NotBlank(message =" le symptome ne doit pas etre vide")
	private String symp;
	@NotBlank(message = "la dignostique diot etre defini")
	private String diagn;
}
