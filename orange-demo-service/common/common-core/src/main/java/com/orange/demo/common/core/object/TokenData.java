package com.orange.demo.common.core.object;

import com.alibaba.fastjson.JSON;
import com.orange.demo.common.core.util.ContextUtil;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * 基于Jwt，用于前后端传递的令牌对象。
 *
 * @author Orange Team
 * @date 2020-08-08
 */
@Data
@ToString
@Slf4j
public class TokenData {

    /**
     * 在HTTP Request对象中的属性键。
     */
    public static final String REQUEST_ATTRIBUTE_NAME = "tokenData";
    /**
     * 用户Id。
     */
    private Long userId;
    /**
     * 是否为超级管理员。
     */
    private Boolean isAdmin;
    /**
     * 用户显示名称。
     */
    private String showName;
    /**
     * 标识不同登录的会话Id。
     */
    private String sessionId;

    /**
     * 将令牌对象添加到Http请求对象。
     *
     * @param tokenData 令牌对象。
     */
    public static void addToRequest(TokenData tokenData) {
        HttpServletRequest request = ContextUtil.getHttpRequest();
        request.setAttribute(TokenData.REQUEST_ATTRIBUTE_NAME, tokenData);
    }

    /**
     * 从Http Request对象中获取令牌对象。
     *
     * @return 令牌对象。
     */
    public static TokenData takeFromRequest() {
        HttpServletRequest request = ContextUtil.getHttpRequest();
        TokenData tokenData = (TokenData) request.getAttribute(REQUEST_ATTRIBUTE_NAME);
        if (tokenData != null) {
            return tokenData;
        }
        String token = request.getHeader(REQUEST_ATTRIBUTE_NAME);
        if (StringUtils.isNotBlank(token)) {
            tokenData = JSON.parseObject(token, TokenData.class);
        } else {
            token = request.getParameter(REQUEST_ATTRIBUTE_NAME);
            if (StringUtils.isNotBlank(token)) {
                tokenData = JSON.parseObject(token, TokenData.class);
            }
        }
        if (tokenData != null) {
            try {
                tokenData.showName = URLDecoder.decode(tokenData.showName, StandardCharsets.UTF_8.name());
            } catch (UnsupportedEncodingException e) {
                log.error("Failed to call TokenData.takeFromRequest", e);
            }
            addToRequest(tokenData);
        }
        return tokenData;
    }
}
