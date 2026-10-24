package sn.estm.service_stock.servcies;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sn.estm.service_stock.dto.LigneCommandeDto;
import sn.estm.service_stock.dto.LigneCommandeReponse;
import sn.estm.service_stock.entities.LigneCommande;
import sn.estm.service_stock.entities.Produit;
import sn.estm.service_stock.repositories.LigneCommandeRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class LigneCommandeService {

    private final LigneCommandeRepository ligneRepo;
    private final ProduitService produitService;

    @Transactional
    public LigneCommandeReponse ajoutLigne(LigneCommandeDto ligneRequete) {

        // Vérification du produit
        if (ligneRequete.getProduit() == null) {
            throw new IllegalArgumentException("Le produit est obligatoire");
        }
        String nomProduit = ligneRequete.getProduit().getNomMed();
        Long categoriId = ligneRequete.getProduit().getCategori().getCaid();
        // Recherche du produit en base
        Produit produit =produitService.trouveProduitAvecCategori(nomProduit,categoriId);
        // Si le produit existe
        if (produit != null) {
            log.info("Le produit existe déjà : {}",produit.getNomMed());
        } else {
            throw new IllegalArgumentException("Produit non trouvé : " + nomProduit);
        }
        
        // Création de la ligne
        LigneCommande ligneCommande = new LigneCommande();
        ligneCommande.setProduit(produit);
        ligneCommande.setPrix(ligneRequete.getPrix());
        ligneCommande.setQte(ligneRequete.getQte());
        
        // Calcul du sous-total
        
        double sousTotal =ligneRequete.getPrix()* ligneRequete.getQte();
        ligneCommande.setSousTotal(sousTotal);
        
        // Association à la commande
        ligneCommande.setCommande(ligneRequete.getCommande());

        // Sauvegarde
        LigneCommande ligneSave = ligneRepo.save(ligneCommande);

        return mapToLigneCommandeReponse(ligneSave);
    }

    public LigneCommandeReponse mapToLigneCommandeReponse(
            LigneCommande ligneCommande) {

        LigneCommandeReponse reponse =
                new LigneCommandeReponse();

        reponse.setPrix(ligneCommande.getPrix());
        reponse.setProduit(ligneCommande.getProduit());
        reponse.setCommande(ligneCommande.getCommande());
        reponse.setQte(ligneCommande.getQte());

        return reponse;
    }
}