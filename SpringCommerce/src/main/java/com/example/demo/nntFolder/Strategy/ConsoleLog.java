package com.example.demo.nntFolder.Strategy;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleLog implements LogStrategy{
    @Override
    public void writeLog(String message) {
        Logger logger = Logger.getLogger("ConsoleLog");
        logger.setLevel(Level.ALL);
        logger.severe(message);
    }
}
