package com.babii.company.analysis.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
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
