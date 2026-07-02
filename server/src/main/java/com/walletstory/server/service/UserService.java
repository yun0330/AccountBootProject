package com.walletstory.server.service;

import com.walletstory.server.entity.UserEntity;
import com.walletstory.server.exception.CustomException;
import com.walletstory.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserEntity create(final UserEntity userEntity) {
        if (userEntity.getUserId() == null) {
            throw new CustomException("아이디를 입력해주세요.", HttpStatus.BAD_REQUEST);
        }
        final String userId = userEntity.getUserId();
        if (userRepository.existsById(userId)){
            throw new CustomException("이미 사용중인 아이디입니다.",HttpStatus.CONFLICT);
        }
        return userRepository.save(userEntity);
    }
    public boolean existsByUserId(String userId) {
        return userRepository.existsById(userId);
    }
}
