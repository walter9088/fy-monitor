package com.fy.monitor.esper;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by walter on 17/7/15.
 */
public class MongoDBTest {

    private static Logger logger= org.slf4j.LoggerFactory.getLogger(MongoDBTest.class);

    public static void main(String[] args) {
        //连接到MongoDB服务 如果是远程连接可以替换“localhost”为服务器所在IP地址
        //ServerAddress()两个参数分别为 服务器地址 和 端口
        ServerAddress serverAddress = new ServerAddress("localhost", 27017);

        List<ServerAddress> addrs = new ArrayList<ServerAddress>();
        addrs.add(serverAddress);

        //MongoCredential.createScramSha1Credential()三个参数分别为 用户名 数据库名称 密码
       // MongoCredential credential = MongoCredential.createMongoCRCredential("rule", "ruledb", "rule".toCharArray());

       // List<MongoCredential> credentials = new ArrayList<MongoCredential>();
       // credentials.add(credential);

        //通过连接认证获取MongoDB连接
        //MongoClient mongoClient = new MongoClient(addrs, credentials);

        MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
        MongoClient mongoClient = new MongoClient(connectionString);

        //连接到数据库
        MongoDatabase mongoDatabase = mongoClient.getDatabase("ruledb");

       // mongoDatabase.createCollection("rule");

        MongoCollection<Document> collection= mongoDatabase.getCollection("rule");

        Document document= new Document();
        document.append("topicId","select status, count(*) as count from com.fy.monitor.jstorm.NginxLogJSON.win:time(2) group by status ");
       // collection.insertOne(document);


        FindIterable<Document> iterable= collection.find();

        MongoCursor<Document> mongoCursor = iterable.iterator();

        while(mongoCursor.hasNext()){
            System.out.print(mongoCursor.next().getString("topicId"));
        }


        logger.info("Connect to database successfully");
    }
}
