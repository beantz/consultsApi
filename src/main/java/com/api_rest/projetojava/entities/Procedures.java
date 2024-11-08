package com.api_rest.projetojava.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;

import com.api_rest.projetojava.entities.Patients;
import com.api_rest.projetojava.entities.Consults;
import com.api_rest.projetojava.entities.Instructions;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="tb_procedures")
public class Procedures {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    @JoinColumn(name="consult_id")
    private Consults consult;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="patients_id")
    private Patients patient;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="instructions_id", referencedColumnName = "id")
    private Instructions instructions;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setPatient(Patients patient){
        this.patient = patient; 
    }
    
    public Patients getPatient(){
        return patient;
    }

    public void setConsult(Consults consult){
        this.consult = consult; 
    }
    
    public Consults getConsult(){
        return consult;
    }

    public Instructions getInstructions() {
        return instructions;
    }

    public void setInstructions(Instructions instructions) {
        this.instructions = instructions;
    }

}
