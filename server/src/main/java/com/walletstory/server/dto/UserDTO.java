package com.walletstory.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String userId;
    private String userPw;
    private String userEmail;
    private String userName;
    private String userPhone;
    private String nickName;
//    private String profile;
//    private String loginToken;
//    private String socialId;
//    private String userType;
}
