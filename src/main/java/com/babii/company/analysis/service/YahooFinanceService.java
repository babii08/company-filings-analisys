package com.babii.company.analysis.service;

import com.babii.company.analysis.domain.YahooFinanceCompanyInfo;
import com.babii.company.analysis.exception.YahooFinanceException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.*;

import static com.babii.company.analysis.util.Constants.LANG;
import static com.babii.company.analysis.util.Constants.NAME_SYMBOL_INFO;
import static com.babii.company.analysis.util.Constants.YAHOO_FINANCE_COMMUNICATION_ERROR;

@Service
public class YahooFinanceService {
    private Logger logger = LoggerFactory.getLogger(YahooFinanceService.class);

    private final RestTemplate restTemplate;

    public YahooFinanceService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<YahooFinanceCompanyInfo> getCompaniesWithSymbolAndName() {
        logger.info("Retrieving all companies");

        final URI uri = UriComponentsBuilder.fromHttpUrl(NAME_SYMBOL_INFO)
//                .queryParam("query", symbol)
//                .queryParam("lang", LANG)
                .build()
                .toUri();
        try {
            ResponseEntity<YahooFinanceCompanyInfo[]> companies = restTemplate
                    .getForEntity(uri, YahooFinanceCompanyInfo[].class);
            return Arrays.asList(Objects.requireNonNull(companies.getBody()));
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new YahooFinanceException(YAHOO_FINANCE_COMMUNICATION_ERROR, e, HttpStatus.NOT_FOUND);
        }
    }
}
