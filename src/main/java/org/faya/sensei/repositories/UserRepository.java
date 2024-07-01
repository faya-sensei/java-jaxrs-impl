package org.faya.sensei.repositories;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import org.faya.sensei.entities.UserEntity;

import java.lang.invoke.MethodHandles;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserRepository implements IRepository<UserEntity> {

    private static final Logger LOGGER = Logger.getLogger(MethodHandles.lookup().lookupClass().getName());

    @Inject
    private EntityManager entityManager;

    @Override
    public Collection<UserEntity> get() {
        try {
            return entityManager
                    .createQuery("SELECT t FROM UserEntity t", UserEntity.class)
                    .getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed fetching user entities: {0}", e.getMessage());
        }

        return Collections.emptyList();
    }

    @Override
    public Optional<UserEntity> get(int id) {
        try {
            final UserEntity userEntity = entityManager.find(UserEntity.class, id);

            return Optional.ofNullable(userEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed fetching user entity with id {0}: {1}", new Object[]{id, e.getMessage()});
        }

        return Optional.empty();
    }

    @Override
    public Optional<UserEntity> get(String key) {
        try {
            final UserEntity userEntity = entityManager
                    .createQuery("SELECT t FROM UserEntity t WHERE t.name = :name", UserEntity.class)
                    .setParameter("name", key)
                    .getSingleResult();

            return Optional.of(userEntity);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed fetching user entity with key {0}: {1}", new Object[]{key, e.getMessage()});
        }

        return Optional.empty();
    }

    @Override
    public int post(UserEntity userEntity) {
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            transaction.begin();
            entityManager.persist(userEntity);
            transaction.commit();

            return userEntity.getId();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed saving user entity: {0}", e.getMessage());
            if (transaction.isActive()) transaction.rollback();
        }

        return -1;
    }

    @Override
    public Optional<UserEntity> put(int id, UserEntity userEntity) {
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            final UserEntity existingUserEntity = entityManager.find(UserEntity.class, id);

            if (existingUserEntity != null) {
                transaction.begin();

                if (userEntity.getName() != null)
                    existingUserEntity.setName(userEntity.getName());
                if (userEntity.getPassword() != null)
                    existingUserEntity.setPassword(userEntity.getPassword());
                if (userEntity.getRole() != null)
                    existingUserEntity.setRole(userEntity.getRole());

                transaction.commit();

                return Optional.of(existingUserEntity);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed updating user entity with id {0}: {1}", new Object[]{id, e.getMessage()});
            if (transaction.isActive()) transaction.rollback();
        }

        return Optional.empty();
    }

    @Override
    public Optional<UserEntity> delete(int id) {
        final EntityTransaction transaction = entityManager.getTransaction();

        try {
            final UserEntity userEntity = entityManager.find(UserEntity.class, id);

            if (userEntity != null) {
                transaction.begin();
                entityManager.remove(userEntity);
                transaction.commit();

                return Optional.of(userEntity);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed removing user entity with id {0}: {1}", new Object[]{id, e.getMessage()});
            if (transaction.isActive()) transaction.rollback();
        }

        return Optional.empty();
    }
}
