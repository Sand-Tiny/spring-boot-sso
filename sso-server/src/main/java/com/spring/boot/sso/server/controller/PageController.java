package com.spring.boot.sso.server.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.boot.sso.api.TokenInfo;
import com.spring.boot.sso.server.util.GlobalSessions;
import com.spring.boot.sso.server.util.TokenUtil;
import com.spring.boot.sso.server.util.UrlUtil;
@RequestMapping("/page")
@RestController
public class PageController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());
    /**
     * 说明：此接口主要接受来自应用系统的认证请求，
     * 此时，returnURL参数需加上，用以向认证中心标识是哪个应用系统，
     * 以及返回该应用的URL。如用户没有登录，应用中心向浏览器用户显示登录页面。
     * 如已登录，则产生临时令牌token，并重定向回该系统。上面登录时序交互图中的2和此接口有关。
     * 当然，该接口也同时接受用户直接向认证中心登录，此时没有returnURL参数，认证中心直接返回登录页面。
     * @param username
     * @param password
     * @param request
     * @return
     * @throws IOException 
     */
    @GetMapping("/login")
    public void login(String redirect, String returnUrl, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("请求了: [server] /page/login");
        logger.info("redirect:{}, returnUrl:{}", redirect, returnUrl);
        System.out.println("请求了: [server] /page/login");
        System.out.println("redirect:" + redirect + ", returnUrl:" + returnUrl);
        String sessionId = session.getId();
        HttpSession cacheSession = GlobalSessions.getSession(sessionId);
        PrintWriter writer = response.getWriter();
        if (cacheSession == null) {// 尚未登录
            String loginpage = "<!DOCTYPE HTML><html><body><form action=\"/auth/login\" method=\"POST\"><input name=\"redirect\" type=\"hidden\" value=\""
                    + redirect
                    + "?returnUrl="
                    + returnUrl
                    + "\"/><br/><input name=\"username\" type=\"text\"/><br/><input name=\"password\" type=\"password\"/><br/><input name=\"submit\" type=\"submit\"/></form></body></html>";
            writer.println(loginpage);
            writer.flush();
            return;
        }
        if (redirect == null || redirect.trim().length() == 0) {// 认证中心自己的请求，直接返回登录页面
            String loginpage = "<!DOCTYPE HTML><html><body><form action=\"/auth/login\" method=\"POST\"><input name=\"redirect\" type=\"hidden\" value=\""
                    + redirect
                    + "?returnUrl="
                    + returnUrl
                    + "\"/><br/><input name=\"username\" type=\"text\"/><br/><input name=\"password\" type=\"password\"/><br/><input name=\"submit\" type=\"submit\"/></form></body></html>";
            writer.println(loginpage);
            writer.flush();
            return;
        }
        String token = UUID.randomUUID().toString();
        String url = UrlUtil.getRedirectUrl(redirect, "token", token);
        url = UrlUtil.getRedirectUrl(url, "returnUrl", returnUrl);
        System.out.println("url: " + url);
        String host = UrlUtil.getClientHost(url);
        TokenInfo tokenInfo = (TokenInfo) cacheSession.getAttribute("tokenInfo");
        tokenInfo.setSsoClient(host);
        TokenUtil.setToken(token, tokenInfo);
        response.sendRedirect(url);
    }

}
