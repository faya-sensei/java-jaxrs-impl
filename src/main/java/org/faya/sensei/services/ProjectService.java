package org.faya.sensei.services;

import jakarta.inject.Inject;
import org.faya.sensei.entities.ProjectEntity;
import org.faya.sensei.entities.UserEntity;
import org.faya.sensei.payloads.ProjectDTO;
import org.faya.sensei.repositories.IRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ProjectService implements IService<ProjectDTO> {

    @Inject
    private IRepository<ProjectEntity> projectRepository;

    @Inject
    private IRepository<UserEntity> userRepository;

    public Optional<ProjectDTO> get(final int id) {
        final Optional<ProjectEntity> boardEntity = projectRepository.get(id);

        return boardEntity.map(ProjectDTO::fromEntity);
    }

    public Collection<ProjectDTO> getBy(final String key, final String value) {
        final Collection<ProjectEntity> projectEntity = projectRepository.getBy(key, value);

        return projectEntity.stream().map(ProjectDTO::fromEntity).toList();
    }

    public Optional<ProjectDTO> create(final ProjectDTO dto) {
        ProjectEntity projectEntity = ProjectDTO.toEntity(dto);
        projectEntity.setUsers(dto.getOwnerIds().stream()
                .map(userRepository::get)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList());
        final int boardId = projectRepository.post(projectEntity);

        return boardId > 0
                ? Optional.of(ProjectDTO.fromEntity(projectEntity))
                : Optional.empty();
    }

    public Optional<ProjectDTO> update(final int id, final ProjectDTO dto) {
        final ProjectEntity projectEntity = ProjectDTO.toEntity(dto);

        final Optional<ProjectEntity> updatedProjectEntity = projectRepository.put(id, projectEntity);

        return updatedProjectEntity.map(ProjectDTO::fromEntity);
    }

    public boolean remove(final int id) {
        final Optional<ProjectEntity> boardEntity = projectRepository.delete(id);

        return boardEntity.isPresent();
    }
}
