package com.logicmonitor.lfps.messages;

import java.io.Serializable;

/**
 * Created by allen.gl on 2015/5/14.
 */
public class LogFileScanRequestMessage implements Serializable {
    private String logDir;

    public String getLogDir() {
        return logDir;
    }

    public LogFileScanRequestMessage(String logDir) {

        this.logDir = logDir;
    }
}
