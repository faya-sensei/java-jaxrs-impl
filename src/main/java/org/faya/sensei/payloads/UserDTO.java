package org.faya.sensei.payloads;

import jakarta.json.bind.annotation.JsonbTransient;
import org.faya.sensei.entities.UserEntity;

public final class UserDTO {

    private Integer id;

    private String name;

    private String password;

    private String role;

    private String token;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonbTransient
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    @JsonbTransient
    public void setRole(String role) {
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    @JsonbTransient
    public void setToken(String token) {
        this.token = token;
    }

    // Converters

    public static UserEntity toEntity(final UserDTO userDTO) {
        UserEntity userEntity = new UserEntity();

        userEntity.setName(userDTO.getName());

        return userEntity;
    }

    public static UserDTO fromEntity(final UserEntity userEntity) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(userEntity.getId());
        userDTO.setName(userEntity.getName());

        return userDTO;
    }
}
