package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * La clase {@code Supermarket} representa una entidad que modela un supermercado.
 *
 * Contiene dos campos: {@code id} y {@code name}, donde:
 * - {@code id} es el identificador unico del supermercado.
 * - {@code name} es el nombre del supermercado.
 */
@Entity
@Table(name = "supermarkets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Supermarket {

    // ============================================================
    // Campos de la entidad
    // ============================================================

    /** Identificador unico del supermercado (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Nombre del supermercado. No puede estar vacio. */
    @NotEmpty(message = "{msg.supermarket.name.notEmpty}")
    @Column(name = "name", nullable = false)
    private String name;

    // Relación uno a muchos con la entidad `Location`. Un supermercado puede tener muchas ubicaciones.
    @OneToMany(mappedBy = "supermarket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Location> locations;

    // ============================================================
    // Constructores adicionales
    // ============================================================

    /**
     * Constructor que excluye el campo {@code id}.
     * Se utiliza para crear instancias antes de insertar el supermercado en la base de datos.
     *
     * @param name Nombre del supermercado.
     */
    public Supermarket(String name) {
        this.name = name;
    }
}
