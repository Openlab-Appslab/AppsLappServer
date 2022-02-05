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

import javax.persistence.Access;

@EnableWebSecurity
public class AuthenticationManager extends WebSecurityConfigurerAdapter {

    private final UserDetailsService service;

    public AuthenticationManager(@Autowired UserDetailsService service) {
        this.service = service;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/api/auth/register").permitAll()
                .mvcMatchers("/api/test").permitAll()
                .mvcMatchers("/api/verify*").permitAll()
                .mvcMatchers("/api/auth/login").hasAnyAuthority("ADMIN", "LAB_MASTER", "PUPIL")
                .mvcMatchers("/api/secureTest").authenticated()
                .anyRequest().denyAll()
                .and()
                .csrf().disable()
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
                .withUser("admin").password(getEncoder().encode("test123456:)")).authorities("ADMIN")
                .and()
                .passwordEncoder(getEncoder());
    }
}
