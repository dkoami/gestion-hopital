package sn.estm.service_consultation.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Medicament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long mid;
	private String medNom;
	private String dose;
	private LocalDateTime dateCr;
	private LocalDateTime dateM;
	@ManyToOne
	@JoinColumn(name = "consultation_id", nullable = false)
	@JsonIgnore
	private Consultation consultation;
}
