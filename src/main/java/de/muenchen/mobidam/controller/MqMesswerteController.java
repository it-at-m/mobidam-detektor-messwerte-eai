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
package de.muenchen.mobidam.controller;

import de.muenchen.mobidam.configuration.LogExecutionTime;
import de.muenchen.mobidam.domain.Constants;
import de.muenchen.mobidam.domain.FzTyp;
import de.muenchen.mobidam.domain.MqMesswerteDto;
import de.muenchen.mobidam.domain.Tagestyp;
import de.muenchen.mobidam.service.MesswerteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(Constants.VERSION1 + "/messwerte")
@Slf4j
@RequiredArgsConstructor
public class MqMesswerteController {

    private final MesswerteService service;

    @PreAuthorize("hasRole(T(de.muenchen.mobidam.domain.Constants).CLIENT_ROLE)")
    @GetMapping(value = "/year", produces = MediaType.APPLICATION_JSON_VALUE)
    @LogExecutionTime
    public ResponseEntity<MqMesswerteDto> loadMesswerteByYear(@RequestParam(name = "year") @NotNull @Positive final Integer year) {
        MqMesswerteDto messwerte = service.loadMesswerteByYear(year);
        return ResponseEntity.ok(messwerte);
    }

    @Operation(summary = "Get messwerte without a time range")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Found messwerte",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MqMesswerteDto.class)
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
    @PreAuthorize("hasRole(T(de.muenchen.mobidam.domain.Constants).CLIENT_ROLE)")
    @GetMapping(value = "/fullrange", produces = MediaType.APPLICATION_JSON_VALUE)
    @LogExecutionTime
    public ResponseEntity<MqMesswerteDto> loadMesswerteFullRange(
            @RequestParam @NotEmpty final List<@NotEmpty String> messquerschnitte,
            @RequestParam @NotNull final LocalDateTime datumVon,
            @RequestParam @NotNull final LocalDateTime datumBis,
            @RequestParam @NotEmpty final List<@NotNull Tagestyp> tagestypen,
            @RequestParam(required = false) final Optional<List<@NotNull FzTyp>> fzTypen,
            @RequestParam(required = false, defaultValue = "${mobidam.detektor.messwerte.eai.pageing.default.page-number:0}") final @PositiveOrZero
            Integer page,
            @RequestParam(required = false, defaultValue = "${mobidam.detektor.messwerte.eai.pageing.default.page-size:100000}") final @Positive
            Integer size) {
        final var pageRequest = PageRequest.of(page, size);
        final var messwerte = service.loadMesswerteWithFullRange(
                messquerschnitte,
                datumVon,
                datumBis,
                tagestypen,
                fzTypen,
                pageRequest);
        return ResponseEntity.ok(messwerte);
    }

    @Operation(summary = "Get messwerte within a time range")
    @ApiResponses(
            value = {
                    @ApiResponse(
                            responseCode = "200", description = "Found messwerte",
                            content = { @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = MqMesswerteDto.class)
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
    @PreAuthorize("hasRole(T(de.muenchen.mobidam.domain.Constants).CLIENT_ROLE)")
    @GetMapping(value = "/timerange", produces = MediaType.APPLICATION_JSON_VALUE)
    @LogExecutionTime
    public ResponseEntity<MqMesswerteDto> loadMesswerteTimeRange(
            @RequestParam @NotEmpty final List<@NotEmpty String> messquerschnitte,
            @RequestParam @NotNull final LocalDate datumVon,
            @RequestParam @NotNull final LocalDate datumBis,
            @RequestParam @NotNull final LocalTime uhrzeitVon,
            @RequestParam @NotNull final LocalTime uhrzeitBis,
            @RequestParam @NotEmpty final List<@NotNull Tagestyp> tagestypen,
            @RequestParam(required = false) final Optional<List<@NotNull FzTyp>> fzTypen,
            @RequestParam(
                    required = false,
                    defaultValue = "${mobidam.detektor.messwerte.eai.pageing.default.page-number:0}"
            ) @PositiveOrZero final Integer page,
            @RequestParam(
                    required = false,
                    defaultValue = "${mobidam.detektor.messwerte.eai.pageing.default.page-size:100000}"
            ) @Positive final Integer size) {
        final var pageRequest = PageRequest.of(page, size);
        final var messwerte = service.loadMesswerteWithinTimeRange(
                messquerschnitte,
                datumVon,
                datumBis,
                uhrzeitVon,
                uhrzeitBis,
                tagestypen,
                fzTypen,
                pageRequest);
        return ResponseEntity.ok(messwerte);
    }
}
