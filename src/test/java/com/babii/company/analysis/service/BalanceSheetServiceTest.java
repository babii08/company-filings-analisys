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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyIterable;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BalanceSheetServiceTest {

    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private BalanceSheetRepository balanceSheetRepository;
    @Mock
    private LinkInfoRepository linkInfoRepository;
    @InjectMocks
    private BalanceSheetService balanceSheetService;

    private List<CompanyDBO> companies;
    private List<BalanceSheetDBO> balanceSheets;
    private List<LinkInfoDBO> links;

    @BeforeEach
    void setup() {
        LocalDate currentDate = LocalDate.now();
        companies = List.of(CompanyDBO.builder()
                .cik(1000045)
                .symbol("aap").build());
        balanceSheets = List.of(BalanceSheetDBO.builder()
                .quarter(Quarter.Q1)
                .link("https://www.sec.gov/Archives/edgar/data/791908/0001140361-13-019452.txt").build());
        links = List.of(LinkInfoDBO.builder()
                .quarter(Quarter.Q1)
                .year(currentDate)
                .documentLink("https://www.sec.gov/Archives/edgar/full-index/2020/QTR3/xbrl.idx").build());
    }

    @Test
    void whenSaveBalanceSheetsRequest_thenSaveInfoToDB() {
        when(linkInfoRepository.saveAll(anyIterable())).thenReturn(links);
        when(companyRepository.findById(any())).thenReturn(Optional.ofNullable(companies.get(0)));
        when(balanceSheetRepository.saveAll(anyIterable())).thenReturn(balanceSheets);
        assertThat(balanceSheetService.saveBalanceSheets()).hasSize(1);
    }

}
