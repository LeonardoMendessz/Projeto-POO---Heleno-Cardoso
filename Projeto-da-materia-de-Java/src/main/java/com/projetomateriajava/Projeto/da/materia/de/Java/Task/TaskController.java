package com.projetomateriajava.Projeto.da.materia.de.Java.Task;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.projetomateriajava.Projeto.da.materia.de.Java.User.UserModel;
import com.projetomateriajava.Projeto.da.materia.de.Java.User.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RequestMapping("task")
@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserRepository  userRepository;


    @PostMapping("/new")
    public String addTask(@ModelAttribute("newTask") TaskModel task) {

        // 1. Obtém o nome de usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 2. Busca o UserModel completo no banco de dados
        UserModel currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado!"));

        // 3. ASSOCIA O USUÁRIO À TAREFA antes de salvar (CHAVE DE SEGURANÇA)
        task.setUser(currentUser);


        task.setConcluido(false);

        // 4. Salva a tarefa
        taskService.criarTarefa(task);

        // 5. Redireciona para a página de listagem
        return "redirect:/task";
    }

    @GetMapping("")
    public String viewTaskList(
            @RequestParam(value = "filter", required = false) String filter,
            Model model) {

        // 1. Obter o usuário logado (Gustavo)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 2. Buscar o objeto UserModel (Entity) no banco de dados
        UserModel currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado no BD!"));

        // 3. Lógica de Filtragem
        List<TaskModel> tasks;

        if ("completed".equals(filter)) {
            // Busca tarefas concluídas (true)
            tasks = taskService.findByUserAndConcluido(currentUser, true);
        } else if ("uncompleted".equals(filter)) {
            // Busca tarefas não concluídas (false)
            tasks = taskService.findByUserAndConcluido(currentUser, false);
        } else {
            // Padrão: Busca todas as tarefas do usuário (se filter for nulo ou 'all')
            tasks = taskService.buscarTarefaporUser(currentUser);
        }

        // 4. Adicionar atributos ao Model
        model.addAttribute("username", username);
        model.addAttribute("tasks", tasks);
        model.addAttribute("newTask", new TaskModel());

        // NOVO: Adiciona o filtro atual para que o HTML possa destacar o botão correto
        model.addAttribute("currentFilter", filter != null ? filter : "all");

        return "task-page";
    }

    @PostMapping("/delete/{id}")
    public String deleteTask(@PathVariable Long id) {


        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();


        UserModel currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado!"));


        TaskModel taskToDelete = taskService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada."));


        if (!taskToDelete.getUser().getId().equals(currentUser.getId())) {

            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para deletar esta tarefa.");
        }


        taskService.deleteTask(taskToDelete);


        return "redirect:/task";
    }


    @PostMapping("/toggle/{id}") // Nova rota para alternar o status
    public String toggleTaskStatus(@PathVariable Long id) {

        // 1. Obter o usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // 2. Buscar o UserModel
        UserModel currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado!"));

        // 3. Buscar a tarefa existente
        TaskModel existingTask = taskService.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tarefa não encontrada."));

        // 4. VERIFICAÇÃO DE PROPRIEDADE (Segurança)
        if (!existingTask.getUser().getId().equals(currentUser.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Você não tem permissão para modificar esta tarefa.");
        }

        // 5. Alternar o status: (Concluído <-> Não Concluído)
        existingTask.setConcluido(!existingTask.getConcluido());

        // 6. Salvar a alteração (o método saveTask que você já tem)
        taskService.saveTask(existingTask);

        // 7. Redirecionar para a lista
        return "redirect:/task";
    }



    @PostMapping("/complete-all")
    public String completeAllTasks() {

        // 1. Obter o usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserModel currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado!"));

        // 2. Buscar TODAS as tarefas do usuário
        List<TaskModel> userTasks = taskService.buscarTarefaporUser(currentUser);

        // 3. Aplicar a mudança em massa
        for (TaskModel task : userTasks) {
            task.setConcluido(true); // Define como CONCLUÍDA
            taskService.saveTask(task);
        }

        // 4. Redirecionar
        return "redirect:/task";
    }



    @PostMapping("/delete-all")
    public String deleteAllTasks() {

        // 1. Obter o usuário logado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        UserModel currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuário logado não encontrado!"));

        // 2. Buscar TODAS as tarefas do usuário
        List<TaskModel> userTasks = taskService.buscarTarefaporUser(currentUser);

        // 3. Aplicar a exclusão em massa
        for (TaskModel task : userTasks) {
            taskService.deleteTask(task.getId());
        }

        // 4. Redirecionar
        return "redirect:/task";
    }
}
