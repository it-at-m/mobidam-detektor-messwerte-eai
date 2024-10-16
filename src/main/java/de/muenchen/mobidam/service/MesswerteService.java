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
import de.muenchen.mobidam.domain.mapper.MesswerteMapper;
import de.muenchen.mobidam.domain.MqMesswerte;
import de.muenchen.mobidam.domain.MqMesswerteDto;
import de.muenchen.mobidam.domain.Tagestyp;
import de.muenchen.mobidam.repository.MqMesswerteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MesswerteService {

    private final MqMesswerteRepository mqMesswerteRepository;

    private final MesswerteMapper messwerteMapper;

    public MqMesswerteDto loadMesswerteByYear(final Integer year) {
        List<MqMesswerte> messwerte = mqMesswerteRepository.findByDatumVon(
                LocalDateTime.of(year, 1, 1, 0, 0, 0));
        return messwerteMapper.map(messwerte);
    }

    public MqMesswerteDto loadMesswerteWithinTimeRange(
            final List<String> messquerschnitte,
            final LocalDate datumVon,
            final LocalDate datumBis,
            final LocalTime uhrzeitVon,
            final LocalTime uhrzeitBis,
            final List<Tagestyp> tagestypen,
            final Optional<List<FzTyp>> fzTypen,
            final PageRequest pageRequest) {
        final Page<MqMesswerte> messwerte;
        if (tagestypen.isEmpty()) {
            messwerte = mqMesswerteRepository.findByMqIdsAndDatumAndUhrzeit(
                    messquerschnitte,
                    datumVon.atStartOfDay(),
                    datumBis.atStartOfDay(),
                    uhrzeitVon,
                    uhrzeitBis,
                    pageRequest);
        } else {
            final var tagesTypIds = tagestypen.stream().map(Tagestyp::getId).toList();
            messwerte = mqMesswerteRepository.findByMqIdsAndDatumAndUhrzeitAndTagestypen(
                    messquerschnitte,
                    datumVon.atStartOfDay(),
                    datumBis.atStartOfDay(),
                    uhrzeitVon,
                    uhrzeitBis,
                    tagesTypIds,
                    pageRequest);
        }
        if (pageRequest.getPageNumber() >= messwerte.getTotalPages()) {
            //throw new Exception();
        }
        final var fzTypes = fzTypen.orElseGet(() -> Arrays.asList(FzTyp.values()));
        final var messwerteResponse = messwerteMapper.map(messwerte.getContent(), fzTypes);
        messwerteResponse.setPage(pageRequest.getPageNumber());
        messwerteResponse.setSize(pageRequest.getPageSize());
        return messwerteResponse;
    }

    public MqMesswerteDto loadMesswerteWithFullRange(
            final List<String> messquerschnitte,
            final LocalDateTime datumVon,
            final LocalDateTime datumBis,
            final List<Tagestyp> tagestypen,
            final Optional<List<FzTyp>> fzTypen,
            final PageRequest pageRequest) {
        final Page<MqMesswerte> messwerte;
        if (tagestypen.isEmpty()) {
            messwerte = mqMesswerteRepository.findByMqIdsAndDatum(
                    messquerschnitte,
                    datumVon,
                    datumBis,
                    pageRequest);
        } else {
            final var tagesTypIds = tagestypen.stream().map(Tagestyp::getId).toList();
            messwerte = mqMesswerteRepository.findByMqIdsAndDatumAndTagestypen(
                    messquerschnitte,
                    datumVon,
                    datumBis,
                    tagesTypIds,
                    pageRequest);
        }
        if (pageRequest.getPageNumber() >= messwerte.getTotalPages()) {
            //throw new Exception();
        }
        final var fzTypes = fzTypen.orElseGet(() -> Arrays.asList(FzTyp.values()));
        final var messwerteResponse = messwerteMapper.map(messwerte.getContent(), fzTypes);
        messwerteResponse.setPage(pageRequest.getPageNumber());
        messwerteResponse.setSize(pageRequest.getPageSize());
        return messwerteResponse;
    }

}
