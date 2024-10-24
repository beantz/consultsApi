package com.example.projeto_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.projeto_java.entity.Patients;

@SpringBootApplication
public class ProjetoJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjetoJavaApplication.class, args);

		Patients Patients = new Patients();

		Patients.setName("Beatriz");
		String nome = Patients.getName();
		System.out.println("seu nome é "+nome);
	}

}
