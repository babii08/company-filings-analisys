package com.babii.company.analysis.repository;

import com.babii.company.analysis.domain.model.BalanceSheetDBO;
import com.babii.company.analysis.domain.model.CompanyDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface BalanceSheetRepository extends JpaRepository<BalanceSheetDBO, Long> {

    List<BalanceSheetDBO> findByCashIsNull();
    Set<BalanceSheetDBO> findByCompany(CompanyDBO companyDBO);
}
