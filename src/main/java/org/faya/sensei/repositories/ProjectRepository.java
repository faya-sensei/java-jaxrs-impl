package org.faya.sensei.repositories;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.faya.sensei.entities.ProjectEntity;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProjectRepository implements IRepository<ProjectEntity> {

    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    private EntityManager entityManager;

    @Override
    public Optional<ProjectEntity> get(int id) {
        try {
            final ProjectEntity projectEntity = entityManager.find(ProjectEntity.class, id);

            return Optional.ofNullable(projectEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed fetching project entity with id {0}: {1}", new Object[]{id, e.getMessage()});
        }

        return Optional.empty();
    }

    @Override
    public Collection<ProjectEntity> getBy(String key, String value) {
        try {
            final StringBuilder queryBuilder = new StringBuilder("SELECT p FROM ProjectEntity p ");
            if (key.contains(".")) {
                final String[] parts = key.split("\\.");
                queryBuilder.append("JOIN p.").append(parts[0]).append(" k WHERE k.").append(parts[1]).append(" = :value");
            } else {
                queryBuilder.append("WHERE p.").append(key).append(" = :value");
            }

            return entityManager
                    .createQuery(queryBuilder.toString(), ProjectEntity.class)
                    .setParameter("value", value)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed fetching project entities by {0}: {1}", new Object[]{key, e.getMessage()});
        }

        return Collections.emptyList();
    }

    @Override
    public int post(ProjectEntity projectEntity) {
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(projectEntity);
            transaction.commit();

            return projectEntity.getId();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed saving project entity: {0}", e.getMessage());
            if (transaction.isActive()) transaction.rollback();
        }

        return -1;
    }

    @Override
    public Optional<ProjectEntity> put(int id, ProjectEntity projectEntity) {
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            final ProjectEntity existingProjectEntity = entityManager.find(ProjectEntity.class, id);

            if (existingProjectEntity != null) {
                transaction.begin();

                if (projectEntity.getName() != null)
                    existingProjectEntity.setName(projectEntity.getName());

                transaction.commit();

                return Optional.of(existingProjectEntity);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed updating project entity with id {0}: {1}", new Object[]{id, e.getMessage()});
            if (transaction.isActive()) transaction.rollback();
        }

        return Optional.empty();
    }

    @Override
    public Optional<ProjectEntity> delete(int id) {
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            final ProjectEntity projectEntity = entityManager.find(ProjectEntity.class, id);

            if (projectEntity != null) {
                transaction.begin();
                entityManager.remove(projectEntity);
                transaction.commit();

                return Optional.of(projectEntity);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed removing project entity with id {0}: {1}", new Object[]{id, e.getMessage()});
            if (transaction.isActive()) transaction.rollback();
        }

        return Optional.empty();
    }
}
