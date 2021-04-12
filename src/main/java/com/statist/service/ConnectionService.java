package com.statist.service;

import com.statist.data.dto.StatisticInfoDto;
import com.statist.data.dto.StatisticRequest;
import com.statist.data.dto.StatisticResponseDto;
import com.statist.data.entity.StatisticInfo;
import com.statist.data.entity.TaskStatus;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public interface ConnectionService {

    StatisticInfoDto save(StatisticInfoDto obj);

    void saveList(Iterable<StatisticInfo> statisticInfos);

    TaskStatus runSearchService(StatisticRequest path) throws FileNotFoundException;

    List<StatisticInfo> getAll();

    List<Map<String, Object>> extensionStatistic();

    StatisticResponseDto statisticByTask(Long taskId);
}
