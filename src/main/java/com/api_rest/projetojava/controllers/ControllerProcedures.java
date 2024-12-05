package com.api_rest.projetojava.controllers;

import com.api_rest.projetojava.entities.Procedures;
import com.api_rest.projetojava.entities.Consults;
import com.api_rest.projetojava.entities.Instructions;
import com.api_rest.projetojava.entities.Patients;
import com.api_rest.projetojava.repositories.PatientsRepository;
import com.api_rest.projetojava.repositories.ConsultsRepository;
import com.api_rest.projetojava.repositories.ProceduresRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping(value="/Procedimentos")
public class ControllerProcedures {
    
    @Autowired
	private PatientsRepository patientsrepository;

    @Autowired
	private ProceduresRepository proceduresrepository;

    @Autowired
    private ConsultsRepository consultsrepository;

    @Operation(summary = "Criar um procedimento vinculado a uma consulta existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Procedimento criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Consulta ou paciente não encontrado")
    })
    //criar um procedimento, vai receber o id da consulta
    @PostMapping(value="/CriarProcedimento/{id}")
	public ResponseEntity<?> insert(@Parameter(description = "Dados do procedimento a ser criado", required = true) 
        @RequestBody Procedures newProcedures,
        @Parameter(description = "ID da consulta para vincular ao procedimento", required = true) 
        @PathVariable Long id) {

        //verificar se o id da consulta é valido e retornar um obj da consulta
        Optional<Consults> consultOptional = consultsrepository.findById(id);

        if(consultOptional.isPresent()){
            
            Consults consult = consultOptional.get();

            Long id_patient = consult.getPatient().getId();

            //vai retornar um obj de paciente atraves do id de paciente que vem na consulta
            Optional<Patients> patientOptional = patientsrepository.findById(id_patient);

            if(patientOptional.isPresent()){

                Patients patient = patientOptional.get();

                Procedures procedures = new Procedures();

                //inserir nome do procedimento
                procedures.setName(newProcedures.getName());

                //inserir consulta
                procedures.setConsult(consult);

                //inseir paciente
                procedures.setPatient(patient);

                //mandar obj pro banco
                proceduresrepository.save(procedures);

                return ResponseEntity.ok("Procedimento criado com sucesso.");

            } else {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Paciente de id "+id_patient+" não encontrada.");

            }

        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Consulta de id "+id+" não encontrada.");

        }
        
    }

    @Operation(summary = "Retornar todos os procedimentos de uma consulta")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Procedimentos encontrados com sucesso"),
        @ApiResponse(responseCode = "404", description = "Consulta não encontrada com o ID fornecido/Consulta não possui procedimentos.")
    })
    //retornar todos os procedimentos de uma consulta List<Procedures>
    @GetMapping("/ConsultarProcedimentos/{id}")
	public ResponseEntity<?> findAllProcedures(@Parameter(description = "ID da consulta para retornar os procedimentos", required = true) 
        @PathVariable Long id) {

		List<Procedures> result = proceduresrepository.findProceduresByConsultId(id);

        if(!result.isEmpty()){

            return ResponseEntity.ok(result);

        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consulta de id "+id+" não foi encontrada ou ainda possui procedimentos cadastrados nela.");

        }

	}

    @Operation(summary = "Deletar um procedimento pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Procedimento deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Procedimento não encontrado com o ID fornecido")
    })
    //remover procedimentos
    @DeleteMapping(value="/deletar/{id}")
	public ResponseEntity<String> Delete(@Parameter(description = "ID do procedimento a ser deletado", required = true) 
        @PathVariable Long id) {

		Optional<Procedures> result = proceduresrepository.findById(id);

		if(result.isPresent()){

			proceduresrepository.deleteById(id);
			return ResponseEntity.ok("Procedimento deletado.");

		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Procedimento de id "+id+" não encontrado! Id pode está errado.");

		}

	}

}
