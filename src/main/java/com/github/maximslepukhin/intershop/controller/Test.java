//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.servlet.*;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//import java.time.Duration;
//import java.time.LocalDateTime;
//
//@Configuration
//public class Test {
//
//    @Bean
//    public FilterRegistrationBean<Filter> loggingFilter() {
//        Filter filter = new Filter() {
//            int count = 0;
//            @Override
//            public void doFilter(ServletRequest request,
//                                 ServletResponse response,
//                                 FilterChain chain) throws IOException, ServletException {
//
//                HttpServletRequest httpReq = (HttpServletRequest) request;
//                System.out.println("Incoming request: " + httpReq.getRequestURI());
//
//                // Пропускаем дальше
//                LocalDateTime startTime = LocalDateTime.now();
//                chain.doFilter(request, response);
//                long duration = Duration.between(startTime, LocalDateTime.now()).toMillis();
//                // sendMetrics(httpReq, duration)
//            }
//        };
//
//        FilterRegistrationBean<Filter> registrationBean =
//                new FilterRegistrationBean<>(filter);
//        registrationBean.setOrder(1); // порядок вызова фильтров
//        registrationBean.addUrlPatterns("/*"); // охватываем все пути
//        return registrationBean;
//    }
//}