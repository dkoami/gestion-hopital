package sn.estm.service_stock.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



import jakarta.persistence.CascadeType;
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
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cmid;

    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "grossiste_id", nullable = false)
    private Grossiste grossiste;

    @OneToMany(
        mappedBy = "commande",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<LigneCommande> lignes = new ArrayList<>();
}
