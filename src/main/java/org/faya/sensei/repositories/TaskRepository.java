package org.faya.sensei.repositories;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.faya.sensei.entities.TaskEntity;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskRepository implements IRepository<TaskEntity> {

    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    private EntityManager entityManager;

    @Override
    public Collection<TaskEntity> get() {
        try {
            return entityManager
                    .createQuery("SELECT t FROM TaskEntity t", TaskEntity.class)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed fetching task entities: {0}", e.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public Optional<TaskEntity> get(int id) {
        try {
            final TaskEntity taskEntity = entityManager.find(TaskEntity.class, id);

            return Optional.ofNullable(taskEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed fetching task entity with id {0}: {1}", new Object[]{id, e.getMessage()});
        }

        return Optional.empty();
    }

    @Override
    public int post(TaskEntity taskEntity) {
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(taskEntity);
            transaction.commit();

            return taskEntity.getId();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed saving task entity: {0}", e.getMessage());
            if (transaction.isActive()) transaction.rollback();
        }

        return -1;
    }

    @Override
    public Optional<TaskEntity> put(int id, TaskEntity taskEntity) {
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            final TaskEntity existingTaskEntity = entityManager.find(TaskEntity.class, id);

            if (existingTaskEntity != null) {
                transaction.begin();

                if (taskEntity.getTitle() != null)
                    existingTaskEntity.setTitle(taskEntity.getTitle());
                if (taskEntity.getDescription() != null)
                    existingTaskEntity.setDescription(taskEntity.getDescription());
                if (taskEntity.getEndDate() != null)
                    existingTaskEntity.setEndDate(taskEntity.getEndDate());
                if (taskEntity.getStatus() != null)
                    existingTaskEntity.setStatus(taskEntity.getStatus());
                if (taskEntity.getAssigner() != null)
                    existingTaskEntity.setAssigner(taskEntity.getAssigner());

                transaction.commit();

                return Optional.of(existingTaskEntity);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed updating task entity with id {0}: {1}", new Object[]{id, e.getMessage()});
            if (transaction.isActive()) transaction.rollback();
        }

        return Optional.empty();
    }

    @Override
    public Optional<TaskEntity> delete(int id) {
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            final TaskEntity taskEntity = entityManager.find(TaskEntity.class, id);

            if (taskEntity != null) {
                transaction.begin();
                entityManager.remove(taskEntity);
                transaction.commit();

                return Optional.of(taskEntity);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed removing task entity with id {0}: {1}", new Object[]{id, e.getMessage()});
            if (transaction.isActive()) transaction.rollback();
        }

        return Optional.empty();
    }
}
