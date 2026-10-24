package sn.estm.service_stock.servcies;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.estm.service_stock.dto.ArticleVenteDto;
import sn.estm.service_stock.dto.ArticleVenteReponse;
import sn.estm.service_stock.dto.ProduitReponse;
import sn.estm.service_stock.entities.ArticleVente;
import sn.estm.service_stock.entities.Produit;
import sn.estm.service_stock.repositories.ArticleVenteRepository;
import sn.estm.service_stock.repositories.ProduitRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleVenteService {

	private final ArticleVenteRepository articleVenteRepository;
	private final ProduitRepository produitRepository;
	
	public ArticleVenteReponse AjoutArticle(ArticleVenteDto articleDto) {
	    Produit produit = produitRepository.findById(articleDto.getProduit().getPrid())
	            .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
	                        
	    if (articleVenteRepository.findBySessionIdAndProduitPridAndVenteIsNull(articleDto.getSessionId(), produit.getPrid()).isPresent()) {
	        throw new RuntimeException("Produit déjà ajouté au panier");
	    }  

	    ArticleVente articleVente = new ArticleVente();
	    transfertArticleVenteDtoAarticleVente(articleDto, articleVente);
	    articleVente.setProduit(produit); 
	    articleVente.setPrix(produit.getPrix());
	    
	    // Calcul du sous-total
	    if (articleVente.getPrix() != null && articleVente.getQte() > 0) {
	        BigDecimal subTotal = articleVente.getPrix().multiply(BigDecimal.valueOf(articleVente.getQte()));
	        articleVente.setSousTotal(subTotal);
	    }
	    
	    // sauvegarde
	    ArticleVente savedArticle = articleVenteRepository.save(articleVente);
	    
	    return transfertArticleVenteAarticleVenteReponse(savedArticle);
	}
	// lister tous les articles y compris les non vendu
	public List<ArticleVenteReponse> listArticles(String sessionId) {
		return articleVenteRepository.findBySessionId(sessionId).stream()
		                                .map(this::transfertArticleVenteAarticleVenteReponse)
		                                .collect(Collectors.toList());
	}
	// lister tous les articles du panier
	public List<ArticleVenteReponse> panierListe(String sessionId) {
		return articleVenteRepository.findByVenteIsNullAndSessionId(sessionId)
				.stream()
                .map(this::transfertArticleVenteAarticleVenteReponse)
                .collect(Collectors.toList());
	}
	
	public List<ArticleVente> listPanierAnvantVente(String sessionId) {
		return articleVenteRepository.findByVenteIsNullAndSessionId(sessionId);
	}
	
	//  Calcul du total des article du panier
	public BigDecimal totalPanier(String sessionId) {
			List<ArticleVente> listArticle=articleVenteRepository.findByVenteIsNullAndSessionId(sessionId);
	        BigDecimal total = listArticle.stream()
	                .map(item -> item.getPrix().multiply(BigDecimal.valueOf(item.getQte())))
	                .reduce(BigDecimal.ZERO, BigDecimal::add);
			return total;
		
		}
	public void suprimer(Long aid, String sessionId) {
		ArticleVente article = articleVenteRepository.findById(aid)
	            .orElseThrow(() -> new RuntimeException("Article non trouvé"));
		if (!article.getSessionId().equals(sessionId)) {
	        throw new RuntimeException("Cet article n'appartient pas à votre panier.");
	    }
	    // Sécurité : Empêcher la suppression si l'article est déjà lié à une vente validée
	    if (article.getVente() != null) {
	        throw new RuntimeException("Impossible de supprimer un article déjà facturé.");
	    }
	    
	    articleVenteRepository.deleteById(aid);
	} 
		
	public void transfertArticleVenteDtoAarticleVente(ArticleVenteDto articleDto, ArticleVente articleVente) {
		articleVente.setPrix(articleDto.getProduit().getPrix());
		articleVente.setQte(articleDto.getQte());
		articleVente.setSessionId(articleDto.getSessionId());
		articleVente.setProduit(articleDto.getProduit());
		articleVente.setSousTotal(articleDto.getSousTotal());
	}
	public ArticleVenteReponse transfertArticleVenteAarticleVenteReponse(ArticleVente article) {
		ArticleVenteReponse articleReponse= new ArticleVenteReponse();
		articleReponse.setAid(article.getAid());
		articleReponse.setPrix(article.getPrix());
		articleReponse.setQte(article.getQte());
		articleReponse.setSousTotal(article.getSousTotal());
		articleReponse.setSessionId(article.getSessionId());
		ProduitReponse reponse= new ProduitReponse();
		if(article.getProduit()!=null) {
			reponse.setPrid(article.getProduit().getPrid());
			reponse.setNomMed(article.getProduit().getNomMed());
			reponse.setPrix(article.getProduit().getPrix());
			reponse.setQte(article.getProduit().getQte());
			articleReponse.setProduitdto(reponse);
		}
		return articleReponse;
	}
}
