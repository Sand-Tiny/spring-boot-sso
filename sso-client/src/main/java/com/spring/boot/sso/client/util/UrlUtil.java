package com.spring.boot.sso.client.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class UrlUtil {
    public static String getRedirectUrl(String redirect, String param, String value) {
        String url = redirect;
        try {
            url = URLDecoder.decode(redirect, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (url.indexOf("?") > 0) {
            url = url + "&" + param + "=" + value;
        } else {
            url = url + "?" + param + "=" + value;
        }
        return url;
    }

    public static String getClientHost(String url) {
        String host = "";
        if (url.startsWith("http://")) {
            host = url.substring("http://".length());
        }
        if (url.startsWith("https://")) {
            host = url.substring("https://".length());
        }
        if (host.indexOf("/")> 0) {
            host = host.substring(0, host.indexOf("/"));
        }
        return host;
    }

}
