/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
 */
package de.muenchen.test.repository;

import de.muenchen.test.domain.MqMesswerte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Provides a Repository for {@link MqMesswerte}. This Repository is exported as a REST resource.
 * <p>
 * The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
 * For specific Documentation on how the generated REST point behaves, please consider the Spring
 * Data Rest Reference
 * <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
 * </p>
 */
@Repository
public interface MqMesswerteRepository extends JpaRepository<MqMesswerte, Long> { //NOSONAR

    @Query("SELECT m FROM MqMesswerte m WHERE m.mqId = 400001 AND m.datumUhrzeitVon >= :datumUhrzeitVon")
    List<MqMesswerte> findByDatumVon(@Param("datumUhrzeitVon") LocalDateTime datumUhrzeitVon);


}
