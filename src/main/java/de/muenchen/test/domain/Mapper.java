package de.muenchen.test.domain;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Mapper {
    public List<MqMesswerteDTO> map(List<MqMesswerte> messwerte) {
        List<MqMesswerteDTO> dtos = new ArrayList<>();
        MqMesswerteDTO dto = new MqMesswerteDTO();
        dto.setFormat("DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS ANZAHL_PKW ANZAHL_LKW ANZALH_BUS");
        dto.setVersion("1.0");
        dto.getMessquerschnitte().add(new MessquerschnitteDTO());
        dto.getMessquerschnitte().get(0).setMqId(messwerte.get(0).getMqId());
        dtos.add(dto);
        for (int i = 0; i < messwerte.size(); i++) {
            dto.getMessquerschnitte().get(0).getIntervalle().add(new ArrayList<>());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i).add(messwerte.get(i).getDatumUhrzeitVon().toString());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i).add(messwerte.get(i).getDatumUhrzeitBis().toString());

            dto.getMessquerschnitte().get(0).getIntervalle().get(i)
                    .add(messwerte.get(i).getAnzahlPkw() == null ? "NULL" : messwerte.get(i).getAnzahlPkw().toString());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i)
                    .add(messwerte.get(i).getAnzahlLkw() == null ? "NULL" : messwerte.get(i).getAnzahlLkw().toString());
            dto.getMessquerschnitte().get(0).getIntervalle().get(i)
                    .add(messwerte.get(i).getAnzahlBus() == null ? "NULL" : messwerte.get(i).getAnzahlBus().toString());

        }
        return dtos;
    }

    public MqMesswerteDTO map(List<MqMesswerte> messwerte, final List<String> fzTypen) {
        MqMesswerteDTO dto = new MqMesswerteDTO();
        List<FzTyp> fzTypList = new ArrayList<>();
        for (String typ : fzTypen) {
            fzTypList.add(FzTyp.valueOf(typ));
        }
        dto.setFormat(createFormat(fzTypList));
        dto.setVersion("1.0");

        mapMesswerte(dto, messwerte, fzTypList);
        return dto;
    }

    private void mapMesswerte(MqMesswerteDTO dto, List<MqMesswerte> messwerte, List<FzTyp> fzTypen) {
        for (MqMesswerte messwert : messwerte) {
            // Get existing mq or create new one:
            Optional<MessquerschnitteDTO> mqDtoOptional = dto.getMessquerschnitte().stream().filter(mq -> mq.getMqId().equals(messwert.getMqId())).findFirst();
            MessquerschnitteDTO mqDto;
            if (mqDtoOptional.isPresent()) {
                mqDto = mqDtoOptional.get();
            } else {
                mqDto = new MessquerschnitteDTO();
                mqDto.setMqId(messwert.getMqId());
                List<List<String>> intervalleList = new ArrayList<>();
                mqDto.setIntervalle(intervalleList);
                dto.getMessquerschnitte().add(mqDto);
            }

            ArrayList<String> intervallWerteList = new ArrayList<>();
            mqDto.getIntervalle().add(intervallWerteList);

            intervallWerteList.add(messwert.getDatumUhrzeitVon().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            intervallWerteList.add(messwert.getDatumUhrzeitBis().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            for (FzTyp fzTyp : fzTypen) {
                switch (fzTyp) {
                case KFZ_VERKEHR:
                    intervallWerteList
                            .add(messwert.getSummeKraftfahrzeugverkehr() == null ? "NULL" : messwert.getSummeKraftfahrzeugverkehr().toString());
                    break;
                case SATTEL_KFZ:
                    intervallWerteList
                            .add(messwert.getAnzahlSattelKfz() == null ? "NULL" : messwert.getAnzahlSattelKfz().toString());
                    break;
                }
            }
        }
    }

    private String createFormat(List<FzTyp> fzTypen) {
        StringBuilder sb = new StringBuilder("DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS");
        for (FzTyp fzTyp : fzTypen) {
            switch (fzTyp) {
            case KFZ_VERKEHR:
                sb.append(" ").append(FzTyp.KFZ_VERKEHR);
                break;
            case SATTEL_KFZ:
                sb.append(" ").append(FzTyp.SATTEL_KFZ);
                break;
            // TODO
            }
        }
        return sb.toString();
    }

}
