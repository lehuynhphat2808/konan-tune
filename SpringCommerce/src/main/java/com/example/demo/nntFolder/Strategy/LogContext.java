package com.example.demo.nntFolder.Strategy;

public class LogContext {
    private LogStrategy logStrategy;

    public void setLogStrategy(LogStrategy logStrategy) {
        this.logStrategy = logStrategy;
    }

    public void writeLog(String message) {
        logStrategy.writeLog(message);
    }
}