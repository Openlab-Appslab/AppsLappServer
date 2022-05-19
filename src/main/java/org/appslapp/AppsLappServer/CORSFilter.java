package org.appslapp.AppsLappServer;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CORSFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
                                 final FilterChain filterChain) throws ServletException, IOException {

        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                "Origin, Content-Type, Accept, Authorization, Accept-Language, connection, Cache-Control, Access-Control-Request-Method, Access-Control-Request-Headers");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET, POST, PUT, PATCH, DELETE, OPTIONS, HEAD");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                "Access-Control-Allow-Origin, Access-Control-Allow-Credentials");
        response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, request.getHeader(HttpHeaders.ORIGIN));
        filterChain.doFilter(request, response);

    }
}