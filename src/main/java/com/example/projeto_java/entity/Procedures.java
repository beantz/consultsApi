package com.example.projeto_java.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name="tb_procedures")
public class Procedures {
    
    //parei aqui
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String guidelines;

    @ManyToOne
    @JoinColumn(name="consult_id")
    private Consults consult;

    @ManyToOne
    @JoinColumn(name="patients_id")
    private Patients patients;

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

    public String getGuidelines(){
        return guidelines;
    }

    public void setGuidelines(String guidelines){
        this.guidelines = guidelines;
    }
}
