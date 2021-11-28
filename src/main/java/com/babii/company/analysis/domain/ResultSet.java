package com.babii.company.analysis.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultSet {

    @JsonProperty("Query")
    String query;
    @JsonProperty("Result")
    List<Result> result;
}
