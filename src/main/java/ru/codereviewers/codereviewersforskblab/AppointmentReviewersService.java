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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class AppointmentReviewersService {
    private final GitLabApi gitLabApi;
    private static final Logger log = LoggerFactory.getLogger(AppointmentReviewersService.class);
    private final ReviewersRepository repo;
    private final List<Project> projects;
    private Date time = new Date();

    public AppointmentReviewersService(GitLabApi gitLabApi, ReviewersRepository repo) throws GitLabApiException {
        this.gitLabApi = gitLabApi;
        this.projects = gitLabApi.getProjectApi().getProjects();
        this.repo = repo;
    }

    public void handleMR() throws GitLabApiException, InterruptedException {

        while (true) {
            for (Project project :
                    projects) {
                var id = project.getId();

                var mrs = gitLabApi.getMergeRequestApi().getMergeRequests(id, Constants.MergeRequestState.OPENED).stream().filter(t -> t.getCreatedAt().after(time)).collect(Collectors.toList());

                time = new Date();
                for (MergeRequest mr :
                        mrs) {
                    var task = mr.getTitle().substring(1, 10); //нужно нормальный парсер сделать
                    var closedMrs = gitLabApi.getMergeRequestApi().getMergeRequests(id, Constants.MergeRequestState.CLOSED).stream().filter(t -> t.getTitle().substring(1, 10).equals(task)).collect(Collectors.toList());
                    if (closedMrs.size() > 0) {
                        for (MergeRequest closedMr :
                                closedMrs) {

                            var reviewer = closedMr.getReviewers().get(0);
                            if (isAvailable(id, reviewer.getUsername())) {
                                mr.setReviewers(List.of(reviewer));
                            }

                        }
                    } else {
                        //логика на подсчет %
                    }
                }
            }
            TimeUnit.MINUTES.sleep(30);
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

    private Boolean isAvailable(Integer projectId, String username) throws GitLabApiException {
        if (countLoad(projectId, username) > 5) {
            return false;
        }
        return repo.findById(username).get().getStatus() == Status.AVAILABLE;
    }
}
