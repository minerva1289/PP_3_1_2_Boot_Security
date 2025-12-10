package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserUpdateDto;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table (name = "users")
@Component
public class User implements UserDetails {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (unique = true, nullable = false, updatable = false)
    private String businessKey;

    @Column
    @NotNull
    @Size (max = 30)
    private String name;

    @Column
    @NotNull
    @Size (max = 30)
    private String lastname;

    @Column (unique = true)
    @NotNull
    @Size (max = 50)
    private String email;

    @Column (unique = true)
    @NotNull
    @Size (max = 255)
    private String password;


    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable (
            name = "user_roles",
            joinColumns = @JoinColumn (name = "user_id"),
            inverseJoinColumns = @JoinColumn (name = "role_id")
    )
    //set с ролями
    private Set <Role> setOfRoles = new HashSet <>();

    public User () {
        this.businessKey = UUID.randomUUID().toString();
    }

    public User (String name, String lastname, String email, String password) {
        this();
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public long getId () {
        return id;
    }

    public void setId (long id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getLastname () {
        return lastname;
    }

    public void setLastname (String lastname) {
        this.lastname = lastname;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    @Override
    public String toString () {
        return "user id: " + id + ", name: " + name + ", lastname: " + lastname + ", email: " + email + "roles: " + setOfRoles;
    }

    @Override
    public boolean equals (Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(businessKey, user.businessKey);
    }

    @Override
    public int hashCode () {
        return Objects.hash(businessKey);
    }

    @Override
    public Collection <? extends GrantedAuthority> getAuthorities () {
        return setOfRoles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }

    public Set <Role> getSetOfRoles () {
        return setOfRoles;
    }

    public void setSetOfRoles (Set <Role> authorities) {
        this.setOfRoles = authorities;
    }

    @Override
    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
    }

    @Override
    public String getUsername () {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired () {
        return true;
    }

    @Override
    public boolean isAccountNonLocked () {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired () {
        return true;
    }

    @Override
    public boolean isEnabled () {
        return true;
    }

    public void updateData (UserUpdateDto userUpdateDto) {
        this.name = userUpdateDto.getFirstName();
        this.lastname = userUpdateDto.getLastName();
        this.email = userUpdateDto.getEmail();
    }

}
