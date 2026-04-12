package com.app.catapi.domain.entity.user;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private UserRole role;
    private String firstName;
    private String lastName;
}
