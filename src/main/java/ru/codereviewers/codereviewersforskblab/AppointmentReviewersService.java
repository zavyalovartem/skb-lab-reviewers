package ru.codereviewers.codereviewersforskblab;

import org.gitlab4j.api.Constants;
import org.gitlab4j.api.GitLabApi;
import org.gitlab4j.api.GitLabApiException;
import org.gitlab4j.api.models.MergeRequest;
import org.gitlab4j.api.models.Project;
import org.gitlab4j.api.models.Reviewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import java.util.List;

import java.util.stream.Collectors;

@Service
public class AppointmentReviewersService {
    private final GitLabApi gitLabApi;
    private static final Logger log = LoggerFactory.getLogger(AppointmentReviewersService.class);
    private final ReviewersRepository repo;
    private final Project project;

    public AppointmentReviewersService(GitLabApi gitLabApi, ReviewersRepository repo, Project project) {
        this.gitLabApi = gitLabApi;
        this.repo = repo;
        this.project = project;
    }

    public void handleMR(Integer mrIid) throws GitLabApiException {
        var id = project.getId();
        var mr = gitLabApi.getMergeRequestApi().getMergeRequest(id, mrIid);
        var task = mr.getTitle().substring(1, 10); //нужно нормальный парсер сделать
        var closedMrs = gitLabApi.getMergeRequestApi().getMergeRequests(id, Constants.MergeRequestState.CLOSED).stream().filter(t -> t.getTitle().substring(1, 10).equals(task)).collect(Collectors.toList());
        if (closedMrs.size() > 0) {
            for (MergeRequest closedMr :
                    closedMrs) {

                var reviewer = closedMr.getReviewers().get(0);
                if (isAvailable(reviewer.getUsername())) {
                    mr.setReviewers(List.of(reviewer));
                }

            }
        } else {
            //логика на подсчет %
        }
    }

    private int countLoad(Integer projectId, String username) throws GitLabApiException {
        int load = 0;
        var mrs = gitLabApi.getMergeRequestApi().getMergeRequests(projectId, Constants.MergeRequestState.OPENED);
        for (MergeRequest mr :
                mrs) {
            var reviewers = mr.getReviewers();
            for (Reviewer reviewer :
                    reviewers) {
                if (reviewer.getUsername().equals(username)) {
                    load++;
                }
            }
        }
        return load;
    }

    private Boolean isAvailable(String username) {
        var user = repo.findById(username);
        return user.filter(reviewers -> reviewers.getStatus() == Status.AVAILABLE).isPresent();
    }
}
