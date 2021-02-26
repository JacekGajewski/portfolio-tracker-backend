package com.tracker.portfolio.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tracker.portfolio.auth.ApplicationUser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtUsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtConfig jwtConfig;
    private final SecretKey secretKey;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {

        try {
            UsernameAndPasswordAuthRequest authRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernameAndPasswordAuthRequest.class);

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    authRequest.getUsername(),
                    authRequest.getPassword()
            );
            return authenticationManager.authenticate(authentication);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {

        Long userId = ((ApplicationUser) authResult.getPrincipal()).getUser_id();
        String token = Jwts.builder()
                .setSubject(authResult.getName())
                .claim("authorities", authResult.getAuthorities())
                .claim("userId", userId)
                .signWith(secretKey)
                .compact();
// TODO Store tokes in database for a users

        response.addHeader("Authorization", "Bearer " + token);
        response.addHeader("UserId", userId.toString());
        response.addHeader("ExpiresIn", jwtConfig.getTokenExpirationAfterDays());
        System.out.println(jwtConfig.getTokenExpirationAfterDays());
        System.out.println(jwtConfig.getTokenPrefix());
        System.out.println(jwtConfig.getSecretKey());
        System.out.println(jwtConfig.getAuthorizationHeader());
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "GET, PUT, POST, DELETE, PATCH, OPTIONS");
        response.setHeader("Access-Control-Max-Age", "3600");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Accept, X-Requested-With, remember-me");
//        response.addHeader(jwtConfig.getAuthorizationHeader(), TODO: Uncomment
//                jwtConfig.getTokenPrefix() + token);
    }
}
