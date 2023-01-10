package com.example.myapplication.dto;

public class PasswordResetCodeDTO {

    private String email;
    private String newPassword;
    private String code;

    public PasswordResetCodeDTO(String email, String newPassword, String code) {
        this.email = email;
        this.newPassword = newPassword;
        this.code = code;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
