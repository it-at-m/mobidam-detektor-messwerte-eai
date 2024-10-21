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
package de.muenchen.mobidam.domain.mapper;

import de.muenchen.mobidam.domain.Constants;
import de.muenchen.mobidam.domain.FzTyp;
import de.muenchen.mobidam.domain.MessquerschnitteDto;
import de.muenchen.mobidam.domain.MesswerteFormatBuilder;
import de.muenchen.mobidam.domain.MqMesswerte;
import de.muenchen.mobidam.domain.MqMesswerteDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MesswerteMapper {

    public MqMesswerteDto map(final Page<MqMesswerte> page, final List<FzTyp> fzTypen) {
        final var messwerte = ListUtils.defaultIfNull(page.getContent(), List.of());
        final var dto = map(messwerte, fzTypen);
        dto.setPageNumber(page.getNumber());
        dto.setPageSize(page.getSize());
        dto.setTotalPages(page.getTotalPages());
        return dto;
    }

    public MqMesswerteDto map(final List<MqMesswerte> messwerte, final List<FzTyp> fzTypen) {
        final var dto = new MqMesswerteDto();
        dto.setFormat(MesswerteFormatBuilder.createFormat(fzTypen));
        dto.setVersion(Constants.VERSION1);
        mapMesswerte(dto, messwerte, fzTypen);
        return dto;
    }

    private void mapMesswerte(final MqMesswerteDto dto, final List<MqMesswerte> messwerteList, final List<FzTyp> fzTypen) {
        for (final var messwerte : messwerteList) {
            // Get existing mq or create new one:
            final var mqDtoOptional = dto.getMessquerschnitte().stream().filter(mq -> mq.getMqId().equals(messwerte.getMqId())).findFirst();
            MessquerschnitteDto mqDto;
            if (mqDtoOptional.isPresent()) {
                mqDto = mqDtoOptional.get();
            } else {
                mqDto = new MessquerschnitteDto();
                mqDto.setMqId(messwerte.getMqId());
                final var intervalleList = new ArrayList<List<String>>();
                mqDto.setIntervalle(intervalleList);
                dto.getMessquerschnitte().add(mqDto);
            }

            final var intervallWerteList = new ArrayList<String>();
            mqDto.getIntervalle().add(intervallWerteList);

            intervallWerteList.add(messwerte.getDatumUhrzeitVon().format(Constants.DATE_FORMATTER));
            intervallWerteList.add(messwerte.getDatumUhrzeitBis().format(Constants.DATE_FORMATTER));

            addSpecifiedMesswerte(messwerte, intervallWerteList, fzTypen);
        }
    }

    private void addSpecifiedMesswerte(final MqMesswerte messwerte, final List<String> intervallWerteList, final List<FzTyp> fzTypen) {
        for (FzTyp fzTyp : fzTypen) {
            switch (fzTyp) {
            case KFZ_VERKEHR:
                intervallWerteList
                        .add(messwerte.getSummeKraftfahrzeugverkehr() == null ? "NULL" : messwerte.getSummeKraftfahrzeugverkehr().toString());
                break;
            case PKW:
                intervallWerteList
                        .add(messwerte.getAnzahlPkw() == null ? "NULL" : messwerte.getAnzahlPkw().toString());
                break;
            case PKWA:
                intervallWerteList
                        .add(messwerte.getAnzahlPkwA() == null ? "NULL" : messwerte.getAnzahlPkwA().toString());
                break;
            case LKW:
                intervallWerteList
                        .add(messwerte.getAnzahlLkw() == null ? "NULL" : messwerte.getAnzahlLkw().toString());
                break;
            case LKWA:
                intervallWerteList
                        .add(messwerte.getAnzahlLkwA() == null ? "NULL" : messwerte.getAnzahlLkwA().toString());
                break;
            case KRAD:
                intervallWerteList
                        .add(messwerte.getAnzahlKrad() == null ? "NULL" : messwerte.getAnzahlKrad().toString());
                break;
            case LFW:
                intervallWerteList
                        .add(messwerte.getAnzahlLfw() == null ? "NULL" : messwerte.getAnzahlLfw().toString());
                break;
            case SATTEL_KFZ:
                intervallWerteList
                        .add(messwerte.getAnzahlSattelKfz() == null ? "NULL" : messwerte.getAnzahlSattelKfz().toString());
                break;
            case BUS:
                intervallWerteList
                        .add(messwerte.getAnzahlBus() == null ? "NULL" : messwerte.getAnzahlBus().toString());
                break;
            case NK_KFZ:
                intervallWerteList
                        .add(messwerte.getAnzahlNkKfz() == null ? "NULL" : messwerte.getAnzahlNkKfz().toString());
                break;
            case ALLE_PKW:
                intervallWerteList
                        .add(messwerte.getSummeAllePkw() == null ? "NULL" : messwerte.getSummeAllePkw().toString());
                break;
            case LASTZUG:
                intervallWerteList
                        .add(messwerte.getSummeLastzug() == null ? "NULL" : messwerte.getSummeLastzug().toString());
                break;
            case GUETERVERKEHR:
                intervallWerteList
                        .add(messwerte.getSummeGueterverkehr() == null ? "NULL" : messwerte.getSummeGueterverkehr().toString());
                break;
            case SCHWERVERKEHR:
                intervallWerteList
                        .add(messwerte.getSummeSchwerverkehr() == null ? "NULL" : messwerte.getSummeSchwerverkehr().toString());
                break;
            case RAD:
                intervallWerteList
                        .add(messwerte.getAnzahlRad() == null ? "NULL" : messwerte.getAnzahlRad().toString());
                break;
            }
        }
    }

}
