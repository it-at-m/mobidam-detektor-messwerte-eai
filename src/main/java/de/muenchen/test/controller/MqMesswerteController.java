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
import java.time.LocalTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MqMesswerteController {

    private final MqMesswerteRepository repo;
    private final Mapper mapper = new Mapper();

    @GetMapping(value = "/messwerte/byYear", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MqMesswerteDTO>> loadMesswerteByYear(@RequestParam(name = "year", required = true) Integer year) {
        log.info("loadMesswerteByYear");
        try {
            List<MqMesswerte> messwerte = repo.findByDatumVon(LocalDateTime.of(year, 1, 1, 0, 0, 0));
            List<MqMesswerteDTO> messwerteDTO = mapper.map(messwerte);
            return ResponseEntity.ok(messwerteDTO);
        } catch (final Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/messwerte", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MqMesswerteDTO> loadMesswerte(@RequestParam(name = "messquerschnitte", required = true) List<String> messquerschnitte,
            @RequestParam(name = "datumVon", required = true) LocalDateTime datumVon,
            @RequestParam(name = "datumBis", required = true) LocalDateTime datumBis,
            @RequestParam(name = "uhrzeitVon", required = false) LocalTime uhrzeitVon,
            @RequestParam(name = "uhrzeitBis", required = false) LocalTime uhrzeitBis,
            @RequestParam(name = "tagestypen", required = true) List<Integer> tagestypen,
            @RequestParam(name = "fzTypen", required = true) List<String> fzTypen,
            @RequestParam(name = "limit", required = false) Integer limit,
            @RequestParam(name = "page", required = false) Integer page) {
        log.info("loadMesswerte");
        List<MqMesswerte> messwerte = null;
        try {
            if (uhrzeitVon == null || uhrzeitBis == null) {
                messwerte = repo.findByDatumAndTagestypenAndFzTypen(messquerschnitte.get(0), datumVon, datumBis);
            } else {
                //                messwerte = repo.findByDatumAndUhrzeitAndTagestypenAndFzTypen(); TODO
            }
            //            if (page > resultPage.getTotalPages()) { TODO
            //                throw new MyResourceNotFoundException();
            //            }
            MqMesswerteDTO messwerteDTO = mapper.map(messwerte, fzTypen);
            return ResponseEntity.ok(messwerteDTO);
        } catch (final Exception ex) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
