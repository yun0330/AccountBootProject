package com.walletstory.server.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user")
public class UserEntity {
    @Id
    @Column(name = "user_id",length = 20,nullable = false)
    private String userId;

    @Column(name = "user_pw", nullable = false)
    private String userPw;

    @Column(name = "user_email", nullable = false)
    private String userEmail;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @Column(name = "nickname", nullable = false)
    private String nickName;

    @Column(name = "enroll_date", nullable = false)
    private LocalDateTime enrollDate;

    @Column(name = "login_token",length = 1000)
    private String loginToken;

//    @Column(name = "profile")
//    private String profile;

//    @Column(name = "social_id")
//    private String socialId;

//    @Column(name = "user_type")
//    private String userType;
}
