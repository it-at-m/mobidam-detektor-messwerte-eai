package de.muenchen.test.domain;

import java.time.format.DateTimeFormatter;

public class Constants {

    public static final String VERSION1 = "v1";

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");;

    public static final String ATTRIBUTE_DATUM_UHRZEIT_VON = "DATUM_UHRZEIT_VON";
    public static final String ATTRIBUTE_DATUM_UHRZEIT_BIS = "DATUM_UHRZEIT_BIS";

    public static final String CLIENT_ROLE = "DETEKTOR_MESSWERTE_VIEWER";
}
