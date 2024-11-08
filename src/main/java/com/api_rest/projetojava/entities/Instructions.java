package com.api_rest.projetojava.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
@Table(name="tb_instructions")
public class Instructions {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //o que pode e não pode fazer, comer
    private String instructions;

    //remedios
    private String medicines;

    @OneToOne(mappedBy = "instructions")
    private Procedures procedures;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getInstructions(){
        return instructions;
    }

    public void setInstructions(String instructions){
        this.instructions = instructions;
    }

    public String getMedicines(){
        return medicines;
    }

    public void setMedicines(String medicines){
        this.medicines = medicines;
    }

    public void setProcedures(Procedures procedures){
        this.procedures = procedures; 
    }
    
    public Procedures getProcedures(){
        return procedures;
    }
    
}

