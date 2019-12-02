package com.spring.boot.sso.client.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.spring.boot.sso.client.SsoClientProperties;

public class LogoutServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 3116715269209018709L;
    
    Logger logger = LoggerFactory.getLogger(this.getClass());

    private SsoClientProperties properties;

    public LogoutServlet(SsoClientProperties properties) {
        super();
        this.properties = properties;
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        super.doGet(request, response);
        logger.info("请求了: [ client ] /auth/logout");
        String sessionId = request.getSession().getId();
        RestTemplate rest = new RestTemplate();
        String url = properties.getSsoServerHost() + "/auth/logout?localId=" + sessionId;
        rest.getForObject(url, String.class);
        response.sendRedirect("/");
    }

}
