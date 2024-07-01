package org.faya.sensei.repositories;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.faya.sensei.entities.StatusEntity;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StatusRepository implements IRepository<StatusEntity> {

    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    private EntityManager entityManager;

    @Override
    public Collection<StatusEntity> get() {
        try {
            return entityManager
                    .createQuery("SELECT t FROM StatusEntity t", StatusEntity.class)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed fetching status entities: {0}", e.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public Optional<StatusEntity> get(String key) {
        try {
            final StatusEntity statusEntity = entityManager
                    .createQuery("SELECT t FROM StatusEntity t WHERE t.name = :name", StatusEntity.class)
                    .setParameter("name", key)
                    .getSingleResult();

            return Optional.of(statusEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed fetching status entity with key {0}: {1}", new Object[]{key, e.getMessage()});
        }

        return Optional.empty();
    }

    @Override
    public int post(StatusEntity statusEntity) {
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(statusEntity);
            transaction.commit();

            return statusEntity.getId();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed saving status entity: {0}", e.getMessage());
            if (transaction.isActive()) transaction.rollback();
        }

        return -1;
    }

    @Override
    public Optional<StatusEntity> put(int id, StatusEntity statusEntity) {
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            final StatusEntity existingStatusEntity = entityManager.find(StatusEntity.class, id);

            if (existingStatusEntity != null) {
                transaction.begin();

                if (statusEntity.getName() != null)
                    existingStatusEntity.setName(statusEntity.getName());

                transaction.commit();

                return Optional.of(existingStatusEntity);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed updating status entity with id {0}: {1}", new Object[]{id, e.getMessage()});
            if (transaction.isActive()) transaction.rollback();
        }

        return Optional.empty();
    }

    @Override
    public Optional<StatusEntity> delete(int id) {
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            final StatusEntity statusEntity = entityManager.find(StatusEntity.class, id);

            if (statusEntity != null) {
                transaction.begin();
                entityManager.remove(statusEntity);
                transaction.commit();

                return Optional.of(statusEntity);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed removing status entity with id {0}: {1}", new Object[]{id, e.getMessage()});
            if (transaction.isActive()) transaction.rollback();
        }

        return Optional.empty();
    }
}
