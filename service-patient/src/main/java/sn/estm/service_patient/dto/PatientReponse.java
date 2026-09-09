package sn.estm.service_patient.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_patient.entities.Adresse;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientReponse {
	private UUID pid;
    private String pnom;
    private String prenom;
    private String profession;
    private String sexe;
    private Integer age;
    private Integer num;
    private Adresse adresse;
    private LocalDateTime	dateCr;
    private LocalDateTime dateM;
}
