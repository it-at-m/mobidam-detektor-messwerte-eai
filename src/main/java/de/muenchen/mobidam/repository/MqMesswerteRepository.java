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

@Repository
public interface MqMesswerteRepository extends JpaRepository<MqMesswerte, Long> { //NOSONAR

    @Query("SELECT m FROM MqMesswerte m WHERE m.mqId = 400001 AND m.datumUhrzeitVon >= :datumUhrzeitVon")
    List<MqMesswerte> findByDatumVon(@Param("datumUhrzeitVon") final LocalDateTime datumUhrzeitVon);

    @Query("SELECT m FROM MqMesswerte m WHERE m.mqId IN :mqIds AND m.datumUhrzeitVon >= :datumUhrzeitVon AND m.datumUhrzeitVon <= :datumUhrzeitBis")
    List<MqMesswerte> findByMqIdsAndDatum(
            @Param("mqIds") final List<String> mqIds,
            @Param("datumUhrzeitVon") final LocalDateTime datumUhrzeitVon,
            @Param("datumUhrzeitBis") final LocalDateTime datumUhrzeitBis
    );

    @Query(
        "SELECT m FROM MqMesswerte m WHERE m.mqId IN :mqIds AND m.datumUhrzeitVon >= :datumUhrzeitVon AND m.datumUhrzeitVon <= :datumUhrzeitBis AND m.tagetyp IN :tagestypen"
    )
    List<MqMesswerte> findByMqIdsAndDatumAndTagestypen(
            @Param("mqIds") final List<String> mqIds,
            @Param("datumUhrzeitVon") final LocalDateTime datumUhrzeitVon,
            @Param("datumUhrzeitBis") final LocalDateTime datumUhrzeitBis,
            @Param("tagestypen") final List<Integer> tagestypen
    );

    @Query(
        "SELECT m FROM MqMesswerte m WHERE m.mqId IN :mqIds AND m.datumUhrzeitVon BETWEEN :datumVon AND :datumBis AND FORMATDATETIME(m.datumUhrzeitVon, 'HH:mm:ss') >= :uhrzeitVon AND FORMATDATETIME(m.datumUhrzeitVon, 'HH:mm:ss') <= :uhrzeitBis"
    )
    List<MqMesswerte> findByMqIdsAndDatumAndUhrzeit(
            @Param("mqIds") final List<String> mqIds,
            @Param("datumVon") final LocalDateTime datumVon,
            @Param("datumBis") final LocalDateTime datumBis,
            @Param("uhrzeitVon") final LocalTime uhrzeitVon,
            @Param("uhrzeitBis") final LocalTime uhrzeitBis
    );

    @Query(
        "SELECT m FROM MqMesswerte m WHERE m.mqId IN :mqIds AND m.datumUhrzeitVon BETWEEN :datumVon AND :datumBis AND FORMATDATETIME(m.datumUhrzeitVon, 'HH:mm:ss') >= :uhrzeitVon AND FORMATDATETIME(m.datumUhrzeitVon, 'HH:mm:ss') <= :uhrzeitBis AND m.tagetyp IN :tagestypen"
    )
    List<MqMesswerte> findByMqIdsAndDatumAndUhrzeitAndTagestypen(
            @Param("mqIds") final List<String> mqIds,
            @Param("datumVon") final LocalDateTime datumVon,
            @Param("datumBis") final LocalDateTime datumBis,
            @Param("uhrzeitVon") final LocalTime uhrzeitVon,
            @Param("uhrzeitBis") final LocalTime uhrzeitBis,
            @Param("tagestypen") final List<Integer> tagestypen
    );
}
