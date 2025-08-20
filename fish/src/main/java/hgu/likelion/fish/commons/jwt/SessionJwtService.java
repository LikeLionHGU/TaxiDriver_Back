package hgu.likelion.fish.commons.jwt;


import hgu.likelion.fish.commons.login.config.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SessionJwtService {
    @Value("${jwt.secret.key}")
    private String salt;


    // JWT 발급
    public String issue(String userId, List<String> roles, long expirySeconds, int tokenVersion) {
        Instant now = Instant.now();
        return Jwts.builder()
                .setSubject(userId)
                .claim("roles", roles)
                .claim("ver", tokenVersion)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusSeconds(expirySeconds)))
                .signWith(Keys.hmacShaKeyFor(salt.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    public List<GrantedAuthority> toAuthorities(Claims claims) {
        Object raw = claims.get("roles");
        if (raw == null) return List.of();

        // roles가 ["ROLE_USER", "ADMIN"] 혹은 "ROLE_USER,ADMIN" 등 다양한 형태로 와도 처리
        Stream<String> roleStream;

        if (raw instanceof Collection<?> col) {
            roleStream = col.stream().map(String::valueOf);
        } else if (raw instanceof String s) {
            roleStream = Arrays.stream(s.split(","));
        } else {
            // 알 수 없는 타입이면 빈 권한
            return List.of();
        }

        Function<String, String> normalize = r -> {
            if (r == null) return null;
            String up = r.trim().toUpperCase(Locale.ROOT);
            if (up.isEmpty()) return null;
            return up.startsWith("ROLE_") ? up : "ROLE_" + up;
        };

        // 중복 제거(LinkedHashSet) → SimpleGrantedAuthority로 변환 → 불변 리스트
        var uniqueRoles = roleStream
                .map(normalize)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return uniqueRoles.stream()
                .map(SimpleGrantedAuthority::new)  // 반드시 Spring의 클래스 import
                .collect(Collectors.toUnmodifiableList());
    }


    // JWT 검증 & 파싱
    public MyPrincipal parseAndValidate(String token) {
        try {
            // 키는 미리 @PostConstruct에서 Keys.hmacShaKeyFor(...)로 만들어 두고 필드로 재사용 권장
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(salt.getBytes(StandardCharsets.UTF_8)))
                    // .setAllowedClockSkewSeconds(30) // 필요 시 시간 드리프트 허용
                    .build()
                    .parseClaimsJws(token);

            Claims c = jws.getBody();

            // subject (userId)
            String userId = c.getSubject();
            if (userId == null || userId.isBlank()) {
                throw new JwtException("JWT subject (userId) is missing");
            }

            // roles: 문자열/리스트/널 등 다양한 입력 방어
            Object raw = c.get("roles");
            Set<Role> roles = toEnumRoles(raw);

            // token version (없으면 0 등 기본값)
            Integer verObj = c.get("ver", Integer.class);
            int version = (verObj != null ? verObj : 0);

            // (선택) 서버 저장 버전과 비교하여 무효화 검증
            // if (!tokenVersionService.isValid(userId, version)) { throw new JwtException("Token version mismatch"); }

            return new MyPrincipal(userId, roles, version);

        } catch (JwtException e) {
            // 서명 불일치/만료/형식 오류 등 포함
            throw new RuntimeException("Invalid or expired JWT", e);
        }
    }

    /** roles 클레임(Object) → Set<Role>로 안전 변환 */
    private Set<Role> toEnumRoles(Object raw) {
        if (raw == null) return Set.of();

        // 문자열 스트림으로 통일
        final List<String> roleNames;
        if (raw instanceof Collection<?> col) {
            roleNames = col.stream().map(String::valueOf).toList();
        } else if (raw instanceof String s) {
            roleNames = Arrays.stream(s.split(",")).map(String::valueOf).toList();
        } else {
            return Set.of();
        }

        return roleNames.stream()
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> {
                    String up = s.toUpperCase(Locale.ROOT);
                    return up.startsWith("ROLE_") ? up : "ROLE_" + up; // 접두사 보정
                })
                .map(Role::valueOf) // "ROLE_USER" -> Role.ROLE_USER
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }


    // 토큰 버전 증가(로그아웃, 강제 만료 등)
    public void revokeForUser(String userId) {
        // DB에 userId별 tokenVersion 컬럼을 두고 +1 증가
        // parseAndValidate 할 때 클레임의 ver과 DB ver을 비교해서 mismatch면 거부
    }
}
