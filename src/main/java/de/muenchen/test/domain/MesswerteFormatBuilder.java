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
package de.muenchen.test.domain;

import java.util.List;

public class MesswerteFormatBuilder {

    public static String createFormat(List<FzTyp> fzTypen) {
        StringBuilder sb = new StringBuilder();
        sb.append(Constants.ATTRIBUTE_DATUM_UHRZEIT_VON).append(" ").append(Constants.ATTRIBUTE_DATUM_UHRZEIT_BIS);
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
