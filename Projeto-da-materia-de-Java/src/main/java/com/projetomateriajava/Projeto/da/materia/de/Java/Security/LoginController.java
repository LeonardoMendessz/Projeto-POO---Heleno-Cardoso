package com.projetomateriajava.Projeto.da.materia.de.Java.Security;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    // Mapeia a URL raiz para redirecionar para a página de login/tarefas
    @GetMapping("/")
    public String rootRedirect() {
        return "redirect:/task";
    }

    // Mapeia a URL de login (necessária para que o Thymeleaf encontre a view)
    @GetMapping("/login")
    public String login() {
        return "login-page";
    }
}