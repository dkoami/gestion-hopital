package sn.estm.service_constante.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.estm.service_constante.entities.Constante;

@Repository
public interface ConstanteRepository extends JpaRepository<Constante, Long>{
	boolean existsByBid(Long bid);
}
