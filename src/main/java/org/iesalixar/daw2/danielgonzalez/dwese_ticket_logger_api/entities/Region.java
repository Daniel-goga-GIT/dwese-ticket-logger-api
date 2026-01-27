package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor // Lombok genera el constructor vacío necesario para JPA
@Table(name = "regions") // Recomendado para definir el nombre de la tabla explícitamente
public class Region {

    // Campo que almacena el identificador único de la región.
    // Es una clave primaria autogenerada por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Campo que almacena el código de la región, normalmente una cadena corta que identifica la región.
    // Ejemplo: "01" para Andalucía.
    @Column(name = "code", nullable = false, length = 2) // Define la columna correspondiente en la tabla
    private String code;

    // Campo que almacena el nombre completo de la región, como "Andalucía" o "Cataluña".
    @Column(name = "name", nullable = false, length = 100) // Define la columna correspondiente en la tabla
    private String name;

    // Campo que almacena la ruta de la imagen de la región.
    // Ajustado para coincidir con la columna 'image' definida en schema.sql
    @Column(name = "image")
    private String imagePath;
    
    // Relación uno a muchos con la entidad Province. Una región puede tener muchas provincias.
    @JsonIgnore // Evita ciclo infinito en serialización JSON
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Province> provinces;
    
    /**
     * Este es un constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Region` cuando no es necesario o no se conoce el `id`
     * (por ejemplo, antes de insertar la región en la base de datos, donde el `id` es autogenerado).
     * @param code Código de la región.
     * @param name Nombre de la región.
     */
    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
