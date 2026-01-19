package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.controllers;

import jakarta.validation.Valid;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities.*;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@RequestMapping("/api/tickets")
public class TicketProductController {

    private static final Logger logger =
            LoggerFactory.getLogger(TicketProductController.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private ProductRepository productRepository;

    //@Autowired
    //private LocationRepository locationRepository;

    //@Autowired
    //private SupermarketRepository supermarketRepository;

    @Autowired
    private ProvinceRepository provinceRepository;

    @Autowired
    private MessageSource messageSource;

    /**
     * Lista todos los tickets disponibles.
     *
     * @return ResponseEntity con la lista de tickets.
     */
    @GetMapping
    public ResponseEntity<List<Ticket>> getAllTickets() {
        logger.info("Solicitando la lista de todos los tickets...");
        try {
            List<Ticket> tickets = ticketRepository.findAll();
            logger.info("Se han encontrado {} tickets.", tickets.size());
            return ResponseEntity.ok(tickets);
        } catch (Exception e) {
            logger.error("Error al listar los tickets: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Obtiene un ticket específico por su ID.
     *
     * @param id ID del ticket solicitado.
     * @return ResponseEntity con el ticket encontrado o un mensaje de error.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable Long id) {
        logger.info("Buscando ticket con ID {}", id);
        try {
            Optional<Ticket> ticket = ticketRepository.findById(id);
            if (ticket.isPresent()) {
                logger.info("Ticket con ID {} encontrado.", id);
                return ResponseEntity.ok(ticket.get());
            } else {
                logger.warn("No se encontró ningún ticket con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            }
        } catch (Exception e) {
            logger.error("Error al buscar el ticket con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Crea un nuevo ticket.
     *
     * @param ticket Objeto JSON que representa el nuevo ticket.
     * @param locale Idioma de los mensajes de error.
     * @return ResponseEntity con el ticket creado o un mensaje de error.
     */
    @PostMapping
    public ResponseEntity<?> createTicket(@Valid @RequestBody Ticket ticket, Locale locale) {
        logger.info("Insertando nuevo ticket con fecha {}", ticket.getDate());
        try {
            Ticket savedTicket = ticketRepository.save(ticket);
            logger.info("Ticket creado exitosamente con ID {}", savedTicket.getId());
            return ResponseEntity.status(HttpStatus.CREATED).body(savedTicket);
        } catch (Exception e) {
            logger.error("Error al crear el ticket: {}", e.getMessage());
            String msg = messageSource.getMessage("msg.ticketcontroller.insert.error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }

    /**
     * Actualiza un ticket existente por su ID.
     *
     * @param id     ID del ticket a actualizar.
     * @param ticket Objeto JSON con los nuevos datos.
     * @param locale Idioma de los mensajes de error.
     * @return ResponseEntity con el ticket actualizado o un mensaje de error.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTicket(@PathVariable Long id, @Valid @RequestBody Ticket ticket, Locale locale) {
        logger.info("Actualizando ticket con ID {}", id);
        try {
            // Verificar si el ticket existe
            Optional<Ticket> existingTicket = ticketRepository.findById(id);
            if (!existingTicket.isPresent()) {
                logger.warn("No se encontró ningún ticket con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El ticket no existe.");
            }
            // Actualizar el ticket
            ticket.setId(id);
            Ticket updatedTicket = ticketRepository.save(ticket);
            logger.info("Ticket con ID {} actualizado exitosamente.", id);
            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            logger.error("Error al actualizar el ticket con ID {}: {}", id, e.getMessage());
            String msg = messageSource.getMessage("msg.ticketcontroller.update.error", null, locale);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(msg);
        }
    }

    /**
     * Elimina un ticket específico por su ID.
     *
     * @param id ID del ticket a eliminar.
     * @return ResponseEntity indicando el resultado de la operación.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTicket(@PathVariable Long id) {
        logger.info("Eliminando ticket con ID {}", id);
        try {
            // Verificar si el ticket existe
            if (!ticketRepository.existsById(id)) {
                logger.warn("No se encontró ningún ticket con ID {}", id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El ticket no existe.");
            }
            // Eliminar el ticket
            ticketRepository.deleteById(id);
            logger.info("Ticket con ID {} eliminado exitosamente.", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error al eliminar el ticket con ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el ticket.");
        }
    }

    /**
     * Busca productos por nombre.
     *
     * @param productSearch Término de búsqueda.
     * @param ticketId      ID del ticket.
     * @return ResponseEntity con los resultados de la búsqueda.
     */
    @GetMapping("/{ticketId}/products/search")
    public ResponseEntity<?> searchProducts(@RequestParam("q") String productSearch, @PathVariable Long ticketId) {
        logger.info("Buscando productos que coincidan con '{}'", productSearch);
        try {
            Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
            if (!ticketOpt.isPresent()) {
                logger.warn("No se encontró el ticket con ID {}", ticketId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el ticket.");
            }

            List<Product> searchResults = productRepository.findByNameContainingIgnoreCase(productSearch);
            logger.info("Se encontraron {} productos.", searchResults.size());
            return ResponseEntity.ok(searchResults);
        } catch (Exception e) {
            logger.error("Error al buscar productos: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al buscar productos.");
        }
    }

    /**
     * Añade un producto existente a un ticket.
     *
     * @param ticketId  ID del ticket.
     * @param productId ID del producto.
     * @param locale    Idioma de los mensajes de error.
     * @return ResponseEntity indicando el resultado de la operación.
     */
    @PostMapping("/{ticketId}/products/{productId}")
    public ResponseEntity<?> addProductToTicket(@PathVariable Long ticketId,
                                                @PathVariable Long productId,
                                                Locale locale) {
        logger.info("Añadiendo producto {} al ticket {}", productId, ticketId);
        try {
            Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
            Optional<Product> productOpt = productRepository.findById(productId);

            if (!ticketOpt.isPresent() || !productOpt.isPresent()) {
                logger.warn("Ticket o producto no encontrados.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket o producto no encontrados.");
            }

            Ticket ticket = ticketOpt.get();
            ticket.getProducts().add(productOpt.get());
            Ticket updatedTicket = ticketRepository.save(ticket);

            logger.info("Producto añadido exitosamente al ticket.");
            return ResponseEntity.ok(updatedTicket);
        } catch (DataIntegrityViolationException e) {
            logger.error("Violación de integridad: {}", e.getMessage());
            String msg = messageSource.getMessage("msg.ticketcontroller.insert.integrity-violation", null, locale);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(msg);
        } catch (Exception e) {
            logger.error("Error al añadir el producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al añadir el producto.");
        }
    }

    /**
     * Crea un nuevo producto y lo asocia a un ticket.
     *
     * @param ticketId    ID del ticket.
     * @param product     Objeto JSON con los datos del producto.
     * @return ResponseEntity con el ticket actualizado.
     */
    @PostMapping("/{ticketId}/products")
    public ResponseEntity<?> addNewProductToTicket(@PathVariable Long ticketId,
                                                   @Valid @RequestBody Product product) {
        logger.info("Añadiendo nuevo producto '{}' al ticket {}", product.getName(), ticketId);
        try {
            Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);

            if (!ticketOpt.isPresent()) {
                logger.warn("No se encontró el ticket con ID {}", ticketId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el ticket.");
            }

            Ticket ticket = ticketOpt.get();

            boolean exists = ticket.getProducts().stream()
                    .anyMatch(p -> p.getName().equalsIgnoreCase(product.getName()));

            if (exists) {
                logger.warn("El producto ya existe en el ticket.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El producto ya existe en el ticket.");
            }

            productRepository.save(product);
            ticket.getProducts().add(product);
            Ticket updatedTicket = ticketRepository.save(ticket);

            logger.info("Nuevo producto añadido exitosamente al ticket.");
            return ResponseEntity.status(HttpStatus.CREATED).body(updatedTicket);
        } catch (Exception e) {
            logger.error("Error al añadir nuevo producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al añadir el nuevo producto.");
        }
    }

    /**
     * Elimina un producto de un ticket.
     *
     * @param ticketId  ID del ticket.
     * @param productId ID del producto.
     * @return ResponseEntity indicando el resultado de la operación.
     */
    @DeleteMapping("/{ticketId}/products/{productId}")
    public ResponseEntity<?> removeProductFromTicket(@PathVariable Long ticketId,
                                                     @PathVariable Long productId) {
        logger.info("Eliminando producto {} del ticket {}", productId, ticketId);
        try {
            Optional<Ticket> ticketOpt = ticketRepository.findById(ticketId);
            Optional<Product> productOpt = productRepository.findById(productId);

            if (!ticketOpt.isPresent() || !productOpt.isPresent()) {
                logger.warn("Ticket o producto no encontrados.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket o producto no encontrados.");
            }

            Ticket ticket = ticketOpt.get();
            ticket.getProducts().remove(productOpt.get());
            Ticket updatedTicket = ticketRepository.save(ticket);

            logger.info("Producto eliminado exitosamente del ticket.");
            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            logger.error("Error al eliminar producto: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el producto.");
        }
    }
}
