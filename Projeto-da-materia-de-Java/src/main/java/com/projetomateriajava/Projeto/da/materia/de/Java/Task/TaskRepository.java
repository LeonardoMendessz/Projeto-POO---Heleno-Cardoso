package com.projetomateriajava.Projeto.da.materia.de.Java.Task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskModel, Long>{
    List<TaskModel>findByconcluido(boolean status);
}
