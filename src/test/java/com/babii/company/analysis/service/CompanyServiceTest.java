package com.babii.company.analysis.service;

import com.babii.company.analysis.domain.model.BalanceSheetDBO;
import com.babii.company.analysis.domain.model.CompanyDBO;
import com.babii.company.analysis.domain.model.LinkInfoDBO;
import com.babii.company.analysis.domain.model.Quarter;
import com.babii.company.analysis.repository.BalanceSheetRepository;
import com.babii.company.analysis.repository.CompanyRepository;
import com.babii.company.analysis.repository.LinkInfoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private BalanceSheetRepository balanceSheetRepository;
    @Mock
    private LinkInfoRepository linkInfoRepository;
    @InjectMocks
    private CompanyService companyService;

    private List<CompanyDBO> companies;
    private List<BalanceSheetDBO> balanceSheets;
    private List<LinkInfoDBO> links;

    @BeforeEach
    void setup() {
        LocalDate currentDate = LocalDate.now();
        String link = "https://www.sec.gov/Archives/edgar/full-index/2012/QTR1/xbrl.idx";
        companies = List.of(CompanyDBO.builder()
                .cik(1111)
                .symbol("aap").build());
        balanceSheets = List.of(BalanceSheetDBO.builder()
                .cash(BigDecimal.valueOf(111))
                .totalCurrentAssets(BigDecimal.valueOf(112))
                .goodwill(BigDecimal.valueOf(113))
                .totalAssets(BigDecimal.valueOf(114))
                .accountsPayable(BigDecimal.valueOf(115))
                .totalCurrentLiabilities(BigDecimal.valueOf(116))
                .totalLiabilities(BigDecimal.valueOf(117))
                .quarter(Quarter.Q1)
                .link(link).build());
        links = List.of(LinkInfoDBO.builder()
                .quarter(Quarter.Q1)
                .year(currentDate)
                .documentLink(link).build());
    }

    @Test
    void whenSaveCompaniesCIKRequest_thenSaveCompaniesInfoToDB() {
        when(companyRepository.saveAll(anyIterable())).thenReturn(companies);
        List<CompanyDBO> result = (List<CompanyDBO>) companyService.saveAllSymbolsWithCIK();
        assertThat(result.get(0)).isEqualTo(companies.get(0));
        verify(companyRepository, times(1)).saveAll(anyIterable());
    }

    @Test
    void whenPopulateCompaniesWithNameRequest_thenSaveNamesForCompaniesInCaseExists() {
        when(companyRepository.findBySymbol(any())).thenReturn(Optional.ofNullable(companies.get(0)));
        when(companyRepository.saveAll(anyIterable())).thenReturn(companies);
        assertThat(companies.size()).isEqualTo(companyService.populateCompanyWithNames().size());
        verify(companyRepository, atLeast(1)).findBySymbol(any());
    }

    @Test
    void whenSaveBalanceSheetsRequest_thenSaveInfoToDB() {
        when(linkInfoRepository.saveAll(anyIterable())).thenReturn(links);
        when(balanceSheetRepository.saveAll(anyIterable())).thenReturn(balanceSheets);
        assertThat(companyService.saveBalanceSheets()).isNotNull();
    }
}
