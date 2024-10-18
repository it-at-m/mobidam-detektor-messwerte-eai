package de.muenchen.mobidam.repository;

import de.muenchen.mobidam.MicroServiceApplication;
import de.muenchen.mobidam.TestConstants;
import de.muenchen.mobidam.TestData;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@SpringBootTest(
        classes = { MicroServiceApplication.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(profiles = { TestConstants.SPRING_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
class MqMesswerteRepositoryTest {

    @Autowired
    private MqMesswerteRepository mqMesswerteRepository;

    @Transactional
    @BeforeEach
    void beforeEach() {
        mqMesswerteRepository.deleteAll();
        final var messwerte = TestData.createMqMesswerte(
                List.of(1L, 2L, 3L),
                List.of(4, 5, 6),
                LocalDate.of(2024, 1, 6),
                LocalDate.of(2024, 1, 12));
        mqMesswerteRepository.saveAllAndFlush(messwerte);
    }

    @Test
    void findByMqIdsAndDatumOneDay() {
        final var result = mqMesswerteRepository.findByMqIdsAndDatum(
                List.of("2"),
                LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.MAX),
                PageRequest.of(0, 10000, Sort.by(Sort.Direction.ASC, "datumUhrzeitVon")));

        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getNumber()).isEqualTo(0);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(288);
        Assertions.assertThat(result.getContent().getFirst().getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(0, 0, 0)));
        Assertions.assertThat(result.getContent().getFirst().getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(0, 15, 0)));
        Assertions.assertThat(result.getContent().getLast().getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(23, 45, 0)));
        Assertions.assertThat(result.getContent().getLast().getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(0, 0, 0)));
    }

    @Test
    void findByMqIdsAndDatumAndTagestypenOneDay() {
        final var result = mqMesswerteRepository.findByMqIdsAndDatumAndTagestypen(
                List.of("2"),
                LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.MAX),
                List.of(5),
                PageRequest.of(0, 10000, Sort.by(Sort.Direction.ASC, "datumUhrzeitVon")));

        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getNumber()).isEqualTo(0);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(96);
        Assertions.assertThat(result.getContent().getFirst().getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(0, 0, 0)));
        Assertions.assertThat(result.getContent().getFirst().getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(0, 15, 0)));
        Assertions.assertThat(result.getContent().getLast().getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(23, 45, 0)));
        Assertions.assertThat(result.getContent().getLast().getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(0, 0, 0)));
    }

    @Test
    void findByMqIdsAndDatumAndUhrzeitOneDay() {
        final var result = mqMesswerteRepository.findByMqIdsAndDatumAndUhrzeit(
                List.of("2"),
                LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.MAX),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 15, 0),
                PageRequest.of(0, 10000, Sort.by(Sort.Direction.ASC, "datumUhrzeitVon")));

        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getNumber()).isEqualTo(0);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(15);
        Assertions.assertThat(result.getContent().getFirst().getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(10, 0, 0)));
        Assertions.assertThat(result.getContent().getFirst().getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(10, 15, 0)));
        Assertions.assertThat(result.getContent().getLast().getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(11, 0, 0)));
        Assertions.assertThat(result.getContent().getLast().getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(11, 15, 0)));
    }

    @Test
    void findByMqIdsAndDatumAndUhrzeitAndTagestypenOneDay() {
        final var result = mqMesswerteRepository.findByMqIdsAndDatumAndUhrzeitAndTagestypen(
                List.of("2"),
                LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.MAX),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 15, 0),
                List.of(5),
                PageRequest.of(0, 10000, Sort.by(Sort.Direction.ASC, "datumUhrzeitVon")));

        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getNumber()).isEqualTo(0);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(5);
        Assertions.assertThat(result.getContent().getFirst().getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(10, 0, 0)));
        Assertions.assertThat(result.getContent().getFirst().getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(10, 15, 0)));
        Assertions.assertThat(result.getContent().getLast().getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(11, 0, 0)));
        Assertions.assertThat(result.getContent().getLast().getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(11, 15, 0)));
    }

    @Test
    void findByMqIdsAndDatumAndUhrzeitAndTagestypenTwoDays() {
        final var result = mqMesswerteRepository.findByMqIdsAndDatumAndUhrzeitAndTagestypen(
                List.of("2"),
                LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.MAX),
                LocalTime.of(10, 0, 0),
                LocalTime.of(11, 15, 0),
                List.of(5),
                PageRequest.of(0, 10000, Sort.by(Sort.Direction.ASC, "datumUhrzeitVon")));

        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getNumber()).isEqualTo(0);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(10);

        Assertions.assertThat(result.getContent().get(0).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(10, 0, 0)));
        Assertions.assertThat(result.getContent().get(0).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(10, 15, 0)));

        Assertions.assertThat(result.getContent().get(1).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(10, 15, 0)));
        Assertions.assertThat(result.getContent().get(1).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(10, 30, 0)));

        Assertions.assertThat(result.getContent().get(2).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(10, 30, 0)));
        Assertions.assertThat(result.getContent().get(2).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(10, 45, 0)));

        Assertions.assertThat(result.getContent().get(3).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(10, 45, 0)));
        Assertions.assertThat(result.getContent().get(3).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(11, 0, 0)));

        Assertions.assertThat(result.getContent().get(4).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(11, 0, 0)));
        Assertions.assertThat(result.getContent().get(4).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(11, 15, 0)));

        Assertions.assertThat(result.getContent().get(5).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(10, 0, 0)));
        Assertions.assertThat(result.getContent().get(5).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(10, 15, 0)));

        Assertions.assertThat(result.getContent().get(6).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(10, 15, 0)));
        Assertions.assertThat(result.getContent().get(6).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(10, 30, 0)));

        Assertions.assertThat(result.getContent().get(7).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(10, 30, 0)));
        Assertions.assertThat(result.getContent().get(7).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(10, 45, 0)));

        Assertions.assertThat(result.getContent().get(8).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(10, 45, 0)));
        Assertions.assertThat(result.getContent().get(8).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(11, 0, 0)));

        Assertions.assertThat(result.getContent().get(9).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(11, 0, 0)));
        Assertions.assertThat(result.getContent().get(9).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(11, 15, 0)));

    }

    @Test
    void findByMqIdsAndDatumAndUhrzeitAndTagestypenTwoDaysAtEndOfDay() {
        final var result = mqMesswerteRepository.findByMqIdsAndDatumAndUhrzeitAndTagestypen(
                List.of("2"),
                LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.MAX),
                LocalTime.of(23, 0, 0),
                LocalTime.of(23, 59, 59),
                List.of(5),
                PageRequest.of(0, 10000, Sort.by(Sort.Direction.ASC, "datumUhrzeitVon")));

        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getNumber()).isEqualTo(0);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(8);

        Assertions.assertThat(result.getContent().get(0).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(23, 0, 0)));
        Assertions.assertThat(result.getContent().get(0).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(23, 15, 0)));

        Assertions.assertThat(result.getContent().get(1).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(23, 15, 0)));
        Assertions.assertThat(result.getContent().get(1).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(23, 30, 0)));

        Assertions.assertThat(result.getContent().get(2).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(23, 30, 0)));
        Assertions.assertThat(result.getContent().get(2).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(23, 45, 0)));

        Assertions.assertThat(result.getContent().get(3).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(23, 45, 0)));
        Assertions.assertThat(result.getContent().get(3).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 10), LocalTime.of(0, 0, 0)));

        Assertions.assertThat(result.getContent().get(4).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(23, 0, 0)));
        Assertions.assertThat(result.getContent().get(4).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(23, 15, 0)));

        Assertions.assertThat(result.getContent().get(5).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(23, 15, 0)));
        Assertions.assertThat(result.getContent().get(5).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(23, 30, 0)));

        Assertions.assertThat(result.getContent().get(6).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(23, 30, 0)));
        Assertions.assertThat(result.getContent().get(6).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(23, 45, 0)));

        Assertions.assertThat(result.getContent().get(7).getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(23, 45, 0)));
        Assertions.assertThat(result.getContent().get(7).getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(2024, 1, 11), LocalTime.of(0, 0, 0)));
    }

}
