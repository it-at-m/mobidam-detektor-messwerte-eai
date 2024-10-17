package de.muenchen.mobidam.repository;

import de.muenchen.mobidam.MicroServiceApplication;
import de.muenchen.mobidam.TestConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        classes = { MicroServiceApplication.class },
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles(profiles = { TestConstants.SPRING_TEST_PROFILE, TestConstants.SPRING_NO_SECURITY_PROFILE })
class MqMesswerteRepositoryTest {

    @Autowired
    private MqMesswerteRepository mqMesswerteRepository;

    @Test
    void test() {

    }

}
