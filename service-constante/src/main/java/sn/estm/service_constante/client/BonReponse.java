package sn.estm.service_constante.client;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BonReponse {
	private Long bid;
	private Integer prix;
	private String nomPartiP;
	private String designation;
	private UUID pid;
	private LocalDateTime dateCr;
	private LocalDateTime dateM;
}
