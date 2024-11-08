package com.api_rest.projetojava.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.api_rest.projetojava.entities.Consults;

public interface ConsultsRepository extends JpaRepository<Consults, Long> {

}
