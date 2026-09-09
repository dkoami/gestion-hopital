package sn.estm.service_consultation.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_consultation.Enums.StatutRendezVous;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class RendezVous {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rid;

    private String texte;
    private LocalDateTime debut;
    private LocalDateTime fin;
    private String couleur;
    
    @Enumerated(EnumType.STRING)
    private StatutRendezVous status;
    @JoinColumn(name = "consultation_id", nullable = false)
    @ManyToOne
    @JsonIgnore
    private Consultation consultation;
}
