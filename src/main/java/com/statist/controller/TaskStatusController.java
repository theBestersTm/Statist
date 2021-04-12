package com.statist.controller;

import com.statist.data.entity.TaskStatus;
import com.statist.service.TaskStatusService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/taskStatus")
@AllArgsConstructor
public class TaskStatusController {
    private final TaskStatusService taskStatusService;

    @GetMapping(value = "")
    public ResponseEntity<TaskStatus> checkFile(@RequestParam Long taskid) {
        return ResponseEntity.ok(
                taskStatusService.findByTaskStatus(taskid)
        );
    }
}
