package com.fy.monitor.influxdb;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;

import java.util.concurrent.TimeUnit;

/**
 * Created by walter on 17/6/17.
 */
public class InfluxdbStore {

    public static void main(String[] args){
        InfluxDB influxDB = InfluxDBFactory.connect("http://127.0.0.1:8086", "admin", "admin");
        String dbName = "nginxlog";

         //influxDB.createDatabase(dbName);


       // influxDB.enableBatch(2000, 100, TimeUnit.MILLISECONDS);

        Point point1 = Point.measurement("status")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .field("200",10)
                .field("500",20)
                .field("300",30)
                .build();
        //influxDB.write(dbName, "autogen", point1);




    }

}
