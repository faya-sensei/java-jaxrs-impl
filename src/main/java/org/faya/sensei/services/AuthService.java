package org.faya.sensei.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.inject.Inject;
import org.faya.sensei.entities.UserEntity;
import org.faya.sensei.entities.UserRole;
import org.faya.sensei.payloads.UserDTO;
import org.faya.sensei.payloads.UserPrincipal;
import org.faya.sensei.repositories.IRepository;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AuthService implements IAuthService {

    private static final Algorithm algorithm = Algorithm.HMAC256(System.getProperty("app.secretKey", "java-jaxrs"));
    private static final String issuer = "org.faya.sensei.java-jaxrs";

    @Inject
    private IRepository<UserEntity> userRepository;

    @Override
    public Optional<UserDTO> login(final UserDTO dto) {
        final Optional<UserEntity> userEntity = userRepository.get(dto.getName());
        if (userEntity.isEmpty()) return Optional.empty();

        final Optional<String> hashedPassword = hashPassword(dto.getPassword());

        return hashedPassword.map(password -> {
            if (password.equals(userEntity.get().getPassword())) {
                final Optional<String> token = generateToken(userEntity.get().getId(),
                        Map.of("name", userEntity.get().getName()));
                if (token.isPresent()) {
                    final UserDTO user = UserDTO.fromEntity(userEntity.get());
                    user.setToken(token.get());
                    return user;
                }
            }

            return null;
        });
    }

    @Override
    public Optional<String> generateToken(int id, Map<String, String> payload) {
        try {
            return Optional.of(JWT.create()
                    .withIssuer(issuer)
                    .withSubject(String.valueOf(id))
                    .withPayload(payload)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + TimeUnit.DAYS.toMillis(5)))
                    .withJWTId(UUID.randomUUID().toString())
                    .sign(algorithm));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserPrincipal> resolveToken(String token) {
        try {
            final DecodedJWT decodedJWT = JWT.require(algorithm).withIssuer(issuer).build().verify(token);

            UserPrincipal user = new UserPrincipal();
            user.setId(Integer.parseInt(decodedJWT.getSubject()));
            user.setName(decodedJWT.getClaim("name").asString());
            user.setRole(decodedJWT.getClaim("role").asString());

            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserDTO> get(String name) {
        final Optional<UserEntity> userEntity = userRepository.get(name);

        if (userEntity.isEmpty()) return Optional.empty();

        return userEntity.map(entity -> {
            final Optional<String> token = generateToken(entity.getId(), Map.of(
                    "name", entity.getName(),
                    "role", entity.getRole().name()
            ));

            if (token.isPresent()) {
                UserDTO user = UserDTO.fromEntity(entity);
                user.setToken(token.get());
                return user;
            }

            return null;
        });
    }

    @Override
    public Optional<UserDTO> create(UserDTO dto) {
        UserEntity userEntity = UserDTO.toEntity(dto);

        final Optional<String> hashedPassword = hashPassword(dto.getPassword());

        return hashedPassword.map(password -> {
            userEntity.setRole(UserRole.USER);
            userEntity.setPassword(password);

            if (userRepository.post(userEntity) > 0) {
                final Optional<String> token = generateToken(userEntity.getId(), Map.of(
                        "name", userEntity.getName(),
                        "role", userEntity.getRole().name()
                ));

                if (token.isPresent()) {
                    final UserDTO user = UserDTO.fromEntity(userEntity);
                    user.setToken(token.get());
                    return user;
                }
            }

            return null;
        });
    }

    @Override
    public Optional<UserDTO> update(final int id, final UserDTO dto) {
        final UserEntity userEntity = UserDTO.toEntity(dto);

        final Optional<UserEntity> updatedUserEntity = userRepository.put(id, userEntity);

        return updatedUserEntity.map(UserDTO::fromEntity);
    }

    @Override
    public boolean remove(int id) {
        return false;
    }

    private Optional<String> hashPassword(final String password) {
        try {
            final byte[] hashedBytes = MessageDigest.getInstance("SHA-256")
                    .digest(password.getBytes(StandardCharsets.UTF_8));

            return Optional.of(IntStream.range(0, hashedBytes.length)
                    .mapToObj(i -> String.format("%02x", hashedBytes[i]))
                    .collect(Collectors.joining()));
        } catch (NoSuchAlgorithmException e) {
            return Optional.empty();
        }
    }
}
