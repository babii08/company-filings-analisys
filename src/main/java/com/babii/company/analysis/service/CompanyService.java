package com.babii.company.analysis.service;

import com.babii.company.analysis.domain.model.BalanceSheetDBO;
import com.babii.company.analysis.domain.model.CompanyDBO;
import com.babii.company.analysis.exception.JsoupException;
import com.babii.company.analysis.repository.CompanyRepository;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.babii.company.analysis.util.Constants.*;
import static com.babii.company.analysis.util.Util.*;

@Service
public class CompanyService {

    Logger logger = LoggerFactory.getLogger(CompanyService.class);
    CompanyRepository companyRepository;
    private int zol = 0;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Bean
    public Collection<CompanyDBO> saveAllSymbolsWithCIK() {
        Document document = getDocument(SYMBOL_CIK_MAP_URL);
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

    @Bean
    public Collection<BalanceSheetDBO> saveAllBalanceSheets() {
        List<CompanyDBO> companies = companyRepository.findAll();
        List<BalanceSheetDBO> balancesheets = companies
                .stream()
                .map(this::saveBalanceSheetFor)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return Collections.emptyList();
    }

    private List<BalanceSheetDBO> saveBalanceSheetFor(CompanyDBO company) {
        String link = FIRST_PART_LINK_10Q + company.getCik() + SECOND_PART_LINK_10Q;
        Document document = getDocument(link);
        Element city = document.getElementsByTag("city").first();
        Element state = document.getElementsByTag("state").first();
        Element street = document.getElementsByTag("street1").first();
        Element phone = document.getElementsByTag("phone").first();

        company.toBuilder()
                .city(city.text())
                .state(state.text())
                .street(street.text())
                .phone(phone.text())
                .build();
        System.out.println(company.getSymbol());

        Elements interactiveData = document.getElementsByTag(TAG_TO_INTERACTIVE_10Q);

        List<BalanceSheetDBO> balanceSheets = interactiveData
                .stream()
                .map(this::getIntoInteractiveDataPage)
                .collect(Collectors.toList());

        return Collections.emptyList();
    }

    private BalanceSheetDBO getIntoInteractiveDataPage(Element element) {
        Document document = getDocument(element.text());
        Element header = document.head();
        Elements scriptTags = header.getElementsByTag("script");
        List<String> documentLinks = scriptTags
                .stream()
                .map(Element::toString)
                .filter(text -> text.contains("reports["))
                .map(text -> text.replace("\n", ""))
                .map(this::getLinksForDocument)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        Optional<Document> balanceSheetDocument = documentLinks
                .stream()
                .map(this::getDocument)
                .filter(this::isBalanceSheetDocument)
                .findFirst();

        if (balanceSheetDocument.isPresent()) {
            System.out.println(++zol);
            return addInfoForBalanceSheet(balanceSheetDocument.get());
        }
        return null;
    }

    private BalanceSheetDBO addInfoForBalanceSheet(Document document) {
        BalanceSheetDBO balanceSheet = new BalanceSheetDBO();
        Elements tableData = document.getElementsByTag("td");
        List<String> tableDataText = tableData
                .stream()
                .map(Element::text)
                .collect(Collectors.toList());
        int tableDataSize = tableDataText.size();
        for(int i=0; i<tableDataSize-1; i++) {
            String lowercaseString = tableDataText.get(i) != null ? tableDataText.get(i).toLowerCase() : "";
            if ("".equals(lowercaseString)) {
                continue;
            }
            if(lowercaseString.contains(CASH) && lowercaseString.contains(CASH_EQUIVALENTS) &&
                    balanceSheet.getCashAndEquivalents() == null) {
                balanceSheet.setCashAndEquivalents(getNrFromDocument(tableDataText.get(i+1)));
                continue;
            }
            if (lowercaseString.contains(TOTAL) && lowercaseString.contains(CURRENT_ASSETS) &&
                    !lowercaseString.contains(NON) && balanceSheet.getTotalCurrentAssets() == null) {
                balanceSheet.setTotalCurrentAssets(getNrFromDocument(tableDataText.get(i+1)));
                continue;
            }
            if (lowercaseString.contains(GOODWILL) && balanceSheet.getGoodwill() == null) {
                balanceSheet.setGoodwill(getNrFromDocument(tableDataText.get(i+1)));
                continue;
            }
            if (lowercaseString.contains(TOTAL) && lowercaseString.contains(ASSETS) &&
                    balanceSheet.getTotalAssets() == null) {
                balanceSheet.setTotalAssets(getNrFromDocument(tableDataText.get(i+1)));
                continue;
            }
            if (lowercaseString.contains(ACCOUNTS_PAYABLE) &&
                    balanceSheet.getAccountsPayable() == null) {
                balanceSheet.setAccountsPayable(getNrFromDocument(tableDataText.get(i+1)));
                continue;
            }
            if (lowercaseString.contains(TOTAL) && lowercaseString.contains(CURRENT_LIABILITIES)
                    && balanceSheet.getTotalCurrentLiabilities() == null) {
                balanceSheet.setTotalCurrentLiabilities(getNrFromDocument(tableDataText.get(i+1)));
                continue;
            }
            if (lowercaseString.contains(TOTAL) && lowercaseString.contains(LIABILITIES) &&
                    !lowercaseString.contains(NON) && balanceSheet.getTotalLiabilities() == null) {
                balanceSheet.setTotalLiabilities(getNrFromDocument(tableDataText.get(i+1)));
                continue;
            }
            if (lowercaseString.contains(COMMON_STOCK) && balanceSheet.getCommonStock() == null) {
                balanceSheet.setCommonStock(getNrFromDocument(tableDataText.get(i+1)));
                continue;
            }
            if (lowercaseString.contains(SHAREHOLDERS_EQUITY) && balanceSheet.getShareholdersEquity() == null) {
                balanceSheet.setShareholdersEquity(getNrFromDocument(tableDataText.get(i+1)));
                continue;
            }
        }
        return balanceSheet;
    }

    private BigDecimal getNrFromDocument(String number){
        return getNumber(number.replaceAll("\\D+",""));
    }

    private boolean isBalanceSheetDocument(Document document) {
        Elements tableHead = document.getElementsByTag("th");
        String text = null;
        if (tableHead != null) {
            text = tableHead.get(0).text();
        }
        if (text != null && text.contains("BALANCE") && text.contains("SHEET")) {
            return true;
        } else {
            return false;
        }
    }

    private List<String> getLinksForDocument(String text) {
        Pattern p = Pattern.compile(DOCUMENTS_LINKS_REGEX);
        Matcher m = p.matcher(text);
        List<String> links = new ArrayList<>();
        while (m.find()) {
            links.add(EDGAR_LINK + m.group());
        }
        return links;
    }

    private Document getDocument(String link) {
        return getJsoupDocument(link).orElseThrow(() -> new JsoupException(ERROR_DATA_NOT_FOUND + link));
    }
}

