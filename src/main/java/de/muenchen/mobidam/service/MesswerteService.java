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

import de.muenchen.mobidam.domain.Constants;
import de.muenchen.mobidam.domain.FzTyp;
import de.muenchen.mobidam.domain.mapper.Mapper;
import de.muenchen.mobidam.domain.MqMesswerte;
import de.muenchen.mobidam.domain.MqMesswerteDTO;
import de.muenchen.mobidam.domain.Tagestyp;
import de.muenchen.mobidam.repository.MqMesswerteRepository;
import lombok.RequiredArgsConstructor;
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

    private final MqMesswerteRepository repo;
    private final Mapper mapper = new Mapper();

    public MqMesswerteDTO loadMesswerteByYear(final Integer year) {
        List<MqMesswerte> messwerte = repo.findByDatumVon(LocalDateTime.of(year, 1, 1, 0, 0, 0));
        return mapper.map(messwerte);
    }

    public MqMesswerteDTO loadMesswerteWithinTimeRange(
            final List<String> messquerschnitte,
            final LocalDate datumVon,
            final LocalDate datumBis,
            final String uhrzeitVon,
            final String uhrzeitBis,
            final List<Tagestyp> tagestypen,
            final Optional<List<FzTyp>> fzTypen) {
        List<MqMesswerte> messwerte;
        if (tagestypen.isEmpty())
            messwerte = repo.findByIdAndDatumAndUhrzeit(messquerschnitte.get(0), datumVon.atStartOfDay(), datumBis.atStartOfDay(),
                    LocalTime.parse(uhrzeitVon, Constants.TIME_FORMATTER), LocalTime.parse(uhrzeitBis, Constants.TIME_FORMATTER));
        else {
            List<Integer> tagestypenInt = tagestypen.stream().map(Tagestyp::getId).toList();
            messwerte = repo.findByIdAndDatumAndUhrzeitAndTagestypen(messquerschnitte.get(0), datumVon.atStartOfDay(), datumBis.atStartOfDay(),
                    LocalTime.parse(uhrzeitVon, Constants.TIME_FORMATTER), LocalTime.parse(uhrzeitBis, Constants.TIME_FORMATTER), tagestypenInt);
        }
        //            if (page > resultPage.getTotalPages()) { TODO
        //                throw new MyResourceNotFoundException();
        //            }
        List<FzTyp> fzTypenList = fzTypen.orElseGet(() -> Arrays.asList(FzTyp.values()));

        return mapper.map(messwerte, fzTypenList);
    }

    public MqMesswerteDTO loadMesswerteWithFullRange(
            final List<String> messquerschnitte,
            final LocalDateTime datumVon,
            final LocalDateTime datumBis,
            final List<Tagestyp> tagestypen,
            final Optional<List<FzTyp>> fzTypen) {
        List<MqMesswerte> messwerte;
        if (tagestypen.isEmpty())
            messwerte = repo.findByIdAndDatum(messquerschnitte.get(0), datumVon, datumBis);
        else {
            List<Integer> tagestypenInt = tagestypen.stream().map(Tagestyp::getId).toList();
            messwerte = repo.findByIdAndDatumAndTagestypen(messquerschnitte.get(0), datumVon, datumBis, tagestypenInt);
        }
        //                    if (page > resultPage.getTotalPages()) { TODO
        //                        throw new MyResourceNotFoundException();
        //                    }
        List<FzTyp> fzTypenList = fzTypen.orElseGet(() -> Arrays.asList(FzTyp.values()));

        return mapper.map(messwerte, fzTypenList);
    }

}
