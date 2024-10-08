package de.muenchen.test.controller;

import de.muenchen.test.domain.Constants;
import de.muenchen.test.domain.FzTyp;
import de.muenchen.test.domain.MqMesswerteDTO;
import de.muenchen.test.service.MesswerteService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(Constants.VERSION1)
@RequiredArgsConstructor
@Slf4j
public class MqMesswerteController {

    private final MesswerteService service;

    @GetMapping(value = "/messwerte/byYear", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MqMesswerteDTO> loadMesswerteByYear(@RequestParam(name = "year") Integer year) {
        log.info("loadMesswerteByYear");
        MqMesswerteDTO messwerteDTO = service.loadMesswerteByYear(year);
        return ResponseEntity.ok(messwerteDTO);
    }

    @GetMapping(value = "/messwerte", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MqMesswerteDTO> loadMesswerte(@RequestParam(name = "messquerschnitte") List<String> messquerschnitte,
            @RequestParam(name = "datumVon") LocalDateTime datumVon,
            @RequestParam(name = "datumBis") LocalDateTime datumBis,
            @RequestParam(name = "uhrzeitVon", required = false) Optional<LocalTime> uhrzeitVon,
            @RequestParam(name = "uhrzeitBis", required = false) Optional<LocalTime> uhrzeitBis,
            @RequestParam(name = "tagestypen") List<Integer> tagestypen,
            @RequestParam(name = "fzTypen", required = false) Optional<List<FzTyp>> fzTypen,
            @RequestParam(name = "limit", required = false) Optional<Integer> limit,
            @RequestParam(name = "page", required = false) Optional<Integer> page) {
        log.info("loadMesswerte");
        MqMesswerteDTO messwerteDTO = service.loadMesswerte(messquerschnitte, datumVon, datumBis, uhrzeitVon, uhrzeitBis, tagestypen, fzTypen, limit, page);
        return ResponseEntity.ok(messwerteDTO);
    }
}
