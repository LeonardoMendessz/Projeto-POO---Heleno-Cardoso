package com.projetomateriajava.Projeto.da.materia.de.Java.Task;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity(name= "Tabela-task")
@Data
public class TaskModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String descricao;
    private Boolean concluido;

    @CreationTimestamp
    private Date dataCriacao;



}
