package com.api_rest.projetojava.controllers;

import java.util.Optional;

import com.api_rest.projetojava.entities.Instructions;
import com.api_rest.projetojava.entities.Patients;
import com.api_rest.projetojava.entities.Procedures;
import com.api_rest.projetojava.repositories.ProceduresRepository;
import com.api_rest.projetojava.repositories.InstructionsRepository;
import com.api_rest.projetojava.repositories.PatientsRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping(value="/Instruções")
public class ControllerInstructions {

    @Autowired
    private ProceduresRepository proceduresrepository;

    @Autowired
    private InstructionsRepository instructionrepository;

    @Autowired
    private PatientsRepository patientsrepository;

    @Operation(summary = "Criar instruções para um procedimento existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instrução inserida com sucesso"),
        @ApiResponse(responseCode = "400", description = "Procedimento não encontrado, verifique o ID passado")
    })
    //criar uma instruction
    @PostMapping(value="/CriarInstruções/{id}")
	public ResponseEntity<?> insert(@Parameter(description = "Instruções a serem criadas", required = true) 
        @RequestBody Instructions newInstructions,
        @Parameter(description = "ID do procedimento ao qual as instruções serão vinculadas", required = true) 
        @PathVariable Long id) {

        //verificar se o id da procedures é valido e retornar um obj da procedures
        Optional<Procedures> proceduresOptional = proceduresrepository.findById(id);

        if(proceduresOptional.isPresent()){

            Procedures procedures = proceduresOptional.get();

            Patients patientbByConsult = procedures.getConsult().getPatient();

            Instructions instruction = new Instructions();

            instruction.setInstructions(newInstructions.getInstructions());
            String medicines = newInstructions.getMedicines();

            //inseir remedio indicados
            instruction.setMedicines(medicines);

            procedures.setInstructions(instruction);
            procedures.setPatient(patientbByConsult);
            proceduresrepository.save(procedures);

            //mandar obj pro banco
            instructionrepository.save(instruction);

            return ResponseEntity.ok("Instrução inserida com sucesso.");

        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Procedimento de id "+id+" não encontrada, verifique o id passado.");

        }
        
    }

    @Operation(summary = "Deletar uma instrução por ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Instrução deletada com sucesso"),
        @ApiResponse(responseCode = "404", description = "Instrução não encontrada, tente novamente")
    })
    //deletar a instrução
    @DeleteMapping(value="/deletar/{id}")
	public ResponseEntity<String> Delete(@Parameter(description = "ID da instrução a ser deletada", required = true) 
        @PathVariable Long id) {

		Optional<Instructions> result = instructionrepository.findById(id);

		if(result.isPresent()){

			instructionrepository.deleteById(id);
			return ResponseEntity.ok("Instrução deletada com sucesso!");

		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instrução de id "+id+" não encontrada! tente novamente.");

		}


	}

}
