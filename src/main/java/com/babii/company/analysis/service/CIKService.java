package com.babii.company.analysis.service;

import com.babii.company.analysis.domain.model.CompanyDBO;
import com.babii.company.analysis.repository.CompanyRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.babii.company.analysis.util.Constants.SYMBOL_CIK_MAP_REGEX;
import static com.babii.company.analysis.util.Constants.SYMBOL_CIK_MAP_URL;
import static com.babii.company.analysis.util.Util.getLongValue;

@Service
public class CIKService {

    Logger logger = LoggerFactory.getLogger(CIKService.class);
    CompanyRepository companyRepository;

    public CIKService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Collection<CompanyDBO> saveAllSymbolsWithCIK() {
        Document document;
        try {
            document = Jsoup.connect(SYMBOL_CIK_MAP_URL).userAgent("Mozilla").get();
        } catch (IOException e) {
            logger.error(e.getMessage(), "Could not get information from given link. Error message: {}");
            return Collections.emptyList();
        }
        String entireString = document.body().text();
        List<String> companyRows = Arrays.asList(entireString.split(SYMBOL_CIK_MAP_REGEX));
        Collection<CompanyDBO> companies = companyRows
                .stream()
                .map(company -> saveCompany(Arrays.asList(company.split(" "))))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return companyRepository.saveAll(companies);
    }

    private CompanyDBO saveCompany(List<String> companyInfo) {
        if (companyInfo.size() < 2) {
            String stringList = Arrays.toString(companyInfo.toArray());
            logger.error(stringList, "Given list less than size 2. Given list: {}");
        } else {
            Optional<Long> cik = getLongValue(companyInfo.get(1));
            if (cik.isPresent()) {
                return CompanyDBO.builder()
                        .cik(cik.get())
                        .symbol(companyInfo.get(0))
                        .build();
            }
        }
        return null;
    }
}

