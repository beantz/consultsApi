package com.api_rest.projetojava.Service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.stereotype.Component;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import com.api_rest.projetojava.repositories.ConsultsRepository;
import com.api_rest.projetojava.repositories.PatientsRepository;
import com.api_rest.projetojava.entities.Patients;
import com.api_rest.projetojava.entities.Consults;

import java.util.Map;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Component
public class ServicePatients {

    //injeção de dependência
    @Autowired
    private PatientsRepository patientsRepository;

    @Autowired
    private ConsultsRepository consultsRepository;
    
    //metodo para verificar se paciente ta cadstrado
    public boolean verifyPatients(Long id){

        Optional<Patients> resul = patientsRepository.findById(id);

        if(resul.isPresent()){

            return true;

        }

        return false;
        
    }

    public Boolean checkInput(Patients newPatient){

        if(!newPatient.getName().isEmpty() && !newPatient.getCity().isEmpty() && newPatient.getNumber() > 0) {

            return true;

        }

        return false;
    }

    public Boolean verifyNumber(Patients newPatient){

        // Verifica se já existe um paciente com o mesmo número
        return patientsRepository.existsByNumber(newPatient.getNumber());    

    }

    //metodo para retornar um obj de paciente ou um obj de consults com id do paciente
    //permite ser retornado tanto um response quanto um obj
    public ResponseEntity<Object> findById(@PathVariable Long id, boolean redirect) {

        Optional<Patients> result = patientsRepository.findById(id);

        //validação se o id existe no banco
        //usado quando quero resgatar um obj de patients completo
        if (result.isPresent() && redirect == true) {

            return ResponseEntity.ok(result.get());

            //usado quando quero criar um novo obj de consults com o id de patients
        } else if(result.isPresent() && redirect == false) {

            Consults consult = new Consults();
            consult.setId(id);
			return ResponseEntity.ok(consult);

        } else {

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado! Tente novamente.");

        }

    }
    
    public <S extends Patients> List<S> findAll(Example<S> example, Sort sort) {
        return patientsRepository.findAll(example, sort);
    }

}
