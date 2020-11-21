package com.babii.company.analysis.domain.mapper;

import com.babii.company.analysis.domain.model.CompanyDBO;
import com.babii.company.analysis.dto.Company;

public class CompanyMapper {

    private CompanyMapper(){}

    public static Company companyOf(CompanyDBO companyDBO) {
        return Company.builder()
                .cik(companyDBO.getCik())
                .symbol(companyDBO.getSymbol())
                .build();
    }
}
