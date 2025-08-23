package hgu.likelion.fish.user.application.service;

import hgu.likelion.fish.commons.login.config.Role;
import hgu.likelion.fish.user.application.dto.UserDto;
import hgu.likelion.fish.user.domain.entity.User;
import hgu.likelion.fish.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public User findUserById(String userId) {

        return userRepository.findById(userId).orElse(null);
    }

    @Transactional
    public Boolean updateAdminUser(UserDto userDto) {

        try {
            User user = userRepository.findUserByUserId(userDto.getId());
            user.setName(userDto.getName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setLocation(userDto.getLocation());

            user.addRole(Role.ROLE_ADMIN); // admin 권한 부여

            userRepository.save(user);

            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Transactional
    public Boolean updateSellerUser(UserDto userDto) {
        try {
            User user = userRepository.findUserByUserId(userDto.getId());
            user.setName(userDto.getName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setLocation(userDto.getLocation());

            user.addRole(Role.ROLE_SELLER); // seller 권한 부여

            userRepository.save(user);

            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }

    @Transactional
    public Boolean updateBuyerUser(UserDto userDto) {
        try {
            User user = userRepository.findUserByUserId(userDto.getId());
            user.setName(userDto.getName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setLocation(userDto.getLocation());

            user.addRole(Role.ROLE_BUYER); // seller 권한 부여

            userRepository.save(user);

            return Boolean.TRUE;
        } catch (Exception e) {
            return Boolean.FALSE;
        }
    }


}
