package com.statist.data.repository;

import com.statist.data.entity.StatisticInfo;
import com.statist.data.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface StatisticRepository extends JpaRepository<StatisticInfo, Long> {
    Optional<StatisticInfo> findByName(String name);
    List<StatisticInfo> findAllByTaskStatusIn(List<TaskStatus> info);
}
