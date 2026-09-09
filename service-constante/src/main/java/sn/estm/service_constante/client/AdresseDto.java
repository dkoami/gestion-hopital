package sn.estm.service_constante.client;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdresseDto {
	@NotBlank(message =" la ville ne doit pas etre vide")
	private String ville;
	@NotBlank(message =" le quartier ne doit pas etre vide")
	private String quart;
}
