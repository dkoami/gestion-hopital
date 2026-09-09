package sn.estm.service_consultation.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.estm.service_consultation.entities.Sejour;

@Repository
public interface SejourRepository extends JpaRepository<Sejour,Long>{
	boolean existsByConsultationCid(Long cid);

}
