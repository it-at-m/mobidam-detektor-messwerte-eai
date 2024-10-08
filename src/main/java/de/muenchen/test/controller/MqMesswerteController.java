package de.muenchen.test.controller;

import de.muenchen.test.domain.Constants;
import de.muenchen.test.domain.FzTyp;
import de.muenchen.test.domain.MqMesswerteDTO;
import de.muenchen.test.domain.Tagestyp;
import de.muenchen.test.service.MesswerteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(Constants.VERSION1 + "/messwerte")
@RequiredArgsConstructor
@Slf4j
public class MqMesswerteController {

    private final MesswerteService service;

    @PreAuthorize("hasRole(T(de.muenchen.test.domain.Constants).CLIENT_ROLE.name())")
    @GetMapping(value = "/year", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MqMesswerteDTO> loadMesswerteByYear(@RequestParam(name = "year") Integer year) {
        log.info("loadMesswerteByYear");
        MqMesswerteDTO messwerteDTO = service.loadMesswerteByYear(year);
        return ResponseEntity.ok(messwerteDTO);
    }

    @Operation(summary = "Get messwerte without a time range")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Found messwerte",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MqMesswerteDTO.class)
                            ) }
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid parameters",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "No messwerte found",
                            content = @Content
                    ) }
    )
    @PreAuthorize("hasRole(T(de.muenchen.test.domain.Constants).CLIENT_ROLE.name())")
    @GetMapping(value = "/fullrange", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MqMesswerteDTO> loadMesswerteFullRange(@RequestParam(name = "messquerschnitte") List<String> messquerschnitte,
            @RequestParam(name = "datumVon") LocalDateTime datumVon,
            @RequestParam(name = "datumBis") LocalDateTime datumBis,
            @RequestParam(name = "tagestypen") Optional<List<Tagestyp>> tagestypen,
            @RequestParam(name = "fzTypen", required = false) Optional<List<FzTyp>> fzTypen,
            @RequestParam(name = "limit", required = false) Optional<Integer> limit,
            @RequestParam(name = "page", required = false) Optional<Integer> page) {
        log.info("loadMesswerte");
        MqMesswerteDTO messwerteDTO = service.loadMesswerteWithFullRange(messquerschnitte, datumVon, datumBis, tagestypen, fzTypen, limit, page);
        return ResponseEntity.ok(messwerteDTO);
    }

    @Operation(summary = "Get messwerte within a time range")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Found messwerte",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MqMesswerteDTO.class)
                            ) }
                    ),
                    @ApiResponse(
                            responseCode = "400", description = "Invalid parameters",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "401", description = "Unauthorized",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404", description = "No messwerte found",
                            content = @Content
                    ) }
    )
    @PreAuthorize("hasRole(T(de.muenchen.test.domain.Constants).CLIENT_ROLE.name())")
    @GetMapping(value = "/timerange", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MqMesswerteDTO> loadMesswerteTimeRange(@RequestParam(name = "messquerschnitte") List<String> messquerschnitte,
            @RequestParam(name = "datumVon") LocalDate datumVon,
            @RequestParam(name = "datumBis") LocalDate datumBis,
            @RequestParam(name = "uhrzeitVon") String uhrzeitVon,
            @RequestParam(name = "uhrzeitBis") String uhrzeitBis,
            @RequestParam(name = "tagestypen") Optional<List<Tagestyp>> tagestypen,
            @RequestParam(name = "fzTypen", required = false) Optional<List<FzTyp>> fzTypen,
            @RequestParam(name = "limit", required = false) Optional<Integer> limit,
            @RequestParam(name = "page", required = false) Optional<Integer> page) {
        log.info("loadMesswerte");
        MqMesswerteDTO messwerteDTO = service.loadMesswerteWithinTimeRange(messquerschnitte, datumVon, datumBis, uhrzeitVon, uhrzeitBis, tagestypen, fzTypen,
                limit, page);
        return ResponseEntity.ok(messwerteDTO);
    }
}
