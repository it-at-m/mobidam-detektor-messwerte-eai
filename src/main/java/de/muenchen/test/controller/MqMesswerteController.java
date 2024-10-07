package de.muenchen.test.controller;

import de.muenchen.test.domain.Mapper;
import de.muenchen.test.domain.MqMesswerte;
import de.muenchen.test.domain.MqMesswerteDTO;
import de.muenchen.test.repository.MqMesswerteRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MqMesswerteController {

    private final MqMesswerteRepository repo;
    private final Mapper mapper = new Mapper();

    @GetMapping(value = "/messwerte", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MqMesswerteDTO>> loadMesswerte(@RequestParam(name = "year", required = true) Integer year) {
        log.info("loadMesswerte");
        try {
            List<MqMesswerte> messwerte = repo.findByDatumVon(LocalDateTime.of(year, 1, 1, 0, 0, 0));
            List<MqMesswerteDTO> messwerteDTO = mapper.map(messwerte);
            return ResponseEntity.ok(messwerteDTO);
        } catch (final Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
