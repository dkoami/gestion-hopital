package sn.estm.service_consultation.dto;


import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_consultation.entities.Consultation;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SejourRequete {
	private LocalDate dateCr;
	private LocalDate dateSortie;
	private Consultation consultation;
}
