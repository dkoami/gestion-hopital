package sn.estm.service_consultation.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.estm.service_consultation.Enums.StatutRendezVous;
import sn.estm.service_consultation.entities.RendezVous;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous, Long>{

	List<RendezVous> findByStatusAndFinBefore(StatutRendezVous confirme, LocalDateTime maintenant);

}
