package sn.estm.service_stock.entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
public class Vente {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long vid;
	private LocalDateTime dateCr;
	private BigDecimal total; 
	@OneToMany(mappedBy = "vente", fetch = FetchType.LAZY)
	@JsonIgnore
	List<ArticleVente> articles= new ArrayList<>();
}
