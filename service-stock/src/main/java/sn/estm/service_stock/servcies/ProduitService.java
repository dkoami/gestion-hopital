package sn.estm.service_stock.servcies;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.query.NativeQuery.ReturnableResultNode;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.estm.service_stock.dto.CategoriReponse;
import sn.estm.service_stock.dto.ProduitReponse;
import sn.estm.service_stock.dto.ProduitRequete;
import sn.estm.service_stock.entities.Categori;
import sn.estm.service_stock.entities.Produit;
import sn.estm.service_stock.repositories.CategoriRepository;
import sn.estm.service_stock.repositories.ProduitRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProduitService {

	private final ProduitRepository produitRepo;
	private final CategoriRepository categoriRepo;
	public ProduitReponse ajoutProduit(ProduitRequete produitRequete) {
	    // On vérifie d'abord si une catégorie a été envoyée dans la requête
	    if (produitRequete.getCategori() == null) {
	        log.warn("categori non associé au produit");
	        throw new RuntimeException("La catégorie du produit est obligatoire");
	    }
	    Categori categ = categoriRepo.findById(produitRequete.getCategori().getCaid())
	            .orElseThrow(() -> new RuntimeException("categori du produit introvable"));
	    
	    Produit produit = new Produit();
	    produit.setCategori(categ);
	    transfertProduitRequeteAProduit(produit, produitRequete);
	    Produit stockProduit = produitRepo.save(produit);
	    return transfertAProduitReponse(stockProduit);
	}
	public Produit trouveProduitAvecCategori(String nomMed, Long cid) {
	
      return produitRepo.findByNomMedAndCategoriCaid(nomMed, cid);
	}
	private ProduitReponse transfertAProduitReponse(Produit stockProduit) {
		// TODO Auto-generated method stub
		ProduitReponse productResponse=new ProduitReponse();
		productResponse.setPrid(stockProduit.getPrid());
		productResponse.setNomMed(stockProduit.getNomMed());
		productResponse.setActive(stockProduit.getActive());
		productResponse.setPrix(stockProduit.getPrix());
		productResponse.setQte(stockProduit.getQte());
		productResponse.setDate(stockProduit.getDate());
		if (stockProduit.getCategori() != null) {
	        CategoriReponse categoriDto = new CategoriReponse();
	        categoriDto.setIdCateg(stockProduit.getCategori().getCaid());
	        categoriDto.setLibCateg(stockProduit.getCategori().getLibCateg()); 
	        productResponse.setCategori(categoriDto);
	    }
		
		return productResponse;
	}
	private void transfertProduitRequeteAProduit(Produit produit, ProduitRequete produitRequete) {
		// TODO Auto-generated method stub
		produit.setNomMed(produitRequete.getNomMed());
		produit.setPrix(produitRequete.getPrix());
		produit.setQte(produitRequete.getQte());
		produit.setDate(LocalDateTime.now());	
	}
	public ProduitReponse miseAjourProduit(Long id, ProduitRequete produitRequete) {
	    Produit existeProduit = produitRepo.findById(id)
	            .orElseThrow(() -> new RuntimeException("Produit absent:" + id));
	    
	    transfertProduitRequeteAProduit(existeProduit, produitRequete);
	    Produit savedProduct = produitRepo.save(existeProduit);

	    return transfertAProduitReponse(savedProduct);
	}
	public List<ProduitReponse> listeProduits() {
		// TODO Auto-generated method stub
		return produitRepo.findByActiveTrue().stream()
				.map(this::transfertAProduitReponse)
				.collect(Collectors.toList());
	}
	public boolean suprimerProduit(Long id) {
		// TODO Auto-generated method stub
		return produitRepo.findById(id)
				.map(product->{
					product.setActive(false);
					produitRepo.save(product);
					return true;
				}).orElse(false);
	}
	public List<ProduitReponse> rechercheProduits(String keyword) {
		// TODO Auto-generated method stub
		return produitRepo.searchProduits(keyword).stream()
				.map(this::transfertAProduitReponse)
				.collect(Collectors.toList());
	}
}
