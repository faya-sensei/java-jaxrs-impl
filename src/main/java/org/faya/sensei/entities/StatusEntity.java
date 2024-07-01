package org.faya.sensei.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "statuses")
public class StatusEntity implements Serializable {

    /**
     * The primary key of the status.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * The name of the status.
     */
    private String name;

    /**
     * The many-to-one relationship with {@link ProjectEntity}.
     */
    @ManyToOne
    @JoinColumn(name = "project_id")
    private ProjectEntity project;

    /**
     * The one-to-many relationship with {@link TaskEntity}. (hidden in database)
     */
    @OneToMany(mappedBy = "status")
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

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

    public List<TaskEntity> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskEntity> tasks) {
        this.tasks = tasks;
    }
}
