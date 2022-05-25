package org.appslapp.AppsLappServer.business.security;

import org.appslapp.AppsLappServer.business.pojo.users.admin.AdminService;
import org.appslapp.AppsLappServer.business.pojo.users.labmaster.LabmasterService;
import org.appslapp.AppsLappServer.business.pojo.users.user.UserService;
import org.appslapp.AppsLappServer.business.security.users.entity.EntityDetailsServiceImp;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@EnableWebSecurity
public class AuthenticationManager extends WebSecurityConfigurerAdapter {
    private final UserService userService;
    private final LabmasterService labmasterService;
    private final AdminService adminService;

    @Autowired
    public AuthenticationManager(UserService userService, LabmasterService lab,
                                 AdminService admin) {
        this.userService = userService;
        this.labmasterService = lab;
        this.adminService = admin;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedHeaders(
                List.of("Authorization", "Cache-Control", "Content-Type", "X-PT-SESSION-ID", "NGSW-BYPASS"));
        config.setAllowedOrigins(List.of("http://localhost:4200", "https://appslappapp.vercel.app"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PUT", "OPTIONS", "PATCH", "DELETE"));
        config.setAllowCredentials(true);
        config.setExposedHeaders(List.of("Authorization"));

        http.cors().configurationSource(request -> config)
                .and()
                .authorizeRequests()
                .mvcMatchers("/api/auth/register").permitAll()
                .mvcMatchers("/api/auth/verify/*").permitAll()
                .mvcMatchers("/api/auth/login").authenticated()
                .mvcMatchers("/api/user/get").authenticated()
                .mvcMatchers("/api/management/getStudents").hasAnyAuthority("ADMIN", "LABMASTER")
                .mvcMatchers("/api/management/createLab").hasAnyAuthority("LABMASTER")
                .mvcMatchers("/api/management/getLabs").hasAnyAuthority("LABMASTER")
                .mvcMatchers("/api/management/createExercise").hasAnyAuthority("ADMIN", "LABMASTER")
                .mvcMatchers("/api/management/createGroupOfExercises").hasAnyAuthority("LABMASTER")
                .mvcMatchers("/api/management/getStudent/*").hasAnyAuthority("LABMASTER")
                .mvcMatchers("/api/management/getLabs").hasAnyAuthority("LABMASTER")
                .mvcMatchers("/api/management/getLab/*").hasAnyAuthority("LABMASTER")
                .mvcMatchers("/api/management/getAllGroups").hasAnyAuthority("LABMASTER")
                .mvcMatchers("/api/management/getAllExercises").hasAnyAuthority("ADMIN", "LABMASTER")
                .mvcMatchers("/api/management/createExercise").hasAnyAuthority("LABMASTER")
                .mvcMatchers("/api/management/addGroupToLab").hasAnyAuthority("LABMASTER")
                .mvcMatchers("/api/management/updateScore").hasAnyAuthority("LABMASTER")
                .mvcMatchers("/api/auth/promoteToLabmaster").hasAnyAuthority("ADMIN")
                .mvcMatchers("/api/auth/createAdmins").permitAll()
                .anyRequest().denyAll()
                .and()
                .csrf().disable()
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
        manager.userDetailsService(new EntityDetailsServiceImp<>(
                List.of(labmasterService, adminService, userService))).passwordEncoder(getEncoder());
    }
}
