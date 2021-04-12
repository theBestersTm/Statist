package com.statist.data.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class StatisticRequest {
    private String path;
    private int deep;
    private Set<String> filteredTypes = new HashSet<>();
}
