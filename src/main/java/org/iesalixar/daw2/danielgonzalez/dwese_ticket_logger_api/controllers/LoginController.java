package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    /**
     * Maneja las solicitudes GET a /login para obtener información sobre el endpoint.
     *
     * @return ResponseEntity con información sobre cómo autenticarse.
     */
    @GetMapping("/login")
    public ResponseEntity<?> loginInfo() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Use POST /login with credentials to authenticate");
        response.put("required_fields", "username, password");
        return ResponseEntity.ok(response);
    }

    /**
     * Maneja las solicitudes POST a /login para autenticar al usuario.
     * Espera un JSON con campos: username y password.
     *
     * @param credentials Map con las credenciales del usuario (username y password).
     * @return ResponseEntity con un mensaje de éxito o error.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");

        // Validar que se proporcionen credenciales
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Username and password are required");
            return ResponseEntity.badRequest().body(error);
        }

        // TODO: Implementar lógica de autenticación real (verificación contra BD, JWT, etc.)
        // Por ahora, una respuesta básica

        Map<String, String> response = new HashMap<>();
        response.put("message", "Login successful");
        response.put("username", username);
        response.put("token", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."); // Placeholder
        return ResponseEntity.ok(response);
    }
}