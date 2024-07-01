package org.faya.sensei.payloads;

import org.faya.sensei.entities.StatusEntity;

public final class StatusDTO {

    private Integer id;

    private String name;

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

    // Converters

    public static StatusEntity toEntity(final StatusDTO statusDTO) {
        StatusEntity statusEntity = new StatusEntity();

        statusEntity.setName(statusDTO.getName());

        return statusEntity;
    }

    public static StatusDTO fromEntity(final StatusEntity statusEntity) {
        StatusDTO statusDTO = new StatusDTO();

        statusDTO.setId(statusEntity.getId());
        statusDTO.setName(statusEntity.getName());

        return statusDTO;
    }
}
