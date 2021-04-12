package com.statist.service;

import com.statist.data.entity.TaskStatus;

import org.springframework.stereotype.Service;

@Service
public interface TaskStatusService {
    TaskStatus findByTaskStatus(Long Taskid);

    TaskStatus createTask(String path);

    TaskStatus updateTask(TaskStatus obj);
}
