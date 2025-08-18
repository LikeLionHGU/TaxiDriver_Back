package hgu.likelion.fish.commons.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final JWTProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtProvider.resolveToken(request);
        String requestURI = request.getRequestURI();

        if (requestURI.startsWith("/login/oauth2/") || requestURI.startsWith("/api/v1/oauth2/google") || requestURI.startsWith("https://sseuim/") || requestURI.startsWith("/project/get/best")) {
            filterChain.doFilter(request, response);
            return; // 이후 로직을 실행하지 않고 리턴
        }



        if (token != null && jwtProvider.validateToken(token)) {

            // check access token
            token = token.split(" ")[1].trim();

            Authentication auth = jwtProvider.getAuthentication(token);

            SecurityContextHolder.getContext().setAuthentication(auth);

        } else {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 상태 코드 설정
            httpResponse.setContentType("application/json");
            httpResponse.setCharacterEncoding("UTF-8");
            httpResponse.getWriter().write("토큰이 만료되었거나 올바르지 않습니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }
}