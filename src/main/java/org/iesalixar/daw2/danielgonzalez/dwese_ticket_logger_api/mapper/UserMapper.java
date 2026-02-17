package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.mapper;

import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.dtos.UserDTO;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities.User;
import org.springframework.stereotype.Component;

/**
 * Mapper para convertir entre la entidad User y sus DTOs.
 */
@Component
public class UserMapper {

    /**
     * Convierte una entidad `User` a un `UserDTO` (datos básicos).
     *
     * @param user Entidad de user.
     * @return DTO correspondiente.
     */
    public UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }
        
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setImage(user.getImage());
        dto.setEnabled(user.isEnabled());
        return dto;
    }

    /**
     * Convierte un `UserDTO` a una entidad `User`.
     * Nota: No incluye password ni roles, solo datos básicos de perfil.
     *
     * @param dto DTO de User.
     * @return Entidad User
     */
    public User toEntity(UserDTO dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setImage(dto.getImage());
        user.setEnabled(dto.isEnabled());
        return user;
    }
}