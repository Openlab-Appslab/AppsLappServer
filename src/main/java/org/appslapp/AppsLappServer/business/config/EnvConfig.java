package org.appslapp.AppsLappServer.business.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import java.util.HashMap;


public class EnvConfig implements EnvironmentPostProcessor {
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        var params = new HashMap<String, Object>();
        var env = System.getenv();
        params.put("DATABASE_URL", env.get("DATABASE_URL"));
        params.put("DATABASE_USERNAME", env.get("DATABASE_USERNAME"));
        params.put("DATABASE_PASSWORD", env.get("DATABASE_PASSWORD"));
        params.put("EMAIL_USERNAME", env.get("EMAIL_USERNAME"));
        params.put("EMAIL_PASSWORD", env.get("EMAIL_PASSWORD"));
        params.put("ADMIN_PASSWORD", env.get("ADMIN_PASSWORD"));
        environment.getPropertySources().addFirst(new SystemEnvironmentPropertySource("Env", params));
    }
}
