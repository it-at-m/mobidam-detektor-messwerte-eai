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
package de.muenchen.test.controller;

import de.muenchen.test.configuration.LogExecutionTime;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @PreAuthorize("hasRole(T(de.muenchen.test.domain.Constants).CLIENT_ROLE)")
    @GetMapping(value = "/year", produces = MediaType.APPLICATION_JSON_VALUE)
    @LogExecutionTime
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
    @SecurityRequirement(name = "oauth2", scopes = { "read" })
    @PreAuthorize("hasRole(T(de.muenchen.test.domain.Constants).CLIENT_ROLE)")
    @GetMapping(value = "/fullrange", produces = MediaType.APPLICATION_JSON_VALUE)
    @LogExecutionTime
    public ResponseEntity<MqMesswerteDTO> loadMesswerteFullRange(@RequestParam(name = "messquerschnitte") List<String> messquerschnitte,
            @RequestParam(name = "datumVon") LocalDateTime datumVon,
            @RequestParam(name = "datumBis") LocalDateTime datumBis,
            @RequestParam(name = "tagestypen") Optional<List<Tagestyp>> tagestypen,
            @RequestParam(name = "fzTypen", required = false) Optional<List<FzTyp>> fzTypen,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "size", required = false) Optional<Integer> size) {
        log.info("loadMesswerte");
        //        Pageable pageable = PageRequest.of(page.get(), size.get());
        MqMesswerteDTO messwerteDTO = service.loadMesswerteWithFullRange(messquerschnitte, datumVon, datumBis, tagestypen, fzTypen);
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
    @SecurityRequirement(name = "oauth2", scopes = { "read" })
    @PreAuthorize("hasRole(T(de.muenchen.test.domain.Constants).CLIENT_ROLE)")
    @GetMapping(value = "/timerange", produces = MediaType.APPLICATION_JSON_VALUE)
    @LogExecutionTime
    public ResponseEntity<MqMesswerteDTO> loadMesswerteTimeRange(@RequestParam(name = "messquerschnitte") List<String> messquerschnitte,
            @RequestParam(name = "datumVon") LocalDate datumVon,
            @RequestParam(name = "datumBis") LocalDate datumBis,
            @RequestParam(name = "uhrzeitVon") String uhrzeitVon,
            @RequestParam(name = "uhrzeitBis") String uhrzeitBis,
            @RequestParam(name = "tagestypen") Optional<List<Tagestyp>> tagestypen,
            @RequestParam(name = "fzTypen", required = false) Optional<List<FzTyp>> fzTypen,
            @RequestParam(name = "page", required = false) Optional<Integer> page,
            @RequestParam(name = "limit", required = false) Optional<Integer> size) {
        log.info("loadMesswerte");
        //        Pageable pageable = PageRequest.of(page.get(), size.get());
        MqMesswerteDTO messwerteDTO = service.loadMesswerteWithinTimeRange(messquerschnitte, datumVon, datumBis, uhrzeitVon, uhrzeitBis, tagestypen, fzTypen);
        return ResponseEntity.ok(messwerteDTO);
    }
}
