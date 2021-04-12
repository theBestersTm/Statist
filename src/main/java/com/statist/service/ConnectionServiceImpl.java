package com.statist.service;

import com.statist.data.dto.StatisticInfoDto;
import com.statist.data.dto.StatisticRequest;
import com.statist.data.dto.StatisticResponseDto;
import com.statist.data.entity.StatisticInfo;
import com.statist.data.entity.TaskStatus;
import com.statist.data.repository.StatisticRepository;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;

@Service
@AllArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

    private static final ConcurrentHashMap<Long, TaskStatus> TASK_TASK_STATUS_MAP = new ConcurrentHashMap<>();
    private final StatisticRepository statisticRepository;
    private final FileService fileService;
    private final TaskStatusService taskStatusService;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public StatisticInfoDto save(StatisticInfoDto obj) {
        StatisticInfo statisticInfo = new StatisticInfo();
        TaskStatus taskStatus = new TaskStatus();
        statisticInfo.setName(obj.getName());
        statisticInfo.setCreationDate(obj.getCreationDate());
        statisticInfo.setExtension(obj.getExtension());
        statisticInfo.setFileAttributes(obj.getFileAttributes());
        statisticInfo.setFullPath(obj.getFullPath());
        statisticInfo.setLastModifyDate(obj.getLastModifyDate());
        statisticInfo.setTaskStatus(taskStatus);
        return StatisticInfoDto.from(statisticRepository.save(statisticInfo));
    }

    @Override
    public void saveList(Iterable<StatisticInfo> statisticInfos) {
        statisticRepository.saveAll(statisticInfos);
    }

    @Override
    public TaskStatus runSearchService(StatisticRequest statisticRequest) {
        TaskStatus task = taskStatusService.createTask(statisticRequest.getPath());

        Function<File, StatisticInfo> getAttributesFromFile = file -> {
            String creationTime = fileService.getAttribute(file, StatisticInfo.FileAttributes.creationTime);
            ZonedDateTime zdt = ZonedDateTime.parse(creationTime);
            TASK_TASK_STATUS_MAP.computeIfPresent(task.getId(), (aLong, taskStatus) -> {
                taskStatus.setFilesProceed(taskStatus.getFilesProceed() + 1);
                return taskStatus;
            });
            TASK_TASK_STATUS_MAP.putIfAbsent(task.getId(), task);
            return new StatisticInfo(null, file.getName(), fileService.getFileExtensions(file),
                    file.getAbsolutePath(), file.length(), zdt.toLocalDateTime(),
                    ofEpochSecond(file.lastModified(), 0, UTC),
                    fileService.getOwner(file), task);
        };

        Executors.newSingleThreadExecutor().submit(() -> fileService.statisticFile(statisticRequest, getAttributesFromFile, (statisticInfos, status) -> {
            saveList(statisticInfos);
            task.setStatus(status.getTaskStatus());
            task.setSecondPassed(System.currentTimeMillis() - task.getStartTime());
            taskStatusService.updateTask(task);
        }));

        return task;
    }

    @Override
    public List<StatisticInfo> getAll() {
        return statisticRepository.findAll();
    }

    @Override
    public List<Map<String, Object>> extensionStatistic() {
        return jdbcTemplate.queryForList("SELECT stat.extension, count(stat.extension) FROM statistics stat GROUP BY stat.extension");
    }

    @Override
    public StatisticResponseDto statisticByTask(Long taskId) {
        List<Map<String, Object>> fileCountAndSize = jdbcTemplate.queryForList("SELECT count(stat.id) as fileCount, sum(stat.size) as fileSize FROM statistics stat " +
                "where stat.task_status = " + taskId);
        List<Map<String, Object>> top5Largest = jdbcTemplate.queryForList("SELECT stat.name as name FROM statistics stat " +
                "where stat.task_status = " + taskId + " " +
                "ORDER BY stat.size DESC " +
                "LIMIT 5");
        List<Map<String, Object>> top5OldFiles = jdbcTemplate.queryForList("SELECT stat.name FROM statistics stat " +
                "where stat.task_status = " + taskId + " " +
                "ORDER BY stat.creation_date ASC " +
                "LIMIT 5");
        List<Map<String, Object>> duplicates = jdbcTemplate.queryForList("SELECT sss.nam as name FROM (" +
                "   select stat.name as nam, count(stat.name) as nameCount" +
                "   from statistics stat" +
                "   where stat.task_status = " + taskId + " " +
                "   group by stat.name " +
                ") as sss " +
                "where sss.nameCount > 1");
        return StatisticResponseDto.builder()
                .totalSize(fileCountAndSize.stream().filter(map-> map.containsKey("fileSize")).map(map-> (BigDecimal) map.get("fileSize")).findFirst().orElse(BigDecimal.valueOf(0)))
                .totalFileCount(fileCountAndSize.stream().filter(map-> map.containsKey("fileCount")).map(map-> (Long) map.get("fileCount")).findFirst().orElse(0L))
                .top5LargestFiles(top5Largest.stream().filter(map-> map.containsKey("name")).map(map-> map.get("name").toString()).collect(Collectors.toList()))
                .top5OldFiles(top5OldFiles.stream().filter(map-> map.containsKey("name")).map(map-> map.get("name").toString()).collect(Collectors.toList()))
                .duplicates(duplicates.stream().filter(map-> map.containsKey("name")).map(map-> map.get("name").toString()).collect(Collectors.toList()))
                .build();
    }
}
