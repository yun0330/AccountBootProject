package com.walletstory.server.controller;

import com.walletstory.server.dto.UserDTO;
import com.walletstory.server.entity.UserEntity;
import com.walletstory.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/member")
public class UserController {

    @Autowired
    private UserService userService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PostMapping("/createuser")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserEntity user = UserEntity.builder()
                .userId(userDTO.getUserId())
                .userPw(passwordEncoder.encode(userDTO.getUserPw()))
                .userEmail(userDTO.getUserEmail())
                .userName(userDTO.getUserName())
                .userPhone(userDTO.getUserPhone())
                .nickName(userDTO.getNickName())
                .enrollDate(LocalDateTime.now())
//                .profile(userDTO.getProfile())
//                .userType(userDTO.getUserType())
                .build();

        UserEntity createUser = userService.create(user);

        UserDTO createUserDTO = UserDTO.builder()
                .userId(createUser.getUserId())
                .build();
        return ResponseEntity.ok(createUserDTO);
    }
    @GetMapping("/checkid")
    public ResponseEntity<Boolean> checkUserId(@RequestParam("userId")String userId) {
        boolean existsUser = userService.existsByUserId(userId);
        return ResponseEntity.ok(existsUser);
    }
}
