package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.dtos.RegionCreateDTO;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.mapper.RegionMapper;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.repositories.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
public class RegionService {

    private static final Logger logger = LoggerFactory.getLogger(RegionService.class);

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private MessageSource messageSource;

    /**
     * Obtiene todas las regiones de la base de datos y las convierte a DTOs.
     *
     * @return Lista de objetos 'RegionDTO' representando todas las regiones.
     */
    public List<RegionDTO> getAllRegions() {
        try {
            logger.info("Obteniendo todas las regiones...");
            List<Region> regions = regionRepository.findAll();
            logger.info("Se encontraron {} regiones.", regions.size());
            return regions.stream()
                    .map(regionMapper::toDTO)
                    .toList();
        } catch (Exception e) {
            logger.error("Error al obtener todas las regiones: {}", e.getMessage());
            throw new RuntimeException("Error al obtener todas las regiones.", e);
        }
    }

    /**
     * Obtiene una región específica por su ID.
     *
     * @param id Identificador de la región.
     * @return Optional que contiene la región si existe.
     */
    public Optional<RegionDTO> getRegionById(Long id) {
        try {
            logger.info("Obteniendo región con ID {}", id);
            Optional<Region> region = regionRepository.findById(id);
            return region.map(regionMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al obtener la región con ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al obtener la región.", e);
        }
    }

    /**
     * Crea una nueva región en la base de datos.
     *
     * @param regionCreateDTO DTO que contiene los datos de la región a crear.
     * @param locale Idioma para los mensajes de error.
     * @return DTO de la región creada.
     * @throws IllegalArgumentException Si el código ya existe.
     */
    public RegionDTO createRegion(RegionCreateDTO regionCreateDTO, Locale locale) {
        if (regionRepository.existsRegionByCode(regionCreateDTO.getCode())) {
            String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
            throw new IllegalArgumentException(errorMessage);
        }

        //Se convierte a Entity para almacenar en la base de datos
        Region region = regionMapper.toEntity(regionCreateDTO);
        Region savedRegion = regionRepository.save(region);
        //Se devuelve el DTO
        return regionMapper.toDTO(savedRegion);
    }

    /**
     * Actualiza una región existente en la base de datos.
     *
     * @param id Identificador de la región a actualizar.
     * @param regionCreateDTO DTO que contiene los nuevos datos de la región.
     * @param locale Idioma para los mensajes de error.
     * @return DTO de la región actualizada.
     * @throws IllegalArgumentException Si la región no existe o el código ya está en uso.
     */
    public RegionDTO updateRegion(Long id, RegionCreateDTO regionCreateDTO, Locale locale) {
        Region existingRegion = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La región no existe."));

        if (regionRepository.existsRegionByCodeAndNotId(regionCreateDTO.getCode(), id)) {
            String errorMessage = messageSource.getMessage("msg.region-controller.update.codeExist", null, locale);
            throw new IllegalArgumentException(errorMessage);
        }

        existingRegion.setCode(regionCreateDTO.getCode());
        existingRegion.setName(regionCreateDTO.getName());
        Region updatedRegion = regionRepository.save(existingRegion);

        return regionMapper.toDTO(updatedRegion);
    }

    /**
     * Elimina una región específica por su ID.
     *
     * @param id Identificador único de la región.
     * @throws IllegalArgumentException Si la región no existe.
     */
    public void deleteRegion(Long id) {
        if (!regionRepository.existsById(id)) {
            throw new IllegalArgumentException("La región no existe");
        }
        regionRepository.deleteById(id);
    }
}