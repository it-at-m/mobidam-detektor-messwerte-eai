package de.muenchen.test.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum Tagestyp {
    WERKTAG_DI_MI_DO(1), WERKTAG_MO_FR(2), SAMSTAG(
            3), SONNTAG_FEIERTAG(4), WERKTAG_FERIEN(5);

    /**
     * Die Id zum TagesTyp.
     */
    @Getter
    private final Integer id;

}
