package com.projetomateriajava.Projeto.da.materia.de.Java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan; // <-- NOVO IMPORT

@SpringBootApplication

@ComponentScan("com.projetomateriajava.Projeto.da.materia.de.Java")
public class ProjetoDaMateriaDeJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoDaMateriaDeJavaApplication.class, args);
	}

}