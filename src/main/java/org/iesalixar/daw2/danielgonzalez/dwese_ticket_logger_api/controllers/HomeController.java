package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class HomeController {
    /**
     * Retorna un mensaje de bienvenida a la API.
     *
     * @return ResponseEntity con un mensaje de bienvenida en formato JSON.
     */
    @GetMapping("/")
    public ResponseEntity<?> home() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Bienvenido a la API de Ticket Logger");
        response.put("version", "1.0");
        response.put("status", "API is running");
        return ResponseEntity.ok(response);
    }
}

