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
package de.muenchen.test.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * This class represents a MqMesswerte.
 * <p>
 * The entity's content will be loaded according to the reference variable.
 * </p>
 */
@Entity
@Table(name = "MDAS_VERKEHRSDETEKTOREN_MESSWERTE_MQ_VW")
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode
@NoArgsConstructor
public class MqMesswerte {

    private static final long serialVersionUID = 1L;

    // ========= //
    // Variables //
    // ========= //

    @Id
    @Column(name = "OBJECTID")
    private Long objectId;

    @Column(name = "MQ_ID")
    private Long mqId;

    @Column(name = "DATUM_UHRZEIT_VON")
    private LocalDateTime datumUhrzeitVon;

    @Column(name = "DATUM_UHRZEIT_BIS")
    private LocalDateTime datumUhrzeitBis;

    @Column(name = "TAGESTYP")
    private Integer tagetyp;

    @Column(name = "PLAUSIBILITAETS_INFO")
    private String plausibilitaetsInfo;

    @Column(name = "STOERUNG")
    private String stoerung;

    @Column(name = "ANZAHL_PKW")
    private Long anzahlPkw;

    @Column(name = "ANZAHL_PKWA")
    private Long anzahlPkwA;

    @Column(name = "ANZAHL_LFW")
    private Long anzahlLfw;

    @Column(name = "ANZAHL_KRAD")
    private Long anzahlKrad;

    @Column(name = "ANZAHL_LKW")
    private Long anzahlLkw;

    @Column(name = "ANZAHL_LKWA")
    private Long anzahlLkwA;

    @Column(name = "ANZAHL_SATTEL_KFZ")
    private Long anzahlSattelKfz;

    @Column(name = "ANZAHL_BUS")
    private Long anzahlBus;

    @Column(name = "ANZAHL_NK_KFZ")
    private Long anzahlNkKfz;

    @Column(name = "SUMME_ALLE_PKW")
    private Long summeAllePkw;

    @Column(name = "SUMME_LASTZUG")
    private Long summeLastzug;

    @Column(name = "SUMME_GUETERVERKEHR")
    private Long summeGueterverkehr;

    @Column(name = "SUMME_SCHWERVERKEHR")
    private Long summeSchwerverkehr;

    @Column(name = "SUMME_KRAFTFAHRZEUGVERKEHR")
    private Long summeKraftfahrzeugverkehr;

    @Column(name = "ANZAHL_RAD")
    private Long anzahlRad;

    // getters and setters

}
