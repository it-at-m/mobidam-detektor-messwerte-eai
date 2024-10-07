package de.muenchen.test.domain;

import java.util.ArrayList;
import java.util.List;

public class Mapper {
    public List<MqMesswerteDTO> map(List<MqMesswerte> messwerte) {
        List<MqMesswerteDTO> dtos = new ArrayList<>();
        MqMesswerteDTO dto = new MqMesswerteDTO();
        dto.setFormat("DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS ANZAHL_PKW ANZAHL_LKW ANZALH_BUS");
        dto.setVersion("1.0");
        dto.getMessquerschnitte().add(new MessquerschnitteDTO());
        dto.getMessquerschnitte().get(0).setMqId(messwerte.get(0).getMqId());
        dtos.add(dto);
        for (int i=0;i<messwerte.size();i++){
            dto.getMessquerschnitte().get(0).getIntervalle().add(new ArrayList<>());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i).add(messwerte.get(i).getDatumUhrzeitVon().toString());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i).add(messwerte.get(i).getDatumUhrzeitBis().toString());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i).add(messwerte.get(i).getAnzahlPkw() == null ? "NULL" : messwerte.get(i).getAnzahlPkw().toString());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i).add(messwerte.get(i).getAnzahlLkw() == null ? "NULL" : messwerte.get(i).getAnzahlLkw().toString());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i).add(messwerte.get(i).getAnzahlBus() == null ? "NULL" : messwerte.get(i).getAnzahlBus().toString());

        }
        return dtos;
    }
}
