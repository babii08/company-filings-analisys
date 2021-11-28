package com.babii.company.analysis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result {

    @JsonProperty("symbol")
    private String symbol;
    @JsonProperty("name")
    private String name;
    @JsonProperty("exch")
    private String exch;
    @JsonProperty("type")
    private String type;
    @JsonProperty("exchDisp")
    private String exchDisp;
    @JsonProperty("typeDisp")
    private String typeDisp;
}
