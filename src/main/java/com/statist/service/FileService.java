package com.statist.service;

import com.statist.data.dto.StatisticRequest;
import com.statist.data.entity.StatisticInfo;
import com.statist.data.entity.TaskStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public interface FileService {

    <T extends StatisticInfo> List<T> statisticFile(StatisticRequest statisticRequest, Function<File, T> convert, BiConsumer<Iterable<T>, TaskStatus.Enum> consumeInfo) throws FileNotFoundException;

    String getFileExtensions(File file);

    String getOwner(File file);

    String getAttribute(File file, StatisticInfo.FileAttributes attr);
}
