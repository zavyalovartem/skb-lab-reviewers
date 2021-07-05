package ru.codereviewers.codereviewersforskblab;


import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class GitLabConfig {

    @Value("${app.token}")
    private String token;
    @Value("${app.host}")
    private String host;
    @Value("${app.project_id}")
    private Integer projectId;

    @Bean
    public GitLabApi gitLabApi(){
        //        gitLabApi.sudo("administrator");
        return new GitLabApi(host, token);
    }

    @Bean
    public Project project() throws GitLabApiException {
        return gitLabApi().getProjectApi().getProject(projectId);
    }
}
