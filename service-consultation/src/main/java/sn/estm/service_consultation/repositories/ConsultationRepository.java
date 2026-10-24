package sn.estm.service_consultation.repositories;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sn.estm.service_consultation.entities.Consultation;

@Repository
public interface ConsultationRepository extends JpaRepository<Consultation,Long>{
	boolean existsByBid(Long bid);

	Consultation findByPid(UUID pid);
	@Query("""
	        SELECT c FROM Consultation c
	        WHERE c.pid IN :pids
	           OR LOWER(c.symp) LIKE LOWER(CONCAT('%', :mc, '%'))
	           OR LOWER(c.diagn) LIKE LOWER(CONCAT('%', :mc, '%'))
	        """)
	    List<Consultation> rechercherAvecPatients(
	            @Param("pids") Collection<UUID> pids,
	            @Param("mc") String mc
	    );
	 @Query("""
		        SELECT c FROM Consultation c
		        WHERE LOWER(c.symp) LIKE LOWER(CONCAT('%', :mc, '%'))
		           OR LOWER(c.diagn) LIKE LOWER(CONCAT('%', :mc, '%'))
		        """)
		    List<Consultation> rechercherSansPatients(
		            @Param("mc") String mc
		    );
}
