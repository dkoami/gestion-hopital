package sn.estm.service_stock.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.estm.service_stock.entities.Vente;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Long>{

}
