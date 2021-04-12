package com.statist.service;

import com.statist.data.entity.TaskStatus;
import com.statist.data.repository.TaskStatusRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class TaskStatusServiceImpl implements TaskStatusService {

    private final TaskStatusRepository taskStatusRepository;

    @Override
    public TaskStatus findByTaskStatus(Long TaskStatusId) {
        return taskStatusRepository.findById(TaskStatusId)
                .orElseThrow(() -> new EntityNotFoundException("can't find Status obj with status : " + TaskStatusId));
    }
    @Override
    public TaskStatus createTask(String path) {
        TaskStatus taskStatus = new TaskStatus();
        taskStatus.setPath(path);
        taskStatus.setStatus(TaskStatus.Enum.ACTIVE.getTaskStatus());
        taskStatus.setStartTime(System.currentTimeMillis());
        return taskStatusRepository.save(taskStatus);
    }

    @Override
    public TaskStatus updateTask(TaskStatus obj) {
        return taskStatusRepository.saveAndFlush(obj);
    }
}


