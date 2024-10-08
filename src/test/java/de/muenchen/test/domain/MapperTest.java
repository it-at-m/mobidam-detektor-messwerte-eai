package de.muenchen.test.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MapperTest {

    private final Mapper mapper = new Mapper();

    @Test
    public void testMap() {
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

        MqMesswerteDTO dto = mapper.map(messwerte, Optional.of(fzTypen));

        assertNotNull(dto);
        assertEquals(Constants.ATTRIBUTE_DATUM_UHRZEIT_VON + " " + Constants.ATTRIBUTE_DATUM_UHRZEIT_BIS + " " + FzTyp.SATTEL_KFZ.name() + " " + FzTyp.KFZ_VERKEHR.name(), dto.getFormat());
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
