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
package de.muenchen.mobidam.service;

import de.muenchen.mobidam.domain.FzTyp;
import de.muenchen.mobidam.domain.MqMesswerte;
import de.muenchen.mobidam.domain.MqMesswerteDto;
import de.muenchen.mobidam.domain.Tagestyp;
import de.muenchen.mobidam.domain.mapper.MesswerteMapper;
import de.muenchen.mobidam.exceptions.PageNumberExceedsTotalPages;
import de.muenchen.mobidam.repository.MqMesswerteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MesswerteService {

    private final MqMesswerteRepository mqMesswerteRepository;

    private final MesswerteMapper messwerteMapper;

    /**
     *
     * @param messquerschnitte die zu extrahierenden Messquerschnitte.
     * @param datumVon als Startdatum zur Extraktion der Messwerte.
     * @param datumBis als Enddatum zur Extraktion der Messwerte.
     * @param uhrzeitVon als Startuhrzeit zur Extraktion der Messwerte eines Messtags.
     * @param uhrzeitBis als Startuhrzeit zur Extraktion der Messwerte eines Messtags.
     * @param tagestypen die zu extrahierenden Tage.
     * @param fzTypen die Auswahl der zu extrahierenden Fahrzeugtypen. Sind keine Fahrzeugtypen gegeben,
     *            werden alle Fahrzeugtypen zurückgegeben.
     * @param pageRequest für den Seitenabruf.
     * @return die Messwerte mit den Informationen zur Paginierung.
     * @throws PageNumberExceedsTotalPages falls die Anzahl gewünschte Seite die Anzahl der verfügbaren
     *             Seiten übersteigt.
     */
    @Transactional(readOnly = true)
    public MqMesswerteDto loadMesswerteWithinTimeRange(
            final List<Long> messquerschnitte,
            final LocalDate datumVon,
            final LocalDate datumBis,
            final LocalTime uhrzeitVon,
            final LocalTime uhrzeitBis,
            final List<Tagestyp> tagestypen,
            final Optional<List<FzTyp>> fzTypen,
            final PageRequest pageRequest) throws PageNumberExceedsTotalPages {
        final var datumVonStartOfDay = LocalDateTime.of(datumVon, LocalTime.MIN);
        final var datumBisEndOfDay = LocalDateTime.of(datumBis, LocalTime.MAX);
        final Page<MqMesswerte> messwerte;

        if (tagestypen.isEmpty()) {
            messwerte = mqMesswerteRepository.findByMqIdsAndDatumAndUhrzeit(
                    messquerschnitte,
                    datumVonStartOfDay,
                    datumBisEndOfDay,
                    uhrzeitVon,
                    uhrzeitBis,
                    pageRequest);
        } else {
            final var tagesTypIds = tagestypen.stream().map(Tagestyp::getId).toList();
            messwerte = mqMesswerteRepository.findByMqIdsAndDatumAndUhrzeitAndTagestypen(
                    messquerschnitte,
                    datumVonStartOfDay,
                    datumBisEndOfDay,
                    uhrzeitVon,
                    uhrzeitBis,
                    tagesTypIds,
                    pageRequest);
        }
        this.throwExceptionWhenPageNumberExceedsTotalPages(pageRequest, messwerte);
        final var fzTypes = fzTypen.orElseGet(() -> Arrays.asList(FzTyp.values()));
        return messwerteMapper.map(messwerte, fzTypes);
    }

    /**
     *
     * @param messquerschnitte die zu extrahierenden Messquerschnitte.
     * @param datumVon als Startdatum zur Extraktion der Messwerte.
     * @param datumBis als Enddatum zur Extraktion der Messwerte.
     * @param tagestypen die zu extrahierenden Tage.
     * @param fzTypen die Auswahl der zu extrahierenden Fahrzeugtypen. Sind keine Fahrzeugtypen gegeben,
     *            werden alle Fahrzeugtypen zurückgegeben.
     * @param pageRequest für den Seitenabruf.
     * @return die Messwerte mit den Informationen zur Paginierung.
     * @throws PageNumberExceedsTotalPages falls die Anzahl gewünschte Seite die Anzahl der verfügbaren
     *             Seiten übersteigt.
     */
    @Transactional(readOnly = true)
    public MqMesswerteDto loadMesswerteWithFullRange(
            final List<Long> messquerschnitte,
            final LocalDate datumVon,
            final LocalDate datumBis,
            final List<Tagestyp> tagestypen,
            final Optional<List<FzTyp>> fzTypen,
            final PageRequest pageRequest) throws PageNumberExceedsTotalPages {
        final var datumVonStartOfDay = LocalDateTime.of(datumVon, LocalTime.MIN);
        final var datumBisEndOfDay = LocalDateTime.of(datumBis, LocalTime.MAX);
        final Page<MqMesswerte> messwerte;

        if (tagestypen.isEmpty()) {
            messwerte = mqMesswerteRepository.findByMqIdsAndDatum(
                    messquerschnitte,
                    datumVonStartOfDay,
                    datumBisEndOfDay,
                    pageRequest);
        } else {
            final var tagesTypIds = tagestypen.stream().map(Tagestyp::getId).toList();
            messwerte = mqMesswerteRepository.findByMqIdsAndDatumAndTagestypen(
                    messquerschnitte,
                    datumVonStartOfDay,
                    datumBisEndOfDay,
                    tagesTypIds,
                    pageRequest);
        }
        this.throwExceptionWhenPageNumberExceedsTotalPages(pageRequest, messwerte);
        final var fzTypes = fzTypen.orElseGet(() -> Arrays.asList(FzTyp.values()));
        return messwerteMapper.map(messwerte, fzTypes);
    }

    /**
     * @param pageRequest
     * @param pageResponse
     * @throws PageNumberExceedsTotalPages falls die angefragte Seitenummer die zur Verfügung stehende
     *             Seitezahl überschreitet.
     */
    protected void throwExceptionWhenPageNumberExceedsTotalPages(final PageRequest pageRequest, final Page pageResponse) throws PageNumberExceedsTotalPages {
        if (pageRequest.getPageNumber() >= pageResponse.getTotalPages()
                && ((pageResponse.getTotalPages() != 0 && pageRequest.getPageNumber() != 0)
                        || (pageResponse.getTotalPages() == 0 && pageRequest.getPageNumber() != 0))) {
            final var errorMessage = "The requested zero based page number exceeds the total number of pages";
            log.error(errorMessage);
            throw new PageNumberExceedsTotalPages(errorMessage);
        }
    }

}
