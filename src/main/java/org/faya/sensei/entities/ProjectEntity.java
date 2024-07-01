package org.faya.sensei.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "projects")
public class ProjectEntity implements Serializable {

    /**
     * The primary key of the project.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The name of the project.
     */
    private String name;

    /**
     * The many-to-many relationship with {@link UserEntity}. (hidden in database)
     */
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "projects_users",
            joinColumns = @JoinColumn(name = "project_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> users = new ArrayList<>();

    /**
     * The one-to-many relationship with {@link StatusEntity}. (hidden in database)
     */
    @OneToMany(mappedBy = "project")
    private List<StatusEntity> statuses = new ArrayList<>();

    /**
     * The one-to-many relationship with {@link TaskEntity}. (hidden in database)
     */
    @OneToMany(mappedBy = "project")
    private List<TaskEntity> tasks = new ArrayList<>();

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

    public List<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<UserEntity> users) {
        this.users = users;
    }

    public List<StatusEntity> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<StatusEntity> statuses) {
        this.statuses = statuses;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}
