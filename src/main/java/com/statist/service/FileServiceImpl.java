package com.statist.service;

import com.statist.data.dto.StatisticRequest;
import com.statist.data.entity.StatisticInfo;
import com.statist.data.entity.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Slf4j
@Service
public class FileServiceImpl implements FileService {

    @Override
    public <T extends StatisticInfo> List<T> statisticFile(StatisticRequest statisticRequest, Function<File, T> convert, BiConsumer<Iterable<T>, TaskStatus.Enum> consumeInfo) throws FileNotFoundException {
        if (!statisticRequest.getPath().isEmpty()) {
            File rootDir = new File(statisticRequest.getPath());
            List<T> result = new ArrayList<>();
            Queue<File> fileTree = new PriorityQueue<>();

            Collections.addAll(fileTree, rootDir.listFiles());

            while (!fileTree.isEmpty()) {
                File currentFile = fileTree.remove();
                if (currentFile.isFile() && !statisticRequest.getFilteredTypes().isEmpty()
                        && !statisticRequest.getFilteredTypes().contains(getFileExtensions(currentFile)))
                    continue;
                if (currentFile.isDirectory() && currentFile.listFiles() != null) {
                    Collections.addAll(fileTree, currentFile.listFiles());
                } else {
                    result.add(convert.apply(currentFile));
                    if (result.size() > 50) {
                        consumeInfo.accept(result, TaskStatus.Enum.ACTIVE);
                        result = new ArrayList<>();
                    }
                }
            }

            consumeInfo.accept(result, TaskStatus.Enum.DONE);

            return result;
        } else {
            throw new FileNotFoundException("file is empty");
        }
    }

    public String getFileExtensions(File file) {
        return Optional.ofNullable(file)
                .filter(File::isFile)
                .map(File::getName)
                .map(name -> {
                    int beginIndex = name.lastIndexOf(".");
                    return beginIndex >= 0 ? name.substring(beginIndex) : name;
                })
                .orElse("");

    }

    public String getOwner(File file) {
        try {
            return Files.getOwner(file.toPath()).getName();
        } catch (IOException e) {
            log.error("attribute : {}, not found in file {}", "owner", file.getAbsolutePath());
            return "";
        }
    }

    public String getAttribute(File file, StatisticInfo.FileAttributes attr) {  // Creation_date attribute

        try {
            return Files.getAttribute(file.toPath(), attr.name()).toString();
        } catch (IOException e) {
            log.error("attribute : {}, not found in file {}", attr.name(), file.getAbsolutePath());
            return "";
        }
    }


}
