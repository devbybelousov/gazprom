package com.gazprom.system.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "email")
    private String email;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    public User( String userName, String password, String name, String lastName, String middleName, Set<Role> roles) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.middleName = middleName;
        this.roles = roles;
    }

    public User(String userName, String password, String name, String lastName,
                String middleName, Set<Role> roles, Department department) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.lastName = lastName;
        this.middleName = middleName;
        this.roles = roles;
        this.department = department;
    }

    public User(String userName, String password){
        this.userName = userName;
        this.password = password;
    }
}
