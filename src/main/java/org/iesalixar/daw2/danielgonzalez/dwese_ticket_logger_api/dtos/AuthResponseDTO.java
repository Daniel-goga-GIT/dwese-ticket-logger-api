package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.dtos;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String message;
}
