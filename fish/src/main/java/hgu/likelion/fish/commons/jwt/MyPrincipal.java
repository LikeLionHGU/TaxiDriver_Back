package hgu.likelion.fish.commons.jwt;



import java.io.Serializable;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

import hgu.likelion.fish.commons.login.config.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class MyPrincipal implements Principal, Serializable {
    private final String userId;
    private final Set<Role> roles;
    private final int version;

    public MyPrincipal(String userId, Set<Role> roles, int version) {
        this.userId = Objects.requireNonNull(userId);
        this.roles = Collections.unmodifiableSet(new LinkedHashSet<>(roles == null ? Set.of() : roles));
        this.version = version;
    }

    @Override public String getName() { return userId; }
    public String getUserId() { return userId; }
    public Set<Role> getRoles() { return roles; }
    public int getVersion() { return version; }

    /** Spring Security 인가용 권한 목록 */
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(Enum::name)                    // ROLE_USER
                .map(SimpleGrantedAuthority::new)  // 반드시 Spring의 클래스
                .collect(Collectors.toUnmodifiableSet());
    }

    // equals/hashCode/toString 필요시 구현
}

