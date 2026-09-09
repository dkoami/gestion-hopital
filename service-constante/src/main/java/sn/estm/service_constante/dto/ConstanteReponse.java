package sn.estm.service_constante.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConstanteReponse {
	private Long constid;
	private Long bid;
	private Integer poids;
	private Integer taille;
	private Integer tesion;
	private Integer temp;
	private LocalDateTime dateCr;
	private LocalDateTime dateM; 
	private PatientDetails patientDetails;
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PatientDetails {
		private String pnom;
		private String prenom;		
    }
	
}
