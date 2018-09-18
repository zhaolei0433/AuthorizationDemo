package cn.ipanel.authorization.demo.interceptor;

import cn.ipanel.authorization.demo.global.SpringUtil;
import cn.ipanel.authorization.demo.global.Globals;
import cn.ipanel.authorization.demo.global.MyException;
import cn.ipanel.authorization.demo.global.SystemDefines;
import cn.ipanel.authorization.demo.service.ManagerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        logger.info("token header == "+extract(httpServletRequest.getHeader(TOKEN_HEADER)));
        managerService.getLoginInfo(extract(httpServletRequest.getHeader(TOKEN_HEADER)), "pc");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
      // System.out.println("postHandle被调用");
    }

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