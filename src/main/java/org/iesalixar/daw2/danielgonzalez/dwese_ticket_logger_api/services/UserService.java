package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.dtos.UserDTO;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities.Role;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities.User;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.mapper.UserMapper;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities(user.getRoles().stream().map(Role::getName).toList().toArray(new String[0]))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }

    public Long getIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(User::getId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + username));
    }

    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::toDTO)
                .orElseThrow(() -> new RuntimeException("El usuario con identificador " + id + " no existe"));
    }
}