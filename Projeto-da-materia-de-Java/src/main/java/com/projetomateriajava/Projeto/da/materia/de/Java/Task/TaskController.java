package com.projetomateriajava.Projeto.da.materia.de.Java.Task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("task")
@RestController
public class TaskController {

    @Autowired
    private TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskModel> criarNovaTarefa(@RequestBody TaskModel taskModel){
        TaskModel novatask = taskService.criarTarefa(taskModel);

        return ResponseEntity.status(HttpStatus.CREATED).body(novatask);
    }

    @GetMapping
    public ResponseEntity<List<TaskModel>> buscarTodas(@RequestParam (value = "concluido", required = false) Boolean statusConcluido){
        List<TaskModel> tarefas = taskService.buscarTodasTarefas(statusConcluido);
        if(tarefas.isEmpty()){
            return ResponseEntity.noContent().build(); // se a lista estiver vazia retorna 204
        }
        return ResponseEntity.ok(tarefas);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskModel> atualizarTarefa(@PathVariable Long id,@RequestParam boolean concluido){

        TaskModel tarefaAtt = taskService.attTarefa(id,concluido);

        return ResponseEntity.ok(tarefaAtt);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTarefa(@PathVariable Long id){
        taskService.deletarTarefa(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deletarTodas(){

        taskService.deletarTodasTarefas();
        return ResponseEntity.noContent().build();
    }


}
