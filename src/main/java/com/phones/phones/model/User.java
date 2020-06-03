package com.phones.phones.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String surname;

    @NotNull
    private String dni;

    @NotNull
    private String username;

    @NotNull
    private String password;

    @JsonIgnore
    private Date creationDate;

    @Column(columnDefinition = "boolean default true")
    private boolean isActive;

    @NotNull
    @JsonBackReference(value = "cityUser")
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_city", nullable = false)
    private City city;

    @NotNull
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "users_x_user_roles",
            joinColumns = @JoinColumn(name = "id_user") ,
            inverseJoinColumns = @JoinColumn(name = "id_user_role")
    )
    private List<UserRole> userRoles = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Line> lines;


    public boolean hasRoleClient() {
        return userRoles
                .stream()
                .anyMatch(userRole -> userRole.getRole().contains("Client"));
    }

    public boolean hasRoleEmployee() {
        return userRoles
                .stream()
                .anyMatch(userRole -> userRole.getRole().contains("Employee"));
    }

}
