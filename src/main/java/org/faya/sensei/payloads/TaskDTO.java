package org.faya.sensei.payloads;

import org.faya.sensei.entities.TaskEntity;

import java.time.LocalDateTime;

public final class TaskDTO {

    private Integer id;

    private String title;

    private String description;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private String status;

    private Integer projectId;

    private Integer assignerId;

    // Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getAssignerId() {
        return assignerId;
    }

    public void setAssignerId(Integer assignerId) {
        this.assignerId = assignerId;
    }

    // Converters

    public static TaskEntity toEntity(final TaskDTO taskDTO) {
        TaskEntity taskEntity = new TaskEntity();

        taskEntity.setTitle(taskDTO.getTitle());
        taskEntity.setDescription(taskDTO.getDescription());
        taskEntity.setStartDate(taskDTO.getStartDate());
        taskEntity.setEndDate(taskDTO.getEndDate());

        return taskEntity;
    }

    public static TaskDTO fromEntity(final TaskEntity taskEntity) {
        TaskDTO taskDTO = new TaskDTO();

        taskDTO.setId(taskEntity.getId());
        taskDTO.setTitle(taskEntity.getTitle());
        taskDTO.setDescription(taskEntity.getDescription());
        taskDTO.setStartDate(taskEntity.getStartDate());
        taskDTO.setEndDate(taskEntity.getEndDate());
        if (taskEntity.getStatus() != null) taskDTO.setStatus(taskEntity.getStatus().getName());
        if (taskEntity.getProject() != null) taskDTO.setProjectId(taskEntity.getProject().getId());
        if (taskEntity.getAssigner() != null) taskDTO.setAssignerId(taskEntity.getAssigner().getId());

        return taskDTO;
    }
}
