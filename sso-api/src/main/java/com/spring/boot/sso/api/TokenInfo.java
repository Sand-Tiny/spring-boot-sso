package com.spring.boot.sso.api;

import java.io.Serializable;

public class TokenInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    /** 用户唯一标识ID */
    private int userId;
    /** 用户登录名 */
    private String username;
    /** 来自登录请求的某应用系统标识 host */
    private String ssoClient;
    /** 本次登录成功的全局会话sessionId */
    private String globalId;
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getSsoClient() {
        return ssoClient;
    }
    public void setSsoClient(String ssoClient) {
        this.ssoClient = ssoClient;
    }
    public String getGlobalId() {
        return globalId;
    }
    public void setGlobalId(String globalId) {
        this.globalId = globalId;
    }
    public static long getSerialversionuid() {
        return serialVersionUID;
    }
    
}
