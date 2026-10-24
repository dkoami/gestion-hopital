package sn.estm.service_constante.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.web.bind.annotation.CrossOrigin;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Constante {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long constid;
	private Long bid;
	private UUID pid;
	private Integer poids;
	private Integer taille;
	private Integer tension;
	private Integer temp;
	private LocalDateTime dateCr;
	private LocalDateTime dateM; 
}
