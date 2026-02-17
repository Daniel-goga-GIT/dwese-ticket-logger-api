package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.dtos;

import lombok.*;

/**
 * DTO para transferir información básica de un usuario.
 * Incluye id, username, nombre, apellido e imagen.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDTO {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private String image;
    private boolean enabled;
}
