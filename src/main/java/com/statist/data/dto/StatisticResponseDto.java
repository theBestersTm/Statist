package com.statist.data.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Builder
@Data
public class StatisticResponseDto {
    private Long totalFileCount;
    private BigDecimal totalSize;
    private List<String> top5LargestFiles;
    private List<String> top5OldFiles;
    private List<String> duplicates;
    private double percentFileFromAll;
}
