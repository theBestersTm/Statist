package com.statist.data.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "statistics")
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatisticInfo {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String extension;
    private String fullPath;
    private Long size;
    @Column(name = "creation_date")
    private LocalDateTime creationDate;
    @Column(name = "last_modify_date")
    private LocalDateTime lastModifyDate;
    @Column(name = "file_attributes")
    private String fileAttributes;

    @ManyToOne
    @JoinColumn(name = "taskStatus")
    private TaskStatus taskStatus;


    public enum  FileAttributes {
        creationTime;
    }

}

