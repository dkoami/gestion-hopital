package sn.estm.service_patient.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Patient {

 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 private UUID pid;
 private String pnom;
 private String prenom;
 private String profession;
 private String	sexe;
 private Integer age;
 private Integer num;
 @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true)
 @JoinColumn(name="adresse_id",nullable = false)
 private Adresse adresse;
 @CreationTimestamp
 private LocalDateTime	dateCr;
 @UpdateTimestamp
 private LocalDateTime dateM;
}
