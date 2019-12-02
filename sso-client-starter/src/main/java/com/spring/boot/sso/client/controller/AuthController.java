package com.spring.boot.sso.client.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.spring.boot.sso.client.SsoClientProperties;
import com.spring.boot.sso.client.util.UrlUtil;

@RequestMapping("auth")
@RestController
public class AuthController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private SsoClientProperties properties;

    /**
     * 说明：接收来自认证中心携带临时令牌token的重定向， 向认证中心/auth/verify接口去验证此token的有效性，
     * 如有效，即建立本地会话，根据returnURL返回浏览器用户的实际请求。
     * 如验证失败，再重定向到认证中心登录页面。上面登录时序交互图中的4与此接口有关。
     * 
     * @param name
     * @return
     * @throws IOException 
     */
    @GetMapping("check")
    public void check(String token, String returnUrl, HttpSession session, HttpServletResponse response) throws IOException {
        logger.info("请求了: [ client ] /auth/check");
        logger.info("token: {}, returnUrl: {}", token, returnUrl);
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

    @GetMapping("logout")
    public void logout(String localId, HttpSession session, HttpServletResponse response) throws IOException {
        logger.info("请求了: [ client ] /auth/logout");
        String sessionId = session.getId();
        RestTemplate rest = new RestTemplate();
        String url = properties.getSsoServerHost() + "/auth/logout?localId=" + sessionId;
        rest.getForObject(url, String.class);
        response.sendRedirect("/");
    }

}
