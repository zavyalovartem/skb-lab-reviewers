package ru.codereviewers.codereviewersforskblab;

import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.Project;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AppointmentReviewersService {
    private final GitLabApi gitLabApi;
    private static final Logger log = LoggerFactory.getLogger(AppointmentReviewersService.class);
    private Date time = new Date();

    public AppointmentReviewersService(GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
    }

    public void handleMR() throws GitLabApiException {
        var projects = gitLabApi.getProjectApi().getProjects();
        for (Project project:
             projects) {
            var id = project.getId();
            try {
                var mr = gitLabApi.getMergeRequestApi().getMergeRequests(id, Constants.MergeRequestState.OPENED).stream().filter(t -> t.getCreatedAt().after(time));
                time = new Date();
            } catch (GitLabApiException e) {
                log.error(e.getMessage());
            }
        }

    }

    private Boolean isOverburdened(SecurityProperties.User user){

        return false;
    }
}
