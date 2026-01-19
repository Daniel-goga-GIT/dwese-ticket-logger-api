package org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.danielgonzalez.dwese_ticket_logger_api.entities.Province;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface ProvinceRepository extends JpaRepository<Province, Long> {

    @Query("SELECT COUNT(r) > 0 FROM Province p WHERE p.code = :code AND p.id != :id")
    boolean existsProvinceByCode(@Param("code") String code);
}
