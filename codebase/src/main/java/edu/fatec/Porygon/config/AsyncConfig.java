package edu.fatec.Porygon.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig {

    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); 
        executor.setMaxPoolSize(100); 
        executor.setQueueCapacity(800); 
        executor.setKeepAliveSeconds(60); 
        executor.setThreadNamePrefix("api-thread-");
        executor.initialize();
        return executor;
    }
}
