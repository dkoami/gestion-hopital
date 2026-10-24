package sn.estm.service_consultation.dto;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsultationReponse {

	private long cid;
	private UUID pid;
	private long bid;
	private String symp;
	private String diagn;
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
