package org.faya.sensei.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class UserEntity implements Serializable {

    /**
     * The primary key of the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The password of the user.
     */
    private String password;

    /**
     * The role of the user.
     */
    @Enumerated(EnumType.STRING)
    private UserRole role;

    /**
     * The many-to-many relationship with {@link ProjectEntity}. (hidden in database)
     */
    @ManyToMany(mappedBy = "users")
    private List<ProjectEntity> projects = new ArrayList<>();

    /**
     * The one-to-many relationship with {@link ProjectEntity}. (hidden in database)
     */
    @OneToMany(mappedBy = "assigner")
    private List<TaskEntity> assignedTasks = new ArrayList<>();

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public List<ProjectEntity> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectEntity> projects) {
        this.projects = projects;
    }

    public List<TaskEntity> getAssignedTasks() {
        return assignedTasks;
    }

    public void setAssignedTasks(List<TaskEntity> assignedTasks) {
        this.assignedTasks = assignedTasks;
    }
}
