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
package de.muenchen.test.configuration.nfcconverter;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

/**
 * <p>
 * Wrapper für Reader der eine NFC-Konvertierung durchführt.
 * </p>
 *
 * <p>
 * <strong>Achtung:</strong>
 * <ul>
 * <li>Bei Java-Readern und -Writern kann gefahrlos eine NFC-Konvertierung
 * durchgeführt werden, da dort Zeichen verarbeitet werden.</li>
 * <li>Dieser Reader liest bei vor dem Lesen des ersten Zeichens denn vollständig Text des
 * gewrappten Readers in einern internen Buffer und führt darauf die NFC-Normalisierung
 * durch. Grund ist, dass NFC-Konvertierung kann nicht auf Basis von einzelnen Zeichen
 * durchgeführt werden kann. Dies kann zu erhöhter Latenz führen.</li>
 * </ul>
 * </p>
 */
@Slf4j
public class NfcReader extends Reader {

    private final Reader original;

    private CharArrayReader converted;

    public NfcReader(final Reader original) {
        this.original = original;
        this.converted = null;
    }

    private void convert() {

        if (converted != null) {
            return;
        }

        log.debug("Converting Reader data to NFC.");
        try {
            final String nfdContent = IOUtils.toString(original);
            final String nfcConvertedContent = NfcHelper.nfcConverter(nfdContent);
            converted = new CharArrayReader(nfcConvertedContent.toCharArray());

        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int read() throws IOException {
        convert();
        return converted.read();
    }

    @Override
    public int read(char[] cbuf, int off, int len) throws IOException {
        convert();
        return converted.read(cbuf, off, len);
    }

    @Override
    public void close() {
        // Nothing to do
    }

    @Override
    public long skip(long n) throws IOException {
        convert();
        return converted.skip(n);
    }

    @Override
    public boolean ready() throws IOException {
        convert();
        return converted.ready();
    }

    @Override
    public boolean markSupported() {
        convert();
        return converted.markSupported();
    }

    @Override
    public void mark(int readAheadLimit) throws IOException {
        convert();
        converted.mark(readAheadLimit);
    }

    @Override
    public void reset() throws IOException {
        convert();
        converted.reset();
    }

}
