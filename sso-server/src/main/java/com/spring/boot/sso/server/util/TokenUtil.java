package com.spring.boot.sso.server.util;

import java.util.HashMap;
import java.util.Map;

import com.spring.boot.sso.api.TokenInfo;

public class TokenUtil {
    
    private static Map<String, TokenInfo> cache = new HashMap<>();
    // 存储临时令牌到redis中，存活期60秒
    public static void setToken(String tokenId, TokenInfo tokenInfo) {
        cache.put(tokenId, tokenInfo);
    }

    // 根据token键取TokenInfo
    public static TokenInfo getToken(String tokenId) {
        return cache.get(tokenId);
    }

    // 删除某个 token键值
    public static void delToken(String tokenId) {
        cache.remove(tokenId);
    }
}
