package com.api_rest.projetojava.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.api_rest.projetojava.entities.Patients;
import com.api_rest.projetojava.repositories.PatientsRepository;
import com.api_rest.projetojava.service.ServicePatients;

@RestController
@RequestMapping(value="/pacientes")
public class ControllerPatients {
	
	@Autowired
	private PatientsRepository patientsRepository;

	@Autowired
	private ServicePatients patientsservice;
	
	//retornar todos
	@GetMapping
	public List<Patients> findAll(){

		List<Patients> result = patientsRepository.findAll();
		return result;
	}
	
	//retornar pelo id
	@GetMapping(value = "/{id}")
	public Object findById(@PathVariable Long id) {

		return patientsservice.findById(id, true);

	}
	
	//cadastrar um registro novo
	@PostMapping(value="/cadastrar")
	public ResponseEntity<?> insert(@RequestBody Patients newPatient) {
		
		//validar se os campos não estão vazios
		if (patientsservice.checkInput(newPatient)) {

			if(!patientsservice.verifyNumber(newPatient)){

				patientsRepository.save(newPatient);
				return ResponseEntity.ok("Paciente cadastrado com sucesso!");

			} else {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Numero de telefone "+newPatient.getNumber()+" ja cadastrado.");

			}

		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos os campos precisam estar preenchidos!.");
		
		}

	}

	//apagar um  registro
	@DeleteMapping(value="/deletar/{id}")
	public ResponseEntity<String> Delete(@PathVariable Long id){

		Optional<Patients> result = patientsRepository.findById(id);

		if(result.isPresent()){

			patientsRepository.deleteById(id);
			return ResponseEntity.ok("Usuario deletado com sucesso!");

		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario de id "+id+" não encontrado! tente novamente.");

		}


	}

	//atualizar um registro
	@PutMapping(value="/atualizar/{id}")
	public ResponseEntity<?> Update(@PathVariable Long id, @RequestBody Patients updatedPatient){

		Optional<Patients> result = patientsRepository.findById(id);

		if(result.isPresent()){
			//pegando os dados atuais
			Patients patient = result.get();
			patient.setName(updatedPatient.getName());
			patient.setCity(updatedPatient.getCity());
			patient.setNumber(updatedPatient.getNumber());

			patientsRepository.save(patient);
			return ResponseEntity.ok("Paciente atualizado com sucesso!");

		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario de id "+id+" não encontrado! tente novamente.");

		}


	}
}
