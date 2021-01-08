package com.babii.company.analysis.util;

import com.babii.company.analysis.domain.model.BalanceSheetDBO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

import static com.babii.company.analysis.util.Constants.*;

public class Util {

    private static Logger logger = LoggerFactory.getLogger(Util.class);
    public static final String[] ending = new String[] {"R1.htm", "R2.htm", "R3.htm", "R4.htm", "R5.htm", "R6.htm", "R7.htm", "R8.htm",
            "R9.htm", "R10.htm"};
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
            return null;
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

    public static boolean isCashValue(String valueString, BalanceSheetDBO balanceSheet) {
        return (valueString.contains(CASH) || valueString.contains(CASH_EQUIVALENTS)) &&
                balanceSheet.getCash() == null;
    }

    public static boolean isCurrentAssets(String valueString, BalanceSheetDBO balanceSheet) {
        return valueString.contains(TOTAL) && valueString.contains(CURRENT_ASSETS) &&
                !valueString.contains(NON) && balanceSheet.getTotalCurrentAssets() == null;
    }

    public static boolean isGoodWill(String valueString, BalanceSheetDBO balanceSheet) {
        return valueString.contains(GOODWILL) && balanceSheet.getGoodwill() == null;
    }

    public static boolean isTotalAssets(String valueString, BalanceSheetDBO balanceSheet) {
        return valueString.contains(TOTAL) && valueString.contains(ASSETS) &&
                balanceSheet.getTotalAssets() == null;
    }

    public static boolean isAccountsPayable(String valueString, BalanceSheetDBO balanceSheet) {
        return valueString.contains(ACCOUNTS_PAYABLE) && balanceSheet.getAccountsPayable() == null;
    }

    public static boolean isCurrentLiabilities(String valueString, BalanceSheetDBO balanceSheet) {
        return valueString.contains(TOTAL) && valueString.contains(CURRENT_LIABILITIES)
                && balanceSheet.getTotalCurrentLiabilities() == null;
    }

    public static boolean isTotalLiabilities(String valueString, BalanceSheetDBO balanceSheet) {
        return valueString.contains(TOTAL) && valueString.contains(LIABILITIES) &&
                !valueString.contains(NON) && balanceSheet.getTotalLiabilities() == null;
    }

    public static boolean isCommonStock(String valueString, BalanceSheetDBO balanceSheet) {
        return valueString.contains(COMMON_STOCK) && balanceSheet.getCommonStock() == null;
    }

    public static boolean isShareHolderEquity(String valueString, BalanceSheetDBO balanceSheet) {
        return valueString.contains(SHAREHOLDERS_EQUITY) || valueString.contains(STOCKHOLDERS_EQUITY)
                && balanceSheet.getShareholdersEquity() == null;
    }
}
