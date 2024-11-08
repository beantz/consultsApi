package com.api_rest.projetojava.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_rest.projetojava.entities.Instructions;

public interface InstructionsRepository extends JpaRepository<Instructions, Long> {

}