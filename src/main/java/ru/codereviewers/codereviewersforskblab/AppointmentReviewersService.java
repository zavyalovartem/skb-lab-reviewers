package ru.codereviewers.codereviewersforskblab;

import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.Reviewer;
import org.gitlab4j.api.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AppointmentReviewersService {
    private final GitLabApi gitLabApi;
    private static final Logger log = LoggerFactory.getLogger(AppointmentReviewersService.class);
    private final ReviewersRepository repo;
    private final List<Project> projects;
    private Date time = new Date();

    public AppointmentReviewersService(GitLabApi gitLabApi, ReviewersRepository repo) throws GitLabApiException {
        this.gitLabApi = gitLabApi;
        this.projects =  gitLabApi.getProjectApi().getProjects();
        this.repo = repo;
    }

    public void handleMR() throws GitLabApiException {

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

    private int countLoad(Integer projectId, String username) throws GitLabApiException {
        int load = 0;
        var mrs = gitLabApi.getMergeRequestApi().getMergeRequests(projectId, Constants.MergeRequestState.OPENED);
        for (MergeRequest mr:
             mrs) {
            var reviewers = mr.getReviewers();
            for (Reviewer reviewer:
                 reviewers) {
                if(reviewer.getUsername()==username){
                    load++;
                }
            }
        }
        return load;
    }

    private Boolean isAvailable(Integer projectId, String username) throws GitLabApiException {
        if(countLoad(projectId, username)>5){
            return false;
        }
        return repo.findById(username).get().getStatus()==Status.AVAILABLE;
    }
}
