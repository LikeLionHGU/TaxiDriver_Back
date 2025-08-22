package hgu.likelion.fish.commons.security;



import hgu.likelion.fish.commons.jwt.JwtCookieAuthFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtCookieAuthFilter jwtCookieAuthFilter) throws Exception {

        // 1) 쿠키에 XSRF-TOKEN 발급 (HttpOnly=false → 프론트/포스트맨에서 읽어 헤더로 보낼 수 있음)
        var repo = CookieCsrfTokenRepository.withHttpOnlyFalse();

        // 2) 헤더에는 "마스킹되지 않은(raw) 토큰"을 기대하도록 설정
        var requestHandler = new CsrfTokenRequestAttributeHandler();
        requestHandler.setCsrfRequestAttributeName("_csrf"); // (선택) 요청 attribute 이름

        http
                .cors(c -> c.configurationSource(corsConfigurationSource()))
                .csrf(csrf -> csrf
                        .csrfTokenRepository(repo)
                        .csrfTokenRequestHandler(requestHandler)   // ★ 핵심: raw 토큰을 받아들임
                )
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(h -> h.disable())
                .formLogin(f -> f.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login/oauth2/**", "/api/v1/oauth2/google").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/temp", "/test", "/user/update").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class)

                // (선택) 3) 항상 XSRF-TOKEN 쿠키가 내려가도록 보조 필터
                //  - GET으로 한 번만 방문해도 쿠키가 보장되게 함
                .addFilterAfter(new OncePerRequestFilter() {
                    @Override
                    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
                            throws ServletException, IOException {
                        // 이 attribute를 한 번 읽으면 CookieCsrfTokenRepository가 쿠키를 셋업함
                        req.getAttribute(org.springframework.security.web.csrf.CsrfToken.class.getName());
                        chain.doFilter(req, res);
                    }
                }, CsrfFilter.class);

        return http.build();
    }
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http, JwtCookieAuthFilter jwtCookieAuthFilter) throws Exception {
//        http
//                .cors(c -> c.configurationSource(corsConfigurationSource()))
//                .csrf(csrf -> csrf
//                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        // 필요 시 일부 엔드포인트만 예외
////                         .ignoringRequestMatchers("/temp")
//                )
//                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ★ 무상태
//                .httpBasic(h -> h.disable())
//                .formLogin(f -> f.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login/oauth2/**", "/api/v1/oauth2/google").permitAll()
//                        .requestMatchers("/temp").permitAll()
//                        .requestMatchers("/test").permitAll()
//                        .requestMatchers("/user/update").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtCookieAuthFilter, UsernamePasswordAuthenticationFilter.class); // ★ 쿠키필터
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


