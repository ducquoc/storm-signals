// Copyright (c) P. Taylor Goetz (ptgoetz@gmail.com)

package backtype.storm.contrib.signals.spout;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import backtype.storm.contrib.signals.StormSignalConnection;
import backtype.storm.contrib.signals.SignalListener;
import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichSpout;

@SuppressWarnings("serial")
public abstract class BaseSignalSpout extends BaseRichSpout implements SignalListener {

    private static final Logger LOG = LoggerFactory.getLogger(BaseSignalSpout.class);
    private String name;
    private StormSignalConnection signalConnection;

    public BaseSignalSpout(String name) {
        this.name = name;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {
        try {
            this.signalConnection = new StormSignalConnection(this.name, this);
            this.signalConnection.init(conf);
        } catch (Exception e) {
            LOG.error("Error creating SignalConnection.", e);
        }
    }
    
    public void sendSignal(String toPath, byte[] signal)throws Exception {
        this.signalConnection.send(toPath, signal);
    }

    @Override
    public void close() {
        super.close();
        this.signalConnection.close();
    }

}
