package com.test.alert;

import com.test.StartApplication;
import com.test.model.EventLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemAlert {
    private static final Logger logger
            = LoggerFactory.getLogger(SystemAlert.class);
    public static void alert(EventLog eventLog){
        logger.debug("to do .."+eventLog);
    }
}
