package com.example.apigateway.setups;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
public class GlobalPostFilter {
    final Logger logger = LoggerFactory.getLogger(GlobalPostFilter.class);
    @Bean
    public GlobalFilter postGlobalPostFilter(){
        return ((exchange, chain) -> {
            return chain.filter(exchange)
                    .then(Mono.fromRunnable(()->{
                        log.info("Global Post AuthenticationFilter executed");
                    }));
        });
    }
}
