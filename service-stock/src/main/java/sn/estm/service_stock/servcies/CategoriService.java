package sn.estm.service_stock.servcies;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import sn.estm.service_stock.dto.CategoriReponse;
import sn.estm.service_stock.dto.CategoriRequete;
import sn.estm.service_stock.entities.Categori;
import sn.estm.service_stock.repositories.CategoriRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class CategoriService {
	private final CategoriRepository categoriRepo;
	
	
	public CategoriReponse ajoutCategori(CategoriRequete categoriRequete) {
		// TODO Auto-generated method stub
		
		boolean existe = categoriRepo.findByLibCateg(categoriRequete.getLibCateg()).isPresent();
		if (existe) {
	        log.warn("La catégorie '{}' existe déjà", categoriRequete.getLibCateg());
	        throw new RuntimeException("La catégorie existe déjà : " + categoriRequete.getLibCateg());
	    }
		Categori categori= new Categori();
		transfertCategoriRequeteAcategori( categori,categoriRequete);
		Categori stockCategori= categoriRepo.save(categori);
		return transfertACategoriReponse(stockCategori);
	}
	private CategoriReponse transfertACategoriReponse(Categori stockCateg) {
		// TODO Auto-generated method stub
		CategoriReponse categResponse=new CategoriReponse();
		categResponse.setLibCateg(stockCateg.getLibCateg());
		categResponse.setIdCateg(stockCateg.getCaid());
		return categResponse;
	}
	private void transfertCategoriRequeteAcategori(Categori categori,CategoriRequete categoriRequete) {
		// TODO Auto-generated method stub
		categori.setLibCateg(categoriRequete.getLibCateg());
	}
	public CategoriReponse miseAjourCategori(Long caid, CategoriRequete categoriRequete) {
		Categori existeCategori = categoriRepo.findById(caid)
	            .orElseThrow(() -> new RuntimeException("categori absent:" + caid));
	    
		transfertCategoriRequeteAcategori(existeCategori,categoriRequete);
		Categori savedCateg = categoriRepo.save(existeCategori);

	    return transfertACategoriReponse(savedCateg);
	}
	public List<CategoriReponse> listeCategori() {
		// TODO Auto-generated method stub
		return categoriRepo.findAll().stream()
				.map(this::transfertACategoriReponse)
				.collect(Collectors.toList());
	}
	public void suprimerCategori(Long caid) {
		// TODO Auto-generated method stub
		if(categoriRepo.findById(caid).isPresent()) {
			 categoriRepo.deleteById(caid);				
		}
		else {
			log.info("medicament n'existe pas");
		}	
	}
	public List<CategoriReponse> rechercheCategori(String keyword) {
		// TODO Auto-generated method stub
		return categoriRepo.findByLibCategContainingIgnoreCase(keyword).stream()
				.map(this::transfertACategoriReponse)
				.collect(Collectors.toList());
	}
}
