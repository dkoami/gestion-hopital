package sn.estm.service_stock.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.estm.service_stock.dto.CategoriReponse;
import sn.estm.service_stock.entities.Categori;

@Repository
public interface CategoriRepository extends JpaRepository<Categori,Long>{

	List<Categori>findByLibCategContainingIgnoreCase(String libCateg);

	public Optional<Categori> findByLibCateg(String libCateg);
}
