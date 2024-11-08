package com.api_rest.projetojava.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_rest.projetojava.entities.Patients;

public interface PatientsRepository extends JpaRepository<Patients, Long> {

    boolean existsByNumber(int number);

}
