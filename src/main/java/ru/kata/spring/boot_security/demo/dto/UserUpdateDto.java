package ru.kata.spring.boot_security.demo.dto;

import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserUpdateDto {
    @NotNull
    private Long id;
    @NotEmpty (message = "Field name can't be empty")
    @Size (min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String firstName;
    @NotEmpty (message = "Field lastname can't be empty")
    @Size (min = 2, max = 30, message = "Lastname should be between 2 and 30 characters")
    private String lastName;
    @NotEmpty (message = "Field e-mail can't be empty")
    @Size (min = 5, max = 50, message = "E-mail should be between 5 and 50 characters")
    private String email;

    private String newPassword = "";
    private String confirmPassword = "";
    @NotEmpty
    private Set <Long> roleID = new HashSet <>();

    public UserUpdateDto() {

    }

    public UserUpdateDto (User user) {
        this.id = user.getId();
        this.firstName = user.getName();
        this.lastName = user.getLastname();
        this.email = user.getEmail();
        this.roleID = user.getSetOfRoles().stream().map(Role::getId).collect(Collectors.toSet());
    }

    public Long getId () {
        return id;
    }

    public void setId (Long id) {
        this.id = id;
    }

    public String getFirstName () {
        return firstName;
    }

    public void setFirstName (String firstName) {
        this.firstName = firstName;
    }

    public String getLastName () {
        return lastName;
    }

    public void setLastName (String lastName) {
        this.lastName = lastName;
    }

    public String getEmail () {
        return email;
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getNewPassword () {
        return newPassword;
    }

    public void setNewPassword (String password) {
        this.newPassword = password;
    }

    public String getConfirmPassword () {
        return confirmPassword;
    }

    public void setConfirmPassword (String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public Set <Long> getRoleID () {
        return roleID;
    }

    public void setRoleID (Set <Long> roleID) {
        this.roleID = roleID;
    }

    @Override
    public String toString () {
        return ("UserUpdateDto: " + firstName + " " + lastName + " " + email + " " + newPassword + " " + id + " " + roleID);
    }
}
