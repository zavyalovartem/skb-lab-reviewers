package ru.codereviewers.codereviewersforskblab;

import org.gitlab4j.api.GitLabApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AppointmentReviewersService {
    private final GitLabApi gitLabApi;
    private static final Logger log = LoggerFactory.getLogger(AppointmentReviewersService.class);

    public AppointmentReviewersService(GitLabApi gitLabApi) {
        this.gitLabApi = gitLabApi;
    }
}
