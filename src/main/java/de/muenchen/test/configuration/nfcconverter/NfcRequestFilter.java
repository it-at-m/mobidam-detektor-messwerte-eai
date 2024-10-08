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

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * <p>
 * Spring-Filter, der eine NFC-Normalisierung aller <em>sicher textuellen</em> Inhalte durchführt.
 * </p>
 *
 * <strong>Achtung:</strong>
 * <ul>
 * <li>Alle Datenströme die in Zusammenhang mit Multipart-Requests stehen werden nicht nach NFC
 * normalisiert.
 * Grund ist, dass hier binäre Datenströme übergeben werden und diese i.d.R. nicht einfacher Text
 * sind.
 * Falls notwendig bzw. sinnvoll kann bzw. muss die Anwendungslogik oder eine geeignete Bibliothek
 * ggf. eine NFC-Normalisierung durchgeführt werden.
 * <li>NFC-Normalisierung kann nur auf der Zeichenebene durchgeführt werden und für die
 * Konvertierung von
 * binären Datenströmen ist die Kenntnis des Datenformats notwendig, was die Kenntnis des
 * verwendeten Charsets
 * impliziert. Dies lässt die NFC-Normalisierung in einem generischen Filter sinnvoll erscheinen.
 * </li>
 * </ul>
 *
 * @see java.text.Normalizer
 * @see HttpServletRequest#getPart(String)
 * @see HttpServletRequest#getParts()
 */
@Component
@Slf4j
public class NfcRequestFilter extends OncePerRequestFilter {

    /**
     * Name des Properties für Konfiguration der White-List für Content-Types.
     *
     * @see #getContentTypes()
     * @see #setContentTypes(String)
     */
    public static final String CONTENTTYPES_PROPERTY = "contentTypes";

    private final Set<String> contentTypes = new HashSet<>();

    /**
     * @return Das Property <em>contentTypes</em>
     */
    public String getContentTypes() {
        return String.join("; ", this.contentTypes);
    }

    /**
     * @param contentTypes Das Property <em>contentTypes</em>
     */
    @Autowired(required = false)
    public void setContentTypes(final String contentTypes) {
        this.contentTypes.clear();
        if (StringUtils.isEmpty(contentTypes)) {
            log.info("Disabling context-type filter.");

        } else {
            final Set<String> newContentTypes = Arrays.stream(contentTypes.split(";")).map(String::trim)
                    .collect(Collectors.toSet());
            this.contentTypes.addAll(newContentTypes);
            log.info("Enabled content-type filtering to NFC for: {}", getContentTypes());

        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        log.debug("Request-Type={}", request.getClass().getName());
        log.debug("Intercepting request for URI {}", request.getRequestURI());

        final String contentType = request.getContentType();
        log.debug("ContentType for request with URI: \"{}\"", contentType);
        if ((contentTypes != null) && (contentTypes.contains(contentType))) {
            log.debug("Processing request {}.", request.getRequestURI());
            filterChain.doFilter(new NfcRequest(request, contentTypes), response);
        } else {
            log.debug("Skip processing of HTTP request since it's content type \"{}\" is not in whitelist.", contentType);
            filterChain.doFilter(request, response);
        }
    }

}
