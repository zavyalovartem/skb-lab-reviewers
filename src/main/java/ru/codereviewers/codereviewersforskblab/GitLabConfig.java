package ru.codereviewers.codereviewersforskblab;


import org.gitlab4j.api.GitLabApi;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class GitLabConfig {

    private String token;
    private String host;

    @Bean
    public GitLabApi gitLabApi() {
        return new GitLabApi(host, token);
    }
}
