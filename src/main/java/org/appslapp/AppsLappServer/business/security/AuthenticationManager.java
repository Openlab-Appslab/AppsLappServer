package org.appslapp.AppsLappServer.business.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class AuthenticationManager extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .mvcMatchers("/api/auth/register").permitAll()
                .mvcMatchers("/api/test").permitAll()
                .mvcMatchers("/api/auth/login").hasAnyAuthority("ADMIN", "LAB_MASTER", "PUPIL")
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

    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder(15);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder manager) throws Exception {
        manager.userDetailsService(userDetailsService()).passwordEncoder(getEncoder());
        manager.inMemoryAuthentication()
                .withUser("admin").password("test123456:)").authorities("ADMIN")
                .and()
                .passwordEncoder(getEncoder());
    }
}
