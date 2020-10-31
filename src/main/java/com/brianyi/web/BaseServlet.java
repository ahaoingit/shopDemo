package com.brianyi.web;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class BaseServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setContentType("text/html;charset=utf-8");
            String requestURI = request.getRequestURI();
            String md = requestURI.split("/")[1].split("\\.")[0];
            //获取客户端提交数据,方法名
            Class clazz = this.getClass();
            Method method = clazz.getMethod(md, HttpServletRequest.class, HttpServletResponse.class);
            method.invoke(this, request, response);
        }catch (Exception ex){ex.printStackTrace();}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
