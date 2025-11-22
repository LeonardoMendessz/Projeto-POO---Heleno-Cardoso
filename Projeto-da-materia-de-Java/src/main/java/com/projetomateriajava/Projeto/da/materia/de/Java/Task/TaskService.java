package com.projetomateriajava.Projeto.da.materia.de.Java.Task;

import com.projetomateriajava.Projeto.da.materia.de.Java.User.UserModel;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    public TaskModel criarTarefa (TaskModel taskModel){
        return taskRepository.save(taskModel);
    }


    public List<TaskModel> buscarTodasTarefas(Boolean status) {

        if (status == null) {
            return taskRepository.findAll();
        } else {
            return taskRepository.findByconcluido(status);
        }


    }

    public List<TaskModel> buscarTarefaporUser (UserModel user){
        return taskRepository.findByUser(user);
    }

    public TaskModel attTarefa (Long id,boolean concluido){
       TaskModel tarefaEncontrada = taskRepository.findById(id).orElseThrow(()
               -> new RuntimeException("Tarefa com ID" + id + " não encontrado"));
       tarefaEncontrada.setConcluido(concluido);
       return taskRepository.save(tarefaEncontrada);

    }

    public void deletarTarefa(Long id){
       TaskModel tarefaDeletar = taskRepository.findById(id).orElseThrow(()
               -> new RuntimeException("Tarefa com ID" + id + " não encontrado"));

        taskRepository.delete(tarefaDeletar);
    }

    public void deletarTodasTarefas(){
        taskRepository.deleteAll();
    }

    @Transactional
    public void concluirTodas(){
        taskRepository.marcarTodasComoConcluidas();
    }

    public void deleteTask(TaskModel task) {
        taskRepository.delete(task);
    }

    public Optional<TaskModel> findById(Long id) {
        return taskRepository.findById(id);
    }

    public TaskModel saveTask(TaskModel task) {
        return taskRepository.save(task);
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }

    public List<TaskModel> findByUserAndConcluido(UserModel user, boolean concluido) {
        // Usa o novo método do Repositório
        return taskRepository.findByUserAndConcluido(user, concluido);
    }
}






