package sn.estm.service_stock.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sn.estm.service_stock.dto.ProduitReponse;
import sn.estm.service_stock.entities.Produit;

public interface ProduitRepository extends JpaRepository<Produit,Long>{

	List<Produit> findByActiveTrue();
	@Query("SELECT p FROM Produit p WHERE p.active = true AND p.qte > 0 AND LOWER(p.nomMed) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Produit> searchProduits(@Param("keyword") String keyword);
	Produit findByNomMedAndCategoriCaid(String nomMed, Long caid);
}
