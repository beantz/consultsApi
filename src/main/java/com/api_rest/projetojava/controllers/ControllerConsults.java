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
import com.api_rest.projetojava.service.ServiceConsult;
import com.api_rest.projetojava.service.ServicePatients;

@RestController
@RequestMapping(value="/Consultas")
public class ControllerConsults {

    //essa anotação instrui o spring a fornecer essas dependências
    @Autowired
	private PatientsRepository patientsRepository;

	@Autowired
    private ConsultsRepository consultsRepository;

	@Autowired
    private ServicePatients patientsservice;

	@Autowired
	private ServiceConsult consultservice;

    //criar uma consulta
    @PostMapping(value="/NovaConsulta/{id}")
	public ResponseEntity<?>  insert(@RequestBody Consults newConsults, @PathVariable Long id) {

		//Verificar se o paciente já está cadastrado
		if (patientsservice.verifyPatients(id)) {

			// Verificar se a data não está vazia e retornar ela
			ResponseEntity<Object> dateHoursResponse = consultservice.verifyDate(newConsults);

			//vai verificar o tipo e me retornar um tipo unico q eu preciso
			Object resultDate = consultservice.verifyType(dateHoursResponse);

			if(resultDate instanceof LocalDateTime){

				LocalDateTime dateHours = (LocalDateTime) resultDate;

				// Retornar um objeto de consulta com o ID do paciente
				ResponseEntity<Object> consultResponse = patientsservice.findById(id, false);

				//vai verificar o tipo e me retornar um tipo unico q eu preciso
				Object resultConsult = consultservice.verifyType(consultResponse);

				if(resultConsult instanceof Consults){

					Consults consult = (Consults) resultConsult;

					consult.setDateConsult(dateHours);
					consultservice.create(consult);

					return ResponseEntity.ok("Consulta marcada com sucesso!");
				}

			} else {

				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro! Data incorreta.");

			}
	
			// Se qualquer etapa falhar, retorne um erro indicando dados inválidos
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erro ao processar a consulta: dados inválidos.");
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERRO! não foi encontrado paciente com id "+id+" O paciente precisa estar cadastrado para realizar uma consulta.");
		}

	}

	//retornar todas as consultas do dia
	@GetMapping
	public ResponseEntity<List<Consults>> getConsultsByDate() {

        List<Consults> consults = consultsRepository.findAll(Sort.by(Sort.Direction.ASC, "dateConsult"));
        return ResponseEntity.ok(consults);

    }
	
    //deletar uma consulta
    @DeleteMapping(value="/deletar/{id}")
	public ResponseEntity<String> Delete(@PathVariable Long id){

		Optional<Consults> result = consultsRepository.findById(id);

		if(result.isPresent()){

			consultsRepository.deleteById(id);
			return ResponseEntity.ok("Consulta cancelada.");

		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consulta de "+id+" não encontrada! Id pode está errado, tente novamente.");

		}

	}

    //atualizar uma consulta
    @PutMapping(value="/Reagendamento/{id}")
	public ResponseEntity<?> Update(@PathVariable Long id, @RequestBody Consults updatedConsult){

		Optional<Consults> result = consultsRepository.findById(id);

		if(result.isPresent()){
			//pegando os dados atuais
			Consults consults = result.get();
			consults.setDateConsult(updatedConsult.getDateConsult());

			consultsRepository.save(consults);
			return ResponseEntity.ok("Consulta reagendada com sucesso!");

		} else {

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consulta de id "+id+" não encontrada! Id pode está errado, tente novamente.");

		}

	}

}
