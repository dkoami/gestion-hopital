package sn.estm.service_stock.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import sn.estm.service_stock.entities.LigneCommande;

public interface LigneCommandeRepository extends JpaRepository<LigneCommande,Long>{

}
