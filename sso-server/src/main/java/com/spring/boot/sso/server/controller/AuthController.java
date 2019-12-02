package com.spring.boot.sso.server.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.spring.boot.sso.api.TokenInfo;
import com.spring.boot.sso.server.util.GlobalSessions;
import com.spring.boot.sso.server.util.TokenUtil;
import com.spring.boot.sso.server.util.UrlUtil;
@RequestMapping("/auth")
@RestController
public class AuthController {
    /**
     * 说明： 处理浏览器用户登录认证请求。如带有returnURL参数，
     * 认证通过后，将产生临时认证令牌token，并携带此token重定向回系统。
     * 如没有带returnURL参数，说明用户是直接从认证中心发起的登录请求，
     * 认证通过后，返回认证中心首页提示用户已登录。上面登录时序交互图中的3和此接口有关。
     * @param username
     * @param password
     * @param redirect
     * @param req
     * @return
     * @throws IOException 
     */
    @PostMapping("/login")
    public void login(String username, String password, String redirect, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("请求了: [server] /auth/login");
        PrintWriter writer = response.getWriter();
        if (!checkLoginInfo(username, password)) {
            writer.write("用户名或密码错误");
            writer.flush();
            return;
        }
        String sessionId = session.getId();
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setGlobalId(sessionId);
        tokenInfo.setUserId(0);
        tokenInfo.setUsername(username);
        String token = UUID.randomUUID().toString();
        String url = UrlUtil.getRedirectUrl(redirect, "token", token);
        String ssoClient = UrlUtil.getClientHost(url);
        tokenInfo.setSsoClient(ssoClient);
        TokenUtil.setToken(token, tokenInfo);
        session.setAttribute("tokenInfo", tokenInfo);
        GlobalSessions.addSession(sessionId, session);
        response.sendRedirect(url);
    }

    private boolean checkLoginInfo(String username, String password) {
        if ("admin".equals(username) && "123456".equals(password)) {
            return true;
        }
        return false;
    }
    /**
     * 说明：认证应用系统来的token是否有效，如有效，应用系统向认证中心注册，
     * 同时认证中心会返回该应用系统登录用户的相关信息，如ID,username等。
     * 上面登录时序交互图中的4和此接口有关。
     * @param token
     * @param localId
     * @return
     * @throws IOException 
     */
    @GetMapping("verify")
    public void verify (String token, String localId, HttpServletResponse response) throws IOException {
        System.out.println("请求了: [server] /auth/verify");
        PrintWriter writer = response.getWriter();
        TokenInfo tokenInfo = TokenUtil.getToken(token);
        if (tokenInfo != null) {
            writer.write(tokenInfo.toString());
            writer.flush();
            return;
        }
        writer.write("token无效");
        writer.flush();
        return;
    }
    /**
     * 说明：登出接口处理两种情况，一是直接从认证中心登出，
     * 一是来自应用重定向的登出请求。这个根据gId来区分，
     * 无gId参数说明直接从认证中心注销，有，说明从应用中来。
     * 接口首先取消当前全局登录会话，其次根据注册的已登录应用，
     * 通知它们进行登出操作。上面登出时序交互图中的2和4与此接口有关。
     * @param localId
     * @return
     */
    @PostMapping("logout")
    public void logout (String localId, HttpSession session, HttpServletResponse response) {
        System.out.println("请求了: [server] /auth/logout");
        String sessionId = localId;
        if (localId == null || localId.trim().length() == 0) {
            sessionId = session.getId();
            GlobalSessions.delSession(sessionId);
            return;
        }
        GlobalSessions.delSession(sessionId);
    }

}
