package com.puspo.codearena.moduler_monolith.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

//5. Scalability: Caching & Async Processing
//For high performance, we need to offload heavy tasks (like sending emails) and cache frequent reads (like product lists).
@Configuration
@EnableAsync
public class AsyncConfig {
    @Bean(name = "taskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); //minimum number of threads to keep alive Example:
//        If 3 tasks come in → only 3 of those 5 will be busy
//        If 6 tasks come in → 5 core threads run tasks; the 6th task → goes to queue.
        executor.setMaxPoolSize(10); //The maximum number of threads allowed.
//      Core pool = 5 threads
//      If overloaded → can grow up to 10 threads
//      When load drops → extra threads are destroyed
        executor.setQueueCapacity(500); //How many tasks can wait in the queue before creating extra threads.
        executor.setThreadNamePrefix("AsyncThread-"); //Sets a readable prefix for debugging.
        executor.initialize();//Builds the thread pool internally and makes it ready to use.
        return executor;
    }
}
