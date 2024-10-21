package de.muenchen.mobidam.service;

import de.muenchen.mobidam.TestData;
import de.muenchen.mobidam.domain.FzTyp;
import de.muenchen.mobidam.domain.MessquerschnitteDto;
import de.muenchen.mobidam.domain.MqMesswerteDto;
import de.muenchen.mobidam.domain.Tagestyp;
import de.muenchen.mobidam.domain.mapper.MesswerteMapper;
import de.muenchen.mobidam.exceptions.PageNumberExceedsTotalPages;
import de.muenchen.mobidam.repository.MqMesswerteRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MesswerteServiceTest {

    @Mock
    private MqMesswerteRepository mqMesswerteRepository;

    private MesswerteService messwerteService;

    @BeforeEach
    void beforeEach() {
        this.messwerteService = new MesswerteService(mqMesswerteRepository, new MesswerteMapper());
        Mockito.reset(mqMesswerteRepository);
    }

    @Test
    void loadMesswerteWithinTimeRangeWithoutTagestyp() throws PageNumberExceedsTotalPages {
        final var messwerte = TestData.createMqMesswerte(
                List.of(1L),
                List.of(4),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1))
                .stream()
                .filter(messwert -> messwert.getDatumUhrzeitVon().isAfter(LocalDateTime.of(2024, 1, 1, 9, 59, 0))
                        && messwert.getDatumUhrzeitVon().isBefore(LocalDateTime.of(2024, 1, 1, 10, 29, 0)))
                .toList();
        final var page = new PageImpl<>(messwerte, PageRequest.of(4, 500), 2153);

        Mockito.when(mqMesswerteRepository.findByMqIdsAndDatumAndUhrzeit(
                List.of(1L),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MAX),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                PageRequest.of(4, 500))).thenReturn(page);

        final var result = messwerteService.loadMesswerteWithinTimeRange(
                List.of(1L),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                List.of(),
                Optional.of(List.of(FzTyp.ALLE_PKW)),
                PageRequest.of(4, 500));

        final var expected = new MqMesswerteDto();
        expected.setTotalPages(5);
        expected.setPageSize(500);
        expected.setPageNumber(4);
        expected.setVersion("v1");
        expected.setFormat("DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS ALLE_PKW");

        final var expectedMq = new MessquerschnitteDto();
        expectedMq.setMqId(1L);
        expectedMq.setIntervalle(
                List.of(List.of("2024-01-01 10:00:00", "2024-01-01 10:15:00", "41"), List.of("2024-01-01 10:15:00", "2024-01-01 10:30:00", "42")));

        expected.setMessquerschnitte(List.of(expectedMq));

        Assertions.assertThat(result).isNotNull().isEqualTo(expected);

        Mockito.verify(mqMesswerteRepository, Mockito.times(1)).findByMqIdsAndDatumAndUhrzeit(
                List.of(1L),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MAX),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                PageRequest.of(4, 500));

        Mockito.verify(mqMesswerteRepository, Mockito.times(0)).findByMqIdsAndDatumAndUhrzeitAndTagestypen(
                Mockito.anyList(),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalTime.class),
                Mockito.any(LocalTime.class),
                Mockito.anyList(),
                Mockito.any(PageRequest.class));
    }

    @Test
    void loadMesswerteWithinTimeRangeWithTagestyp() throws PageNumberExceedsTotalPages {
        final var messwerte = TestData.createMqMesswerte(
                List.of(1L),
                List.of(4),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1))
                .stream()
                .filter(messwert -> messwert.getDatumUhrzeitVon().isAfter(LocalDateTime.of(2024, 1, 1, 9, 59, 0))
                        && messwert.getDatumUhrzeitVon().isBefore(LocalDateTime.of(2024, 1, 1, 10, 29, 0)))
                .toList();
        final var page = new PageImpl<>(messwerte, PageRequest.of(4, 500), 2153);

        Mockito.when(mqMesswerteRepository.findByMqIdsAndDatumAndUhrzeitAndTagestypen(
                List.of(1L),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MAX),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                List.of(Tagestyp.SONNTAG_FEIERTAG.getId()),
                PageRequest.of(4, 500))).thenReturn(page);

        final var result = messwerteService.loadMesswerteWithinTimeRange(
                List.of(1L),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                List.of(Tagestyp.SONNTAG_FEIERTAG),
                Optional.of(List.of(FzTyp.ALLE_PKW)),
                PageRequest.of(4, 500));

        final var expected = new MqMesswerteDto();
        expected.setTotalPages(5);
        expected.setPageSize(500);
        expected.setPageNumber(4);
        expected.setVersion("v1");
        expected.setFormat("DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS ALLE_PKW");

        final var expectedMq = new MessquerschnitteDto();
        expectedMq.setMqId(1L);
        expectedMq.setIntervalle(
                List.of(List.of("2024-01-01 10:00:00", "2024-01-01 10:15:00", "41"), List.of("2024-01-01 10:15:00", "2024-01-01 10:30:00", "42")));

        expected.setMessquerschnitte(List.of(expectedMq));

        Assertions.assertThat(result).isNotNull().isEqualTo(expected);

        Mockito.verify(mqMesswerteRepository, Mockito.times(0)).findByMqIdsAndDatumAndUhrzeit(
                Mockito.anyList(),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalTime.class),
                Mockito.any(LocalTime.class),
                Mockito.any(PageRequest.class));

        Mockito.verify(mqMesswerteRepository, Mockito.times(1)).findByMqIdsAndDatumAndUhrzeitAndTagestypen(
                List.of(1L),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MAX),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 0, 0),
                List.of(Tagestyp.SONNTAG_FEIERTAG.getId()),
                PageRequest.of(4, 500));
    }

    @Test
    void loadMesswerteWithFullRangeWithoutTagestyp() throws PageNumberExceedsTotalPages {
        final var messwerte = TestData.createMqMesswerte(
                List.of(1L),
                List.of(4),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1))
                .stream()
                .filter(messwert -> messwert.getDatumUhrzeitVon().isAfter(LocalDateTime.of(2024, 1, 1, 9, 59, 0))
                        && messwert.getDatumUhrzeitVon().isBefore(LocalDateTime.of(2024, 1, 1, 10, 29, 0)))
                .toList();
        final var page = new PageImpl<>(messwerte, PageRequest.of(4, 500), 2153);

        Mockito.when(mqMesswerteRepository.findByMqIdsAndDatum(
                List.of(1L),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MAX),
                PageRequest.of(4, 500))).thenReturn(page);

        final var result = messwerteService.loadMesswerteWithFullRange(
                List.of(1L),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1),
                List.of(),
                Optional.of(List.of(FzTyp.ALLE_PKW)),
                PageRequest.of(4, 500));

        final var expected = new MqMesswerteDto();
        expected.setTotalPages(5);
        expected.setPageSize(500);
        expected.setPageNumber(4);
        expected.setVersion("v1");
        expected.setFormat("DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS ALLE_PKW");

        final var expectedMq = new MessquerschnitteDto();
        expectedMq.setMqId(1L);
        expectedMq.setIntervalle(
                List.of(List.of("2024-01-01 10:00:00", "2024-01-01 10:15:00", "41"), List.of("2024-01-01 10:15:00", "2024-01-01 10:30:00", "42")));

        expected.setMessquerschnitte(List.of(expectedMq));

        Assertions.assertThat(result).isNotNull().isEqualTo(expected);

        Mockito.verify(mqMesswerteRepository, Mockito.times(1)).findByMqIdsAndDatum(
                List.of(1L),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MAX),
                PageRequest.of(4, 500));

        Mockito.verify(mqMesswerteRepository, Mockito.times(0)).findByMqIdsAndDatumAndTagestypen(
                Mockito.anyList(),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.anyList(),
                Mockito.any(PageRequest.class));
    }

    @Test
    void loadMesswerteWithFullRangeWithTagestyp() throws PageNumberExceedsTotalPages {
        final var messwerte = TestData.createMqMesswerte(
                List.of(1L),
                List.of(4),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1))
                .stream()
                .filter(messwert -> messwert.getDatumUhrzeitVon().isAfter(LocalDateTime.of(2024, 1, 1, 9, 59, 0))
                        && messwert.getDatumUhrzeitVon().isBefore(LocalDateTime.of(2024, 1, 1, 10, 29, 0)))
                .toList();
        final var page = new PageImpl<>(messwerte, PageRequest.of(4, 500), 2153);

        Mockito.when(mqMesswerteRepository.findByMqIdsAndDatumAndTagestypen(
                List.of(1L),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MAX),
                List.of(Tagestyp.SONNTAG_FEIERTAG.getId()),
                PageRequest.of(4, 500))).thenReturn(page);

        final var result = messwerteService.loadMesswerteWithFullRange(
                List.of(1L),
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 1, 1),
                List.of(Tagestyp.SONNTAG_FEIERTAG),
                Optional.of(List.of(FzTyp.ALLE_PKW)),
                PageRequest.of(4, 500));

        final var expected = new MqMesswerteDto();
        expected.setTotalPages(5);
        expected.setPageSize(500);
        expected.setPageNumber(4);
        expected.setVersion("v1");
        expected.setFormat("DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS ALLE_PKW");

        final var expectedMq = new MessquerschnitteDto();
        expectedMq.setMqId(1L);
        expectedMq.setIntervalle(
                List.of(List.of("2024-01-01 10:00:00", "2024-01-01 10:15:00", "41"), List.of("2024-01-01 10:15:00", "2024-01-01 10:30:00", "42")));

        expected.setMessquerschnitte(List.of(expectedMq));

        Assertions.assertThat(result).isNotNull().isEqualTo(expected);

        Mockito.verify(mqMesswerteRepository, Mockito.times(0)).findByMqIdsAndDatum(
                Mockito.anyList(),
                Mockito.any(LocalDateTime.class),
                Mockito.any(LocalDateTime.class),
                Mockito.any(PageRequest.class));

        Mockito.verify(mqMesswerteRepository, Mockito.times(1)).findByMqIdsAndDatumAndTagestypen(
                List.of(1L),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 1), LocalTime.MAX),
                List.of(Tagestyp.SONNTAG_FEIERTAG.getId()),
                PageRequest.of(4, 500));
    }

}
