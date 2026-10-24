package sn.estm.service_stock.entities;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
public class Categori {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long caid;
	private String libCateg;

	@OneToMany(mappedBy = "categori", fetch = FetchType.LAZY)
	@JsonIgnore // Eviter la boucle infinie lors de la sérialisation JSON
	private List<Produit> produit;
}
