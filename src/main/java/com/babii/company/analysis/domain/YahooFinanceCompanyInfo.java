package com.babii.company.analysis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YahooFinanceCompanyInfo {
    @JsonProperty("symbol")
    String symbol;
    @JsonProperty("name")
    String name;
    @JsonProperty("date")
    String date;
    @JsonProperty("isEnabled")
    boolean isEnabled;
    @JsonProperty("type")
    String type;
    @JsonProperty("iexId")
    String iexId;

}
