package com.spring.boot.sso.client.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

import com.spring.boot.sso.client.SsoClientProperties;
import com.spring.boot.sso.client.util.UrlUtil;

public class CheckServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = 5705677235440774575L;

    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private SsoClientProperties properties;

    public CheckServlet(SsoClientProperties properties) {
        super();
        this.properties = properties;
    }
    

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String token = request.getParameter("token");
        String returnUrl = request.getParameter("returnUrl");
        logger.info("请求了: [ client ] /auth/check");
        logger.info("token: {}, returnUrl: {}", token, returnUrl);
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        RestTemplate rest = new RestTemplate();
        String url = properties.getSsoServerHost() + "/auth/verify?token=" + token + "&localId=" + sessionId;
        String result = rest.getForObject(url, String.class);
        String host = UrlUtil.getClientHost(returnUrl);
        if ("token无效".equals(result)) {
            response.sendRedirect(properties.getSsoServerHost() + "/page/login?redirect=http://" + host + "/auth/check&returnUrl=" + returnUrl);
            return;
        }
        session.setAttribute("isLogin", true);
        response.sendRedirect(returnUrl);
    }
    
}
