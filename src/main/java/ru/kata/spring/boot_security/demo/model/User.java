package ru.kata.spring.boot_security.demo.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.dto.UserUpdateDto;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
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
    @NotNull //@NotEmpty (message = "Field name can't be empty")
    //@Size (min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    @Size(max = 30)
    private String name;

    @Column
    @NotNull //@NotEmpty (message = "Field lastname can't be empty")
    //@Size (min = 2, max = 30, message = "Lastname should be between 2 and 30 characters")
    @Size(max = 30)
    private String lastname;

    @Column (unique = true)
    @NotNull //@NotEmpty (message = "Field e-mail can't be empty")
    //@Size (min = 5, max = 50, message = "E-mail should be between 5 and 50 characters")
    @Size(max = 50)
    private String email;

    @Column (unique = true)
    @NotNull //@NotEmpty
    //@Size (min = 5, max = 255, message = "Password should be between 5 and 50 characters")
    @Size(max = 255)
    private String password;


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_roles", // Промежуточная таблица
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )

    //set с ролями
    private Set<Role> setOfRoles = new HashSet<>();


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

//    public Set <Long> getRolesID () {
//        return rolesID;
//    }

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
        System.out.println("CALL getAuthorities");
        return setOfRoles.stream().map(role -> new SimpleGrantedAuthority(role.getAuthority())).collect(Collectors.toList());
    }

    public Set<Role> getSetOfRoles () {
        return setOfRoles;
    }
    public void setSetOfRoles (Set<Role> authorities) {
        this.setOfRoles = authorities;
    }
//    public void setRole (Role role) {
//        this.authorities.add(role);
//    }

    @Override
    public String getPassword () {
        return password;
    }
    public void setPassword (String password) {
        this.password = password;
    }

    @Override
    public String getUsername () {
        return getName();
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

    public void updateData(UserUpdateDto userUpdateDto) {
        this.name = userUpdateDto.getFirstName();
        this.lastname = userUpdateDto.getLastName();
        this.email = userUpdateDto.getEmail();
    }

}
