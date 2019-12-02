package com.spring.boot.sso.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spring.boot.sso.client.filter.LoginFilter;
import com.spring.boot.sso.client.servlet.CheckServlet;
import com.spring.boot.sso.client.servlet.LogoutServlet;

@Configuration
@ConditionalOnClass(LoginFilter.class)
@EnableConfigurationProperties(SsoClientProperties.class)
public class SsoClientAutoConfigure {

    private static final Logger logger = LoggerFactory.getLogger(SsoClientAutoConfigure.class);
    @Autowired
    private SsoClientProperties properties;

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "sso.client", value = "enabled", havingValue = "true")
    FilterRegistrationBean getFilter() {
        logger.debug("装配过滤器 ====== START");
        FilterRegistrationBean registration = new FilterRegistrationBean();
        List<String> urlPatterns = new ArrayList<>();
        urlPatterns.add("^(?i)((?!/auth/).)*$");
        if (properties.getUrlPatterns() != null && properties.getUrlPatterns().length > 0) {
            urlPatterns.addAll(Arrays.asList(properties.getUrlPatterns()));
        }
        registration.setFilter(new LoginFilter(properties.getSsoServerHost()));
        registration.addUrlPatterns(urlPatterns.toArray(new String[urlPatterns.size()]));
        registration.setName("loginFilter");
        registration.setOrder(1);
        logger.debug("装配过滤器 ====== END");
        return registration;
    }
    
    @Bean
    public ServletRegistrationBean checkServlet() {
        return new ServletRegistrationBean(new CheckServlet(properties), "/auth/check");
    }
    
    @Bean
    public ServletRegistrationBean logoutServlet() {
        return new ServletRegistrationBean(new LogoutServlet(properties), "/auth/logout");
    }

}
