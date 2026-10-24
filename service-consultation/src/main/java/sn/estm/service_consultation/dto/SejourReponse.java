package sn.estm.service_consultation.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_consultation.Enums.StatutSejour;
import sn.estm.service_consultation.entities.Consultation;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SejourReponse {
	private Long sid;
	private LocalDate dateCr;
	private LocalDate dateM;
	private Long cid;
	private StatutSejour statut;
	private LocalDate dateSortie;
	PatientDetails patientDetails;
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PatientDetails {
		private String pnom;
		private String prenom;		
    }
}
