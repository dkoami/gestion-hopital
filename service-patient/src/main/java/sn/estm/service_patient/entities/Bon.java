package sn.estm.service_patient.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Bon {
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long bid;
	private Integer prix;
	private String nomPartiP;
	private String designation;
	@ManyToOne
	@JoinColumn(name="patient_id",nullable = false)
	private Patient patient;
	private LocalDateTime dateCr;
	private LocalDateTime dateM;
}
