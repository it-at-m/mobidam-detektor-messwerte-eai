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
package de.muenchen.mobidam.configuration;

import de.muenchen.mobidam.configuration.nfcconverter.NfcRequestFilter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * <p>
 * <em>Beispiel</em> für Konfiguration des NFC Request-Filters
 * </p>
 * <ul>
 * <li>Es werden alle Requests gefiltert, die an URIs unter <em>/*</em> geschickt werden.</li>
 * <li>Filter ist in Bean <em>nfcRequestFilter</em> enthalten.</li>
 * <li>Es werden nur Requests mit den Content-Types <em>text/plain</em>; <em>application/json</em>
 * und <em>text/html</em> gefiltert.</li>
 * </ul>
 */
@Configuration
public class UnicodeConfiguration {

    private static final String NFC_FILTER_NAME = "nfcRequestFilter";

    private static final String NFC_WHITE_LIST = "text/plain; application/json; application/hal+json; text/html";

    private static final String[] NFC_URLS = ArrayUtils.toArray("/*");

    @Bean
    public FilterRegistrationBean<NfcRequestFilter> nfcRequestFilterRegistration(final NfcRequestFilter nfcRequestFilter) {

        final FilterRegistrationBean<NfcRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(nfcRequestFilter);
        registration.setName(NFC_FILTER_NAME);
        registration.setOrder(Ordered.LOWEST_PRECEDENCE);
        registration.setAsyncSupported(false);

        //
        // Setzen der URLs, auf die Filter anzuwenden ist.
        //
        registration.addUrlPatterns(NFC_URLS);

        //
        // Setzen der White-List von ContentTypes für
        //
        registration.addInitParameter(NfcRequestFilter.CONTENTTYPES_PROPERTY, NFC_WHITE_LIST);

        return registration;

    }

}
