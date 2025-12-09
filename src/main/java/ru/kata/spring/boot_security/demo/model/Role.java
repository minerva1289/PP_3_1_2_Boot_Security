package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Entity
@Table (name = "roles")
@Component
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (unique = true)
    private String name;

    public Role () {
    }

    public Role (String role) {
        this.name = role;
    }

    @Override
    public String getAuthority () {
        return name;
    }

    public void setRole (String role) {
        this.name = role;
    }

    public Long getId () {
        return id;
    }
}
