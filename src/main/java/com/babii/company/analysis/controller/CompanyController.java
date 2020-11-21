package com.babii.company.analysis.controller;

import com.babii.company.analysis.domain.mapper.CompanyMapper;
import com.babii.company.analysis.dto.Company;
import com.babii.company.analysis.service.CIKService;
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

    CIKService cikService;

    public CompanyController(CIKService cikService) {
        this.cikService = cikService;
    }

    @PostMapping("cik/save")
    public ResponseEntity<List<Company>> saveCIKDataCompanies() {
        List<Company> companies = cikService.saveAllSymbolsWithCIK()
            .stream()
            .map(CompanyMapper::companyOf)
            .collect(Collectors.toList());
             return ok().body(companies);
    }

}

