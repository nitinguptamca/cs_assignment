package com.test.model;

import com.test.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventLog {
    private String id;
    private Status state;
    private String type;
    private String host;
    private Timestamp timestamp;
}
