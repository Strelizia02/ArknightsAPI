package com.strelizia.arknights.Aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strelizia.arknights.dao.LoginMapper;
import com.strelizia.arknights.vo.JsonResult;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@Aspect
@Component
@SuppressWarnings({"unused"})
public class TokenAspect {

    public static final String TOKEN_KEY = "Authorization";

    @Autowired
    private LoginMapper loginMapper;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private HttpServletRequest request;

    @Pointcut("@annotation(com.strelizia.arknights.annotation.Token)")
    public void annotationPointcut() {

    }

    @Before("annotationPointcut()")
    public void beforePointcut(JoinPoint joinPoint) {
        // 此处进入到方法前  可以实现一些业务逻辑
    }

    @Around("annotationPointcut()")
    public void doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = request.getHeader(TOKEN_KEY);
        String mes;
        if (null == token){
            mes = "no Authorization, please pass Authorization";
        } else {
            Boolean isTrue = this.checkToken(token);
            if (isTrue) {
                loginMapper.refreshToken(token);
                joinPoint.proceed();
                return;
            } else {
                mes = "token is not alive";
            }
        }
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(JsonResult.failureWithCode("301", mes)));
    }

    /**
     * 在切入点return内容之后切入内容（可以用来对处理返回值做一些加工处理）
     */
    @AfterReturning("annotationPointcut()")
    public void doAfterReturning(JoinPoint joinPoint) {
    }

    private Boolean checkToken(String token) {
        int len = loginMapper.getToken(token).size();
        return len > 0;
    }

}
