package com.fy.monitor.esper;

import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

/**
 * Created by walter on 17/6/17.
 */
public class NginxLogStatusListener implements UpdateListener {

    private Logger  logger   = LoggerFactory.getLogger(NginxLogStatusListener.class);

    public InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "admin", "admin");
    public String   dbName   = "nginxlog";

    public NginxLogStatusListener() {
        influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);
    }

    public void update(EventBean[] newEvents, EventBean[] oldEvents) {

        if (newEvents != null) {
            if (newEvents.length > 0) {

                Point point1 = Point.measurement("status").time(System.currentTimeMillis(),
                                                                TimeUnit.MILLISECONDS).field(newEvents[newEvents.length
                                                                                                       - 1].get("status").toString(),
                                                                                             newEvents[newEvents.length
                                                                                                       - 1].get("count")).build();
                influxDB.write(dbName, "default", point1);
            }

        }
    }
}
