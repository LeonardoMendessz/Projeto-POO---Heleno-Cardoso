package com.projetomateriajava.Projeto.da.materia.de.Java.Task;

import com.projetomateriajava.Projeto.da.materia.de.Java.User.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<TaskModel, Long>{
    List<TaskModel>findByconcluido(boolean status);
    List<TaskModel> findByUserUsername(String username);
    List<TaskModel> findByUserAndConcluido(UserModel user, boolean concluido);
    List<TaskModel>findByUser(UserModel user);

    @Modifying
    @Query("UPDATE TaskModel t SET t.concluido = true")
    void marcarTodasComoConcluidas();
}
