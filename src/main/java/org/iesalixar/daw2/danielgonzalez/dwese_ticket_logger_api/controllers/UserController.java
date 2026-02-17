package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.controllers;

import io.jsonwebtoken.Claims;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.dtos.UserDTO;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.services.UserService;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador REST para gestionar operaciones relacionadas con usuarios.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * Obtiene la información del usuario autenticado actualmente.
     *
     * @return UserDTO del usuario autenticado.
     */
    @GetMapping()
    public ResponseEntity<UserDTO> getUser(@RequestHeader("Authorization") String tokenHeader) {
        logger.info("Solicitando la informacion del usuario logueado");

        String token = tokenHeader.replace("Bearer ", "");

        Long id = jwtUtil.extractClaim(token, claims -> (Long) claims.get("id"));

        try{
            UserDTO userDTO = userService.getUserById(id);
            logger.info("Se han encontrado...");
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            logger.error("Error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id el identificador del usuario.
     * @return UserDTO del usuario.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO userDTO = userService.getUserById(id);
        return ResponseEntity.ok(userDTO);
    }
}
