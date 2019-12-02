package com.spring.boot.sso.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("sso.client")
public class SsoClientProperties {
    
    private String ssoServerHost;
    private String urlPatterns;
    public String getSsoServerHost() {
        return ssoServerHost;
    }
    public void setSsoServerHost(String ssoServerHost) {
        this.ssoServerHost = ssoServerHost;
    }
    public String[] getUrlPatterns() {
        if (urlPatterns == null || urlPatterns.trim().length() == 0) {
            return null;
        }
        String[] urlPatternArray = urlPatterns.split(",");
        List<String> urlPatterns = new ArrayList<>(Arrays.asList(urlPatternArray));
        for (Iterator<String> iterator = urlPatterns.iterator();iterator.hasNext();) {
            String urlPattern = iterator.next();
            if ("*".equals(urlPattern) || "/*".equals(urlPattern)) {
                iterator.remove();
                continue;
            }
        }
        return urlPatterns.toArray(new String[urlPatterns.size()]);
    }
    public void setUrlPatterns(String urlPatterns) {
        this.urlPatterns = urlPatterns;
    }
}
