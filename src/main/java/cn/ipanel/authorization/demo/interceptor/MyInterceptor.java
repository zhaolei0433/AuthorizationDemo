package cn.ipanel.authorization.demo.interceptor;

import cn.ipanel.authorization.demo.SpringUtil;
import cn.ipanel.authorization.demo.global.Globals;
import cn.ipanel.authorization.demo.global.MyException;
import cn.ipanel.authorization.demo.global.SystemDefines;
import cn.ipanel.authorization.demo.service.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhaolei
 * Create: 2018/9/14 15:11
 * Modified By:
 * Description:
 */
public class MyInterceptor implements HandlerInterceptor {
    private static Logger logger = LoggerFactory.getLogger(MyInterceptor.class);

    private ManagerService managerService = SpringUtil.getBean(ManagerService.class);
    private static final String TOKEN_HEADER = SystemDefines.JWT_TOKEN_HEADER;
    private static final String HEADER_PREFIX = SystemDefines.JWT_HEADER_PREFIX;

    //在请求处理之前进行调用（Controller方法调用之前
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("preHandle is called !");
        String url = httpServletRequest.getRequestURL().toString();
        logger.info("url == "+url);
        logger.info("token == "+httpServletRequest.getHeader(TOKEN_HEADER));
        try {
            //
            //
            logger.info("token header == "+extract(httpServletRequest.getHeader(TOKEN_HEADER)));
            //  managerService.getLoginInfo(extract(httpServletRequest.getHeader(TOKEN_HEADER)), "pc");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        httpServletResponse.sendRedirect("/error");
        //httpServletResponse.getOutputStream().println("{\"data\":\"token is no available\"}");
        return false;    //如果false，停止流程，api被拦截
    }
    //请求处理之后进行调用，但是在视图被渲染之前（Controller方法调用之后）
    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
      // System.out.println("postHandle被调用");
    }
    //在整个请求结束之后被调用，也就是在DispatcherServlet 渲染了对应的视图之后执行（主要是用于进行资源清理工作）
    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
       // System.out.println("afterCompletion被调用");
    }
    private static String extract(String header) throws Exception {
        if (Globals.isEmpty(header) || Globals.isBlank(header)) {
            throw new MyException("Authorization header cannot be blank");
        }
        if (!header.startsWith(HEADER_PREFIX) || header.length() <= HEADER_PREFIX.length()) {
            throw new MyException("Authorization header is error");
        }
        return header.substring(HEADER_PREFIX.length(), header.length());
    }
}