package com.cware.partner.common.config;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

	@Value("${partner.coupang.product.corePoolSize}")
	int PRODUCT_CORE_POOL_SIZE;

	@Value("${partner.coupang.product.maxPoolSize}")
	int PRODUCT_MAX_POOL_SIZE;

	@Value("${partner.coupang.product.queueCapacity}")
	int PRODUCT_QUEUE_CAPACITY;

	@Value("${partner.coupang.option.corePoolSize}")
	int OPTION_CORE_POOL_SIZE;

	@Value("${partner.coupang.option.maxPoolSize}")
	int OPTION_MAX_POOL_SIZE;

	@Value("${partner.coupang.option.queueCapacity}")
	int OPTION_QUEUE_CAPACITY;

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(PRODUCT_CORE_POOL_SIZE);
		executor.setMaxPoolSize(PRODUCT_MAX_POOL_SIZE);
		executor.setQueueCapacity(PRODUCT_QUEUE_CAPACITY);
		executor.setThreadNamePrefix("ProductCoupang-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(60);
		executor.initialize();
		return executor;
	}

	@Bean
	public Executor optionAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(OPTION_CORE_POOL_SIZE);
		executor.setMaxPoolSize(OPTION_MAX_POOL_SIZE);
		executor.setQueueCapacity(OPTION_QUEUE_CAPACITY);
		executor.setThreadNamePrefix("OptionCoupang-");
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		executor.setWaitForTasksToCompleteOnShutdown(true);
		executor.setAwaitTerminationSeconds(60);
		executor.initialize();
		return executor;
	}
}
