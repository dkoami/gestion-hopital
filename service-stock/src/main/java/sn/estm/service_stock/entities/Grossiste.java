package sn.estm.service_stock.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Grossiste {
   
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
	private Long gid;
	private LocalDateTime dateCr;
	private LocalDateTime dateM;
	private String nomF;
	@OneToMany(mappedBy = "grossiste",fetch = FetchType.LAZY)
	private List<Commande> commande;
}
