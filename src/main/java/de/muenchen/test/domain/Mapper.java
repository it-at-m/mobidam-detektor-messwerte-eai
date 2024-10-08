package de.muenchen.test.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Mapper {
    public MqMesswerteDTO map(List<MqMesswerte> messwerte) {
        MqMesswerteDTO dto = new MqMesswerteDTO();
        dto.setFormat(Constants.ATTRIBUTE_DATUM_UHRZEIT_VON+" " + Constants.ATTRIBUTE_DATUM_UHRZEIT_BIS + " ANZAHL_PKW ANZAHL_LKW ANZALH_BUS");
        dto.setVersion(Constants.VERSION1);
        dto.getMessquerschnitte().add(new MessquerschnitteDTO());
        dto.getMessquerschnitte().get(0).setMqId(messwerte.get(0).getMqId());
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
        return dto;
    }

    public MqMesswerteDTO map(List<MqMesswerte> messwerte, final Optional<List<FzTyp>> fzTypen) {
        MqMesswerteDTO dto = new MqMesswerteDTO();
        if (fzTypen.isPresent())
            dto.setFormat(MesswerteFormatBuilder.createFormat(fzTypen.get()));
        else
            dto.setFormat(MesswerteFormatBuilder.createFormat(Arrays.asList(FzTyp.values())));
        dto.setVersion(Constants.VERSION1);

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

            intervallWerteList.add(messwerte.getDatumUhrzeitVon().format(Constants.DATE_FORMATTER));
            intervallWerteList.add(messwerte.getDatumUhrzeitBis().format(Constants.DATE_FORMATTER));

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
