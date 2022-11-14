package io.nsingla.selenium.logger;

import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.Logs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.util.ArrayList;

public class ConsoleLogger {

    public static final Logger webdriver_logger = LoggerFactory.getLogger("webdriver");

    /**
     * Prints the console entries
     *
     * @param logs     {@link Logs}, which will be filtered for {@link LogEntries}
     * @param testName {@link String} of the test name
     */
    public static void printConsoleEntries(Logs logs, String testName) {
        LogEntries logEntries = filterLogEntries(logs.get(LogType.BROWSER));
        if (!logEntries.getAll().isEmpty()) {
            MDC.put("methodName", testName + "-console");
            ConsoleLogger.webdriver_logger.debug("Console errors from browser:");
            for (LogEntry logEntry : logEntries) {
                ConsoleLogger.webdriver_logger.debug(logEntry.getMessage());
            }
            ConsoleLogger.webdriver_logger.debug("----------------------------------");
            MDC.remove("methodName");
        }
    }

    private static LogEntries filterLogEntries(LogEntries logEntries) {
        ArrayList<LogEntry> newLogEntryList = new ArrayList<>();
        for (LogEntry logEntry : logEntries.getAll()) {
            if (!logEntry.getMessage().contains("scripts/src/common/eventTracking.js")) {
                newLogEntryList.add(logEntry);
            }
        }
        return new LogEntries(newLogEntryList);
    }
}
