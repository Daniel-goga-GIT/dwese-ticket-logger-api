package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio para la entidad User que extiende JpaRepository.
 * Proporciona operaciones CRUD y consultas personalizadas para la entidad User.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca un usuario por su nombre de usuario.
     *
     * @param username el nombre de usuario a buscar.
     * @return un Optional que contiene el usuario si se encuentra, o vacío si no existe.
     */
    Optional<User> findByUsername(String username);

    /**
     * Verifica si existe un usuario con el nombre de usuario dado.
     *
     * @param username el nombre de usuario a verificar.
     * @return true si existe un usuario con ese nombre, false en caso contrario.
     */
    boolean existsByUsername(String username);
}