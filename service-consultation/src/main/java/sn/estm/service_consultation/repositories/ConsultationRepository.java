package sn.estm.service_consultation.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.estm.service_consultation.entities.Consultation;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation,Long>{
	boolean existsByBid(Long bid);

	Consultation findByPid(UUID pid);
}
