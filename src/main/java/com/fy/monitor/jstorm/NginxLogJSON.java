package com.fy.monitor.jstorm;

import java.io.Serializable;

/**
 * Created by walter on 17/6/17.
 */
public class NginxLogJSON implements Serializable {


	/*
	 * {"cookies":"cookie_name=look+me%21", "request_time":"0.000",
	 * "request":"GET \/index.php HTTP\/1.1", "msec":"1471853337.405",
	 * "time_local":"22\/Aug\/2016:16:08:57 +0800",
	 * "http_user_agent":"Mozilla\/5.0 (Windows NT 6.1; WOW64; rv:48.0)
	 * Gecko\/20100101 Firefox\/48.0", "remote_addr":"192.168.30.133",
	 * "status":"000" }
	 */

    private String remote_addr;
    private String time_local;
    private String msec;
    private String request;
    private String status;
    private String body_bytes_sent;
    private String http_referer;
    private String http_user_agent;
    private String request_time;
    private String upstream_addr;
    private String cookies;
    private String original;

    private String uuid;
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getCookies() {
        return cookies;
    }

    public void setCookies(String cookies) {
        this.cookies = cookies;
    }

    public String getRemote_addr() {
        return remote_addr;
    }

    public void setRemote_addr(String remote_addr) {
        this.remote_addr = remote_addr;
    }

    public String getTime_local() {
        return time_local;
    }

    public void setTime_local(String time_local) {
        this.time_local = time_local;
    }

    public String getMsec() {
        return msec;
    }

    public void setMsec(String msec) {
        this.msec = msec;
    }

    public String getRequest() {
        return request;
    }

    public void setRequest(String request) {
        this.request = request;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBody_bytes_sent() {
        return body_bytes_sent;
    }

    public void setBody_bytes_sent(String body_bytes_sent) {
        this.body_bytes_sent = body_bytes_sent;
    }

    public String getHttp_referer() {
        return http_referer;
    }

    public void setHttp_referer(String http_referer) {
        this.http_referer = http_referer;
    }

    public String getHttp_user_agent() {
        return http_user_agent;
    }

    public void setHttp_user_agent(String http_user_agent) {
        this.http_user_agent = http_user_agent;
    }

    public String getRequest_time() {
        return request_time;
    }

    public void setRequest_time(String request_time) {
        this.request_time = request_time;
    }

    public String getUpstream_addr() {
        return upstream_addr;
    }

    public void setUpstream_addr(String upstream_addr) {
        this.upstream_addr = upstream_addr;
    }


    private String _SPLIT = "#";
    public NginxLogJSON(String _SPLITwith) {
        this._SPLIT = _SPLITwith;
    }

    public NginxLogJSON() {

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        StringBuilder sb = new StringBuilder();
        sb.append(this.cookies).append(_SPLIT);
        sb.append(this.request_time).append(_SPLIT);
        sb.append(this.type).append(_SPLIT);
        sb.append(this.request_source).append(_SPLIT);
        sb.append(this.http_version).append(_SPLIT);
        sb.append(this.msec).append(_SPLIT);
        sb.append(this.time_local).append(_SPLIT);
        sb.append(this.timeZone).append(_SPLIT);
        sb.append(this.http_user_agent).append(_SPLIT);

        sb.append(this.remote_addr).append(_SPLIT);
        sb.append(this.status).append(_SPLIT);

        sb.append(this.body_bytes_sent).append(_SPLIT);
        sb.append(this.http_referer).append(_SPLIT);
        sb.append(this.upstream_addr);

        return sb.toString();
    }


    private String timeZone;
    private String type;
    private String request_source;
    private String http_version;
    private String hbase_id;

    public String getHbase_id() {
        return hbase_id;
    }

    public void setHbase_id(String hbase_id) {
        this.hbase_id = hbase_id;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRequest_source() {
        return request_source;
    }

    public void setRequest_source(String request_source) {
        this.request_source = request_source;
    }

    public String getHttp_version() {
        return http_version;
    }

    public void setHttp_version(String http_version) {
        this.http_version = http_version;
    }

}
