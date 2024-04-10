package everycoding.NalseeFlux.chat;

import everycoding.NalseeFlux.dto.UserInfo;
import everycoding.NalseeFlux.webClientService.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final AuthenticationService authenticationService;


    @GetMapping("/hihello")
    public UserInfo getUser (HttpServletRequest request) {
        log.info("쿠키확인" + request.getCookies().toString());
        Cookie[] cookies = request.getCookies();
        String token = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("AccessToken".equals(cookie.getName())) {
                    token = cookie.getValue();
                    log.info("token={}", token);
                }
            }
        }
        UserInfo block = authenticationService.getUserInfo(token).block();

        return block;
    }

//    @GetMapping("/hihellohi")
//    public UserInfo getUser (org.springframework.http.server.ServerHttpRequest request) {
//        String token = extractAuthTokenFromCookies(request);
//        UserInfo block = authenticationService.getUserInfo(token).block();
//
//        return block;
//    }
//
//
//    private String extractAuthTokenFromCookies(org.springframework.http.server.ServerHttpRequest request) {
//        HttpHeaders headers = request.getHeaders();
//        String cookieHeader = headers.getFirst(HttpHeaders.COOKIE);
//        if (cookieHeader != null) {
//            String[] cookies = cookieHeader.split("; ");
//            for (String cookie : cookies) {
//                if (cookie.startsWith("AccessToken=")) {
//                    return cookie.substring("AccessToken=".length());
//                }
//            }
//        }
//        return null;
//    }
}
