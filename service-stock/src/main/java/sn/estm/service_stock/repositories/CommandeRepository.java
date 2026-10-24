package sn.estm.service_stock.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.estm.service_stock.entities.Commande;

@Repository
public interface CommandeRepository extends JpaRepository<Commande, Long>{

}
