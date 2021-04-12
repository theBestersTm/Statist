package com.statist.data.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "taskStatus")
@Entity
@Data
@NoArgsConstructor
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String status;
    private long startTime;
    private long secondPassed;
    private int filesProceed;
    private String path;
    public TaskStatus(String name) {
        this.status = name;
    }
    @Getter
    public enum Enum {
        ACTIVE ("Active"),
        DONE ("Done"),
        NOT_ACTIVE ("NotActive");

        private String taskStatus;

        Enum(String name) {
            this.taskStatus = name;
        }
    }

}

