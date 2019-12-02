package com.spring.boot.sso.client.filter;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;

@WebFilter(filterName="loginFilter", urlPatterns = {"/hello"})
public class LoginFilter implements Filter {
    @Value("${sso.host}")
    private String ssoHost;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession();
        Boolean isLogin = (Boolean) session.getAttribute("isLogin");
        if (isLogin != null && isLogin) {
            chain.doFilter(request, response);
            return;
        }
        StringBuffer url = req.getRequestURL();
        String queryString = req.getQueryString();
        if (queryString != null && queryString.trim().length() > 0) {
            url.append("?").append(queryString.trim());
        }
        String returnUrl = URLEncoder.encode(url.toString(), "utf-8");
        int port = req.getServerPort();
        String redirect = req.getScheme() + "://" + req.getServerName();
        if (port != 80) {
            redirect += ":" + port;
        }
        //跳转至sso认证中心
        res.sendRedirect(ssoHost + "/page/login?redirect=" + redirect + "/auth/check&returnUrl=" + returnUrl);
    }

    @Override
    public void destroy() {
        
    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        
    }
}
