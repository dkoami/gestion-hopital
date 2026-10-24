package sn.estm.service_stock.servcies;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import sn.estm.service_stock.dto.CommandeDto;
import sn.estm.service_stock.dto.GrossisteReponse;
import sn.estm.service_stock.dto.GrossisteRequete;
import sn.estm.service_stock.dto.LigneCommandeDto;
import sn.estm.service_stock.entities.Commande;
import sn.estm.service_stock.entities.Grossiste;
import sn.estm.service_stock.entities.LigneCommande;
import sn.estm.service_stock.repositories.GrossisteRepository;

@Service
@RequiredArgsConstructor
public class GrossisteService {

	private final GrossisteRepository grossisteRepo;

	public GrossisteReponse ajouterGrossiste(GrossisteRequete grossisteRequete) {
		Grossiste grossiste = new Grossiste();
		mapToGrossiste(grossiste, grossisteRequete);
		Grossiste saveGrossiste = grossisteRepo.save(grossiste);
		return mapToReponse(saveGrossiste);
	}
	public GrossisteReponse updateGrossiste(Long gid,GrossisteRequete grossisteRequete) {
		    Grossiste grossiste = grossisteRepo.findById(gid)
		            .orElseThrow(() -> new EntityNotFoundException("Grossiste introuvable avec l'ID : " + gid));
		    mapToGrossiste(grossiste, grossisteRequete);
		    Grossiste savedGrossiste = grossisteRepo.save(grossiste);

		    return mapToReponse(savedGrossiste);
	}
    public List<GrossisteReponse> listeGrosiste(){
    	return grossisteRepo.findAll().stream()
    	                       .map(this::mapToReponse)
    	                       .collect(Collectors.toList());
    }
    public GrossisteReponse mapToReponse(Grossiste grossiste) {
        GrossisteReponse reponse = new GrossisteReponse();
        reponse.setNomF(grossiste.getNomF());
        reponse.setDateCr(grossiste.getDateCr());
        reponse.setDateM(grossiste.getDateM());

        if (grossiste.getCommande() != null) {
            List<CommandeDto> commandesDto = grossiste.getCommande().stream()
                    .map(commande -> {
                        CommandeDto dto = new CommandeDto();
                        dto.setCmid(commande.getCmid());
                        dto.setDate(commande.getDate());

                        if (commande.getLignes() != null) {
                            List<LigneCommandeDto> lignesDto = commande.getLignes().stream()
                                    .map(ligne -> {
                                        LigneCommandeDto ligneDto = new LigneCommandeDto();
                                        ligneDto.setLic(ligne.getLic());
                                        ligneDto.setPrix(ligne.getPrix());
                                        ligneDto.setQte(ligne.getQte());
                                        ligneDto.setSousTotal(ligne.getSousTotal());
                                        if(ligne.getProduit()!=null) {
                                         ligneDto.setProduit(ligne.getProduit());	
                                        }
                                        return ligneDto;
                                    })
                                    .toList();
                            dto.setLignes(lignesDto);
                        }

                        return dto;
                    })
                    .toList();

            reponse.setCommandeDto(commandesDto);
        }

        return reponse;
    }

	public void mapToGrossiste(Grossiste grossiste, GrossisteRequete grossisteRequete) {
		grossiste.setNomF(grossisteRequete.getNomF());
		grossiste.setDateCr(LocalDateTime.now());
		grossiste.setDateM(LocalDateTime.now());
	}
}
