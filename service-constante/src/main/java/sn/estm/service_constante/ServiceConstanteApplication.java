package sn.estm.service_constante;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class ServiceConstanteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceConstanteApplication.class, args);
	}

}
