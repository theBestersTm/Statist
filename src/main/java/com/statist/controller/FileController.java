package com.statist.controller;


import com.statist.data.dto.StatisticRequest;
import com.statist.data.entity.TaskStatus;
import com.statist.service.ConnectionService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(value = "/findStats")
@AllArgsConstructor
public class FileController {
    private final ConnectionService connectionService;

    @GetMapping()
    public ResponseEntity<TaskStatus> checkFile(@RequestBody StatisticRequest statisticRequest) throws IOException {
        return ResponseEntity.ok(
                connectionService.runSearchService(statisticRequest)
        );
    }

    @GetMapping(value = "extensionStatistic")
    public ResponseEntity getExtensionStatistic() {

        return ResponseEntity.ok(connectionService.extensionStatistic());
    }

    @GetMapping(value = "/{taskId}")
    public ResponseEntity<?> getStatisticByTask(@PathVariable Long taskId) {
        return ResponseEntity.ok(connectionService.statisticByTask(taskId));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<?> handleStorageFileNotFound(IOException exc) {
        return ResponseEntity
                .badRequest()
                .body(exc.getMessage());
    }
}
