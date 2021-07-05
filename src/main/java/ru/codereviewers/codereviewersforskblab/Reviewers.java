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

    public Reviewers() {
    }
}
enum Status {
    AVAILABLE,
    NOT_AVAILABLE
}