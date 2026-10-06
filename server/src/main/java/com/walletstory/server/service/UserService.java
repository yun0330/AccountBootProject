package com.walletstory.server.service;

import com.walletstory.server.dto.UserDTO;
import com.walletstory.server.entity.UserEntity;
import com.walletstory.server.exception.CustomException;
import com.walletstory.server.repository.UserRepository;
import com.walletstory.server.security.TokenProvision;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvision tokenProvision;

    public UserEntity create(final UserEntity userEntity) {
        if (userEntity.getUserId() == null) {
            throw new CustomException("아이디를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        final String userId = userEntity.getUserId();
        if (userRepository.existsById(userId)){
            throw new CustomException("이미 사용중인 아이디입니다.",HttpStatus.CONFLICT);
        }
        userEntity.setUserPw(passwordEncoder.encode(userEntity.getUserPw()));
        return userRepository.save(userEntity);
    }
    public boolean existsByUserId(String userId) {
        return userRepository.existsById(userId);
    }
    public UserEntity getByCredentials(
            final String userId,
            final String userPw
    ) { final UserEntity originalUser = userRepository.findByUserId(userId);

    if (originalUser == null || !passwordEncoder.matches(userPw, originalUser.getUserPw())) {
        throw new CustomException("아이디 또는 비밀번호가 올바르지 않습니다.",
                HttpStatus.UNAUTHORIZED);
    }
    return originalUser;
    }

    public void updateUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    @Transactional
    public UserDTO refreshToken(String oldRefreshToken) {
        if (oldRefreshToken == null || oldRefreshToken.isBlank()) {
            throw new CustomException("Refresh 토큰이 없습니다.",HttpStatus.UNAUTHORIZED);
        }
        String userId = tokenProvision.validateAndGetUserId(oldRefreshToken, "refresh");
        UserEntity user = userRepository.findByUserId(userId);

        if (user == null) {
            throw new CustomException("사용자를 찾을 수 없습니다.",HttpStatus.UNAUTHORIZED);
        }

        if (user.getLoginToken() == null || !oldRefreshToken.equals(user.getLoginToken())) {
            user.setLoginToken(null);
            userRepository.save(user);

            throw new CustomException("Refresh 토큰이 유효하지 않습니다.",HttpStatus.UNAUTHORIZED);
        }
        String newAccessToken = tokenProvision.createAccessToken(user);
        String newRefreshToken = tokenProvision.createRefreshToken(user);

        user.setLoginToken(newRefreshToken);
        userRepository.save(user);

        return UserDTO.builder()
                .userId(user.getUserId())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

}
