package hgu.likelion.fish.commons.jwt;


import hgu.likelion.fish.user.domain.entity.User;
import hgu.likelion.fish.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JPAUserService {
    private final UserRepository userRepository;

    public UserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {

        User user = userRepository.findById(userId).orElseThrow(
                () -> new UsernameNotFoundException("Invalid authentication!")
        );

        return new CustomUserDetails(user);
    }
}
