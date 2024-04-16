//package everycoding.NalseeFlux.chat;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.web.servlet.config.annotation.AsyncSupportConfigurer;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void configureAsyncSupport(AsyncSupportConfigurer configurer) {
//        configurer.setTaskExecutor(mvcTaskExecutor());
//    }
//
//    public ThreadPoolTaskExecutor mvcTaskExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setCorePoolSize(5); // 기본 스레드 수
//        taskExecutor.setMaxPoolSize(50); // 최대 스레드 수
//        taskExecutor.setQueueCapacity(100); // 큐 최대 길이
//        taskExecutor.setThreadNamePrefix("mvc-exec-");
//        taskExecutor.initialize();
//        return taskExecutor;
//    }
//}
