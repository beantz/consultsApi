package com.api_rest.projetojava.entities;

import com.api_rest.projetojava.entities.Procedures;
import com.api_rest.projetojava.entities.Patients;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.time.LocalDateTime;

@Entity
@Table(name="tb_consults")
public class Consults {
    
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="date_consult")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateConsult;

    @OneToMany
    @JoinColumn(name="consult_id") /* define qual será a coluna da tabela de procedimentos (Procedures) que irá armazenar a chave estrangeira (foreign key) referente à consulta. */
    private List<Procedures> procedures;

    @ManyToOne
    @JoinColumn(name="patients_id")
    private Patients patient; 

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public LocalDateTime getDateConsult(){
        return dateConsult;
    }
    
    public void setDateConsult(LocalDateTime dateConsult){
        this.dateConsult = dateConsult;
    }

    //metodo usada para inserir o id de usuario
    public void setPatient(Patients patient){
        this.patient = patient; 
    }
    
    public Patients getPatient(){
        return patient;
    }

}