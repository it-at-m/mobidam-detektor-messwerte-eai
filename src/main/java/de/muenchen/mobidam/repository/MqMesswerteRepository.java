/*
 * The MIT License
 * Copyright © 2023 Landeshauptstadt München | it@M
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package de.muenchen.mobidam.repository;

import de.muenchen.mobidam.domain.MqMesswerte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Provides a Repository for {@link MqMesswerte}. This Repository is exported as a REST resource.
 * <p>
 * The Repository handles CRUD Operations. Every Operation is secured and takes care of the tenancy.
 * For specific Documentation on how the generated REST point
 * behaves, please consider the Spring Data Rest Reference
 * <a href="http://docs.spring.io/spring-data/rest/docs/current/reference/html/">here</a>.
 * </p>
 */
@Repository
public interface MqMesswerteRepository extends JpaRepository<MqMesswerte, Long> { //NOSONAR

    @Query("SELECT m FROM MqMesswerte m WHERE m.mqId = 400001 AND m.datumUhrzeitVon >= :datumUhrzeitVon")
    List<MqMesswerte> findByDatumVon(@Param("datumUhrzeitVon") LocalDateTime datumUhrzeitVon);

    @Query("SELECT m FROM MqMesswerte m WHERE m.mqId = :mqId AND m.datumUhrzeitVon >= :datumUhrzeitVon AND m.datumUhrzeitVon <= :datumUhrzeitBis")
    List<MqMesswerte> findByIdAndDatum(@Param("mqId") String mqId, @Param("datumUhrzeitVon") LocalDateTime datumUhrzeitVon,
            @Param("datumUhrzeitBis") LocalDateTime datumUhrzeitBis);

    @Query(
        "SELECT m FROM MqMesswerte m WHERE m.mqId = :mqId AND m.datumUhrzeitVon >= :datumUhrzeitVon AND m.datumUhrzeitVon <= :datumUhrzeitBis AND m.tagetyp IN :tagestypen"
    )
    List<MqMesswerte> findByIdAndDatumAndTagestypen(@Param("mqId") String mqId, @Param("datumUhrzeitVon") LocalDateTime datumUhrzeitVon,
            @Param("datumUhrzeitBis") LocalDateTime datumUhrzeitBis, @Param("tagestypen") List<Integer> tagestypen);

    @Query(
        "SELECT m FROM MqMesswerte m WHERE m.mqId = :mqId AND m.datumUhrzeitVon BETWEEN :datumVon AND :datumBis AND FORMATDATETIME(m.datumUhrzeitVon, 'HH:mm:ss') >= :uhrzeitVon AND FORMATDATETIME(m.datumUhrzeitVon, 'HH:mm:ss') <= :uhrzeitBis"
    )
    List<MqMesswerte> findByIdAndDatumAndUhrzeit(@Param("mqId") String mqId, @Param("datumVon") LocalDateTime datumVon,
            @Param("datumBis") LocalDateTime datumBis, @Param("uhrzeitVon") LocalTime uhrzeitVon, @Param("uhrzeitBis") LocalTime uhrzeitBis);

    @Query(
        "SELECT m FROM MqMesswerte m WHERE m.mqId = :mqId AND m.datumUhrzeitVon BETWEEN :datumVon AND :datumBis AND FORMATDATETIME(m.datumUhrzeitVon, 'HH:mm:ss') >= :uhrzeitVon AND FORMATDATETIME(m.datumUhrzeitVon, 'HH:mm:ss') <= :uhrzeitBis AND m.tagetyp IN :tagestypen"
    )
    List<MqMesswerte> findByIdAndDatumAndUhrzeitAndTagestypen(@Param("mqId") String mqId, @Param("datumVon") LocalDateTime datumVon,
            @Param("datumBis") LocalDateTime datumBis, @Param("uhrzeitVon") LocalTime uhrzeitVon, @Param("uhrzeitBis") LocalTime uhrzeitBis,
            @Param("tagestypen") List<Integer> tagestypen);
}
