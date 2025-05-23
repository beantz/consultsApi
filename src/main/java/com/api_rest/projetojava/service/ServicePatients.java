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
    
    public <S extends Patients> List<S> findAll(Example<S> example, Sort sort) {
        return patientsRepository.findAll(example, sort);
    }

}
