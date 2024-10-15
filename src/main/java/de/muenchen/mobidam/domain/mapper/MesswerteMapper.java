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
import de.muenchen.mobidam.domain.MessquerschnitteDTO;
import de.muenchen.mobidam.domain.MesswerteFormatBuilder;
import de.muenchen.mobidam.domain.MqMesswerte;
import de.muenchen.mobidam.domain.MqMesswerteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MesswerteMapper {

    public MqMesswerteDTO map(final List<MqMesswerte> messwerte) {
        MqMesswerteDTO dto = new MqMesswerteDTO();
        dto.setFormat(Constants.ATTRIBUTE_DATUM_UHRZEIT_VON + " " + Constants.ATTRIBUTE_DATUM_UHRZEIT_BIS + " ANZAHL_PKW ANZAHL_LKW ANZALH_BUS");
        dto.setVersion(Constants.VERSION1);
        dto.getMessquerschnitte().add(new MessquerschnitteDTO());
        dto.getMessquerschnitte().get(0).setMqId(messwerte.get(0).getMqId());
        for (int i = 0; i < messwerte.size(); i++) {
            dto.getMessquerschnitte().get(0).getIntervalle().add(new ArrayList<>());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i).add(messwerte.get(i).getDatumUhrzeitVon().toString());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i).add(messwerte.get(i).getDatumUhrzeitBis().toString());

            dto.getMessquerschnitte().get(0).getIntervalle().get(i)
                    .add(messwerte.get(i).getAnzahlPkw() == null ? "NULL" : messwerte.get(i).getAnzahlPkw().toString());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i)
                    .add(messwerte.get(i).getAnzahlLkw() == null ? "NULL" : messwerte.get(i).getAnzahlLkw().toString());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i)
                    .add(messwerte.get(i).getAnzahlBus() == null ? "NULL" : messwerte.get(i).getAnzahlBus().toString());

        }
        return dto;
    }

    public MqMesswerteDTO map(final List<MqMesswerte> messwerte, final List<FzTyp> fzTypen) {
        MqMesswerteDTO dto = new MqMesswerteDTO();
        dto.setFormat(MesswerteFormatBuilder.createFormat(fzTypen));
        dto.setVersion(Constants.VERSION1);

        mapMesswerte(dto, messwerte, fzTypen);
        return dto;
    }

    private void mapMesswerte(final MqMesswerteDTO dto, final List<MqMesswerte> messwerteList, final List<FzTyp> fzTypen) {
        for (MqMesswerte messwerte : messwerteList) {
            // Get existing mq or create new one:
            Optional<MessquerschnitteDTO> mqDtoOptional = dto.getMessquerschnitte().stream().filter(mq -> mq.getMqId().equals(messwerte.getMqId())).findFirst();
            MessquerschnitteDTO mqDto;
            if (mqDtoOptional.isPresent()) {
                mqDto = mqDtoOptional.get();
            } else {
                mqDto = new MessquerschnitteDTO();
                mqDto.setMqId(messwerte.getMqId());
                List<List<String>> intervalleList = new ArrayList<>();
                mqDto.setIntervalle(intervalleList);
                dto.getMessquerschnitte().add(mqDto);
            }

            List<String> intervallWerteList = new ArrayList<>();
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
