package com.pacman.basictodorestapi;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component //->initialize this whatever the objects required to initialize the spring project
public class TimeMonitorAspect {
    @After("@annotation(TimeMonitor)") //->Before/After/Around Advice
    public void logTime(){ //-> this method will be executed when TimeMonitor annotation will be attached to a method for '@Around("@annotation(TimeMonitor)")' this
        //business logic --> this is going to be executed if we use @After then after and @Before then before or @Surround
        System.out.println("Logging Time: ");//logging order can be different because it's handling behind
    }
}
