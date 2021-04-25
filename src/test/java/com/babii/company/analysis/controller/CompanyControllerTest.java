//package com.babii.company.analysis.controller;
//
//import com.babii.company.analysis.domain.model.BalanceSheetDBO;
//import com.babii.company.analysis.domain.model.CompanyDBO;
//import com.babii.company.analysis.service.CompanyService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//import static org.hamcrest.Matchers.hasSize;
//import static org.hamcrest.Matchers.is;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(CompanyController.class)
//class CompanyControllerTest {
//
//    @Autowired
//    private MockMvc mvc;
//
//    @MockBean
//    private CompanyService companyService;
//
//    private List<CompanyDBO> companies;
//    private List<BalanceSheetDBO> balanceSheets;
//
//    @BeforeEach
//    public void setup() {
//        companies = List.of(CompanyDBO.builder()
//                .cik(12345678)
//                .symbol("aaa")
//                .build());
//        balanceSheets = List.of(BalanceSheetDBO.builder()
//                .id(1111)
//                .cash(BigDecimal.valueOf(100))
//                .totalCurrentLiabilities(BigDecimal.valueOf(1000))
//                .company(companies.get(0))
//                .build());
//    }
//
//    @Test
//    void givenCompanyPostRequest_whenExtractingAllCompaniesFromLink_thenReturnSavedCompanies() throws Exception{
//        when(companyService.saveAllSymbolsWithCIK()).thenReturn(companies);
//        mvc.perform(post("/api/companies/cik/save")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].symbol", is(companies.get(0).getSymbol())));
//    }
//
//    @Test
//    void givenCompanyPostRequest_whenExtractingAllDataFromSecDB_thenReturnSavedCompanies() throws Exception{
//        when(companyService.saveBalanceSheets()).thenReturn(balanceSheets);
//        mvc.perform(post("/api/companies/balance-sheet/save/all")
//                .contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(1)))
//                .andExpect(jsonPath("$[0].cashAndEquivalents", is(balanceSheets.get(0).getCash().intValue())));
//    }
//
//
//}
