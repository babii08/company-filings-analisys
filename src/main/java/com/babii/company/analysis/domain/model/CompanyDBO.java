package com.babii.company.analysis.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Builder
@Entity
@Table(name = "Company")
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDBO {

    @Id
    @Column(name = "CIK")
    long cik;

    @Column(name = "SYMBOL")
    String symbol;

}
