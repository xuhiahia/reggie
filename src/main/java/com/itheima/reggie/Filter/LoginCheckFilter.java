package com.itheima.reggie.Filter;

import com.alibaba.fastjson.JSON;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import net.sf.jsqlparser.statement.select.First;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = {"/*"})
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        //拿到当前请求地址
        String requestURI = request.getRequestURI();

        String[] urls=new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**","/front/**"
                ,"/common/**","/user/sendMsg"
                ,"/user/login"
        };
        boolean check = check(urls, requestURI);
        //如果匹配就放行
        if(check){
            filterChain.doFilter(request,response);
            return;
        }
        //如果要拦截但是登入了也放行
        if(request.getSession().getAttribute("employee")!=null){
            BaseContext.setThreadLocal((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(request,response);
            return;
        }
        if(request.getSession().getAttribute("user")!=null){
            Long userId= (Long) request.getSession().getAttribute("user");

            BaseContext.setThreadLocal(userId);
            filterChain.doFilter(request,response);
            return;
        }

        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;


    }
    public boolean check(String[] urls,String url){
        for(String each:urls){
            if(PATH_MATCHER.match(each,url)){
                return true;
            }
        }
        return false;
    }
}
