package sn.estm.service_consultation.entities;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Sejour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sid;
    private LocalDateTime dateCr;
    private LocalDateTime dateM;

    @OneToOne
    @JoinColumn(name = "consultation_id", nullable = false) 
    @JsonIgnore
    private Consultation consultation;
}