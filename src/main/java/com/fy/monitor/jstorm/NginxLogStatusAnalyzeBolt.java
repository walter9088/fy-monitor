package com.fy.monitor.jstorm;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.alibaba.fastjson.JSONObject;
import com.fy.monitor.esper.EsperAnalyzeEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by walter on 17/6/17.
 */
public class NginxLogStatusAnalyzeBolt implements IRichBolt {

    private Logger logger = LoggerFactory.getLogger(NginxLogStatusAnalyzeBolt.class);

    public NginxLogStatusAnalyzeBolt(){
    }

    public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

    }

    public void execute(Tuple input) {

        String message = (String)input.getValue(0);
        JSONObject jsStr = JSONObject.parseObject(message);
        NginxLogJSON nginxLogJSON=JSONObject.toJavaObject(jsStr,NginxLogJSON.class);

        EsperAnalyzeEngine.execut(nginxLogJSON);
    }

    public void cleanup() {

    }

    public void declareOutputFields(OutputFieldsDeclarer declarer) {

    }

    public Map<String, Object> getComponentConfiguration() {
        return null;
    }
}
