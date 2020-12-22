package com.babii.company.analysis.repository;

import com.babii.company.analysis.domain.model.LinkInfoDBO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LinkInfoRepository extends JpaRepository<LinkInfoDBO, Long> {

}
