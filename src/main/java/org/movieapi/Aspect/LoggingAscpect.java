package org.movieapi.Aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAscpect {

    private static  final Logger logger = LoggerFactory.getLogger(LoggingAscpect.class);

    //Cross cutting concern logic
    @Before("execution(* org.movieapi.Service.Impl.MovieServiceImpl.getMovies(..))")
    public void logBeforeMethod(JoinPoint joinPoint) {
      logger.info("Before method Execution {} "  ,  joinPoint.getSignature().getName());
    }



}
