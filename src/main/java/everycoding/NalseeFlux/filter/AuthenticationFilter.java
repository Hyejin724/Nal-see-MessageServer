package everycoding.NalseeFlux.filter;

import everycoding.NalseeFlux.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class AuthenticationFilter implements WebFilter {

    private final AuthenticationService authenticationService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");

        return authenticationService.getUserInfo(token)
                .flatMap(userInfo -> {
                    if (userInfo != null) {
                        // 사용자 정보를 ServerWebExchange의 속성으로 추가
                        exchange.getAttributes().put("userInfo", userInfo);
                        return chain.filter(exchange); // 요청 처리 계속
                    } else {
                        return Mono.empty(); // 이 부분은 실행되지 않음
                    }
                })
                .switchIfEmpty(Mono.defer(() -> {
                    // 인증 실패 처리
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete(); // 요청 처리 중단 및 인증 실패 응답 반환
                }));
    }

}
