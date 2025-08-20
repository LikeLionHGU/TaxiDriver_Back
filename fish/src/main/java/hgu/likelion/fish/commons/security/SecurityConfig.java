package hgu.likelion.fish.commons.security;



import hgu.likelion.fish.commons.jwt.JwtCookieAuthFilter;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseCookie;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtCookieAuthFilter jwtCookieAuthFilter) throws Exception {
        http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        // 필요 시 일부 엔드포인트만 예외
                        // .ignoringRequestMatchers("/api/logout")
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ★ 무상태
                .httpBasic(h -> h.disable())
                .formLogin(f -> f.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login/oauth2/**", "/api/v1/oauth2/google").permitAll()
                        .requestMatchers("/temp").hasRole("ADMIN")
                        .requestMatchers("/test").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class); // ★ 쿠키필터
        return http.build();
    }
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                // CORS: 정확한 오리진 + credentials 허용
//                .cors(c -> c.configurationSource(corsConfigurationSource()))
//                // 브라우저 환경이면 CSRF를 켜고, 쿠키로 토큰 전달 (SPA는 헤더로 되돌려보내면 됨)
//                .csrf(csrf -> csrf
//                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                )
//                // 세션 기반 (STATEFUL)
//                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
//                .httpBasic(h -> h.disable())
//                .formLogin(f -> f.disable())
//
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login/oauth2/**").permitAll()        // OAuth2 redirect URI
//                        .requestMatchers("/api/v1/oauth2/google").permitAll()   // 콜백 엔드포인트
//                        .requestMatchers(HttpMethod.GET, "/api/me").authenticated()
//                        .requestMatchers(HttpMethod.POST, "/api/logout").authenticated()
//                        .anyRequest().authenticated()                            // 나머지는 인증 필요
//                )
//
//                // 로그아웃: 서버 세션 무효화 + 만료 쿠키 내려주기
//                .logout(l -> l
//                        .logoutUrl("/api/logout")
//                        .logoutSuccessHandler((req, res, auth) -> {
//                            var session = req.getSession(false);
//                            if (session != null) session.invalidate();
//
//                            // HTTPS 운영이라면 secure(true) 권장
//                            ResponseCookie expired = ResponseCookie.from("JSESSIONID", "")
//                                    .httpOnly(true).secure(true).sameSite("Lax")
//                                    .path("/").maxAge(0)
//                                    .build();
//                            res.addHeader("Set-Cookie", expired.toString());
//                            res.setStatus(HttpServletResponse.SC_NO_CONTENT);
//                        })
//                );
//
//        return http.build();
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();


        config.setAllowedOrigins(List.of("http://localhost:8080"));
        config.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(Arrays.asList(
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                "Access-Control-Allow-Headers",
                "X-Requested-With",
                "observe"
        ));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}


