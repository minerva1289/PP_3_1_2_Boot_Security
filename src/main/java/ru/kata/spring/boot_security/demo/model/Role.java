package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import javax.validation.constraints.Pattern;

@Entity
@Table (name = "roles")
@Component
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    @Column (unique = true)
    @Pattern(regexp = "^ROLE_[A-Z]+$", message = "Формат роли должен быть 'ROLE_НАЗВАНИЕ_ЗАГЛАВНЫМИ_БУКВАМИ'")
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
