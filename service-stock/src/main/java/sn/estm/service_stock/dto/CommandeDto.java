package sn.estm.service_stock.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_stock.entities.Grossiste;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommandeDto {

    private Long cmid;
    private LocalDateTime date;
    private Grossiste grossiste; 
    private List<LigneCommandeDto> lignes;
}
