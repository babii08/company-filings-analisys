package com.babii.company.analysis.controller;

import com.babii.company.analysis.domain.mapper.CompanyMapper;
import com.babii.company.analysis.domain.model.CompanyDBO;
import com.babii.company.analysis.dto.Company;
import com.babii.company.analysis.service.CompanyService;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/companies/")
public class CompanyController {

    CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("cik/save")
    public ResponseEntity<List<Company>> saveCIKDataCompanies() {
        List<Company> companies = companyService.saveAllSymbolsWithCIK()
            .stream()
            .map(CompanyMapper::companyOf)
            .collect(Collectors.toList());
             return ok().body(companies);
    }

//    @PostMapping("balance-sheet/save/all")
//    public ResponseEntity<List<CompanyDBO>> saveAllBalanceSheets() {
//        return (ResponseEntity<List<CompanyDBO>>) ResponseEntity.ok();
//    }
}

