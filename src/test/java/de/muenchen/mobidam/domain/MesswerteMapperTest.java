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
package de.muenchen.mobidam.domain;

import de.muenchen.mobidam.domain.mapper.MesswerteMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MesswerteMapperTest {

    private final MesswerteMapper messwerteMapper = new MesswerteMapper();

    @Test
    public void map() {
        final List<MqMesswerte> messwerte = new ArrayList<>();

        LocalDateTime datumVon1 = LocalDateTime.of(2024, 9, 1, 0, 0, 0);
        LocalDateTime datumBis1 = LocalDateTime.of(2024, 9, 1, 0, 15, 0);
        messwerte.add(createMesswerte(400101L, datumVon1, datumBis1, 23L, 15L));

        LocalDateTime datumVon2 = LocalDateTime.of(2024, 9, 1, 0, 15, 0);
        LocalDateTime datumBis2 = LocalDateTime.of(2024, 9, 1, 0, 30, 0);
        messwerte.add(createMesswerte(400101L, datumVon2, datumBis2, 31L, 16L));

        LocalDateTime datumVon3 = LocalDateTime.of(2024, 9, 1, 0, 0, 0);
        LocalDateTime datumBis3 = LocalDateTime.of(2024, 9, 1, 0, 15, 0);
        messwerte.add(createMesswerte(400102L, datumVon1, datumBis1, 23L, 15L));

        LocalDateTime datumVon4 = LocalDateTime.of(2024, 9, 1, 0, 15, 0);
        LocalDateTime datumBis4 = LocalDateTime.of(2024, 9, 1, 0, 30, 0);
        messwerte.add(createMesswerte(400102L, datumVon2, datumBis2, 31L, 16L));

        final List<FzTyp> fzTypen = new ArrayList<>();
        fzTypen.add(FzTyp.SATTEL_KFZ);
        fzTypen.add(FzTyp.KFZ_VERKEHR);

        MqMesswerteDto dto = messwerteMapper.map(messwerte, fzTypen);

        assertNotNull(dto);
        assertEquals(Constants.ATTRIBUTE_DATUM_UHRZEIT_VON + " " + Constants.ATTRIBUTE_DATUM_UHRZEIT_BIS + " " + FzTyp.SATTEL_KFZ.name() + " "
                + FzTyp.KFZ_VERKEHR.name(), dto.getFormat());
        assertNotNull(dto.getMessquerschnitte());
        assertFalse(dto.getMessquerschnitte().isEmpty());

        // MQ1
        assertNotNull(dto.getMessquerschnitte().get(0));
        assertEquals(400101L, dto.getMessquerschnitte().get(0).getMqId());
        assertNotNull(dto.getMessquerschnitte().get(0).getIntervalle());
        assertFalse(dto.getMessquerschnitte().get(0).getIntervalle().isEmpty());

        // MQ1 Intervall1
        assertNotNull(dto.getMessquerschnitte().get(0).getIntervalle().get(0));
        assertFalse(dto.getMessquerschnitte().get(0).getIntervalle().get(0).isEmpty());
        assertNotNull(dto.getMessquerschnitte().get(0).getIntervalle().get(0).get(0));
        assertFalse(dto.getMessquerschnitte().get(0).getIntervalle().get(0).get(0).isEmpty());
        assertEquals(datumVon1.format(Constants.DATE_FORMATTER), dto.getMessquerschnitte().get(0).getIntervalle().get(0).get(0));
        assertEquals(datumBis1.format(Constants.DATE_FORMATTER), dto.getMessquerschnitte().get(0).getIntervalle().get(0).get(1));
        assertEquals("15", dto.getMessquerschnitte().get(0).getIntervalle().get(0).get(2));
        assertEquals("23", dto.getMessquerschnitte().get(0).getIntervalle().get(0).get(3));

        // MQ1 Intervall2
        assertNotNull(dto.getMessquerschnitte().get(0).getIntervalle().get(1));
        assertFalse(dto.getMessquerschnitte().get(0).getIntervalle().get(1).isEmpty());
        assertNotNull(dto.getMessquerschnitte().get(0).getIntervalle().get(1).get(0));
        assertFalse(dto.getMessquerschnitte().get(0).getIntervalle().get(1).get(0).isEmpty());
        assertEquals(datumVon2.format(Constants.DATE_FORMATTER), dto.getMessquerschnitte().get(0).getIntervalle().get(1).get(0));
        assertEquals(datumBis2.format(Constants.DATE_FORMATTER), dto.getMessquerschnitte().get(0).getIntervalle().get(1).get(1));
        assertEquals("16", dto.getMessquerschnitte().get(0).getIntervalle().get(1).get(2));
        assertEquals("31", dto.getMessquerschnitte().get(0).getIntervalle().get(1).get(3));

        // MQ2
        assertNotNull(dto.getMessquerschnitte().get(1));
        assertEquals(400102L, dto.getMessquerschnitte().get(1).getMqId());
        assertNotNull(dto.getMessquerschnitte().get(1).getIntervalle());
        assertFalse(dto.getMessquerschnitte().get(1).getIntervalle().isEmpty());

        // MQ2 Intervall1
        assertNotNull(dto.getMessquerschnitte().get(1).getIntervalle().get(0));
        assertFalse(dto.getMessquerschnitte().get(1).getIntervalle().get(0).isEmpty());
        assertNotNull(dto.getMessquerschnitte().get(1).getIntervalle().get(0).get(0));
        assertFalse(dto.getMessquerschnitte().get(1).getIntervalle().get(0).get(0).isEmpty());
        assertEquals(datumVon3.format(Constants.DATE_FORMATTER), dto.getMessquerschnitte().get(1).getIntervalle().get(0).get(0));
        assertEquals(datumBis3.format(Constants.DATE_FORMATTER), dto.getMessquerschnitte().get(1).getIntervalle().get(0).get(1));
        assertEquals("15", dto.getMessquerschnitte().get(1).getIntervalle().get(0).get(2));
        assertEquals("23", dto.getMessquerschnitte().get(1).getIntervalle().get(0).get(3));

        // MQ2 Intervall2
        assertNotNull(dto.getMessquerschnitte().get(1).getIntervalle().get(1));
        assertFalse(dto.getMessquerschnitte().get(1).getIntervalle().get(1).isEmpty());
        assertNotNull(dto.getMessquerschnitte().get(1).getIntervalle().get(1).get(0));
        assertFalse(dto.getMessquerschnitte().get(1).getIntervalle().get(1).get(0).isEmpty());
        assertEquals(datumVon4.format(Constants.DATE_FORMATTER), dto.getMessquerschnitte().get(1).getIntervalle().get(1).get(0));
        assertEquals(datumBis4.format(Constants.DATE_FORMATTER), dto.getMessquerschnitte().get(1).getIntervalle().get(1).get(1));
        assertEquals("16", dto.getMessquerschnitte().get(1).getIntervalle().get(1).get(2));
        assertEquals("31", dto.getMessquerschnitte().get(1).getIntervalle().get(1).get(3));
    }

    private MqMesswerte createMesswerte(long mqId, LocalDateTime datumVon, LocalDateTime datumBis, long summeKraftfahrzeugverkehr, long anzahlSattelKfz) {
        MqMesswerte messwerte = new MqMesswerte();
        messwerte.setMqId(mqId);
        messwerte.setDatumUhrzeitVon(datumVon);
        messwerte.setDatumUhrzeitBis(datumBis);
        messwerte.setSummeKraftfahrzeugverkehr(summeKraftfahrzeugverkehr);
        messwerte.setAnzahlSattelKfz(anzahlSattelKfz);
        return messwerte;
    }
}
