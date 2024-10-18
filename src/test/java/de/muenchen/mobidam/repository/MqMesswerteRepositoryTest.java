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
                List.of(1L, 2L, 3L, 4L),
                List.of(4,5,6),
                LocalDate.of(2024,1,1),
                LocalDate.of(2024,1,31)
        );
        mqMesswerteRepository.saveAllAndFlush(messwerte);
    }

    @Test
    void findByMqIdsAndDatumOneDay() {
        final var result = mqMesswerteRepository.findByMqIdsAndDatum(
                List.of("2"),
                LocalDateTime.of(LocalDate.of(24,1,10), LocalTime.MIN),
                LocalDateTime.of(LocalDate.of(24,1,10), LocalTime.MAX),
                PageRequest.of(0, 10000, Sort.by(Sort.Direction.ASC,"datumUhrzeitVon"))
        );

        result.getContent();
        Assertions.assertThat(result.getTotalPages()).isEqualTo(1);
        Assertions.assertThat(result.getNumber()).isEqualTo(0);
        Assertions.assertThat(result.getNumberOfElements()).isEqualTo(96);
        Assertions.assertThat(result.getContent().getFirst().getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(24,1,10), LocalTime.of(0,0,0)));
        Assertions.assertThat(result.getContent().getFirst().getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(24,1,10), LocalTime.of(0,15,0)));
        Assertions.assertThat(result.getContent().getLast().getDatumUhrzeitVon())
                .isEqualTo(LocalDateTime.of(LocalDate.of(24,1,10), LocalTime.of(23,45,0)));
        Assertions.assertThat(result.getContent().getLast().getDatumUhrzeitBis())
                .isEqualTo(LocalDateTime.of(LocalDate.of(24,1,10), LocalTime.of(0,0,0)));
    }



}
