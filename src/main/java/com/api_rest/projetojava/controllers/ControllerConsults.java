package com.api_rest.projetojava.controllers;

import java.util.List;
import java.time.LocalDateTime;
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
import org.springframework.data.domain.Sort;

import com.api_rest.projetojava.entities.Consults;
import com.api_rest.projetojava.entities.Patients;
import com.api_rest.projetojava.repositories.ConsultsRepository;
import com.api_rest.projetojava.repositories.PatientsRepository;
import com.api_rest.projetojava.Service.ServiceConsult;
import com.api_rest.projetojava.Service.ServicePatients;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/consultas")
public class ControllerConsults {

	@Autowired
	private PatientsRepository patientsRepository;

	@Autowired
    private ConsultsRepository consultsRepository;

	@Autowired
    private ServicePatients patientsservice;

	@Autowired
	private ServiceConsult consultservice;

    @Operation(
        summary = "Marcar nova consulta",
        description = "Cria uma nova consulta para um paciente existente, verificando se o paciente está cadastrado e se a data é válida."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta marcada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Erro ao processar a consulta: dados inválidos ou data incorreta"),
        @ApiResponse(responseCode = "404", description = "Paciente não encontrado")
    })

    @PostMapping(value = "/NovaConsulta/{id}")
    public ResponseEntity<?> insert(
        @Parameter(description = "ID do paciente", example = "1")
        @PathVariable Long id,
        
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Objeto da consulta contendo os detalhes da nova consulta"
        )
        @RequestBody Consults newConsults
    ) {
        // Verificar se o paciente está cadastrado
        if (patientsservice.verifyPatients(id)) {

            Optional<Patients> optionalPatient = patientsRepository.findById(id);
            Patients patient = optionalPatient.get();

            // Verificar se a data não está vazia e retornar ela
            ResponseEntity<Object> dateHoursResponse = consultservice.verifyDate(newConsults);

            // Verificar o tipo de dado retornado
            Object resultDate = consultservice.verifyType(dateHoursResponse);

            if (resultDate instanceof LocalDateTime) {
                LocalDateTime dateHours = (LocalDateTime) resultDate;

                Consults consult = new Consults();
                consult.setDateConsult(dateHours);
                consult.setPatient(patient);
                consultsRepository.save(consult);

                return ResponseEntity.ok("Consulta marcada com sucesso!");
                
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro! Data incorreta..");
            }

        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERRO! não foi encontrado paciente com id "+id+" O paciente precisa estar cadastrado para realizar uma consulta.");
        }
    }
	
	@Operation(summary = "Obter todas as consultas ordenadas pela data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de consultas retornada com sucesso"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
	//retornar todas as consultas do dia
	@GetMapping
	public ResponseEntity<List<Consults>> getConsultsByDate() {

        List<Consults> consults = consultsRepository.findAll(Sort.by(Sort.Direction.ASC, "dateConsult"));
        return ResponseEntity.ok(consults);

    }

    @Operation(summary = "Deletar uma consulta pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta cancelada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Consulta não encontrada. Id pode estar incorreto"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    //deletar uma consulta
    @DeleteMapping(value="/deletar/{id}")
	public ResponseEntity<String> Delete(@PathVariable Long id){

		Optional<Consults> consultsOptional = consultsRepository.findById(id);

		if(consultsOptional.isPresent()){

			consultsRepository.deleteById(id);
			return ResponseEntity.ok("Consulta cancelada.");

		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consulta de id "+id+" não encontrada! Id pode está errado, tente novamente.");

		}

	}

	@Operation(summary = "Reagendar uma consulta existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Consulta reagendada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Consulta não encontrada. Id pode estar incorreto"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    //atualizar uma consulta
    @PutMapping(value="/Reagendamento/{id}")
	public ResponseEntity<?> Update(@PathVariable Long id, @RequestBody Consults updatedConsult){

		Optional<Consults> consultsOptional = consultsRepository.findById(id);

		if(consultsOptional.isPresent()){
			//pegando os dados atuais
			Consults consults = consultsOptional.get();

            consults.setDateConsult(updatedConsult.getDateConsult());

            consultsRepository.save(consults);
            return ResponseEntity.ok("Consulta reagendada com sucesso!");

		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consulta de id "+id+" não encontrada! Id pode está errado, tente novamente.");

		}

	}

}
