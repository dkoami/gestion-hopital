package sn.estm.service_stock.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VenteReponse {
	private Long vid;
	private LocalDateTime dateCr;
	private BigDecimal total;
	List<ArticleVenteDto> articlesDto;
}
