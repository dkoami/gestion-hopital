package sn.estm.service_consultation.clients;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data

public class PatientRequete {
	@NotBlank(message =" le Nom ne doit pas etre vide")
    private String pnom;
	@NotBlank(message =" le prenom ne doit pas etre vide")
    private String prenom;
	@NotBlank(message =" la profession ne doit pas etre vide")
    private String profession;
	@NotBlank(message = "Le sexe est obligatoire")
	@Pattern(regexp = "^(M|F|Masculin|Féminin)$", message = "Le sexe doit être 'M', 'F', 'Masculin' ou 'Féminin'")
    private String sexe;
	@NotNull(message = "L'âge est obligatoire")
	@Positive(message = "L'âge doit être un nombre positif")
    private Integer age;
    private Integer num;
    private AdresseDto adresseDto;
}
