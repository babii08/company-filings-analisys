package com.babii.company.analysis.util;

public class Constants {

    private Constants() {}
    public static final String SYMBOL_CIK_MAP_URL = "https://www.sec.gov/include/ticker.txt";
    public static final String SYMBOL_CIK_MAP_REGEX = "(?<=\\d )";
    public static final String FIRST_PART_LINK_10Q = "https://www.sec.gov/cgi-bin/browse-edgar?action=getcompany&CIK=";
    public static final String SECOND_PART_LINK_10Q = "&type=10-Q&dateb=&owner=include&count=100&search_text=&output=atom";
    public static final String TAG_TO_INTERACTIVE_10Q = "xbrl_href";
    public static final String DOCUMENTS_LINKS_REGEX = "/Archives/edgar/data/(.+?)htm";
    public static final String EDGAR_LINK = "https://www.sec.gov/";
    public static final String ERROR_DATA_NOT_FOUND = "IOException. Data at given link not found: ";
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
    public static final String SHAREHOLDERS_EQUITY = "shareholders' equity";
    public static final String STOCKHOLDERS_EQUITY = "stockholders' equity";
}
