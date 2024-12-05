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
import com.api_rest.projetojava.Service.ServicePatients;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value="/pacientes")
public class ControllerPatients {
	
	@Autowired
	private PatientsRepository patientsRepository;

	@Autowired
	private ServicePatients patientsservice;
	
	@Operation(summary = "Retornar todos os pacientes")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de pacientes retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
	//retornar todos
	@GetMapping
	public List<Patients> findAll(){

		List<Patients> result = patientsRepository.findAll();
		return result;
	}
	
	@Operation(summary = "Buscar paciente pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente encontrado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
	//retornar pelo id
	@GetMapping(value = "/{id}")
	public ResponseEntity<Object> findById(@Parameter(description = "ID do paciente a ser buscado", example = "1", required = true)
	@PathVariable Long id) {

		Optional<Patients> result = patientsRepository.findById(id);

		if(result.isPresent()){

			return ResponseEntity.ok(result.get()); 

		}

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário não encontrado, verifique o id e tente novamente.");

	}
	
	@Operation(summary = "Cadastrar um novo paciente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente cadastrado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro de validação: número de telefone já cadastrado ou campos obrigatórios vazios"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
	//cadastrar um registro novo
	@PostMapping(value="/cadastrar")
	public ResponseEntity<?> insert(@Parameter(description = "Dados do novo paciente", required = true)
	@RequestBody Patients newPatient) {
		
		//validar se os campos não estão vazios
		if (patientsservice.checkInput(newPatient)) {

			if(!patientsservice.verifyNumber(newPatient)){

				patientsRepository.save(newPatient);
				return ResponseEntity.ok("Paciente cadastrado com sucesso!");

			} else {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Numero de telefone "+newPatient.getNumber()+" já está cadastrado.");

			}

		} else {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Todos os campos precisam estar preenchidos!.");
		
		}

	}

	@Operation(summary = "Deletar um paciente pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado com o ID fornecido")
    })
	//apagar um  registro
	@DeleteMapping(value="/deletar/{id}")
	public ResponseEntity<String> Delete(@Parameter(description = "ID do paciente a ser deletado", required = true)
			@PathVariable Long id) {

		Optional<Patients> result = patientsRepository.findById(id);

		if(result.isPresent()){

			patientsRepository.deleteById(id);
			return ResponseEntity.ok("Usuario deletado com sucesso!");

		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario de id "+id+" não encontrado! tente novamente.");

		}

	}


    @Operation(summary = "Atualizar informações de um paciente pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Paciente atualizado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado com o ID fornecido")
    })
	//atualizar um registro
	@PutMapping(value="/atualizar/{id}")
	public ResponseEntity<?> Update(@Parameter(description = "ID do paciente a ser atualizado", required = true) 
		@PathVariable Long id,
		@Parameter(description = "Dados atualizados do paciente", required = true) 
		@RequestBody Patients updatedPatient) {

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
