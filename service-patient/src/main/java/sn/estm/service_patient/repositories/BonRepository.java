package sn.estm.service_patient.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import sn.estm.service_patient.entities.Bon;

public  interface  BonRepository extends JpaRepository<Bon,Long> {
	
	List<Bon> findByPatientPid(UUID pid);
}
