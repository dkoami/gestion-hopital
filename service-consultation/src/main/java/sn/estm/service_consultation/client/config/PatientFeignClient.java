package sn.estm.service_consultation.client.config;

import java.util.List;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sn.estm.service_consultation.clients.BonReponse;
import sn.estm.service_consultation.clients.PatientReponse;

@FeignClient(name = "service-patient", url = "http://localhost:8887")
public interface PatientFeignClient {
	
	@GetMapping("/api/bon/patient/{pid}")
     List<BonReponse> afficherListeBonPatient(@PathVariable UUID pid);
	@GetMapping("/api/patient/{id}") 
    PatientReponse afficheUnPatient(@PathVariable("id") UUID id);
}
