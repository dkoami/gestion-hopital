package sn.estm.service_stock.servcies;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import sn.estm.service_stock.dto.ArticleVenteDto;
import sn.estm.service_stock.dto.ArticleVenteReponse;
import sn.estm.service_stock.dto.VenteReponse;
import sn.estm.service_stock.entities.ArticleVente;
import sn.estm.service_stock.entities.Produit;
import sn.estm.service_stock.entities.Vente;
import sn.estm.service_stock.repositories.ArticleVenteRepository;
import sn.estm.service_stock.repositories.ProduitRepository;
import sn.estm.service_stock.repositories.VenteRepository;

@Service
@RequiredArgsConstructor
public class VenteService {

    private final ArticleVenteService articleService;
    private final VenteRepository venteRepository;
    private final ArticleVenteRepository articleVenteRepository;
    private final ProduitRepository produitRepository;

    @Transactional
    public VenteReponse CreerVente(String sessionId) {
        //  Récupère uniquement les articles du panier en cours
    	List<ArticleVente> listeArticle=  articleService.listPanierAnvantVente(sessionId);
    	//List<ArticleVenteReponse> listeArticleReponses= articleService.listArticles(sessionId);
    	BigDecimal totalArticle = articleService.totalPanier(sessionId);
    	if(listeArticle.isEmpty()&&totalArticle.compareTo(BigDecimal.ZERO) == 0) {
    		 throw new RuntimeException("Le panier est vide, impossible de créer une vente.");
    	}
        
        //  Enregistrement de la Vente
        Vente vente = new Vente();
        vente.setDateCr(LocalDateTime.now());
        vente.setTotal(totalArticle);
        Vente venteEnregistree = venteRepository.save(vente);

        //  Mettre à jour les stocks et ajouter les articles à la vente 
        for (ArticleVente item : listeArticle) {
            Produit produit = item.getProduit();
            if (produit != null) {
                if (produit.getQte() < item.getQte()) {
                    throw new RuntimeException("Stock insuffisant pour le produit : " + produit.getNomMed());
                }
                // Décrémentation du stock
                produit.setQte(produit.getQte() - item.getQte());
                produitRepository.save(produit);
            }

            // On associe l'article à la vente (Conserve la trace en BDD)
            item.setVente(venteEnregistree);
            articleVenteRepository.save(item);
        }

        venteEnregistree.setArticles(listeArticle);

        // Génération du DTO de réponse
        
        return trasfertAventeReponse(venteEnregistree);
    }

    public List<VenteReponse> tousVente(){
    	return	venteRepository.findAll().stream()
    	                         .map(this::trasfertAventeReponse)
    	                         .collect(Collectors.toList());
    	
    }
    public VenteReponse trasfertAventeReponse( Vente vente) {
    	VenteReponse venteReponse = new VenteReponse();
        venteReponse.setVid(vente.getVid());
        venteReponse.setDateCr(LocalDateTime.now());
        venteReponse.setTotal(vente.getTotal());
        
        if (vente.getArticles() != null) {
            List<ArticleVenteDto> articledto = vente.getArticles().stream().map(article -> {
                ArticleVenteDto dto = new ArticleVenteDto();
                dto.setAid(article.getAid());
                dto.setPrix(article.getPrix());
                dto.setQte(article.getQte());
                dto.setSousTotal(article.getSousTotal());
                dto.setSessionId(article.getSessionId());
                if (article.getProduit() != null) {
                    dto.setProduit(article.getProduit()); 
                }
                return dto;
            }).collect(Collectors.toList());
            venteReponse.setArticlesDto(articledto);
        }
        return venteReponse;
    }
}