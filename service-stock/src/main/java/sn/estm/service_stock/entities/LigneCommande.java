package sn.estm.service_stock.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LigneCommande {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long lic;	
 private  Double prix;
 private Integer qte;
 private Double sousTotal;
 @ManyToOne
 @JsonIgnore
 private Produit produit;
 @ManyToOne
 private Commande commande;
}
