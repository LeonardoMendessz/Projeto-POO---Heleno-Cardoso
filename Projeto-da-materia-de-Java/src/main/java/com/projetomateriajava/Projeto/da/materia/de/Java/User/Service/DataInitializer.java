package com.projetomateriajava.Projeto.da.materia.de.Java.User.Service;

import com.projetomateriajava.Projeto.da.materia.de.Java.User.UserModel;
import com.projetomateriajava.Projeto.da.materia.de.Java.User.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.findByUsername("Gustavo").isEmpty()) {

            UserModel gustavo = new UserModel();
            gustavo.setUsername("Gustavo");

            gustavo.setPassword(passwordEncoder.encode("10203040"));

            userRepository.save(gustavo);
            System.out.println("Usuário 'Gustavo' inicializado com sucesso.");
        }
    }
}
