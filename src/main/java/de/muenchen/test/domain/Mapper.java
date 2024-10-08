package de.muenchen.test.domain;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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

    public MqMesswerteDTO map(List<MqMesswerte> messwerte, final Optional<List<FzTyp>> fzTypen) {
        MqMesswerteDTO dto = new MqMesswerteDTO();
        if (fzTypen.isPresent())
            dto.setFormat(MesswerteFormatBuilder.createFormat(fzTypen.get()));
        else
            dto.setFormat(MesswerteFormatBuilder.createDefaultFormat());
        dto.setVersion(Constants.VERSION);

        mapMesswerte(dto, messwerte, fzTypen);
        return dto;
    }

    private void mapMesswerte(MqMesswerteDTO dto, List<MqMesswerte> messwerteList, Optional<List<FzTyp>> fzTypen) {
        for (MqMesswerte messwerte : messwerteList) {
            // Get existing mq or create new one:
            Optional<MessquerschnitteDTO> mqDtoOptional = dto.getMessquerschnitte().stream().filter(mq -> mq.getMqId().equals(messwerte.getMqId())).findFirst();
            MessquerschnitteDTO mqDto;
            if (mqDtoOptional.isPresent()) {
                mqDto = mqDtoOptional.get();
            } else {
                mqDto = new MessquerschnitteDTO();
                mqDto.setMqId(messwerte.getMqId());
                List<List<String>> intervalleList = new ArrayList<>();
                mqDto.setIntervalle(intervalleList);
                dto.getMessquerschnitte().add(mqDto);
            }

            List<String> intervallWerteList = new ArrayList<>();
            mqDto.getIntervalle().add(intervallWerteList);

            intervallWerteList.add(messwerte.getDatumUhrzeitVon().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            intervallWerteList.add(messwerte.getDatumUhrzeitBis().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            if (fzTypen.isEmpty())
                addSpecifiedMesswerte(messwerte, intervallWerteList, new ArrayList<>(Arrays.asList(FzTyp.values())));
            else
                addSpecifiedMesswerte(messwerte, intervallWerteList, fzTypen.get());
        }
    }

    private void addSpecifiedMesswerte(MqMesswerte messwerte, List<String> intervallWerteList, List<FzTyp> fzTypen) {
        for (FzTyp fzTyp : fzTypen) {
            switch (fzTyp) {
            case KFZ_VERKEHR:
                intervallWerteList
                        .add(messwerte.getSummeKraftfahrzeugverkehr() == null ? "NULL" : messwerte.getSummeKraftfahrzeugverkehr().toString());
                break;
            case PKW:
                intervallWerteList
                        .add(messwerte.getAnzahlPkw() == null ? "NULL" : messwerte.getAnzahlPkw().toString());
                break;
            case PKWA:
                intervallWerteList
                        .add(messwerte.getAnzahlPkwA() == null ? "NULL" : messwerte.getAnzahlPkwA().toString());
                break;
            case LKW:
                intervallWerteList
                        .add(messwerte.getAnzahlLkw() == null ? "NULL" : messwerte.getAnzahlLkw().toString());
                break;
            case LKWA:
                intervallWerteList
                        .add(messwerte.getAnzahlLkwA() == null ? "NULL" : messwerte.getAnzahlLkwA().toString());
                break;
            case KRAD:
                intervallWerteList
                        .add(messwerte.getAnzahlKrad() == null ? "NULL" : messwerte.getAnzahlKrad().toString());
                break;
            case LFW:
                intervallWerteList
                        .add(messwerte.getAnzahlLfw() == null ? "NULL" : messwerte.getAnzahlLfw().toString());
                break;
            case SATTEL_KFZ:
                intervallWerteList
                        .add(messwerte.getAnzahlSattelKfz() == null ? "NULL" : messwerte.getAnzahlSattelKfz().toString());
                break;
            case BUS:
                intervallWerteList
                        .add(messwerte.getAnzahlBus() == null ? "NULL" : messwerte.getAnzahlBus().toString());
                break;
            case NK_KFZ:
                intervallWerteList
                        .add(messwerte.getAnzahlNkKfz() == null ? "NULL" : messwerte.getAnzahlNkKfz().toString());
                break;
            case ALLE_PKW:
                intervallWerteList
                        .add(messwerte.getSummeAllePkw() == null ? "NULL" : messwerte.getSummeAllePkw().toString());
                break;
            case LASTZUG:
                intervallWerteList
                        .add(messwerte.getSummeLastzug() == null ? "NULL" : messwerte.getSummeLastzug().toString());
                break;
            case GUETERVERKEHR:
                intervallWerteList
                        .add(messwerte.getSummeGueterverkehr() == null ? "NULL" : messwerte.getSummeGueterverkehr().toString());
                break;
            case SCHWERVERKEHR:
                intervallWerteList
                        .add(messwerte.getSummeSchwerverkehr() == null ? "NULL" : messwerte.getSummeSchwerverkehr().toString());
                break;
            case RAD:
                intervallWerteList
                        .add(messwerte.getAnzahlRad() == null ? "NULL" : messwerte.getAnzahlRad().toString());
                break;
            }
        }
    }

}
