package ru.kata.spring.boot_security.demo.dto;


import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class AdminPasswordChangeDto {
    @NotNull
    private Long id;
    @NotEmpty
    @Size (min = 5, max = 50, message = "Password should be between 5 and 50 characters")
    private String password;
    @NotEmpty
    @Size (min = 5, max = 50, message = "Password should be between 5 and 50 characters")
    private String confirmPassword;

    public AdminPasswordChangeDto(Long id) {
        this.id = id;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getConfirmPassword() {
        return confirmPassword;
    }
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
}
