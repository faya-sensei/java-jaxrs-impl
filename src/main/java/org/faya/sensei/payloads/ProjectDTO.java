package org.faya.sensei.payloads;

import org.faya.sensei.entities.ProjectEntity;
import org.faya.sensei.entities.TaskEntity;
import org.faya.sensei.entities.UserEntity;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class ProjectDTO {

    private Integer id;

    private String name;

    private List<Integer> ownerIds;

    private List<TaskDTO> tasks;

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

    public List<Integer> getOwnerIds() {
        return ownerIds;
    }

    public void setOwnerIds(List<Integer> ownerIds) {
        this.ownerIds = ownerIds;
    }

    public List<TaskDTO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskDTO> tasks) {
        this.tasks = tasks;
    }

    // Converters

    public static ProjectEntity toEntity(final ProjectDTO projectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();

        projectEntity.setName(projectDTO.getName());

        return projectEntity;
    }

    public static ProjectDTO fromEntity(final ProjectEntity projectEntity) {
        List<TaskEntity> taskEntities = projectEntity.getTasks();
        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setId(projectEntity.getId());
        projectDTO.setName(projectEntity.getName());
        projectDTO.setOwnerIds(projectEntity.getUsers().stream().map(UserEntity::getId).toList());
        projectDTO.setTasks(taskEntities.stream().map(TaskDTO::fromEntity).toList());

        return projectDTO;
    }
}
