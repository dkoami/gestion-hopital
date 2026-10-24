package sn.estm.service_stock.dto;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class GrossisteRequete {

	private Long gid;
	private LocalDateTime dateCr;
	private LocalDateTime dateM;
	private String nomF;
	private List<CommandeDto> commandeDto;
}
