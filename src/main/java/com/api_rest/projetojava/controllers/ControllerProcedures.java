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

    //criar um procedimento, vai receber o id da consulta
    @PostMapping(value="/CriarProcedimento/{id}")
	public ResponseEntity<?> insert(@RequestBody Procedures newProcedures, @PathVariable Long id) {

        //verificar se o id da consulta é valido e retornar um obj da consulta
        Optional<Consults> consultOptional = consultsrepository.findById(id);

        if(consultOptional.isPresent()){
            
            Consults consult = consultOptional.get();

            //vai retornar um obj de paciente atraves do id de paciente que vem na consulta
            Optional<Patients> patientOptional = patientsrepository.findById(consult.getPatient().getId());

            if(patientOptional.isPresent()){

                Patients patient = patientOptional.get();

                Procedures procedures = new Procedures();

                String name = newProcedures.getName();

                //inserir nome do procedimento
                procedures.setName(name);

                //inserir consulta
                procedures.setConsult(consult);

                //inseir paciente
                procedures.setPatient(patient);

                //mandar obj pro banco
                proceduresrepository.save(procedures);

                return ResponseEntity.ok("Procedimento criado com sucesso.");

            } else {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Paciente não encontrada.");

            }

        } else {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Consulta não encontrada.");

        }
        
    }

    //retornar todos os procedimentos de uma consulta List<Procedures>
    @GetMapping("/ConsultarProcedimentos/{id}")
	public ResponseEntity<?> findAllProcedures(@PathVariable Long id){

		List<Procedures> result = proceduresrepository.findProceduresByConsultId(id);

        if(!result.isEmpty()){

            return ResponseEntity.ok(result);

        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consulta não entrada, verifique o id passado.");

        }

	}

    //remover procedimentos
    @DeleteMapping(value="/deletar/{id}")
	public ResponseEntity<String> Delete(@PathVariable Long id){

		Optional<Procedures> result = proceduresrepository.findById(id);

		if(result.isPresent()){

			proceduresrepository.deleteById(id);
			return ResponseEntity.ok("Procedimento deletado.");

		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Procedimento não encontrado! Id pode está errado, tente novamente.");

		}

	}

}
