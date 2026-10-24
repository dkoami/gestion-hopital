package sn.estm.service_patient.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import sn.estm.service_patient.entities.Patient;

@Repository
public interface PatientRypository extends JpaRepository<Patient,UUID>{

	// Vérifie s'il existe un patient avec ce pnom ET ce prenom (ignore la casse)
    boolean existsByPnomIgnoreCaseAndPrenomIgnoreCase(String pnom, String prenom);
    @Query("SELECT p FROM Patient p WHERE " +
            "LOWER(p.pnom) LIKE LOWER(CONCAT('%', :mc, '%')) OR " +
            "LOWER(p.prenom) LIKE LOWER(CONCAT('%', :mc, '%'))")
     List<Patient> chercherParMotCle(@Param("mc") String mc);
}
