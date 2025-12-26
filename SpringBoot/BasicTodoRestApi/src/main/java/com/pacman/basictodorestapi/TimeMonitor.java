package com.pacman.basictodorestapi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//-> this will define where this annotation will be applicable like method,class etc.
@Retention(RetentionPolicy.RUNTIME)//->I want this in runtime (like @RestController works in runtime but @Override doesn't
public @interface TimeMonitor {
}
