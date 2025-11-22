package com.projetomateriajava.Projeto.da.materia.de.Java.Security;

import com.projetomateriajava.Projeto.da.materia.de.Java.User.UserModel;
import com.projetomateriajava.Projeto.da.materia.de.Java.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserModel userModel = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario não encontrado: " + username) );

        return org.springframework.security.core.userdetails.User.builder()
                .username(userModel.getUsername())
                .password(userModel.getPassword())
                .roles("USER")
                .build();
    }
}
