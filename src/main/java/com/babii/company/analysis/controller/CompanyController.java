package com.babii.company.analysis.controller;

import com.babii.company.analysis.domain.mapper.CompanyMapper;
import com.babii.company.analysis.dto.BalanceSheet;
import com.babii.company.analysis.dto.Company;
import com.babii.company.analysis.service.CompanyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/companies/")
public class CompanyController {

    private CompanyService companyService;

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

    @PostMapping("balance-sheet/save/all")
    public ResponseEntity<List<BalanceSheet>> saveAllBalanceSheets() {
        List<BalanceSheet> balanceSheets = companyService.saveBalanceSheets()
                .stream()
                .map(CompanyMapper::balanceSheetOf)
                .collect(Collectors.toList());
        return ok().body(balanceSheets);
    }

    @GetMapping("/all")
    public ResponseEntity<List<String>> getAllCompanies() {
//        List<Company> companies = companyService.getAllCompanies()
//                .stream()
//                .map(CompanyMapper::companyOf)
//                .collect(Collectors.toList());
        List<String> companies = Arrays.asList("aa", "bb");
        return ok().body(companies);
    }

}

