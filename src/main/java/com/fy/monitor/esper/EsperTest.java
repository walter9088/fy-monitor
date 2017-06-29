package com.fy.monitor.esper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.fy.monitor.jstorm.NginxLogJSON;

/**
 * Created by walter on 17/6/17.
 */
public class EsperTest {

    private static Logger logger = LoggerFactory.getLogger(EsperTest.class);

    public static void main(String[] args) {
        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();

        EPAdministrator admin = epService.getEPAdministrator();

        String product = NginxLogJSON.class.getName();

        String epl = "select status, count(*) from " + product + ".win:time_batch(1 sec)  group by status";

        EPStatement state = admin.createEPL(epl);
        state.addListener(new NginxLogStatusListener());

        EPRuntime runtime = epService.getEPRuntime();

        for (int i = 0; i < 100; i++) {

            NginxLogJSON apple1 = new NginxLogJSON();
            apple1.setRequest("a");
            apple1.setStatus("200");

            EsperAnalyzeEngine.execut(apple1);
        }
        NginxLogJSON apple1 = new NginxLogJSON();
        apple1.setRequest("a");
        apple1.setStatus("200");

        runtime.sendEvent(apple1);

        NginxLogJSON apple2 = new NginxLogJSON();
        apple2.setRequest("a");
        apple2.setStatus("300");
        runtime.sendEvent(apple2);

        NginxLogJSON apple3 = new NginxLogJSON();
        apple3.setRequest("a");
        apple3.setStatus("400");
        runtime.sendEvent(apple3);

        NginxLogJSON apple4 = new NginxLogJSON();
        apple3.setRequest("a");
        apple4.setStatus("400");
        runtime.sendEvent(apple4);

        // EsperAnalyzeEngine.execut(apple1);
        //
        // EsperAnalyzeEngine.execut(apple2);
        //
        // EsperAnalyzeEngine.execut(apple3);
        //
        // EsperAnalyzeEngine.execut(apple4);
    }
}
