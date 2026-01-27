package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.services;

import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.dtos.RegionCreateDTO;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.dtos.RegionDTO;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities.Region;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.mapper.RegionMapper;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.repositories.RegionRepository;
import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.services.FileStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import jakarta.validation.Valid;
import java.util.Locale;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Service
public class RegionService {

    private static final Logger logger = LoggerFactory.getLogger(RegionService.class);

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private RegionMapper regionMapper;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private FileStorageService fileStorageService;

    /**
     * Obtiene todas las regiones con paginación y las convierte en una página de RegionDTO.
     *
     * @param pageable Objeto de paginación que define la página, el tamaño y la ordenación.
     * @return Página de RegionDTO.
     */
    public Page<RegionDTO> getAllRegions(Pageable pageable) {
        logger.info("Solicitando todas las regiones con paginación: página {}, tamaño {}",
                pageable.getPageNumber(), pageable.getPageSize());
        try {
            Page<Region> regions = regionRepository.findAll(pageable);
            logger.info("Se han encontrado {} regiones en la página actual.", regions.getNumberOfElements());
            return regions.map(regionMapper::toDTO);
        } catch (Exception e) {
            logger.error("Error al obtener la lista paginada de regiones: {}", e.getMessage());
            throw e;
        }
    }

    /**
     * Obtiene una región específica por su ID.
     *
     * @param id Identificador de la región.
     * @return Optional que contiene la región si existe.
     */
    public Optional<RegionDTO> getRegionById(Long id) {
        Optional<Region> region = regionRepository.findById(id);
        return region.map(regionMapper::toDTO);
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
        logger.info("Creando una nueva región con código {}", regionCreateDTO.getCode());

        // Verificar si ya existe una región con el mismo código
        if (regionRepository.existsRegionByCode(regionCreateDTO.getCode())) {
            String errorMessage = messageSource.getMessage("msg.region-controller.insert.codeExist", null, locale);
            throw new IllegalArgumentException(errorMessage);
        }

        // Procesar la imagen si se proporciona
        String fileName = null;
        if (regionCreateDTO.getImageFile() != null && !regionCreateDTO.getImageFile().isEmpty()) {
            fileName = fileStorageService.saveFile(regionCreateDTO.getImageFile());
            if (fileName == null) {
                throw new RuntimeException("Error al guardar la imagen.");
            }
        }

        // Crear la entidad Region
        Region region = regionMapper.toEntity(regionCreateDTO);
        region.setImagePath(fileName);

        // Guardar la nueva región
        Region savedRegion = regionRepository.save(region);
        logger.info("Región creada exitosamente con ID {}", savedRegion.getId());

        // Convertir la entidad guardada a DTO y devolverla
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
        logger.info("Actualizando región con ID {}", id);

        // Buscar la región existente
        Region existingRegion = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La región no existe."));

        // Verificar si el código ya está en uso por otra región
        if (regionRepository.existsRegionByCodeAndNotId(regionCreateDTO.getCode(), id)) {
            String errorMessage = messageSource.getMessage("msg.region-controller.update.codeExist", null, locale);
            throw new IllegalArgumentException(errorMessage);
        }

        // Procesar la imagen si se proporciona
        String fileName = existingRegion.getImagePath(); // Conservar la imagen existente por defecto
        if (regionCreateDTO.getImageFile() != null && !regionCreateDTO.getImageFile().isEmpty()) {
            fileName = fileStorageService.saveFile(regionCreateDTO.getImageFile());
            if (fileName == null) {
                throw new RuntimeException("Error al guardar la nueva imagen.");
            }
        }

        // Actualizar los datos de la región
        existingRegion.setCode(regionCreateDTO.getCode());
        existingRegion.setName(regionCreateDTO.getName());
        existingRegion.setImagePath(fileName);

        // Guardar los cambios
        Region updatedRegion = regionRepository.save(existingRegion);
        logger.info("Región con ID {} actualizada exitosamente.", updatedRegion.getId());

        // Convertir la entidad actualizada a DTO y devolverla
        return regionMapper.toDTO(updatedRegion);
    }

    /**
     * Elimina una región específica por su ID.
     *
     * @param id Identificador único de la región.
     * @throws IllegalArgumentException Si la región no existe.
     */
    public void deleteRegion(Long id) {
        logger.info("Buscando región con ID {}", id);

        // Buscar la región
        Region region = regionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("La región no existe."));

        // Eliminar la imagen asociada si existe
        if (region.getImagePath() != null && !region.getImagePath().isEmpty()) {
            fileStorageService.deleteFile(region.getImagePath());
            logger.info("Imagen asociada a la región con ID {} eliminada.", id);
        }

        // Eliminar la región
        regionRepository.deleteById(id);
        logger.info("Región con ID {} eliminada exitosamente.", id);
    }
}