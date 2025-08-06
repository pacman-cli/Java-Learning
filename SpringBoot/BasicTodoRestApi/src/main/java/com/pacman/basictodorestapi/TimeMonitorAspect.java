package com.pacman.basictodorestapi;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component //->initialize this whatever the objects required to initialize the spring project
public class TimeMonitorAspect {
    //ProceedingJoinPoint special type of join point that we can use in Around Advice which helps us to control when to start execution of that TimeMonitor annotated method.There is a Proceed method inside ProceedingJoinPoint which helps us to start the execution of our code and also throws and exception so whenever we use this we need to use try catch
    @Around("@annotation(TimeMonitor)") //->Before/After/Around Advice
    public void logTime(ProceedingJoinPoint proceedingJoinPoint){ //-> this method will be executed when TimeMonitor annotation will be attached to a method for '@Around("@annotation(TimeMonitor)")' this
         long start = System.currentTimeMillis();//start time of the code
        try{
            //execute the join point
            proceedingJoinPoint.proceed();

        } catch (Throwable e) {
            System.out.println("Something went wrong during the execution" + e.getMessage());
        } finally {
            long end = System.currentTimeMillis();
            long totalExecutionTime = end -start;
            System.out.println("Total time of execution of the method is :"+ totalExecutionTime + "ms..");
        }

        //business logic --> this is going to be executed if we use @After then after and @Before then before or @Surround
        //System.out.println("Logging Time: ");//logging order can be different because it's handling behind
    }
}
