package com.statist.data.dto;


import com.statist.data.entity.StatisticInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatisticInfoDto implements Serializable {

    private String name;
    private String extension;
    private String fullPath;
    private LocalDateTime creationDate;
    private LocalDateTime lastModifyDate;
    private String fileAttributes;
    private Long taskStatus;

    public static StatisticInfoDto from(StatisticInfo statisticInfo) {
        return StatisticInfoDto.builder()
                .name(statisticInfo.getName())
                .extension(statisticInfo.getExtension())
                .fullPath(statisticInfo.getFullPath())
                .creationDate(statisticInfo.getCreationDate())
                .lastModifyDate(statisticInfo.getLastModifyDate())
                .taskStatus(statisticInfo.getTaskStatus().getId())
                .build();
    }
}
