package org.juggling;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class HttpTask implements Serializable {

    private String url;

    private String method;

    private Map<String, String> params;

    public String getUrl() {
        return url;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public String getMethod() {
        return method;
    }

    public static HttpTask GET() {
        return new HttpTask().method("GET");
    }

    public HttpTask withUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpTask withParam(String key, String value) {
        if (params == null) {
            params = new HashMap<>();
        }
        params.put(key, value);
        return this;
    }

    public HttpTask method(String method) {
        this.method = method;
        return this;
    }

    @Override
    public String toString() {
        return "HttpTask{" +
                "url='" + url + '\'' +
                ", method='" + method + '\'' +
                ", params=" + params +
                '}';
    }

}
