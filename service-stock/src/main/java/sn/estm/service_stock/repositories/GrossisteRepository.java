package sn.estm.service_stock.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.estm.service_stock.entities.Grossiste;

@Repository
public interface GrossisteRepository extends JpaRepository<Grossiste,Long> {

}
