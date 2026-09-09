package sn.estm.service_consultation.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_consultation.entities.Consultation;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SejourReponse {
	private Long sid;
	private LocalDateTime dateCr;
	private LocalDateTime dateM;
	private Long cid;
	PatientDetails patientDetails;
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PatientDetails {
		private String pnom;
		private String prenom;		
    }
}
