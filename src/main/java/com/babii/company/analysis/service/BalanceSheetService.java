package com.babii.company.analysis.service;

import com.babii.company.analysis.domain.mapper.CompanyMapper;
import com.babii.company.analysis.domain.model.BalanceSheetDBO;
import com.babii.company.analysis.domain.model.CompanyDBO;
import com.babii.company.analysis.domain.model.LinkInfoDBO;
import com.babii.company.analysis.domain.model.Quarter;
import com.babii.company.analysis.dto.BalanceSheet;
import com.babii.company.analysis.dto.Company;
import com.babii.company.analysis.exception.DocumentException;
import com.babii.company.analysis.exception.JsoupException;
import com.babii.company.analysis.repository.BalanceSheetRepository;
import com.babii.company.analysis.repository.CompanyRepository;
import com.babii.company.analysis.repository.LinkInfoRepository;
import com.babii.company.analysis.util.Util;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.babii.company.analysis.domain.mapper.CompanyMapper.balanceSheetOf;
import static com.babii.company.analysis.domain.model.Quarter.UNKNOWN;
import static com.babii.company.analysis.domain.model.Quarter.getQuarter;
import static com.babii.company.analysis.util.Constants.ABSOLUTE_LINK;
import static com.babii.company.analysis.util.Constants.DOCUMENT_NOT_FOUND;
import static com.babii.company.analysis.util.Constants.EDGAR_ARCHIVES_LINK;
import static com.babii.company.analysis.util.Constants.FILE_LOCATION;
import static com.babii.company.analysis.util.Constants.TABLE_DATA_LINK;
import static com.babii.company.analysis.util.Constants.XBRL_FILE_NAME;
import static com.babii.company.analysis.util.Constants.YEARS_PAGE;
import static com.babii.company.analysis.util.Util.ending;
import static com.babii.company.analysis.util.Util.getDocument;
import static com.babii.company.analysis.util.Util.getNumber;
import static com.babii.company.analysis.util.Util.isAccountsPayable;
import static com.babii.company.analysis.util.Util.isCashValue;
import static com.babii.company.analysis.util.Util.isCommonStock;
import static com.babii.company.analysis.util.Util.isCurrentAssets;
import static com.babii.company.analysis.util.Util.isCurrentLiabilities;
import static com.babii.company.analysis.util.Util.isGoodWill;
import static com.babii.company.analysis.util.Util.isShareHolderEquity;
import static com.babii.company.analysis.util.Util.isTotalAssets;
import static com.babii.company.analysis.util.Util.isTotalLiabilities;
import static org.apache.logging.log4j.util.Strings.isBlank;

@Service
public class BalanceSheetService {
    private Logger logger = LoggerFactory.getLogger(BalanceSheetService.class);

    private BalanceSheetRepository balanceSheetRepository;
    private LinkInfoRepository linkInfoRepository;
    private CompanyRepository companyRepository;

    public BalanceSheetService(BalanceSheetRepository balanceSheetRepository,
                               LinkInfoRepository linkInfoRepository,
                               CompanyRepository companyRepository) {
        this.balanceSheetRepository = balanceSheetRepository;
        this.linkInfoRepository = linkInfoRepository;
        this.companyRepository = companyRepository;
    }

    public Collection<BalanceSheetDBO> extractBalanceSheetInformation() {
        List<BalanceSheetDBO> balanceSheets = balanceSheetRepository.findAll();
        return setFinancialInformation(balanceSheets);
    }

    public Collection<BalanceSheetDBO> saveBalanceSheets() {
        Document doc = getDocument(YEARS_PAGE);
        if (doc == null) {
            throw new DocumentException(DOCUMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Elements years = doc.select(TABLE_DATA_LINK);
        List<LinkInfoDBO> yearsLinks = years.stream()
                .filter(element -> isThisDecadeYear(element.text()))
                .map(element -> setLinkInfo(element.attr(ABSOLUTE_LINK), element.text()))
                .collect(Collectors.toList());

        List<LinkInfoDBO> documentsLinks = yearsLinks.stream()
                .map(this::getFilesWithData)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        documentsLinks = linkInfoRepository.saveAll(documentsLinks);

        List<BalanceSheetDBO> companyWithLink = documentsLinks.stream()
                .filter(this::saveFile)
                .map(this::getSavedFile)
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        companyWithLink = balanceSheetRepository.saveAll(companyWithLink);

        return setFinancialInformation(companyWithLink);
    }

    public List<BalanceSheetDBO> setFinancialInformation(List<BalanceSheetDBO> companies) {
        List<BalanceSheetDBO> balanceSheets = new ArrayList<>();
        companies.forEach(company -> {
            if (company.getLink() == null) {
                return;
            }
            String quarterLink = company.getLink().replace("-", "").replace(".txt", "/");
            List<String> potentialLinks = Stream.of(ending).map(end -> quarterLink + end).collect(Collectors.toList());
            Optional<Document> balanceSheetDoc = potentialLinks.stream()
                    .map(Util::getDocument)
                    .filter(Objects::nonNull)
                    .filter(this::isBalanceSheetDocument)
                    .findFirst();
            balanceSheetDoc.ifPresent(document -> {
                BalanceSheetDBO info;
                String valueType = document.getElementsByTag("th").get(0).text().toUpperCase();
                if (valueType.contains("MILLIONS")) {
                    info = addInfoForBalanceSheet(document, company, 1);
                } else if (valueType.contains("THOUSANDS")) {
                    info = addInfoForBalanceSheet(document, company, 1000);
                } else {
                    info = addInfoForBalanceSheet(document, company, 1000000);
                }
                balanceSheets.add(info);
            });
        });
        return balanceSheetRepository.saveAll(balanceSheets);
    }

    private LinkInfoDBO setLinkInfo(String link, String text) {
        int year = getNumber(text).intValue();
        return LinkInfoDBO.builder()
                .year(Year.of(year).atDay(1))
                .documentLink(link)
                .build();
    }

    private List<BalanceSheetDBO> getSavedFile(LinkInfoDBO linkInfo) {
        try {
            String content = new String(Files.readAllBytes(Paths.get(FILE_LOCATION + XBRL_FILE_NAME)));
            logger.info("Getting info from saved file");
            String[] context = content.split("--+");
            if (!areColumnAccordingToPattern(context[0])) {
                throw new DocumentException("file not according to pattern:" + context[0], HttpStatus.NOT_ACCEPTABLE);
            }
            List<String> rows = Arrays.asList(context[1].split("\\n"));
            return rows.stream()
                    .filter(row -> !"".equals(row))
                    .map(row -> getInfoFromRow(row, linkInfo))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Could not load saved file");
            return Collections.emptyList();
        }
    }

    private BalanceSheetDBO getInfoFromRow(String row, LinkInfoDBO linkInfoDBO) {
        List<String> info = Arrays.asList(row.split("\\|"));
        if (!"10-Q".equals(info.get(2))) {
            return null;
        }
        BigDecimal number = getNumber(info.get(0));
        if(number == null) {
            throw new JsoupException("Cik is null. Could not continue...");
        }
        List<String> datesString = Arrays.asList(info.get(3).split("-"));
        List<Integer> dates = datesString.stream()
                .filter(date -> getNumber(date) != null)
                .map(date -> getNumber(date).intValue())
                .collect(Collectors.toList());

        Optional<CompanyDBO> company = companyRepository.findById(number.longValue());
        if (company.isEmpty()) {
            return null;
        }

        return BalanceSheetDBO.builder()
                .company(company.get())
                .date(dates.size() == datesString.size() ? LocalDate.of(dates.get(0), dates.get(1), dates.get(2)) : null)
                .link(EDGAR_ARCHIVES_LINK + info.get(4))
                .quarter(linkInfoDBO.getQuarter())
                .build();

    }

    private boolean areColumnAccordingToPattern(String context) {
        String columns = context.substring(context.indexOf("CIK"), context.length()-1);
        List<String> columnsList = Arrays.asList(columns.split("\\|"));
        return columnsList.size() == 5 && "CIK".equals(columnsList.get(0)) && "Company Name".equals(columnsList.get(1))
                && "Form Type".equals(columnsList.get(2)) && "Date Filed".equals(columnsList.get(3))
                && "Filename".equals(columnsList.get(4));
    }

    private boolean saveFile(LinkInfoDBO linkInfo) {
        byte[] documentBytes;
        try(FileOutputStream fos = new FileOutputStream(FILE_LOCATION + XBRL_FILE_NAME)) {
            documentBytes = Jsoup.connect(linkInfo.getDocumentLink())
                    .userAgent("Mozilla")
                    .ignoreContentType(true)
                    .execute()
                    .bodyAsBytes();
            fos.write(documentBytes);
            logger.info("File was saved");
        } catch (IOException e) {
            logger.error(linkInfo.getDocumentLink(), "Could not save file at link: {}");
            return false;
        }
        return true;
    }

    private List<LinkInfoDBO> getFilesWithData(LinkInfoDBO yearLinks) {
        Document doc = getDocument(yearLinks.getDocumentLink());
        List<LinkInfoDBO> quarterLinks = new ArrayList<>();
        if (doc == null) {
            throw new DocumentException(DOCUMENT_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        Elements elements = doc.select(TABLE_DATA_LINK);
        elements.forEach(element -> {
            LinkInfoDBO qLink = yearLinks.clone();
            Quarter quarter = getCurrentQuarter(element.text());
            if (quarter.equals(UNKNOWN)) {
                qLink.setDocumentLink(null);
                return;
            }
            qLink.setQuarter(quarter);
            Document quarterLink = getDocument(element.attr(ABSOLUTE_LINK));
            if (quarterLink == null) {
                qLink.setDocumentLink(null);
                return;
            }
            Elements docLists = quarterLink.select(TABLE_DATA_LINK);
            Optional<String> fileLink = docLists.stream()
                    .map(this::getFullLinkOfXBRLElement)
                    .filter(Objects::nonNull)
                    .findFirst();
            fileLink.ifPresent(qLink::setDocumentLink);
            quarterLinks.add(qLink);
        });
        return quarterLinks;
    }

    private String getFullLinkOfXBRLElement(Element element) {
        if ("xbrl.idx".equals(element.text())) {
            return element.attr(ABSOLUTE_LINK);
        }
        return null;
    }

    private Quarter getCurrentQuarter(String text) {
        Optional<String> quarter =  Stream.of(Quarter.values()).map(Quarter::getText).filter(text::equals).findFirst();
        if(quarter.isPresent()) {
            return getQuarter(quarter.get());
        }
        return UNKNOWN;
    }

    private BalanceSheetDBO addInfoForBalanceSheet(Document document, BalanceSheetDBO balanceSheet, int divisionValue) {
        Elements tableaHeaders = document.getElementsByTag("th");
        tableaHeaders.forEach(header -> logger.info(header.text()));
        Elements tableData = document.getElementsByTag("td");
        List<String> tableDataText = tableData
                .stream()
                .map(Element::text)
                .collect(Collectors.toList());
        balanceSheet.setLink(document.location());
        int tableDataSize = tableDataText.size();
        int i=0;
        while(i < tableDataSize-1) {
            String valueString = tableDataText.get(i) != null ? tableDataText.get(i).toLowerCase() : "";
            if (isBlank(valueString)) {
                i++;
                continue;
            }
            if(isCashValue(valueString, balanceSheet)) {
                balanceSheet.setCash(getNrFromDocument(tableDataText.get(++i), divisionValue));
                continue;
            }
            if (isCurrentAssets(valueString, balanceSheet)) {
                balanceSheet.setTotalCurrentAssets(getNrFromDocument(tableDataText.get(++i), divisionValue));
                continue;
            }
            if (isGoodWill(valueString, balanceSheet)) {
                balanceSheet.setGoodwill(getNrFromDocument(tableDataText.get(++i), divisionValue));
                continue;
            }
            if (isTotalAssets(valueString, balanceSheet)) {
                balanceSheet.setTotalAssets(getNrFromDocument(tableDataText.get(++i), divisionValue));
                continue;
            }
            if (isAccountsPayable(valueString, balanceSheet)) {
                balanceSheet.setAccountsPayable(getNrFromDocument(tableDataText.get(++i), divisionValue));
                continue;
            }
            if (isCurrentLiabilities(valueString, balanceSheet)) {
                balanceSheet.setTotalCurrentLiabilities(getNrFromDocument(tableDataText.get(++i), divisionValue));
                continue;
            }
            if (isTotalLiabilities(valueString, balanceSheet)) {
                balanceSheet.setTotalLiabilities(getNrFromDocument(tableDataText.get(++i), divisionValue));
                continue;
            }
            if (isCommonStock(valueString, balanceSheet)) {
                balanceSheet.setCommonStock(getNrFromDocument(tableDataText.get(++i), divisionValue));
                continue;
            }
            if (isShareHolderEquity(valueString, balanceSheet)) {
                balanceSheet.setShareholdersEquity(getNrFromDocument(tableDataText.get(++i), divisionValue));
            }
            i++;
        }
        return balanceSheet;
    }

    private BigDecimal getNrFromDocument(String number, int divisionValue){
        BigDecimal value =  getNumber(number.replaceAll("\\D+",""));
        if (value != null) {
            return value.divide(BigDecimal.valueOf(divisionValue), 4, RoundingMode.HALF_UP);
        }
        return null;
    }

    private boolean isBalanceSheetDocument(Document document) {
        Elements tableHead = document.getElementsByTag("th");
        String text = null;
        if (tableHead != null) {
            text = tableHead.get(0).text();
        }
        return text != null &&
                (text.toUpperCase().contains("BALANCE") && (text.toUpperCase().contains("SHEET")) ||
                        text.toUpperCase().contains("SHEETS") || text.toUpperCase().contains("FINANCIAL CONDITION") ||
                        text.toUpperCase().contains("FINANCIAL POSITION"));
    }

    private boolean isThisDecadeYear(String text) {
        BigDecimal year = getNumber(text);
        return year != null && year.compareTo(BigDecimal.valueOf(2011L)) > 0;
    }

    public Collection<BalanceSheet> getBalanceSheetOf(String companySymbol) {
        CompanyDBO company = companyRepository.findBySymbol(companySymbol).orElseThrow(() -> new RuntimeException());
        Collection<BalanceSheetDBO> balanceDBOs = balanceSheetRepository.findByCompany(company);
        return balanceDBOs.stream()
                .map( CompanyMapper::balanceSheetOf)
                .collect(Collectors.toList());
    }
}
