package com.fy.monitor.esper;

import com.espertech.esper.client.*;
import com.fy.monitor.jstorm.NginxLogJSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

/**
 * Created by walter on 17/6/17.
 */
public class EsperAnalyzeEngine implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(EsperAnalyzeEngine.class);

    private static EPServiceProvider epService;
    private static EPAdministrator admin;
    private static String product = NginxLogJSON.class.getName();

    private static String epl = "select status, count(*) as count from " + product + ".win:time(2) group by status ";

    private static EPStatement state;

    private static EPRuntime runtime;

    static{
        epService = EPServiceProviderManager.getDefaultProvider();
        admin = epService.getEPAdministrator();
        state = admin.createEPL(epl);
        state.addListener(new NginxLogStatusListener());
        runtime = epService.getEPRuntime();
    }

    public EsperAnalyzeEngine(){

    }

    public static void execut(NginxLogJSON json){

        logger.info("this is EsperAnalyzeEngine:"+json.toString());
        runtime.sendEvent(json);
    }
}
