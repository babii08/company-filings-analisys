package com.babii.company.analysis.controller;

import com.babii.company.analysis.domain.mapper.CompanyMapper;
import com.babii.company.analysis.dto.Company;
import com.babii.company.analysis.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/companies/")
public class CompanyController {

    private final CompanyService companyService;

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

    @GetMapping("/all")
    public ResponseEntity<List<Company>> getAllCompanies() {
        List<Company> companies = companyService.getAllCompanies()
                .stream()
                .map(CompanyMapper::companyOf)
                .collect(Collectors.toList());
        return ok().body(companies);
    }

    @PostMapping("name/save")
    public ResponseEntity<List<Company>> saveCompaniesName() {
        List<Company> companies = companyService.saveCompaniesName()
                .stream()
                .map(CompanyMapper::companyOf)
                .collect(Collectors.toList());
        return ok().body(companies);
    }
}

