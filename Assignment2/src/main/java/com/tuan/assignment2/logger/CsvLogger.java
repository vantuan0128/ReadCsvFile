package com.tuan.assignment2.logger;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.SimpleFormatter;

@Component
public class CsvLogger implements Logger{

    private final java.util.logging.Logger logger;

    public CsvLogger() {
        this.logger = java.util.logging.Logger.getLogger("CsvErrorLog");
    }

    @Override
    public void initializerLogger() throws IOException {
        FileHandler fh = new FileHandler("csv_error_log.txt", 1024 * 1024, 5, true);
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter);
    }

    @Override
    public void logWarning(int lineNumber, String message) {
        logger.warning("Line " + lineNumber + ": " + message);
    }

    @Override
    public void logError(int lineNumber, Exception e) {
        logger.severe("Error at line " + lineNumber + ": " + e.getMessage());
    }
}
