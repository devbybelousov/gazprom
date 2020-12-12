package com.gazprom.system.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {
    private Long userId;
    private String userName;
    private String email;
    private String name;
    private String lastName;
    private String middleName;
    private String department;
}
