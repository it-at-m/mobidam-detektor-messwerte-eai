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
package de.muenchen.mobidam;

import de.muenchen.mobidam.domain.MqMesswerte;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {

    public static List<MqMesswerte> createMqMesswerte(
            final List<Long> mqIds,
            final List<Integer> tagestypen,
            final LocalDate datumVon,
            final LocalDate datumBis) {
        final var counter = new AtomicLong(1);

        return datumVon.datesUntil(datumBis.plusDays(1))
                .flatMap(day -> mqIds.stream().flatMap(mqId -> tagestypen.stream().flatMap(tagesTyp -> {

                    var localDateTime = LocalDateTime.of(day, LocalTime.MIN);
                    final var messwertList = new ArrayList<MqMesswerte>();
                    for (var index = 0; index < 96; index++) {
                        final var messwerte = new MqMesswerte();
                        messwerte.setObjectId(counter.get());
                        messwerte.setMqId(mqId);
                        messwerte.setDatumUhrzeitVon(localDateTime);
                        if (index < 95) {
                            localDateTime = localDateTime.plusMinutes(15);
                            messwerte.setDatumUhrzeitBis(localDateTime);
                        } else {
                            // Last interval of day
                            messwerte.setDatumUhrzeitBis(LocalDateTime.of(day, LocalTime.MIN));
                        }
                        messwerte.setTagetyp(tagesTyp);
                        messwerte.setPlausibilitaetsInfo("plausibel");
                        messwerte.setStoerung("keine Störung");
                        messwerte.setAnzahlPkw(counter.get());
                        messwerte.setAnzahlPkwA(counter.get());
                        messwerte.setAnzahlLfw(counter.get());
                        messwerte.setAnzahlKrad(counter.get());
                        messwerte.setAnzahlLkw(counter.get());
                        messwerte.setAnzahlLkwA(counter.get());
                        messwerte.setAnzahlSattelKfz(counter.get());
                        messwerte.setAnzahlBus(counter.get());
                        messwerte.setAnzahlNkKfz(counter.get());
                        messwerte.setSummeAllePkw(counter.get());
                        messwerte.setSummeLastzug(counter.get());
                        messwerte.setSummeGueterverkehr(counter.get());
                        messwerte.setSummeSchwerverkehr(counter.get());
                        messwerte.setSummeKraftfahrzeugverkehr(counter.get());
                        messwerte.setAnzahlRad(counter.get());
                        counter.incrementAndGet();
                        messwertList.add(messwerte);
                    }
                    return messwertList.stream();

                }))).toList();
    }

}
