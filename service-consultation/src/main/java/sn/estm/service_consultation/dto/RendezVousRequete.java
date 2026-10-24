package sn.estm.service_consultation.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_consultation.Enums.StatutRendezVous;
import sn.estm.service_consultation.entities.Consultation;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RendezVousRequete {
	 @NotBlank(message="veillez definir l'objet du rendez-vous")
	 private String texte;
	 
	 @NotNull(message = "la date/heure de début est obligatoire")
	 private LocalDateTime debut;
	 
	 @NotNull(message = "la date/heure de fin est obligatoire")
	 private LocalDateTime fin;
	 
	 private String couleur;
	 
	 private StatutRendezVous status;
	 
	 private Consultation consultation;
}
