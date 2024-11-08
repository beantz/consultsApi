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

@RestController
@RequestMapping(value="/Instruções")
public class ControllerInstructions {

    @Autowired
    private ProceduresRepository proceduresrepository;

    @Autowired
    private InstructionsRepository instructionrepository;

    @Autowired
    private PatientsRepository patientsrepository;

    //criar uma instruction
    @PostMapping(value="/CriarInstruções/{id}")
	public ResponseEntity<?> insert(@RequestBody Instructions newInstructions, @PathVariable Long id) {

        //verificar se o id da procedures é valido e retornar um obj da procedures
        Optional<Procedures> proceduresOptional = proceduresrepository.findById(id);

        if(proceduresOptional.isPresent()){

            Procedures procedures = proceduresOptional.get();

            Patients patientbByConsult = procedures.getConsult().getPatient();

            Instructions instruction = new Instructions();

            String instructions = newInstructions.getInstructions();
            String medicines = newInstructions.getMedicines();

            //inserir nome do procedimento
            instruction.setInstructions(instructions);

            //inseir remedio indicados
            instruction.setMedicines(medicines);

            procedures.setInstructions(instruction);
            procedures.setPatient(patientbByConsult);
            proceduresrepository.save(procedures);

            //mandar obj pro banco
            instructionrepository.save(instruction);

            return ResponseEntity.ok("Instrução inserida com sucesso.");
            

        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Procedimento não encontrada, verifique o id passado.");

        }
        
    }

    //deletar a instrução
    @DeleteMapping(value="/deletar/{id}")
	public ResponseEntity<String> Delete(@PathVariable Long id){

		Optional<Instructions> result = instructionrepository.findById(id);

		if(result.isPresent()){

			instructionrepository.deleteById(id);
			return ResponseEntity.ok("Instrução deletada com sucesso!");

		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Instrução não encontrada! tente novamente.");

		}


	}

}
