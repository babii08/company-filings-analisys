package com.babii.company.analysis;

import com.babii.company.analysis.controller.CompanyController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AnalysisApplicationTests {

	@Autowired
	CompanyController companyController;

	@Test
	void contextLoads() {
		assertThat(companyController).isNotNull();
	}

}
