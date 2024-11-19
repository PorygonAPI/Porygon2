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
        executor.setCorePoolSize(10); // Tamanho do pool de threads
        executor.setMaxPoolSize(50);  // Tamanho máximo do pool de threads
        executor.setQueueCapacity(100); // Tamanho da fila
        executor.setThreadNamePrefix("api-thread-");
        executor.initialize();
        return executor;
    }
}
