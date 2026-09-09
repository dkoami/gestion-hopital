package sn.estm.service_patient.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_patient.entities.Patient;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonReponse {
	private Long bid;
	private Integer prix;
	private String nomPartiP;
	private String designation;
	private Patient patient;
	private LocalDateTime dateCr;
	private LocalDateTime dateM;
}
