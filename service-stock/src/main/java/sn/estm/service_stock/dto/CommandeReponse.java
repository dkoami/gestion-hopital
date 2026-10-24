package sn.estm.service_stock.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import sn.estm.service_stock.entities.Grossiste;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CommandeReponse {
    private Long cmid;
    private Grossiste nomFou;
    private LocalDateTime date;
}
