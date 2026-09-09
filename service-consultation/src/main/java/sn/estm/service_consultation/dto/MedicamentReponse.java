package sn.estm.service_consultation.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_consultation.entities.Consultation;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicamentReponse {
	private Long mid;
	private String medNom;
	private String dose;
	private LocalDateTime dateCr;
	private LocalDateTime dateM;
	private long cid;
	private PatientDetails patientDetails;
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PatientDetails {
		private String pnom;
		private String prenom;		
    }
}
