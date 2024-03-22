package everycoding.NalseeFlux.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
//import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
//import org.springframework.security.config.web.server.ServerHttpSecurity;
//import org.springframework.security.web.server.SecurityWebFilterChain;
//import org.springframework.web.cors.CorsConfiguration;
//import org.springframework.web.cors.reactive.CorsConfigurationSource;
//import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
//import org.springframework.web.util.pattern.PathPatternParser;
//
//import java.util.Arrays;
//
//@Configuration
//@EnableReactiveMethodSecurity
//@EnableWebFluxSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
//        return http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                // CORS 설정
//                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
//                // 인증 방식 설정
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/**").permitAll() // 모든 요청에 대해 접근 허용
//                )
//                // 세션 관리
//                .build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(true);
//        config.setAllowedOriginPatterns(Arrays.asList("*")); // 이 메소드는 deprecated 될 수 있으니, setAllowedOriginPatterns(Arrays.asList("*")) 사용을 고려하세요.
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
//        source.registerCorsConfiguration("/**", config);
//
//        return source;
//    }
//}



import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // 스프링 시큐리티의 HTTP 보안 설정에서 CSRF 보호기능 비활성화 (Cross-Site Request Forgery, 사이트 간 요청 위조)
                .formLogin(AbstractHttpConfigurer::disable) // 폼 로그인 비활성화 : API 서버와 같이 폼 로그인이 필요없는 방식) 비활성화
                .httpBasic(AbstractHttpConfigurer::disable)
                .cors(configurer -> configurer.configurationSource(corsConfigurationSource())) // (Cross-Origin Resource Sharing) 웹 앱의 보안을 유지하면서 다른 출처의 리소스 요청을 허용하도록 설정
                .sessionManagement(configure -> configure.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 서버가 사용자 세션을 유지하지 않음. 서버의 확장성을 높이고 client와 server 간의 결합도를 낮춘다.
//                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll())
        ;
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addExposedHeader("*");
        corsConfiguration.setAllowCredentials(true);

        corsConfiguration.setAllowedOrigins(List.of("https://ide-frontend-wheat.vercel.app/login", "krmp-d2hub-idock.9rum.cc/dev-test/repo_85a78215dc68", "https://ide-frontend-six.vercel.app", "https://ide-frontend-wheat.vercel.app", "http://localhost:3000", "https://k547f55f71a44a.user-app.krampoline.com", "http://localhost:5173"));

        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PATCH", "PUT", "DELETE", "OPTIONS"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}