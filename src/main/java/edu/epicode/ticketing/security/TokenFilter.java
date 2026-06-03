package edu.epicode.ticketing.security;

import edu.epicode.ticketing.entities.User;
import edu.epicode.ticketing.exceptions.NotFoundException;
import edu.epicode.ticketing.exceptions.UnauthorizedException;
import edu.epicode.ticketing.services.UsersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenTools tokenTools;
    private final UsersService usersService;

    public TokenFilter(TokenTools tokenTools, UsersService usersService){
        this.tokenTools = tokenTools;
        this.usersService = usersService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            // 1. If the Authorization header is missing, the request continues without authentication.
            // 2. If the header is present, it must follow the format: "Bearer <token>"
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // 3. The token string is extracted by removing the "Bearer " prefix.
            String accessToken = authorizationHeader.replace("Bearer ", "");

            // 4. Verify the token (not expired, not manipulated, not malformed)
            tokenTools.verifyToken(accessToken);

            //**************** AUTHORIZATION *******************

            // 1. Extract userId from the token
            UUID userId = tokenTools.extractIdFromToken(accessToken);
            // 2. Find by Id
            User authenticatedUser = this.usersService.findById(userId);
            // 3. Associate user to Security Context
            Authentication authentication = new UsernamePasswordAuthenticationToken(authenticatedUser, null, authenticatedUser.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 4. If everything is fine, next

            filterChain.doFilter(request, response); // Next
        } catch (UnauthorizedException e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        } catch (NotFoundException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
        catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Problems with tokens");
        }
    }

    // shouldNotFilter returns true for /auth/** paths, which excludes them from JWT verification.
    // Login and register endpoints do not require an existing token.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
