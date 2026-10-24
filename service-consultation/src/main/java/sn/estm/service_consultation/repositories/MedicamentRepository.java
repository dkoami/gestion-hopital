package sn.estm.service_consultation.repositories;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sn.estm.service_consultation.entities.Medicament;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament,Long>{
    
	@Query("SELECT m FROM Medicament m WHERE FUNCTION('DATE', m.dateCr) = :date")
	public List<Medicament> findByDateCrDateOnly(@Param("date")LocalDate date);
	
	@Query("""
		    SELECT m FROM Medicament m
		    JOIN FETCH m.consultation c
		    WHERE LOWER(m.medNom) LIKE LOWER(CONCAT('%', :mc, '%'))
		       OR LOWER(m.dose)   LIKE LOWER(CONCAT('%', :mc, '%'))
		       OR LOWER(c.symp)   LIKE LOWER(CONCAT('%', :mc, '%'))
		       OR LOWER(c.diagn)  LIKE LOWER(CONCAT('%', :mc, '%'))
		    """)
		List<Medicament> rechercherParMotCle(@Param("mc") String mc);

}
