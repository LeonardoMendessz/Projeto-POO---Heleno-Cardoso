package com.projetomateriajava.Projeto.da.materia.de.Java.Task;

import com.projetomateriajava.Projeto.da.materia.de.Java.User.UserModel;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
public class TaskModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserModel user;

    private String descricao;
    private Boolean concluido;

    @CreationTimestamp
    private Date dataCriacao;



}
