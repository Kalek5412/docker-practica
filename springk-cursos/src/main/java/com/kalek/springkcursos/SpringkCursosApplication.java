package com.kalek.springkcursos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableFeignClients
public class SpringkCursosApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringkCursosApplication.class, args);
	}

}
