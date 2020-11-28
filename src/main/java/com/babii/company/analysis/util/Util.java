package com.babii.company.analysis.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.babii.company.analysis.util.Constants.SYMBOL_CIK_MAP_URL;

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

    public static Optional<Document> getJsoupDocument(String link) {
        Document document;
        try {
            document = Jsoup.connect(link).userAgent("Mozilla").get();
            return Optional.of(document);
        } catch (IOException e) {
            logger.error(e.getMessage(), "Could not get information from given link. Error message: {}");
            return Optional.empty();
        }
    }
}
