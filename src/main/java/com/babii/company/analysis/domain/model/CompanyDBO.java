package com.babii.company.analysis.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "Company")
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDBO {

    @Id
    @Column(name = "CIK")
    private long cik;

    @Column(name = "SYMBOL")
    private String symbol;

    @Column(name = "CITY")
    private String city;

    @Column(name = "STATE")
    private String state;

    @Column(name = "STREET")
    private String street;

    @Column(name = "PHONE")
    private String phone;

    @OneToMany(
            mappedBy = "company",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BalanceSheetDBO> balanceSheets = new ArrayList<>();
}
