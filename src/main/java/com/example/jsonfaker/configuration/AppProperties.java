package com.example.jsonfaker.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotEmpty;

@Configuration
@ConfigurationProperties(prefix = "facker", ignoreUnknownFields = false)
public class AppProperties {

    @NotEmpty
    private String uri;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
