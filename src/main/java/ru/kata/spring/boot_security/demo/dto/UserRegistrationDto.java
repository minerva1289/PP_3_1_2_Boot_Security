package ru.kata.spring.boot_security.demo.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

public class UserRegistrationDto {
    @NotEmpty (message = "Field name can't be empty")
    @Size (min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String firstName;
    @NotEmpty (message = "Field lastname can't be empty")
    @Size (min = 2, max = 30, message = "Lastname should be between 2 and 30 characters")
    private String lastName;
    @NotEmpty (message = "Field e-mail can't be empty")
    @Size (min = 5, max = 50, message = "E-mail should be between 5 and 50 characters")
    private String email;
    @NotEmpty (message = "Field can't be empty")
    @Size (min = 5, max = 50, message = "Password should be between 5 and 50 characters")
    private String password;
    @NotEmpty (message = "Field can't be empty")
    @Size (min = 5, max = 50, message = "Password should be between 5 and 50 characters")
    private String confirmPassword;
    private Set <Long> roleID = new HashSet <>();

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

    public String getPassword () {
        return password;
    }

    public void setPassword (String password) {
        this.password = password;
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
        return "userDto" + firstName + " " + lastName + " " + email;
    }

}
