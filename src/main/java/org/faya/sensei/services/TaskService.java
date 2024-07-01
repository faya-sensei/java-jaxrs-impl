package org.faya.sensei.services;

import jakarta.inject.Inject;
import org.faya.sensei.entities.ProjectEntity;
import org.faya.sensei.entities.StatusEntity;
import org.faya.sensei.entities.TaskEntity;
import org.faya.sensei.entities.UserEntity;
import org.faya.sensei.payloads.TaskDTO;
import org.faya.sensei.repositories.IRepository;

import java.util.Optional;

public class TaskService implements IService<TaskDTO> {

    @Inject
    private IRepository<UserEntity> userRepository;

    @Inject
    private IRepository<ProjectEntity> projectRepository;

    @Inject
    private IRepository<StatusEntity> statusRepository;

    @Inject
    private IRepository<TaskEntity> taskRepository;

    public Optional<TaskDTO> create(final TaskDTO dto) {
        TaskEntity taskEntity = TaskDTO.toEntity(dto);

        final Optional<ProjectEntity> projectEntity = projectRepository.get(dto.getProjectId());
        if (projectEntity.isEmpty()) throw new IllegalArgumentException("Board does not exist");
        taskEntity.setProject(projectEntity.get());

        final Optional<StatusEntity> statusEntity = statusRepository.get(dto.getStatus());
        if (statusEntity.isEmpty()) throw new IllegalArgumentException("Status does not exist");
        taskEntity.setStatus(statusEntity.get());

        if (dto.getAssignerId() != null) {
            final Optional<UserEntity> userEntity = userRepository.get(dto.getAssignerId());
            userEntity.ifPresent(taskEntity::setAssigner);
        }

        return taskRepository.post(taskEntity) > 0
                ? Optional.of(TaskDTO.fromEntity(taskEntity))
                : Optional.empty();
    }

    public Optional<TaskDTO> update(final int id, final TaskDTO dto) {
        TaskEntity taskEntity = TaskDTO.toEntity(dto);

        if (dto.getStatus() != null) {
            final Optional<StatusEntity> statusEntity = statusRepository.get(dto.getStatus());
            if (statusEntity.isEmpty()) throw new IllegalArgumentException("Status does not exist");
            taskEntity.setStatus(statusEntity.get());
        }

        if (dto.getAssignerId() != null) {
            final Optional<UserEntity> userEntity = userRepository.get(dto.getAssignerId());
            if (userEntity.isEmpty()) throw new IllegalArgumentException("Assigner does not exist");
            taskEntity.setAssigner(userEntity.get());
        }

        final Optional<TaskEntity> updatedTaskEntity = taskRepository.put(id, taskEntity);

        return updatedTaskEntity.map(TaskDTO::fromEntity);
    }

    @Override
    public boolean remove(int id) {
        final Optional<TaskEntity> taskEntity = taskRepository.delete(id);

        return taskEntity.isPresent();
    }
}
