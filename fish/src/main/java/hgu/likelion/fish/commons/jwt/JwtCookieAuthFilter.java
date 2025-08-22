package hgu.likelion.fish.commons.jwt;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtCookieAuthFilter extends OncePerRequestFilter {

    private final SessionJwtService sessionJwtService; // 서명 검증, 파싱, 버전 체크 등

    // 공개 엔드포인트는 스킵
    private static final AntPathMatcher matcher = new AntPathMatcher();
    private static final String[] SKIP = {
            "/login/oauth2/**", "/api/v1/oauth2/google", "/health", "/public/**"
    };

    public JwtCookieAuthFilter(SessionJwtService sessionJwtService) {
        this.sessionJwtService = sessionJwtService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        for (String p : SKIP) if (matcher.match(p, path)) return true;
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        String token = readCookie(req, "SESSION");

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                MyPrincipal principal = sessionJwtService.parseAndValidate(token); // userId, roles(["ROLE_USER"]), version

                var auth = UsernamePasswordAuthenticationToken.authenticated(
                        principal,
                        null,                          // JWT니까 credentials 없음
                        principal.getAuthorities()     // 여기서 ROLE_ 접두사 포함된 Authority 생성됨
                );


                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (ExpiredJwtException e) {
                SecurityContextHolder.clearContext();
            } catch (JwtException e) {
                SecurityContextHolder.clearContext();
            }
        }
        CsrfToken csrf = (CsrfToken) req.getAttribute(CsrfToken.class.getName());
        if (csrf != null) {
            // 필요시 response header에도 넣어줄 수 있음
            res.setHeader("X-CSRF-TOKEN", csrf.getToken());
        }

        req.getAttribute(org.springframework.security.web.csrf.CsrfToken.class.getName());
        chain.doFilter(req, res);
    }

    private String readCookie(HttpServletRequest req, String name) {
        if (req.getCookies() == null) return null;
        for (Cookie c : req.getCookies()) if (name.equals(c.getName())) return c.getValue();
        return null;
    }
}