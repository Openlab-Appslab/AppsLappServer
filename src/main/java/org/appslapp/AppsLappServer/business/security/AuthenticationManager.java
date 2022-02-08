package org.appslapp.AppsLappServer.business.security;

import org.appslapp.AppsLappServer.business.security.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import javax.persistence.Access;
import java.util.Collections;
import java.util.List;

@EnableWebSecurity
public class AuthenticationManager extends WebSecurityConfigurerAdapter {

    private final UserDetailsService service;

    public AuthenticationManager(@Autowired UserDetailsService service) {
        this.service = service;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(
                List.of("Authorization", "Cache-Control", "Content-Type", "X-PT-SESSION-ID", "NGSW-BYPASS"));
        config.setAllowedOrigins(List.of("*"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT","OPTIONS","PATCH", "DELETE"));
        config.setAllowCredentials(false);
        config.setExposedHeaders(List.of("Authorization"));

        http.authorizeRequests()
                .mvcMatchers("/api/auth/register").permitAll()
                .mvcMatchers("/api/test").permitAll()
                .mvcMatchers("/api/verify*").permitAll()
                .mvcMatchers("/api/auth/login").authenticated()
                .mvcMatchers("/api/secureTest").authenticated()
                .anyRequest().denyAll()
                .and()
                .csrf().disable().cors().configurationSource(request -> config)
                .and()
                .httpBasic();
        //deploy
        /*
        requiresChannel()
        .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
        .requiresSecure()
         */
    }

    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder manager) throws Exception {
        manager.userDetailsService(service).passwordEncoder(getEncoder());
        manager.inMemoryAuthentication()
                .withUser("admin").password(getEncoder().encode(System.getenv().get("ADMIN_PASSWORD"))).authorities("ADMIN")
                .and()
                .passwordEncoder(getEncoder());
    }
}
