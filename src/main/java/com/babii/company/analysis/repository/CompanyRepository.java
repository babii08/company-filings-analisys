package com.babii.company.analysis.repository;

import com.babii.company.analysis.domain.model.CompanyDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<CompanyDBO, Long> {

    Optional<CompanyDBO> findBySymbol(String symbol);
}
