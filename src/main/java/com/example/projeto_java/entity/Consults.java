package com.example.projeto_java.entity;
import com.example.projeto_java.entity.Procedures;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.ManyToOne;
import java.util.List;

@Entity
@Table(name="tb_consults")
public class Consults {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="data_consult")
    @Temporal(TemporalType.DATE)
    private Date dateConsult;

    @OneToMany
    @JoinColumn(name="consults_id") /* define qual será a coluna da tabela de procedimentos (Procedures) que irá armazenar a chave estrangeira (foreign key) referente à consulta. */
    private List<Procedures> procedures;

    @ManyToOne
    @JoinColumn(name="patient_id")
    private Patients patient;

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Date getDate(){
        return dateConsult;
    }

    public void setDate(Date dateConsult){
        this.dateConsult = dateConsult;
    }
    

}
