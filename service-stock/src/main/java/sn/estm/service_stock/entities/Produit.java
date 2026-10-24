package sn.estm.service_stock.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Produit {

	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private Long prid;
	 private String nomMed;
	 private BigDecimal prix;
	 private Integer qte;
	 private Boolean active=true;
	 @ManyToOne
	 @JsonIgnore
	 @JoinColumn(name = "categori_id")
	 private Categori categori;
	 private LocalDateTime date;
	 
	 @OneToMany(mappedBy = "produit")
	 @JsonIgnore
	 private List<ArticleVente> articleVentes;
}
