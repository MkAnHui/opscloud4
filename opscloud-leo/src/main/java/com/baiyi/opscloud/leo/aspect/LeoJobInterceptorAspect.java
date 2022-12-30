package com.baiyi.opscloud.leo.aspect;

import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.leo.annotation.LeoJobInterceptor;
import com.baiyi.opscloud.leo.exception.LeoJobException;
import com.baiyi.opscloud.leo.interceptor.LeoDoJobInterceptorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @Author baiyi
 * @Date 2022/12/29 13:29
 * @Version 1.0
 */
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LeoJobInterceptorAspect {

    private final DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();

    private final ExpressionParser expressionParser = new SpelExpressionParser();

    private final LeoDoJobInterceptorHandler leoDoJobInterceptorHandler;

    @Pointcut(value = "@annotation(com.baiyi.opscloud.leo.annotation.LeoJobInterceptor)")
    public void annotationPoint() {
    }

    @Around("@annotation(leoJobInterceptor)")
    public Object around(ProceedingJoinPoint joinPoint, LeoJobInterceptor leoJobInterceptor) throws Throwable {
        //获取切面方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        //获取方法的形参名称
        String[] params = discoverer.getParameterNames(method);
        //获取方法的实际参数值
        Object[] arguments = joinPoint.getArgs();
        //设置解析SpEL所需的数据上下文
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < Objects.requireNonNull(params).length; len++) {
            context.setVariable(params[len], arguments[len]);
        }
        //解析表达式并获取SpEL的值
        Expression expression = expressionParser.parseExpression(leoJobInterceptor.jobIdSpEL());
        Object jobIdParam = expression.getValue(context);
        if (jobIdParam instanceof Integer) {
            Integer jobId = (Integer) jobIdParam;
            if (IdUtil.isEmpty(jobId)) {
                throw new LeoJobException("任务ID不存在！");
            }
            leoDoJobInterceptorHandler.verifyAuthorization(jobId);
            // 并发校验
            if (!leoJobInterceptor.concurrent()) {
                leoDoJobInterceptorHandler.verifyConcurrent(jobId);
            }
        } else {
            throw new LeoJobException("任务ID类型不正确！");
        }
        return joinPoint.proceed();
    }

}
