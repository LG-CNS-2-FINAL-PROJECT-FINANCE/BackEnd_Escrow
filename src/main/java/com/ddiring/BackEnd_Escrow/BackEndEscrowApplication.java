package com.ddiring.BackEnd_Escrow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = "com.ddiring.BackEnd_Escrow.client")
public class BackEndEscrowApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackEndEscrowApplication.class, args);
	}

}