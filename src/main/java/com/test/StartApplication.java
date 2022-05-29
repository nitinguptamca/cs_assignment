package com.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.alert.SystemAlert;
import com.test.connection.HsqldbConnection;
import com.test.model.EventLog;
import one.util.streamex.StreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class StartApplication {

    private static final Logger logger
            = LoggerFactory.getLogger(StartApplication.class);

    public static EventLog getExecutionTime(EventLog e1, EventLog e2) {
        // 1000 nanoseconds  = 1 millisecond
        logger.info(" 1000 nanoseconds  = 1 millisecond need to convert nanoseconds to millisecond");
        if (e2.getState() == Status.FINISHED && e1.getState() == Status.STARTED &&
                (e2.getTimestamp().getNanos() / 1000 - e1.getTimestamp().getNanos() / 1000) > 4)
            return e2;
        else if (e2.getState() == Status.STARTED && e1.getState() == Status.FINISHED &&
                (e1.getTimestamp().getNanos() / 1000 - e2.getTimestamp().getNanos() / 1000) > 4)
            return e1;
        else
            return null;

    }

    public static void main(String[] args) throws IOException {
        logger.info(" Start execution ");
        HsqldbConnection.createTable();
        Scanner scanner = new Scanner(System.in);
        logger.info("Take the path to logfile.txt as an input argument: ");
        String filePath = scanner.next();
        logger.info("file path is ", filePath);

        ObjectMapper objectMapper = new ObjectMapper();
        List<EventLog> listOfLogs = Files
                .readAllLines(Paths.get(filePath))
                .parallelStream()
                .map(line -> {
                    EventLog eventLog = null;
                    try {
                        eventLog = objectMapper.readValue(line, EventLog.class);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return eventLog;
                }).collect(Collectors.toList());
        logger.info("total no of element", listOfLogs);

        StreamEx.of(listOfLogs)
                .pairMap((current, next) -> getExecutionTime(current, next))
                .filter(element -> element != null)
                .forEach(ee -> {
                    SystemAlert.alert(ee);
                    HsqldbConnection.insertRecords(ee);
                });

    }
}
