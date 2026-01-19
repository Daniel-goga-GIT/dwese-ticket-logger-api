package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase {@code Location} representa una entidad que modela una ubicacion.
 *
 * Contiene cinco campos: {@code id}, {@code address}, {@code city}, {@code supermarket} y {@code province}, donde:
 * - {@code id} es el identificador unico de la ubicacion.
 * - {@code address} es la direccion.
 * - {@code city} es la ciudad.
 * - {@code supermarket} es la referencia al supermercado al que pertenece la ubicacion.
 * - {@code province} es la provincia a la que pertenece la ubicacion.
 */
@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    // ============================================================
    // Campos de la entidad
    // ============================================================

    /** Identificador unico de la ubicacion (clave primaria autogenerada). */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Direccion de la ubicacion. No puede estar vacia. */
    @NotEmpty(message = "{msg.location.address.notEmpty}")
    @Column(name = "address", nullable = false)
    private String address;

    /** Ciudad de la ubicacion. No puede estar vacia. */
    @NotEmpty(message = "{msg.location.city.notEmpty}")
    @Column(name = "city", nullable = false)
    private String city;

    /** Relacion con el supermercado al que pertenece la ubicacion. No puede ser nulo. */
    @NotNull(message = "{msg.location.supermarket.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supermarket_id", nullable = false)
    private Supermarket supermarket;

    /** Relacion con la provincia a la que pertenece la ubicacion. No puede ser nulo. */
    @NotNull(message = "{msg.location.province.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    // ============================================================
    // Constructores adicionales
    // ============================================================

    /**
     * Constructor que excluye el campo {@code id}.
     * Se utiliza para crear instancias antes de insertar la ubicacion en la base de datos.
     *
     * @param address     Direccion de la ubicacion.
     * @param city        Ciudad de la ubicacion.
     * @param supermarket Supermercado al que pertenece la ubicacion.
     * @param province    Provincia a la que pertenece la ubicacion.
     */
    public Location(String address, String city, Supermarket supermarket, Province province) {
        this.address = address;
        this.city = city;
        this.supermarket = supermarket;
        this.province = province;
    }
}
