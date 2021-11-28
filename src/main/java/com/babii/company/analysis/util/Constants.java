package com.babii.company.analysis.util;

public class Constants {

    private Constants() {}
    public static final String CASH = "cash";
    public static final String CASH_EQUIVALENTS = "cash equivalents";
    public static final String TOTAL = "total";
    public static final String NON = "non";
    public static final String CURRENT_ASSETS = "current assets";
    public static final String GOODWILL = "goodwill";
    public static final String ASSETS = "assets";
    public static final String ACCOUNTS_PAYABLE = "accounts payable";
    public static final String CURRENT_LIABILITIES = "current liabilities";
    public static final String LIABILITIES = "liabilities";
    public static final String COMMON_STOCK = "common stock";
    public static final String SHAREHOLDERS_EQUITY = "shareholders’ equity";
    public static final String STOCKHOLDERS_EQUITY = "stockholders’ equity";
    public static final String XBRL_FILE_NAME = "xbrl.idx";
    public static final String DOCUMENT_NOT_FOUND = "Document not found";
    public static final String LANG = "en-US";

    //-----------------Messages---------------------------//
    public static final String YAHOO_FINANCE_COMMUNICATION_ERROR = " Error when trying to access yahoo endpoint";

    //-----------------Regex-----------------------------//
    public static final String SYMBOL_CIK_MAP_REGEX = "(?<=\\d )";

    // ----------------Links----------------------------//
//    public static final String NAME_SYMBOL_INFO = "http://d.yimg.com/aq/autoc";
    public static final String NAME_SYMBOL_INFO = "https://api.iextrading.com/1.0/ref-data/symbols";
    public static final String YEARS_PAGE = "https://www.sec.gov/Archives/edgar/full-index/";
    public static final String SYMBOL_CIK_MAP_URL = "https://www.sec.gov/include/ticker.txt";
    public static final String EDGAR_ARCHIVES_LINK = "https://www.sec.gov//Archives/";
    public static final String FILE_LOCATION = "C:\\Users\\Andrian\\company analysis\\company-filings-analisys\\src\\main\\resources\\";

    //-----------------Paths---------------------------//
    public static final String TABLE_DATA_LINK = "td > a[href]";
    public static final String ABSOLUTE_LINK = "abs:href";
}
