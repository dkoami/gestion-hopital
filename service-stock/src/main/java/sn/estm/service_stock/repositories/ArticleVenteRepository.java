package sn.estm.service_stock.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.estm.service_stock.entities.ArticleVente;

@Repository
public interface ArticleVenteRepository extends JpaRepository<ArticleVente, Long>{

	List<ArticleVente> findByVenteIsNullAndSessionId(String sessionId);
	List<ArticleVente> findBySessionId(String sessionId);
	Optional<ArticleVente> findBySessionIdAndProduitPridAndVenteIsNull(String sessionId, Long prid);

}
