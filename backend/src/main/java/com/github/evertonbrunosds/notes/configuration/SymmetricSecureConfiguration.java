package com.github.evertonbrunosds.notes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.github.evertonbrunosds.notes.model.SymmetricSecureModel;

@Configuration
@PropertySource(value = "classpath:symmetric-secure.properties", encoding = "UTF-8")
public class SymmetricSecureConfiguration {

    @Bean
    public SymmetricSecureModel symmetricSecureModel(final Environment environment) {
        return SymmetricSecureModel.builder()
                .algorithm(environment.getProperty("algorithm", ""))
                .transformation(environment.getProperty("transformation", ""))
                .key(environment.getProperty("key", "").getBytes())
                .build();
    }

}
