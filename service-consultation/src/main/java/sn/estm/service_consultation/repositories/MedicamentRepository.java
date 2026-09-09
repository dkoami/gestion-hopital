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

}
