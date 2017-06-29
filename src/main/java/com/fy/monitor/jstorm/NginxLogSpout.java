package com.fy.monitor.jstorm;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import com.alibaba.fastjson.JSONObject;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;


import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by walter on 17/6/17.
 */
public class NginxLogSpout implements IRichSpout {


    private static String topic ="nginx_log_access";

    private static String zk_connector="127.0.0.1:2181";

    private static String group_id="monitor-group";

    private ConsumerConnector consumer;

    private SpoutOutputCollector collector;

    private Logger logger= LoggerFactory.getLogger(NginxLogSpout.class);

    /**
     * spout可以有构造函数，但构造函数只执行一次，是在提交任务时，
     * 创建spout对象，因此在task分配到具体worker之前的初始化工作可以在此处完成，一旦完成，初始化的内容将携带到每一个task内
     * （因为提交任务时将spout序列化到文件中去，在worker起来时再将spout从文件中反序列化出来）。
     */
    public NginxLogSpout(){

    }

    /**
     *  定义spout发送数据，每个字段的含义
     * @param declarer
     */
    public void declareOutputFields(OutputFieldsDeclarer declarer) {



        declarer.declare(new Fields("nginxlog"));

    }

    /**
     * 获取本spout的component 配置
     * @return
     */
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    /**
     * open 是当task启动后执行的初始化动作
     * @param conf
     * @param context
     * @param collector
     */
    public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {

        consumer = createConsumer();

         this.collector=collector;
    }

    private ConsumerConnector createConsumer() {

        Properties props = new Properties();
        /**
         * 必须的配置
         */
        props.put("zookeeper.connect", zk_connector);
        /**
         * 必须的配置， 代表该消费者所属的 consumer group
         */
        props.put("group.id", group_id);
        /**
         * 多长时间没有发送心跳信息到zookeeper就会认为其挂掉了，默认是6000
         */
        props.put("zookeeper.session.timeout.ms", "6000");
        /**
         * 可以允许zookeeper follower 比 leader慢的时长
         */
        props.put("zookeeper.sync.time.ms", "200");
        /**
         * 控制consumer offsets提交到zookeeper的频率， 默认是60 * 1000
         */
        props.put("auto.commit.interval.ms", "1000");

        return Consumer.createJavaConsumerConnector(new ConsumerConfig(props));


    }
    /**
     * close是当task被shutdown后执行的动作
     */
    public void close() {

    }

    /**
     * 当task被激活时触发动作
     */
    public void activate() {

    }

    /**
     * 是task被deactive时，触发的动作
     */
    public void deactivate() {

    }

    /**
     * 是spout实现核心， nextuple完成自己的逻辑，即每一次取消息后，用collector 将消息emit出去
     */
    public void nextTuple() {

        logger.error("****************start nextTuple ********************");
        Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
        topicCountMap.put(topic, 1);
        /**
         * createMessageStreams 为每个topic创建 message stream
         */
        Map<String, List<KafkaStream<byte[], byte[]>>> messageStreams = consumer.createMessageStreams(topicCountMap);
        KafkaStream<byte[], byte[]> stream = messageStreams.get(topic).get(0);
        ConsumerIterator<byte[], byte[]> iterator = stream.iterator();
        logger.info("****************start nextTuple iterator********************");
        while (iterator.hasNext()) {
            try {
                String message = new String(iterator.next().message());

                Values values = new Values();
                values.add(0,message);
                logger.info("****************get message********************"+message);
                collector.emit(values);

            } catch (Throwable e) {
                logger.error("nginx_log consumer fail:",e);
            }
        }
    }

    public void ack(Object msgId) {

    }

    public void fail(Object msgId) {

    }
}
