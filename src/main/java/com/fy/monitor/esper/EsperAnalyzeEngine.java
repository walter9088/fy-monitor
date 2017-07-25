package com.fy.monitor.esper;

import com.espertech.esper.client.*;
import com.fy.monitor.jstorm.NginxLogJSON;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by walter on 17/6/17.
 */
public class EsperAnalyzeEngine implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(EsperAnalyzeEngine.class);

    private static EPServiceProvider epService;
    private static EPAdministrator admin;
//    private static String product = NginxLogJSON.class.getName();
//    private static String epl = "select status, count(*) as count from " + product + ".win:time(2) group by status ";
//    private static List<EPStatement> stateList = new ArrayList<EPStatement>();

    private static EPRuntime runtime;

    public static Map<String, EPStatement> ruleMap = new HashMap<String, EPStatement>();

    static {
        epService = EPServiceProviderManager.getDefaultProvider();
        admin = epService.getEPAdministrator();
        runtime = epService.getEPRuntime();
        ruleLoad(ruleMap);
    }

    public EsperAnalyzeEngine() {

    }

    public static void execut(NginxLogJSON json) {

        logger.info("this is EsperAnalyzeEngine:" + json.toString());
        runtime.sendEvent(json);
    }


    /**
     * @param ruleMap
     */
    public static void ruleLoad(final Map<String, EPStatement> ruleMap) {

        new Thread() {

            MongoDatabase mongoDatabase = null;

            @Override
            public void run() {

                if(null==mongoDatabase){
                    init();
                }

                addRule();
                destoryRule();
                try {
                    Thread.sleep(1000 * 60 * 5);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            public void addRule() {

                MongoCollection<Document> collection = mongoDatabase.getCollection("rule");

                /**add*/
                FindIterable<Document> iterable = collection.find();

                MongoCursor<Document> mongoCursor = iterable.iterator();

                /**key,规则状态add,destory，规则*/
                while (mongoCursor.hasNext()) {
                    String epl = mongoCursor.next().getString("topicId");
                    if (null != epl) {

                        EPStatement state = admin.createEPL(epl);
                        ruleMap.put(epl,state);

                        state.addListener(new NginxLogStatusListener());
                        admin.compileEPL(epl);
                    }
                }

                // TODO: 17/7/15

            }

            public void destoryRule() {

                MongoCollection<Document> collection = mongoDatabase.getCollection("rule");

                /**destory*/
                FindIterable<Document> iterable = collection.find();


                MongoCursor<Document> mongoCursor = iterable.iterator();

                while (mongoCursor.hasNext()) {
                    String epl = mongoCursor.next().getString("topicId");
                    if (null != epl) {
                        ruleMap.get(epl).destroy();

                        ruleMap.remove(epl);
                    }
                }

            }

            public void init() {

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

                mongoDatabase = mongoClient.getDatabase("ruledb");

                // mongoDatabase.createCollection("rule");


            }

        }.start();
    }
}
