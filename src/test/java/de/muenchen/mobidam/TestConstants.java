/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2020
 */
package de.muenchen.mobidam;

import de.muenchen.mobidam.domain.MqMesswerte;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import oracle.ucp.util.Chain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TestConstants {

    public static final String SPRING_TEST_PROFILE = "unittest";

    public static final String SPRING_NO_SECURITY_PROFILE = "no-security";

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
