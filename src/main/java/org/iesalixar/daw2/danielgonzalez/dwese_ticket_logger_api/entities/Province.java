package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * La clase {@code Province} representa una entidad que modela una provincia dentro de la base de datos.
 *
 * Contiene cuatro campos: {@code id}, {@code code}, {@code name} y {@code region}, donde:
 * - {@code id} es el identificador único de la provincia.
 * - {@code code} es un código asociado a la provincia.
 * - {@code name} es el nombre de la provincia.
 * - {@code region} es la relación con la entidad {@link Region}, que representa la comunidad autónoma.
 *
 * Las anotaciones de Lombok reducen el código repetitivo generando automáticamente
 * métodos comunes como getters, setters y constructores.
 */
@Entity
@Table(name = "provinces")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Province {

    // ============================================================
    // Campos de la entidad
    // ============================================================

    /** Identificador único de la provincia (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Código de la provincia, normalmente una cadena corta que la identifica.
     * Ejemplo: "23" para Jaén.
     */
    @NotEmpty(message = "{msg.province.code.notEmpty}")
    @Size(max = 2, message = "{msg.province.code.size}")
    @Column(name = "code", nullable = false, length = 2)
    private String code;

    /**
     * Nombre completo de la provincia, como "Sevilla" o "Jaén".
     */
    @NotEmpty(message = "{msg.province.name.notEmpty}")
    @Size(max = 100, message = "{msg.province.name.size}")
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * Relación con la entidad {@link Region}, representando la comunidad autónoma
     * a la que pertenece la provincia.
     */
    @NotNull(message = "{msg.province.region.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id", nullable = false)
    private Region region;

    // Relación uno a muchos con la entidad `Location`. Una provincia puede tener muchas ubicaciones.
    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Location> locations;

    // ============================================================
    // Constructores adicionales
    // ============================================================

    /**
     * Constructor que excluye el campo {@code id}.
     * Se utiliza para crear instancias antes de insertar la provincia en la base de datos.
     *
     * @param code   Código de la provincia.
     * @param name   Nombre de la provincia.
     */
    public Province(String code, String name) {
        this.code = code;
        this.name = name;
    }

    /**
     * Constructor que excluye el campo {@code id}, incluyendo la región obligatoria.
     */
    public Province(String code, String name, Region region) {
        this.code = code;
        this.name = name;
        this.region = region;
    }
}
