package com.hichina.main.back.hichinamainback.config;

import com.hichina.main.back.hichinamainback.controller.UserController;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author xiefengchang
 */
@Aspect
@Component
public class HichinaAutoLogAspect {
    private static final Logger LOG = LoggerFactory.getLogger(HichinaAutoLogAspect.class);

    @Around(value = "execution(* com.hichina.main.back.hichinamainback.controller.*.*(..)) && @annotation(enableHichinaAutoLog)")
    public Object enableHichinaAutoLog(ProceedingJoinPoint joinPoint, EnableHichinaAutoLog enableHichinaAutoLog ) throws Throwable {
        String whoami = UserController.currentUser();
        final long start = System.currentTimeMillis();
        LOG.info("==="+whoami+"Entering "+joinPoint.getSignature().getName()+" method of "+joinPoint.getSignature().getDeclaringTypeName());
        final Object proceed = joinPoint.proceed();
        // TODO: 2023/5/19 log into future log system like elasticsearch etc.

        final long executionTime = System.currentTimeMillis() - start;
        LOG.info("===" + joinPoint.getSignature() + " executed in " + executionTime + "ms");
        LOG.info("==="+whoami+" returning "+joinPoint.getSignature().getName()+" method of "+joinPoint.getSignature().getDeclaringTypeName());
        return proceed;
    }
}
