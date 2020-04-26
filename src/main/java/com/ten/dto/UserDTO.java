package com.ten.dto;

public class UserDTO {

    private Long id;
    private String login;
    private String password;
    private Long role;

    public Long getId() {
        return id;
    }

    public String getPassword() {
        return password;
    }

    public String getLogin() {
        return login;
    }

    public Long getRole() {
        return role;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setRole(Long role) {
        this.role = role;
    }
}
