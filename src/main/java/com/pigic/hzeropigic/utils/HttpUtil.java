package com.pigic.hzeropigic.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author guchang.pan@hand-china.com
 *
 */
public class HttpUtil {

    /**
     * 获取当前Request
     * @return
     */
    public static HttpServletRequest getCurrentHttpServletRequest(){
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    /**
     * 获取当前Response
     * @return
     */
    public static HttpServletResponse getCurrentHttpServletResponse(){
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        return attributes.getResponse();
    }

    /**
     * 获取一个Session，如果不存在创建一个
     */
    public static HttpSession getOneHttpSession(){
        return getCurrentHttpServletRequest().getSession(true);
    }

    /**
     * 获取当前Session
     */
    public static HttpSession getCurrentHttpSession(){
        return getCurrentHttpServletRequest().getSession(false);
    }

    /**
     * 获取当前Request
     * @return
     */
    public static ServletContext getCurrentServletContext(){
        return getCurrentHttpServletRequest().getSession(true).getServletContext();
    }
}
