package ru.codereviewers.codereviewersforskblab;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class Reviewers {
    @Id
    private String username;
    @NonNull
    private Status status;
    private Integer load;
    public Reviewers() {
    }
}
enum Status {
    AVAILABLE,
    OVERBURDENED,
    NOT_AVAILABLE
}