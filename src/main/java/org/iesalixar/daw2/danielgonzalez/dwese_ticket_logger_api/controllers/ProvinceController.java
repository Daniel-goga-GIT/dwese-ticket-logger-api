package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities.Province;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.repositories.ProvinceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

/**
 * Controlador para gestionar las operaciones CRUD de las provincias.
 */
@RestController
@RequestMapping("/api/provinces")
public class ProvinceController {

    private static final Logger logger = LoggerFactory.getLogger(ProvinceController.class);

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todas las provincias almacenadas en la base de datos.
     *
     * @return ResponseEntity con la lista de provincias o un error en caso de fallo.
     */
    @GetMapping
    public ResponseEntity<List<Province>> getAllProvinces() {
        logger.info("Solicitando la lista de todas las provincias...");
        try {
            List<Province> provinces = provinceRepository.findAll();
            logger.info("Se han encontrado {} provincias.", provinces.size());
            return ResponseEntity.ok(provinces);
        } catch (Exception e) {
            logger.error("Error al listar las provincias: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtiene una provincia específica por su ID.
     *
     * @param id ID de la provincia solicitada.
     * @return ResponseEntity con la provincia encontrada o un mensaje de error si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Province> getProvinceById(@PathVariable Long id) {
        logger.info("Buscando provincia con ID {}", id);
        try {
            Optional<Province> province = provinceRepository.findById(id);
            if (province.isPresent()) {
                logger.info("Provincia con ID {} encontrada: {}", id, province.get());
                return ResponseEntity.ok(province.get());
            } else {
                logger.warn("No se encontró ninguna provincia con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            logger.error("Error al buscar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Crea una nueva provincia en la base de datos.
     *
     * @param province Objeto JSON que representa la nueva provincia.
     * @param locale Idioma de los mensajes de error.
     * @return ResponseEntity con la provincia creada o un mensaje de error.
     */
    @PostMapping
    public ResponseEntity<?> createProvince(@Valid @RequestBody Province province, Locale locale) {
        logger.info("Insertando nueva provincia con código {}", province.getCode());
        try {
            // Guardar la nueva provincia
            Province savedProvince = provinceRepository.save(province);
            logger.info("Provincia creada exitosamente con ID {}", savedProvince.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProvince);
        } catch (Exception e) {
            logger.error("Error al crear la provincia: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la provincia.");
        }
    }

    /**
     * Actualiza una provincia existente por su ID.
     *
     * @param id        ID de la provincia a actualizar.
     * @param province Objeto JSON con los nuevos datos.
     * @param locale    Idioma de los mensajes de error.
     * @return ResponseEntity con la provincia actualizada o un mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProvince(@PathVariable Long id, @Valid @RequestBody Province province, Locale locale) {
        logger.info("Actualizando provincia con ID {}", id);
        try {
            // Verificar si la provincia existe
            Optional<Province> existingProvince = provinceRepository.findById(id);
            if (!existingProvince.isPresent()) {
                logger.warn("No se encontró ninguna provincia con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La provincia no existe.");
            }
            // Actualizar la provincia
            province.setId(id); // Asegurarse de que el ID no cambie
            Province updatedProvince = provinceRepository.save(province);
            logger.info("Provincia con ID {} actualizada exitosamente.", id);
            return ResponseEntity.ok(updatedProvince);
        } catch (Exception e) {
            logger.error("Error al actualizar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la provincia.");
        }
    }

    /**
     * Elimina una provincia específica por su ID.
     *
     * @param id ID de la provincia a eliminar.
     * @return ResponseEntity indicando el resultado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProvince(@PathVariable Long id) {
        logger.info("Eliminando provincia con ID {}", id);
        try {
            // Verificar si la provincia existe
            if (!provinceRepository.existsById(id)) {
                logger.warn("No se encontró ninguna provincia con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La provincia no existe.");
            }
            // Eliminar la provincia
            provinceRepository.deleteById(id);
            logger.info("Provincia con ID {} eliminada exitosamente.", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error al eliminar la provincia con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la provincia.");
        }
    }
}
