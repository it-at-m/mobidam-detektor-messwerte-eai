package de.muenchen.test.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*{
  "version": "1.0",
  "format": "DATUM_UHRZEIT_VON,DATUM_UHRZEIT_BIS,ANZAHL_PKW,ANZAHL_LKW,...",
  "messquerschnitte": [
    {
      "mqId": "400001",
      "intervalle": [
        {
          "2024-05-01T00:00:00.000Z",
          "2024-05-01T00:15:00.000Z",
          "22",
          "34",
          "..."
        },
        {
          "2024-05-01T00:15:00.000Z",
          "2024-05-01T00:30:00.000Z",
          "21",
          "54",
          "..."
        }
      ]
    }
  ]
}*/
@Data
public class MqMesswerteDTO implements Serializable {

    private String version;
    private String format;
    private List<MessquerschnitteDTO> messquerschnitte = new ArrayList<>();

}
