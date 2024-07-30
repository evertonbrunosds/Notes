package com.github.evertonbrunosds.notes.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import com.github.evertonbrunosds.notes.model.SymmetricSecureProcessor;

import lombok.Getter;

@Configuration
@PropertySource(value = "classpath:symmetric-secure-processor.properties", encoding = "UTF-8")
public class AESProcessorConfiguration {

    public class AESProcessor extends SymmetricSecureProcessor {

        private static final String ALGORITHM = "AES";

        private static final String TRANSFORMATION = "aes.transformation";

        private static final String KEY = "aes.key";

        public AESProcessor(final String transformation, final byte[] key) {
            super(ALGORITHM, transformation, key);
        }

    }

    private static final String DEFAULT = "";

    @Getter(onMethod_ = @Bean)
    private final AESProcessor aes;

    public AESProcessorConfiguration(final Environment environment) {
        this.aes = new AESProcessor(
                environment.getProperty(AESProcessor.TRANSFORMATION, DEFAULT),
                environment.getProperty(AESProcessor.KEY, DEFAULT).getBytes());
    }

}
