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
package de.muenchen.test.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * The class adds a {@link HttpHeaders#CACHE_CONTROL} header to each http response, if the header is
 * not already set.
 */
@Configuration
public class CacheControlConfiguration {

    private static final String CACHE_CONTROL_HEADER_VALUES = "no-cache, no-store, must-revalidate";

    @Bean
    public FilterRegistrationBean<CacheControlFilter> cacheControlFilter() {
        FilterRegistrationBean<CacheControlFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new CacheControlFilter());
        registration.addUrlPatterns("/*");
        return registration;
    }

    /**
     * The concrete implementation of the cache control filter which adds a
     * {@link HttpHeaders#CACHE_CONTROL} to a http response, if the header is not already
     * set.
     */
    public static class CacheControlFilter extends OncePerRequestFilter {

        /**
         * The method which adds the {@link HttpHeaders#CACHE_CONTROL} header to the
         * {@link HttpServletResponse} given in the parameter, if the header is not
         * already set.
         *
         * Same contract as for {@code super.doFilter}, but guaranteed to be just invoked once per request
         * within a single request thread. See
         * {@link OncePerRequestFilter#shouldNotFilterAsyncDispatch()} for details.
         * <p>
         * Provides HttpServletRequest and HttpServletResponse arguments instead of the default
         * ServletRequest and ServletResponse ones.
         */
        @Override
        protected void doFilterInternal(HttpServletRequest request,
                HttpServletResponse response,
                FilterChain filterChain) throws ServletException, IOException {

            final String cacheControlHeaderValue = response.getHeader(HttpHeaders.CACHE_CONTROL);
            if (StringUtils.isBlank(cacheControlHeaderValue)) {
                response.addHeader(HttpHeaders.CACHE_CONTROL, CACHE_CONTROL_HEADER_VALUES);
            }

            filterChain.doFilter(request, response);

        }

    }

}
