package com.babii.company.analysis.controller;

import com.babii.company.analysis.domain.mapper.CompanyMapper;
import com.babii.company.analysis.dto.BalanceSheet;
import com.babii.company.analysis.service.BalanceSheetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/balance-sheet/")
public class BalanceSheetController {

    BalanceSheetService balanceSheetService;

    public BalanceSheetController(BalanceSheetService balanceSheetService) {
        this.balanceSheetService = balanceSheetService;
    }

    @PostMapping("save/all")
    public ResponseEntity<List<BalanceSheet>> saveAllBalanceSheets() {
        List<BalanceSheet> balanceSheets = balanceSheetService.saveBalanceSheets()
                .stream()
                .map(CompanyMapper::balanceSheetOf)
                .collect(Collectors.toList());
        return ok().body(balanceSheets);
    }

    @PostMapping("extract/all")
    public ResponseEntity<List<BalanceSheet>> extractBalanceSheetInfo() {
        List<BalanceSheet> balanceSheets = balanceSheetService.extractBalanceSheetInformation()
                .stream()
                .map(CompanyMapper::balanceSheetOf)
                .collect(Collectors.toList());
        return ok().body(balanceSheets);
    }

    @GetMapping("/get/{company}")
    public ResponseEntity<Collection<BalanceSheet>> getCompanyBalanceSheet(@PathVariable String company) {
        return ok().body(balanceSheetService.getBalanceSheetOf(company));
    }
}
