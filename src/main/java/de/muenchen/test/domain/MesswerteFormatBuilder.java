package de.muenchen.test.domain;

import java.util.List;

public class MesswerteFormatBuilder {

    public static String createDefaultFormat() {
        StringBuilder sb = new StringBuilder("DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS");
        for (FzTyp fzTyp : FzTyp.values()) {
            sb.append(" ").append(fzTyp);
        }
        return sb.toString();
    }

    public static String createFormat(List<FzTyp> fzTypen) {
        StringBuilder sb = new StringBuilder("DATUM_UHRZEIT_VON DATUM_UHRZEIT_BIS");
        for (FzTyp fzTyp : fzTypen) {
            switch (fzTyp) {
            case KFZ_VERKEHR:
                sb.append(" ").append(FzTyp.KFZ_VERKEHR);
                break;
            case PKW:
                sb.append(" ").append(FzTyp.PKW);
                break;
            case PKWA:
                sb.append(" ").append(FzTyp.PKWA);
                break;
            case LKW:
                sb.append(" ").append(FzTyp.LKW);
                break;
            case LKWA:
                sb.append(" ").append(FzTyp.LKWA);
                break;
            case KRAD:
                sb.append(" ").append(FzTyp.KRAD);
                break;
            case LFW:
                sb.append(" ").append(FzTyp.LFW);
                break;
            case SATTEL_KFZ:
                sb.append(" ").append(FzTyp.SATTEL_KFZ);
                break;
            case BUS:
                sb.append(" ").append(FzTyp.BUS);
                break;
            case NK_KFZ:
                sb.append(" ").append(FzTyp.NK_KFZ);
                break;
            case ALLE_PKW:
                sb.append(" ").append(FzTyp.ALLE_PKW);
                break;
            case LASTZUG:
                sb.append(" ").append(FzTyp.LASTZUG);
                break;
            case GUETERVERKEHR:
                sb.append(" ").append(FzTyp.GUETERVERKEHR);
                break;
            case SCHWERVERKEHR:
                sb.append(" ").append(FzTyp.SCHWERVERKEHR);
                break;
            case RAD:
                sb.append(" ").append(FzTyp.RAD);
                break;
            }
        }
        return sb.toString();
    }

}
