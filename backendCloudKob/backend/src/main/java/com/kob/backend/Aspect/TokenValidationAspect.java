package com.kob.backend.Aspect;


import com.kob.backend.pojo.User;
import com.kob.backend.service.impl.utils.GetUserMessage;
import com.mysql.cj.Session;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;

@Aspect
@Component
public class TokenValidationAspect {

    @Autowired
    GetUserMessage getUserMessage;

    @Pointcut("execution(public * com.kob.backend.service.impl.user.bot..*(..))")
    public void userAccountServiceAspect() {}

    @Pointcut("execution(public * com.kob.backend.consumer..*(..))")
    public void webSocketServerAspect() {}
////    @Before("userAccountServiceAspect || ")
//    @Before("userAccountServiceAspect()")
//    public void validateToken(JoinPoint joinPoint) {
//        System.out.println("acpect before userAccountServiceAspect");
//        System.out.println(joinPoint.getSignature().getName());
//    }
//
//    @After("userAccountServiceAspect()")
//    public void afterTest(JoinPoint joinPoint){
//        System.out.println("After method: " + joinPoint.getSignature());
//    }

    @Around("userAccountServiceAspect()")
    public Object aroundTest(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Aspect around userAccountServiceAspect");
        User one = getUserMessage.getUser();
        Object[] args = joinPoint.getArgs();
        if(args.length > 0 && args[0] instanceof Map){
            Map<String,String> params = (Map<String,String>) args[0];
            params.put("userId", one.getId().toString());
        }
        // 执行目标方法
        Object result = joinPoint.proceed(args);

        // 执行完目标方法后的操作
        System.out.println("After method execution");

        return result;  // 返回目标方法的执行结果
    }


//    @Around("webSocketServerAspect()")
//    public Object aroundWeb(ProceedingJoinPoint joinPoint) throws Throwable{
//        System.out.println("web around userAccountServiceAspect");
//
//        Object[] args = joinPoint.getArgs();
//        if(args.length > 0){
//            for(int i =0 ;i<args.length;i++){
//                if(args[i] instanceof Session){
//                    Session  session = (Session) args[i];
//                    System.out.println(session);
//                }else if(args[i] instanceof String){
//                    System.out.println("token :" + args[i]);
//                }
//            }
//        }
//    }
}
