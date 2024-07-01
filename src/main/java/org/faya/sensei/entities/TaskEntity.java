package org.faya.sensei.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
public class TaskEntity implements Serializable {

    /**
     * The primary key of the task.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The summary title of the task.
     */
    private String title;

    /**
     * The detail description of the task.
     */
    private String description;

    /**
     * The start date time of the task.
     */
    private LocalDateTime startDate;

    /**
     * The due date time of the task.
     */
    private LocalDateTime endDate;

    /**
     * The many-to-one relationship with {@link ProjectEntity}.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    /**
     * The many-to-one relationship with {@link StatusEntity}.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "status_id")
    private StatusEntity status;

    /**
     * The many-to-one relationship with {@link UserEntity}.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "assigner_id")
    private UserEntity assigner;

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

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public StatusEntity getStatus() {
        return status;
    }

    public void setStatus(StatusEntity status) {
        this.status = status;
    }

    public UserEntity getAssigner() {
        return assigner;
    }

    public void setAssigner(UserEntity assigner) {
        this.assigner = assigner;
    }
}
