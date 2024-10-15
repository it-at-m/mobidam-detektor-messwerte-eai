/*
 * Copyright (c): it@M - Dienstleister für Informations- und Telekommunikationstechnik
 * der Landeshauptstadt München, 2023
 */
package de.muenchen.test.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AspectConfiguration {

    @Around("@annotation(de.muenchen.test.configuration.LogExecutionTime)")
    public Object logExecutionTime(final ProceedingJoinPoint joinPoint) throws Throwable {
        final var start = System.currentTimeMillis();
        final var proceed = joinPoint.proceed();
        final var executionTime = System.currentTimeMillis() - start;
        log.debug(">> {} executed in {} seconds", joinPoint.getSignature(), executionTime / 1000.0);
        return proceed;
    }
}
