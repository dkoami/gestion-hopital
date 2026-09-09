package sn.estm.service_patient.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sn.estm.service_patient.entities.Patient;

@Repository
public interface PatientRypository extends JpaRepository<Patient,UUID>{

	// Vérifie s'il existe un patient avec ce pnom ET ce prenom (ignore la casse)
    boolean existsByPnomIgnoreCaseAndPrenomIgnoreCase(String pnom, String prenom);
}
