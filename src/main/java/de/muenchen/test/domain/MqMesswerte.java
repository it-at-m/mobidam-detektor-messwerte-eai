/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2024
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
