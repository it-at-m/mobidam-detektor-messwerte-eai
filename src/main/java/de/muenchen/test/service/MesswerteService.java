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
package de.muenchen.test.service;

import de.muenchen.test.domain.Constants;
import de.muenchen.test.domain.FzTyp;
import de.muenchen.test.domain.Mapper;
import de.muenchen.test.domain.MqMesswerte;
import de.muenchen.test.domain.MqMesswerteDTO;
import de.muenchen.test.domain.Tagestyp;
import de.muenchen.test.repository.MqMesswerteRepository;
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

    public MqMesswerteDTO loadMesswerteByYear(Integer year) {
        List<MqMesswerte> messwerte = repo.findByDatumVon(LocalDateTime.of(year, 1, 1, 0, 0, 0));
        return mapper.map(messwerte);
    }

    public MqMesswerteDTO loadMesswerteWithinTimeRange(List<String> messquerschnitte, LocalDate datumVon, LocalDate datumBis, String uhrzeitVon,
            String uhrzeitBis, Optional<List<Tagestyp>> tagestypen, Optional<List<FzTyp>> fzTypen) {
        List<MqMesswerte> messwerte;
        if (tagestypen.isEmpty())
            messwerte = repo.findByIdAndDatumAndUhrzeit(messquerschnitte.get(0), datumVon.atStartOfDay(), datumBis.atStartOfDay(),
                    LocalTime.parse(uhrzeitVon, Constants.TIME_FORMATTER), LocalTime.parse(uhrzeitBis, Constants.TIME_FORMATTER));
        else {

            List<Integer> tagestypenInt = tagestypen.get().stream().map(Tagestyp::getId).toList();
            messwerte = repo.findByIdAndDatumAndUhrzeitAndTagestypen(messquerschnitte.get(0), datumVon.atStartOfDay(), datumBis.atStartOfDay(),
                    LocalTime.parse(uhrzeitVon, Constants.TIME_FORMATTER), LocalTime.parse(uhrzeitBis, Constants.TIME_FORMATTER), tagestypenInt);
        }
        //            if (page > resultPage.getTotalPages()) { TODO
        //                throw new MyResourceNotFoundException();
        //            }
        List<FzTyp> fzTypenList = fzTypen.orElseGet(() -> Arrays.asList(FzTyp.values()));

        return mapper.map(messwerte, fzTypenList);
    }

    public MqMesswerteDTO loadMesswerteWithFullRange(List<String> messquerschnitte, LocalDateTime datumVon, LocalDateTime datumBis,
            Optional<List<Tagestyp>> tagestypen, Optional<List<FzTyp>> fzTypen) {
        List<MqMesswerte> messwerte;
        if (tagestypen.isEmpty())
            messwerte = repo.findByIdAndDatum(messquerschnitte.get(0), datumVon, datumBis);
        else {
            List<Integer> tagestypenInt = tagestypen.get().stream().map(Tagestyp::getId).toList();
            messwerte = repo.findByIdAndDatumAndTagestypen(messquerschnitte.get(0), datumVon, datumBis, tagestypenInt);
        }
        //                    if (page > resultPage.getTotalPages()) { TODO
        //                        throw new MyResourceNotFoundException();
        //                    }
        List<FzTyp> fzTypenList = fzTypen.orElseGet(() -> Arrays.asList(FzTyp.values()));

        return mapper.map(messwerte, fzTypenList);
    }

}
