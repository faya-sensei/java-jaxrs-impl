package org.faya.sensei.middlewares;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import org.faya.sensei.entities.UserRole;
import org.faya.sensei.payloads.UserPrincipal;
import org.faya.sensei.services.IAuthService;

import java.security.Principal;
import java.util.Optional;

@Provider
@JWTAuth
@Priority(Priorities.AUTHENTICATION)
public class JWTAuthFilter implements ContainerRequestFilter {

    @Inject
    private IAuthService authService;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        final String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer"))
            throw new NotAuthorizedException("Authorization header must be provided.");

        final String token = authorizationHeader.substring("Bearer".length()).trim();

        final Optional<UserPrincipal> user = authService.resolveToken(token);
        if (user.isPresent()) {
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return user.get();
                }

                @Override
                public boolean isUserInRole(String role) {
                    UserRole userRole = UserRole.valueOf(user.get().getRole().toUpperCase());

                    return UserRole.valueOf(role.toUpperCase()).getLevel() <= userRole.getLevel();
                }

                @Override
                public boolean isSecure() {
                    return requestContext.getUriInfo()
                            .getAbsolutePath()
                            .getScheme()
                            .equalsIgnoreCase("https");
                }

                @Override
                public String getAuthenticationScheme() {
                    return "JWT_BEARER";
                }
            });
        } else {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }
}
