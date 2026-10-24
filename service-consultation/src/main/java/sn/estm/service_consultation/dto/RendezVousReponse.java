package sn.estm.service_consultation.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_consultation.Enums.StatutRendezVous;
import sn.estm.service_consultation.entities.Consultation;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RendezVousReponse {

	 private Long rid;
	 private String texte;
	 private LocalDateTime debut;
	 private LocalDateTime fin;
	 private String couleur;
	 private StatutRendezVous status;
	 private Consultation consultation;
	 private PatientDetails patientDetails;
		@Data
		@AllArgsConstructor
		@NoArgsConstructor
		public static class PatientDetails {
			private String pnom;
			private String prenom;	
			private Integer num;
	    }
}
