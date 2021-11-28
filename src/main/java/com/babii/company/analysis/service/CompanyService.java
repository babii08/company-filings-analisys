package com.babii.company.analysis.service;

import com.babii.company.analysis.domain.Result;
import com.babii.company.analysis.domain.YahooFinanceCompanyInfo;
import com.babii.company.analysis.domain.model.CompanyDBO;
import com.babii.company.analysis.exception.DocumentException;
import com.babii.company.analysis.repository.CompanyRepository;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.babii.company.analysis.util.Constants.DOCUMENT_NOT_FOUND;
import static com.babii.company.analysis.util.Constants.SYMBOL_CIK_MAP_REGEX;
import static com.babii.company.analysis.util.Constants.SYMBOL_CIK_MAP_URL;
import static com.babii.company.analysis.util.Util.getDocument;
import static com.babii.company.analysis.util.Util.getLongValue;

@Service
@Transactional
public class CompanyService {

    private Logger logger = LoggerFactory.getLogger(CompanyService.class);
    private CompanyRepository companyRepository;
    private YahooFinanceService yahooFinanceService;

    public CompanyService(CompanyRepository companyRepository,
                          YahooFinanceService yahooFinanceService) {
        this.companyRepository = companyRepository;
        this.yahooFinanceService = yahooFinanceService;
    }

    public Collection<CompanyDBO> saveAllSymbolsWithCIK() {
        Document document = getDocument(SYMBOL_CIK_MAP_URL);
        if (document == null) {
            throw new DocumentException(DOCUMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
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

    public Collection<CompanyDBO> populateCompanyWithNames() {
        String line;
        List<CompanyDBO> updatedCompanies = new ArrayList<>();
        String path = "src/main/resources/secwiki_tickers.csv";
        File file = new File(path);
        String absolutePath = file.getAbsolutePath();
        try (BufferedReader br = new BufferedReader(new FileReader(absolutePath))) {
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                companyRepository.findBySymbol(values[0].toLowerCase()).ifPresent(company ->
                    updatedCompanies.add(company.toBuilder()
                            .name(values[1])
                            .sector(values[2])
                            .industry(values[3]).build()));
            }
        } catch (IOException e) {
            logger.error("Could not read line from file");
        }
        return companyRepository.saveAll(updatedCompanies);
    }

    public Collection<CompanyDBO> getAllCompanies() {
        return companyRepository.findAll();
    }

    public Collection<CompanyDBO> saveCompaniesName() {
        List<YahooFinanceCompanyInfo> companies = yahooFinanceService.getCompaniesWithSymbolAndName();
        return companies
                .stream()
                .map(this::updateCompany)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private CompanyDBO updateCompany(YahooFinanceCompanyInfo company) {
        String lowerCaseCompany = company.getSymbol() != null ? company.getSymbol().toLowerCase() : company.getSymbol();
        Optional<CompanyDBO> optionalSavedCompany = companyRepository.findBySymbol(lowerCaseCompany);
        if (optionalSavedCompany.isPresent()) {
            CompanyDBO savedCompany = optionalSavedCompany.get();
            savedCompany.setName(company.getName());
            return companyRepository.save(savedCompany);
        } else {
            return null;
        }
    }
}

