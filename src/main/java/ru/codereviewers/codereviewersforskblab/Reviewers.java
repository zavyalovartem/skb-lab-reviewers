package ru.codereviewers.codereviewersforskblab;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Reviewers {
    @Id
    private String username; //??username??
    @NonNull
    private Status status;

    private Integer openReviews;
    public Reviewers() {
        openReviews = 0;
    }
}
enum Status {
    OVERBURDENED,
    AVAILABLE,
    NOT_AVAILABLE
}