//package sn.estm.service_stock.servcies;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import lombok.RequiredArgsConstructor;
//import sn.estm.service_stock.dto.CommandeDto;
//import sn.estm.service_stock.dto.LigneCommandeDto;
//import sn.estm.service_stock.entities.Categori;
//import sn.estm.service_stock.entities.Commande;
//import sn.estm.service_stock.entities.Grossiste;
//import sn.estm.service_stock.entities.LigneCommande;
//import sn.estm.service_stock.entities.Produit;
//import sn.estm.service_stock.repositories.CategoriRepository;
//import sn.estm.service_stock.repositories.CommandeRepository;
//import sn.estm.service_stock.repositories.GrossisteRepository;
//import sn.estm.service_stock.repositories.ProduitRepository;
//
//@Service
//@RequiredArgsConstructor
//public class CommandeService {
//
//    private final CommandeRepository commandeRepository;
//    private final ProduitRepository produitRepository;
//    private final GrossisteRepository grossisteRepository;
//    private final CategoriRepository categoriRepository;
//
//    @Transactional
//    public Commande creerCommande(CommandeDto request) {
//        
//        // 1. Validation de la présence du grossiste
//        if (request.getGrossiste() == null || request.getGrossiste().getGid() == null) {
//            throw new IllegalArgumentException("L'identifiant du grossiste est obligatoire.");
//        }
//
//        Grossiste grossiste = grossisteRepository.findById(request.getGrossiste().getGid())
//                .orElseThrow(() -> new RuntimeException("Grossiste non trouvé avec l'ID: " + request.getGrossiste().getGid()));
//
//        Commande commande = new Commande();
//        commande.setDate(LocalDateTime.now());
//        commande.setGrossiste(grossiste);
//
//        // 2. Traitement des lignes de commande
//        if (request.getLignes() == null || request.getLignes().isEmpty()) {
//            throw new IllegalArgumentException("La commande doit contenir au moins une ligne.");
//        }
//
//        for (LigneCommandeDto ligneDto : request.getLignes()) {
//            Produit produit;
//            Produit produitDto = ligneDto.getProduit();
//
//            if (produitDto == null) {
//                throw new IllegalArgumentException("Chaque ligne de commande doit spécifier un produit.");
//            }
//
//            // Cas A : Le produit existe déjà en base (son ID 'prid' est fourni)
//            if (produitDto.getPrid() != null) {
//                produit = produitRepository.findById(produitDto.getPrid())
//                        .orElseThrow(() -> new RuntimeException("Produit non trouvé avec l'ID: " + produitDto.getPrid()));
//            } 
//            // Cas B : Le produit n'existe pas (pas de 'prid'), on le crée dynamiquement
//            else {
//                if (produitDto.getCategori() == null || produitDto.getCategori().getCaid() == null) {
//                    throw new IllegalArgumentException("La catégorie est obligatoire pour créer un nouveau produit.");
//                }
//
//                Categori categori = categoriRepository.findById(produitDto.getCategori().getCaid())
//                        .orElseThrow(() -> new RuntimeException("Catégorie non trouvée avec l'ID: " + produitDto.getCategori().getCaid()));
//
//                Produit nouveauProduit = new Produit();
//                nouveauProduit.setNomMed(produitDto.getNomMed());
//                
//                // Alignement du prix (conversion si nécessaire)
//                if (produitDto.getPrix() != null) {
//                    nouveauProduit.setPrix(produitDto.getPrix());
//                } else if (ligneDto.getPrix() != null) {
//                    nouveauProduit.setPrix(BigDecimal.valueOf(ligneDto.getPrix()));
//                }
//
//                nouveauProduit.setQte(produitDto.getQte() != null ? produitDto.getQte() : 0);
//                nouveauProduit.setActive(true);
//                nouveauProduit.setCategori(categori);
//                nouveauProduit.setDate(LocalDateTime.now());
//
//                produit = produitRepository.save(nouveauProduit);
//            }
//
//            // 3. Création et affectation de la ligne de commande
//            LigneCommande ligne = new LigneCommande();
//            ligne.setProduit(produit);
//            ligne.setQte(ligneDto.getQte());
//            ligne.setPrix(ligneDto.getPrix());
//
//            double prixUnitaire = ligneDto.getPrix() != null ? ligneDto.getPrix() : 0.0;
//            int quantite = ligneDto.getQte() != null ? ligneDto.getQte() : 0;
//            ligne.setSousTotal(prixUnitaire * quantite);
//
//            ligne.setCommande(commande);
//            commande.getLignes().add(ligne);
//        }
//
//        // 4. Persistence en cascade de la commande et de ses lignes
//        return commandeRepository.save(commande);
//    }
//}