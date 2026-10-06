package com.walletstory.server.controller;

import com.walletstory.server.dto.UserDTO;
import com.walletstory.server.entity.UserEntity;
import com.walletstory.server.security.TokenProvision;
import com.walletstory.server.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TokenProvision tokenProvision;

    @PostMapping("/createuser")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserEntity user = UserEntity.builder()
                .userId(userDTO.getUserId())
                .userPw(userDTO.getUserPw())
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

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserDTO userDTO) {
        UserEntity user = userService.getByCredentials(
                userDTO.getUserId(),
                userDTO.getUserPw()
        );

        String accessToken = tokenProvision.createAccessToken(user);
        String refreshToken = tokenProvision.createRefreshToken(user);

        user.setLoginToken(refreshToken);
        userService.updateUser(user);

        final UserDTO responseUserDTO = UserDTO.builder()
                .userId(user.getUserId())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        return ResponseEntity.ok(responseUserDTO);
    }

    @PostMapping("/refresh")
    public ResponseEntity<UserDTO> refresh(@RequestBody UserDTO userDTO) {
        UserDTO responseUserDTO = userService.refreshToken(userDTO.getRefreshToken());
        return ResponseEntity.ok(responseUserDTO);
    }
}
