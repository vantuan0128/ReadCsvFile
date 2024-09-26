package com.tuan.assignment2.logger;

import java.io.IOException;

public interface Logger {
    void logWarning(int lineNumber, String message);
    void logError(int lineNumber, Exception e);
    void initializerLogger() throws IOException;
}
