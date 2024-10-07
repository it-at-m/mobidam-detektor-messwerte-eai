package de.muenchen.test.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*{
  "version": "1.0",
  "format": "ANZAHL_PKW;ANZAHL_LKW;...",
  "messquerschnitte": [
    {
      "mqId": "400001",
      "intervalle": [
        {
          "datumUhrzeitVon": "2024-05-01T00:00:00.000Z",
          "datumUhrzeitBis": "2024-05-01T00:15:00.000Z",
          "messwerte": "22;34;..."
        },
        {
          "datumUhrzeitVon": "2024-05-01T00:15:00.000Z",
          "datumUhrzeitBis": "2024-05-01T00:30:00.000Z",
          "messwerte": "22;34;..."
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
