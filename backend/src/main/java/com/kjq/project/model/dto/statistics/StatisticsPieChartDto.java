package com.kjq.project.model.dto.statistics;

import lombok.Data;

import java.io.Serializable;

@Data
public class StatisticsPieChartDto implements Serializable {
    private String name;
    private Long value;
    private static final long serialVersionUID = 1L;
}
