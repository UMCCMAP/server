package com.umc.cmap.Domain.Cafe.Exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class CafeNotFoundException extends RuntimeException {
    private static final Logger logger = LoggerFactory.getLogger(CafeNotFoundException.class);
    public CafeNotFoundException(String message) {
        super(message);
        logger.error(message);
    }
}
