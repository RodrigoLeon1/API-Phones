package com.phones.phones.dto;

import com.phones.phones.model.City;
import com.phones.phones.model.UserRole;
import lombok.*;

import java.util.Set;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserDto {

    private String name;
    private String surname;
    private String dni;
    private String username;
    private String password;
    private City city;
    private Set<UserRole> userRoles;
    private boolean active;

}
