package com.api_rest.projetojava.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

import com.api_rest.projetojava.entities.Instructions;
import com.api_rest.projetojava.entities.Procedures;

public interface ProceduresRepository extends JpaRepository<Procedures, Long> {

    @Query("SELECT t FROM Procedures t WHERE t.consult.id = :consultId")
    List<Procedures> findProceduresByConsultId(@Param("consultId") Long id);

}

//TESTAR SE ISSO AQUI VAI FUNCIONAR