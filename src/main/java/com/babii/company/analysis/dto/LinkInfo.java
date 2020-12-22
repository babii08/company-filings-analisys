package com.babii.company.analysis.dto;

import com.babii.company.analysis.domain.model.Quarter;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Deque;

@Getter
@Setter
@Builder(toBuilder = true)
public class LinkInfo {

    Quarter quarter;
    LocalDate year;
    String documentLink;
}
