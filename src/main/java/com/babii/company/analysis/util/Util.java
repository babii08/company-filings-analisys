package com.babii.company.analysis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;

public class Util {

    static Logger logger = LoggerFactory.getLogger(Util.class);

    private Util() { }

    public static Optional<Long> getLongValue(String value) {
        BigDecimal number = getNumber(value);
        if (number != null) {
            return Optional.of(number.longValue());
        }
        return Optional.empty();
    }

    public static BigDecimal getNumber(String number) {
        if (number == null) {
            return null;
        }
        try {
            return new BigDecimal(number);
        } catch (NumberFormatException e) {
            logger.error(number, "Given string not a number: {}");
            throw e;
        }
    }
}
