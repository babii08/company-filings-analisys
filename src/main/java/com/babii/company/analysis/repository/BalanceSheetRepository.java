package com.babii.company.analysis.repository;

import com.babii.company.analysis.domain.model.BalanceSheetDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceSheetRepository extends JpaRepository<BalanceSheetDBO, Long> {

}
