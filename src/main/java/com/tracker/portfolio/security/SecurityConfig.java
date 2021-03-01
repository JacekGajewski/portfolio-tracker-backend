package com.tracker.portfolio.security;

import com.tracker.portfolio.auth.ApplicationUserService;
import com.tracker.portfolio.enums.UserRole;
import com.tracker.portfolio.jwt.JwtConfig;
import com.tracker.portfolio.jwt.JwtTokenVerifier;
import com.tracker.portfolio.jwt.JwtUsernamePasswordAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.crypto.SecretKey;

import java.util.Arrays;

import static com.tracker.portfolio.enums.UserPermission.STOCK_READ;
import static com.tracker.portfolio.enums.UserRole.ADMIN;
import static com.tracker.portfolio.enums.UserRole.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserService applicationUserService;
    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors().and()
                .csrf().disable()//TODO: Enable it later
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(new JwtUsernamePasswordAuthFilter(authenticationManager(), jwtConfig, secretKey))
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtUsernamePasswordAuthFilter.class)
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*", "/js/*",  "/login")
                    .permitAll()
                .antMatchers("/portfolio/**")
                    .hasAnyRole(ADMIN.name(), USER.name())
                .antMatchers(HttpMethod.POST, "/users")
                    .permitAll()
                .antMatchers("/users")
                    .hasRole(ADMIN.name())
                .antMatchers("/position/**")
                    .hasAnyRole(ADMIN.name(), USER.name())
                .antMatchers("/stocks/price/**")
                    .hasAnyRole(ADMIN.name(), USER.name())
                .antMatchers("/stocks/**")
                    .hasRole(ADMIN.name())
                .anyRequest()
                    .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(applicationUserService);
        return provider;
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
//        OPTIONS must be last
        configuration.setAllowedMethods(Arrays.asList("GET", "PUT", "POST", "DELETE", "PATCH", "OPTIONS"));
        configuration.setExposedHeaders(Arrays.asList("Authorization", "content-type", "UserId", "Access-Control-Allow-Origin", "ExpiresIn"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "content-type", "UserId", "Access-Control-Allow-Origin", "ExpiresIn"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
