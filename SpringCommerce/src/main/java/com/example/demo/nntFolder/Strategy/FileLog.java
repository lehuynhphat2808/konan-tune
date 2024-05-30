package com.example.demo.nntFolder.Strategy;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class FileLog implements LogStrategy{
    @Override
    public void writeLog(String message) {
        String logFilePath = "src/main/java/com/example/demo/nntFolder/file.log";
        int fileSizeLimit = 1024 * 1024;
        int fileCount = 5;
        boolean append = true;
        FileHandler fileHandler;

        try {
            fileHandler = new FileHandler(logFilePath, fileSizeLimit, fileCount, append);
            fileHandler.setFormatter(new SimpleFormatter());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        Logger logger = Logger.getLogger("FileLogger");
        logger.addHandler(fileHandler);
        logger.setLevel(Level.INFO);
        logger.severe(message);
    }
}
