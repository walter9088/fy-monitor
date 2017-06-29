package com.fy.monitor.jstorm;

import backtype.storm.Config;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.BoltDeclarer;
import backtype.storm.topology.TopologyBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by walter on 17/6/17.
 */
public class NginxLogKafkaTopology {


    private final static Logger logger = LoggerFactory.getLogger(NginxLogKafkaTopology.class);

    private static Map<String, Object> conf = new HashMap<String,Object>();
    static {

    }

    public static void main(String[] _arguments) throws InterruptedException, AlreadyAliveException,
            InvalidTopologyException {

        try {
            TopologyBuilder topology = new TopologyBuilder();
            topology.setSpout("nginx_log_Spout", new NginxLogSpout(), 1);
            BoltDeclarer boltDeclarer = topology.setBolt("nginx_log_analyze_bolt", new NginxLogStatusAnalyzeBolt(), 1);

            boltDeclarer
                    .localOrShuffleGrouping("nginx_log_Spout");

            conf.put("topology.workers", 1);
            conf.put(Config.STORM_CLUSTER_MODE, "distributed");

            StormSubmitter.submitTopology("nginx_log_topology", conf, topology.createTopology());


        }catch (Exception e) {
            e.printStackTrace();
        }

    }
}
