package org.appslapp.AppsLappServer.business.security;

import org.appslapp.AppsLappServer.business.security.Admin.AdminDetailsServiceImp;
import org.appslapp.AppsLappServer.business.security.Labmaster.LabmasterDetailsServiceImp;
import org.appslapp.AppsLappServer.business.security.User.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

@EnableWebSecurity
public class AuthenticationManager extends WebSecurityConfigurerAdapter {

    private final UserDetailsService service;
    private final UserDetailsService labmasterService;
    private final UserDetailsService adminService;

    public AuthenticationManager(@Autowired UserDetailsServiceImp service, @Autowired LabmasterDetailsServiceImp lab,
                                 @Autowired AdminDetailsServiceImp admin) {
        this.service = service;
        this.labmasterService = lab;
        this.adminService = admin;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(
                List.of("Authorization", "Cache-Control", "Content-Type", "X-PT-SESSION-ID", "NGSW-BYPASS"));
        config.setAllowedOrigins(List.of("https://appslappapp.vercel.app", "http://localhost:4200/"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));

        http.authorizeRequests()
                .mvcMatchers("/api/auth/register").permitAll()
                .mvcMatchers("/api/auth/verify*").permitAll()
                .mvcMatchers("/api/auth/login").authenticated()
                .mvcMatchers("/api/user/get").authenticated()
                .mvcMatchers("/api/management/getStudents").hasAnyAuthority("ADMIN", "LABMASTER")
                .mvcMatchers("/api/management/createLab").hasAnyAuthority("ADMIN", "LABMASTER")
                .mvcMatchers("/api/management/getLabs").hasAnyAuthority("LABMASTER")
                .mvcMatchers("/api/management/createExercise").hasAnyAuthority("ADMIN", "LABMASTER")
                .mvcMatchers("/api/auth/createAdmins").permitAll()
                .anyRequest().denyAll()
                .and()
                .csrf().disable().cors().configurationSource(request -> config)
                .and()
                .httpBasic()
                .and()
                .requiresChannel()
                .requestMatchers(r -> r.getHeader("X-Forwarded-Proto") != null)
                .requiresSecure();
    }

    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder manager) throws Exception {
        manager.userDetailsService(service).passwordEncoder(getEncoder());
        manager.userDetailsService(labmasterService).passwordEncoder(getEncoder());
        manager.userDetailsService(adminService).passwordEncoder(getEncoder());


        manager.inMemoryAuthentication()
                .withUser("admin").password(getEncoder().encode(
                        System.getenv().get("ADMIN_PASSWORD"))).authorities("ADMIN")
                .and()
                .passwordEncoder(getEncoder());
    }
}
