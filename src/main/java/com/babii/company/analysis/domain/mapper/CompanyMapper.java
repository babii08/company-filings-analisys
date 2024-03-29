package com.babii.company.analysis.domain.mapper;

import com.babii.company.analysis.domain.model.BalanceSheetDBO;
import com.babii.company.analysis.domain.model.CompanyDBO;
import com.babii.company.analysis.dto.BalanceSheet;
import com.babii.company.analysis.dto.Company;
public class CompanyMapper {

    private CompanyMapper(){}

    public static Company companyOf(CompanyDBO companyDBO) {
        return Company.builder()
                .cik(companyDBO.getCik())
                .symbol(companyDBO.getSymbol())
                .city(companyDBO.getCity())
                .name(companyDBO.getName())
                .phone(companyDBO.getPhone())
                .industry(companyDBO.getIndustry())
                .sector(companyDBO.getSector())
                .state(companyDBO.getState())
                .street(companyDBO.getStreet())
                .build();
    }

    public static BalanceSheet balanceSheetOf(BalanceSheetDBO balanceSheetDBO) {
        return BalanceSheet.builder()
                .id(balanceSheetDBO.getId())
                .cashAndEquivalents(balanceSheetDBO.getCash())
                .totalCurrentAssets(balanceSheetDBO.getTotalCurrentAssets())
                .goodwill(balanceSheetDBO.getGoodwill())
                .totalAssets(balanceSheetDBO.getTotalAssets())
                .accountsPayable(balanceSheetDBO.getAccountsPayable())
                .totalCurrentLiabilities(balanceSheetDBO.getTotalCurrentLiabilities())
                .totalLiabilities(balanceSheetDBO.getTotalLiabilities())
                .commonStock(balanceSheetDBO.getCommonStock())
                .shareholdersEquity(balanceSheetDBO.getShareholdersEquity())
                .totalLiabilitiesAndEquity(balanceSheetDBO.getTotalLiabilitiesAndEquity())
                .companyCik(balanceSheetDBO.getCompany().getCik())
                .date(balanceSheetDBO.getDate())
                .quarter(balanceSheetDBO.getQuarter())
                .link(balanceSheetDBO.getLink())
                .build();
    }
}
