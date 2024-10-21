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

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/*{
  "version": "1.0",
  "format": "DATUM_UHRZEIT_VON,DATUM_UHRZEIT_BIS,ANZAHL_PKW,ANZAHL_LKW,...",
  "page": 5,
  "size": 100000
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
public class MqMesswerteDto implements Serializable {

    private String version;

    private String format;

    private Integer pageNumber;

    private Integer pageSize;

    private Integer totalPages;

    private List<MessquerschnitteDto> messquerschnitte = new ArrayList<>();

}
