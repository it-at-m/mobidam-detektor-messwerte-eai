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
package de.muenchen.mobidam.configuration.nfcconverter;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Wrapper für HttpServletRequest, der NFC-Konvertierung durchführt.
 *
 * @see java.text.Normalizer
 */
@Slf4j
public class NfcRequest extends HttpServletRequestWrapper implements HttpServletRequest {

    @SuppressWarnings("unused")
    private final Set<String> contentTypes;
    private Map<String, String[]> params;
    private Cookie[] cookies;
    private Map<String, List<String>> headers;

    public NfcRequest(final HttpServletRequest request, final Set<String> contentTypes) {
        super(request);
        this.params = null;
        this.cookies = null;
        this.headers = null;
        this.contentTypes = contentTypes;
    }

    private void convert() {
        if (params != null) {
            return;
        }
        this.params = NfcHelper.nfcConverter(getRequest().getParameterMap());
        this.cookies = NfcHelper.nfcConverter(getOriginalRequest().getCookies());
        this.headers = NfcHelper.nfcConverterForHeadersFromOriginalRequest(getOriginalRequest());
    }

    @Override
    public Cookie[] getCookies() {
        convert();
        return this.cookies;
    }

    @Override
    public String getHeader(final String name) {
        convert();
        final List<String> values = headers.get(NfcHelper.nfcConverter(name));
        return (values == null) ? null : values.get(0);
    }

    @Override
    public Enumeration<String> getHeaders(final String name) {
        convert();
        final List<String> values = this.headers.get(NfcHelper.nfcConverter(name));
        return (values == null) ? Collections.emptyEnumeration() : IteratorUtils.asEnumeration(values.iterator());
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        convert();
        return IteratorUtils.asEnumeration(this.headers.keySet().iterator());
    }

    @Override
    public String getPathInfo() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getPathInfo());
    }

    @Override
    public String getPathTranslated() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getPathTranslated());
    }

    @Override
    public String getContextPath() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getContextPath());
    }

    @Override
    public String getQueryString() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getQueryString());
    }

    @Override
    public String getRemoteUser() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getRemoteUser());
    }

    @Override
    public String getRequestedSessionId() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getRequestedSessionId());
    }

    @Override
    public String getRequestURI() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getRequestURI());
    }

    @Override
    public StringBuffer getRequestURL() {
        convert();
        return NfcHelper.nfcConverter(getOriginalRequest().getRequestURL());
    }

    /**
     * {@inheritDoc}
     *
     * Only the username is converted to nfc. Password won't be touched!
     */
    @Override
    public void login(String username, String password) throws ServletException {
        getOriginalRequest().login(NfcHelper.nfcConverter(username), password);
    }

    @Override
    public String getParameter(final String name) {
        convert();
        final String[] values = this.params.get(NfcHelper.nfcConverter(name));
        return (values == null) ? null : values[0];
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        convert();
        return this.params;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        convert();
        return IteratorUtils.asEnumeration(this.params.keySet().iterator());
    }

    @Override
    public String[] getParameterValues(final String name) {
        convert();
        return this.params.get(NfcHelper.nfcConverter(name));
    }

    @Override
    public BufferedReader getReader() throws IOException {
        log.debug("getReader()");
        return new BufferedReader(new NfcReader(getOriginalRequest().getReader()));
    }

    @Override
    public String getRemoteHost() {
        return NfcHelper.nfcConverter(getRequest().getRemoteHost());
    }

    @Override
    public Part getPart(final String name) throws IOException, ServletException {
        log.debug("getPart({})", name);
        return getOriginalRequest().getPart(name);
    }

    @Override
    public Collection<Part> getParts() throws IOException, ServletException {
        log.debug("getParts()");
        return getOriginalRequest().getParts();
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {

        final String encoding = getOriginalRequest().getCharacterEncoding();

        String content = null;
        try (final InputStream is = getOriginalRequest().getInputStream()) {
            content = new String(IOUtils.toByteArray(is), encoding);
        }

        log.debug("Converting InputStream data to NFC.");
        final String nfcConvertedContent = NfcHelper.nfcConverter(content);
        return new NfcServletInputStream(new ByteArrayInputStream(nfcConvertedContent.getBytes()));
    }

    private HttpServletRequest getOriginalRequest() {
        return (HttpServletRequest) getRequest();
    }

}
