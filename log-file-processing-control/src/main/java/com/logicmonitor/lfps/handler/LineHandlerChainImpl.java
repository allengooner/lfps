package com.logicmonitor.lfps.handler;

/**
 * Created by allen.gl on 2015/5/13.
 */
public class LineHandlerChainImpl implements LineHandlerChain {

    private LineHandler[] handlers;
    private int position = 0;

    public LineHandlerChainImpl(LineHandler[] handlers) {
        this.handlers = handlers;
    }

    public String doHandle(String line) {
        return handlers[position++].doHandle(line);
    }
}
