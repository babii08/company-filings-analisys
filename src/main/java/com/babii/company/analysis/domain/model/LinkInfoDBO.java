package com.babii.company.analysis.domain.model;

import com.babii.company.analysis.dto.LinkInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "Quarter_Link")
@Builder(toBuilder = true)
public class LinkInfoDBO implements Cloneable{

    private static final Logger logger
            = LoggerFactory.getLogger(LinkInfoDBO.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private long id;
    @Column(name = "QUARTER")
    private Quarter quarter;
    @Column(name = "YEAR")
    private LocalDate year;
    @Column(name = "LINK")
    private String documentLink;

    @Override
    public LinkInfoDBO clone() {
        LinkInfoDBO linkClone = null;
        try {
            linkClone = (LinkInfoDBO) super.clone();
        } catch (CloneNotSupportedException e) {
            logger.error("Error while cloning LinkInfoDBO class");
        }
        return linkClone;
    }
}
